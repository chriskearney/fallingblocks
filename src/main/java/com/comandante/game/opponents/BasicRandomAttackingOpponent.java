package com.comandante.game.opponents;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBlockType;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardData;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

import static com.comandante.game.board.GameBlock.RANDOM;
import static com.comandante.game.board.GameBlock.RANDOM_COUNTDOWN_SIZE;
import static com.comandante.game.board.GameBlock.RANDOM_COUNTDOWN_VALUES;


public class BasicRandomAttackingOpponent implements Opponent {

    private final List<List<GameBoardCellEntity[]>> baseAttack;
    private final GameBoardData gameBoardData;
    private final Random random = new Random();

    public BasicRandomAttackingOpponent(GameBoardData gameBoardData) {
        this.gameBoardData = gameBoardData;
        this.baseAttack = Lists.newArrayList(getBaseAttacks());
    }

    @Override
    public List<GameBoardCellEntity[]> getAttack(int amt) {
        System.out.println("ATTACK SIZE: " + amt);
        List<GameBoardCellEntity[]> randomBaseAttack = getRandomBaseAttack();
        AttackSizeDetails attackSizeDetailForAmt = getAttackSizeDetailForAmt(amt);
        List<GameBoardCellEntity[]> gameBoardCellEntities = alterAttackBasedOnDetails(attackSizeDetailForAmt, randomBaseAttack);
        return gameBoardCellEntities;
    }

    public List<GameBoardCellEntity[]> getRandomBaseAttack()
    {
        return baseAttack.get(random.nextInt(baseAttack.size()));
    }

    private List<GameBoardCellEntity[]> alterAttackBasedOnDetails(AttackSizeDetails attackSizeDetails, List<GameBoardCellEntity[]> baseAttack) {
        List<GameBoardCellEntity[]> finalAttackAlteredBasedOnDetails = Lists.newArrayList();

        List<GameBoardCellEntity[]> alteredRowBaseAttack = Lists.newArrayList();
        for (GameBoardCellEntity[] attackRow : baseAttack) {
            GameBoardCellEntity[] alteredRow = new GameBoardCellEntity[attackSizeDetails.rowsWide];
            System.arraycopy(attackRow, 0, alteredRow, 0, attackSizeDetails.rowsWide);
            alteredRowBaseAttack.add(alteredRow);
        }

        while (finalAttackAlteredBasedOnDetails.size() < attackSizeDetails.rowsHigh) {
            finalAttackAlteredBasedOnDetails.addAll(alteredRowBaseAttack);
        }

        return finalAttackAlteredBasedOnDetails;
    }

    private AttackSizeDetails getAttackSizeDetailForAmt(int amt) {
        AttackSizeDetails attackSizeDetails = new AttackSizeDetails();
        if (amt > 1 && amt < 40) {
            attackSizeDetails.rowsHigh = 1;
            attackSizeDetails.rowsWide = 2;
        } else if (amt > 40 && amt < 150) {
            attackSizeDetails.rowsHigh = 1;
            attackSizeDetails.rowsWide = 10;
        } else if (amt > 150 && amt < 300) {
            attackSizeDetails.rowsHigh = 2;
            attackSizeDetails.rowsWide = 10;
        } else if (amt > 300 && amt < 600) {
            attackSizeDetails.rowsHigh = 4;
            attackSizeDetails.rowsWide = 10;
        } else if (amt > 600 && amt < 1500) {
            attackSizeDetails.rowsHigh = 6;
            attackSizeDetails.rowsWide = 10;
        } else if (amt > 1500 && amt < 2500) {
            attackSizeDetails.rowsHigh = 7;
            attackSizeDetails.rowsWide = 10;
        } else if (amt > 2500 && amt < 5000) {
            attackSizeDetails.rowsHigh = 9;
            attackSizeDetails.rowsWide = 10;
        } else if (amt > 5000) {
            attackSizeDetails.rowsHigh = 12;
            attackSizeDetails.rowsWide = 10;
        }
        return attackSizeDetails;
    }

    private List<List<GameBoardCellEntity[]>> getBaseAttacks() {

        List<List<GameBoardCellEntity[]>> baseAttacks = Lists.newArrayList();
        {
            /*
                        BASE ATTACK 1

            */
            List<GameBoardCellEntity[]> baseAttack = Lists.newArrayList();

            GameBlockType countDownBlockTypeA = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
            GameBlockType countDownBlockTypeB = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
            GameBlockType countDownBlockTypeC = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));

            {
                GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoardData.getCellEntities().length];

                gameBoardCellEntities[0] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[1] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[3] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[4] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[6] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[7] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[8] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[9] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                baseAttack.add(gameBoardCellEntities);
            }

            {
                GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoardData.getCellEntities().length];

                gameBoardCellEntities[0] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[1] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[3] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[4] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[6] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[7] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[8] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[9] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                baseAttack.add(gameBoardCellEntities);
            }

            baseAttacks.add(baseAttack);
        }

        {
            /*
                        BASE ATTACK 2

            */
            List<GameBoardCellEntity[]> baseAttack = Lists.newArrayList();

            GameBlockType countDownBlockTypeA = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
            GameBlockType countDownBlockTypeB = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));
            GameBlockType countDownBlockTypeC = RANDOM_COUNTDOWN_VALUES.get(RANDOM.nextInt(RANDOM_COUNTDOWN_SIZE));

            {
                GameBoardCellEntity[] gameBoardCellEntities = new GameBoardCellEntity[gameBoardData.getCellEntities().length];

                gameBoardCellEntities[0] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[1] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[2] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[3] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[4] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[5] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                gameBoardCellEntities[6] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[7] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeB));
                gameBoardCellEntities[8] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeA));
                gameBoardCellEntities[9] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(countDownBlockTypeC));
                baseAttack.add(gameBoardCellEntities);
            }

            baseAttacks.add(baseAttack);
        }

        return baseAttacks;
    }

    private static class AttackSizeDetails {
        int rowsWide;
        int rowsHigh;
    }
}
