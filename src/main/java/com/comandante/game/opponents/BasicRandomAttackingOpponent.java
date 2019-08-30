package com.comandante.game.opponents;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBlockType;
import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBoardData;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.comandante.game.board.GameBlock.RANDOM;
import static com.comandante.game.board.GameBlock.RANDOM_COUNTDOWN_SIZE;
import static com.comandante.game.board.GameBlock.RANDOM_COUNTDOWN_VALUES;


public class BasicRandomAttackingOpponent implements Opponent {

    private final List<List<GameBoardCellEntity[]>> baseAttack;
    private final GameBoardData gameBoardData;
    private final Random random = new Random();

    private long startTime;
    private static final int ONE_MINUTE = 1 * 60 * 1000;
    private static final int TWO_MINUTE = 2 * 60 * 1000;
    private static final int THREE_MINUTES = 3 * 60 * 1000;
    private static final int FOUR_MINUTES = 4 * 60 * 1000;
    private static final int FIVE_MINUTES = 5 * 60 * 1000;


    public BasicRandomAttackingOpponent(GameBoardData gameBoardData) {
        this.gameBoardData = gameBoardData;
        this.baseAttack = Lists.newArrayList(getBaseAttacks());
        start();
    }

    public void start() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public List<GameBoardCellEntity[]> getAttack() {
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;

        if (elapsed <= ONE_MINUTE) {
            if (percentChance(0.30)) {
                return generateAttack(getRandomNumberInRange(5, 50));
            }
        } else if (elapsed <= TWO_MINUTE) {
            if (percentChance(0.39)) {
                return generateAttack(getRandomNumberInRange(75, 140));
            }

        } else if (elapsed <= THREE_MINUTES) {
            if (percentChance(0.56)) {
                return generateAttack(getRandomNumberInRange(140, 240));
            }

        } else if (elapsed <= FOUR_MINUTES) {
            if (percentChance(0.63)) {
                return generateAttack(getRandomNumberInRange(300, 600));
            }
        } else if (elapsed <= FIVE_MINUTES) {
            if (percentChance(0.78)) {
                return generateAttack(getRandomNumberInRange(600, 800));
            }
        } else {
            if (percentChance(0.88)) {
                return generateAttack(getRandomNumberInRange(600, 1200));
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public void takeAttack(int amt) {

    }

    private List<GameBoardCellEntity[]> generateAttack(int amt) {
        List<GameBoardCellEntity[]> randomBaseAttack = getRandomBaseAttack();
        AttackSizeDetails attackSizeDetailForAmt = getAttackSizeDetailForAmt(amt);
        return alterAttackBasedOnDetails(attackSizeDetailForAmt, randomBaseAttack);
    }

    public List<GameBoardCellEntity[]> getRandomBaseAttack() {
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
            finalAttackAlteredBasedOnDetails.addAll(copyOf(alteredRowBaseAttack));
        }

        return finalAttackAlteredBasedOnDetails;
    }

    private List<GameBoardCellEntity[]> copyOf(List<GameBoardCellEntity[]> orig) {
        List<GameBoardCellEntity[]> copy = Lists.newArrayList();
        for (GameBoardCellEntity[] gameBoardCellEntities: orig) {
            GameBoardCellEntity[] copiedRow = new GameBoardCellEntity[orig.get(0).length];
            int pos = 0;
            for (GameBoardCellEntity ge: gameBoardCellEntities) {
                if (ge.getGameBlock().isPresent()) {
                    copiedRow[pos] = new GameBoardCellEntity(GameBlock.newCountDownBlockOfType(ge.getGameBlock().get().getType()));
                } else {
                    copiedRow[pos] = ge;
                }
                pos++;
            }
            copy.add(copiedRow);
        }

        return copy;
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

    public static Boolean percentChance(double chance) {
        return Math.random() <= chance;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
