package com.comandante.game.board;

import com.google.gson.Gson;

public class GameBoardDataSerialization {

    private final Gson gson = new Gson();


    public String serialize(GameBoardData gameBoardData) {
        return gson.toJson(gameBoardData);
    }

    public GameBoardData deserialize(String json) {
        return gson.fromJson(json, GameBoardData.class);
    }
}
