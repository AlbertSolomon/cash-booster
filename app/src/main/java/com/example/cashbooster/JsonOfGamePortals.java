package com.example.cashbooster;

public class JsonOfGamePortals {
    private String userID;
    private String gameState;
    private String gameStart;
    private Integer gameAmount;
    private Integer gameCode;

    public JsonOfGamePortals(){}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }


    public String getGameStart() {
        return gameStart;
    }

    public void setGameStart(String gameStart) {
        this.gameStart = gameStart;
    }

    public Integer getGameAmount() {
        return gameAmount;
    }

    public void setGameAmount(Integer gameAmount) {
        this.gameAmount = gameAmount;
    }

    public Integer getGameCode() {
        return gameCode;
    }

    public void setGameCode(Integer gameCode) {
        this.gameCode = gameCode;
    }
}
