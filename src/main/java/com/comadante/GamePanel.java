package com.comadante;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public GamePanel() {
        GridLayout experimentLayout = new GridLayout(0,2);
        setLayout(experimentLayout);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
        System.out.println("PAINT");
    }
}
