package com.comandante.game.board;

import com.google.common.collect.Lists;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.comandante.game.board.GameBoard.NO_OP;
import static com.comandante.game.board.GameBoardUtil.subtractCoords;


public class GameBoardData {
    public final static int BLOCK_SIZE = 32;
    private GameBoardCellEntity[][] cellEntities;
    private Optional<GameBlockPair> blockPairActive = Optional.empty();

    private int initLengthX;
    private int initLengthY;

    public GameBoardData(int[][] init) {
        this.initLengthX = init.length;
        this.initLengthY = init[0].length;
        resetBoard();
    }

    public void resetBoard() {
        cellEntities = new GameBoardCellEntity[initLengthX][initLengthY];
        int invocationNumber = 0;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                invocationNumber++;
                setCellEntity(new GameBoardCoords(i, j), new GameBoardCellEntity(invocationNumber, new GameBoardCoords(i, j)));
            }
        }
    }

    public Optional<GameBoardCellEntity> getByPoint(Point point) {
        return getCellsFromBottom().stream()
                .filter(gameBoardCellEntity -> gameBoardCellEntity.getRectangle() != null && gameBoardCellEntity.getRectangle().contains(point))
                .findFirst();
    }

    public GameBoardCellEntity[][] getCellEntities() {
        return cellEntities;
    }

    public List<GameBoardCellEntity> getCellsFromBottom() {
        List<GameBoardCellEntity> gameBoardCellEntities = Lists.newArrayList();
        for (int i = cellEntities[0].length - 1; i >= 0; i--) {
            for (int j = 0; j < cellEntities.length; j++) {
                gameBoardCellEntities.add(cellEntities[j][i]);
            }
        }
        return gameBoardCellEntities;
    }

    public List<GameBoardCellEntity[]> getListOfRowsFromBottom() {
        List<GameBoardCellEntity[]> gameBoardCellEntityArrays = Lists.newArrayList();

        for (int i = cellEntities[0].length - 1; i >= 0; i--) {
            List<GameBoardCellEntity> gameBoardCellEntities = Lists.newArrayList();
            for (int j = 0; j < cellEntities.length; j++) {
                gameBoardCellEntities.add(cellEntities[j][i]);
            }
            gameBoardCellEntityArrays.add(gameBoardCellEntities.toArray(new GameBoardCellEntity[gameBoardCellEntities.size()]));
        }

        return gameBoardCellEntityArrays;
    }


    public Dimension getPreferredSize() {
        return new Dimension(cellEntities.length * BLOCK_SIZE, cellEntities[0].length * BLOCK_SIZE);
    }

    private void setCellEntity(GameBoardCoords gameBoardCoords, GameBoardCellEntity gameBoardCellEntity) {
        cellEntities[gameBoardCoords.i][gameBoardCoords.j] = gameBoardCellEntity;
    }

    public Optional<GameBoardCellEntity> getCellEntity(GameBoardCoords gameBoardCoords) {
        try {
            GameBoardCellEntity entity = cellEntities[gameBoardCoords.i][gameBoardCoords.j];
            return Optional.of(entity);
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return Optional.empty();
    }

    public Optional<GameBlockPair> insertNewBlockPairAndDetectGameOver(GameBlockPair gameBlockPair) {
        int insertNewBlockCell = cellEntities.length / 2;
        if (cellEntities[insertNewBlockCell][0].isOccupied()) {
            return Optional.empty();
        }
        cellEntities[insertNewBlockCell][0] = new GameBoardCellEntity(cellEntities[insertNewBlockCell][0].getId(), cellEntities[insertNewBlockCell][0].getGameBoardCoords(), gameBlockPair.getBlockA());
        cellEntities[insertNewBlockCell][1] = new GameBoardCellEntity(cellEntities[insertNewBlockCell][1].getId(), cellEntities[insertNewBlockCell][1].getGameBoardCoords(), gameBlockPair.getBlockB());
        blockPairActive = Optional.of(gameBlockPair);
        return Optional.of(gameBlockPair);
    }

    public boolean processAllDrops() {
        boolean wasDrop = false;
        for (GameBoardCellEntity ce : getCellsFromBottom()) {
            if (ce.isOccupied()) {
                if (!ce.getGameBlock().get().isMarkForDeletion() && isCellEntityBelowIsEmptyOrNotBorder(ce)) {
                    moveCellEntityContents(GameBoardCoords.MoveDirection.DOWN, ce, NO_OP);
                    wasDrop = true;
                }
            }
        }
        return wasDrop;
    }

    public void evaluateRestingStatus() {
        for (GameBoardCellEntity ce : getCellsFromBottom()) {
            if (ce.getGameBlock().isPresent()) {
                if (!isCellEntityBelowIsEmptyOrNotBorder(ce)) {
                    ce.getGameBlock().get().setResting(true);
                } else {
                    ce.getGameBlock().get().setResting(false);
                }
            }
        }
    }

    public boolean insertAttackBlocksAndDetectGameOver(GameBoardCellEntity[][] attackBlocks) {
        List<GameBoardCellEntity[]> listOfRowsFromBottom = getListOfRowsFromBottom();
        for (GameBoardCellEntity[] next: listOfRowsFromBottom) {
            boolean b = insertAttackBlocksAndDetectGameOver(next);
            if (b) {
                return true;
            }
        }
        return false;
    }

    public boolean insertAttackBlocksAndDetectGameOver(GameBoardCellEntity[] attackRow) {
        for (int i = 0; i < attackRow.length; i++) {
            GameBoardCellEntity existingGameBlockOnBoard = cellEntities[i][0];
            if (existingGameBlockOnBoard.isOccupied()) {
                //game over
                return true;
            } else {
                // set the existing cell to the incoming attack block
                cellEntities[i][0] = new GameBoardCellEntity(existingGameBlockOnBoard.getId(), existingGameBlockOnBoard.getGameBoardCoords(), attackRow[i].getGameBlock().get());
            }
        }
        return false;
    }

    public boolean isCellEntityBelowIsEmptyOrNotBorder(GameBoardCellEntity gameBoardCellEntity) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        if (j == (cellEntities[0].length - 1)) {
            return false;
        }
        if (!cellEntities[i][j + 1].isOccupied()) {
            return true;
        }
        return false;
    }

    public Optional<GameBlockPair.BlockBOrientation> getBlockBOrientation() {
        if (!blockPairActive.isPresent()) {
            return Optional.empty();
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        Optional<GameBoardCoords> blockAGameBoardCoords = getCoords(gameBlockPair.getBlockA());
        Optional<GameBoardCoords> blockBGameBoardCoords = getCoords(gameBlockPair.getBlockB());
        if (blockAGameBoardCoords.isPresent() && blockBGameBoardCoords.isPresent()) {
            return GameBlockPair.BlockBOrientation.fromCoords(subtractCoords(blockAGameBoardCoords.get(), blockBGameBoardCoords.get()));
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getBlockBEntity() {
        if (!blockPairActive.isPresent()) {
            return Optional.empty();
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        if (getCoords(gameBlockPair.getBlockB()).isPresent()) {
            return getCellEntity(getCoords(gameBlockPair.getBlockB()).get());
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getBlockAEntity() {
        if (!blockPairActive.isPresent()) {
            return Optional.empty();
        }
        GameBlockPair gameBlockPair = blockPairActive.get();
        if (getCoords(gameBlockPair.getBlockA()).isPresent()) {
            return getCellEntity(getCoords(gameBlockPair.getBlockA()).get());
        }
        return Optional.empty();
    }

    public boolean isBlockPairActive() {
        return blockPairActive.isPresent();
    }

    public boolean isSpaceAvailable(GameBoardCoords.MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
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

        return !destinationEntity.isOccupied();
    }

    public Optional<GameBoardCellEntity[]> getMatchingRowAboveIfExists(GameBoardCellEntity[] oneRowGroupOfMatching) {
        java.util.List<GameBoardCellEntity> matchingNorthernNeighbors = new ArrayList<>();
        for (GameBoardCellEntity cellEntity : oneRowGroupOfMatching) {
            Optional<GameBoardCellEntity> northNeighbor = getCellEntityIfOccupiedSameType(GameBoardCoords.MoveDirection.UP, cellEntity);
            if (!northNeighbor.isPresent()) {
                return Optional.empty();
            }
            matchingNorthernNeighbors.add(northNeighbor.get());
        }
        return Optional.of(matchingNorthernNeighbors.toArray(new GameBoardCellEntity[0]));
    }

    public Optional<GameBoardCellEntity> getCellEntityIfOccupiedSameType(GameBoardCoords.MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
        Optional<GameBoardCellEntity> cellEntityIfOccupied = getCellEntityIfOccupied(direction, gameBoardCellEntity);
        if (cellEntityIfOccupied.isPresent() && cellEntityIfOccupied.get().getType().equals(gameBoardCellEntity.getType())) {
            return cellEntityIfOccupied;
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getCellEntityIfOccupied(GameBoardCoords.MoveDirection direction, GameBoardCellEntity gameBoardCellEntity) {
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

    public Optional<GameBoardCoords> getCoords(GameBlock gameBlock) {
        Optional<GameBoardCoords> foundCoords = Optional.empty();
        for (GameBoardCellEntity ce : getCellsFromBottom()) {
            if (ce.getGameBlock().isPresent() && ce.getGameBlock().get().equals(gameBlock)) {
                foundCoords = Optional.of(ce.getGameBoardCoords());
            }
        }
        return foundCoords;
    }

    public List<GameBoardCellEntity> getOccupiedNeighborsOfType(GameBoardCellEntity gameBoardCellEntity, GameBlock.Type targetType) {
        List<GameBoardCellEntity> neighbors = new ArrayList<>();
        Optional<GameBoardCellEntity> upCell = getCellEntityIfOccupied(GameBoardCoords.MoveDirection.UP, gameBoardCellEntity);
        Optional<GameBoardCellEntity> downCell = getCellEntityIfOccupied(GameBoardCoords.MoveDirection.DOWN, gameBoardCellEntity);
        Optional<GameBoardCellEntity> leftCell = getCellEntityIfOccupied(GameBoardCoords.MoveDirection.LEFT, gameBoardCellEntity);
        Optional<GameBoardCellEntity> rightCell = getCellEntityIfOccupied(GameBoardCoords.MoveDirection.RIGHT, gameBoardCellEntity);
        upCell.ifPresent(neighbors::add);
        downCell.ifPresent(neighbors::add);
        leftCell.ifPresent(neighbors::add);
        rightCell.ifPresent(neighbors::add);
        return neighbors.stream().filter(ce -> {
            if (ce.getType().getRelated().isPresent()) {
                GameBlock.Type relatedType = ce.getType().getRelated().get();
                if (relatedType.equals(targetType)) {
                    return true;
                }
            }
            return ce.getType().equals(targetType);
        }).collect(Collectors.toList());
    }

    public boolean allBlocksResting() {

        List<GameBoardCellEntity> cellsFromBottom = getCellsFromBottom();
        for (GameBoardCellEntity gameBoardCellEntity: cellsFromBottom) {
            if (gameBoardCellEntity.isOccupied()) {
                GameBlock gameBlock = gameBoardCellEntity.getGameBlock().get();
                if (gameBlock.isMarkForDeletion()) {
                    return false;
                }
                if (!gameBlock.isResting()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setRow(int rowNumber, GameBoardCellEntity[] row) {
        for (int i = 0; i <= cellEntities.length - 1; i++) {
            GameBoardCellEntity gameBoardCellEntity = new GameBoardCellEntity(i, new GameBoardCoords(i, rowNumber), row[i].getGameBlock().orElse(null));
            cellEntities[i][rowNumber] = gameBoardCellEntity;
        }
    }

    public Optional<GameBoardCellEntity> moveCellEntityContents(GameBoardCoords.MoveDirection moveDirection, GameBoardCellEntity gameBoardCellEntity, Runnable postRun) {
        int i = gameBoardCellEntity.getGameBoardCoords().i;
        int j = gameBoardCellEntity.getGameBoardCoords().j;
        Optional<GameBoardCellEntity> cellEntityOptional = getCellEntity(new GameBoardCoords(i + moveDirection.getDirectionApplyCoords().i, j + moveDirection.getDirectionApplyCoords().j));
        if (!cellEntityOptional.isPresent() || cellEntityOptional.get().isOccupied()) {
            return Optional.empty();
        }
        getCellEntities()[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j] = new GameBoardCellEntity(cellEntityOptional.get().getId(), cellEntityOptional.get().getGameBoardCoords(), gameBoardCellEntity.getGameBlock().orElse(null));
        getCellEntities()[i][j] = new GameBoardCellEntity(gameBoardCellEntity.getId(), gameBoardCellEntity.getGameBoardCoords());
        if (blockPairActive.isPresent()) {
            blockPairActive.get().getBlockA().setResting(false);
            blockPairActive.get().getBlockB().setResting(false);
        }
        postRun.run();
        return Optional.ofNullable(getCellEntities()[i + moveDirection.getDirectionApplyCoords().i][j + moveDirection.getDirectionApplyCoords().j]);
    }
}
