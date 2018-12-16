package com.comadante;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GamePanel gamePanel = new GamePanel();
        StandardBlockRenderFactory standardBlockRenderFactory = new StandardBlockRenderFactory();
        GameBoard gameBoard = new GameBoard(new int[10][20], standardBlockRenderFactory, new StandardBlockPairFactory());
        BufferedImage sciFi = ImageIO.read(Main.class.getResourceAsStream("/tilesets/GlossyEyeBalls/RetroSciFiBackground480x575.png"));
        ImageIcon imageIcon = new ImageIcon(sciFi);
        JLabel jLabel = new JLabel();
        jLabel.setIcon(imageIcon);
        gamePanel.add(gameBoard);
        gamePanel.add(jLabel);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(550, 570));
        frame.getContentPane().add(gamePanel);
        frame.pack();
        frame.setVisible(true);
    }
}
