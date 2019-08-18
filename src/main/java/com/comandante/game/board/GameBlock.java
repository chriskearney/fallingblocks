package com.comandante.game.board;

import com.comandante.game.assetmanagement.RenderInvoker;
import com.comandante.game.assetmanagement.TileSet;
import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.logic.GameBlockRenderer;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.stream.Collectors;

public class GameBlock {

    private final static Random RANDOM = new Random();
    private final static List<Type> NORMAL_VALUES = Collections.unmodifiableList(Arrays.asList(Type.getNormalRandomPool()));
    private final static int NORMAL_VALUES_SIZE = NORMAL_VALUES.size();

    private final static List<Type> RANDOM_VALUES = Collections.unmodifiableList(Arrays.asList(Type.getRandomPool()));
    private final static int RANDOM_VALUE_SIZE = RANDOM_VALUES.size();

    private final Type type;
    private final UUID identifier;
    private boolean resting = false;
    private Optional<BorderType> borderType;
    private final Optional<InvocationRound<BufferedImage>> invocationRound;

    public GameBlock(Type type, InvocationRound<BufferedImage> invocationRenderRounds) {
        this.type = type;
        this.identifier = UUID.randomUUID();
        this.invocationRound = Optional.of(invocationRenderRounds);
    }

    public GameBlock(Type type) {
        this.type = type;
        this.identifier = UUID.randomUUID();
        this.invocationRound = Optional.empty();
    }

    public GameBlock(Type type, UUID uuid) {
        this.type = type;
        this.identifier = uuid;
        this.invocationRound = Optional.empty();
    }

    public Type getType() {
        return type;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public static GameBlock randomNormalBlock() {
        Type randomType = NORMAL_VALUES.get(RANDOM.nextInt(NORMAL_VALUES_SIZE));
        return new GameBlock(randomType);
    }

    public static GameBlock randomMagicBlock(GameBlockRenderer gameBlockRenderer) {
        Type randomType = RANDOM_VALUES.get(RANDOM.nextInt(RANDOM_VALUE_SIZE));
        InvocationRound<BufferedImage> bufferedImageInvocationRound = new InvocationRound<>(3, new RenderInvoker(gameBlockRenderer.getImage(randomType)), true);
        return new GameBlock(randomType, bufferedImageInvocationRound);
    }

    public static GameBlock diamondBlock() {
        return new GameBlock(Type.DIAMOND);
    }

    public void setResting(boolean resting) {
        this.resting = resting;
    }

    public boolean isResting() {
        return resting;
    }

    public Optional<BorderType> getBorderType() {
        return borderType;
    }

    public void setBorderType(Optional<BorderType> borderType) {
        this.borderType = borderType;
    }

    public TileSetGameBlockRenderer.BlockTypeBorder getBlockTypeBorder() {
        if (borderType == null) {
            return new TileSetGameBlockRenderer.BlockTypeBorder(getType());
        }
        return new TileSetGameBlockRenderer.BlockTypeBorder(getType(), borderType.orElse(BorderType.NO_BORDER));
    }

    public Optional<BufferedImage> getImageToRender() {
        if (!invocationRound.isPresent()) {
            return Optional.empty();
        }
        return invocationRound.get().invoke();
    }

    public enum BorderType {
        TOP,
        TOP_LEFT,
        LEFT,
        BOTTOM_LEFT,
        TOP_RIGHT,
        RIGHT,
        BOTTOM_RIGHT,
        BOTTOM,
        TOP_BOTTOM,
        TOP_LEFT_BOTTOM,
        TOP_RIGHT_BOTTOM,
        TOP_LEFT_RIGHT,
        NO_BORDER
    }

    public enum Type {
        BLUE,
        GREEN,
        RED,
        YELLOW,
        DIAMOND,
        MAGIC_BLUE(Optional.of(BLUE)),
        MAGIC_GREEN(Optional.of(GREEN)),
        MAGIC_RED(Optional.of(RED)),
        MAGIC_YELLOW(Optional.of(YELLOW)),
        EMPTY;

        private Optional<Type> magicRelated;

        Type(Optional<Type> magicRelated) {
            this.magicRelated = magicRelated;
        }

        Type() {
            this.magicRelated = Optional.empty();
        }

        public boolean isMagic() {
            return magicRelated.isPresent();
        }

        public Optional<Type> getRelated() {
            return magicRelated;
        }

        public static Type[] getNormalRandomPool() {
            ArrayList<Type> normalBlockTypes = Arrays.asList(values()).stream().filter(type -> !type.isMagic()).collect(Collectors.toCollection(ArrayList::new));
            normalBlockTypes.remove(EMPTY);
            normalBlockTypes.remove(DIAMOND);
            return normalBlockTypes.toArray(new Type[0]);
        }

        public static Type[] getRandomPool() {
            List<Type> randoms = new ArrayList<>();
            for (Type t : Type.values()) {
                if (t.isMagic()) {
                    randoms.add(t);
                }
            }
            return randoms.toArray(new Type[0]);
        }

    }
}
