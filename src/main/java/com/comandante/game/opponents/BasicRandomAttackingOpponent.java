package com.comandante.game.opponents;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBlockType;
import com.comandante.game.board.GameBoard;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardCoords;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

import static com.comandante.game.board.GameBlock.RANDOM;
import static com.comandante.game.board.GameBlock.RANDOM_COUNTDOWN_SIZE;
import static com.comandante.game.board.GameBlock.RANDOM_COUNTDOWN_VALUES;


public class BasicRandomAttackingOpponent implements Opponent {


    @Override
    public List<GameBoardCellEntity[]> getAttack(GameBoard gameBoard) {

        double random = Math.random();

        if (random < .3) {
            return getCenterAttack1(gameBoard);
        } else if (random > .3 && random < .6) {
            return getCenterAttack2(gameBoard);
        } else {
            return getCenterAttack3(gameBoard);
        }

    }

    private List<GameBoardCellEntity[]> getCenterAttack1(GameBoard gameBoard) {
        List<GameBoardCellEntity[]> gameBoardCellEntitiesRows = Lists.newArrayList();

        GameBlockType countDownBlockTypeA = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
        GameBlockType countDownBlockTypeB = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];

            gameBoardCellEntities[0] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[9] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[9] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        return gameBoardCellEntitiesRows;
    }


    private List<GameBoardCellEntity[]> getCenterAttack2(GameBoard gameBoard) {
        List<GameBoardCellEntity[]> gameBoardCellEntitiesRows = Lists.newArrayList();

        GameBlockType countDownBlockTypeA = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
        GameBlockType countDownBlockTypeB = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
        GameBlockType countDownBlockTypeC = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        return gameBoardCellEntitiesRows;
    }

    private List<GameBoardCellEntity[]> getCenterAttack3(GameBoard gameBoard) {
        List<GameBoardCellEntity[]> gameBoardCellEntitiesRows = Lists.newArrayList();

        GameBlockType countDownBlockTypeA = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
        GameBlockType countDownBlockTypeB = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
        GameBlockType countDownBlockTypeC = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[3] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[9] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        {
            GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoard.getGameBoardData().getCellEntities().length];
            gameBoardCellEntities[0] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[1] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
            gameBoardCellEntities[3] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[4] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
            gameBoardCellEntities[6] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[7] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntities[8] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
            gameBoardCellEntities[9] = new GameBoardCellEntity(-1, new GameBoardCoords(0, 0));
            gameBoardCellEntitiesRows.add(gameBoardCellEntities);
        }

        return gameBoardCellEntitiesRows;
    }
}
