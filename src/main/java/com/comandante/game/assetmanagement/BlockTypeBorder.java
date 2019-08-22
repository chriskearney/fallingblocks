package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlock;

import java.util.Objects;
import java.util.Optional;

public class BlockTypeBorder {
    private final GameBlock.Type type;
    private final Optional<GameBlock.BorderType> borderType;

    public BlockTypeBorder(GameBlock.Type type, GameBlock.BorderType borderType) {
        this.type = type;
        this.borderType = Optional.ofNullable(borderType);
    }

    public BlockTypeBorder(GameBlock.Type type) {
        this(type, null);
    }


    public GameBlock.Type getType() {
        return type;
    }

    public Optional<GameBlock.BorderType> getBorderType() {
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
