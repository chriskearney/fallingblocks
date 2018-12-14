package com.comadante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGame extends JFrame implements ActionListener {
    private JPanel gamePanel = new JPanel();

    public MainGame() {
        gamePanel.add(new GameBoard(new int[10][20]));
        add(gamePanel);
        setSize(400,500);
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
