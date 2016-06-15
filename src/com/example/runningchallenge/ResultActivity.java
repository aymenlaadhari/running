package com.example.runningchallenge;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Markers;
import model.RunnerStatistics;
import ressources.Constatntes;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.runningchallenge.app.AppController;
import com.facebook.Session;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import contentprovider.DatabaseHelper;

public class ResultActivity extends ActionBarActivity {
	TextView time, speed, calories;
	Markers markers;
	DatabaseHelper helper;
	GoogleMap map;
	List<LatLng> markerPoints;
	RunnerStatistics runnerStatistics;
	Date sqlDate;
	// url post statistics
	private String urlPostStatistics = Constatntes.BASE_URL
			+ "RunnerStatistics?externalID=%1$s&externalProvider=%2$s";

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#99000000")));
		Drawable d = getResources().getDrawable(R.drawable.header);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(d);
		View mActionBarView = getLayoutInflater().inflate(
				R.layout.actionbar_custom_view_home, null);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		markers = (Markers) getIntent().getSerializableExtra("poinResult");
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapResult)).getMap();
		helper = new DatabaseHelper(getApplicationContext());
		runnerStatistics = helper.findAllStat().get(
				helper.findAllStat().size() - 1);

		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/berlin-sans-fb-demi-bold.ttf");
		time = (TextView) findViewById(R.id.textTime);
		speed = (TextView) findViewById(R.id.textSpeed);
		calories = (TextView) findViewById(R.id.textCalories);
		time.setTypeface(custom_font);
		speed.setTypeface(custom_font);
		calories.setTypeface(custom_font);
		calories.setText(String.valueOf(runnerStatistics.getCalories())
				.substring(0, 3) + "k.cal");
		DecimalFormat newSpeed = new DecimalFormat("#0.0");
		speed.setText(newSpeed.format(runnerStatistics.getSpeed()).replaceAll(
				",", ".")
				+ "KM/H");
		time.setText(runnerStatistics.getTime() + "min");
		final ImageButton buttonSubmit = (ImageButton) findViewById(R.id.imageSubmit);

		/*
		 * Button Submit
		 */
		buttonSubmit.setOnClickListener(new OnClickListener() {

			@SuppressLint("ShowToast")
			@Override
			public void onClick(View v) {
				if (runnerStatistics.getRunner().getFbID() != null) {
					makePostStatistics(runnerStatistics.getRunner().getFbID(),
							"Facebook");
				} else {
					makePostStatistics(runnerStatistics.getRunner().getTwID(),
							"Twitter");
				}

				Intent intent = new Intent(getApplication(),
						StatisticsActivity.class);
				startActivity(intent);

			}
		});

		ImageButton buttonClass = (ImageButton) findViewById(R.id.actionBarIconClassment);
		buttonClass.getBackground().setAlpha(5);
		buttonClass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplication(),
						ClassementActivity.class);
				startActivity(intent);
			}
		});

		

		if (markers.getLat().size() >= 2) {
			LatLng latLng1 = new LatLng(Double.parseDouble(markers.getLat()
					.get(0)), Double.parseDouble(markers.getLen().get(0)));
			LatLng latLng2 = new LatLng(Double.parseDouble(markers.getLat()
					.get(markers.getLat().size() - 1)),
					Double.parseDouble(markers.getLen().get(
							markers.getLat().size() - 1)));

			CameraPosition cameraPosition = CameraPosition.builder()
					.target(latLng2).zoom(18).bearing(90).build();
			map.animateCamera(
					CameraUpdateFactory.newCameraPosition(cameraPosition),
					2000, null);
			MarkerOptions markerOptions1 = new MarkerOptions();
			MarkerOptions markerOptions2 = new MarkerOptions();
			markerOptions1.position(latLng1);
			markerOptions2.position(latLng2);
			markerOptions1.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.pin1));
			markerOptions2.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.pin2));

			map.addMarker(markerOptions1);

			for (int i = 0; i < markers.getLat().size() - 1; i++) {

				LatLng latLngstart = new LatLng(Double.parseDouble(markers
						.getLat().get(i)), Double.parseDouble(markers.getLen()
						.get(i)));
				LatLng latLngfin = new LatLng(Double.parseDouble(markers
						.getLat().get(i + 1)), Double.parseDouble(markers
						.getLen().get(i + 1)));

				map.addPolyline(new PolylineOptions()
						.add(latLngstart, latLngfin).width(8)
						.color(Color.GREEN).geodesic(true));

			}
			map.addMarker(markerOptions2);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.result, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_signout) {

			if (Session.getActiveSession() != null) {
				Session.getActiveSession().closeAndClearTokenInformation();
				finish();
			}

			Session.setActiveSession(null);
			finish();

			return true;

		}
		if (id == R.id.action_statistics) {
			Intent intent = new Intent(getApplication(),
					StatisticsActivity.class);
			startActivity(intent);
		}
		if (id == R.id.action_preferences) {
			Intent intent = new Intent(getApplication(),
					PreferencesActivity.class);
			startActivity(intent);
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * makePostStatistics
	 */
	public void makePostStatistics(String ID, String Provider) {

		final String id = ID;
		final String provider = Provider;

		StringRequest req = new StringRequest(
				Request.Method.POST,
				"http://runningchallange5950.azurewebsites.net/api/RunnerStatistics?externalID="
						+ id + "&externaProvider=" + provider,
				createMyReqSuccessListener(), createMyReqErrorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				DecimalFormat newSpeed = new DecimalFormat("#0.0");
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/json; charset=utf-8");
				params.put("RunnerStatisticsId", "1");// default value
				params.put("RunnerId", "2");// default
											// value
				sqlDate = new Date(runnerStatistics.getRunningDate().getTime());
				String date = sqlDate.toString()
						+ "T"
						+ String.valueOf(runnerStatistics.getRunningDate()
								.getHours())
						+ ":"
						+ String.valueOf(runnerStatistics.getRunningDate()
								.getMinutes())
						+ ":"
						+ String.valueOf(runnerStatistics.getRunningDate()
								.getSeconds());

				params.put("RunningDate", date);
				if (runnerStatistics.getCalories() == 0) {
					params.put("Calories", "0.0");

				} else {
					params.put("Calories",
							String.valueOf(runnerStatistics.getCalories())
									.replaceAll(",", "."));

				}
				if (runnerStatistics.getSpeed() == 0) {
					params.put("Speed", "0.0");

				} else {
					params.put("Speed",
							newSpeed.format(runnerStatistics.getSpeed())
									.replaceAll(",", "."));

				}
				params.put("Speed",
						newSpeed.format(runnerStatistics.getSpeed())
								.replaceAll(",", "."));
				params.put("Time",
						runnerStatistics.getTime().replaceAll(":", "."));
				params.put("Distance",
						String.valueOf(runnerStatistics.getDistance()));
				params.put("Score", runnerStatistics.getNote());
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				// do not add anything here
				return headers;
			}
		};

		AppController.getInstance().addToRequestQueue(req);
	}

	private Response.Listener<String> createMyReqSuccessListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Toast.makeText(getApplicationContext(), "Data submitted !",
						Toast.LENGTH_LONG).show();
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(),
						"Failed submitting data", Toast.LENGTH_LONG).show();
			}
		};
	}
}
