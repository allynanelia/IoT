package com.walkPark.walkinthepark.models;

import org.parceler.Parcel;

/**
 * Created by Boon Sing on 28-Feb-18.
 */
@Parcel
public class CheckPoint {
    String id;
    String route_id;
    String title;
    String image_url_hint;
    String hint_description;
    String image_url_found;
    String found_description;
    String steps;
    String points;
    String time_taken;
    String status;
    String beacon_instance_id;

    public CheckPoint() {
    }

    public String getTime_taken() {
        return time_taken;
    }

    public void setTime_taken(String time_taken) {
        this.time_taken = time_taken;
    }

    public String getBeacon_instance_id() {
        return beacon_instance_id;
    }

    public void setBeacon_instance_id(String beacon_instance_id) {
        this.beacon_instance_id = beacon_instance_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_url_hint() {
        return image_url_hint;
    }

    public void setImage_url_hint(String image_url_hint) {
        this.image_url_hint = image_url_hint;
    }

    public String getHint_description() {
        return hint_description;
    }

    public void setHint_description(String hint_description) {
        this.hint_description = hint_description;
    }

    public String getImage_url_found() {
        return image_url_found;
    }

    public void setImage_url_found(String image_url_found) {
        this.image_url_found = image_url_found;
    }

    public String getFound_description() {
        return found_description;
    }

    public void setFound_description(String found_description) {
        this.found_description = found_description;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
