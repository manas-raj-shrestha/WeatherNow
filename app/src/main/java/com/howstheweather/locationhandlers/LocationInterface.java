package com.howstheweather.locationhandlers;

/**
 * Handles the location callback on Successful or failure to get location from device
 */
public interface LocationInterface {
    void getLocation(LocationCatcher.LocationCallBack locationCallBack);
}