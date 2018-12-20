package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlock;

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

    @Override
    public BufferedImage getTopAndLeftBordered() {
        return spriteSheet.getSubimage(8, 0, 8, 8);
    }

    public BufferedImage getLeftBordered() {
        return spriteSheet.getSubimage(0, 16, 8, 8);
    }

    public BufferedImage getLeftAndBottomBordered() {
        return spriteSheet.getSubimage(0, 24, 8, 8);
    }

    public BufferedImage getBottomBordered() {
        return spriteSheet.getSubimage(8, 24, 8, 8);
    }

    public BufferedImage getBottomAndRightBordered() {
        return spriteSheet.getSubimage(24, 24, 8, 8);
    }

    public BufferedImage getRightBordered() {
        return spriteSheet.getSubimage(24, 16, 8, 8);
    }

    public BufferedImage getTopAndRightBordered() {
        return spriteSheet.getSubimage(24, 8, 8, 8);
    }

    public BufferedImage getTopBordered() {
        return spriteSheet.getSubimage(8, 8, 8, 8);
    }

    @Override
    public BufferedImage get(GameBlock.BorderType borderType) {
        switch (borderType) {
            case TOP:
                return getTopBordered();
            case TOP_LEFT:
                return getTopAndLeftBordered();
            case LEFT:
                getLeftBordered();
            case TOP_RIGHT:
                return getTopAndRightBordered();
            case LEFT_BOTTOM:
                getLeftAndBottomBordered();
            case BOTTOM:
                getBottomBordered();
            case BOTTOM_RIGHT:
                getBottomAndRightBordered();
            case RIGHT:
                getRightBordered();
        }
    }
}
