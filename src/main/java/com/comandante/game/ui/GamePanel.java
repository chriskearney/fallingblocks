package com.comandante.game.ui;

import com.comandante.game.board.DelayFire;
import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoard;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardCoords;
import com.comandante.game.textboard.TextBoard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel {

    private final DelayFire delayFire = new DelayFire();

    public GamePanel(GameBoard gameBoard, TextBoard textBoard) {
        GridLayout experimentLayout = new GridLayout(1, 2);
        experimentLayout.setVgap(0);
        experimentLayout.setHgap(1);
        setLayout(experimentLayout);
        add(gameBoard);
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                GameBoardCellEntity byPoint = gameBoard.getGameBoardData().getByPoint(e.getPoint());
                delayFire.add(byPoint.getId(), new Runnable() {
                    @Override
                    public void run() {
                        GameBoardCoords gameBoardCoords = byPoint.getGameBoardCoords();
                        System.out.printf("x=" + gameBoardCoords.i + " y=" + gameBoardCoords.j);
                        if (byPoint.getGameBlock().isPresent()) {
                            GameBlock gameBlock = byPoint.getGameBlock().get();
                            System.out.printf(" gameBlockType: " + gameBlock.getType() + " id: " + gameBlock.getIdentifier() + " resting: " + gameBlock.isResting() + " ");
                            if (gameBlock.getBorderType() != null && gameBlock.getBorderType().isPresent()) {
                                System.out.println("borderType: " + gameBlock.getBorderType().get());
                            } else {
                                System.out.println();
                            }
                        } else {
                            System.out.println();
                        }
                    }
                });
                repaint();
            }

        });
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}
