package com.walkPark.walkinthepark.events;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class UpdateBottomBarEvent {
    private final String fragment_title;

    public UpdateBottomBarEvent(String fragment_title) {
        this.fragment_title = fragment_title;
    }

    public String getFragment_title() {
        return fragment_title;
    }
}
