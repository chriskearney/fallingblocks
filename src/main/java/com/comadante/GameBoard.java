package com.comadante;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import static java.awt.Color.*;
import static java.awt.Color.cyan;

public class GameBoard extends JComponent implements ActionListener {

    private final static int BOARD_SIZE = 20;
    private final static Color[] COLORS = {darkGray, green, blue, red, yellow, magenta, pink, cyan};

    private final int[][] a;
    private final int width;
    private final int height;
    private final Timer timer;

    private CellEntity[][] cellEntities;
    private Optional<BlockPair> falling;

    private final Random random = new Random();

    public GameBoard(int[][] a) {
        this.a = a;
        width = a.length;
        height = a[0].length;
        this.cellEntities = new CellEntity[a.length][a[0].length];
        resetBoard();
        timer = new Timer(500, this);
        timer.start();
        insertNewBlockPair(new BlockPair(GameBlock.random(), GameBlock.random()));
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
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                cellEntities[i][j] = new CellEntity();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        boolean wasDrop;
        do {
            wasDrop = processBlocksThatNeedToFall();
        } while (wasDrop);
        repaint();
    }

    private void insertNewBlockPair(BlockPair blockPair) {
        int insertNewBlockCell = cellEntities.length / 2;
        cellEntities[insertNewBlockCell][1] = new CellEntity(blockPair.getBlockA());
        cellEntities[insertNewBlockCell][0] = new CellEntity(blockPair.getBlockB());
        falling = Optional.of(blockPair);
    }

    private boolean processBlocksThatNeedToFall() {
        boolean wasDrop = false;
        for (int i = cellEntities.length - 1; i > 0; i--) {
           if (processRowForDrop(i)) {
               wasDrop = true;
           }
        }
        return wasDrop;
    }

    private boolean processRowForDrop(int i) {
        boolean wasDrop = false;
        for (int j = 0; j < cellEntities.length; j++) {
            CellEntity cellEntity = cellEntities[i][j];
            if (isCellEntityBelowIsEmptyOrNotBorder(i, j)) {
                moveCellEntityDownOne(cellEntity, i, j);
                wasDrop = true;
            }
        }
        return wasDrop;
    }

    private boolean isCellEntityBelowIsEmptyOrNotBorder(int i, int j) {
        if (j == (cellEntities[0].length - 1)) {
            return false;
        }
        if (!cellEntities[i][j + 1].isOccupied()) {
            return true;
        }
        return false;
    }

    private void moveCellEntityDownOne(CellEntity cellEntity, int i, int j) {
        cellEntities[i][j + 1] = cellEntity;
        cellEntities[i][j] = new CellEntity();
    }
}
