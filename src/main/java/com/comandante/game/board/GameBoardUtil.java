package com.comandante.game.board;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GameBoardUtil {

    public static GameBoardCoords subtractCoords(GameBoardCoords a, GameBoardCoords b) {
        int i = a.i - b.i;
        int j = a.j - b.j;

        return new GameBoardCoords(i, j);
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

    public static Iterator<GameBoardCellEntity[]> getIteratorOfRowsFromBottom(GameBoardCellEntity[][] cells) {
        GameBoardCellEntity[][] mutabableCellEntriesCopy = Arrays.copyOf(cells, cells.length);
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
}
