package com.comadante;

import com.comadante.GameBoardCoords.MoveDirection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.comadante.GameBlockPair.BlockBOrientation.BOTTOM_OF;
import static com.comadante.GameBlockPair.BlockBOrientation.LEFT_OF;
import static com.comadante.GameBlockPair.BlockBOrientation.RIGHT_OF;
import static com.comadante.GameBlockPair.BlockBOrientation.TOP_OF;

public class GameBoard extends JComponent implements ActionListener, KeyListener {

    public final static int BLOCK_SIZE = 27;

    private final Timer timer;
    private final CellEntity[][] cellEntities;
    private final BlockRenderFactory blockRenderFactory;
    private final BlockPairFactory blockPairFactory;
    private final MagicBlockProcessor magicBlockProcessor;

    //Some State
    private Optional<GameBlockPair> blockPairActive;

    public GameBoard(int[][] a, BlockRenderFactory blockRenderFactory, BlockPairFactory blockPairFactory, MagicBlockProcessor magicBlockProcessor) {
        this.cellEntities = new CellEntity[a.length][a[0].length];
        this.blockRenderFactory = blockRenderFactory;
        this.blockPairFactory = blockPairFactory;
        this.magicBlockProcessor = magicBlockProcessor;
        resetBoard();
        timer = new Timer(500, this);
        timer.start();
        addKeyListener(this);
        setOpaque(false);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (allBlocksResting()) {
            insertNewBlockPair(blockPairFactory.createBlockPair(this));
            repaint();
        }
        processAllDrops();
        if (allBlocksResting()) {
            boolean wasThereMagic;
            do {
                wasThereMagic = magicBlockProcessor.process(this);
                if (wasThereMagic) {
                    processAllDrops();
                }
            } while (wasThereMagic);
        }
        repaint();
        repaint();
    }

    public void paintComponent(Graphics g) {
        runOnEveryCellEntity((invocationNumber, currentCords) -> {
            Optional<CellEntity> cellEntity = getCellEntity(currentCords);
            cellEntity.ifPresent(cellEntity1 -> blockRenderFactory.render(cellEntity1.getType(), g, currentCords));
        });
    }

    public Dimension getPreferredSize() {
        return new Dimension(cellEntities.length * BLOCK_SIZE, cellEntities[0].length * BLOCK_SIZE);
    }

    private void resetBoard() {
        runOnEveryCellEntity((invocationNumber, currentCords) -> setCellEntity(currentCords, new CellEntity(invocationNumber, currentCords)));
    }

