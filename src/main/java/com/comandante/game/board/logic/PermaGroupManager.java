package com.comandante.game.board.logic;

import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoard;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermaGroupManager {


    Optional<UUID> getPermaGroupForBlock(GameBlock block);

    void createPermagroup(GameBoard.BlockGroup blockGroup);

    boolean areAnyBlocksPartOfPermaGroup(GameBoard.BlockGroup blockGroup);

    List<GameBoard.BlockGroup> getPermaGroups();

    GameBoard.BlockGroup get(UUID uuid);

    void reset();
}
