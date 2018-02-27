package com.walkPark.walkinthepark.models;

import java.util.ArrayList;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class Game {
    String game_id;
    String game_name;
    String game_image_url;
    String game_description;
    String game_est_steps;
    ArrayList<String> checkpoints;

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

    public String getGame_image_url() {
        return game_image_url;
    }

    public void setGame_image_url(String game_image_url) {
        this.game_image_url = game_image_url;
    }

    public String getGame_description() {
        return game_description;
    }

    public void setGame_description(String game_description) {
        this.game_description = game_description;
    }

    public String getGame_est_steps() {
        return game_est_steps;
    }

    public void setGame_est_steps(String game_est_steps) {
        this.game_est_steps = game_est_steps;
    }

    public ArrayList<String> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(ArrayList<String> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
