package com.comadante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MainGame extends JFrame implements ActionListener, KeyListener {
    private JPanel gamePanel = new JPanel();

    public MainGame() {
        GameBoard gameBoard = new GameBoard(new int[10][20]);
        gamePanel.add(gameBoard);
        add(gamePanel);
        setSize(350,700);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    public static void main(String[] args) {
        MainGame mainGame = new MainGame();
        mainGame.repaint();
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        System.out.println(Character.toString(keyEvent.getKeyChar()));

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.println(Character.toString(keyEvent.getKeyChar()));
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        System.out.println(Character.toString(keyEvent.getKeyChar()));

    }
}
