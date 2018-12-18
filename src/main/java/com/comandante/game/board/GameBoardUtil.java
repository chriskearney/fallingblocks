package com.comandante.game.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
