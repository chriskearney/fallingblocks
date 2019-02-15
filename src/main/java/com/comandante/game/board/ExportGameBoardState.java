package com.comandante.game.board;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class ExportGameBoardState {

    public final static String HOME_DIRECTORY = System.getProperty("user.home");
    private final static GameBoardDataSerialization gameBoardDataSerialization = new GameBoardDataSerialization();

    public static void export(GameBoardData gameBoardData) throws IOException {
        String serializedGameBoardData = gameBoardDataSerialization.serialize(gameBoardData);
        File file = fileWithDirectoryAssurance(HOME_DIRECTORY + "/.fallingblocks/gameboardstate", System.currentTimeMillis() + ".json");
        file.createNewFile();
        Files.write(file.toPath(), serializedGameBoardData.getBytes(), StandardOpenOption.WRITE);

    }

    private static File fileWithDirectoryAssurance(String directory, String filename) {
        File dir = new File(directory);
        if (!dir.exists()) dir.mkdirs();
        return new File(directory + "/" + filename);
    }


}