    public Optional<CellEntity> getCellEntity(GameBoardCoords gameBoardCoords) {
        try {
            CellEntity entity = cellEntities[gameBoardCoords.i][gameBoardCoords.j];
            return Optional.of(entity);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return Optional.empty();
    }

    private void setCellEntity(GameBoardCoords gameBoardCoords, CellEntity cellEntity) {
        cellEntities[gameBoardCoords.i][gameBoardCoords.j] = cellEntity;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                //
                break;
            case KeyEvent.VK_DOWN:
                moveActiveBlockPair(MoveDirection.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                moveActiveBlockPair(MoveDirection.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                moveActiveBlockPair(MoveDirection.RIGHT);
                break;
            case KeyEvent.VK_CONTROL:
                rotate();
                break;
        }
    }

    public void runOnEveryCellEntity(CellRun cellRun) {
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

    private void insertNewBlockPair(GameBlockPair gameBlockPair) {
        int insertNewBlockCell = cellEntities.length / 2;
        cellEntities[insertNewBlockCell][0] = new CellEntity(cellEntities[insertNewBlockCell][0].getId(), cellEntities[insertNewBlockCell][0].getGameBoardCoords(), gameBlockPair.getBlockA());
        cellEntities[insertNewBlockCell][1] = new CellEntity(cellEntities[insertNewBlockCell][1].getId(), cellEntities[insertNewBlockCell][1].getGameBoardCoords(), gameBlockPair.getBlockB());
        blockPairActive = Optional.of(gameBlockPair);
    }

    private boolean processRowForDrop(CellEntity[] row) {
        boolean wasDrop = false;
        for (CellEntity cellEntity : row) {
            if (cellEntity.isOccupied()) {
                if (!cellEntity.isMarkedForDestruction() && isCellEntityBelowIsEmptyOrNotBorder(cellEntity)) {
                    moveCellEntityContents(MoveDirection.DOWN, cellEntity, false);
                    wasDrop = true;
                } else {
                    cellEntity.getGameBlock().get().setResting(true);
                }
            }
        }
        return wasDrop;
    }

    private boolean isCellEntityBelowIsEmptyOrNotBorder(CellEntity cellEntity) {
        int i = cellEntity.getGameBoardCoords().i;
        int j = cellEntity.getGameBoardCoords().j;
        // Because I will forget:
        // I am detecting if a cellentity that contains a block that belongs to an active falling blockpair.
        // because the following if statement means, we have reached the bottom border
        // i will mark the blockpair as inactive / or optional.empty
        if (j == (cellEntities[0].length - 1)) {
            if (blockPairActive.isPresent() && cellEntity.getGameBlock().isPresent()) {
                if (cellEntity.getGameBlock().get().equals(blockPairActive.get().getBlockAEntity()) || cellEntity.getGameBlock().get().equals(blockPairActive.get().getBlockBEntity())) {
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

    private Optional<CellEntity> moveCellEntityContents(MoveDirection moveDirection, CellEntity cellEntity, boolean repaint) {
        int i = cellEntity.getGameBoardCoords().i;
        int j = cellEntity.getGameBoardCoords().j;
        Optional<CellEntity> cellEntityOptional = getCellEntity(new GameBoardCoords(i + moveDirection.getDirectionApplyCoords().i, j + moveDirection.getDirectionApplyCoords().j));
        if (!cellEntityOptional.isPresent() || cellEntityOptional.get().isOccupied()) {
            return Optional.empty();
        }
        cellEntities[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j] = new CellEntity(cellEntityOptional.get().getId(), cellEntityOptional.get().getGameBoardCoords(), cellEntity.getGameBlock().orElse(null));
        cellEntities[i][j] = new CellEntity(cellEntity.getId(), cellEntity.getGameBoardCoords());
        if (repaint) {
            repaint();
        }
        return Optional.ofNullable(cellEntities[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j]);
    }

    private void moveActiveBlockPair(GameBoardCoords.MoveDirection direction) {
        if (!blockPairActive.isPresent()) {
            System.out.println("Its not active!");
            return;
        }
        GameBlockPair gameBlockPair = blockPairActive.get();

        Optional<CellEntity> blockAEntityOpt = gameBlockPair.getBlockAEntity();
        Optional<CellEntity> blockBEntityOpt = gameBlockPair.getBlockBEntity();

        if (!blockAEntityOpt.isPresent() || !blockBEntityOpt.isPresent()) {
            return;
        }

        CellEntity blockAEntity = blockAEntityOpt.get();
        CellEntity blockBEntity = blockBEntityOpt.get();

        if (isSpaceAvailable(direction, blockAEntity) &&
                isSpaceAvailable(direction, blockBEntity)) {

            Optional<GameBlockPair.BlockBOrientation> blockBOrientationOptional = gameBlockPair.getBlockBOrientation();
            if (!blockBOrientationOptional.isPresent()) {
                return;
            }

            GameBlockPair.BlockBOrientation blockBOrientation = blockBOrientationOptional.get();

            if ((blockBOrientation.equals(BOTTOM_OF) || blockBOrientation.equals(TOP_OF)) && (!direction.equals(MoveDirection.DOWN))) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(BOTTOM_OF) && direction.equals(MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }

            if (blockBOrientation.equals(TOP_OF) && direction.equals(MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if ((blockBOrientation.equals(RIGHT_OF) || blockBOrientation.equals(LEFT_OF)) && direction.equals(MoveDirection.DOWN)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(LEFT_OF) && direction.equals(MoveDirection.LEFT)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }

            if (blockBOrientation.equals(LEFT_OF) && direction.equals(MoveDirection.RIGHT)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(RIGHT_OF) && direction.equals(MoveDirection.LEFT)) {
                moveCellEntityContents(direction, blockAEntity, false);
                moveCellEntityContents(direction, blockBEntity, true);
            }

            if (blockBOrientation.equals(RIGHT_OF) && direction.equals(MoveDirection.RIGHT)) {
                moveCellEntityContents(direction, blockBEntity, false);
                moveCellEntityContents(direction, blockAEntity, true);
            }
        }
    }

    private boolean isSpaceAvailable(MoveDirection direction, CellEntity cellEntity) {
        Optional<CellEntity> destinationEntityOptional = getCellEntity(new GameBoardCoords(cellEntity.getGameBoardCoords().i + direction.getDirectionApplyCoords().i, cellEntity.getGameBoardCoords().j + direction.getDirectionApplyCoords().j));
        if (!destinationEntityOptional.isPresent()) {
            return false;
        }
        CellEntity destinationEntity = destinationEntityOptional.get();
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

    private Optional<CellEntity> getCellEntityIfOccupied(MoveDirection direction, CellEntity cellEntity) {
        Optional<CellEntity> destinationEntityOptional = getCellEntity(new GameBoardCoords(cellEntity.getGameBoardCoords().i + direction.getDirectionApplyCoords().i, cellEntity.getGameBoardCoords().j + direction.getDirectionApplyCoords().j));
        if (!destinationEntityOptional.isPresent()) {
            return Optional.empty();
        }
        CellEntity destinationEntity = destinationEntityOptional.get();
        if (destinationEntity.getGameBlock().isPresent()) {
            GameBlock gameBlock = destinationEntity.getGameBlock().get();
            if (blockPairActive.isPresent()) {
                if (blockPairActive.get().getBlockB().equals(gameBlock) ||
                        blockPairActive.get().getBlockA().equals(gameBlock)) {
                    return Optional.of(destinationEntity);
                }
            }
        }

        if (destinationEntity.isOccupied()) {
            return Optional.of(destinationEntity);
        }
        return Optional.empty();
    }

    private void rotate() {
        if (!blockPairActive.isPresent()) {
            System.out.println("Its not active!");
            return;
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        Optional<GameBlockPair.BlockBOrientation> blockBOrientation = gameBlockPair.getBlockBOrientation();
        if (!blockBOrientation.isPresent()) {
            System.out.printf("Can not determine the orientation of block b to block a!");
            return;
        }
        GameBlockPair.BlockBOrientation orientation = blockBOrientation.get();
        Optional<CellEntity> blockBEntity = gameBlockPair.getBlockBEntity();
        if (blockBEntity.isPresent()) {
            if (orientation.equals(BOTTOM_OF)) {
                moveCellEntityContents(MoveDirection.UP, moveCellEntityContents(MoveDirection.LEFT, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.LEFT_OF)) {
                moveCellEntityContents(MoveDirection.RIGHT, moveCellEntityContents(MoveDirection.UP, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.TOP_OF)) {
                moveCellEntityContents(MoveDirection.DOWN, moveCellEntityContents(MoveDirection.RIGHT, blockBEntity.get(), true).get(), true);
            }
            if (orientation.equals(GameBlockPair.BlockBOrientation.RIGHT_OF)) {
                moveCellEntityContents(MoveDirection.LEFT, moveCellEntityContents(MoveDirection.DOWN, blockBEntity.get(), true).get(), true);
            }
        }
    }

    public Optional<GameBoardCoords> getCoords(GameBlock gameBlock) {
        final GameBoardCoords[] located = {null};
        runOnEveryCellEntity((invocationNumber, currentCords) -> {
            CellEntity cE = cellEntities[currentCords.i][currentCords.j];
            if (cE.getGameBlock().isPresent() && cE.getGameBlock().get().equals(gameBlock)) {
                located[0] = cE.getGameBoardCoords();
            }
        });
        return Optional.ofNullable(located[0]);
    }

    public boolean allBlocksResting() {
        final boolean[] allResting = {true};
        runOnEveryCellEntity((invocationNumber, currentCords) -> {
            Optional<CellEntity> cellEntity = getCellEntity(currentCords);
            cellEntity.ifPresent(cellEntity1 -> {
                if (cellEntity1.getGameBlock().isPresent()) {
                    if (!cellEntity1.getGameBlock().get().isResting()) {
                        allResting[0] = false;
                    }
                }
            });
        });
        return allResting[0];
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

    public CellEntity[][] getCellEntities() {
        return cellEntities;
    }

    public java.util.List<CellEntity> getOccupiedNeighbors(CellEntity cellEntity, GameBlock.Type filter) {
        java.util.List<CellEntity> neighbors = new ArrayList<>();
        Optional<CellEntity> upCell = getCellEntityIfOccupied(MoveDirection.UP, cellEntity);
        Optional<CellEntity> downCell = getCellEntityIfOccupied(MoveDirection.DOWN, cellEntity);
        Optional<CellEntity> leftCell = getCellEntityIfOccupied(MoveDirection.LEFT, cellEntity);
        Optional<CellEntity> rightCell = getCellEntityIfOccupied(MoveDirection.RIGHT, cellEntity);
        upCell.ifPresent(neighbors::add);
        downCell.ifPresent(neighbors::add);
        leftCell.ifPresent(neighbors::add);
        rightCell.ifPresent(neighbors::add);
        return neighbors.stream().filter(cellEntity1 -> {
            if (cellEntity1.getType().getRelated().isPresent()) {
                GameBlock.Type relatedType = cellEntity1.getType().getRelated().get();
                if (relatedType.equals(filter)) {
                    return true;
                }
            }
            return cellEntity1.getType().equals(filter);
        }).collect(Collectors.toList());
    }
}
