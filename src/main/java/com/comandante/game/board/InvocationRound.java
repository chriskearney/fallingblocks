package com.comandante.game.board;

import java.util.Optional;

public class InvocationRound<T> {

    private final Invoker<T> invoker;
    private boolean useLastReturn = false;
    private Optional<Runnable> invokeRoundCompleteHandler = Optional.empty();

    public void setInvokeRoundCompleteHandler(Optional<Runnable> invokeRoundCompleteHandler) {
        this.invokeRoundCompleteHandler = invokeRoundCompleteHandler;
    }

    private T lastReturn;

    private int numberOfInvocationsPerRound;
    private int currentRoundInvocationCount = 0;

    public InvocationRound(int numberOfInvocationsPerRound, Invoker<T> invoker) {
        this.invoker = invoker;
        this.numberOfInvocationsPerRound = numberOfInvocationsPerRound;
    }

    public InvocationRound(int numberOfInvocationsPerRound, Invoker<T> invoker, boolean useLastReturn) {
        this.useLastReturn = useLastReturn;
        this.numberOfInvocationsPerRound = numberOfInvocationsPerRound;
        this.invoker = invoker;
    }

    public void setNumberOfInvocationsPerRound(int numberOfInvocationsPerRound) {
        this.numberOfInvocationsPerRound = numberOfInvocationsPerRound;
    }

    public Optional<T> invoke() {
        if (invoker.maxRounds() > 0 && invoker.numberRoundsComplete() > invoker.maxRounds() && invokeRoundCompleteHandler.isPresent()) {
            invokeRoundCompleteHandler.get().run();
        }
        if (processRoundStatus()) {
            Optional<T> invoke = invoker.invoke();
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

    public interface Invoker<T> {
        Optional<T> invoke();
        int numberRoundsComplete();
        default int maxRounds() {
            return -1;
        }
    }
}
