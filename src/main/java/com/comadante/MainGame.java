package com.comadante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class MainGame extends JFrame implements ActionListener {
    private JPanel gamePanel = new JPanel();

    public MainGame() throws IOException {
        StandardBlockRenderFactory standardBlockRenderFactory = new StandardBlockRenderFactory();
        GameBoard gameBoard = new GameBoard(new int[10][20], standardBlockRenderFactory, new StandardBlockPairFactory());
        gamePanel.add(gameBoard);
        setSize(1024,1050);
        add(gamePanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) throws IOException {
        MainGame mainGame = new MainGame();
        mainGame.repaint();
    }
}
