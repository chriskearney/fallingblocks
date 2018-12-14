package com.comadante;

import java.awt.*;
import java.util.Optional;

public class CellEntity
{
    private final int id;
    private final GameBoard.Coords coords;
    private final Optional<GameBlock> gameBlockOptional;

    public CellEntity(int id, GameBoard.Coords coords, GameBlock gameBlock) {
        this.id = id;
        this.coords = coords;
        gameBlockOptional = Optional.ofNullable(gameBlock);
    }

    public CellEntity(int id, GameBoard.Coords coords) {
        this.id = id;
        this.coords = coords;
        gameBlockOptional = Optional.empty();
    }

    public CellEntity(CellEntity orig, GameBoard.Coords newCords) {
        this(orig.getId(), newCords, orig.gameBlockOptional.orElse(null));
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

    public GameBoard.Coords getCoords() {
        return coords;
    }

    public Optional<GameBlock> getGameBlock() {
        return gameBlockOptional;
    }
}
