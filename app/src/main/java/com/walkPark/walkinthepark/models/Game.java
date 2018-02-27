package com.walkPark.walkinthepark.models;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class Game {
    String game_id;
    String game_name;
    String game_image;
    String game_hint;
    String game_description;

    public Game() {
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getGame_image() {
        return game_image;
    }

    public void setGame_image(String game_image) {
        this.game_image = game_image;
    }

    public String getGame_hint() {
        return game_hint;
    }

    public void setGame_hint(String game_hint) {
        this.game_hint = game_hint;
    }

    public String getGame_description() {
        return game_description;
    }

    public void setGame_description(String game_description) {
        this.game_description = game_description;
    }

}
