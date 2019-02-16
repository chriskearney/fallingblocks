package com.comandante.game.board;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BlockGroup {


    private final UUID blockGroupId;
    List<List<GameBoardCellEntity>> groupOfBlocks = Lists.newArrayList();

    public BlockGroup() {
        blockGroupId = UUID.randomUUID();
    }

    public void addRow(GameBoardCellEntity[] row) {
        List<GameBoardCellEntity> gameBoardCellEntities = Arrays.asList(row);
        groupOfBlocks.add(gameBoardCellEntities);
    }

    public GameBoardCellEntity[][] getRawGroup() {
        GameBoardCellEntity[][] rawGroup = new GameBoardCellEntity[groupOfBlocks.size()][];
        GameBoardCellEntity[] blankArray = new GameBoardCellEntity[0];
        for (int i = 0; i < groupOfBlocks.size(); i++) {
            rawGroup[i] = groupOfBlocks.get(i).toArray(blankArray);
        }
        return rawGroup;
    }

    public int size() {
        return groupOfBlocks.size();
    }

    public int getX() {
        return groupOfBlocks.get(0).size();
    }

    public int getY() {
        return groupOfBlocks.size();
    }

    public int getArea() {
        return getX() * getY();
    }

    public List<GameBlock> getAllGameBlocks() {
        List<GameBlock> allBlocks = Lists.newArrayList();
        groupOfBlocks.forEach(gameBoardCellEntities -> {
            for (GameBoardCellEntity boardCellEntity : gameBoardCellEntities) {
                if (boardCellEntity.getGameBlock().isPresent()) {
                    allBlocks.add(boardCellEntity.getGameBlock().get());
                }
            }
        });
        return allBlocks;
    }

    public GameBlock getByXandY(int x, int y) {
        x = x - 1;
        y = y - 1;
        return groupOfBlocks.get(y).get(x).getGameBlock().get();
    }

    public UUID getBlockGroupId() {
        return blockGroupId;
    }
}
