package com.comandante.game.textboard;

import com.comandante.game.assetmanagement.PixelFont;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class TextBoardContents {

    private final static PixelFont.Type DEFAULT_FONT_TYPE = PixelFont.Type.COLOUR8;
    private final static int MAX_BATTLE_MESSAGE_SIZE = 5;

    private TextBoard.TextCellEntity[] header;
    private TextBoard.TextCellEntity[] footer;

    private final int boardMaxI;
    private final int boardMaxJ;
    private final ArrayBlockingQueue<TextBoard.TextCellEntity[]> battleMessagesQueue;
    private final List<TextBoard.TextCellEntity[]> battleLog = new ArrayList<>();
    private boolean isGamePaused = false;

    public TextBoardContents(int[][] a) {
        this.boardMaxI = a.length;
        this.boardMaxJ = a[0].length;
        setHeader("");
        setFooter("");
        battleMessagesQueue = new ArrayBlockingQueue<>(100);
    }

    public void setPaused(boolean p) {
        this.isGamePaused = p;
    }

    public void setHeader(String h) {
        header = createTextCellEntityArray(h);
    }

    public void setHeader(TextBoard.TextCellEntity[] headerChars) {
        this.header = headerChars;
    }

    public void setFooter(TextBoard.TextCellEntity[] footerChars) {
        this.footer = footerChars;
    }

    public void setFooter(String f) {
        this.footer = createTextCellEntityArray(f);
    }

    public void addBattleLogMessage(String message) {
        battleMessagesQueue.add(createTextCellEntityArray(message));
    }

    public TextBoard.TextCellEntity[][] getAsciiArray() {
        TextBoard.TextCellEntity[][] builtAsciiArray = new TextBoard.TextCellEntity[boardMaxI][boardMaxJ];
        builtAsciiArray[0] = header;
        builtAsciiArray[boardMaxI - 1] = footer;

        // START BATTLE LOG
        List<TextBoard.TextCellEntity[]> scrollingBattleMessages = new ArrayList<>();
        battleMessagesQueue.drainTo(scrollingBattleMessages, MAX_BATTLE_MESSAGE_SIZE);
        if (!scrollingBattleMessages.isEmpty()) {
            for (int i = 0; i < scrollingBattleMessages.size(); i++) {
                addNewRemoveOldToMakeRoom(scrollingBattleMessages.remove(i));
            }
        }

        builtAsciiArray[10] = createTextCellEntityArray(PixelFont.Type.COLOUR3, "CTRL - Rotate");
        builtAsciiArray[11] = createTextCellEntityArray(PixelFont.Type.COLOUR3, "ARROWS - Movement");
        builtAsciiArray[12] = createTextCellEntityArray(PixelFont.Type.COLOUR3, "P - Pause");

        if (isGamePaused) {
            builtAsciiArray[20] = createTextCellEntityArray(PixelFont.Type.COLOUR6, "PAUSED");
        }

        builtAsciiArray[21] = createTextCellEntityArray(PixelFont.Type.COLOUR2, "Battle Log:");
        int i = 22;
        for (TextBoard.TextCellEntity[] cellEntities: battleLog) {
            builtAsciiArray[i] = cellEntities;
            i++;
        }
        // END BATTLE LOG

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
