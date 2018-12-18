package com.comandante.game.board;

import java.util.Optional;

public class GameBoardCellEntity
{
    private final int id;
    private final GameBoardCoords gameBoardCoords;
    private final Optional<GameBlock> gameBlockOptional;
    private boolean markedForDestruction = false;

    public GameBoardCellEntity(int id, GameBoardCoords gameBoardCoords, GameBlock gameBlock) {
        this.id = id;
        this.gameBoardCoords = gameBoardCoords;
        gameBlockOptional = Optional.ofNullable(gameBlock);
    }

    public GameBoardCellEntity(int id, GameBoardCoords gameBoardCoords) {
        this.id = id;
        this.gameBoardCoords = gameBoardCoords;
        gameBlockOptional = Optional.empty();
    }

    public boolean isOccupied() {
        return gameBlockOptional.isPresent();
    }

    public int getId() {
        return id;
    }

    public GameBoardCoords getGameBoardCoords() {
        return gameBoardCoords;
    }

    public Optional<GameBlock> getGameBlock() {
        return gameBlockOptional;
    }

    public GameBlock.Type getType() {
        if (gameBlockOptional.isPresent()) {
            return gameBlockOptional.get().getType();
        }
        return GameBlock.Type.EMPTY;
    }

    public void setMarkedForDestruction(boolean markedForDestruction) {
        this.markedForDestruction = markedForDestruction;
    }

    public boolean isMarkedForDestruction() {
        return markedForDestruction;
    }
}
