package com.comandante.game.board.logic;

import com.comandante.game.board.GameBoardCellEntity;
import com.comandante.game.board.GameBlock;
import com.comandante.game.board.GameBoard;
import com.comandante.game.board.GameBoardCoords;
import com.comandante.game.board.GameBoardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
                gameBoard.alterScore((int) Math.pow((destroyed), (destroyed * .5)));
                gameBoard.repaint();
            }
        }
        return wasMagic;
    }

    private int destroyCellEntitiesThatAreMarkedForDeletion(GameBoard gameBoard) {
        int destroyed = 0;
        for (GameBoardCellEntity ce : gameBoard.getGameBoardData().getCellsFromBottom()) {
           if (ce.isMarkedForDestruction()) {
               destroyed++;
               gameBoard.getGameBoardData().getCellEntities()[ce.getGameBoardCoords().i][ce.getGameBoardCoords().j] = new GameBoardCellEntity(ce.getId(), ce.getGameBoardCoords(), null);
           }
        }
        return  destroyed;
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
