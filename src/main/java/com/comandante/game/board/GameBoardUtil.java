package com.comandante.game.board;

import java.util.*;
import java.util.stream.Collectors;

public class GameBoardUtil {

    public static GameBoardCoords subtractCoords(GameBoardCoords a, GameBoardCoords b) {
        int i = a.i - b.i;
        int j = a.j - b.j;

        return new GameBoardCoords(i, j);
    }

    public static GameBoardCoords addCords(GameBoardCoords a, GameBoardCoords b) {
        int i = a.i + b.i;
        int j = a.j + b.j;

        return new GameBoardCoords(i, j);
    }
}
