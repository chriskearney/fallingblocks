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

import static com.comandante.game.board.GameBlock.BorderType.BOTTOM;
import static com.comandante.game.board.GameBlock.BorderType.BOTTOM_LEFT;
import static com.comandante.game.board.GameBlock.BorderType.BOTTOM_RIGHT;
import static com.comandante.game.board.GameBlock.BorderType.LEFT;
import static com.comandante.game.board.GameBlock.BorderType.NO_BORDER;
import static com.comandante.game.board.GameBlock.BorderType.RIGHT;
import static com.comandante.game.board.GameBlock.BorderType.TOP;
import static com.comandante.game.board.GameBlock.BorderType.TOP_LEFT;
import static com.comandante.game.board.GameBlock.BorderType.TOP_RIGHT;
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

    private static final Runnable NO_OP = () -> { };

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

    public PermaGroupManager getPermaGroupManager() {
        return permaGroupManager;
    }

    public GameBoardData getGameBoardData() {
        return gameBoardData;
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


    public Dimension getPreferredSize() {
        return gameBoardData.getPreferredSize();
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

    private Runnable rePainter() {
        return () -> {
            repaint();
        };
    }


    private boolean processAllDrops() {
        boolean wasDrop = false;
        for (GameBoardCellEntity ce : gameBoardData.getCellsFromBottom()) {
            if (ce.isOccupied()) {
                if (!ce.isMarkedForDestruction() && gameBoardData.isCellEntityBelowIsEmptyOrNotBorder(ce)) {
                    gameBoardData.moveCellEntityContents(MoveDirection.DOWN, ce, NO_OP);
                    wasDrop = true;
                } else {
                    ce.getGameBlock().get().setResting(true);
                }
            }
        }
        return wasDrop;
    }

    public void moveActiveBlockPair(GameBoardCoords.MoveDirection direction) {
        if (!gameBoardData.isBlockPairActive()) {
            return;
        }
        GameBlockPair gameBlockPair = gameBoardData.getBlockPairActive().get();

        Optional<GameBoardCellEntity> blockAEntityOpt = gameBoardData.getBlockAEntity();
        Optional<GameBoardCellEntity> blockBEntityOpt = gameBoardData.getBlockBEntity();

        if (!blockAEntityOpt.isPresent() || !blockBEntityOpt.isPresent()) {
            return;
        }

        GameBoardCellEntity blockAEntity = blockAEntityOpt.get();
        GameBoardCellEntity blockBEntity = blockBEntityOpt.get();

        if (gameBoardData.isSpaceAvailable(direction, blockAEntity) &&
                gameBoardData.isSpaceAvailable(direction, blockBEntity)) {

            Optional<GameBlockPair.BlockBOrientation> blockBOrientationOptional = gameBoardData.getBlockBOrientation();
            if (!blockBOrientationOptional.isPresent()) {
                return;
            }

            GameBlockPair.BlockBOrientation blockBOrientation = blockBOrientationOptional.get();

            if ((blockBOrientation.equals(BOTTOM_OF) || blockBOrientation.equals(TOP_OF)) && (!direction.equals(GameBoardCoords.MoveDirection.DOWN))) {
                gameBoardData.moveCellEntityContents(direction, blockAEntity, NO_OP);
                gameBoardData.moveCellEntityContents(direction, blockBEntity, rePainter());
            }

            if (blockBOrientation.equals(BOTTOM_OF) && direction.equals(GameBoardCoords.MoveDirection.DOWN)) {
                gameBoardData.moveCellEntityContents(direction, blockBEntity, NO_OP);
                gameBoardData.moveCellEntityContents(direction, blockAEntity, rePainter());
            }

            if (blockBOrientation.equals(TOP_OF) && direction.equals(GameBoardCoords.MoveDirection.DOWN)) {
                gameBoardData.moveCellEntityContents(direction, blockAEntity, NO_OP);
                gameBoardData.moveCellEntityContents(direction, blockBEntity, rePainter());
            }

            if ((blockBOrientation.equals(RIGHT_OF) || blockBOrientation.equals(LEFT_OF)) && direction.equals(GameBoardCoords.MoveDirection.DOWN)) {
                gameBoardData.moveCellEntityContents(direction, blockAEntity, NO_OP);
                gameBoardData.moveCellEntityContents(direction, blockBEntity, rePainter());
            }

            if (blockBOrientation.equals(LEFT_OF) && direction.equals(GameBoardCoords.MoveDirection.LEFT)) {
                gameBoardData.moveCellEntityContents(direction, blockBEntity, NO_OP);
                gameBoardData.moveCellEntityContents(direction, blockAEntity, rePainter());
            }

            if (blockBOrientation.equals(LEFT_OF) && direction.equals(GameBoardCoords.MoveDirection.RIGHT)) {
                gameBoardData.moveCellEntityContents(direction, blockAEntity, NO_OP);
                gameBoardData.moveCellEntityContents(direction, blockBEntity, rePainter());
            }

            if (blockBOrientation.equals(RIGHT_OF) && direction.equals(GameBoardCoords.MoveDirection.LEFT)) {
                gameBoardData.moveCellEntityContents(direction, blockAEntity, NO_OP);
                gameBoardData.moveCellEntityContents(direction, blockBEntity, rePainter());
            }

            if (blockBOrientation.equals(RIGHT_OF) && direction.equals(GameBoardCoords.MoveDirection.RIGHT)) {
                gameBoardData.moveCellEntityContents(direction, blockBEntity, NO_OP);
                gameBoardData.moveCellEntityContents(direction, blockAEntity, rePainter());
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
        }
        return listOfGroups;
    }

    public void rotate() {
        if (!gameBoardData.isBlockPairActive()) {
            return;
        }
        GameBlockPair gameBlockPair = gameBoardData.getBlockPairActive().get();
        Optional<GameBlockPair.BlockBOrientation> blockBOrientation = gameBoardData.getBlockBOrientation();
        if (!blockBOrientation.isPresent()) {
            System.out.print("Can not determine the orientation of block b to block a!");
            return;
        }
        GameBlockPair.BlockBOrientation orientation = blockBOrientation.get();
        Optional<GameBoardCellEntity> blockBEntity = gameBoardData.getBlockBEntity();
        if (blockBEntity.isPresent()) {
            if (orientation.equals(BOTTOM_OF)) {
                gameBoardData.moveCellEntityContents(GameBoardCoords.MoveDirection.UP, gameBoardData.moveCellEntityContents(GameBoardCoords.MoveDirection.LEFT, blockBEntity.get(), rePainter()).get(), rePainter());
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.LEFT_OF)) {
                gameBoardData.moveCellEntityContents(GameBoardCoords.MoveDirection.RIGHT, gameBoardData.moveCellEntityContents(GameBoardCoords.MoveDirection.UP, blockBEntity.get(), rePainter()).get(), rePainter());
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.TOP_OF)) {
                gameBoardData.moveCellEntityContents(GameBoardCoords.MoveDirection.DOWN, gameBoardData.moveCellEntityContents(GameBoardCoords.MoveDirection.RIGHT, blockBEntity.get(), rePainter()).get(), rePainter());
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.RIGHT_OF)) {
                gameBoardData.moveCellEntityContents(GameBoardCoords.MoveDirection.LEFT, gameBoardData.moveCellEntityContents(GameBoardCoords.MoveDirection.DOWN, blockBEntity.get(), rePainter()).get(), rePainter());
            }
        }
    }

    private Map<GameBlock.Type, List<BlockGroup>> buildGroupsByType() {
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

    private List<BlockGroup> buildAllGroups() {
        List<BlockGroup> allGroups = Lists.newArrayList();
        Map<GameBlock.Type, List<BlockGroup>> blockGroups = buildGroupsByType();
        for (Map.Entry<GameBlock.Type, List<BlockGroup>> entry : blockGroups.entrySet()) {
            allGroups.addAll(entry.getValue());
        }
        return allGroups;
    }

    public void calculatePermaGroups() {
        permaGroupManager.reset();
        boolean finished = false;
        while (!finished) {
            BlockGroup largestOpenBlockGroup = null;
            List<BlockGroup> blockGroups = buildAllGroups();
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

}
