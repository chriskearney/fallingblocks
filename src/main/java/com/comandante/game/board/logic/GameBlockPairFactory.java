package com.comandante.game.board.logic;

import com.comandante.game.board.GameBlockPair;
import com.comandante.game.board.GameBoard;

public interface GameBlockPairFactory {

    GameBlockPair createBlockPair(GameBoard gameBoard);

}
