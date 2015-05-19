package com.openweather_test.app.entity;

import com.google.gson.annotations.SerializedName;

public class Main {
	private double temp;
	@SerializedName("temp_min")
	private double tempMin;
	@SerializedName("temp_max")
	private double tempMax;
	private double pressure;
	@SerializedName("sea_level")
	private double seaLevel;
	@SerializedName("grnd_level")
	private double grndLevel;
	private int humidity;


	public double getTemp() {
		return temp;
	}

	public double getTempMin() {
		return tempMin;
	}

	public double getTempMax() {
		return tempMax;
	}

	public double getPressure() {
		return pressure;
	}

	public double getSeaLevel() {
		return seaLevel;
	}

	public double getGrndLevel() {
		return grndLevel;
	}

	public int getHumidity() {
		return humidity;
	}
}
