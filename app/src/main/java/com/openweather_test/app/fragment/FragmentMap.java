package com.openweather_test.app.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.view.*;
import android.widget.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.openweather_test.app.R;
import com.openweather_test.app.entity.CityModel;
import com.openweather_test.app.server_api.RequestHelper;
import com.openweather_test.app.server_api.RequestLoader;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentMap extends Fragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<JSONObject> {

	private static GoogleMap map;

	private static final int LOADER_CITY = 0;

	private Bundle cityBundle;

	private ImageView ivIcon;
	private ProgressBar prgbIcon;

	private ProgressBar prgbMain;

	private LinearLayout llWeatherInfo;

	private TextView tvSunrise;
	private TextView tvSunset;
	private TextView tvMainWeather;
	private TextView tvMainWeatherDescription;
	private TextView tvTemp;
	private TextView tvTempMin;
	private TextView tvTempMax;
	private TextView tvPressure;
	private TextView tvSeaLevel;
	private TextView tvGrndLevel;
	private TextView tvCity;
	private TextView tvHumidity;
	private TextView tvWindSpeed;
	private TextView tvWindDeg;

	private CityModel cityModel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		setRetainInstance(true);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_map, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		llWeatherInfo = (LinearLayout) view.findViewById(R.id.ll_info);
		prgbMain = (ProgressBar) view.findViewById(R.id.prgb_main);

		ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
		prgbIcon = (ProgressBar) view.findViewById(R.id.prgb_icon);

		tvSunrise = (TextView) view.findViewById(R.id.tv_sunrise);
		tvSunset = (TextView) view.findViewById(R.id.tv_sunset);
		tvMainWeather = (TextView) view.findViewById(R.id.tv_main_weather);
		tvMainWeatherDescription = (TextView) view.findViewById(R.id.tv_weather_description);
		tvTemp = (TextView) view.findViewById(R.id.tv_temp);
		tvTempMin = (TextView) view.findViewById(R.id.tv_temp_min);
		tvTempMax = (TextView) view.findViewById(R.id.tv_temp_max);
		tvPressure = (TextView) view.findViewById(R.id.tv_pressure);
		tvSeaLevel = (TextView) view.findViewById(R.id.tv_sea_level);
		tvGrndLevel = (TextView) view.findViewById(R.id.tv_grnd_level);
		tvCity = (TextView) view.findViewById(R.id.tv_city);
		tvHumidity = (TextView) view.findViewById(R.id.tv_humidity);
		tvWindSpeed = (TextView) view.findViewById(R.id.tv_speed);
		tvWindDeg = (TextView) view.findViewById(R.id.tv_deg);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState == null) {
			SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (map != null && cityModel != null) {
			fillMap(cityModel);
			fillWeather(cityModel);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_search, menu);

		SearchManager searchManager =
				(SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView =
				(SearchView) menu.findItem(R.id.action_search).getActionView();
		searchView.setSearchableInfo(
				searchManager.getSearchableInfo(getActivity().getComponentName()));
		searchView.setIconifiedByDefault(false);

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				return true;

			}

			@Override
			public boolean onQueryTextSubmit(String s) {
				if (s.length() > 0) {
					if (getLoaderManager().getLoader(LOADER_CITY) != null)
						getLoaderManager().destroyLoader(LOADER_CITY);
					if (cityBundle != null)
						cityBundle = null;
					cityBundle = RequestHelper.getWeatherByCity(s);
					getLoaderManager().initLoader(LOADER_CITY, cityBundle, FragmentMap.this).forceLoad();
				}
				return false;
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
	}

	@Override
	public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
		llWeatherInfo.setVisibility(View.GONE);
		prgbMain.setVisibility(View.VISIBLE);
		prgbMain.setIndeterminate(true);
		return new RequestLoader(getActivity(), args);
	}

	@Override
	public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {
		switch (loader.getId()) {
			case LOADER_CITY:
				if (data.has("coord")) {
					Gson gson = new Gson();
					cityModel = gson.fromJson(data.toString(), CityModel.class);
					if (cityModel != null) {
						if (map != null) {
							fillMap(cityModel);
						}
						fillWeather(cityModel);
					}
				} else {
					try {
						if (data.has("cod") && data.getString("cod").equals("404")) {
							Toast.makeText(getActivity(), data.getString("message"), Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				prgbMain.setVisibility(View.GONE);
				prgbMain.setIndeterminate(false);
				break;
		}
	}

	@Override
	public void onLoaderReset(Loader<JSONObject> loader) {

	}

	private void fillMap(CityModel cityModel) {
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(cityModel.getLatLong(), 13));

		map.addMarker(new MarkerOptions()
				.title(cityModel.getName())
				.position(cityModel.getLatLong()));
	}

	private void fillWeather(CityModel cityModel) {
		ImageLoader.getInstance().displayImage(cityModel.getWeather().get(0).getIcon(), ivIcon, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				prgbIcon.setVisibility(View.VISIBLE);
				prgbIcon.setIndeterminate(true);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				prgbIcon.setVisibility(View.GONE);
				prgbIcon.setIndeterminate(false);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				prgbIcon.setVisibility(View.GONE);
				prgbIcon.setIndeterminate(false);
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				prgbIcon.setVisibility(View.GONE);
				prgbIcon.setIndeterminate(false);
			}
		});

		llWeatherInfo.setVisibility(View.VISIBLE);

		tvSunrise.setText(getResources().getString(R.string.sunrise) + " " + cityModel.getSys().getSunriseFormatted() + " ");
		tvSunset.setText(getResources().getString(R.string.sunset) + " " + cityModel.getSys().getSunsetFormatted());
		tvMainWeather.setText(getResources().getString(R.string.main_weather) + " " + cityModel.getWeather().get(0).getMain());
		tvMainWeatherDescription.setText(getResources().getString(R.string.weather_description) + " " + cityModel.getWeather().get(0).getDescription());
		tvTemp.setText(getResources().getString(R.string.temp) + " " + cityModel.getMain().getTemp());
		tvTempMin.setText(getResources().getString(R.string.temp_min) + " " + cityModel.getMain().getTempMin());
		tvTempMax.setText(getResources().getString(R.string.temp_max) + " " + cityModel.getMain().getTempMax());
		tvPressure.setText(getResources().getString(R.string.pressure) + " " + cityModel.getMain().getPressure());
		tvSeaLevel.setText(getResources().getString(R.string.sea_level) + " " + cityModel.getMain().getSeaLevel());
		tvGrndLevel.setText(getResources().getString(R.string.grnd_level) + " " + cityModel.getMain().getGrndLevel());
		tvCity.setText(getResources().getString(R.string.city) + " " + cityModel.getName());
		tvHumidity.setText(getResources().getString(R.string.humidity) + " " + cityModel.getMain().getHumidity());
		tvWindSpeed.setText(getResources().getString(R.string.speed) + " " + cityModel.getWind().getSpeed() + " ");
		tvWindDeg.setText(getResources().getString(R.string.deg) + " " + cityModel.getWind().getDeg());
	}

}
