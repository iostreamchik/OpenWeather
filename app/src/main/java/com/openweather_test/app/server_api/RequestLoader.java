package com.openweather_test.app.server_api;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.util.Log;
import android.widget.Toast;
import com.openweather_test.app.R;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class RequestLoader extends Loader<JSONObject> {

	private static final String TAG = "RequestLoader";

	public static final String EXTRA_REQUEST_TYPE = "request_type";
	public static final String EXTRA_REQUEST_STRING = "request_params";

	private RequestType requestType;
	private String request;

	private ServerTask serverTask;

	public RequestLoader(Context context, Bundle bundle) {
		super(context);
		if (bundle != null) {
			requestType = (RequestType) bundle.getSerializable(EXTRA_REQUEST_TYPE);
			request = bundle.getString(EXTRA_REQUEST_STRING);
		}
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		startLoad();
	}

	@Override
	protected void onForceLoad() {
		super.onForceLoad();
		startLoad();
	}

	private void startLoad() {
		if (!isNetworkAvailable()) {
			stopLoading();
			Toast.makeText(getContext(), getContext().getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
		}

		if (serverTask != null)
			serverTask.cancel(true);
		serverTask = new ServerTask();

		serverTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);

		Log.d(TAG, "onStartLoading: " + request);
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		boolean connected = networkInfo != null && networkInfo.isConnected();
		return connected;
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		Log.d(TAG, "onStopLoading: " + request);
		if (serverTask != null) {
			serverTask.cancel(true);
		}
	}

	@Override
	protected void onAbandon() {
		super.onAbandon();
		Log.d(TAG, "onAbandon: " + request);
	}

	@Override
	protected void onReset() {
		super.onReset();
		getResultFromTask(null);
		Log.d(TAG, "onReset: " + request);
	}

	void getResultFromTask(JSONObject result) {
		if (result != null)
			Log.d(TAG, "getResultFromTask: " + "deliverResult(result) -> " + result);
		deliverResult(result);
	}

	private class ServerTask extends AsyncTask<String, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Log.d(TAG, "onPreExecute: " + request);
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			Log.d(TAG, "doInBackground: " + request);
			String result = null;
			try {
				HttpResponse response = null;
				HttpClient client = new DefaultHttpClient();

				switch (requestType) {
					case GET:
						HttpGet get = new HttpGet();
						get.setURI(new URI(request));
						response = client.execute(get);
						break;
					case POST:

						break;
				}

				if (response != null) {
					result = StringEscapeUtils.unescapeJava(EntityUtils.toString(response.getEntity()));
				}
				JSONObject object = null;
				if (result != null) {
					Log.d(TAG, "result = StringEscapeUtils: " + result);
					object = new JSONObject(result);
				}

				if(object == null)
					object = new JSONObject();
				return object;

			} catch (URISyntaxException e) {
				e.printStackTrace();
				return new JSONObject();
			} catch (JSONException e) {
				e.printStackTrace();
				return new JSONObject();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return new JSONObject();
			} catch (IOException e) {
				e.printStackTrace();
				return new JSONObject();
			}
		}

		@Override
		protected void onPostExecute(JSONObject jsonObject) {
			super.onPostExecute(jsonObject);
			if (jsonObject != null)
				Log.d(TAG, "onPostExecute response: " + jsonObject.toString());
			getResultFromTask(jsonObject);
		}
	}
}
