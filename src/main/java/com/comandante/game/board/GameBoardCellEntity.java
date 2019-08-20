package com.comandante.game.board;

import java.awt.Rectangle;
import java.util.Optional;

public class GameBoardCellEntity {
    private final int id;
    private final GameBoardCoords gameBoardCoords;
    private final Optional<GameBlock> gameBlockOptional;
    private Rectangle rectangle;

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

    public GameBoardCellEntity(GameBlock gameBlock) {
        this.id = 1;
        this.gameBoardCoords = new GameBoardCoords(-1, -1);
        gameBlockOptional = Optional.of(gameBlock);
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

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
