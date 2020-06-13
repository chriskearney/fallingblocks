package com.comandante.game.board.logic;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBlockPair;
import com.comandante.game.board.GameBoard;

public class StandardGameBlockPairFactory implements GameBlockPairFactory {

    private GameBlockPair nextPair;
    private final GameBlockRenderer gameBlockRenderer;

    public StandardGameBlockPairFactory(GameBlockRenderer gameBlockRenderer) {
        this.gameBlockRenderer = gameBlockRenderer;
    }

    @Override
    public GameBlockPair createBlockPair(GameBoard gameBoard) {
        if (nextPair == null) {
            nextPair = randomPair(gameBoard);
        }

        GameBlockPair returnPair = nextPair;

        nextPair = randomPair(gameBoard);

        return returnPair;
    }

    @Override
    public GameBlockPair getNextPair() {
        return nextPair;
    }

    private GameBlockPair randomPair(GameBoard gameBoard) {
        GameBlock blockA = GameBlock.randomNormalBlock();
        if (Math.random() < 0.2) {
            blockA = GameBlock.randomMagicBlock(gameBlockRenderer);
        }

        if (Math.random() < .03) {
            blockA = GameBlock.diamondBlock(gameBlockRenderer);
        }

        GameBlock blockB = GameBlock.randomNormalBlock();
        if (Math.random() < 0.2) {
            blockB = GameBlock.randomMagicBlock(gameBlockRenderer);
        }

        return new GameBlockPair(blockA, blockB);
        //return new GameBlockPair(new GameBlock(GameBlock.Type.GREEN), new GameBlock(GameBlock.Type.GREEN), gameBoard);
    }
}
