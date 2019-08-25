package com.comandante.game.assetmanagement;

import com.comandante.game.board.InvocationRound;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

public class RenderInvoker implements InvocationRound.Invoker<BufferedImage, Void> {

    private final List<BufferedImage> bufferedImages;
    private int currentImage = 0;
    private int numberRoundsComplete = 0;

    public RenderInvoker(List<BufferedImage> bufferedImages) {
        this.bufferedImages = bufferedImages;
    }

    @Override
    public Optional<BufferedImage> invoke(Void nope) {
        if (currentImage >= bufferedImages.size()) {
            currentImage = 0;
            numberRoundsComplete ++;
        }
        BufferedImage image = bufferedImages.get(currentImage);
        currentImage++;
        return Optional.of(image);
    }

    @Override
    public int numberRoundsComplete() {
        return numberRoundsComplete;
    }
}
