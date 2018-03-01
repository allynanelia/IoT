package com.walkPark.walkinthepark.models;

import java.util.List;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class Route {
    String _id;
    String name;
    String image_url;
    String description;
    String estimated_steps;
    List<String> checkpoints;

    public Route() {}

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEstimated_steps() {
        return estimated_steps;
    }

    public void setEstimated_steps(String estimated_steps) {
        this.estimated_steps = estimated_steps;
    }

    public List<String> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<String> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
