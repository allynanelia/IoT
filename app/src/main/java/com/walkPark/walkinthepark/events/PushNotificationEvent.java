package com.walkPark.walkinthepark.events;

import java.util.Map;

/**
 * Created by nanelia on 12/4/18.
 */

public class PushNotificationEvent {
    private Map<String, String> message;

    public PushNotificationEvent(Map<String, String> message) {
        this.message = message;
    }

    public Map<String, String> getMessage() {
        return message;
    }
}
