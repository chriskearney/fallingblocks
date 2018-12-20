package com.comandante.game.board.logic;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoard;

import java.util.*;
import java.util.function.Predicate;

public class BasicPermaGroupManager implements PermaGroupManager {

    Map<UUID, GameBoard.BlockGroup> permaGroups = new HashMap<>();

    @Override
    public Optional<UUID> getPermaGroupForBlock(GameBlock block) {
        Optional<Map.Entry<UUID, GameBoard.BlockGroup>> first = permaGroups.entrySet().stream().filter(uuidBlockGroupEntry -> {
            List<GameBlock> allGameBlocks = uuidBlockGroupEntry.getValue().getAllGameBlocks();
            return allGameBlocks.stream().anyMatch(b -> b.getIdentifier().equals(block.getIdentifier()));
        }).findFirst();
        return first.map(Map.Entry::getKey);
    }

    @Override
    public void createPermagroup(GameBoard.BlockGroup blockGroup) {
        permaGroups.put(UUID.randomUUID(), blockGroup);

    }

    @Override
    public boolean areAnyBlocksPartOfPermaGroup(GameBoard.BlockGroup blockGroup) {
        for (GameBlock gameBlock: blockGroup.getAllGameBlocks()) {
            if (getPermaGroupForBlock(gameBlock).isPresent()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<GameBoard.BlockGroup> getPermaGroups() {
        List<GameBoard.BlockGroup> allGroups = new ArrayList<>();
        for (Map.Entry<UUID, GameBoard.BlockGroup> next : permaGroups.entrySet()) {
            allGroups.add(next.getValue());
        }
        return allGroups;
    }

    @Override
    public void reset() {
        permaGroups = new HashMap<>();
    }

    @Override
    public GameBoard.BlockGroup get(UUID uuid) {
        return permaGroups.get(uuid);
    }
}
