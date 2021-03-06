package com.comandante;

import com.comandante.game.MusicManager;
import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.GameBoard;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardData;
import com.comandante.game.board.logic.StandardGameBlockPairFactory;
import com.comandante.game.board.logic.StandardMagicGameBlockProcessor;
import com.comandante.game.board.logic.invoker.EvaluateAttackInvoker;
import com.comandante.game.board.logic.invoker.InvokerHarness;
import com.comandante.game.opponents.BasicRandomAttackingOpponent;
import com.comandante.game.textboard.TextBoard;
import com.comandante.game.ui.GamePanel;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Main extends JFrame {


    public Main() throws IOException, InvalidMidiDataException, MidiUnavailableException {
        configurateOperatingSpecificBehavior();
        TileSetGameBlockRenderer tileSetBlockRenderProcessor = new TileSetGameBlockRenderer("diamond");
        TextBoard textBoard = new TextBoard(new int[33][37], tileSetBlockRenderProcessor);
        GameBoardData gameBoardData = new GameBoardData(new int[10][20]);
        MusicManager musicManager = new MusicManager(MidiSystem.getSequencer());
        musicManager.loadMusic();
        musicManager.playMusic();
        setTitle("PixelPuzzler");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        BasicRandomAttackingOpponent basicRandomAttackingOpponent = new BasicRandomAttackingOpponent(gameBoardData);

        InvokerHarness<List<GameBoardCellEntity[]>, Void> opponentHarness = new InvokerHarness<>(5, new EvaluateAttackInvoker(basicRandomAttackingOpponent), false);

        GameBoard gameBoard = new GameBoard(gameBoardData, tileSetBlockRenderProcessor, new StandardGameBlockPairFactory(tileSetBlockRenderProcessor), new StandardMagicGameBlockProcessor(), textBoard, musicManager, opponentHarness);
        GamePanel gamePanel = new GamePanel(gameBoard, textBoard);
        getContentPane().add(gamePanel);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) throws IOException, InvalidMidiDataException, MidiUnavailableException {
        try {
            Main main = new Main();
        } catch (Error e) {
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
                    System.setProperty("apple.awt.graphics.EnableQ2DX", "true");
                    System.setProperty("apple.laf.useScreenMenuBar", "true");
                    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "PixelFighter");

                    // create an instance of the Mac Application class, so i can handle the
                    // mac quit event with the Mac ApplicationAdapter

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
