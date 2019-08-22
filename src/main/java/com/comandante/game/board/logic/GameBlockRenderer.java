package com.comandante.game.board.logic;

import com.comandante.game.assetmanagement.BlockTypeBorder;
import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoardCellEntity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public interface GameBlockRenderer {

    BufferedImage emptyBlackImage = new BufferedImage(8, 8, 6);

    void render(BlockTypeBorder blockTypeBorder, GameBoardCellEntity gameBoardCellEntity, Graphics g);

    java.util.List<BufferedImage> getImage(GameBlock.Type type);
}
