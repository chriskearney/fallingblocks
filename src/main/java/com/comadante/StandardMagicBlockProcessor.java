package com.comadante;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


public class StandardMagicBlockProcessor implements MagicBlockProcessor {

    @Override
    public boolean process(GameBoard gameBoard) {
        boolean wasMagic = false;
        List<CellEntity> cellEntitiesWithMagicBlocks = getCellEntitiesWithMagicBlocks(gameBoard);
        for (CellEntity cellEntity : cellEntitiesWithMagicBlocks) {
            if (processMagic(gameBoard, cellEntity, cellEntity.getType().getRelated().get())) {
                wasMagic = true;
                // There was magic, lets remove all of the "tombstoned" cells
                    gameBoard.runOnEveryCellEntity((invocationNumber, currentCords) -> {
                        Optional<CellEntity> cellEntityOptional = gameBoard.getCellEntity(currentCords);
                        if (cellEntityOptional.isPresent() && cellEntityOptional.get().isMarkedForDestruction()) {
                            CellEntity entity = cellEntityOptional.get();
                            gameBoard.getCellEntities()[currentCords.i][currentCords.j] =
                                    new CellEntity(entity.getId(), entity.getGameBoardCoords(), null);
                        }
                    });
                    gameBoard.repaint();

            }
        }

        return wasMagic;
    }

    private boolean processMagic(GameBoard gameBoard, CellEntity cellEntity, GameBlock.Type targetType) {
        boolean wasMagic = false;
        List<CellEntity> likeNeighbors = destroyLikeNeighbors(gameBoard, cellEntity, targetType);
        if (!likeNeighbors.isEmpty()) {
            wasMagic = true;
            for (CellEntity entity : likeNeighbors) {
                processMagic(gameBoard, entity, targetType);
            }
        }
        return wasMagic;
    }


    private List<CellEntity> getCellEntitiesWithMagicBlocks(GameBoard gameBoard) {
        List<CellEntity> magicCellEntities = new ArrayList<>();
        Iterator<CellEntity[]> iteratorOfRowsFromBottom = GameBoardUtil.getIteratorOfRowsFromBottom(gameBoard.getCellEntities());
        while (iteratorOfRowsFromBottom.hasNext()) {
            CellEntity[] cellEntities = iteratorOfRowsFromBottom.next();
            for (CellEntity cellEntity : cellEntities) {
                if (cellEntity.getType().isMagic()) {
                    magicCellEntities.add(cellEntity);
                }
            }
        }
        return magicCellEntities;
    }

    private List<CellEntity> destroyLikeNeighbors(GameBoard gameBoard, CellEntity cellEntity, GameBlock.Type targetType) {
        List<CellEntity> destroyedNeighbors = new ArrayList<>();
        if ((cellEntity.getType().equals(targetType) || (cellEntity.getType().getRelated().isPresent() && cellEntity.getType().getRelated().get().equals(targetType)))) {
            List<CellEntity> occupiedNeighbors = gameBoard.getOccupiedNeighbors(cellEntity, targetType);
            for (CellEntity ce : occupiedNeighbors) {
                if (!ce.isMarkedForDestruction()) {
                    ce.setMarkedForDestruction(true);
                    destroyedNeighbors.add(ce);
                }
            }
        }
        return destroyedNeighbors;
    }
}
