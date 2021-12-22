package com.example.cashbooster;

public class FirestoreBucketObj {

    String Amount,GameCode,GameState,currentTime, GameType,Id;

    public FirestoreBucketObj(String amount, String gameCode, String gameState, String currentTime, String gameType, String id) {
        this.Amount = amount;
        this.GameCode = gameCode;
        this.GameState = gameState;
        this.currentTime = currentTime;
        this.GameType = gameType;
        this.Id = id;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getGameCode() {
        return GameCode;
    }

    public void setGameCode(String gameCode) {
        GameCode = gameCode;
    }

    public String getGameState() {
        return GameState;
    }

    public void setGameState(String gameState) {
        GameState = gameState;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getGameType() {
        return GameType;
    }

    public void setGameType(String gameType) {
        GameType = gameType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
