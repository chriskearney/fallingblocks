package com.comandante.game.ui;

import com.comandante.game.textboard.TextBoard;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreenPanel extends JPanel {

    private final TextBoard textBoard;
    private final Dimension dimension;

    public WelcomeScreenPanel(TextBoard textBoard, Dimension preferredSize) {
        super();
        GridLayout experimentLayout = new GridLayout(1, 2);
        experimentLayout.setVgap(0);
        experimentLayout.setHgap(1);
        setLayout(experimentLayout);
        this.textBoard = textBoard;
        this.dimension = preferredSize;
        add(this.textBoard);
    }

    public Dimension getPreferredSize() {
        return new Dimension((int) dimension.getWidth() * 2, (int) dimension.getHeight());
    }
}
