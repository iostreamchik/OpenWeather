package com.openweather_test.app.entity;

import java.util.ArrayList;

public class CityModel {
	private Coord coord;
	private Sys sys;
	private ArrayList<Weather> weather;
	private String base;
	private Main main;
	private Wind wind;
	private Clouds clouds;
	private long dt;
	private int id;
	private String name;
	private int cod;

	public Coord getCoord() {
		return coord;
	}

	public Sys getSys() {
		return sys;
	}

	public ArrayList<Weather> getWeather() {
		return weather;
	}

	public String getBase() {
		return base;
	}

	public Main getMain() {
		return main;
	}

	public Wind getWind() {
		return wind;
	}

	public Clouds getClouds() {
		return clouds;
	}

	public long getDt() {
		return dt;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getCod() {
		return cod;
	}
}
