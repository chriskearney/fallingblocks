package com.comandante.game.board;

import com.google.common.collect.Lists;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Collectors;

import static com.comandante.game.board.GameBoard.NO_OP;
import static com.comandante.game.board.GameBoardUtil.subtractCoords;


public class GameBoardData {
    public final static int BLOCK_SIZE = 36;
    private GameBoardCellEntity[][] cellEntities;
    private Optional<GameBlockPair> blockPairActive = Optional.empty();
    private final ArrayBlockingQueue<InsertionQueueItem> insertionQueue = new ArrayBlockingQueue<>(500);
    private int initLengthX;
    private int initLengthY;

    public GameBoardData(int[][] init) {
        this.initLengthX = init.length;
        this.initLengthY = init[0].length;
        resetBoard();
    }

    public void resetBoard() {
        cellEntities = new GameBoardCellEntity[initLengthX][initLengthY];
        int invocationNumber = 0;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                invocationNumber++;
                setCellEntity(new GameBoardCoords(i, j), new GameBoardCellEntity(invocationNumber, new GameBoardCoords(i, j)));
            }
        }
        insertionQueue.clear();
    }

    public boolean
    processInsertionQueueAndDetectGameOver() {
        Optional<InsertionQueueItem> insertionQueueItemOptional = Optional.ofNullable(insertionQueue.poll());
        if (!insertionQueueItemOptional.isPresent()) {
            return false;
        }
        InsertionQueueItem insertionQueueItem = insertionQueueItemOptional.get();
        return insertRowOfBlocksAndDetectGameOver(insertionQueueItem.getGameBoardCellEntities().get(0));
    }

    public Optional<GameBoardCellEntity> getByPoint(Point point) {
        return getCellsFromBottom().stream()
                .filter(gameBoardCellEntity -> gameBoardCellEntity.getRectangle() != null && gameBoardCellEntity.getRectangle().contains(point))
                .findFirst();
    }

    public GameBoardCellEntity[][] getCellEntities() {
        return cellEntities;
    }

    public List<GameBoardCellEntity> getCellsFromBottom() {
        List<GameBoardCellEntity> gameBoardCellEntities = Lists.newArrayList();
        for (int i = cellEntities[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cellEntities.length; j++) {
                gameBoardCellEntities.add(cellEntities[j][i]);
            }
        }
        return gameBoardCellEntities;
    }

    public List<GameBoardCellEntity[]> getListOfRowsFromBottom() {
        List<GameBoardCellEntity[]> gameBoardCellEntityArrays = Lists.newArrayList();

        for (int i = cellEntities[0].length - 1; i >= 0; i--) {
            List<GameBoardCellEntity> gameBoardCellEntities = Lists.newArrayList();
            for (int j = 0; j < cellEntities.length; j++) {
                gameBoardCellEntities.add(cellEntities[j][i]);
            }
            gameBoardCellEntityArrays.add(gameBoardCellEntities.toArray(new GameBoardCellEntity[gameBoardCellEntities.size()]));
        }

        return gameBoardCellEntityArrays;
    }


    public Dimension getPreferredSize() {
        return new Dimension(cellEntities.length * BLOCK_SIZE, cellEntities[0].length * BLOCK_SIZE);
    }

    private void setCellEntity(GameBoardCoords gameBoardCoords, GameBoardCellEntity gameBoardCellEntity) {
        cellEntities[gameBoardCoords.i][gameBoardCoords.j] = gameBoardCellEntity;
    }

    public Optional<GameBoardCellEntity> getCellEntity(GameBoardCoords gameBoardCoords) {
        try {
            GameBoardCellEntity entity = cellEntities[gameBoardCoords.i][gameBoardCoords.j];
            return Optional.of(entity);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return Optional.empty();
    }

    public void insertNewBlockPair(GameBlockPair gameBlockPair) {
        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[cellEntities.length];

            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(gameBlockPair.getBlockB());
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));

            try {
                insertionQueue.put(new InsertionQueueItem(true, Lists.newArrayList(new GameBoardCellEntity[][]{gameBoardCellEntities})));
            } catch (Exception e) {
                //
            }
        }

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[cellEntities.length];

            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(gameBlockPair.getBlockA());
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));

            try {
                insertionQueue.put(new InsertionQueueItem(true, Lists.newArrayList(new GameBoardCellEntity[][]{gameBoardCellEntities})));
            } catch (Exception e) {
                //
            }
        }
        blockPairActive = Optional.of(gameBlockPair);
    }

    public void addRowsToInsertionQueue(List<GameBoardCellEntity []> row) {
        for (GameBoardCellEntity[] ge: row) {
            try {
                insertionQueue.put(new InsertionQueueItem(false, Collections.singletonList(ge)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean processAllDrops() {
        boolean wasDrop = false;
        for (GameBoardCellEntity ce : getCellsFromBottom()) {
            if (ce.isOccupied()) {
                if (!ce.getGameBlock().get().isMarkForDeletion() && isCellEntityBelowIsEmptyOrNotBorder(ce)) {
                    moveCellEntityContents(GameBoardCoords.MoveDirection.DOWN, ce, NO_OP);
                    wasDrop = true;
                }
            }
        }
        return wasDrop;
    }

    public void evaluateRestingStatus() {
        for (GameBoardCellEntity ce : getCellsFromBottom()) {
            if (ce.getGameBlock().isPresent()) {
                if (!isCellEntityBelowIsEmptyOrNotBorder(ce)) {
                    ce.getGameBlock().get().setResting(true);
                } else {
                    ce.getGameBlock().get().setResting(false);
                }
            }
        }
    }

    public boolean insertRowOfBlocksAndDetectGameOver(GameBoardCellEntity[] attackRow) {
        for (int i = 0; i < attackRow.length; i++) {
            GameBoardCellEntity existingGameCellEntity = cellEntities[i][0];
            if (existingGameCellEntity.isOccupied()) {
                //game over
                return true;
            } else {
                // set the existing cell to the incoming attack block
                if (attackRow[i].getGameBlock().isPresent()) {
                    cellEntities[i][0] = new GameBoardCellEntity(existingGameCellEntity.getId(), existingGameCellEntity.getGameBoardCoords(), attackRow[i].getGameBlock().get());
                }
            }
        }
        return false;
    }

    public boolean isCellEntityBelowIsEmptyOrNotBorder(GameBoardCellEntity gameBoardCellEntity) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        if (j == (cellEntities[0].length - 1)) {
            return false;
        }
        if (!cellEntities[i][j + 1].isOccupied()) {
            return true;
        }
        return false;
    }

    public Optional<GameBlockPair.BlockBOrientation> getBlockBOrientation() {
        if (!blockPairActive.isPresent()) {
            return Optional.empty();
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        Optional<GameBoardCoords> blockAGameBoardCoords = getCoords(gameBlockPair.getBlockA());
        Optional<GameBoardCoords> blockBGameBoardCoords = getCoords(gameBlockPair.getBlockB());
        if (blockAGameBoardCoords.isPresent() && blockBGameBoardCoords.isPresent()) {
            return GameBlockPair.BlockBOrientation.fromCoords(subtractCoords(blockAGameBoardCoords.get(), blockBGameBoardCoords.get()));
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getBlockBEntity() {
        if (!blockPairActive.isPresent()) {
            return Optional.empty();
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        if (getCoords(gameBlockPair.getBlockB()).isPresent()) {
            return getCellEntity(getCoords(gameBlockPair.getBlockB()).get());
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getBlockAEntity() {
        if (!blockPairActive.isPresent()) {
            return Optional.empty();
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        if (getCoords(gameBlockPair.getBlockA()).isPresent()) {
            return getCellEntity(getCoords(gameBlockPair.getBlockA()).get());
        }
        return Optional.empty();
    }

    public boolean isBlockPairActive() {
        return blockPairActive.isPresent();
    }

    public boolean isSpaceAvailable(GameBoardCoords.MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
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

        return !destinationEntity.isOccupied();
    }

    public Optional<GameBoardCellEntity[]> getMatchingRowAboveIfExists(GameBoardCellEntity[] oneRowGroupOfMatching) {
        java.util.List<GameBoardCellEntity> matchingNorthernNeighbors = new ArrayList<>();
        for (GameBoardCellEntity cellEntity : oneRowGroupOfMatching) {
            Optional<GameBoardCellEntity> northNeighbor = getCellEntityIfOccupiedSameType(GameBoardCoords.MoveDirection.UP, cellEntity);
            if (!northNeighbor.isPresent()) {
                return Optional.empty();
            }
            matchingNorthernNeighbors.add(northNeighbor.get());
        }
        return Optional.of(matchingNorthernNeighbors.toArray(new GameBoardCellEntity[0]));
    }

    public Optional<GameBoardCellEntity> getCellEntityIfOccupiedSameType(GameBoardCoords.MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
        Optional<GameBoardCellEntity> cellEntityIfOccupied = getCellEntityIfOccupied(direction, gameBoardCellEntity);
        if (cellEntityIfOccupied.isPresent() && cellEntityIfOccupied.get().getType().equals(gameBoardCellEntity.getType())) {
            return cellEntityIfOccupied;
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getCellEntityIfOccupied(GameBoardCoords.MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
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

    public Optional<GameBoardCoords> getCoords(GameBlock gameBlock) {
        Optional<GameBoardCoords> foundCoords = Optional.empty();
        for (GameBoardCellEntity ce : getCellsFromBottom()) {
            if (ce.getGameBlock().isPresent() && ce.getGameBlock().get().equals(gameBlock)) {
                foundCoords = Optional.of(ce.getGameBoardCoords());
            }
        }
        return foundCoords;
    }

    public List<GameBoardCellEntity> getOccupiedNeighborsOfType(GameBoardCellEntity gameBoardCellEntity, GameBlockType targetType) {
        List<GameBoardCellEntity> neighbors = new ArrayList<>();
        Optional<GameBoardCellEntity> upCell = getCellEntityIfOccupied(GameBoardCoords.MoveDirection.UP, gameBoardCellEntity);
        Optional<GameBoardCellEntity> downCell = getCellEntityIfOccupied(GameBoardCoords.MoveDirection.DOWN, gameBoardCellEntity);
        Optional<GameBoardCellEntity> leftCell = getCellEntityIfOccupied(GameBoardCoords.MoveDirection.LEFT, gameBoardCellEntity);
        Optional<GameBoardCellEntity> rightCell = getCellEntityIfOccupied(GameBoardCoords.MoveDirection.RIGHT, gameBoardCellEntity);
        upCell.ifPresent(neighbors::add);
        downCell.ifPresent(neighbors::add);
        leftCell.ifPresent(neighbors::add);
        rightCell.ifPresent(neighbors::add);
        return neighbors.stream().filter(ce -> {
            if (ce.getType().getRelated().isPresent()) {
                GameBlockType relatedType = ce.getType().getRelated().get();
                if (relatedType.equals(targetType)) {
                    return true;
                }
            }
            return ce.getType().equals(targetType);
        }).collect(Collectors.toList());
    }

    public boolean allBlocksResting() {

        List<GameBoardCellEntity> cellsFromBottom = getCellsFromBottom();
        for (GameBoardCellEntity gameBoardCellEntity : cellsFromBottom) {
            if (gameBoardCellEntity.isOccupied()) {
                GameBlock gameBlock = gameBoardCellEntity.getGameBlock().get();
                if (gameBlock.isMarkForDeletion()) {
                    return false;
                }
                if (!gameBlock.isResting()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setRow(int rowNumber, GameBoardCellEntity[] row) {
        for (int i = 0; i <= cellEntities.length - 1; i++) {
            GameBoardCellEntity gameBoardCellEntity = new GameBoardCellEntity(i, new GameBoardCoords(i, rowNumber), row[i].getGameBlock().orElse(null));
            cellEntities[i][rowNumber] = gameBoardCellEntity;
        }
    }

    public Optional<GameBoardCellEntity> moveCellEntityContents(GameBoardCoords.MoveDirection moveDirection, GameBoardCellEntity gameBoardCellEntity, Runnable postRun) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        Optional<GameBoardCellEntity> cellEntityOptional = getCellEntity(new GameBoardCoords(i + moveDirection.getDirectionApplyCoords().i, j + moveDirection.getDirectionApplyCoords().j));
        if (!cellEntityOptional.isPresent() || cellEntityOptional.get().isOccupied()) {
            return Optional.empty();
        }
        getCellEntities()[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j] = new GameBoardCellEntity(cellEntityOptional.get().getId(), cellEntityOptional.get().getGameBoardCoords(), gameBoardCellEntity.getGameBlock().orElse(null));
        getCellEntities()[i][j] = new GameBoardCellEntity(gameBoardCellEntity.getId(), gameBoardCellEntity.getGameBoardCoords());
        if (blockPairActive.isPresent()) {
            blockPairActive.get().getBlockA().setResting(false);
            blockPairActive.get().getBlockB().setResting(false);
        }
        postRun.run();
        return Optional.ofNullable(getCellEntities()[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j]);
    }

    public static class InsertionQueueItem {
        private final boolean blockPair;
        private final List<GameBoardCellEntity[]> gameBoardCellEntities;

        public InsertionQueueItem(boolean blockPair, List<GameBoardCellEntity[]> gameBoardCellEntities) {
            this.blockPair = blockPair;
            this.gameBoardCellEntities = gameBoardCellEntities;
        }

        public boolean isBlockPair() {
            return blockPair;
        }

        public List<GameBoardCellEntity[]> getGameBoardCellEntities() {
            return gameBoardCellEntities;
        }
    }
}
