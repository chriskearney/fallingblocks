package com.comandante.game.assetmanagement;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardCoords;
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
    private final static Map<Integer, BufferedImage> numbers = Maps.newHashMap();

    private final String tileSetName;

    public TileSetGameBlockRenderer(String tileSetName) throws IOException {
        this.tileSetName = tileSetName;
        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "blue.png");
            if (ifExists.isPresent()) {
                BufferedImage blueTileSetImg = ifExists.get();
                PixelArtTileset blueTileSet = new PixelArtTileset(blueTileSetImg);
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.BLUE), blueTileSet.getStandardBlockFrames());
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_BLUE), blueTileSet.getCountDownFrames());
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
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_CYAN), cyanTileSet.getCountDownFrames());
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
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_GOLD), goldTileSet.getCountDownFrames());
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
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_MAGENTA), magentaTileSet.getCountDownFrames());
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
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_ORANGE), orangeTileSet.getCountDownFrames());
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
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_PURPLE), purpleTileSet.getCountDownFrames());
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
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_RED), redTileSet.getCountDownFrames());
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
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_YELLOW), yellowTileSet.getCountDownFrames());
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
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.COUNTDOWN_GREEN), greenTileSet.getCountDownFrames());
                GameBlock.BorderType[] borderTypes = GameBlock.BorderType.values();
                for (GameBlock.BorderType borderType : borderTypes) {
                    imagesNew.put(new BlockTypeBorder(GameBlock.Type.GREEN, borderType), Collections.singletonList(greenTileSet.get(borderType)));
                }
                imagesNew.put(new BlockTypeBorder(GameBlock.Type.MAGIC_GREEN), greenTileSet.getMagicBlockFrames());
            }
        }

        {
            Optional<BufferedImage> ifExists = getIfExists(tileSetName, "numbers.png");
            if (ifExists.isPresent()) {
                BufferedImage one = ifExists.get().getSubimage(0, 0, 8, 8);
                BufferedImage two = ifExists.get().getSubimage(8, 0, 8, 8);
                BufferedImage three = ifExists.get().getSubimage(16, 0, 8, 8);
                BufferedImage four = ifExists.get().getSubimage(24, 0, 8, 8);
                BufferedImage five = ifExists.get().getSubimage(32, 0, 8, 8);
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
            imagesNew.put(new BlockTypeBorder(GameBlock.Type.DIAMOND), Lists.newArrayList(frame1, frame2, frame3, frame4, frame5, frame6, frame7, frame8));
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

        Optional<BufferedImage> numberToRender = Optional.empty();
        Optional<BufferedImage> imageToRender = Optional.empty();
        if (gameBoardCellEntity.getGameBlock().isPresent()) {
            imageToRender = gameBoardCellEntity.getGameBlock().get().getImageToRender();
            if (gameBoardCellEntity.getGameBlock().get().getType().getCountDownRelated().isPresent()) {
                int currentCountDownInteger = gameBoardCellEntity.getGameBlock().get().getCurrentCountDownInteger();
                if (currentCountDownInteger == 0) {
                    GameBlock gameBlock = GameBlock.blockOfType(gameBoardCellEntity.getType().getCountDownRelated().get(), this);
                    cellEntities[gameBoardCellEntity.getGameBoardCoords().i][gameBoardCellEntity.getGameBoardCoords().j] = new GameBoardCellEntity(gameBoardCellEntity.getId(), gameBoardCellEntity.getGameBoardCoords(), gameBlock);;
                    gameBoardCellEntity = new GameBoardCellEntity(gameBlock);
                } else {
                    numberToRender = Optional.ofNullable(numbers.get(currentCountDownInteger));
                }
            }
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

        if (blockTypeBorder.getType().equals(GameBlock.Type.EMPTY)) {
            g.clearRect(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        } else {
            g.drawImage(imageToRender.get(), currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
            if (numberToRender.isPresent()) {
                g.drawImage(numberToRender.get(), currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);
            }
            gameBoardCellEntity.setRectangle(new Rectangle(currentCoords.i * BLOCK_SIZE, currentCoords.j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE));
        }
    }


    @Override
    public List<BufferedImage> getImage(GameBlock.Type type) {
        return imagesNew.get(new BlockTypeBorder(type));
    }


}
