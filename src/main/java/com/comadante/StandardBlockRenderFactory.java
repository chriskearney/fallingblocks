package com.comadante;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.comadante.GameBoard.BLOCK_SIZE;

public class StandardBlockRenderFactory implements BlockRenderFactory {

    Map<GameBlock.Type, BufferedImage> blockImageTypes = new HashMap<>();

    public StandardBlockRenderFactory() throws IOException {
        blockImageTypes.put(GameBlock.Type.BLUE, getImageFor(GameBlock.Type.BLUE));
        blockImageTypes.put(GameBlock.Type.GREEN, getImageFor(GameBlock.Type.GREEN));
        blockImageTypes.put(GameBlock.Type.YELLOW, getImageFor(GameBlock.Type.YELLOW));
        blockImageTypes.put(GameBlock.Type.RED, getImageFor(GameBlock.Type.RED));
        blockImageTypes.put(GameBlock.Type.EMPTY, getImageFor(GameBlock.Type.EMPTY));
        blockImageTypes.put(GameBlock.Type.MAGIC_BLUE, getImageFor(GameBlock.Type.MAGIC_BLUE));
        blockImageTypes.put(GameBlock.Type.MAGIC_GREEN, getImageFor(GameBlock.Type.MAGIC_GREEN));
        blockImageTypes.put(GameBlock.Type.MAGIC_YELLOW, getImageFor(GameBlock.Type.MAGIC_YELLOW));
        blockImageTypes.put(GameBlock.Type.MAGIC_RED, getImageFor(GameBlock.Type.MAGIC_RED));
    }

    private BufferedImage getImageFor(GameBlock.Type type) throws IOException {
        return ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/" + type + ".png"));
    }

    @Override
    public void render(GameBlock.Type type, Graphics g, GameBoardCoords currentCoords) {
        BufferedImage image = blockImageTypes.get(type);
        g.setColor(Color.black);
        g.fillRect(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        g.drawImage(image, currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
    }
}
