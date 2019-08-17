package com.comandante.game.board;

import java.util.Optional;

public class InvocationRound<T> {

    private final Invoker<T> invoker;

    private int numberOfInvocationsPerRound;
    private int currentRoundInvocationCount = 0;

    public InvocationRound(int numberOfInvocationsPerRound, Invoker<T> invoker) {
        this.numberOfInvocationsPerRound = numberOfInvocationsPerRound;
        this.invoker = invoker;
    }

    public void setNumberOfInvocationsPerRound(int numberOfInvocationsPerRound) {
        this.numberOfInvocationsPerRound = numberOfInvocationsPerRound;
    }

    public Optional<T> invoke() {
        if (processRoundStatus()) {
            return invoker.invoke();
        }
        return Optional.empty();
    }

    private boolean processRoundStatus() {
        currentRoundInvocationCount++;
        if (currentRoundInvocationCount < numberOfInvocationsPerRound) {
            return false;
        }
        currentRoundInvocationCount = 0;
        return true;
    }

    public interface Invoker<T> {
        Optional<T> invoke();
    }
}
