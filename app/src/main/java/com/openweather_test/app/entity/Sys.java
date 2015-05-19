package com.openweather_test.app.entity;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class Sys {

	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

	private double message;
	private String country;
	private long sunrise;
	private long sunset;

	public double getMessage() {
		return message;
	}

	public String getCountry() {
		return country;
	}

	public long getSunrise() {
		return sunrise;
	}

	public String getSunriseFormatted() {
		return TIME_FORMAT.format(sunrise * 1000);
	}

	public long getSunset() {
		return sunset;
	}

	public String getSunsetFormatted() {
		return TIME_FORMAT.format(sunset * 1000);
	}
}
