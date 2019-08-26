package com.comandante.game.board.logic.invoker;

import com.comandante.game.assetmanagement.BlockTypeBorder;
import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.google.common.collect.Lists;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.comandante.game.assetmanagement.TileSetGameBlockRenderer.imagesNew;

public class CountDownBlockInvoker implements InvokerHarness.Invoker<BufferedImage, UUID> {


    private final List<BufferedImage> bufferedImages;
    private List<UUID> rounds = Lists.newArrayList();
    private int currentImage = 0;
    public static int MAX_ROUNDS_OF_COUNTDOWN = 5;

    public CountDownBlockInvoker(BlockTypeBorder blockTypeBorder) {
        this.bufferedImages = imagesNew.get(blockTypeBorder);
    }

    @Override
    public Optional<BufferedImage> invoke(UUID uuid) {
        if (!rounds.contains(uuid)) {
            rounds.add(uuid);
        }

        int currentCountDownInteger = getCurrentCountDownInteger();
        if (currentImage >= bufferedImages.size()) {
            currentImage = 0;
        }

        if (currentCountDownInteger <= 0) {
            return Optional.empty();
        }
        BufferedImage image = bufferedImages.get(currentImage);
        BufferedImage overlay = TileSetGameBlockRenderer.numbers.get(currentCountDownInteger);

        currentImage++;
        return Optional.of(combineImages(image, overlay));
    }

    @Override
    public int numberRoundsComplete() {
        return rounds.size();
    }

    @Override
    public int maxRounds() {
        return MAX_ROUNDS_OF_COUNTDOWN;
    }

    public int getCurrentCountDownInteger() {
        return (MAX_ROUNDS_OF_COUNTDOWN + 1) - rounds.size();
    }

    private BufferedImage combineImages(BufferedImage image, BufferedImage overlay) {
        Graphics g = null;
        try {
            int w = Math.max(image.getWidth(), overlay.getWidth());
            int h = Math.max(image.getHeight(), overlay.getHeight());
            BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            g = combined.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.drawImage(overlay, 0, 0, null);

            return combined.getSubimage(0, 0, w, h);
        } finally {
            if (g != null) {
                g.dispose();
            }
        }
    }
}
