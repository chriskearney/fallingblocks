package com.comandante.game.board;

import com.comandante.game.assetmanagement.BlockTypeBorder;
import com.comandante.game.assetmanagement.DestructInvoker;
import com.comandante.game.assetmanagement.RenderInvoker;
import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.logic.GameBlockRenderer;
import com.google.common.collect.Sets;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
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
    private final Optional<InvocationRound<BufferedImage, Void>> invocationRound;
    private Optional<InvocationRound<BufferedImage, Void>> destructionRound;

    private boolean markForDeletion = false;
    private boolean readyForDeletion = false;


    public GameBlock(Type type, InvocationRound<BufferedImage, Void> invocationRenderRounds) {
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

    public static GameBlock randomNormalBlock() {
        Type randomType = NORMAL_VALUES.get(RANDOM.nextInt(NORMAL_VALUES_SIZE));
        return new GameBlock(randomType);
    }

    public static GameBlock randomMagicBlock(GameBlockRenderer gameBlockRenderer) {
        Type randomType = RANDOM_VALUES.get(RANDOM.nextInt(RANDOM_VALUE_SIZE));
        InvocationRound<BufferedImage, Void> bufferedImageInvocationRound = new InvocationRound<>(3, new RenderInvoker(gameBlockRenderer.getImage(randomType)), true);
        return new GameBlock(randomType, bufferedImageInvocationRound);
    }

    public static GameBlock diamondBlock(GameBlockRenderer gameBlockRenderer) {
        InvocationRound<BufferedImage, Void> bufferedImageInvocationRound = new InvocationRound<>(3, new RenderInvoker(gameBlockRenderer.getImage(Type.DIAMOND)), true);
        return new GameBlock(Type.DIAMOND, bufferedImageInvocationRound);
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

    public boolean isMarkForDeletion() {
        return markForDeletion;
    }

    public boolean isReadyForDeletion() {
        return readyForDeletion;
    }

    public void setReadyForDeletion(boolean readyForDeletion) {
        this.readyForDeletion = readyForDeletion;
    }

    public void setMarkForDeletion(boolean markForDeletion) {
        if (markForDeletion) {
            Runnable postDeleteCode = () -> setReadyForDeletion(true);
            this.destructionRound = Optional.of(new InvocationRound<BufferedImage, Void>(3, new DestructInvoker(getBlockTypeBorder()), true));
            this.destructionRound.get().setInvokeRoundCompleteHandler(Optional.of(postDeleteCode));
        }
        this.markForDeletion = markForDeletion;
    }

    public Type getType() {
        return type;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public BlockTypeBorder getBlockTypeBorder() {
        if (borderType == null) {
            return new BlockTypeBorder(getType());
        }
        return new BlockTypeBorder(getType(), borderType.orElse(BorderType.NO_BORDER));
    }

    public Optional<BufferedImage> getImageToRender() {
        if (markForDeletion) {
            if (destructionRound.isPresent()) {
                return destructionRound.get().invoker(null);
            }
        }
        if (!invocationRound.isPresent()) {
            return Optional.empty();
        }
        return invocationRound.get().invoker(null);
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

        private Optional<Type> magicRelated;
        private Optional<Type> countDownRelated;


        Type(Optional<Type> magicRelated, Optional<Type> countDownRelated) {
            this.magicRelated = magicRelated;
            this.countDownRelated = countDownRelated;
        }

        Type(Optional<Type> magicRelated) {
            this.magicRelated = magicRelated;
            this.countDownRelated = Optional.empty();
        }

        Type() {
            this.magicRelated = Optional.empty();
            this.countDownRelated = Optional.empty();
        }

        public boolean isMagic() {
            return magicRelated.isPresent();
        }

        public Optional<Type> getRelated() {
            return magicRelated;
        }

        public Optional<Type> getCountDownRelated() {
            return countDownRelated;
        }

        public static Type[] getNormalRandomPool() {
            Set<Type> resolvedTypes = Sets.newHashSet();
            for (Map.Entry<BlockTypeBorder, List<BufferedImage>> next : TileSetGameBlockRenderer.imagesNew.entrySet()) {
                resolvedTypes.add(next.getKey().getType());
            }
            List<Type> collect = resolvedTypes.stream().filter(type -> !type.isMagic() && !type.getCountDownRelated().isPresent()).collect(Collectors.toList());
            collect.remove(EMPTY);
            collect.remove(DIAMOND);
            return collect.toArray(new Type[0]);
        }

        public static Type[] getRandomPool() {
            Set<Type> resolvedTypes = Sets.newHashSet();
            for (Map.Entry<BlockTypeBorder, List<BufferedImage>> next : TileSetGameBlockRenderer.imagesNew.entrySet()) {
                resolvedTypes.add(next.getKey().getType());
            }

            List<Type> collect = resolvedTypes.stream().filter(type -> type.isMagic()).collect(Collectors.toList());
            return collect.toArray(new Type[0]);
        }

    }
}
