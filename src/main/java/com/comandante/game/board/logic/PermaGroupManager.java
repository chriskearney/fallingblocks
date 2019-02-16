package com.comandante.game.board.logic;

import com.comandante.game.board.BlockGroup;
import com.comandante.game.board.GameBlock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermaGroupManager {


    Optional<UUID> getPermaGroupForBlock(GameBlock block);

    void createPermagroup(BlockGroup blockGroup);

    boolean areAnyBlocksPartOfPermaGroup(BlockGroup blockGroup);

    List<BlockGroup> getPermaGroups();

    BlockGroup get(UUID uuid);

    void reset();
}
