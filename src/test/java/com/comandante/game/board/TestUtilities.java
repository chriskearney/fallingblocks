package com.comandante.game.board;

import com.google.common.io.Resources;

import java.io.IOException;
import java.nio.charset.Charset;

public class TestUtilities {
    public static String readGameBoardState(String file) throws IOException {
        return Resources.toString(Resources.getResource("gameboardstate/" + file), Charset.defaultCharset());
    }
}
