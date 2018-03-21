package com.walkPark.walkinthepark;

import android.content.Context;
import android.content.SharedPreferences;

import com.walkPark.walkinthepark.models.UserInfo;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class Prefs {
    //This is for e.g. Tutorial walk through
    //Change profile pic
    //notification
    //Env.
    public static UserInfo user;

    public static UserInfo getUserProfile() {
        //Reminder: Call json service and return immediately.
        //This is for simplicity usage, correct procedure should be checking if shared pref exist
        //Else return json.
        if(user == null) {
            return new UserInfo();
        } else{
            return user;
        }
    }

    public static void setUser(UserInfo userIf) {
        user = userIf;
    }
}
