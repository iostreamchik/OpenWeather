package com.openweather_test.app.server_api;

import android.os.Bundle;

public class RequestHelper {
	public static final String MAIN_URL = "http://api.openweathermap.org/";
	public static final String WEATHER = MAIN_URL + "data/2.5/weather?";
	public static final String CITY = WEATHER + "q=";
	public static final String ICON = MAIN_URL + "img/w/";

	public static Bundle getWeatherByCity(String city) {
		Bundle bundle = new Bundle();
		bundle.putSerializable(RequestLoader.EXTRA_REQUEST_TYPE, RequestType.GET);
		bundle.putString(RequestLoader.EXTRA_REQUEST_STRING, CITY + city);
		return bundle;
	}

}

