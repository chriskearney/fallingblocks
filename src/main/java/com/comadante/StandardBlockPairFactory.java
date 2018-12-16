package com.comadante;

public class StandardBlockPairFactory implements BlockPairFactory {


    @Override
    public GameBlockPair createBlockPair(GameBoard gameBoard) {

        GameBlock blockA = GameBlock.randomNormalBlock();
        if (Math.random() < 0.05) {
            blockA = GameBlock.randomMagicBlock();
        }

        GameBlock blockB = GameBlock.randomNormalBlock();
        if (Math.random() < 0.05) {
            blockB = GameBlock.randomMagicBlock();
        }

        return new GameBlockPair(blockA, blockB, gameBoard);
    }
}
