package com.comandante;

import com.comandante.game.MusicManager;
import com.comandante.game.assetmanagement.PixelFont;
import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.GameBoard;
import com.comandante.game.board.GameBoardData;
import com.comandante.game.board.logic.StandardGameBlockPairFactory;
import com.comandante.game.board.logic.StandardMagicGameBlockProcessor;
import com.comandante.game.textboard.TextBoard;
import com.comandante.game.ui.GamePanel;
import com.comandante.game.ui.WelcomeScreenPanel;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.io.IOException;
import java.util.Locale;

public class Main extends JFrame {


    public Main() throws IOException, InvalidMidiDataException, MidiUnavailableException {
        configurateOperatingSpecificBehavior();
        TileSetGameBlockRenderer tileSetBlockRenderProcessor = new TileSetGameBlockRenderer("8bit");
        TextBoard textBoard = new TextBoard(new int[27][32], tileSetBlockRenderProcessor);
        GameBoardData gameBoardData = new GameBoardData(new int[10][20]);
        MusicManager musicManager = new MusicManager(MidiSystem.getSequencer());
        musicManager.loadMusis();
        musicManager.playMusic();
        GameBoard gameBoard = new GameBoard(gameBoardData, tileSetBlockRenderProcessor, new StandardGameBlockPairFactory(), new StandardMagicGameBlockProcessor(), textBoard, musicManager);
        GamePanel gamePanel = new GamePanel(gameBoard, textBoard);
        setTitle("PixelPuzzler");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        getContentPane().add(gamePanel);


//        int[][] rawBoard = new int[27][32];
//        TextBoard welcomeScreenTextBoard = new TextBoard(rawBoard, tileSetBlockRenderProcessor);
//        WelcomeScreenPanel welcomeScreenPanel = new WelcomeScreenPanel(welcomeScreenTextBoard, gameBoard.getGameBoardData().getPreferredSize());
//        getContentPane().add(welcomeScreenPanel);

        pack();
        setVisible(true);


    }

    public static void main(String[] args) throws IOException, InvalidMidiDataException, MidiUnavailableException {
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
