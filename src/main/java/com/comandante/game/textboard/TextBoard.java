package com.comandante.game.textboard;

import com.comandante.game.assetmanagement.PixelFont;
import com.comandante.game.assetmanagement.PixelFontSpriteManager;
import com.comandante.game.board.GameBlockPair;
import com.comandante.game.board.logic.GameBlockRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.comandante.game.board.GameBoardData.BLOCK_SIZE;

public class TextBoard extends JComponent implements ActionListener {

    private final float scaleTime = 2f;
    private final int FONT_SIZE_I = (int) (5 * scaleTime);
    private final int FONT_SIZE_J = (int) (12 * scaleTime);
    private final PixelFontSpriteManager pixelFontSpriteManager;
    private final Timer timer;
    private TextBoardContents textBoardContents;
    private GameBlockPair nextBlockPair;
    private final GameBlockRenderer gameBlockRenderer;

    private TextCellEntity[][] lastArray;

    private boolean gameOver = false;

    private final int maxI;
    private final int maxJ;

    private final int[][] a;

    public TextBoard(int[][] a, GameBlockRenderer gameBlockRenderer) throws IOException {
        maxI = a.length;
        maxJ = a[0].length;
        this.a = a;
        this.textBoardContents = new TextBoardContents(a);
        this.pixelFontSpriteManager = new PixelFontSpriteManager();
        this.gameBlockRenderer = gameBlockRenderer;
        timer = new Timer(400, this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        TextCellEntity[][] asciiArray;
        if (gameOver) {
            nextBlockPair = null;
            asciiArray = textBoardContents.getGameOverArray();
        } else {
            asciiArray = textBoardContents.getAsciiArray();
        }

        if (Arrays.deepEquals(lastArray, asciiArray)) {
            lastArray = asciiArray;
            return;
        }
        lastArray = asciiArray;
        repaint();
    }

    public void setGameOver(boolean state) {
        gameOver = state;
    }

    public void paintComponent(Graphics g) {


        for (int i = 0; i < maxI; i++) {
            for (int j = 0; j < maxJ; j++) {
                g.setColor(Color.black);
                g.fillRect((j * FONT_SIZE_I), i * FONT_SIZE_J, FONT_SIZE_I, FONT_SIZE_J);
                TextCellEntity textCellEntity = lastArray[i][j];
                if (textCellEntity == null) {
                    textCellEntity = new TextCellEntity(PixelFont.Type.COLOUR8, 32);
                }
                BufferedImage image = pixelFontSpriteManager.get(textCellEntity.getType(), textCellEntity.getAsciiCode());
                g.drawImage(image, (j * FONT_SIZE_I), i * FONT_SIZE_J, FONT_SIZE_I, FONT_SIZE_J, null);

                if (nextBlockPair != null) {
                    BufferedImage blockAImage = gameBlockRenderer.getImage(nextBlockPair.getBlockA().getType()).get(0);
                    BufferedImage blockBImage = gameBlockRenderer.getImage(nextBlockPair.getBlockB().getType()).get(0);
                    int pairY = 50;
                    g.drawImage(blockAImage, 13, pairY, BLOCK_SIZE, BLOCK_SIZE, null);
                    g.drawImage(blockBImage, 13, pairY + BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE, null);

                }
            }
        }
    }

    public void setNextBlockPair(GameBlockPair gameBlockPair) {
        this.nextBlockPair = gameBlockPair;
    }

    static class TextCellEntity {
        private final PixelFont.Type type;
        private final int asciiCode;

        public TextCellEntity(PixelFont.Type type, int asciiCode) {
            this.type = type;
            this.asciiCode = asciiCode;
        }

        public int getAsciiCode() {
            return asciiCode;
        }

        public PixelFont.Type getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TextCellEntity that = (TextCellEntity) o;
            return asciiCode == that.asciiCode &&
                    type == that.type;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, asciiCode);
        }
    }

    public TextBoardContents getTextBoardContents() {
        return textBoardContents;
    }

    public void reset() {
        this.gameOver = false;
        this.textBoardContents = new TextBoardContents(a);
    }
}
