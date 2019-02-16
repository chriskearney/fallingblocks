package com.comandante.game.board.logic;

import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardCoords;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface GameBlockRenderer {

    void render(TileSetGameBlockRenderer.BlockTypeBorder blockTypeBorder, GameBoardCellEntity gameBoardCellEntity, Graphics g);

    java.util.List<BufferedImage> getImage(GameBlock.Type type);
}
