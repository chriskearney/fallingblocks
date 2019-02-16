package com.comandante.game.board;

import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.GameBoardCoords.MoveDirection;
import com.comandante.game.board.logic.BasicPermaGroupManager;
import com.comandante.game.board.logic.GameBlockPairFactory;
import com.comandante.game.board.logic.GameBlockRenderer;
import com.comandante.game.board.logic.MagicGameBlockProcessor;
import com.comandante.game.board.logic.PermaGroupManager;
import com.comandante.game.textboard.TextBoard;
import com.google.common.collect.Lists;

import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.comandante.game.board.GameBlock.BorderType.*;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.BOTTOM_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.LEFT_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.RIGHT_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.TOP_OF;

public class GameBoard extends JComponent implements ActionListener, KeyListener {

    private final Timer timer;
    private final GameBoardData gameBoardData;
    private final GameBlockRenderer gameBlockRenderer;
    private final GameBlockPairFactory gameBlockPairFactory;
    private final MagicGameBlockProcessor magicGameBlockProcessor;
    private final TextBoard textBoard;
    private final PermaGroupManager permaGroupManager;
    private Integer score = 0;
    private Integer largestScore = 0;
    private boolean paused = false;


    public GameBoard(GameBoardData gameBoardData,
                     GameBlockRenderer gameBlockRenderer,
                     GameBlockPairFactory gameBlockPairFactory,
                     MagicGameBlockProcessor magicGameBlockProcessor,
                     TextBoard textBoard) {
        this.gameBoardData = gameBoardData;
        this.gameBlockRenderer = gameBlockRenderer;
        this.gameBlockPairFactory = gameBlockPairFactory;
        this.magicGameBlockProcessor = magicGameBlockProcessor;
        this.textBoard = textBoard;
        this.permaGroupManager = new BasicPermaGroupManager();
        this.timer = new Timer(400, this);
        this.timer.start();
        addKeyListener(this);
        setOpaque(false);
        alterScore(0);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (paused) {
            return;
        }
        if (gameBoardData.allBlocksResting()) {
            gameBoardData.insertNewBlockPair(gameBlockPairFactory.createBlockPair(this));
            textBoard.setNextBlockPair(gameBlockPairFactory.getNextPair());
            repaint();
        }
        processAllDrops();
        if (gameBoardData.allBlocksResting()) {
            calculatePermaGroups();
            boolean wasThereMagic;
            do {
                wasThereMagic = magicGameBlockProcessor.process(this);
                if (wasThereMagic) {
                    processAllDrops();
                }
            } while (wasThereMagic);
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        for (GameBoardCellEntity ce : gameBoardData.getCellsFromBottom()) {
            GameBlock.Type type = ce.getType();
            if (type.equals(GameBlock.Type.EMPTY)) {
                gameBlockRenderer.render(new TileSetGameBlockRenderer.BlockTypeBorder(type), ce, g);
            } else {
                Optional<GameBlock> gameBlock = ce.getGameBlock();
                gameBlockRenderer.render(gameBlock.get().getBlockTypeBorder(), ce, g);
            }
        }
    }

    public Dimension getPreferredSize() {
        return gameBoardData.getPreferredSize();
    }


    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_S:
                moveActiveBlockPair(MoveDirection.DOWN);
                break;
            case KeyEvent.VK_A:
                moveActiveBlockPair(MoveDirection.LEFT);
                break;
            case KeyEvent.VK_D:
                moveActiveBlockPair(MoveDirection.RIGHT);
                break;
            case KeyEvent.VK_W:
                rotate();
                break;
            case KeyEvent.VK_P:
                togglePause();
                break;
            case KeyEvent.VK_M:
                try {
                    ExportGameBoardState.export(getGameBoardData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private boolean processAllDrops() {
        boolean wasDrop = false;
        for (GameBoardCellEntity ce : gameBoardData.getCellsFromBottom()) {
            if (ce.isOccupied()) {
                if (!ce.isMarkedForDestruction() && gameBoardData.isCellEntityBelowIsEmptyOrNotBorder(ce)) {
                    moveCellEntityContents(MoveDirection.DOWN, ce, false);
                    wasDrop = true;
                } else {
                    ce.getGameBlock().get().setResting(true);
                }
            }
        }
        return wasDrop;
    }


    public Optional<GameBoardCellEntity> moveCellEntityContents(GameBoardCoords.MoveDirection moveDirection, GameBoardCellEntity gameBoardCellEntity, boolean repaint) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        Optional<GameBoardCellEntity> cellEntityOptional = gameBoardData.getCellEntity(new GameBoardCoords(i + moveDirection.getDirectionApplyCoords().i, j + moveDirection.getDirectionApplyCoords().j));
        if (!cellEntityOptional.isPresent() || cellEntityOptional.get().isOccupied()) {
            return Optional.empty();
        }
        gameBoardData.getCellEntities()[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j] = new GameBoardCellEntity(cellEntityOptional.get().getId(), cellEntityOptional.get().getGameBoardCoords(), gameBoardCellEntity.getGameBlock().orElse(null));
        gameBoardData.getCellEntities()[i][j] = new GameBoardCellEntity(gameBoardCellEntity.getId(), gameBoardCellEntity.getGameBoardCoords());
        if (repaint) {
            repaint();
        }
        return Optional.ofNullable(gameBoardData.getCellEntities()[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j]);
    }

    public void moveActiveBlockPair(GameBoardCoords.MoveDirection direction) {
        if (!gameBoardData.isBlockPairActive()) {
            return;
        }
        GameBlockPair gameBlockPair = gameBoardData.getBlockPairActive().get();

        Optional<GameBoardCellEntity> blockAEntityOpt = gameBlockPair.getBlockAEntity();
        Optional<GameBoardCellEntity> blockBEntityOpt = gameBlockPair.getBlockBEntity();

        if (!blockAEntityOpt.isPresent() || !blockBEntityOpt.isPresent()) {
            return;
        }

        GameBoardCellEntity blockAEntity = blockAEntityOpt.get();
        GameBoardCellEntity blockBEntity = blockBEntityOpt.get();

        if (gameBoardData.isSpaceAvailable(direction, blockAEntity) &&
                gameBoardData.isSpaceAvailable(direction, blockBEntity)) {

            Optional<GameBlockPair.BlockBOrientation> blockBOrientationOptional = gameBlockPair.getBlockBOrientation();
            if (!blockBOrientationOptional.isPresent()) {
                return;
            }

            GameBlockPair.BlockBOrientation blockBOrientation = blockBOrientationOptional.get();

            if ((blockBOrientation.equals(BOTTOM_OF) || blockBOrientation.equals(TOP_OF)) && (!direction.equals(GameBoardCoords.MoveDirection.DOWN))) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(BOTTOM_OF) && direction.equals(GameBoardCoords.MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }

            if (blockBOrientation.equals(TOP_OF) && direction.equals(GameBoardCoords.MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if ((blockBOrientation.equals(RIGHT_OF) || blockBOrientation.equals(LEFT_OF)) && direction.equals(GameBoardCoords.MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(LEFT_OF) && direction.equals(GameBoardCoords.MoveDirection.LEFT)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }

            if (blockBOrientation.equals(LEFT_OF) && direction.equals(GameBoardCoords.MoveDirection.RIGHT)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(RIGHT_OF) && direction.equals(GameBoardCoords.MoveDirection.LEFT)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(RIGHT_OF) && direction.equals(GameBoardCoords.MoveDirection.RIGHT)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }
        }
    }

    public void togglePause() {
        paused = !paused;
        textBoard.getTextBoardContents().setPaused(paused);
    }


    public List<List<GameBoardCellEntity>> getGroupsOfLikeBlocksFromRow(GameBoardCellEntity[] row) {
        List<List<GameBoardCellEntity>> listOfGroups = Lists.newArrayList();
        for (GameBoardCellEntity cellEntity : row) {
            if (!cellEntity.isOccupied()) {
                continue;
            }
            List<GameBoardCellEntity> group = Lists.newArrayList();
            GameBoardCellEntity nextEntityToCheck = cellEntity;
            boolean isLikeNeighbor = true;
            do {
                group.add(nextEntityToCheck);
                Optional<GameBoardCellEntity> cellEntityIfOccupiedSameType = gameBoardData.getCellEntityIfOccupiedSameType(MoveDirection.RIGHT, nextEntityToCheck);
                if (!cellEntityIfOccupiedSameType.isPresent()) {
                    isLikeNeighbor = false;
                } else {
                    nextEntityToCheck = cellEntityIfOccupiedSameType.get();
                }
                if (group.size() > 1){
                    listOfGroups.add(Lists.newArrayList(group));
                }
            } while (isLikeNeighbor);
//            if (!group.isEmpty() && group.size() > 1) {
//                listOfGroups.add(group);
//            }
        }
        return listOfGroups;
    }

    public void rotate() {
        if (!gameBoardData.isBlockPairActive()) {
            return;
        }
        GameBlockPair gameBlockPair = gameBoardData.getBlockPairActive().get();
        Optional<GameBlockPair.BlockBOrientation> blockBOrientation = gameBlockPair.getBlockBOrientation();
        if (!blockBOrientation.isPresent()) {
            System.out.print("Can not determine the orientation of block b to block a!");
            return;
        }
        GameBlockPair.BlockBOrientation orientation = blockBOrientation.get();
        Optional<GameBoardCellEntity> blockBEntity = gameBlockPair.getBlockBEntity();
        if (blockBEntity.isPresent()) {
            if (orientation.equals(BOTTOM_OF)) {
                moveCellEntityContents(GameBoardCoords.MoveDirection.UP, moveCellEntityContents(GameBoardCoords.MoveDirection.LEFT, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.LEFT_OF)) {
                moveCellEntityContents(GameBoardCoords.MoveDirection.RIGHT, moveCellEntityContents(GameBoardCoords.MoveDirection.UP, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.TOP_OF)) {
                moveCellEntityContents(GameBoardCoords.MoveDirection.DOWN, moveCellEntityContents(GameBoardCoords.MoveDirection.RIGHT, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.RIGHT_OF)) {
                moveCellEntityContents(GameBoardCoords.MoveDirection.LEFT, moveCellEntityContents(GameBoardCoords.MoveDirection.DOWN, blockBEntity.get(), true).get(), true);
            }
        }
    }

    public Map<GameBlock.Type, List<BlockGroup>> getBlockGroups() {
        List<BlockGroup> matchingGroups = Lists.newArrayList();
        Iterator<GameBoardCellEntity[]> iteratorOfRowsFromBottom = gameBoardData.getIteratorOfRowsFromBottom();
        while (iteratorOfRowsFromBottom.hasNext()) {
            GameBoardCellEntity[] next = iteratorOfRowsFromBottom.next();
            List<List<GameBoardCellEntity>> groupsOfLikeBlocksFromRow = getGroupsOfLikeBlocksFromRow(next);
            for (List<GameBoardCellEntity> rowGroup : groupsOfLikeBlocksFromRow) {
                BlockGroup blockGroup = new BlockGroup();
                boolean areMatchingRows = true;
                List<GameBoardCellEntity> processingRow = rowGroup;
                do {
                    blockGroup.addRow(processingRow.toArray(new GameBoardCellEntity[0]));
                    Optional<GameBoardCellEntity[]> matchingRowAboveIfExists = gameBoardData.getMatchingRowAboveIfExists(processingRow.toArray(new GameBoardCellEntity[0]));
                    areMatchingRows = matchingRowAboveIfExists.isPresent();
                    if (areMatchingRows) {
                        processingRow = Arrays.asList(matchingRowAboveIfExists.get());
                    }
                } while (areMatchingRows);
                if (blockGroup.size() > 1) {
                    matchingGroups.add(blockGroup);
                }
            }
        }
        Map<GameBlock.Type, List<BlockGroup>> groupsByType = new HashMap<>();
        for (BlockGroup group : matchingGroups) {
            GameBlock.Type type = group.getRawGroup()[0][0].getType();
            groupsByType.computeIfAbsent(type, k -> Lists.newArrayList());
            groupsByType.get(type).add(group);
        }
        return groupsByType;
    }

    public List<BlockGroup> getAllGroups() {
        List<BlockGroup> allGroups = Lists.newArrayList();
        Map<GameBlock.Type, List<BlockGroup>> blockGroups = getBlockGroups();
        for (Map.Entry<GameBlock.Type, List<BlockGroup>> entry : blockGroups.entrySet()) {
            allGroups.addAll(entry.getValue());
        }
        return allGroups;
    }

    public static class BlockGroup {

        private final UUID blockGroupId;

        public BlockGroup() {
            blockGroupId = UUID.randomUUID();
        }

        List<List<GameBoardCellEntity>> groupOfBlocks = Lists.newArrayList();

        public void addRow(GameBoardCellEntity[] row) {
            List<GameBoardCellEntity> gameBoardCellEntities = Arrays.asList(row);
            groupOfBlocks.add(gameBoardCellEntities);
        }

        public GameBoardCellEntity[][] getRawGroup() {
            GameBoardCellEntity[][] rawGroup = new GameBoardCellEntity[groupOfBlocks.size()][];
            GameBoardCellEntity[] blankArray = new GameBoardCellEntity[0];
            for (int i = 0; i < groupOfBlocks.size(); i++) {
                rawGroup[i] = groupOfBlocks.get(i).toArray(blankArray);
            }
            return rawGroup;
        }

        public int size() {
            return groupOfBlocks.size();
        }

        public int getX() {
            return groupOfBlocks.get(0).size();
        }

        public int getY() {
            return groupOfBlocks.size();
        }

        public int getArea() {
            return getX() * getY();
        }

        public List<GameBlock> getAllGameBlocks() {
            List<GameBlock> allBlocks = Lists.newArrayList();
            groupOfBlocks.forEach(gameBoardCellEntities -> {
                for (GameBoardCellEntity boardCellEntity : gameBoardCellEntities) {
                    if (boardCellEntity.getGameBlock().isPresent()) {
                        allBlocks.add(boardCellEntity.getGameBlock().get());
                    }
                }
            });
            return allBlocks;
        }

        public GameBlock getByXandY(int x, int y) {
            x = x - 1;
            y = y - 1;
            return groupOfBlocks.get(y).get(x).getGameBlock().get();
        }

        public UUID getBlockGroupId() {
            return blockGroupId;
        }
    }


    public void calculatePermaGroups() {
        permaGroupManager.reset();
        boolean finished = false;
        while (!finished) {
            BlockGroup largestOpenBlockGroup = null;
            List<BlockGroup> blockGroups = getAllGroups();
            for (BlockGroup blockGroup : blockGroups) {
                if (permaGroupManager.areAnyBlocksPartOfPermaGroup(blockGroup)) {
                    continue;
                }
                if (largestOpenBlockGroup == null || blockGroup.getArea() > largestOpenBlockGroup.getArea()) {
                    largestOpenBlockGroup = blockGroup;
                }
            }
            if (largestOpenBlockGroup != null && largestOpenBlockGroup.getArea() >= 4) {
                permaGroupManager.createPermagroup(largestOpenBlockGroup);
                continue;
            }
            finished = true;
        }


        List<BlockGroup> permaGroups = permaGroupManager.getPermaGroups();
        permaGroups.forEach(this::deriveBorderTypes);
    }


    private void deriveBorderTypes(BlockGroup blockGroup) {
        final int maxX = blockGroup.getX();
        final int maxY = blockGroup.getY();
        for (int x = 1; x <= maxX; x++) {
            for (int y = 1; y <= maxY; y++) {
                GameBlock.BorderType type = null;
                GameBlock byXandY = blockGroup.getByXandY(x, y);
                if (x < maxY && x < maxX) {
                    type = NO_BORDER;
                }
                if (x == 1 && y == maxY) {
                    type = TOP_LEFT;
                }
                if (x == 1 && y < maxY && y > 1) {
                    type = LEFT;
                }
                if (x == 1 && y == 1) {
                    type = BOTTOM_LEFT;
                }
                if (y == 1 && x < maxX && x > 1) {
                    type = BOTTOM;
                }
                if (x == maxX && y == 1) {
                    type = BOTTOM_RIGHT;
                }
                if (y < maxY && y > 1 && x == maxX) {
                    type = RIGHT;
                }
                if (x == maxX && y == maxY) {
                    type = TOP_RIGHT;
                }
                if (y == maxY && x < maxX && x > 1) {
                    type = TOP;
                }
                byXandY.setBorderType(Optional.ofNullable(type));
            }
        }


    }



    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    public TextBoard getTextBoard() {
        return textBoard;
    }

    public void alterScore(int amount) {
        if (amount > largestScore) {
            largestScore = amount;
        }
        score += amount;
        this.textBoard.getTextBoardContents().setScore(score);
        if (amount > 0) {
            this.textBoard.getTextBoardContents().addNewPointsToBattleLog(amount);
        }
    }

    public PermaGroupManager getPermaGroupManager() {
        return permaGroupManager;
    }

    public GameBoardData getGameBoardData() {
        return gameBoardData;
    }
}
