package com.comandante.game.board;

import java.util.*;
import java.util.stream.Collectors;

public class GameBlock {

    private final static Random RANDOM = new Random();
    private final static List<Type> NORMAL_VALUES = Collections.unmodifiableList(Arrays.asList(Type.getNormalRandomPool()));
    private final static int NORMAL_VALUES_SIZE = NORMAL_VALUES.size();

    private final static List<Type> RANDOM_VALUES = Collections.unmodifiableList(Arrays.asList(Type.getRandomPool()));
    private final static int RANDOM_VALUE_SIZE = RANDOM_VALUES.size();

    private final Type type;
    private boolean resting = false;

    private GameBlock(Type type) {
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
        Type randomType = RANDOM_VALUES.get(RANDOM.nextInt(RANDOM_VALUE_SIZE));
        return new GameBlock(randomType);
    }

    public void setResting(boolean resting) {
        this.resting = resting;
    }

    public boolean isResting() {
        return resting;
    }

    public enum BorderType {
        TOP_LEFT,
        LEFT,
        LEFT_BOTTOM,
        BOTTOM,
        BOTTOM_RIGHT,
        RIGHT,
        TOP,
        TOP_RIGHT;
    }

    public enum Type {
        BLUE,
        GREEN,
        RED,
        YELLOW,
        MAGIC_BLUE(Optional.of(BLUE)),
        MAGIC_GREEN(Optional.of(GREEN)),
        MAGIC_RED(Optional.of(RED)),
        MAGIC_YELLOW(Optional.of(YELLOW)),
        EMPTY;

        private Optional<Type> magicRelated;

        Type(Optional<Type> magicRelated) {
            this.magicRelated = magicRelated;
        }

        Type() {
            this.magicRelated = Optional.empty();
        }

        public boolean isMagic() {
            return magicRelated.isPresent();
        }

        public Optional<Type> getRelated() {
            return magicRelated;
        }

        public static Type[] getNormalRandomPool() {
            ArrayList<Type> normalBlockTypes = Arrays.asList(values()).stream().filter(type -> !type.isMagic()).collect(Collectors.toCollection(ArrayList::new));
            normalBlockTypes.remove(EMPTY);
            return normalBlockTypes.toArray(new Type[0]);
        }

        public static Type[] getRandomPool() {
            List<Type> randoms = new ArrayList<>();
            for (Type t : Type.values()) {
                if (t.isMagic()) {
                    randoms.add(t);
                }
            }
            return randoms.toArray(new Type[0]);
        }

    }
}
