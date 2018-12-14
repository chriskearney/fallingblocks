package com.comadante;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GameBoardUtil {

    public static Iterator<CellEntity[]> getIteratorOfRowsFromBottom(CellEntity[][] cellEntities) {
        CellEntity[][] mutabableCellEntriesCopy = Arrays.copyOf(cellEntities, cellEntities.length);
        return new Iterator<CellEntity[]>() {
            private List<CellEntity> nextRow = new ArrayList<>();

            @Override
            public boolean hasNext() {
                nextRow = getAndRemoveLastRow(mutabableCellEntriesCopy);
                return !nextRow.isEmpty();
            }

            @Override
            public CellEntity[] next() {
                if (nextRow.isEmpty()) {
                    throw new RuntimeException("Need to call hasNext first!");
                }
                return nextRow.toArray(new CellEntity[0]);
            }
        };
    }

    private static List<CellEntity> getAndRemoveLastRow(CellEntity[][] arrays) {
        List<CellEntity> cellEntitiesArray = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            CellEntity[] row = arrays[i];
            if (row.length - 1 < 0) {
                continue;
            }
            CellEntity lastElement = row[row.length - 1];
            cellEntitiesArray.add(lastElement);
            CellEntity[] cellEntiesWithRemoved = Arrays.copyOf(row, row.length - 1);
            arrays[i] = cellEntiesWithRemoved;
        }
        return cellEntitiesArray;
    }

}
