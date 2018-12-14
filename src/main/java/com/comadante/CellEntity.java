package com.comadante;

import java.awt.*;
import java.util.Optional;

public class CellEntity
{
    private final Optional<GameBlock> gameBlockOptional;

    public CellEntity(GameBlock gameBlock) {
        gameBlockOptional = Optional.of(gameBlock);
    }

    public CellEntity() {
        gameBlockOptional = Optional.empty();
    }

    public Color getColor() {
        if (gameBlockOptional.isPresent()) {
            return gameBlockOptional.get().getColor();
        }
        return Color.black;
    }
}
