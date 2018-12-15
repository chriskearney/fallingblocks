package com.comadante;

import java.awt.*;
import java.util.Optional;

public class CellEntity
{
    private final int id;
    private final GameBoardCoords gameBoardCoords;
    private final Optional<GameBlock> gameBlockOptional;

    public CellEntity(int id, GameBoardCoords gameBoardCoords, GameBlock gameBlock) {
        this.id = id;
        this.gameBoardCoords = gameBoardCoords;
        gameBlockOptional = Optional.ofNullable(gameBlock);
    }

    public CellEntity(int id, GameBoardCoords gameBoardCoords) {
        this.id = id;
        this.gameBoardCoords = gameBoardCoords;
        gameBlockOptional = Optional.empty();
    }

    public Color getColor() {
        if (gameBlockOptional.isPresent()) {
            return gameBlockOptional.get().getColor();
        }
        return Color.black;
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
}
