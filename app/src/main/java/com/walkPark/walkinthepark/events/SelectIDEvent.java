package com.walkPark.walkinthepark.events;

/**
 * Created by singy on 3/20/2018.
 */

public class SelectIDEvent {

    private int playerID;

    public SelectIDEvent(int playerID) {
        this.playerID = playerID;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
