package com.walkPark.walkinthepark.events;

import com.walkPark.walkinthepark.models.Game;

/**
 * Created by Boon Sing on 19-Feb-18.
 */

public class GameTriggerEvent {
    private final Game game;


    public GameTriggerEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }
}
