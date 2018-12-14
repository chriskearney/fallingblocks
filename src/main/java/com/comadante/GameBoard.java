package com.comadante;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static java.awt.Color.*;
import static java.awt.Color.cyan;

public class GameBoard extends JComponent implements ActionListener {

    private final static int BOARD_SIZE = 20;
    private final static Color[] COLORS = {darkGray, green, blue, red, yellow, magenta, pink, cyan};

    private final int[][] a;
    private final int width;
    private final int height;
    private final Timer timer;
    boolean wasDrop = false;

    private CellEntity[][] cellEntities;
    private Optional<BlockPair> falling;

    private final Random random = new Random();

    public GameBoard(int[][] a) {
        this.a = a;
        width = a.length;
        height = a[0].length;
        this.cellEntities = new CellEntity[a.length][a[0].length];
        resetBoard();
        timer = new Timer(1000, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        int size = BOARD_SIZE;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                g.setColor(cellEntities[i][j].getColor());
                g.fill3DRect(i * size, j * size, size, size, true);
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(width * BOARD_SIZE, height * BOARD_SIZE);
    }

    private void resetBoard() {
        random.nextInt(COLORS.length);
        int cellId = 0;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                cellId++;
                cellEntities[i][j] = new CellEntity(cellId, new Coords(i, j));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!wasDrop) {
            insertNewBlockPair(new BlockPair(GameBlock.random(), GameBlock.random()));
        }
        wasDrop = processAllDrops();
        repaint();
    }

    private boolean processAllDrops() {
        boolean wasDrop = false;
        Iterator<CellEntity[]> iteratorOfRowsFromBottom = getIteratorOfRowsFromBottom(cellEntities);
        while (iteratorOfRowsFromBottom.hasNext()) {
            CellEntity[] nextRow = iteratorOfRowsFromBottom.next();
            if (processRowForDrop(nextRow)) {
                wasDrop = true;
            }
        }
        return wasDrop;
    }

    private void insertNewBlockPair(BlockPair blockPair) {
        int insertNewBlockCell = cellEntities.length / 2;
        cellEntities[insertNewBlockCell][1] = new CellEntity(cellEntities[insertNewBlockCell][1].getId(), cellEntities[insertNewBlockCell][1].getCoords(), blockPair.getBlockA());
        cellEntities[insertNewBlockCell][0] = new CellEntity(cellEntities[insertNewBlockCell][0].getId(), cellEntities[insertNewBlockCell][0].getCoords(), blockPair.getBlockB());
        falling = Optional.of(blockPair);
    }

    private boolean processRowForDrop(CellEntity[] row) {
        boolean wasDrop = false;
        for (int i = 0; i < row.length; i++) {
            CellEntity cellEntity = row[i];
            if (cellEntity.isOccupied()) {
                if (isCellEntityBelowIsEmptyOrNotBorder(cellEntity.getCoords())) {
                    moveCellEntityDownOne(cellEntity, cellEntity.getCoords());
                    wasDrop = true;
                }
            }
        }
        return wasDrop;
    }

    private boolean isCellEntityBelowIsEmptyOrNotBorder(Coords coords) {
        int i = coords.i;
        int j = coords.j;
        if (j == (cellEntities[0].length - 1)) {
            return false;
        }
        if (!cellEntities[i][j + 1].isOccupied()) {
            return true;
        }
        return false;
    }

    private void moveCellEntityDownOne(CellEntity cellEntity, Coords coords) {
        int i = coords.i;
        int j = coords.j;
        cellEntities[i][j + 1] = new CellEntity(cellEntity, new Coords(i, j + 1));
        cellEntities[i][j] = new CellEntity(cellEntities[i][j].getId(), new Coords(i, j));
    }

    private Iterator<CellEntity[]> getIteratorOfRowsFromBottom(CellEntity[][] cellEntities) {
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

    private List<CellEntity> getAndRemoveLastRow(CellEntity[][] arrays) {
        List<CellEntity> cellEntitiesArray = new ArrayList<>();
        for (int i = 0; i < arrays.length; i++) {
            CellEntity[] row = arrays[i];
            if (row.length - 1 < 0) {
                continue;
            }
            CellEntity lastElement = row[row.length -1];
            cellEntitiesArray.add(lastElement);
            CellEntity[] cellEntiesWithRemoved = Arrays.copyOf(row, row.length - 1);
            arrays[i] = cellEntiesWithRemoved;
        }
        return cellEntitiesArray;
    }

    public static class Coords {
        int i;
        int j;

        public Coords(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
}
