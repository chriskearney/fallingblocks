package com.comandante.game.board;

public class GameBoardUtil {

    public static GameBoardCoords subtractCoords(GameBoardCoords a, GameBoardCoords b) {
        int i = a.i - b.i;
        int j = a.j - b.j;

        return new GameBoardCoords(i, j);
    }
}

