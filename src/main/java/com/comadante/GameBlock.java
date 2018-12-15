package com.comadante;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameBlock {

    enum Type {
        BLUE,
        GREEN,
        RED,
        YELLOW,
        MAGIC_BLUE,
        MAGIC_GREEN,
        MAGIC_RED,
        MAGIC_YELLOW,
        EMPTY;

        public static Type[] getRandomPool() {
            ArrayList<Type> types = new ArrayList<>(Arrays.asList(values()));
            types.remove(EMPTY);
            return types.toArray(new Type[types.size()]);
        }
    }
    private final static Random RANDOM = new Random();
    private final static List<Type> VALUES = Collections.unmodifiableList(Arrays.asList(Type.getRandomPool()));
    private final static int SIZE = VALUES.size();

    private final Type type;

    public GameBlock(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public static GameBlock random() {
        Type randomType = VALUES.get(RANDOM.nextInt(SIZE));
        return new GameBlock(randomType);
    }
}
