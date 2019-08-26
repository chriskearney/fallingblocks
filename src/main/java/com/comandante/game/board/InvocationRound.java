package com.comandante.game.board;

import java.util.Optional;

public class InvocationRound<T, R> {

    private final Invoker<T, R> invoker;
    private boolean useLastReturn = false;
    private Optional<Runnable> invokeRoundCompleteHandler = Optional.empty();

    public void setInvokeRoundCompleteHandler(Optional<Runnable> invokeRoundCompleteHandler) {
        this.invokeRoundCompleteHandler = invokeRoundCompleteHandler;
    }

    private T lastReturn;

    private int numberOfInvocationsPerRound;
    private int currentRoundInvocationCount = 0;

    public InvocationRound(int numberOfInvocationsPerRound, Invoker<T, R> invoker) {
        this.invoker = invoker;
        this.numberOfInvocationsPerRound = numberOfInvocationsPerRound;
    }

    public InvocationRound(int numberOfInvocationsPerRound, Invoker<T, R> invoker, boolean useLastReturn) {
        this.useLastReturn = useLastReturn;
        this.numberOfInvocationsPerRound = numberOfInvocationsPerRound;
        this.invoker = invoker;
    }

    public void setNumberOfInvocationsPerRound(int numberOfInvocationsPerRound) {
        this.numberOfInvocationsPerRound = numberOfInvocationsPerRound;
    }

    public Optional<T> invoker(R r) {
        if (invoker.maxRounds() > 0 && invoker.numberRoundsComplete() > invoker.maxRounds() && invokeRoundCompleteHandler.isPresent()) {
            invokeRoundCompleteHandler.get().run();
            return Optional.empty();
        }
        if (processRoundStatus()) {
            Optional<T> invoke = invoker.invoke(r);
            if (invoke.isPresent() && useLastReturn) {
                lastReturn = invoke.get();
            }
            return invoke;
        }

        return Optional.ofNullable(lastReturn);
    }

    private boolean processRoundStatus() {
        currentRoundInvocationCount++;
        if (currentRoundInvocationCount < numberOfInvocationsPerRound) {
            return false;
        }
        currentRoundInvocationCount = 0;
        return true;
    }

    public void setRunNow() {
        currentRoundInvocationCount = numberOfInvocationsPerRound;
    }

    public interface Invoker<T, R> {
        Optional<T> invoke(R r);
        int numberRoundsComplete();
        default int maxRounds() {
            return -1;
        }
    }
}
