package com.comandante.game.opponents;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoard;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardCoords;
import com.google.common.collect.Lists;

import java.util.List;

public class BasicRandomAttackingOpponent implements Opponent {
    @Override
    public List<GameBoardCellEntity[]> getAttack(GameBoard gameBoard) {
        List<GameBoardCellEntity[]> gameBoardCellEntitiesRows = Lists.newArrayList();
        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.randomNormalBlock());
            gameBoardCellEntities[6] = new GameBoardCellEntity(GameBlock.randomNormalBlock());
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }


        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.randomNormalBlock());
            gameBoardCellEntities[6] = new GameBoardCellEntity(GameBlock.randomNormalBlock());
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.randomNormalBlock());
            gameBoardCellEntities[6] = new GameBoardCellEntity(GameBlock.randomNormalBlock());
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        return gameBoardCellEntitiesRows;
    }
}
