package com.comandante.game.board;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class GameBoardData {
    public final static int BLOCK_SIZE = 32;

    private final GameBoardCellEntity[][] cellEntities;

    public void setBlockPairActive(Optional<GameBlockPair> blockPairActive) {
        this.blockPairActive = blockPairActive;
    }

    private Optional<GameBlockPair> blockPairActive = Optional.empty();

    public GameBoardData(int[][] init) {
        this.cellEntities = new GameBoardCellEntity[init.length][init[0].length];
        initializeBoard();
    }

    public GameBoardData(GameBoardCellEntity[][] cellEntities) {
        this.cellEntities = cellEntities;
    }

    public GameBoardCellEntity[][] getCellEntities() {
        return cellEntities;
    }

    public List<GameBoardCellEntity> getCellsFromBottom() {
        List<GameBoardCellEntity> gameBoardCellEntityList = new ArrayList<>();
        Iterator<GameBoardCellEntity[]> iteratorOfRowsFromBottom = getIteratorOfRowsFromBottom();
        while (iteratorOfRowsFromBottom.hasNext()) {
            Collections.addAll(gameBoardCellEntityList, iteratorOfRowsFromBottom.next());
        }
        return gameBoardCellEntityList;
    }

    public Iterator<GameBoardCellEntity[]> getIteratorOfRowsFromBottom() {
        GameBoardCellEntity[][] mutabableCellEntriesCopy = Arrays.copyOf(cellEntities, cellEntities.length);
        return new Iterator<GameBoardCellEntity[]>() {
            private List<GameBoardCellEntity> nextRow = new ArrayList<>();

            @Override
            public boolean hasNext() {
                nextRow = getAndRemoveLastRow(mutabableCellEntriesCopy);
                return !nextRow.isEmpty();
            }

            @Override
            public GameBoardCellEntity[] next() {
                if (nextRow.isEmpty()) {
                    throw new RuntimeException("Need to call hasNext first!");
                }
                return nextRow.toArray(new GameBoardCellEntity[0]);
            }
        };
    }

    private List<GameBoardCellEntity> getAndRemoveLastRow(GameBoardCellEntity[][] arrays) {
        List<GameBoardCellEntity> cellEntitiesArray = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            GameBoardCellEntity[] row = arrays[i];
            if (row.length - 1 < 0) {
                continue;
            }
            GameBoardCellEntity lastElement = row[row.length - 1];
            cellEntitiesArray.add(lastElement);
            GameBoardCellEntity[] cellEntiesWithRemoved = Arrays.copyOf(row, row.length - 1);
            arrays[i] = cellEntiesWithRemoved;
        }
        return cellEntitiesArray;
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
        int insertNewBlockCell = cellEntities.length / 2;
        cellEntities[insertNewBlockCell][0] = new GameBoardCellEntity(cellEntities[insertNewBlockCell][0].getId(), cellEntities[insertNewBlockCell][0].getGameBoardCoords(), gameBlockPair.getBlockA());
        cellEntities[insertNewBlockCell][1] = new GameBoardCellEntity(cellEntities[insertNewBlockCell][1].getId(), cellEntities[insertNewBlockCell][1].getGameBoardCoords(), gameBlockPair.getBlockB());
        blockPairActive = Optional.of(gameBlockPair);
    }

    public boolean isCellEntityBelowIsEmptyOrNotBorder(GameBoardCellEntity gameBoardCellEntity) {
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


    public List<GameBoardCellEntity> getCellsFromBottom(GameBoardCellEntity[][] cellEntities) {
        List<GameBoardCellEntity> gameBoardCellEntityList = new ArrayList<>();
        Iterator<GameBoardCellEntity[]> iteratorOfRowsFromBottom = getIteratorOfRowsFromBottom();
        while (iteratorOfRowsFromBottom.hasNext()) {
            Collections.addAll(gameBoardCellEntityList, iteratorOfRowsFromBottom.next());
        }
        return gameBoardCellEntityList;
    }

    public boolean isBlockPairActive() {
        return blockPairActive.isPresent() && (!blockPairActive.filter(gameBlockPair -> gameBlockPair.getBlockA().isResting() || gameBlockPair.getBlockB().isResting()).isPresent());
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

        if (!destinationEntity.isOccupied()) {
            return true;
        }
        return false;
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



    public List<GameBoardCellEntity> getOccupiedNeighborsOfType(GameBoardCellEntity gameBoardCellEntity, GameBlock.Type targetType) {
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
                GameBlock.Type relatedType = ce.getType().getRelated().get();
                if (relatedType.equals(targetType)) {
                    return true;
                }
            }
            return ce.getType().equals(targetType);
        }).collect(Collectors.toList());
    }

    public boolean allBlocksResting() {
        boolean allResting = true;
        for (GameBoardCellEntity ce : getCellsFromBottom()) {
            if (ce.isOccupied() && !ce.getGameBlock().get().isResting()) {
                allResting = false;
            }
        }
        return allResting;
    }

    public void setRow(int rowNumber, GameBoardCellEntity[] row) {
        for (int i = 0; i <= cellEntities.length - 1; i++) {
            GameBoardCellEntity gameBoardCellEntity = new GameBoardCellEntity(i, new GameBoardCoords(i, rowNumber), row[i].getGameBlock().orElse(null));
            cellEntities[i][rowNumber] = gameBoardCellEntity;
        }
    }

    public Optional<GameBlockPair> getBlockPairActive() {
        return blockPairActive;
    }

}
