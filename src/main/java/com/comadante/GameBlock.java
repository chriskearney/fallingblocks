package com.comadante;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GameBlock {

    enum Type {
        BLUE,
        GREEN,
        RED,
        YELLOW,
        MAGIC_BLUE(true),
        MAGIC_GREEN(true),
        MAGIC_RED(true),
        MAGIC_YELLOW(true),
        EMPTY;

        private final boolean magic;

        Type(boolean magic) {
            this.magic = magic;
        }

        Type() {
            this(false);
        }

        public boolean isMagic() {
            return magic;
        }

        public static Type[] getNormalRandomPool() {
            ArrayList<Type> normalBlockTypes = Arrays.asList(values()).stream().filter(type -> !type.magic).collect(Collectors.toCollection(ArrayList::new));
            normalBlockTypes.remove(EMPTY);
            return normalBlockTypes.toArray(new Type[0]);
        }

        public static Type[] getMagicRandomPool() {
            ArrayList<Type> magicBlockTypes = Arrays.asList(values()).stream().filter(type -> type.magic).collect(Collectors.toCollection(ArrayList::new));
            magicBlockTypes.remove(EMPTY);
            return magicBlockTypes.toArray(new Type[0]);
        }
    }
    private final static Random RANDOM = new Random();
    private final static List<Type> NORMAL_VALUES = Collections.unmodifiableList(Arrays.asList(Type.getNormalRandomPool()));
    private final static int NORMAL_VALUES_SIZE = NORMAL_VALUES.size();

    private final static List<Type> MAGIC_VALUES = Collections.unmodifiableList(Arrays.asList(Type.getMagicRandomPool()));
    private final static int MAGIC_VALUES_SIZE = NORMAL_VALUES.size();

    private final Type type;

    public GameBlock(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public static GameBlock randomNormalBlock() {
        Type randomType = NORMAL_VALUES.get(RANDOM.nextInt(NORMAL_VALUES_SIZE));
        return new GameBlock(randomType);
    }


    public static GameBlock randomMagicBlock() {
        Type randomType = NORMAL_VALUES.get(RANDOM.nextInt(NORMAL_VALUES_SIZE));
        return new GameBlock(randomType);
    }
}
