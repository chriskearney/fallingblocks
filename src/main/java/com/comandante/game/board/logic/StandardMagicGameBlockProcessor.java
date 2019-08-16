package com.comandante.game.board.logic;

import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoard;
import com.comandante.game.board.GameBoardCoords;
import com.comandante.game.board.GameBoardUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class StandardMagicGameBlockProcessor implements MagicGameBlockProcessor {

    @Override
    public boolean process(GameBoard gameBoard) {
        boolean wasMagic = false;
        List<GameBoardCellEntity> cellEntitiesWithMagicBlocks = getCellEntitiesWithMagicBlocks(gameBoard);
        for (GameBoardCellEntity gameBoardCellEntity : cellEntitiesWithMagicBlocks) {
            if (processMagic(gameBoard, gameBoardCellEntity, gameBoardCellEntity.getType().getRelated().get())) {
                wasMagic = true;
                int destroyed = destroyCellEntitiesThatAreMarkedForDeletion(gameBoard);
                gameBoard.alterScore(destroyed);
                gameBoard.repaint();
            }
        }
        return wasMagic;
    }

    private int destroyCellEntitiesThatAreMarkedForDeletion(GameBoard gameBoard) {
        int destroyed = 0;
        Map<UUID, Integer> blocksDestroyedByGroup = Maps.newHashMap();
        Map<GameBlock.Type, Integer> blocksNotInAGroupDestroyedByType = Maps.newHashMap();
        List<GameBoardCellEntity> cellEntitiesMarkedForDeletion = getCellEntitiesMarkedForDeletion(gameBoard);
        for (GameBoardCellEntity ce : cellEntitiesMarkedForDeletion) {
            destroyed++;
            Optional<UUID> permaGroupForBlock = gameBoard.getPermaGroupManager().getPermaGroupForBlock(ce.getGameBlock().get());
            if (permaGroupForBlock.isPresent()) {
                blocksDestroyedByGroup.putIfAbsent(permaGroupForBlock.get(), 0);
                blocksDestroyedByGroup.put(permaGroupForBlock.get(), blocksDestroyedByGroup.get(permaGroupForBlock.get()) + 1);
            } else {
                GameBlock.Type gameBlockType = ce.getGameBlock().get().getType();
                if (gameBlockType.getRelated().isPresent()) {
                    gameBlockType = gameBlockType.getRelated().get();
                }
                blocksNotInAGroupDestroyedByType.putIfAbsent(gameBlockType, 0);
                blocksNotInAGroupDestroyedByType.put(gameBlockType, blocksNotInAGroupDestroyedByType.get(gameBlockType) + 1);
            }
            gameBoard.getGameBoardData().getCellEntities()[ce.getGameBoardCoords().i][ce.getGameBoardCoords().j] = new GameBoardCellEntity(ce.getId(), ce.getGameBoardCoords(), null);
        }

        int groupScoreTotal = 0;
        for (Map.Entry<UUID, Integer> next : blocksDestroyedByGroup.entrySet()) {
            int groupScore = next.getValue() * next.getValue() * 4;
            if (groupScore <= 16) {
                groupScore = groupScore + (int) (groupScore * .6);
            } else if (groupScore <= 256) {
                groupScore = groupScore + (int) (groupScore * .8);
            } else if (groupScore <= 512) {
                groupScore = groupScore + (int) (groupScore * 1);
            } else if (groupScore <= 1024) {
                groupScore = groupScore + (int) (groupScore * 1.2);
            } else if (groupScore <= 1512) {
                groupScore = groupScore + (int) (groupScore * 1.4);
            } else if (groupScore <= 2048) {
                groupScore = groupScore + (int) (groupScore * 1.6);
            } else if (groupScore <= 2512) {
                groupScore = groupScore + (int) (groupScore * 1.8);
            }
            groupScoreTotal += groupScore;
        }

        int typeScoreTotal = 0;
        for (Map.Entry<GameBlock.Type, Integer> next: blocksNotInAGroupDestroyedByType.entrySet()) {
            if (next.getValue() <= 1) {
                continue;
            }
            int typeScore = next.getValue() * next.getValue();
            typeScoreTotal =+ typeScore;
        }

        return destroyed + groupScoreTotal + typeScoreTotal;
    }

    private List<GameBoardCellEntity> getCellEntitiesMarkedForDeletion(GameBoard gameBoard) {
        List<GameBoardCellEntity> gameBoardCellEntities = Lists.newArrayList();
        for (GameBoardCellEntity ce : gameBoard.getGameBoardData().getCellsFromBottom()) {
            if (ce.isMarkedForDestruction()) {
                gameBoardCellEntities.add(ce);
            }
        }
        return gameBoardCellEntities;
    }

    private boolean processMagic(GameBoard gameBoard, GameBoardCellEntity gameBoardCellEntity, GameBlock.Type targetType) {
        boolean wasMagic = false;
        List<GameBoardCellEntity> likeNeighbors = destroyLikeNeighbors(gameBoard, gameBoardCellEntity, targetType);
        if (!likeNeighbors.isEmpty()) {
            wasMagic = true;
            for (GameBoardCellEntity entity : likeNeighbors) {
                processMagic(gameBoard, entity, targetType);
            }
        }
        return wasMagic;
    }


    private List<GameBoardCellEntity> getCellEntitiesWithMagicBlocks(GameBoard gameBoard) {
        List<GameBoardCellEntity> magicCellEntities = new ArrayList<>();
        for (GameBoardCellEntity ce : gameBoard.getGameBoardData().getCellsFromBottom()) {
            if (ce.getType().isMagic()) {
                magicCellEntities.add(ce);
            }
        }
        return magicCellEntities;
    }

    private List<GameBoardCellEntity> destroyLikeNeighbors(GameBoard gameBoard, GameBoardCellEntity gameBoardCellEntity, GameBlock.Type targetType) {
        List<GameBoardCellEntity> destroyedNeighbors = new ArrayList<>();
        if ((gameBoardCellEntity.getType().equals(targetType) || (gameBoardCellEntity.getType().getRelated().isPresent() && gameBoardCellEntity.getType().getRelated().get().equals(targetType)))) {
            List<GameBoardCellEntity> occupiedNeighbors = gameBoard.getGameBoardData().getOccupiedNeighborsOfType(gameBoardCellEntity, targetType);
            for (GameBoardCellEntity ce : occupiedNeighbors) {
                if (!ce.isMarkedForDestruction()) {
                    ce.setMarkedForDestruction(true);
                    destroyedNeighbors.add(ce);
                }
            }
        }
        return destroyedNeighbors;
    }

}
