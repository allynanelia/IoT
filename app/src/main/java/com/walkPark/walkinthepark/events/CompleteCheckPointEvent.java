package com.walkPark.walkinthepark.events;

import com.walkPark.walkinthepark.models.CheckpointDetails;

/**
 * Created by Boon Sing on 28-Feb-18.
 */

public class CompleteCheckPointEvent {
    private final CheckpointDetails checkPointDetails;

    public CompleteCheckPointEvent(CheckpointDetails checkPointDetails) {
        this.checkPointDetails = checkPointDetails;
    }

    public CheckpointDetails getCheckPointDetails() {
        return checkPointDetails;
    }
}
