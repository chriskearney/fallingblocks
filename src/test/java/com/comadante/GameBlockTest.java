package com.comadante;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class GameBlockTest {

    @Test
    public void testUnderstandingMultiDimensionalArrays() throws Exception {
        int cellNumber = 0;
        String[][] arrays = new String[10][20];
        for (int i = 0; i < arrays.length; i++) {
            for (int j = 0; j < arrays[0].length; j++) {
                arrays[i][j] = Integer.toString(cellNumber);
                cellNumber++;
            }
        }
        System.out.println(to2DString(arrays));
        Iterator<String[]> iteratorOfRowsFromBottom = getIteratorOfRowsFromBottom(arrays);
        while (iteratorOfRowsFromBottom.hasNext()) {
            String[] next = iteratorOfRowsFromBottom.next();
            String s = Arrays.toString(next);
            System.out.println(s);
        }
        //printArray(arrays);
    }

    public static <T> String to2DString(T[][] x) {
        final String vSep = "\n";
        final String hSep = ", ";
        final StringBuilder sb = new StringBuilder();

        if(x != null)
            for(int i = 0; i < x.length; i++) {
                final T[] a = x[i];
                if(i > 0) {
                    sb.append(vSep);
                }
                if(a != null)
                    for(int j = 0; j < a.length; j++) {
                        final T b = a[j];
                        if(j > 0) {
                            sb.append(hSep);
                        }
                        sb.append(b);
                    }
            }
        return sb.toString();
    }

    public Iterator<String[]> getIteratorOfRowsFromBottom(String[][] strings) {
        return new Iterator<String[]>() {
            private List<String> nextRow = new ArrayList<>();

            @Override
            public boolean hasNext() {
                nextRow = getAndRemoveLastRow(strings);
                return !nextRow.isEmpty();
            }

            @Override
            public String[] next() {
                if (nextRow.isEmpty()) {
                    throw new RuntimeException("Need to call hasNext first!");
                }
                return nextRow.toArray(new String[0]);
            }
        };

    }

    public List<String> getAndRemoveLastRow(String[][] arrays) {
        List<String> stringArray = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            String[] row = arrays[i];
            if (row.length - 1 < 0) {
                continue;
            }
            String lastElement = row[row.length -1];
            stringArray.add(lastElement);
            String[] strings1 = Arrays.copyOf(row, row.length - 1);
            arrays[i] = strings1;
        }
        return stringArray;
    }


}