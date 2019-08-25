package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlockBorderType;

import java.awt.image.BufferedImage;
import java.util.List;

public interface TileSet {

    List<BufferedImage> getStandardBlockFrames();

    List<BufferedImage> getMagicBlockFrames();

    List<BufferedImage> getCountDownFrames();

    BufferedImage get(GameBlockBorderType borderType);
}
