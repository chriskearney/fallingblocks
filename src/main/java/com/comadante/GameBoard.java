package com.comadante;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.Color.*;
import static java.awt.Color.cyan;

public class GameBoard extends JComponent implements ActionListener {

    private final static int BOARD_SIZE = 20;
    private final static Color[] COLORS = {darkGray, green, blue, red, yellow, magenta, pink, cyan};

    private final int[][] a;
    private final int width;
    private final int height;
    private final Timer timer;

    private CellEntity[][] cellEntities;

    private final Random random = new Random();

    public GameBoard(int[][] a) {
        this.a = a;
        width = a.length;
        height = a[0].length;
        this.cellEntities = new CellEntity[a.length][a[0].length];
        resetBoard();
        timer = new Timer(500, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        int size = BOARD_SIZE;
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                g.setColor(cellEntities[i][j].getColor());
                g.fill3DRect(i * size, j * size, size, size, true);
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(width * BOARD_SIZE, height * BOARD_SIZE);
    }

    private void resetBoard() {
        random.nextInt(COLORS.length);
        for (int i = 0; i < cellEntities.length; i++) {
            for (int j = 0; j < cellEntities[0].length; j++) {
                cellEntities[i][j] = new CellEntity();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        cellEntities[random.nextInt(cellEntities.length)][random.nextInt(cellEntities[0].length)] = new CellEntity(GameBlock.random());
        repaint();
    }
}
