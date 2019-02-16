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
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class GamePanel extends JPanel {

    private final DelayFire delayFire = new DelayFire();

    public GamePanel(GameBoard gameBoard, TextBoard textBoard) {
        GridLayout experimentLayout = new GridLayout(1, 2);
        experimentLayout.setVgap(0);
        experimentLayout.setHgap(1);
        setLayout(experimentLayout);
        add(gameBoard);
        add(textBoard);
        addMouseMotionListener(getDebugMouseAdapter(gameBoard));
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }

    private MouseAdapter getDebugMouseAdapter(GameBoard gameBoard) {
        return new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Optional<GameBoardCellEntity> gameBoardCellEntityOptional = gameBoard.getGameBoardData().getByPoint(e.getPoint());
                if (!gameBoardCellEntityOptional.isPresent()) {
                    return;
                }
                GameBoardCellEntity gameBoardCellEntity = gameBoardCellEntityOptional.get();
                delayFire.add(gameBoardCellEntity.getId(), () -> {
                    GameBoardCoords gameBoardCoords = gameBoardCellEntity.getGameBoardCoords();
                    System.out.print("x=" + gameBoardCoords.i + " y=" + gameBoardCoords.j);
                    if (gameBoardCellEntity.getGameBlock().isPresent()) {
                        GameBlock gameBlock = gameBoardCellEntity.getGameBlock().get();
                        System.out.print(" gameBlockType: " + gameBlock.getType() + " id: " + gameBlock.getIdentifier() + " resting: " + gameBlock.isResting() + " ");
                        if (gameBlock.getBorderType() != null && gameBlock.getBorderType().isPresent()) {
                            System.out.print("borderType: " + gameBlock.getBorderType().get());
                        }
                        Optional<UUID> permaGroupForBlock = gameBoard.getPermaGroupManager().getPermaGroupForBlock(gameBlock);
                        permaGroupForBlock.ifPresent(uuid -> System.out.print(" blockGroup: " + uuid));
                    }
                    System.out.println();
                });
                repaint();
            }
        };
    }
}
