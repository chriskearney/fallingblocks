package com.comandante.game.board;

import com.comandante.game.assetmanagement.BlockTypeBorder;
import com.comandante.game.board.logic.invoker.CountDownBlockInvoker;
import com.comandante.game.board.logic.invoker.DestructInvoker;
import com.comandante.game.board.logic.invoker.RenderInvoker;
import com.comandante.game.board.logic.GameBlockRenderer;
import com.comandante.game.board.logic.invoker.InvokerHarness;
import com.google.common.collect.Lists;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class GameBlock {

    public final static Random RANDOM = new Random();
    private final static List<GameBlockType> NORMAL_VALUES = Collections.unmodifiableList(Arrays.asList(GameBlockType.getNormalRandomPool()));
    private final static int NORMAL_VALUES_SIZE = NORMAL_VALUES.size();

    private final static List<GameBlockType> RANDOM_MAGIC_VALUES = Collections.unmodifiableList(Arrays.asList(GameBlockType.getRandomMagicPool()));
    private final static int RANDOM_VALUE_SIZE = RANDOM_MAGIC_VALUES.size();

    public final static List<GameBlockType> RANDOM_COUNTDOWN_VALUES = Collections.unmodifiableList(Arrays.asList(GameBlockType.getRandomCountDownPool()));
    public final static int RANDOM_COUNTDOWN_SIZE = RANDOM_COUNTDOWN_VALUES.size();

    private final GameBlockType type;
    private final UUID identifier;
    private boolean resting = false;
    private Optional<GameBlockBorderType> borderType;
    private final Optional<InvokerHarness<BufferedImage, Void>> invocationRound;
    private Optional<InvokerHarness<BufferedImage, Void>> destructionRound;

    private boolean markForDeletion = false;
    private boolean readyForDeletion = false;

    public boolean isReadyForCountDownConversion() {
        return readyForCountDownConversion;
    }

    private boolean readyForCountDownConversion = false;
    private Optional<InvokerHarness<BufferedImage, UUID>> countDownRound = Optional.empty();
    private UUID currentRound;

    public void setCurrentRound(UUID currentRound) {
        this.currentRound = currentRound;
    }

    public GameBlock(GameBlockType type, UUID identifier, InvokerHarness<BufferedImage, Void> invocationRenderRounds) {
        this.type = type;
        this.identifier = identifier;
        this.invocationRound = Optional.of(invocationRenderRounds);
    }

    public GameBlock(GameBlockType type, InvokerHarness<BufferedImage, Void> invocationRenderRounds) {
        this.type = type;
        this.identifier = UUID.randomUUID();
        this.invocationRound = Optional.of(invocationRenderRounds);
    }

    public GameBlock(GameBlockType type) {
        this.type = type;
        this.identifier = UUID.randomUUID();
        this.invocationRound = Optional.empty();
    }

    public GameBlock(GameBlockType type, UUID uuid) {
        this.type = type;
        this.identifier = uuid;
        this.invocationRound = Optional.empty();
    }

    public static GameBlock randomNormalBlock() {
        GameBlockType randomType = NORMAL_VALUES.get(RANDOM.nextInt(NORMAL_VALUES_SIZE));
        return new GameBlock(randomType);
    }

    public static GameBlock randomMagicBlock(GameBlockRenderer gameBlockRenderer) {
        GameBlockType randomType = RANDOM_MAGIC_VALUES.get(RANDOM.nextInt(RANDOM_VALUE_SIZE));
        return blockOfType(randomType, UUID.randomUUID(), gameBlockRenderer);
    }

    public static GameBlock diamondBlock(GameBlockRenderer gameBlockRenderer) {
        return blockOfType(GameBlockType.DIAMOND, UUID.randomUUID(), gameBlockRenderer);
    }

    public static GameBlock newCountDownBlockOfType(GameBlockType type) {
        GameBlock gameBlock = new GameBlock(type);
        gameBlock.setStartCountDown();
        return gameBlock;
    }

    public static GameBlock basicBlockOfType(GameBlockType type, GameBlockRenderer gameBlockRenderer) {
        return new GameBlock(type, UUID.randomUUID());
    }

    public static GameBlock blockOfType(GameBlockType type, UUID identifier, GameBlockRenderer gameBlockRenderer) {
        InvokerHarness<BufferedImage, Void> bufferedImageInvokerHarness = new InvokerHarness<>(3, new RenderInvoker(gameBlockRenderer.getImage(type)), true);
        return new GameBlock(type, identifier, bufferedImageInvokerHarness);
    }

    public void setResting(boolean resting) {
        this.resting = resting;
    }

    public boolean isResting() {
        return resting;
    }

    public Optional<GameBlockBorderType> getBorderType() {
        return borderType;
    }

    public void setBorderType(Optional<GameBlockBorderType> borderType) {
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
            this.destructionRound = Optional.of(new InvokerHarness<BufferedImage, Void>(3, new DestructInvoker(getBlockTypeBorder()), true));
            this.destructionRound.get().setInvokeRoundCompleteHandler(Optional.of(postDeleteCode));
        }
        this.markForDeletion = markForDeletion;
    }

    public void setStartCountDown() {
        Runnable postDeleteCode = () -> setReadyForCountDownConversion(true);
        InvokerHarness<BufferedImage, UUID> bufferedImageUUIDInvokerHarness = new InvokerHarness<>(3, new CountDownBlockInvoker(getBlockTypeBorder()), true);
        bufferedImageUUIDInvokerHarness.setRunNow();
        this.countDownRound = Optional.of(bufferedImageUUIDInvokerHarness);
        this.countDownRound.get().setInvokeRoundCompleteHandler(Optional.of(postDeleteCode));
    }

    public void setReadyForCountDownConversion(boolean readyForCountDownConversion) {
        this.readyForCountDownConversion = readyForCountDownConversion;
    }

    public GameBlockType getType() {
        return type;
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public BlockTypeBorder getBlockTypeBorder() {
        if (borderType == null) {
            return new BlockTypeBorder(getType());
        }
        return new BlockTypeBorder(getType(), borderType.orElse(GameBlockBorderType.NO_BORDER));
    }

    public Optional<BufferedImage> getImageToRender() {
        if (markForDeletion) {
            if (destructionRound.isPresent()) {
                return destructionRound.get().invoker(null);
            }
        }
        if (countDownRound.isPresent()) {
            return countDownRound.get().invoker(currentRound);
        }

        if (!invocationRound.isPresent()) {
            return Optional.empty();
        }
        return invocationRound.get().invoker(null);
    }
}
