package com.comadante;

import java.util.Optional;

import static com.comadante.GameBoardUtil.subtractCoords;

public class GameBlockPair {
    private final GameBlock blockA;
    private final GameBlock blockB;
    private final GameBoard gameBoard;

    public GameBlockPair(GameBlock blockA, GameBlock blockB, GameBoard gameBoard) {
        this.blockA = blockA;
        this.blockB = blockB;
        this.gameBoard = gameBoard;
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

    public Optional<BlockBOrientation> getBlockBOrientation() {
        GameBoardCoords blockAGameBoardCoords = gameBoard.getCoords(blockA);
        GameBoardCoords blockBGameBoardCoords = gameBoard.getCoords(blockB);
        return BlockBOrientation.fromCoords(subtractCoords(blockAGameBoardCoords, blockBGameBoardCoords));
    }

    public CellEntity getBlockBEntity() {
        return gameBoard.getCellEntity(gameBoard.getCoords(blockB));
    }

    public CellEntity getBlockAEntity() {
        return gameBoard.getCellEntity(gameBoard.getCoords(blockA));
    }

}
