package com.comandante;

import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBlockPair;
import com.comandante.game.board.GameBoard;
import com.comandante.game.board.logic.GameBlockPairFactory;
import com.comandante.game.board.logic.StandardGameBlockPairFactory;
import com.comandante.game.board.logic.StandardMagicGameBlockProcessor;
import com.comandante.game.textboard.TextBoard;
import com.comandante.game.ui.GamePanel;

import javax.swing.*;
import java.io.IOException;
import java.util.Locale;

public class Main extends JFrame {


    public Main() throws IOException {
        configurateOperatingSpecificBehavior();
        GamePanel gamePanel = new GamePanel();
        TileSetGameBlockRenderer tileSetBlockRenderProcessor = new TileSetGameBlockRenderer("8bit");
        TextBoard textBoard = new TextBoard(new int[27][32], tileSetBlockRenderProcessor);
        GameBoard gameBoard = new GameBoard(new int[10][20], tileSetBlockRenderProcessor, new StandardGameBlockPairFactory(), new StandardMagicGameBlockProcessor(), textBoard);
        gamePanel.add(gameBoard);
        gamePanel.add(textBoard);
        setTitle("Mystery Fighter");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(gamePanel);
        pack();
        setVisible(true);

    }

    public static void main(String[] args) throws IOException {
        try {
            Main main = new Main();
        } catch (Error e) {
            System.out.println("BLERGH");
            e.printStackTrace();
        }
    }

    private void configurateOperatingSpecificBehavior() {
        try {
            OsCheck.OSType ostype = OsCheck.getOperatingSystemType();
            switch (ostype) {
                case Windows:
                    break;
                case MacOS:
                    Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool false");
                    Runtime.getRuntime().addShutdownHook(new Thread() {
                        public void run() {
                            try {
                                Runtime.getRuntime().exec("defaults write NSGlobalDomain ApplePressAndHoldEnabled -bool true");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case Linux:
                    break;
                case Other:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final class OsCheck {
        /**
         * types of Operating Systems
         */
        public enum OSType {
            Windows, MacOS, Linux, Other
        }

        ;

        // cached result of OS detection
        protected static OSType detectedOS;

        /**
         * detect the operating system from the os.name System property and cache
         * the result
         *
         * @returns - the operating system detected
         */
        public static OSType getOperatingSystemType() {
            if (detectedOS == null) {
                String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
                if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                    detectedOS = OSType.MacOS;
                } else if (OS.indexOf("win") >= 0) {
                    detectedOS = OSType.Windows;
                } else if (OS.indexOf("nux") >= 0) {
                    detectedOS = OSType.Linux;
                } else {
                    detectedOS = OSType.Other;
                }
            }
            return detectedOS;
        }
    }

}
