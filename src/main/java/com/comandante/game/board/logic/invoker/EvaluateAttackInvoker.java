package com.comandante.game.board.logic.invoker;

import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.opponents.BasicRandomAttackingOpponent;
import com.comandante.game.opponents.Opponent;

import java.util.List;
import java.util.Optional;

public class EvaluateAttackInvoker implements InvokerHarness.Invoker<List<GameBoardCellEntity[]>, Void> {

    private final Opponent opponent;

    public EvaluateAttackInvoker(Opponent opponent) {
        this.opponent = opponent;
    }

    @Override
    public Optional<List<GameBoardCellEntity[]>> invoke(Void aVoid) {
        List<GameBoardCellEntity[]> attack = opponent.getAttack();
        if (attack.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(attack);
    }

    @Override
    public int numberRoundsComplete() {
        return 0;
    }
}
