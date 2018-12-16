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

    public Optional<BlockBOrientation> getBlockBOrientation() {
        Optional<GameBoardCoords> blockAGameBoardCoords = gameBoard.getCoords(blockA);
        Optional<GameBoardCoords> blockBGameBoardCoords = gameBoard.getCoords(blockB);
        if (blockAGameBoardCoords.isPresent() && blockBGameBoardCoords.isPresent()) {
            return BlockBOrientation.fromCoords(subtractCoords(blockAGameBoardCoords.get(), blockBGameBoardCoords.get()));
        }
        return Optional.empty();
    }

    public Optional<CellEntity> getBlockBEntity() {
        if (gameBoard.getCoords(blockB).isPresent()) {
            return gameBoard.getCellEntity(gameBoard.getCoords(blockB).get());
        }
        return Optional.empty();
    }

    public Optional<CellEntity> getBlockAEntity() {
        if (gameBoard.getCoords(blockA).isPresent()) {
            return gameBoard.getCellEntity(gameBoard.getCoords(blockA).get());
        }
        return Optional.empty();
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
