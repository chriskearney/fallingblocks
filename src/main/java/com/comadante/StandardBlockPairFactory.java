package com.comadante;

public class StandardBlockPairFactory implements BlockPairFactory {


    @Override
    public GameBlockPair createBlockPair(GameBoard gameBoard) {
        return new GameBlockPair(GameBlock.randomNormalBlock(), GameBlock.randomNormalBlock(), gameBoard);
    }
}
