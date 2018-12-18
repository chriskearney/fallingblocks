package com.comandante.game.board.logic;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBlockPair;
import com.comandante.game.board.GameBoard;

public class StandardGameBlockPairFactory implements GameBlockPairFactory {


    @Override
    public GameBlockPair createBlockPair(GameBoard gameBoard) {

        GameBlock blockA = GameBlock.randomNormalBlock();
        if (Math.random() < 0.2) {
            blockA = GameBlock.randomMagicBlock();
        }

        GameBlock blockB = GameBlock.randomNormalBlock();
        if (Math.random() < 0.2) {
            blockB = GameBlock.randomMagicBlock();
        }

        return new GameBlockPair(blockA, blockB, gameBoard);
    }
}
