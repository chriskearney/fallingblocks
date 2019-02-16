package com.comandante.game.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GameBoardDataSerialization {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public String serialize(GameBoardData gameBoardData) {
        return gson.toJson(gameBoardData);
    }

    public GameBoardData deserialize(String json) {
        return gson.fromJson(json, GameBoardData.class);
    }
}
