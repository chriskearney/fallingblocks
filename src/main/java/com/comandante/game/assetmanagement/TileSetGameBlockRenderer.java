package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoardCoords;
import com.comandante.game.board.logic.GameBlockRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.comandante.game.board.GameBoard.BLOCK_SIZE;

public class TileSetGameBlockRenderer implements GameBlockRenderer {

    private final static Map<GameBlock.Type, java.util.List<BufferedImage>> images = new HashMap<>();

    private final String tileSetName;

    public TileSetGameBlockRenderer(String tileSetName) throws IOException {
        this.tileSetName = tileSetName;
        {
            BufferedImage blueTileSetImg = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/BLUE_TILESET.png"));
            PixelFontTileset blueTileSet = new PixelFontTileset(blueTileSetImg);
            images.put(GameBlock.Type.BLUE, blueTileSet.getStandardBlockFrames());
            images.put(GameBlock.Type.MAGIC_BLUE, blueTileSet.getMagicBlockFrames());
        }

        {
            BufferedImage redTileSetImg = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/RED_TILESET.png"));
            PixelFontTileset redTileSet = new PixelFontTileset(redTileSetImg);
            images.put(GameBlock.Type.RED, redTileSet.getStandardBlockFrames());
            images.put(GameBlock.Type.MAGIC_RED, redTileSet.getMagicBlockFrames());
        }

        {
            BufferedImage yellowTileSetImg = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/YELLOW_TILESET.png"));
            PixelFontTileset yellowTileSet = new PixelFontTileset(yellowTileSetImg);
            images.put(GameBlock.Type.YELLOW, yellowTileSet.getStandardBlockFrames());
            images.put(GameBlock.Type.MAGIC_YELLOW, yellowTileSet.getMagicBlockFrames());
        }

        {
            BufferedImage greenTileSetImg = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/GREEN_TILESET.png"));
            PixelFontTileset greenTileSet = new PixelFontTileset(greenTileSetImg);
            images.put(GameBlock.Type.GREEN, greenTileSet.getStandardBlockFrames());
            images.put(GameBlock.Type.MAGIC_GREEN, greenTileSet.getMagicBlockFrames());
        }

        BufferedImage emptyBlackImage = new BufferedImage(8, 8, 1);
        Graphics2D g2d = emptyBlackImage.createGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, 8, 8);
        g2d.dispose();
        images.put(GameBlock.Type.EMPTY, Collections.singletonList(emptyBlackImage));

    }

    @Override
    public void render(GameBlock.Type type, Graphics g, GameBoardCoords currentCoords) {
        BufferedImage image = images.get(type).get(0);
        g.setColor(Color.black);
        g.fillRect(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        g.drawImage(image, currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
    }

    @Override
    public List<BufferedImage> getImage(GameBlock.Type type) {
        return images.get(type);
    }
}
