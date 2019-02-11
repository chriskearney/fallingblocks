package com.comandante.game.ui;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel() {
        GridLayout experimentLayout = new GridLayout(1, 2);
        experimentLayout.setVgap(0);
        experimentLayout.setHgap(1);
        setLayout(experimentLayout);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}
