package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardCoords;
import com.comandante.game.board.logic.GameBlockRenderer;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static com.comandante.game.board.GameBoardData.BLOCK_SIZE;

public class TileSetGameBlockRenderer implements GameBlockRenderer {

    private final static Map<BlockTypeBorder, java.util.List<BufferedImage>> imagesNew = new HashMap<>();

    private final String tileSetName;

    public TileSetGameBlockRenderer(String tileSetName) throws IOException {
        this.tileSetName = tileSetName;
        {
            BufferedImage blueTileSetImg = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/BLUE_TILESET.png"));
            PixelArtTileset blueTileSet = new PixelArtTileset(blueTileSetImg);
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.BLUE), blueTileSet.getStandardBlockFrames());
            GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
            for (GameBlock.BorderType borderType: borderTypes) {
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.BLUE, borderType), Collections.singletonList(blueTileSet.get(borderType)));
            }
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_BLUE), blueTileSet.getMagicBlockFrames());

        }

        {
            BufferedImage redTileSetImg = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/RED_TILESET.png"));
            PixelArtTileset redTileSet = new PixelArtTileset(redTileSetImg);
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.RED), redTileSet.getStandardBlockFrames());
            GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
            for (GameBlock.BorderType borderType: borderTypes) {
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.RED, borderType), Collections.singletonList(redTileSet.get(borderType)));
            }
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_RED), redTileSet.getMagicBlockFrames());

        }

        {
            BufferedImage yellowTileSetImg = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/YELLOW_TILESET.png"));
            PixelArtTileset yellowTileSet = new PixelArtTileset(yellowTileSetImg);
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.YELLOW), yellowTileSet.getStandardBlockFrames());
            GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
            for (GameBlock.BorderType borderType: borderTypes) {
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.YELLOW, borderType), Collections.singletonList(yellowTileSet.get(borderType)));
            }
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_YELLOW), yellowTileSet.getMagicBlockFrames());
        }

        {
            BufferedImage greenTileSetImg = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/GREEN_TILESET.png"));
            PixelArtTileset greenTileSet = new PixelArtTileset(greenTileSetImg);
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.GREEN), greenTileSet.getStandardBlockFrames());
            GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
            for (GameBlock.BorderType borderType: borderTypes) {
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.GREEN, borderType), Collections.singletonList(greenTileSet.get(borderType)));
            }
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_GREEN), greenTileSet.getMagicBlockFrames());


        }

        BufferedImage emptyBlackImage = new BufferedImage(8, 8, 1);
        Graphics2D g2d = emptyBlackImage.createGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, 8, 8);
        g2d.dispose();
        imagesNew.put(new BlockTypeBorder(GameBlock.Type.EMPTY) , Collections.singletonList(emptyBlackImage));

    }

    @Override
    public void render(BlockTypeBorder blockTypeBorder, GameBoardCellEntity gameBoardCellEntity, Graphics g) {
        GameBoardCoords currentCoords = gameBoardCellEntity.getGameBoardCoords();
        List<BufferedImage> bufferedImages = imagesNew.get(blockTypeBorder);
        if (bufferedImages == null) {
            System.out.println("WHAT");
        }
        BufferedImage image = bufferedImages.get(0);
        g.setColor(Color.black);
        g.fillRect(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        g.drawImage(image, currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
        gameBoardCellEntity.setRectangle(new Rectangle(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE));
        //https://stackoverflow.com/questions/34461186/how-to-detect-mouse-hover-on-an-image-drawn-from-paintcomponents-drawimage-m
    }



    @Override
    public List<BufferedImage> getImage(GameBlock.Type type) {
        return imagesNew.get(new BlockTypeBorder(type));
    }

    public static class BlockTypeBorder {
        private final GameBlock.Type type;
        private final Optional<GameBlock.BorderType> borderType;

        public BlockTypeBorder(GameBlock.Type type, GameBlock.BorderType borderType) {
            this.type = type;
            this.borderType = Optional.ofNullable(borderType);
        }

        public BlockTypeBorder(GameBlock.Type type) {
            this(type, null);
        }


        public GameBlock.Type getType() {
            return type;
        }

        public Optional<GameBlock.BorderType> getBorderType() {
            return borderType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BlockTypeBorder that = (BlockTypeBorder) o;
            return type == that.type &&
                    Objects.equals(borderType, that.borderType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, borderType);
        }
    }
}
