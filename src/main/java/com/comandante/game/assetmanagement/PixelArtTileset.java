package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlock;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class PixelArtTileset implements TileSet {

    private final BufferedImage spriteSheet;

    public PixelArtTileset(BufferedImage spriteSheet) throws IOException {
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

    public BufferedImage getTopAndLeftBordered() {
        return spriteSheet.getSubimage(0, 8, 8, 8);
    }

    public BufferedImage getLeftBordered() {
        return spriteSheet.getSubimage(0, 16, 8, 8);
    }

    public BufferedImage getBottomLeftBordered() {
        return spriteSheet.getSubimage(0, 24, 8, 8);
    }

    public BufferedImage getBottomBordered() {
        return spriteSheet.getSubimage(8, 24, 8, 8);
    }

    public BufferedImage getBottomAndRightBordered() {
        return spriteSheet.getSubimage(16, 24, 8, 8);
    }

    public BufferedImage getRightBordered() {
        return spriteSheet.getSubimage(16, 16, 8, 8);
    }

    public BufferedImage getTopAndRightBordered() {
        return spriteSheet.getSubimage(16, 8, 8, 8);
    }

    public BufferedImage getTopBordered() {
        return spriteSheet.getSubimage(8, 8, 8, 8);
    }


    private BufferedImage getTopRightBottomBordered() {
        return spriteSheet.getSubimage(32, 0, 8, 8);
    }

    private BufferedImage getTopLeftBottomBordered() {
        return spriteSheet.getSubimage(16, 0, 8, 8);
    }

    private BufferedImage getTopBottomBordered() {
        return spriteSheet.getSubimage(24, 0, 8, 8);
    }

    @Override
    public BufferedImage get(GameBlock.BorderType borderType) {
        switch (borderType) {
            case TOP:
                return getTopBordered();
            case TOP_LEFT:
                return getTopAndLeftBordered();
            case LEFT:
                return getLeftBordered();
            case TOP_RIGHT:
                return getTopAndRightBordered();
            case BOTTOM_LEFT:
                return getBottomLeftBordered();
            case BOTTOM:
                return getBottomBordered();
            case BOTTOM_RIGHT:
                return getBottomAndRightBordered();
            case RIGHT:
                return getRightBordered();
            case TOP_BOTTOM:
                return getTopBottomBordered();
            case TOP_LEFT_BOTTOM:
                return getTopLeftBottomBordered();
            case TOP_RIGHT_BOTTOM:
                return getTopRightBottomBordered();
            case TOP_LEFT_RIGHT:
                return getTopLeftRightBordered();
            case NO_BORDER:
                return getNoBorder();
        }
        throw new RuntimeException("Unable to return a borderTypeTile");
    }

    private BufferedImage getNoBorder() {
            return spriteSheet.getSubimage(8, 16, 8, 8);

    }

    private BufferedImage getTopLeftRightBordered() {
        return rotateClockwise90(getTopLeftBottomBordered());
    }


    public static BufferedImage rotateClockwise90(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage dest = new BufferedImage(h, w, src.getType());
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                dest.setRGB(y, w - x - 1, src.getRGB(x, y));
        return dest;
    }
}
