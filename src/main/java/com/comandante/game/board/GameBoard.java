package com.comandante.game.board;

import com.comandante.game.board.GameBoardCoords.MoveDirection;
import com.comandante.game.board.logic.GameBlockPairFactory;
import com.comandante.game.board.logic.GameBlockRenderer;
import com.comandante.game.board.logic.MagicGameBlockProcessor;
import com.comandante.game.textboard.TextBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

import static com.comandante.game.board.GameBlockPair.BlockBOrientation.BOTTOM_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.LEFT_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.RIGHT_OF;
import static com.comandante.game.board.GameBlockPair.BlockBOrientation.TOP_OF;

public class GameBoard extends JComponent implements ActionListener, KeyListener {

    public final static int BLOCK_SIZE = 32;

    private final Timer timer;
    private final GameBoardCellEntity[][] cellEntities;
    private final GameBlockRenderer gameBlockRenderer;
    private final GameBlockPairFactory gameBlockPairFactory;
    private final MagicGameBlockProcessor magicGameBlockProcessor;
    private final TextBoard textBoard;
    private Integer score = 0;
    private Integer largestScore = 0;
    private boolean paused = false;

    //Some State
    private Optional<GameBlockPair> blockPairActive;

    public GameBoard(int[][] a, GameBlockRenderer gameBlockRenderer, GameBlockPairFactory gameBlockPairFactory, MagicGameBlockProcessor magicGameBlockProcessor, TextBoard textBoard) {
        this.cellEntities = new GameBoardCellEntity[a.length][a[0].length];
        this.gameBlockRenderer = gameBlockRenderer;
        this.gameBlockPairFactory = gameBlockPairFactory;
        this.magicGameBlockProcessor = magicGameBlockProcessor;
        this.textBoard = textBoard;
        initializeBoard();
        timer = new Timer(400, this);
        timer.start();
        addKeyListener(this);
        setOpaque(false);
        alterScore(0);
    }

    public void addNotify() {
        super.addNotify();
        requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (paused) {
            return;
        }
        if (allBlocksResting()) {
            insertNewBlockPair(gameBlockPairFactory.createBlockPair(this));
            textBoard.setNextBlockPair(gameBlockPairFactory.getNextPair());
            repaint();
        }
        processAllDrops();
        if (allBlocksResting()) {
            boolean wasThereMagic;
            do {
                wasThereMagic = magicGameBlockProcessor.process(this);
                if (wasThereMagic) {
                    processAllDrops();
                }
            } while (wasThereMagic);
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        for (GameBoardCellEntity ce: GameBoardUtil.getCellsFromBottom(cellEntities)) {
            gameBlockRenderer.render(ce.getType(), g, ce.getGameBoardCoords());
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(cellEntities.length * BLOCK_SIZE, cellEntities[0].length * BLOCK_SIZE);
    }

    private void initializeBoard() {
        int invocationNumber = 0;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                invocationNumber++;
                setCellEntity(new GameBoardCoords(i, j), new GameBoardCellEntity(invocationNumber, new GameBoardCoords(i, j)));
            }
        }
    }

    public Optional<GameBoardCellEntity> getCellEntity(GameBoardCoords gameBoardCoords) {
        try {
            GameBoardCellEntity entity = cellEntities[gameBoardCoords.i][gameBoardCoords.j];
            return Optional.of(entity);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return Optional.empty();
    }

    private void setCellEntity(GameBoardCoords gameBoardCoords, GameBoardCellEntity gameBoardCellEntity) {
        cellEntities[gameBoardCoords.i][gameBoardCoords.j] = gameBoardCellEntity;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_S:
                moveActiveBlockPair(MoveDirection.DOWN);
                break;
            case KeyEvent.VK_A:
                moveActiveBlockPair(MoveDirection.LEFT);
                break;
            case KeyEvent.VK_D:
                moveActiveBlockPair(MoveDirection.RIGHT);
                break;
            case KeyEvent.VK_W:
                rotate();
                break;
            case KeyEvent.VK_P:
                togglePause();
                break;
        }
    }

    private boolean processAllDrops() {
        boolean wasDrop = false;
        for (GameBoardCellEntity ce: GameBoardUtil.getCellsFromBottom(cellEntities)) {
            if (ce.isOccupied()) {
                if (!ce.isMarkedForDestruction() && isCellEntityBelowIsEmptyOrNotBorder(ce)) {
                    moveCellEntityContents(MoveDirection.DOWN, ce, false);
                    wasDrop = true;
                } else {
                    ce.getGameBlock().get().setResting(true);
                }
            }
        }
        return wasDrop;
    }

    public void togglePause() {
        paused = !paused;
        textBoard.getTextBoardContents().setPaused(paused);
    }

    private void insertNewBlockPair(GameBlockPair gameBlockPair) {
        int insertNewBlockCell = cellEntities.length / 2;
        cellEntities[insertNewBlockCell][0] = new GameBoardCellEntity(cellEntities[insertNewBlockCell][0].getId(), cellEntities[insertNewBlockCell][0].getGameBoardCoords(), gameBlockPair.getBlockA());
        cellEntities[insertNewBlockCell][1] = new GameBoardCellEntity(cellEntities[insertNewBlockCell][1].getId(), cellEntities[insertNewBlockCell][1].getGameBoardCoords(), gameBlockPair.getBlockB());
        blockPairActive = Optional.of(gameBlockPair);
    }

