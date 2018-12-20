package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlock;

import java.awt.image.BufferedImage;
import java.util.List;

public interface TileSet {

    List<BufferedImage> getStandardBlockFrames();

    List<BufferedImage> getMagicBlockFrames();

    List<BufferedImage> getDiamondBlockFrames();

    List<BufferedImage> getCountDownBlockOne();

    List<BufferedImage> getCountDownBlockTwo();

    List<BufferedImage> getCountDownBlockThree();

    List<BufferedImage> getCountDownBlockFour();

    List<BufferedImage> getCountDownBlockFive();

    BufferedImage get(GameBlock.BorderType borderType);

    enum Type {
        GREEN,
        YELLOW,
        RED,
        BLUE
    }
}
