package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardCoords;
import com.comandante.game.board.logic.GameBlockRenderer;
import com.google.common.collect.Lists;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.comandante.game.board.GameBoardData.BLOCK_SIZE;

public class TileSetGameBlockRenderer implements GameBlockRenderer {

    public final static Map<BlockTypeBorder, java.util.List<BufferedImage>> imagesNew = new HashMap<>();

    private final String tileSetName;

    public TileSetGameBlockRenderer(String tileSetName) throws IOException {
        this.tileSetName = tileSetName;
        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "blue.png");
            if (ifExists.isPresent()) {
                BufferedImage blueTileSetImg = ifExists.get();
                PixelArtTileset blueTileSet = new PixelArtTileset(blueTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.BLUE), blueTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.BLUE, borderType), Collections.singletonList(blueTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_BLUE), blueTileSet.getMagicBlockFrames());
            }

        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "cyan.png");
            if (ifExists.isPresent()) {
                BufferedImage cyanTileSetImg = ifExists.get();
                PixelArtTileset cyanTileSet = new PixelArtTileset(cyanTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.CYAN), cyanTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.CYAN, borderType), Collections.singletonList(cyanTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_CYAN), cyanTileSet.getMagicBlockFrames());
            }

        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "gold.png");
            if (ifExists.isPresent()) {
                BufferedImage goldTileSetImg = ifExists.get();
                PixelArtTileset goldTileSet = new PixelArtTileset(goldTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.GOLD), goldTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.GOLD, borderType), Collections.singletonList(goldTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_GOLD), goldTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "magenta.png");
            if (ifExists.isPresent()) {
                BufferedImage magentaTileSetImg = ifExists.get();
                PixelArtTileset magentaTileSet = new PixelArtTileset(magentaTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGENTA), magentaTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGENTA, borderType), Collections.singletonList(magentaTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_MAGENTA), magentaTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "orange.png");
            if (ifExists.isPresent()) {
                BufferedImage orangeTileSetImg = ifExists.get();
                PixelArtTileset orangeTileSet = new PixelArtTileset(orangeTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.ORANGE), orangeTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.ORANGE, borderType), Collections.singletonList(orangeTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_ORANGE), orangeTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "purple.png");
            if (ifExists.isPresent()) {
                BufferedImage purpleTileSetImg = ifExists.get();
                PixelArtTileset purpleTileSet = new PixelArtTileset(purpleTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.PURPLE), purpleTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.PURPLE, borderType), Collections.singletonList(purpleTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_PURPLE), purpleTileSet.getMagicBlockFrames());
            }


        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "red.png");
            if (ifExists.isPresent()) {
                BufferedImage redTileSetImg = ifExists.get();
                PixelArtTileset redTileSet = new PixelArtTileset(redTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.RED), redTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.RED, borderType), Collections.singletonList(redTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_RED), redTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "yellow.png");
            if (ifExists.isPresent()) {
                BufferedImage yellowTileSetImg = ifExists.get();
                PixelArtTileset yellowTileSet = new PixelArtTileset(yellowTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.YELLOW), yellowTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.YELLOW, borderType), Collections.singletonList(yellowTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_YELLOW), yellowTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "green.png");
            if (ifExists.isPresent()) {
                BufferedImage greenTileSetImg = ifExists.get();
                PixelArtTileset greenTileSet = new PixelArtTileset(greenTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.GREEN), greenTileSet.getStandardBlockFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.GREEN, borderType), Collections.singletonList(greenTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_GREEN), greenTileSet.getMagicBlockFrames());
            }
        }

        {
            BufferedImage diamondTileSetImage = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/diamond.png"));
            BufferedImage frame1 = diamondTileSetImage.getSubimage(0, 0, 8, 8);
            BufferedImage frame2 = diamondTileSetImage.getSubimage(8, 0, 8, 8);
            BufferedImage frame3 = diamondTileSetImage.getSubimage(16, 0, 8, 8);
            BufferedImage frame4 = diamondTileSetImage.getSubimage(24, 0, 8, 8);
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.DIAMOND), Lists.newArrayList(frame1, frame2, frame3, frame4));
        }

        Graphics2D g2d = emptyBlackImage.createGraphics();
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, 8, 8);
        g2d.dispose();
        imagesNew.put(new BlockTypeBorder(GameBlock.Type.EMPTY), Collections.singletonList(GameBlockRenderer.emptyBlackImage));

    }

    private static Optional<BufferedImage> getIfExists(String tileSetName, String fileName) {
        try {
            BufferedImage read = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/" + fileName));
            return Optional.of(read);
        } catch (Exception e) {

        }
        return Optional.empty();
    }

    @Override
    public void render(BlockTypeBorder blockTypeBorder, GameBoardCellEntity gameBoardCellEntity, Graphics g) {
        GameBoardCoords currentCoords = gameBoardCellEntity.getGameBoardCoords();

        Optional<BufferedImage> imageToRender = Optional.empty();
        if (gameBoardCellEntity.getGameBlock().isPresent()) {
            imageToRender = gameBoardCellEntity.getGameBlock().get().getImageToRender();
        }
        if (!imageToRender.isPresent()) {
            List<BufferedImage> bufferedImages = imagesNew.get(blockTypeBorder);
            if (bufferedImages == null) {
                return;
            }
            imageToRender = Optional.ofNullable(bufferedImages.get(0));
        }

        if (!imageToRender.isPresent()) {
            return;
        }

        g.setColor(Color.black);
        g.fillRect(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        g.drawImage(imageToRender.get(), currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
        gameBoardCellEntity.setRectangle(new Rectangle(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE));
        //https://stackoverflow.com/questions/34461186/how-to-detect-mouse-hover-on-an-image-drawn-from-paintcomponents-drawimage-m
    }


    @Override
    public List<BufferedImage> getImage(GameBlock.Type type) {
        return imagesNew.get(new BlockTypeBorder(type));
    }


}
