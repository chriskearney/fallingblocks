package com.comandante.game.board;

import java.util.Optional;

import static com.comandante.game.board.GameBoardUtil.subtractCoords;

public class GameBlockPair {
    private final GameBlock blockA;
    private final GameBlock blockB;
    private final transient GameBoardData gameBoardData;

    public GameBlockPair(GameBlock blockA, GameBlock blockB, GameBoardData gameBoardData) {
        this.blockA = blockA;
        this.blockB = blockB;
        this.gameBoardData = gameBoardData;
    }

    public GameBlock getBlockA() {
        return blockA;
    }

    public GameBlock getBlockB() {
        return blockB;
    }

    public Optional<BlockBOrientation> getBlockBOrientation() {
        Optional<GameBoardCoords> blockAGameBoardCoords = gameBoardData.getCoords(blockA);
        Optional<GameBoardCoords> blockBGameBoardCoords = gameBoardData.getCoords(blockB);
        if (blockAGameBoardCoords.isPresent() && blockBGameBoardCoords.isPresent()) {
            return BlockBOrientation.fromCoords(subtractCoords(blockAGameBoardCoords.get(), blockBGameBoardCoords.get()));
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getBlockBEntity() {
        if (gameBoardData.getCoords(blockB).isPresent()) {
            return gameBoardData.getCellEntity(gameBoardData.getCoords(blockB).get());
        }
        return Optional.empty();
    }

    public Optional<GameBoardCellEntity> getBlockAEntity() {
        if (gameBoardData.getCoords(blockA).isPresent()) {
            return gameBoardData.getCellEntity(gameBoardData.getCoords(blockA).get());
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
