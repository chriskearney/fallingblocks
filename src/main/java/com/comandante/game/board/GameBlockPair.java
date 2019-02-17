package com.comandante.game.board;

import java.util.Optional;

public class GameBlockPair {
    private final GameBlock blockA;
    private final GameBlock blockB;

    public GameBlockPair(GameBlock blockA, GameBlock blockB) {
        this.blockA = blockA;
        this.blockB = blockB;
    }

    public GameBlock getBlockA() {
        return blockA;
    }

    public GameBlock getBlockB() {
        return blockB;
    }

    public enum BlockBOrientation {
        BOTTOM_OF(new GameBoardCoords(0, -1)),
        LEFT_OF(new GameBoardCoords(1, 0)),
        TOP_OF(new GameBoardCoords(0, 1)),
        RIGHT_OF(new GameBoardCoords(-1, 0));

        private final GameBoardCoords applyCords;

        BlockBOrientation(GameBoardCoords applyCords) {
            this.applyCords = applyCords;
        }

        public GameBoardCoords getApplyCords() {
            return applyCords;
        }

        public static Optional<BlockBOrientation> fromCoords(GameBoardCoords gameBoardCoords) {
            for (BlockBOrientation orientation : BlockBOrientation.values()) {
                if (orientation.getApplyCords().equals(gameBoardCoords)) {
                    return Optional.of(orientation);
                }
            }
            return Optional.empty();
        }
    }

}
