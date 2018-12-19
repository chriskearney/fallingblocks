package com.comandante.game.board.logic;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoardCoords;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface GameBlockRenderer {

    void render(GameBlock.Type type, Graphics g, GameBoardCoords currentCoords);

    java.util.List<BufferedImage> getImage(GameBlock.Type type);
}
