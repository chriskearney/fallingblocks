package com.comadante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.Optional;

public class GameBoard extends JComponent implements ActionListener, KeyListener {

    private final static int BOARD_SIZE = 30;

    private final Timer timer;
    private final CellEntity[][] cellEntities;

    //Some State
    private Optional<BlockPair> blockPairActive;
    private boolean wasDrop = false;

    public GameBoard(int[][] a) {
        this.cellEntities = new CellEntity[a.length][a[0].length];
        resetBoard();
        timer = new Timer(1000, this);
        timer.start();
        addKeyListener(this);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    public void paintComponent(Graphics g) {
        runOnEveryCellEntity((invocationNumber, currentCords) -> {
            CellEntity cellEntity = getCellEntity(currentCords);
            g.setColor(cellEntity.getColor());
            g.fill3DRect(currentCords.i * BOARD_SIZE, currentCords.j * BOARD_SIZE, BOARD_SIZE, BOARD_SIZE, true);
        });
    }

    public Dimension getPreferredSize() {
        return new Dimension(cellEntities.length * BOARD_SIZE, cellEntities[0].length * BOARD_SIZE);
    }

    private void resetBoard() {
        runOnEveryCellEntity((invocationNumber, currentCords) -> setCellEntity(currentCords, new CellEntity(invocationNumber, currentCords)));
    }

    private CellEntity getCellEntity(Coords coords) {
        return cellEntities[coords.i][coords.j];
    }

    private void setCellEntity(Coords coords, CellEntity cellEntity) {
        cellEntities[coords.i][coords.j] = cellEntity;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!wasDrop) {
            insertNewBlockPair(new BlockPair(GameBlock.random(), GameBlock.random()));
            repaint();
        }
        wasDrop = processAllDrops();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_CONTROL) {
            rotateRight();
        }
    }

    private void runOnEveryCellEntity(CellRun cellRun) {
        int invocationNumber = 0;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                invocationNumber++;
                cellRun.run(invocationNumber, new Coords(i, j));
            }
        }
    }

    private boolean processAllDrops() {
        boolean wasDrop = false;
        Iterator<CellEntity[]> iteratorOfRowsFromBottom = GameBoardUtil.getIteratorOfRowsFromBottom(cellEntities);
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
        blockPairActive = Optional.of(blockPair);
    }

    private boolean processRowForDrop(CellEntity[] row) {
        boolean wasDrop = false;
        for (int i = 0; i < row.length; i++) {
            CellEntity cellEntity = row[i];
            if (cellEntity.isOccupied()) {
                if (isCellEntityBelowIsEmptyOrNotBorder(cellEntity)) {
                    moveCellEntityDownOne(cellEntity);
                    wasDrop = true;
                }
            }
        }
        return wasDrop;
    }

    private boolean isCellEntityBelowIsEmptyOrNotBorder(CellEntity cellEntity) {
        int i = cellEntity.getCoords().i;
        int j = cellEntity.getCoords().j;
        if (j == (cellEntities[0].length - 1)) {
            if (blockPairActive.isPresent()) {
                if (cellEntity.getGameBlock().equals(blockPairActive.get())) {
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

    private void moveCellEntityDownOne(CellEntity cellEntity) {
        int i = cellEntity.getCoords().i;
        int j = cellEntity.getCoords().j;
        cellEntities[i][j + 1] = new CellEntity(cellEntity, new Coords(i, j + 1));
        cellEntities[i][j] = new CellEntity(cellEntities[i][j].getId(), new Coords(i, j));
    }


    private void rotateRight() {
        if (!blockPairActive.isPresent()) {
            System.out.println("Its not active!");
            return;
        }
    }

    public static class Coords {
        int i;
        int j;

        public Coords(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }

    interface CellRun {
        void run(int invocationNumber, Coords currentCords);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

}
