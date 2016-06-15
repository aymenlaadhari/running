package com.example.runningchallenge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import model.Markers;
import model.Runner;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.MapFragment;

import contentprovider.DatabaseHelper;

public class ResultActivity extends ActionBarActivity {
	TextView time, speed, calories;
	Markers markers;
    DatabaseHelper helper;
	GoogleMap map;
	List<LatLng> markerPoints;

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
		View mActionBarView = getLayoutInflater().inflate(R.layout.actionbar_custom_view_home, null);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		markers = (Markers) getIntent().getSerializableExtra("poinResult");
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapResult)).getMap();
		helper = new DatabaseHelper(getApplicationContext());
		if (markers.getLat().size() >= 2 ) {
		LatLng latLng1 = new LatLng(Double.parseDouble(markers.getLat().get(0)),
				Double.parseDouble(markers.getLen().get(0)));
		LatLng latLng2 = new LatLng(Double.parseDouble(markers.getLat().get(markers.getLat().size()-1)),
				Double.parseDouble(markers.getLen().get(markers.getLat().size()-1)));
		
	
			CameraPosition cameraPosition = CameraPosition.builder()
					.target(latLng2).zoom(18)
					.bearing(90).build();
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
				LatLng latLngstart = new LatLng(Double.parseDouble(markers.getLat().get(i)), Double.parseDouble(markers.getLen().get(i)));
				LatLng latLngfin = new LatLng(Double.parseDouble(markers.getLat().get(i+1)), Double.parseDouble(markers.getLen().get(i+1)));

				map.addPolyline(new PolylineOptions()
						.add(latLngstart, latLngfin)
						.width(8).color(Color.BLUE).geodesic(true));
			}
			map.addMarker(markerOptions2);
	}
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/berlin-sans-fb-demi-bold.ttf");
		time = (TextView) findViewById(R.id.textTime);
		speed = (TextView) findViewById(R.id.textSpeed);
		calories = (TextView) findViewById(R.id.textCalories);
		time.setTypeface(custom_font);
		speed.setTypeface(custom_font);
		calories.setTypeface(custom_font);
		calories.setText(String.valueOf(helper.findAllStat().get(helper.findAllStat().size()-1).getCalories()).substring(0, 3)+"k.cal");
		speed.setText(String.valueOf(helper.findAllStat().get(helper.findAllStat().size()-1).getSpeed()).substring(0, 3)+"KM/H");
		time.setText(helper.findAllStat().get(helper.findAllStat().size()-1).getTime());
		String timeRes = new String();
		timeRes = getIntent().getStringExtra("tempsInfo");

		// time.setText(markers.getTemps());
		// speed.setText(markers.getVitesse());
		// calories.setText(markers.getCalories());
		final ImageButton buttonSubmit = (ImageButton) findViewById(R.id.imageSubmit);

		/*
		 * Button Submit
		 */
		buttonSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplication(),
						StatisticsActivity.class);
				startActivity(intent);
//				Intent intentDB = new Intent(getApplication(),
//						AndroidDatabaseManager.class);
//				startActivity(intentDB);

			}
		});
		
		ImageButton buttonClass = (ImageButton)findViewById(R.id.actionBarIconClassment);
		buttonClass.getBackground().setAlpha(32); 
		buttonClass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplication(),
						ClassementActivity.class);
				startActivity(intent);
			}
		});

		
		ImageButton buttonStat = (ImageButton)findViewById(R.id.actionBarIconStat);
		buttonStat.getBackground().setAlpha(32); 
		buttonStat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplication(),
						StatisticsActivity.class);
				startActivity(intent);
			}
		});
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
		// TODO Auto-generated method stub
//		int id = item.getItemId();
//		if (id == R.id.classement) {
//			Intent intent = new Intent(getApplication(),
//					ClassementActivity.class);
//			startActivity(intent);
//		}

		return super.onOptionsItemSelected(item);
	}

}
