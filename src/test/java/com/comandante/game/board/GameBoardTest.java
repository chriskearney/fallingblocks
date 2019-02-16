package com.comandante.game.board;

import com.comandante.game.assetmanagement.TileSetGameBlockRenderer;
import com.comandante.game.board.logic.StandardGameBlockPairFactory;
import com.comandante.game.board.logic.StandardMagicGameBlockProcessor;
import com.comandante.game.textboard.TextBoard;
import com.comandante.game.ui.GamePanel;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.JFrame;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GameBoardTest {

    @Test
    public void TestGetLikeGroupsFromRow() throws IOException {
        TextBoard textBoard = new TextBoard(new int[10][20], new TileSetGameBlockRenderer("8bit"));
        String gameBoardDataJson = TestUtilities.readGameBoardState("TESTCASE_1.json");
        GameBoardDataSerialization gameBoardDataSerialization = new GameBoardDataSerialization();
        GameBoardData gameBoardData = gameBoardDataSerialization.deserialize(gameBoardDataJson);
        GameBoard gameBoard = new GameBoard(gameBoardData, new TileSetGameBlockRenderer("8bit"), new StandardGameBlockPairFactory(), new StandardMagicGameBlockProcessor(), textBoard);
        gameBoard.calculatePermaGroups();
        List<BlockGroup> permaGroups = gameBoard.getPermaGroupManager().getPermaGroups();
        Optional<BlockGroup> first = permaGroups.stream().filter(blockGroup -> blockGroup.groupOfBlocks.get(0).size() == 4).findFirst();
        BlockGroup blockGroup = first.get();
        Assert.assertEquals(blockGroup.getByXandY(1, 1).getIdentifier(), UUID.fromString("1297c647-e99a-481b-aa78-e7ce05b30a01"));
        Assert.assertEquals(blockGroup.getByXandY(2, 1).getIdentifier(), UUID.fromString("a89d2011-dad4-4727-ae5b-f6316edd5962"));
        Assert.assertEquals(blockGroup.getByXandY(3, 1).getIdentifier(), UUID.fromString("626d7eae-0ea3-4881-a623-9b9e7bee832a"));
        Assert.assertEquals(blockGroup.getByXandY(4, 1).getIdentifier(), UUID.fromString("1ccfbc85-45c4-4306-9592-cbc6228ffe94"));
        Assert.assertEquals(blockGroup.getByXandY(1, 2).getIdentifier(), UUID.fromString("4fad86e7-cb14-40d3-96ed-87252deff155"));
        Assert.assertEquals(blockGroup.getByXandY(2, 2).getIdentifier(), UUID.fromString("e9aa78bf-3d36-4fbc-975d-19bb598e9abc"));
        Assert.assertEquals(blockGroup.getByXandY(3, 2).getIdentifier(), UUID.fromString("e7f23eb5-98ec-4087-8160-3b42e95207c9"));
        Assert.assertEquals(blockGroup.getByXandY(4, 2).getIdentifier(), UUID.fromString("3fca4d32-aba9-4168-937f-98737b28805f"));
    }

    // Useful for getting a handle on json exports of the board, visually
    @Test
    public void testRenderFromJson() throws IOException, InterruptedException {
        TileSetGameBlockRenderer tileSetBlockRenderProcessor = new TileSetGameBlockRenderer("8bit");
        TextBoard textBoard = new TextBoard(new int[27][32], tileSetBlockRenderProcessor);
        String gameBoardDataJson = TestUtilities.readGameBoardState("TESTCASE_3.json");
        GameBoardDataSerialization gameBoardDataSerialization = new GameBoardDataSerialization();
        GameBoardData gameBoardData = gameBoardDataSerialization.deserialize(gameBoardDataJson);
        GameBoard gameBoard = new GameBoard(gameBoardData, tileSetBlockRenderProcessor, new StandardGameBlockPairFactory(), new StandardMagicGameBlockProcessor(), textBoard);
        GamePanel gamePanel = new GamePanel(gameBoard, textBoard);
        JFrame jFrame = new JFrame();
        jFrame.getContentPane().add(gamePanel);
        jFrame.pack();
        jFrame.setVisible(true);
        gameBoard.calculatePermaGroups();
        while (true) {
            Thread.sleep(1200);
        }
    }
}