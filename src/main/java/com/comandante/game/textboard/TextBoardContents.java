package com.comandante.game.textboard;

import com.comandante.game.assetmanagement.PixelFont;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class TextBoardContents {

    private final static PixelFont.Type DEFAULT_FONT_TYPE = PixelFont.Type.COLOUR8;
    private final static int MAX_BATTLE_MESSAGE_SIZE = 5;

    private Integer score = 0;

    private final int boardMaxI;
    private final int boardMaxJ;
    private final ArrayBlockingQueue<TextBoard.TextCellEntity[]> battleMessagesQueue;
    private final List<TextBoard.TextCellEntity[]> battleLog = new ArrayList<>();
    private boolean isGamePaused = false;

    public TextBoardContents(int[][] a) {
        this.boardMaxI = a.length;
        this.boardMaxJ = a[0].length;
        battleMessagesQueue = new ArrayBlockingQueue<>(100);
    }

    public void setPaused(boolean p) {
        this.isGamePaused = p;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void addBattleLogMessage(String message) {
        battleMessagesQueue.add(createTextCellEntityArray(message));
    }

    public void addNewPointsToBattleLog(Integer amt) {
        TextBoard.TextCellEntity[] textCellEntities = createTextCellEntityArray(PixelFont.Type.COLOUR3, "+" + amt);
        battleMessagesQueue.add(textCellEntities);
    }

    public TextBoard.TextCellEntity[][] getAsciiArray() {
        TextBoard.TextCellEntity[][] builtAsciiArray = new TextBoard.TextCellEntity[boardMaxI][boardMaxJ];
        builtAsciiArray[1] = createTextCellEntityArray(PixelFont.Type.COLOUR1, "Next");

        // START BATTLE LOG
        List<TextBoard.TextCellEntity[]> scrollingBattleMessages = new ArrayList<>();
        battleMessagesQueue.drainTo(scrollingBattleMessages, MAX_BATTLE_MESSAGE_SIZE);
        if (!scrollingBattleMessages.isEmpty()) {
            for (int i = 0; i < scrollingBattleMessages.size(); i++) {
                addNewRemoveOldToMakeRoom(scrollingBattleMessages.remove(i));
            }
        }
        builtAsciiArray[10] = createTextCellEntityArray(PixelFont.Type.COLOUR7, "Rotate   = W");
        builtAsciiArray[11] = createTextCellEntityArray(PixelFont.Type.COLOUR7, "Movement = A,S,D");
        builtAsciiArray[12] = createTextCellEntityArray(PixelFont.Type.COLOUR7, "Pause    = P");


        if (isGamePaused) {
            builtAsciiArray[20] = createTextCellEntityArray(PixelFont.Type.COLOUR6, "PAUSED");
        }

        String stringScore = String.format("%1$" + 9 + "s", Integer.toString(score));
        builtAsciiArray[21] = createTextCellEntityArray(PixelFont.Type.COLOUR8, stringScore.replace(" ", "0"));
        int i = 22;
        for (TextBoard.TextCellEntity[] cellEntities: battleLog) {
            builtAsciiArray[i] = cellEntities;
            i++;
        }
        // END BATTLE LOG

        return builtAsciiArray;
    }

    public TextBoard.TextCellEntity[][] getGameOverArray() {
        TextBoard.TextCellEntity[][] builtAsciiArray = new TextBoard.TextCellEntity[boardMaxI][boardMaxJ];
        builtAsciiArray[10] = createTextCellEntityArray(PixelFont.Type.COLOUR2, "GAME OVER");
        String stringScore = String.format("%1$" + 9 + "s", Integer.toString(score));
        builtAsciiArray[11] = createTextCellEntityArray(PixelFont.Type.COLOUR8, "Final Score: " + stringScore.replace(" ", "0"));
        builtAsciiArray[13] = createTextCellEntityArray(PixelFont.Type.COLOUR3, "Press R to restart.");

        return builtAsciiArray;
    }

    public String pad(String pad) {
        return " " + String.format("%1$-" + (boardMaxJ -1) + "s", pad);
    }

    public TextBoard.TextCellEntity[] createTextCellEntityArray(PixelFont.Type type, String source) {
        TextBoard.TextCellEntity[] array = new TextBoard.TextCellEntity[boardMaxJ];
        String pad = pad(source);
        char[] headerChars = pad.toCharArray();
        for (int y = 0; y < boardMaxJ; y++) {
            array[y] = new TextBoard.TextCellEntity(type, (int) headerChars[y]);
        }
        return array;
    }

    public TextBoard.TextCellEntity[] createTextCellEntityArray(String source) {
        return createTextCellEntityArray(DEFAULT_FONT_TYPE, source);
    }

    private void addNewRemoveOldToMakeRoom(TextBoard.TextCellEntity[] cellEntities) {
        if (battleLog.size() == MAX_BATTLE_MESSAGE_SIZE) {
            battleLog.remove(0);
        }
        battleLog.add(cellEntities);
    }
}
