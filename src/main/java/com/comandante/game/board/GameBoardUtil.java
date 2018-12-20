package com.comandante.game.board;

import java.util.*;
import java.util.stream.Collectors;

public class GameBoardUtil {

    public static Iterator<GameBoardCellEntity[]> getIteratorOfRowsFromBottom(GameBoardCellEntity[][] cellEntities) {
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

    public static List<GameBoardCellEntity> getCellsFromBottom(GameBoardCellEntity[][] cellEntities) {
        List<GameBoardCellEntity> gameBoardCellEntityList = new ArrayList<>();
        Iterator<GameBoardCellEntity[]> iteratorOfRowsFromBottom = getIteratorOfRowsFromBottom(cellEntities);
        while (iteratorOfRowsFromBottom.hasNext()) {
            Collections.addAll(gameBoardCellEntityList, iteratorOfRowsFromBottom.next());
        }
        return gameBoardCellEntityList;
    }

    private static List<GameBoardCellEntity> getAndRemoveLastRow(GameBoardCellEntity[][] arrays) {
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


    public static List<GameBoardCellEntity> getOccupiedNeighborsOfType(GameBoard gameBoard, GameBoardCellEntity gameBoardCellEntity, GameBlock.Type targetType) {
        List<GameBoardCellEntity> neighbors = new ArrayList<>();
        Optional<GameBoardCellEntity> upCell = gameBoard.getCellEntityIfOccupied(GameBoardCoords.MoveDirection.UP, gameBoardCellEntity);
        Optional<GameBoardCellEntity> downCell = gameBoard.getCellEntityIfOccupied(GameBoardCoords.MoveDirection.DOWN, gameBoardCellEntity);
        Optional<GameBoardCellEntity> leftCell = gameBoard.getCellEntityIfOccupied(GameBoardCoords.MoveDirection.LEFT, gameBoardCellEntity);
        Optional<GameBoardCellEntity> rightCell = gameBoard.getCellEntityIfOccupied(GameBoardCoords.MoveDirection.RIGHT, gameBoardCellEntity);
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

    public static GameBoardCoords subtractCoords(GameBoardCoords a, GameBoardCoords b) {
        int i = a.i - b.i;
        int j = a.j - b.j;

        return new GameBoardCoords(i, j);
    }

    public static GameBoardCoords addCords(GameBoardCoords a, GameBoardCoords b) {
        int i = a.i + b.i;
        int j = a.j + b.j;

        return new GameBoardCoords(i, j);
    }
}
