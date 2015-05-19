package com.openweather_test.app.activity;

import android.os.Bundle;
import com.openweather_test.app.R;
import com.openweather_test.app.fragment.FragmentMap;


public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initToolbar();

		if (savedInstanceState == null) {
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, new FragmentMap(),
							FragmentMap.class.getSimpleName())
					.addToBackStack(null)
					.commit();
		}
	}

}
