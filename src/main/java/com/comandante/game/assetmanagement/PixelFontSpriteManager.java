package com.comandante.game.assetmanagement;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PixelFontSpriteManager {

    public final static Map<PixelFont.Type, PixelFont> PIXEL_FONT_SPRITE_MAP = new HashMap<>();

    public PixelFontSpriteManager() throws IOException {
        for (PixelFont.Type type: PixelFont.Type.values()) {
            PIXEL_FONT_SPRITE_MAP.put(type, new PixelFont(type));
        }
    }

    public PixelFont getPixelFontSprite(PixelFont.Type type) {
        return PIXEL_FONT_SPRITE_MAP.get(type);
    }


    public BufferedImage get(PixelFont.Type type, int asciiCode) {
        return PIXEL_FONT_SPRITE_MAP.get(type).get(asciiCode);
    }

    public List<BufferedImage> get(PixelFont.Type type, String s) {
        List<BufferedImage> images = new ArrayList<>();
        char[] chars = s.toCharArray();
        for (char c: chars) {
            images.add(PIXEL_FONT_SPRITE_MAP.get(type).get(c));
        }
        return images;
    }
}