    private boolean isCellEntityBelowIsEmptyOrNotBorder(GameBoardCellEntity gameBoardCellEntity) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        // Because I will forget:
        // I am detecting if a cellentity that contains a block that belongs to an active falling blockpair.
        // because the following if statement means, we have reached the bottom border
        // i will mark the blockpair as inactive / or optional.empty
        if (j == (cellEntities[0].length - 1)) {
            if (blockPairActive.isPresent() && gameBoardCellEntity.getGameBlock().isPresent()) {
                if (gameBoardCellEntity.getGameBlock().get().equals(blockPairActive.get().getBlockAEntity()) || gameBoardCellEntity.getGameBlock().get().equals(blockPairActive.get().getBlockBEntity())) {
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

    private Optional<GameBoardCellEntity> moveCellEntityContents(MoveDirection moveDirection, GameBoardCellEntity gameBoardCellEntity, boolean repaint) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        Optional<GameBoardCellEntity> cellEntityOptional = getCellEntity(new GameBoardCoords(i + moveDirection.getDirectionApplyCoords().i, j + moveDirection.getDirectionApplyCoords().j));
        if (!cellEntityOptional.isPresent() || cellEntityOptional.get().isOccupied()) {
            return Optional.empty();
        }
        cellEntities[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j] = new GameBoardCellEntity(cellEntityOptional.get().getId(), cellEntityOptional.get().getGameBoardCoords(), gameBoardCellEntity.getGameBlock().orElse(null));
        cellEntities[i][j] = new GameBoardCellEntity(gameBoardCellEntity.getId(), gameBoardCellEntity.getGameBoardCoords());
        if (repaint) {
            repaint();
        }
        return Optional.ofNullable(cellEntities[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j]);
    }


    private boolean isBlockPairActive() {
        return blockPairActive.isPresent() && (!blockPairActive.filter(gameBlockPair -> gameBlockPair.getBlockA().isResting() || gameBlockPair.getBlockB().isResting()).isPresent());
    }

    private void moveActiveBlockPair(GameBoardCoords.MoveDirection direction) {
        if (!isBlockPairActive()) {
            return;
        }
        GameBlockPair gameBlockPair = blockPairActive.get();

        Optional<GameBoardCellEntity> blockAEntityOpt = gameBlockPair.getBlockAEntity();
        Optional<GameBoardCellEntity> blockBEntityOpt = gameBlockPair.getBlockBEntity();

        if (!blockAEntityOpt.isPresent() || !blockBEntityOpt.isPresent()) {
            return;
        }

        GameBoardCellEntity blockAEntity = blockAEntityOpt.get();
        GameBoardCellEntity blockBEntity = blockBEntityOpt.get();

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

    private boolean isSpaceAvailable(MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
        Optional<GameBoardCellEntity> destinationEntityOptional = getCellEntity(new GameBoardCoords(gameBoardCellEntity.getGameBoardCoords().i + direction.getDirectionApplyCoords().i, gameBoardCellEntity.getGameBoardCoords().j + direction.getDirectionApplyCoords().j));
        if (!destinationEntityOptional.isPresent()) {
            return false;
        }
        GameBoardCellEntity destinationEntity = destinationEntityOptional.get();
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

    public Optional<GameBoardCellEntity> getCellEntityIfOccupied(MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
        Optional<GameBoardCellEntity> destinationEntityOptional = getCellEntity(new GameBoardCoords(gameBoardCellEntity.getGameBoardCoords().i + direction.getDirectionApplyCoords().i, gameBoardCellEntity.getGameBoardCoords().j + direction.getDirectionApplyCoords().j));
        if (!destinationEntityOptional.isPresent()) {
            return Optional.empty();
        }
        GameBoardCellEntity destinationEntity = destinationEntityOptional.get();
        if (destinationEntity.isOccupied()) {
            return Optional.of(destinationEntity);
        }
        return Optional.empty();
    }

    private void rotate() {
        if (!isBlockPairActive()) {
            return;
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        Optional<GameBlockPair.BlockBOrientation> blockBOrientation = gameBlockPair.getBlockBOrientation();
        if (!blockBOrientation.isPresent()) {
            System.out.printf("Can not determine the orientation of block b to block a!");
            return;
        }
        GameBlockPair.BlockBOrientation orientation = blockBOrientation.get();
        Optional<GameBoardCellEntity> blockBEntity = gameBlockPair.getBlockBEntity();
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
        Optional<GameBoardCoords> foundCoords = Optional.empty();
        for (GameBoardCellEntity ce: GameBoardUtil.getCellsFromBottom(cellEntities)) {
            if (ce.getGameBlock().isPresent() && ce.getGameBlock().get().equals(gameBlock)) {
                foundCoords = Optional.of(ce.getGameBoardCoords());
            }
        }
        return foundCoords;
    }

    public boolean allBlocksResting() {
        boolean allResting = true;
        for (GameBoardCellEntity ce: GameBoardUtil.getCellsFromBottom(cellEntities)) {
            if (ce.isOccupied() && !ce.getGameBlock().get().isResting()) {
                allResting = false;
            }
        }
        return allResting;
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    public GameBoardCellEntity[][] getCellEntities() {
        return cellEntities;
    }

    public TextBoard getTextBoard() {
        return textBoard;
    }

    public void alterScore(int amount) {
        if (amount > largestScore) {
            largestScore = amount;
        }
        score += amount;
        this.textBoard.getTextBoardContents().setScore(score);
        if (amount > 0) {
            this.textBoard.getTextBoardContents().addNewPointsToBattleLog(amount);
        }
    }
}
