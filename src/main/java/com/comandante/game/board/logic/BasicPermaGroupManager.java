package com.comandante.game.board.logic;

import com.comandante.game.board.BlockGroup;
import com.comandante.game.board.GameBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class BasicPermaGroupManager implements PermaGroupManager {

    Map<UUID, BlockGroup> permaGroups = new HashMap<>();

    @Override
    public Optional<UUID> getPermaGroupForBlock(GameBlock block) {
        Optional<Map.Entry<UUID, BlockGroup>> first = permaGroups.entrySet().stream().filter(uuidBlockGroupEntry -> {
            List<GameBlock> allGameBlocks = uuidBlockGroupEntry.getValue().getAllGameBlocks();
            return allGameBlocks.stream().anyMatch(b -> b.getIdentifier().equals(block.getIdentifier()));
        }).findFirst();
        return first.map(Map.Entry::getKey);
    }

    @Override
    public void createPermagroup(BlockGroup blockGroup) {
        permaGroups.put(UUID.randomUUID(), blockGroup);

    }

    @Override
    public boolean areAnyBlocksPartOfPermaGroup(BlockGroup blockGroup) {
        for (GameBlock gameBlock: blockGroup.getAllGameBlocks()) {
            if (getPermaGroupForBlock(gameBlock).isPresent()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BlockGroup> getPermaGroups() {
        List<BlockGroup> allGroups = new ArrayList<>();
        for (Map.Entry<UUID, BlockGroup> next : permaGroups.entrySet()) {
            allGroups.add(next.getValue());
        }
        return allGroups;
    }

    @Override
    public void reset() {
        permaGroups = new HashMap<>();
    }

    @Override
    public BlockGroup get(UUID uuid) {
        return permaGroups.get(uuid);
    }
}
