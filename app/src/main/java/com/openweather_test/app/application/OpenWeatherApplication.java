package com.openweather_test.app.application;

import android.app.Application;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class OpenWeatherApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		ImageLoader imageLoader;
		DisplayImageOptions options;
		options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.delayBeforeLoading(400)
				.build();
		ImageLoaderConfiguration mImageLoaderConfig = new ImageLoaderConfiguration.Builder(this)
				.defaultDisplayImageOptions(options)
				.denyCacheImageMultipleSizesInMemory()
				.threadPoolSize(5)
				.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(mImageLoaderConfig);
	}
}
