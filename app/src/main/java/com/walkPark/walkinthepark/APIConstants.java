package com.walkPark.walkinthepark;

/**
 * Created by Boon Sing on 16-Feb-18.
 */

public class APIConstants {

    //This page basically stores all the api calls URL as well as the response code.
    public static final String DEVICE_TYPE = "Android";

    //For api
    public static final String API_BASE_URL = "";

    //For response code
    public static final int STATUS_CODE_OK = 0; //No error
    public static final int STATUS_CODE_BAD_REQUEST = 400; //Bad request, invalid data
    public static final int STATUS_CODE_UNAUTHORIZED = 401; //Basic authentication invalidate
    public static final int STATUS_CODE_NOT_FOUND = 404; //Could not find content
    public static final int STATUS_CODE_TOKEN_INVALIDATE = 405; //Token invalidate
    public static final int STATUS_CODE_TOKEN_EXPIRED = 406; //Token expired


}
