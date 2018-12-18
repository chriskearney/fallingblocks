package com.comandante;

import com.comandante.game.board.GameBoard;
import com.comandante.game.ui.GamePanel;
import com.comandante.game.board.logic.StandardGameBlockPairFactory;
import com.comandante.game.board.logic.StandardMagicGameBlockProcessor;
import com.comandante.game.textboard.TextBoard;
import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;

import javax.swing.*;
import java.io.IOException;

public class Main extends JFrame {


    public Main() throws IOException {
        GamePanel gamePanel = new GamePanel();
        TileSetGameBlockRenderer tileSetBlockRenderProcessor = new TileSetGameBlockRenderer("8bit");
        TextBoard textBoard = new TextBoard(new int[27][32]);
        GameBoard gameBoard = new GameBoard(new int[10][20], tileSetBlockRenderProcessor, new StandardGameBlockPairFactory(), new StandardMagicGameBlockProcessor(), textBoard);
        gamePanel.add(gameBoard);
        gamePanel.add(textBoard);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(gamePanel);
        pack();
        setVisible(true);

    }

    public static void main(String[] args) throws IOException {
        Main main = new Main();
    }
}
