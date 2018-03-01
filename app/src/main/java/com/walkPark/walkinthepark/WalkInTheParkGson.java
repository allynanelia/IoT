package com.walkPark.walkinthepark;

import com.google.gson.Gson;

/**
 * Created by Boon Sing on 01-Mar-18.
 */

public class WalkInTheParkGson {

    private static Gson gsonClient;

    public static Gson getInstance() {
        if (gsonClient == null) {
            gsonClient = new Gson();
        }
        return gsonClient;
    }

    public WalkInTheParkGson() {}
}
