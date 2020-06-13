package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlockBorderType;
import com.comandante.game.board.GameBlockType;

import java.util.Objects;
import java.util.Optional;

public class BlockTypeBorder {
    private final GameBlockType type;
    private final Optional<GameBlockBorderType> borderType;

    public BlockTypeBorder(GameBlockType type, GameBlockBorderType borderType) {
        this.type = type;
        this.borderType = Optional.ofNullable(borderType);
    }

    public BlockTypeBorder(GameBlockType type) {
        this(type, null);
    }


    public GameBlockType getType() {
        return type;
    }

    public Optional<GameBlockBorderType> getBorderType() {
        return borderType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockTypeBorder that = (BlockTypeBorder) o;
        return type == that.type &&
                Objects.equals(borderType, that.borderType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, borderType);
    }
}
