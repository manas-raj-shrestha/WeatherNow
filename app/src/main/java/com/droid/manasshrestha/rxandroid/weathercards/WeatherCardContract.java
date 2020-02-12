package com.droid.manasshrestha.rxandroid.weathercards;

import android.graphics.drawable.Drawable;


import com.droid.manasshrestha.rxandroid.textModels.Temp;

import lecho.lib.hellocharts.model.LineChartData;

/**
 * Created by ManasShrestha on 3/29/16.
 */
public interface WeatherCardContract {

    interface Views {
        void setWeekDay(String weekDay);

        void setWeatherIcon(Drawable drawable);

        void setHumidity(int humidity);

        void setAvgTemp(int temp);

        void setClouds(int clouds);

        void setCardBackground(int colorId);

        void setAtmosphericPressure(String pressure);

        void setWindSpeed(String windSpeed);

        void setWindDirection(String windDirection);

        void setTemperature(Temp temperature);

        void setWeatherDesc(String desc);

        void setLineSetData(LineChartData lineChartData);
    }

}