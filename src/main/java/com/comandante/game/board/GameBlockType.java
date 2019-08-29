package com.comandante.game.board;

import com.comandante.game.assetmanagement.BlockTypeBorder;
import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.google.common.collect.Sets;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public enum GameBlockType {
    BLUE,
    CYAN,
    GOLD,
    GREEN,
    MAGENTA,
    ORANGE,
    PURPLE,
    RED,
    YELLOW,
    DIAMOND,

    COUNTDOWN_BLUE(Optional.empty(), Optional.of(BLUE)),
    COUNTDOWN_CYAN(Optional.empty(), Optional.of(CYAN)),
    COUNTDOWN_GOLD(Optional.empty(), Optional.of(GOLD)),
    COUNTDOWN_GREEN(Optional.empty(), Optional.of(GREEN)),
    COUNTDOWN_MAGENTA(Optional.empty(), Optional.of(MAGENTA)),
    COUNTDOWN_ORANGE(Optional.empty(), Optional.of(ORANGE)),
    COUNTDOWN_PURPLE(Optional.empty(), Optional.of(PURPLE)),
    COUNTDOWN_RED(Optional.empty(), Optional.of(RED)),
    COUNTDOWN_YELLOW(Optional.empty(), Optional.of(YELLOW)),

    MAGIC_BLUE(Optional.of(BLUE)),
    MAGIC_CYAN(Optional.of(CYAN)),
    MAGIC_GOLD(Optional.of(GOLD)),
    MAGIC_GREEN(Optional.of(GREEN)),
    MAGIC_MAGENTA(Optional.of(MAGENTA)),
    MAGIC_ORANGE(Optional.of(ORANGE)),
    MAGIC_PURPLE(Optional.of(PURPLE)),
    MAGIC_RED(Optional.of(RED)),
    MAGIC_YELLOW(Optional.of(YELLOW)),
    EMPTY;

    private Optional<GameBlockType> magicRelated;
    private Optional<GameBlockType> countDownRelated;


    GameBlockType(Optional<GameBlockType> magicRelated, Optional<GameBlockType> countDownRelated) {
        this.magicRelated = magicRelated;
        this.countDownRelated = countDownRelated;
    }

    GameBlockType(Optional<GameBlockType> magicRelated) {
        this.magicRelated = magicRelated;
        this.countDownRelated = Optional.empty();
    }

    GameBlockType() {
        this.magicRelated = Optional.empty();
        this.countDownRelated = Optional.empty();
    }

    public boolean isMagic() {
        return magicRelated.isPresent();
    }

    public Optional<GameBlockType> getRelated() {
        return magicRelated;
    }

    public Optional<GameBlockType> getCountDownRelated() {
        return countDownRelated;
    }

    public static GameBlockType[] getNormalRandomPool() {
        Set<GameBlockType> resolvedTypes = Sets.newHashSet();
        for (Map.Entry<BlockTypeBorder, List<BufferedImage>> next : TileSetGameBlockRenderer.imagesNew.entrySet()) {
            resolvedTypes.add(next.getKey().getType());
        }
        List<GameBlockType> collect = resolvedTypes.stream().filter(type -> !type.isMagic() && !type.getCountDownRelated().isPresent()).collect(Collectors.toList());
        collect.remove(EMPTY);
        collect.remove(DIAMOND);
        return collect.toArray(new GameBlockType[0]);
    }

    public static GameBlockType[] getRandomMagicPool() {
        Set<GameBlockType> resolvedTypes = Sets.newHashSet();
        for (Map.Entry<BlockTypeBorder, List<BufferedImage>> next : TileSetGameBlockRenderer.imagesNew.entrySet()) {
            resolvedTypes.add(next.getKey().getType());
        }

        List<GameBlockType> collect = resolvedTypes.stream().filter(type -> type.isMagic()).collect(Collectors.toList());
        return collect.toArray(new GameBlockType[0]);
    }

    public static GameBlockType[] getRandomCountDownPool() {
        Set<GameBlockType> resolvedTypes = Sets.newHashSet();
        for (Map.Entry<BlockTypeBorder, List<BufferedImage>> next : TileSetGameBlockRenderer.imagesNew.entrySet()) {
            resolvedTypes.add(next.getKey().getType());
        }
        List<GameBlockType> collect = resolvedTypes.stream().filter(type -> type.getCountDownRelated().isPresent()).collect(Collectors.toList());
        return collect.toArray(new GameBlockType[0]);
    }

}
