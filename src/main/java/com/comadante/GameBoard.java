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

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (!wasDrop) {
            insertNewBlockPair(new BlockPair(GameBlock.random(), GameBlock.random(), this));
            repaint();
        }
        wasDrop = processAllDrops();
        repaint();
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

    public CellEntity getCellEntity(GameBoardCoords gameBoardCoords) {
        return cellEntities[gameBoardCoords.i][gameBoardCoords.j];
    }

    private void setCellEntity(GameBoardCoords gameBoardCoords, CellEntity cellEntity) {
        cellEntities[gameBoardCoords.i][gameBoardCoords.j] = cellEntity;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_CONTROL) {
            rotate();
        }
    }

    private void runOnEveryCellEntity(CellRun cellRun) {
        int invocationNumber = 0;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                invocationNumber++;
                cellRun.run(invocationNumber, new GameBoardCoords(i, j));
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
        cellEntities[insertNewBlockCell][0] = new CellEntity(cellEntities[insertNewBlockCell][0].getId(), cellEntities[insertNewBlockCell][0].getGameBoardCoords(), blockPair.getBlockA());
        cellEntities[insertNewBlockCell][1] = new CellEntity(cellEntities[insertNewBlockCell][1].getId(), cellEntities[insertNewBlockCell][1].getGameBoardCoords(), blockPair.getBlockB());
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
        int i = cellEntity.getGameBoardCoords().i;
        int j = cellEntity.getGameBoardCoords().j;
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
        int i = cellEntity.getGameBoardCoords().i;
        int j = cellEntity.getGameBoardCoords().j;
        CellEntity existingCellEntityInDestintation = cellEntities[i][j + 1];
        if (existingCellEntityInDestintation.isOccupied()) {
            return;
        }
        cellEntities[i][j + 1] = new CellEntity(existingCellEntityInDestintation.getId(), existingCellEntityInDestintation.getGameBoardCoords(), cellEntity.getGameBlock().orElse(null));
        cellEntities[i][j] = new CellEntity(cellEntity.getId(), cellEntity.getGameBoardCoords());
    }

    private void moveCellEntityRightOne(CellEntity cellEntity) {
        int i = cellEntity.getGameBoardCoords().i;
        int j = cellEntity.getGameBoardCoords().j;
        CellEntity existingCellEntityInDestintation = cellEntities[i + 1][j];
        if (existingCellEntityInDestintation.isOccupied()) {
            return;
        }
        cellEntities[i + 1][j] = new CellEntity(existingCellEntityInDestintation.getId(), existingCellEntityInDestintation.getGameBoardCoords(), cellEntity.getGameBlock().orElse(null));
        cellEntities[i][j] = new CellEntity(cellEntity.getId(), cellEntity.getGameBoardCoords());
    }

    private void moveCellEntityLeftOne(CellEntity cellEntity) {
        int i = cellEntity.getGameBoardCoords().i;
        int j = cellEntity.getGameBoardCoords().j;
        CellEntity existingCellEntityInDestintation = cellEntities[i - 1][j];
        if (existingCellEntityInDestintation.isOccupied()) {
            return;
        }
        cellEntities[i - 1][j] = new CellEntity(existingCellEntityInDestintation.getId(), existingCellEntityInDestintation.getGameBoardCoords(), cellEntity.getGameBlock().orElse(null));
        cellEntities[i][j] = new CellEntity(cellEntity.getId(), cellEntity.getGameBoardCoords());
    }

    private void moveCellEntityUpOne(CellEntity cellEntity) {
        int i = cellEntity.getGameBoardCoords().i;
        int j = cellEntity.getGameBoardCoords().j;
        CellEntity existingCellEntityInDestintation = cellEntities[i][j - 1];
        if (existingCellEntityInDestintation.isOccupied()) {
            return;
        }
        cellEntities[i][j - 1] = new CellEntity(existingCellEntityInDestintation.getId(), existingCellEntityInDestintation.getGameBoardCoords(), cellEntity.getGameBlock().orElse(null));
        cellEntities[i][j] = new CellEntity(cellEntity.getId(), cellEntity.getGameBoardCoords());
    }

    private void moveCellEntityToDirection(GameBoardCoords gameBoardCoords, CellEntity cellEntity) {

    }


    private void rotate() {
        if (!blockPairActive.isPresent()) {
            System.out.println("Its not active!");
            return;
        }
        BlockPair blockPair = blockPairActive.get();
        Optional<BlockPair.BlockBOrientation> blockBOrientation = blockPair.getBlockBOrientation();
        if (!blockBOrientation.isPresent()) {
            System.out.printf("Can not determine the orientation of block b to block a!");
            return;
        }
        BlockPair.BlockBOrientation orientation = blockBOrientation.get();
        if (orientation.equals(BlockPair.BlockBOrientation.BOTTOM_OF)) {
            moveCellEntityLeftOne(blockPair.getBlockBEntity());
            repaint();
            moveCellEntityUpOne(getCellEntity(getCoords(blockPair.getBlockB())));
            repaint();
        }
        if (orientation.equals(BlockPair.BlockBOrientation.LEFT_OF)) {
            moveCellEntityUpOne(blockPair.getBlockBEntity());
            repaint();
            moveCellEntityRightOne(blockPair.getBlockBEntity());
            repaint();
        }
        if (orientation.equals(BlockPair.BlockBOrientation.TOP_OF)) {
            moveCellEntityRightOne(blockPair.getBlockBEntity());
            repaint();
            moveCellEntityDownOne(blockPair.getBlockBEntity());
            repaint();
        }
        if (orientation.equals(BlockPair.BlockBOrientation.RIGHT_OF)) {
            moveCellEntityDownOne(blockPair.getBlockBEntity());
            repaint();
            moveCellEntityLeftOne(blockPair.getBlockBEntity());
            repaint();
        }
    }

    public GameBoardCoords getCoords(GameBlock gameBlock) {
        final GameBoardCoords[] located = {null};
        runOnEveryCellEntity((invocationNumber, currentCords) -> {
            CellEntity cE = cellEntities[currentCords.i][currentCords.j];
            if (cE.getGameBlock().isPresent() && cE.getGameBlock().get().equals(gameBlock)) {
                located[0] = cE.getGameBoardCoords();
            }
        });
        if (located[0] == null) {
            throw new RuntimeException("Unable to find coordinates for gameBlock.");
        }
        return located[0];
    }

    interface CellRun {
        void run(int invocationNumber, GameBoardCoords currentCords);
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

}
