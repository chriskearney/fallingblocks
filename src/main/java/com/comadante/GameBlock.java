package com.comadante;

import java.awt.*;
import java.util.Random;

import static java.awt.Color.*;
import static java.awt.Color.cyan;

public class GameBlock {

    private final Color color;
    private final boolean magic;
    private final static Random random = new Random();
    private final static Color[] COLORS = {darkGray, green, blue, red, yellow};


    public GameBlock(Color color, boolean magic) {
        this.color = color;
        this.magic = magic;
    }

    public GameBlock(Color color) {
        this(color, false);
    }

    public Color getColor() {
        return color;
    }

    public boolean isMagic() {
        return magic;
    }

    public static GameBlock random() {
        return new GameBlock(COLORS[random.nextInt(COLORS.length)]);
    }
}
