package com.comandante.game.board;

import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.GameBoardCoords.MoveDirection;
import com.comandante.game.board.logic.BasicPermaGroupManager;
import com.comandante.game.board.logic.GameBlockPairFactory;
import com.comandante.game.board.logic.GameBlockRenderer;
import com.comandante.game.board.logic.MagicGameBlockProcessor;
import com.comandante.game.board.logic.PermaGroupManager;
import com.comandante.game.textboard.TextBoard;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

import static com.comandante.game.board.GameBlock.BorderType.*;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.BOTTOM_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.LEFT_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.RIGHT_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.TOP_OF;

public class GameBoard extends JComponent implements ActionListener, KeyListener {

    public final static int BLOCK_SIZE = 32;

    private final Timer timer;
    private final GameBoardCellEntity[][] cellEntities;
    private final GameBlockRenderer gameBlockRenderer;
    private final GameBlockPairFactory gameBlockPairFactory;
    private final MagicGameBlockProcessor magicGameBlockProcessor;
    private final TextBoard textBoard;
    private final PermaGroupManager permaGroupManager;
    private Integer score = 0;
    private Integer largestScore = 0;
    private boolean paused = false;

    //Some State
    private Optional<GameBlockPair> blockPairActive;

    public GameBoard(int[][] a, GameBlockRenderer gameBlockRenderer, GameBlockPairFactory gameBlockPairFactory, MagicGameBlockProcessor magicGameBlockProcessor, TextBoard textBoard) {
        this.cellEntities = new GameBoardCellEntity[a.length][a[0].length];
        this.gameBlockRenderer = gameBlockRenderer;
        this.gameBlockPairFactory = gameBlockPairFactory;
        this.magicGameBlockProcessor = magicGameBlockProcessor;
        this.textBoard = textBoard;
        this.permaGroupManager = new BasicPermaGroupManager();
        initializeBoard();
        timer = new Timer(400, this);
        timer.start();
        addKeyListener(this);
        setOpaque(false);
        alterScore(0);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (paused) {
            return;
        }
        if (allBlocksResting()) {
            insertNewBlockPair(gameBlockPairFactory.createBlockPair(this));
            textBoard.setNextBlockPair(gameBlockPairFactory.getNextPair());
            repaint();
        }
        processAllDrops();
        if (allBlocksResting()) {
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
        for (GameBoardCellEntity ce : GameBoardUtil.getCellsFromBottom(cellEntities)) {
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
        return new Dimension(cellEntities.length * BLOCK_SIZE, cellEntities[0].length * BLOCK_SIZE);
    }

    private void initializeBoard() {
        int invocationNumber = 0;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                invocationNumber++;
                setCellEntity(new GameBoardCoords(i, j), new GameBoardCellEntity(invocationNumber, new GameBoardCoords(i, j)));
            }
        }
    }

    public Optional<GameBoardCellEntity> getCellEntity(GameBoardCoords gameBoardCoords) {
        try {
            GameBoardCellEntity entity = cellEntities[gameBoardCoords.i][gameBoardCoords.j];
            return Optional.of(entity);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return Optional.empty();
    }

    private void setCellEntity(GameBoardCoords gameBoardCoords, GameBoardCellEntity gameBoardCellEntity) {
        cellEntities[gameBoardCoords.i][gameBoardCoords.j] = gameBoardCellEntity;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        System.out.println("KeyCode received: " + keyCode);
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
        }
    }

    private boolean processAllDrops() {
        boolean wasDrop = false;
        for (GameBoardCellEntity ce : GameBoardUtil.getCellsFromBottom(cellEntities)) {
            if (ce.isOccupied()) {
                if (!ce.isMarkedForDestruction() && isCellEntityBelowIsEmptyOrNotBorder(ce)) {
                    moveCellEntityContents(MoveDirection.DOWN, ce, false);
                    wasDrop = true;
                } else {
                    ce.getGameBlock().get().setResting(true);
                }
            }
        }
        return wasDrop;
    }

    public void togglePause() {
        paused = !paused;
        textBoard.getTextBoardContents().setPaused(paused);
    }

    private void insertNewBlockPair(GameBlockPair gameBlockPair) {
        int insertNewBlockCell = cellEntities.length / 2;
        cellEntities[insertNewBlockCell][0] = new GameBoardCellEntity(cellEntities[insertNewBlockCell][0].getId(), cellEntities[insertNewBlockCell][0].getGameBoardCoords(), gameBlockPair.getBlockA());
        cellEntities[insertNewBlockCell][1] = new GameBoardCellEntity(cellEntities[insertNewBlockCell][1].getId(), cellEntities[insertNewBlockCell][1].getGameBoardCoords(), gameBlockPair.getBlockB());
        blockPairActive = Optional.of(gameBlockPair);
    }

