package com.comadante;

public class BlockPair {

    private final GameBlock blockA;
    private final GameBlock blockB;

    public BlockPair(GameBlock blockA, GameBlock blockB) {
        this.blockA = blockA;
        this.blockB = blockB;
    }

    public GameBlock getBlockA() {
        return blockA;
    }

    public GameBlock getBlockB() {
        return blockB;
    }
}
