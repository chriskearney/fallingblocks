package com.comandante.game.assetmanagement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PixelFont implements TextBoardFont {

    private final Map<Integer, BufferedImage> characterImages = new HashMap<>();

    public PixelFont(Type type) throws IOException {
        BufferedImage fontImage = ImageIO.read(TileSetGameBlockRenderer.class.getResourceAsStream("/fonts/PixelFont/gnsh-bitmapfont-" + type.toString().toLowerCase() + ".png"));

        int asciiCode = 32;

        int x = 0;
        int y = 0;
        for (int i = 0; i < 20; i++) {
            characterImages.put(asciiCode, fontImage.getSubimage(x,y,5, 12));
            asciiCode++;
            x += 5;
        }

        y += 12;
        x = 0;
        for (int i = 0; i < 20; i++) {
            characterImages.put(asciiCode, fontImage.getSubimage(x,y,5, 12));
            asciiCode++;
            x += 5;
        }

        y += 12;
        x = 0;
        for (int i = 0; i < 20; i++) {
            characterImages.put(asciiCode, fontImage.getSubimage(x,y,5, 12));
            asciiCode++;
            x += 5;
        }

        y += 12;
        x = 0;
        for (int i = 0; i < 20; i++) {
            characterImages.put(asciiCode, fontImage.getSubimage(x,y,5, 12));
            asciiCode++;
            x += 5;
        }

        y += 12;
        x = 0;
        for (int i = 0; i < 15; i++) {
            characterImages.put(asciiCode, fontImage.getSubimage(x,y,5, 12));
            asciiCode++;
            x += 5;
        }
    }


    @Override
    public BufferedImage get(int c) {
        return characterImages.get(c);
    }

    public enum Type {
        COLOUR1,
        COLOUR2,
        COLOUR3,
        COLOUR4,
        COLOUR5,
        COLOUR6,
        COLOUR7,
        COLOUR8,
        COLOUR9;
    }
}