    private boolean isCellEntityBelowIsEmptyOrNotBorder(GameBoardCellEntity gameBoardCellEntity) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        // Because I will forget:
        // I am detecting if a cellentity that contains a block that belongs to an active falling blockpair.
        // because the following if statement means, we have reached the bottom border
        // i will mark the blockpair as inactive / or optional.empty
        if (j == (cellEntities[0].length - 1)) {
            if (blockPairActive.isPresent() && gameBoardCellEntity.getGameBlock().isPresent()) {
                if (gameBoardCellEntity.getGameBlock().get().equals(blockPairActive.get().getBlockAEntity()) || gameBoardCellEntity.getGameBlock().get().equals(blockPairActive.get().getBlockBEntity())) {
                    blockPairActive = Optional.empty();
                }
            }
            return false;
        }
        if (!cellEntities[i][j + 1].isOccupied()) {
            return true;
        }
        return false;
    }

    private Optional<GameBoardCellEntity> moveCellEntityContents(MoveDirection moveDirection, GameBoardCellEntity gameBoardCellEntity, boolean repaint) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        Optional<GameBoardCellEntity> cellEntityOptional = getCellEntity(new GameBoardCoords(i + moveDirection.getDirectionApplyCoords().i, j + moveDirection.getDirectionApplyCoords().j));
        if (!cellEntityOptional.isPresent() || cellEntityOptional.get().isOccupied()) {
            return Optional.empty();
        }
        cellEntities[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j] = new GameBoardCellEntity(cellEntityOptional.get().getId(), cellEntityOptional.get().getGameBoardCoords(), gameBoardCellEntity.getGameBlock().orElse(null));
        cellEntities[i][j] = new GameBoardCellEntity(gameBoardCellEntity.getId(), gameBoardCellEntity.getGameBoardCoords());
        if (repaint) {
            repaint();
        }
        return Optional.ofNullable(cellEntities[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j]);
    }


    private boolean isBlockPairActive() {
        return blockPairActive.isPresent() && (!blockPairActive.filter(gameBlockPair -> gameBlockPair.getBlockA().isResting() || gameBlockPair.getBlockB().isResting()).isPresent());
    }

    private void moveActiveBlockPair(GameBoardCoords.MoveDirection direction) {
        if (!isBlockPairActive()) {
            return;
        }
        GameBlockPair gameBlockPair = blockPairActive.get();

        Optional<GameBoardCellEntity> blockAEntityOpt = gameBlockPair.getBlockAEntity();
        Optional<GameBoardCellEntity> blockBEntityOpt = gameBlockPair.getBlockBEntity();

        if (!blockAEntityOpt.isPresent() || !blockBEntityOpt.isPresent()) {
            return;
        }

        GameBoardCellEntity blockAEntity = blockAEntityOpt.get();
        GameBoardCellEntity blockBEntity = blockBEntityOpt.get();

        if (isSpaceAvailable(direction, blockAEntity) &&
                isSpaceAvailable(direction, blockBEntity)) {

            Optional<GameBlockPair.BlockBOrientation> blockBOrientationOptional = gameBlockPair.getBlockBOrientation();
            if (!blockBOrientationOptional.isPresent()) {
                return;
            }

            GameBlockPair.BlockBOrientation blockBOrientation = blockBOrientationOptional.get();

            if ((blockBOrientation.equals(BOTTOM_OF) || blockBOrientation.equals(TOP_OF)) && (!direction.equals(MoveDirection.DOWN))) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(BOTTOM_OF) && direction.equals(MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }

            if (blockBOrientation.equals(TOP_OF) && direction.equals(MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if ((blockBOrientation.equals(RIGHT_OF) || blockBOrientation.equals(LEFT_OF)) && direction.equals(MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(LEFT_OF) && direction.equals(MoveDirection.LEFT)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }

            if (blockBOrientation.equals(LEFT_OF) && direction.equals(MoveDirection.RIGHT)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(RIGHT_OF) && direction.equals(MoveDirection.LEFT)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(RIGHT_OF) && direction.equals(MoveDirection.RIGHT)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }
        }
    }

    private boolean isSpaceAvailable(MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
        Optional<GameBoardCellEntity> destinationEntityOptional = getCellEntity(new GameBoardCoords(gameBoardCellEntity.getGameBoardCoords().i + direction.getDirectionApplyCoords().i, gameBoardCellEntity.getGameBoardCoords().j + direction.getDirectionApplyCoords().j));
        if (!destinationEntityOptional.isPresent()) {
            return false;
        }
        GameBoardCellEntity destinationEntity = destinationEntityOptional.get();
        if (destinationEntity.getGameBlock().isPresent()) {
            GameBlock gameBlock = destinationEntity.getGameBlock().get();
            if (blockPairActive.isPresent()) {
                if (blockPairActive.get().getBlockB().equals(gameBlock) ||
                        blockPairActive.get().getBlockA().equals(gameBlock)) {
                    return true;
                }
            }
        }

        if (!destinationEntity.isOccupied()) {
            return true;
        }
        return false;
    }

    public Optional<GameBoardCellEntity> getCellEntityIfOccupiedSameType(MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
        Optional<GameBoardCellEntity> cellEntityIfOccupied = getCellEntityIfOccupied(direction, gameBoardCellEntity);
        if (cellEntityIfOccupied.isPresent() && cellEntityIfOccupied.get().getType().equals(gameBoardCellEntity.getType())) {
            return cellEntityIfOccupied;
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getCellEntityIfOccupied(MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
        Optional<GameBoardCellEntity> destinationEntityOptional = getCellEntity(new GameBoardCoords(gameBoardCellEntity.getGameBoardCoords().i + direction.getDirectionApplyCoords().i, gameBoardCellEntity.getGameBoardCoords().j + direction.getDirectionApplyCoords().j));
        if (!destinationEntityOptional.isPresent()) {
            return Optional.empty();
        }
        GameBoardCellEntity destinationEntity = destinationEntityOptional.get();
        if (destinationEntity.isOccupied()) {
            return Optional.of(destinationEntity);
        }
        return Optional.empty();
    }

    private void rotate() {
        if (!isBlockPairActive()) {
            return;
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        Optional<GameBlockPair.BlockBOrientation> blockBOrientation = gameBlockPair.getBlockBOrientation();
        if (!blockBOrientation.isPresent()) {
            System.out.printf("Can not determine the orientation of block b to block a!");
            return;
        }
        GameBlockPair.BlockBOrientation orientation = blockBOrientation.get();
        Optional<GameBoardCellEntity> blockBEntity = gameBlockPair.getBlockBEntity();
        if (blockBEntity.isPresent()) {
            if (orientation.equals(BOTTOM_OF)) {
                moveCellEntityContents(MoveDirection.UP, moveCellEntityContents(MoveDirection.LEFT, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.LEFT_OF)) {
                moveCellEntityContents(MoveDirection.RIGHT, moveCellEntityContents(MoveDirection.UP, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.TOP_OF)) {
                moveCellEntityContents(MoveDirection.DOWN, moveCellEntityContents(MoveDirection.RIGHT, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.RIGHT_OF)) {
                moveCellEntityContents(MoveDirection.LEFT, moveCellEntityContents(MoveDirection.DOWN, blockBEntity.get(), true).get(), true);
            }
        }
    }

    public void setRow(int rowNumber, GameBoardCellEntity[] row) {
        for (int i = 0; i <= cellEntities.length - 1; i++) {
            GameBoardCellEntity gameBoardCellEntity = new GameBoardCellEntity(i, new GameBoardCoords(i, rowNumber), row[i].getGameBlock().orElse(null));
            cellEntities[i][rowNumber] = gameBoardCellEntity;
        }
    }

    public java.util.List<java.util.List<GameBoardCellEntity>> getGroupsOfLikeBlocksFromRow(GameBoardCellEntity[] row) {
        java.util.List<java.util.List<GameBoardCellEntity>> listOfGroups = new ArrayList<>();
        for (GameBoardCellEntity cellEntity : row) {
            if (!cellEntity.isOccupied()) {
                continue;
            }
            java.util.List<GameBoardCellEntity> group = new ArrayList<>();
            GameBoardCellEntity nextEntityToCheck = cellEntity;
            boolean isLikeNeighbor = true;
            do {
                group.add(nextEntityToCheck);
                Optional<GameBoardCellEntity> cellEntityIfOccupiedSameType = getCellEntityIfOccupiedSameType(MoveDirection.RIGHT, nextEntityToCheck);
                if (!cellEntityIfOccupiedSameType.isPresent()) {
                    isLikeNeighbor = false;
                } else {
                    nextEntityToCheck = cellEntityIfOccupiedSameType.get();
                }
            } while (isLikeNeighbor);
            if (!group.isEmpty() && group.size() > 1) {
                listOfGroups.add(group);
            }
        }
        return listOfGroups;
    }

    public Optional<GameBoardCellEntity[]> getMatchingRowAboveIfExists(GameBoardCellEntity[] oneRowGroupOfMatching) {
        java.util.List<GameBoardCellEntity> matchingNorthernNeighbors = new ArrayList<>();
        for (GameBoardCellEntity cellEntity : oneRowGroupOfMatching) {
            Optional<GameBoardCellEntity> northNeighbor = getCellEntityIfOccupiedSameType(MoveDirection.UP, cellEntity);
            if (!northNeighbor.isPresent()) {
                return Optional.empty();
            }
            matchingNorthernNeighbors.add(northNeighbor.get());
        }
        return Optional.of(matchingNorthernNeighbors.toArray(new GameBoardCellEntity[0]));
    }

    public Map<GameBlock.Type, java.util.List<BlockGroup>> getBlockGroups() {
        java.util.List<BlockGroup> matchingGroups = new ArrayList<>();
        Iterator<GameBoardCellEntity[]> iteratorOfRowsFromBottom = GameBoardUtil.getIteratorOfRowsFromBottom(getCellEntities());
        while (iteratorOfRowsFromBottom.hasNext()) {
            GameBoardCellEntity[] next = iteratorOfRowsFromBottom.next();
            java.util.List<java.util.List<GameBoardCellEntity>> groupsOfLikeBlocksFromRow = getGroupsOfLikeBlocksFromRow(next);
            for (java.util.List<GameBoardCellEntity> rowGroup : groupsOfLikeBlocksFromRow) {
                BlockGroup blockGroup = new BlockGroup();
                boolean areMatchingRows = true;
                java.util.List<GameBoardCellEntity> processingRow = rowGroup;
                do {
                    blockGroup.addRow(processingRow.toArray(new GameBoardCellEntity[0]));
                    Optional<GameBoardCellEntity[]> matchingRowAboveIfExists = getMatchingRowAboveIfExists(processingRow.toArray(new GameBoardCellEntity[0]));
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
        Map<GameBlock.Type, java.util.List<BlockGroup>> groupsByType = new HashMap<>();
        for (BlockGroup group : matchingGroups) {
            GameBlock.Type type = group.getRawGroup()[0][0].getType();
            groupsByType.computeIfAbsent(type, k -> new ArrayList<>());
            groupsByType.get(type).add(group);
        }
        return groupsByType;
    }

    public java.util.List<BlockGroup> getAllGroups() {
        java.util.List<BlockGroup> allGroups = new ArrayList<>();
        Map<GameBlock.Type, java.util.List<BlockGroup>> blockGroups = getBlockGroups();
        for (Map.Entry<GameBlock.Type, java.util.List<BlockGroup>> entry : blockGroups.entrySet()) {
            allGroups.addAll(entry.getValue());
        }
        return allGroups;
    }

    public static class BlockGroup {
        java.util.List<java.util.List<GameBoardCellEntity>> groupOfBlocks = new ArrayList<>();

        public void addRow(GameBoardCellEntity[] row) {
            List<GameBoardCellEntity> gameBoardCellEntities = Arrays.asList(row);
            //Collections.reverse(gameBoardCellEntities);
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

        public java.util.List<GameBlock> getAllGameBlocks() {
            java.util.List<GameBlock> allBlocks = new ArrayList<>();
            groupOfBlocks.forEach(new Consumer<java.util.List<GameBoardCellEntity>>() {
                @Override
                public void accept(java.util.List<GameBoardCellEntity> gameBoardCellEntities) {
                    for (GameBoardCellEntity boardCellEntity : gameBoardCellEntities) {
                        if (boardCellEntity.getGameBlock().isPresent()) {
                            allBlocks.add(boardCellEntity.getGameBlock().get());
                        }
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
    }


    public void calculatePermaGroups() {
        permaGroupManager.reset();
        boolean finished = false;
        while (!finished) {
            BlockGroup largestOpenBlockGroup = null;
            java.util.List<BlockGroup> blockGroups = getAllGroups();
            for (BlockGroup blockGroup : blockGroups) {
                if (permaGroupManager.areAnyBlocksPartOfPermaGroup(blockGroup)) {
                    continue;
                }
                if (largestOpenBlockGroup == null || blockGroup.getArea() > largestOpenBlockGroup.getArea()) {
                    largestOpenBlockGroup = blockGroup;
                }
            }
            if (largestOpenBlockGroup != null) {
                permaGroupManager.createPermagroup(largestOpenBlockGroup);
                continue;
            }
            finished = true;
        }


        java.util.List<BlockGroup> permaGroups = permaGroupManager.getPermaGroups();
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

    public Optional<GameBoardCoords> getCoords(GameBlock gameBlock) {
        Optional<GameBoardCoords> foundCoords = Optional.empty();
        for (GameBoardCellEntity ce : GameBoardUtil.getCellsFromBottom(cellEntities)) {
            if (ce.getGameBlock().isPresent() && ce.getGameBlock().get().equals(gameBlock)) {
                foundCoords = Optional.of(ce.getGameBoardCoords());
            }
        }
        return foundCoords;
    }

    public boolean allBlocksResting() {
        boolean allResting = true;
        for (GameBoardCellEntity ce : GameBoardUtil.getCellsFromBottom(cellEntities)) {
            if (ce.isOccupied() && !ce.getGameBlock().get().isResting()) {
                allResting = false;
            }
        }
        return allResting;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    public GameBoardCellEntity[][] getCellEntities() {
        return cellEntities;
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
}
