package com.walkPark.walkinthepark.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Boon Sing on 16-Feb-18.
 */
@Parcel
public class Route {
    String id;
    String name;
    String image_url;
    String description;
    String estimated_steps;
    List<CheckPoint> checkpoints;

    public Route() {}

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = _id;
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

    public List<CheckPoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<CheckPoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
