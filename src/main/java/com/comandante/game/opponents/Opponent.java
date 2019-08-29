package com.comandante.game.opponents;

import com.comandante.game.board.GameBoard;
import com.comandante.game.board.GameBoardCellEntity;

import java.util.List;

public interface Opponent {

    List<GameBoardCellEntity[]> getAttack(int amt);

}
