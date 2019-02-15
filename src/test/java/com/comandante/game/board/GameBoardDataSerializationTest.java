package com.comandante.game.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class GameBoardDataSerializationTest {

    GameBoardData gameBoardData;

    @Before
    public void setUp() throws Exception {
        gameBoardData = new GameBoardData(new int[10][20]);
        GameBoardCellEntity gameBoardCellEntity[] = new GameBoardCellEntity[10];
        gameBoardCellEntity[0] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntity[1] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntity[2] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntity[3] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        UUID block1x1 = UUID.randomUUID();
        UUID block2x1 = UUID.randomUUID();
        UUID block3x1 = UUID.randomUUID();
        UUID block4x1 = UUID.randomUUID();
        gameBoardCellEntity[4] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN, block1x1));
        gameBoardCellEntity[5] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN, block2x1));
        gameBoardCellEntity[6] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN, block3x1));
        gameBoardCellEntity[7] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN, block4x1));
        gameBoardCellEntity[8] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.BLUE));
        gameBoardCellEntity[9] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.BLUE));

        GameBoardCellEntity gameBoardCellEntityRowStepAbove[] = new GameBoardCellEntity[10];
        gameBoardCellEntityRowStepAbove[0] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove[1] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove[2] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove[3] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));

        UUID block1x2 = UUID.randomUUID();
        UUID block2x2 = UUID.randomUUID();
        UUID block3x2 = UUID.randomUUID();
        UUID block4x2 = UUID.randomUUID();


        GameBlock gameBlock1x2 = new GameBlock(GameBlock.Type.GREEN, block1x2);

        gameBoardCellEntityRowStepAbove[4] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), gameBlock1x2);
        gameBoardCellEntityRowStepAbove[5] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN, block2x2));
        gameBoardCellEntityRowStepAbove[6] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN, block3x2));
        gameBoardCellEntityRowStepAbove[7] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN, block4x2));
        gameBoardCellEntityRowStepAbove[8] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.BLUE));
        gameBoardCellEntityRowStepAbove[9] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.BLUE));

        GameBoardCellEntity gameBoardCellEntityRowStepAbove2[] = new GameBoardCellEntity[10];
        gameBoardCellEntityRowStepAbove2[0] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove2[1] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove2[2] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove2[3] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove2[4] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove2[5] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN));
        gameBoardCellEntityRowStepAbove2[6] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0), new GameBlock(GameBlock.Type.GREEN));
        gameBoardCellEntityRowStepAbove2[7] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove2[8] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));
        gameBoardCellEntityRowStepAbove2[9] = new GameBoardCellEntity(0, new GameBoardCoords(0, 0));


        gameBoardData.setRow(gameBoardData.getCellEntities()[0].length - 1, gameBoardCellEntity);
        gameBoardData.setRow(gameBoardData.getCellEntities()[0].length - 2, gameBoardCellEntityRowStepAbove);
        gameBoardData.setRow(gameBoardData.getCellEntities()[0].length - 3, gameBoardCellEntityRowStepAbove2);
    }

    @Test
    public void testGameDataSerialization() throws Exception {

        GameBoardDataSerialization gameBoardDataSerialization = new GameBoardDataSerialization();
        String serialize = gameBoardDataSerialization.serialize(gameBoardData);

        GameBoardData deserialize = gameBoardDataSerialization.deserialize(serialize);

        int deserializedId = deserialize.getCellEntity(new GameBoardCoords(2, 5)).get().getId();
        int originalId = gameBoardData.getCellEntity(new GameBoardCoords(2, 5)).get().getId();
        ;

        Assert.assertEquals(originalId, deserializedId);

    }

}