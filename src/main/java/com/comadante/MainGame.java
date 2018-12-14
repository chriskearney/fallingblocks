package com.comadante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGame extends JFrame implements ActionListener {
    private JPanel gamePanel = new JPanel();

    public MainGame() {
        GameBoard gameBoard = new GameBoard(new int[10][20]);
        gamePanel.add(gameBoard);
        add(gamePanel);
        setSize(350,700);
        setVisible(true);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        MainGame mainGame = new MainGame();
        mainGame.repaint();
    }
}
