package com.comadante;

import java.awt.*;

public interface BlockRenderFactory {

    void render(GameBlock.Type type, Graphics g, GameBoardCoords currentCoords);

}
