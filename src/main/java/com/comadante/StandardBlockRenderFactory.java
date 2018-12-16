package com.comadante;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.comadante.GameBoard.BLOCK_SIZE;

public class StandardBlockRenderFactory implements BlockRenderFactory {

    Map<GameBlock.Type, BufferedImage> typeBufferedImageMap = new HashMap<>();

    public StandardBlockRenderFactory() throws IOException {
        BufferedImage blueBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/BlueBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.BLUE, blueBlock);

        BufferedImage greenBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/GreenBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.GREEN, greenBlock);

        BufferedImage yellowBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/YellowBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.YELLOW, yellowBlock);

        BufferedImage redBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/RedBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.RED, redBlock);

        BufferedImage magicBlueBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/MagicBlueBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.MAGIC_BLUE, magicBlueBlock);

        BufferedImage magicGreenBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/MagicGreenBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.MAGIC_GREEN, magicGreenBlock);

        BufferedImage magicYellowBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/MagicYellowBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.MAGIC_YELLOW, magicYellowBlock);

        BufferedImage magicRedBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/MagicRedBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.MAGIC_RED, magicRedBlock);

        BufferedImage emptyBlock = ImageIO.read(StandardBlockRenderFactory.class.getResourceAsStream("/tilesets/GlossyEyeBalls/EmptyBlock256x256.png"));
        typeBufferedImageMap.put(GameBlock.Type.EMPTY, emptyBlock);
    }

    @Override
    public void render(GameBlock.Type type, Graphics g, GameBoardCoords currentCoords) {
            BufferedImage image = typeBufferedImageMap.get(type);
            g.setColor(Color.black);
            g.fillRect(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            g.drawImage(image, currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
    }
}
