package com.comadante;

import java.util.Objects;

public class GameBoardCoords {
    int i;
    int j;

    public GameBoardCoords(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameBoardCoords gameBoardCoords = (GameBoardCoords) o;
        return i == gameBoardCoords.i &&
                j == gameBoardCoords.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }

    public static GameBoardCoords MOVE_TO_THE_LEFT = new GameBoardCoords(-1, 0);
    public static GameBoardCoords MOVE_TO_THE_RIGHT = new GameBoardCoords()
}
