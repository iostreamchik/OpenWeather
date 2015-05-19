package com.openweather_test.app.server_api;

public class RequestHelper {
	public static final String MAIN_URL = "http://api.openweathermap.org/";
	public static final String WEATHER = MAIN_URL + "data/2.5/weather?";
	public static final String CITY = WEATHER + "q=";

	public String getWeatherByCity(String city) {
		return CITY + city;
	}

}

