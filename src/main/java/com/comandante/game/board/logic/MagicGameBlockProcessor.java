package com.comandante.game.board.logic;

import com.comandante.game.board.GameBoard;

import java.util.Optional;

public interface MagicGameBlockProcessor {

    boolean process(GameBoard gameBoard);

    Optional<ScoringDetails> destroyCellEntitiesThatAreMarkedForDeletion(GameBoard gameBoard);

    void processDiamondBlocks(GameBoard gameBoard);

    public static ScoringDetails getBlank() {
        ScoringDetails scoringDetails = new ScoringDetails();
        scoringDetails.base = 0;
        return scoringDetails;
    }

    public static class ScoringDetails {
        public int base;
        public int bonus;
        public int chainReactionScore;

        public int getScore() {
            return base + bonus + chainReactionScore;
        }
    }
}
