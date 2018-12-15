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

    enum MoveDirection {
        LEFT(new GameBoardCoords(-1, 0)),
        RIGHT(new GameBoardCoords(1, 0)),
        UP(new GameBoardCoords(0, -1)),
        DOWN(new GameBoardCoords(0, 1));

        private final GameBoardCoords directionApplyCoords;

        MoveDirection(GameBoardCoords gameBoardCoords) {
            directionApplyCoords = gameBoardCoords;
        }

        public GameBoardCoords getDirectionApplyCoords() {
            return directionApplyCoords;
        }
    }
}
