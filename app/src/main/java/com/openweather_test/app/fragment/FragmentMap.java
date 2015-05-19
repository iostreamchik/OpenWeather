package com.openweather_test.app.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.SearchView;
import android.view.*;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.openweather_test.app.R;
import com.openweather_test.app.server_api.RequestHelper;
import com.openweather_test.app.server_api.RequestLoader;
import org.json.JSONObject;

public class FragmentMap extends Fragment implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<JSONObject> {

	private static GoogleMap map;

	private static final int LOADER_CITY = 0;
	private static final int LOADER_WEATHER = 1;

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

	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState == null) {
			SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
					.findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);
		}

		Bundle bundle = new RequestHelper().getWeatherByCity("London");
		getLoaderManager().initLoader(LOADER_CITY, bundle, this);
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
					//todo search city

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

//		LatLng sydney = new LatLng(-33.867, 151.206);

//		map.setMyLocationEnabled(true);

//		map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
//
//		map.addMarker(new MarkerOptions()
//				.title("Sydney")
//				.snippet("The most populous city in Australia.")
//				.position(sydney));
	}

	@Override
	public Loader<JSONObject> onCreateLoader(int id, Bundle args) {
		return new RequestLoader(getActivity(), args);
	}

	@Override
	public void onLoadFinished(Loader<JSONObject> loader, JSONObject data) {

	}

	@Override
	public void onLoaderReset(Loader<JSONObject> loader) {

	}
}
