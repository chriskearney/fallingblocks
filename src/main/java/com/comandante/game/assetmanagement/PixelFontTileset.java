package com.comandante.game.assetmanagement;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class PixelFontTileset implements TileSet {

    private final BufferedImage spriteSheet;

    public PixelFontTileset(BufferedImage spriteSheet) throws IOException {
        this.spriteSheet = spriteSheet;
    }

    @Override
    public List<BufferedImage> getStandardBlockFrames() {
        return Collections.singletonList(spriteSheet.getSubimage(8, 0, 8, 8));
    }

    @Override
    public List<BufferedImage> getMagicBlockFrames() {
        return Collections.singletonList(spriteSheet.getSubimage(32, 16, 8, 8));
    }

    @Override
    public List<BufferedImage> getDiamondBlockFrames() {
        return Collections.singletonList(spriteSheet.getSubimage(40, 0, 8, 8));
    }

    @Override
    public List<BufferedImage> getCountDownBlockOne() {
        return null;
    }

    @Override
    public List<BufferedImage> getCountDownBlockTwo() {
        return null;
    }

    @Override
    public List<BufferedImage> getCountDownBlockThree() {
        return null;
    }

    @Override
    public List<BufferedImage> getCountDownBlockFour() {
        return null;
    }

    @Override
    public List<BufferedImage> getCountDownBlockFive() {
        return null;
    }
}
