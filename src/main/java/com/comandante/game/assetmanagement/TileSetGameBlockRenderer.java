package com.comandante.game.assetmanagement;

import com.comandante.game.board.*;
import com.comandante.game.board.logic.GameBlockRenderer;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.imageio.ImageIO;
import java.awt.Graphics;
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
    public final static Map<Integer, BufferedImage> numbers = Maps.newHashMap();

    private final String tileSetName;

    public TileSetGameBlockRenderer(String tileSetName) throws IOException {
        this.tileSetName = tileSetName;
        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "blue.png");
            if (ifExists.isPresent()) {
                BufferedImage blueTileSetImg = ifExists.get();
                PixelArtTileset blueTileSet = new PixelArtTileset(blueTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.BLUE), blueTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_BLUE), blueTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.BLUE, borderType), Collections.singletonList(blueTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_BLUE), blueTileSet.getMagicBlockFrames());
            }

        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "cyan.png");
            if (ifExists.isPresent()) {
                BufferedImage cyanTileSetImg = ifExists.get();
                PixelArtTileset cyanTileSet = new PixelArtTileset(cyanTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.CYAN), cyanTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_CYAN), cyanTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.CYAN, borderType), Collections.singletonList(cyanTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_CYAN), cyanTileSet.getMagicBlockFrames());
            }

        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "gold.png");
            if (ifExists.isPresent()) {
                BufferedImage goldTileSetImg = ifExists.get();
                PixelArtTileset goldTileSet = new PixelArtTileset(goldTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.GOLD), goldTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_GOLD), goldTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.GOLD, borderType), Collections.singletonList(goldTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_GOLD), goldTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "magenta.png");
            if (ifExists.isPresent()) {
                BufferedImage magentaTileSetImg = ifExists.get();
                PixelArtTileset magentaTileSet = new PixelArtTileset(magentaTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGENTA), magentaTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_MAGENTA), magentaTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.MAGENTA, borderType), Collections.singletonList(magentaTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_MAGENTA), magentaTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "orange.png");
            if (ifExists.isPresent()) {
                BufferedImage orangeTileSetImg = ifExists.get();
                PixelArtTileset orangeTileSet = new PixelArtTileset(orangeTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.ORANGE), orangeTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_ORANGE), orangeTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.ORANGE, borderType), Collections.singletonList(orangeTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_ORANGE), orangeTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "purple.png");
            if (ifExists.isPresent()) {
                BufferedImage purpleTileSetImg = ifExists.get();
                PixelArtTileset purpleTileSet = new PixelArtTileset(purpleTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.PURPLE), purpleTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_PURPLE), purpleTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.PURPLE, borderType), Collections.singletonList(purpleTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_PURPLE), purpleTileSet.getMagicBlockFrames());
            }


        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "red.png");
            if (ifExists.isPresent()) {
                BufferedImage redTileSetImg = ifExists.get();
                PixelArtTileset redTileSet = new PixelArtTileset(redTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.RED), redTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_RED), redTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.RED, borderType), Collections.singletonList(redTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_RED), redTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "yellow.png");
            if (ifExists.isPresent()) {
                BufferedImage yellowTileSetImg = ifExists.get();
                PixelArtTileset yellowTileSet = new PixelArtTileset(yellowTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.YELLOW), yellowTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_YELLOW), yellowTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.YELLOW, borderType), Collections.singletonList(yellowTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_YELLOW), yellowTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "green.png");
            if (ifExists.isPresent()) {
                BufferedImage greenTileSetImg = ifExists.get();
                PixelArtTileset greenTileSet = new PixelArtTileset(greenTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlockType.GREEN), greenTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlockType.COUNTDOWN_GREEN), greenTileSet.getCountDownFrames());
                GameBlockBorderType[] borderTypes = GameBlockBorderType.values();
                for (GameBlockBorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlockType.GREEN, borderType), Collections.singletonList(greenTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlockType.MAGIC_GREEN), greenTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "numbers.png");
            if (ifExists.isPresent()) {
                BufferedImage five = ifExists.get().getSubimage(0, 0, 8, 8);
                BufferedImage four = ifExists.get().getSubimage(8, 0, 8, 8);
                BufferedImage three = ifExists.get().getSubimage(16, 0, 8, 8);
                BufferedImage two = ifExists.get().getSubimage(24, 0, 8, 8);
                BufferedImage one = ifExists.get().getSubimage(32, 0, 8, 8);
                numbers.put(1, one);
                numbers.put(2, two);
                numbers.put(3, three);
                numbers.put(4, four);
                numbers.put(5, five);
            }
        }

        {
            BufferedImage diamondTileSetImage = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/diamond.png"));
            BufferedImage frame1 = diamondTileSetImage.getSubimage(0, 0, 8, 8);
            BufferedImage frame2 = diamondTileSetImage.getSubimage(8, 0, 8, 8);
            BufferedImage frame3 = diamondTileSetImage.getSubimage(16, 0, 8, 8);
            BufferedImage frame4 = diamondTileSetImage.getSubimage(24, 0, 8, 8);
            BufferedImage frame5 = diamondTileSetImage.getSubimage(32, 0, 8, 8);
            BufferedImage frame6 = diamondTileSetImage.getSubimage(40, 0, 8, 8);
            BufferedImage frame7 = diamondTileSetImage.getSubimage(48, 0, 8, 8);
            BufferedImage frame8 = diamondTileSetImage.getSubimage(56, 0, 8, 8);
            imagesNew.put(new BlockTypeBorder(GameBlockType.DIAMOND), Lists.newArrayList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8));
        }

    }

    private static Optional<BufferedImage> getIfExists(String tileSetName, String fileName) {
        try {
            BufferedImage read = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/tilesets/" + tileSetName + "/" + fileName));
            if (read != null) {
                System.out.println(read.getType());
            }
            return Optional.of(read);
        } catch (Exception e) {

        }
        return Optional.empty();
    }

    @Override
    public void render(GameBoardCellEntity[][] cellEntities, BlockTypeBorder blockTypeBorder, GameBoardCellEntity gameBoardCellEntity, Graphics g) {
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

        if (blockTypeBorder.getType().equals(GameBlockType.EMPTY)) {
            g.clearRect(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        } else {
            g.drawImage(imageToRender.get(), currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
            gameBoardCellEntity.setRectangle(new Rectangle(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE));
        }
    }


    @Override
    public List<BufferedImage> getImage(GameBlockType type) {
        return imagesNew.get(new BlockTypeBorder(type));
    }


}
