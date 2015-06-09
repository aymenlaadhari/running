package com.example.runningchallenge;

import interfaces.Constants;
import interfaces.GPSCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import manager.GPSManager;
import model.Markers;
import model.Runner;
import model.RunnerStatistics;
import services.FusedLocationService;
import settings.AppSettings;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.Session;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;

import contentprovider.DatabaseHelper;

@SuppressLint({ "InlinedApi", "InflateParams" })
public class MapActivity extends ActionBarActivity implements
		android.location.LocationListener, GPSCallback, SensorEventListener {
	private GPSManager gpsManager = null;
	private double speed = 0.0;
	private int measurement_index = Constants.INDEX_KM;
	private GoogleMap googleMap;
	double mySpeed;
	private float parcours = 0;
	int i = 0;
	boolean fraude = false;
	double sommeCalories = 0.0;
	CameraPosition cameraPosition;
	FusedLocationService fusedLocationService;
	private static LatLng prev;
	int flag = 0;
	public static final String Poid = "poidKey";
	private LocationManager locationManager;
	private String provider;
	Marker mPositionMarker;
	private ArrayList<LatLng> markerPoints;
	int count = 0;
	Button btSteps, btSpeed, btCalories;
	Chronometer chronometer;
	float constante = (float) 1.02784823;
	SharedPreferences prefs;
	String speedString;
	String unitString;
	float distance;
	MenuItem items;
	float poids;
	String calories;
	List<String> speedList;
	List<Float> distanceList;
	List<String> tempsList;
	List<String> caloriesList;
	List<String> lats;
	List<String> lons;
	List<Float> accelerations;
	List<MarkerOptions> listMarkerOptions = new ArrayList<MarkerOptions>();
	TextView textSpeed;
	TextView textTemps;
	TextView textCalories;
	DatabaseHelper helper;
	Runner runner;
	private float lastX, lastY, lastZ;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private float deltaXMax = 0;
	private float deltaYMax = 0;
	private float deltaZMax = 0;
	private float deltaX = 0;
	private float deltaY = 0;
	private float deltaZ = 0;
	private float vibrateThreshold = 0;
	public Vibrator v;
	ImageButton buttonClass;

	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		helper = new DatabaseHelper(getApplicationContext());
		prefs = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
		// poids = Float.parseFloat(prefs.getString(Poid, ""));
		poids = helper.findAllRunner().get(0).getWeight();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#99000000")));

		Drawable d = getResources().getDrawable(R.drawable.header);
		actionBar.setBackgroundDrawable(d);
		View mActionBarView = getLayoutInflater().inflate(
				R.layout.actionbar_custom_view_home, null);
		actionBar.setCustomView(mActionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		speedList = new ArrayList<String>();
		distanceList = new ArrayList<Float>();
		tempsList = new ArrayList<String>();
		caloriesList = new ArrayList<String>();
		lats = new ArrayList<String>();
		lons = new ArrayList<String>();
		accelerations = new ArrayList<Float>();
		listMarkerOptions = new ArrayList<MarkerOptions>();

		/*
		 * Initialisation de tout les composants
		 */
		setContentView(R.layout.activity_map);
		googleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		googleMap.setMyLocationEnabled(true);
		googleMap.setBuildingsEnabled(true);
		googleMap.setIndoorEnabled(true);

		LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
			if (locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				recreate();
			}

		} else if (locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Criteria crit = new Criteria();

			Location loc = locMan.getLastKnownLocation(locMan.getBestProvider(
					crit, false));

			if (loc != null) {
				LatLng latLng = new LatLng(loc.getLatitude(),
						loc.getLongitude());

				CameraPosition camPos = new CameraPosition.Builder()

				.target(new LatLng(loc.getLatitude(), loc.getLongitude()))

				.zoom(12.8f)

				.build();

				CameraUpdate camUpdate = CameraUpdateFactory
						.newCameraPosition(camPos);

				googleMap.moveCamera(camUpdate);
				googleMap.addMarker(new MarkerOptions().position(latLng).title(
						"Hello"));

			}

		}

		gpsManager = new GPSManager();
		gpsManager.startListening(getApplicationContext());
		gpsManager.setGPSCallback(this);
		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/berlin-sans-fb-demi-bold.ttf");
		TextView textShowSpeed = (TextView) findViewById(R.id.textViewVitesse);
		textShowSpeed.setTypeface(custom_font);
		textShowSpeed.setText(getString(R.string.info));
		// textShowSpeed.setVisibility(View.INVISIBLE);
		measurement_index = AppSettings.getMeasureUnit(this);
		chronometer = (Chronometer) findViewById(R.id.chronometer);
		runner = (Runner) helper.findAllRunner().get(0);
		final Button mNotification_on_btn = (Button) findViewById(R.id.on_btn);
		final Button mNotification_off_btn = (Button) findViewById(R.id.off_btn);
		buttonClass = (ImageButton) findViewById(R.id.actionBarIconClassment);
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

		/*
		 * The start button
		 */
		mNotification_on_btn.setVisibility(View.GONE);
		mNotification_off_btn.setVisibility(View.VISIBLE);
		chronometer.setVisibility(View.INVISIBLE);
		mNotification_on_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mNotification_on_btn.setVisibility(View.INVISIBLE);
				mNotification_off_btn.setVisibility(View.VISIBLE);
				chronometer.stop();
				Date date = new Date();
				if (speedList.isEmpty()) {
					addStatistics(sommeCalories, distance, runner, date, Double
							.parseDouble("0.0"), chronometer.getText()
							.toString(), "0");
				} else {

					addStatistics(sommeCalories, distance, runner, date,
							speedMoy(speedList), chronometer.getText()
									.toString(), String.valueOf(note(
									showElapsedTime(), speedMoy(speedList))));
				}

				chronometer.setBase(SystemClock.elapsedRealtime());

				Intent inpoints = new Intent(MapActivity.this,
						ResultActivity.class);
				Markers markers = new Markers();
				markers.setLat(lats);
				markers.setLen(lons);
				inpoints.putExtra("poinResult", markers);
				getDatBase();
				startActivity(inpoints);
				finish();
			}
		});
		mNotification_off_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				init();
				mNotification_off_btn.setVisibility(View.INVISIBLE);
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.start();
				mNotification_on_btn.setVisibility(View.VISIBLE);
			}
		});
	}

	private void init() {
		googleMap.clear();
		this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			buildAlertMessageNoGps();
		}
		markerPoints = new ArrayList<LatLng>();
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);

		googleMap.getUiSettings().setTiltGesturesEnabled(true);
		googleMap.setTrafficEnabled(true);
		locationManager.requestLocationUpdates(provider, 400, 1, this);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
			// success! we have an accelerometer

			accelerometer = sensorManager
					.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			sensorManager.registerListener(this, accelerometer,
					SensorManager.SENSOR_DELAY_NORMAL);
			vibrateThreshold = accelerometer.getMaximumRange() / 2;
		} else {
			// fai! we dont have an accelerometer!
		}

		// initialize vibration
		v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.map, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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

	/*
	 * (Detect the location's changes)
	 * 
	 * @see
	 * android.location.LocationListener#onLocationChanged(android.location.
	 * Location)
	 */
	@Override
	public void onLocationChanged(Location location) {

		/*
		 * Handle the location change
		 */
		location = locationManager.getLastKnownLocation(provider);
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		Log.e("I shouldn't be here" + String.valueOf(lat) + ","
				+ String.valueOf(lng), provider);

		prev = new LatLng(lat, lng);
		// Adding new item to the ArrayList
		markerPoints.add(prev);

		// Creating MarkerOptions
		MarkerOptions options = new MarkerOptions();

		/*
		 * Setting the position of the marker
		 */
		options.position(prev);
		if (markerPoints.size() == 1) {
			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin1));
			googleMap.addMarker(options);
			CameraPosition cameraPosition = CameraPosition.builder()
					.target(prev).zoom(20).bearing(90).build();
			googleMap.animateCamera(
					CameraUpdateFactory.newCameraPosition(cameraPosition),
					2000, null);
		} else if (markerPoints.size() >= 2) {

			options.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin2));

		}

		/*
		 * Add new marker to the Google Map
		 */

		if (markerPoints.size() >= 2) {
			if (location.hasSpeed()) {

				mySpeed = location.getSpeed();

			}
			googleMap.addPolyline(new PolylineOptions()
					.add(markerPoints.get(count),
							markerPoints.get(markerPoints.size() - 1)).width(8)
					.color(Color.BLUE).geodesic(true));
			Location locationA = new Location("point A");
			locationA
					.setLatitude(markerPoints.get(markerPoints.size() - 2).latitude);
			locationA
					.setLongitude(markerPoints.get(markerPoints.size() - 2).longitude);
			Location locationB = new Location("point B");
			locationB.setLatitude(prev.latitude);
			locationB.setLongitude(prev.longitude);
			distance = locationA.distanceTo(locationB);
			parcours = parcours + distance;
			speedList.add(speedString);
			CameraPosition cameraPosition = CameraPosition.builder()
					.target(prev).zoom(20).bearing(90).build();
			googleMap.animateCamera(
					CameraUpdateFactory.newCameraPosition(cameraPosition),
					2000, null);

			/*
			 * add marker after each 10m
			 */
			if (Math.round(parcours) % 10 == 0) {
				// speedList.add(speedString + " " + unitString);

				distanceList.add(parcours);
				tempsList.add(chronometer.getText().toString());
				calories = String.valueOf(getCalories(poids, constante,
						distance));
				double caloriesD = getCalories(poids, constante, distance);
				sommeCalories = sommeCalories + caloriesD;

				caloriesList.add(calories);
				LatLng local = new LatLng(location.getLatitude(),
						location.getLongitude());
				MarkerOptions marker = new MarkerOptions();
				marker.position(local);
				listMarkerOptions.add(marker);
				marker.title(tempsList.get(i));
				googleMap.addMarker(marker);
				// googleMap.addMarker(options);
				// googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
				i++;

			}

			count++;
		}
		lats.add(String.valueOf(markerPoints.get(count).latitude));
		lons.add(String.valueOf(markerPoints.get(count).longitude));

	}

	/*
	 * Get accumulated time
	 */
	private long showElapsedTime() {
		long elapsedMillis = SystemClock.elapsedRealtime()
				- chronometer.getBase();
		return elapsedMillis;
	}

	@Override
	protected void onResume() {
		super.onResume();

		/*
		 * mettre à jour le gps
		 */
		// locationManager.requestLocationUpdates(provider, 400, 1, this);

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (sensorManager != null) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (sensorManager != null) {
			sensorManager.unregisterListener(this);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (sensorManager != null) {
			sensorManager.unregisterListener(this);
		}

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public void animateMarker(final Marker marker, final Location location) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final LatLng startLatLng = marker.getPosition();
		final double startRotation = marker.getRotation();
		final long duration = 500;

		final Interpolator interpolator = new LinearInterpolator();

		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
						/ duration);

				double lng = t * location.getLongitude() + (1 - t)
						* startLatLng.longitude;
				double lat = t * location.getLatitude() + (1 - t)
						* startLatLng.latitude;

				float rotation = (float) (t * location.getBearing() + (1 - t)
						* startRotation);

				marker.setPosition(new LatLng(lat, lng));
				marker.setRotation(rotation);

				if (t < 1.0) {
					// Post again 16ms later.
					handler.postDelayed(this, 16);
				}
			}
		});

	}

	/*
	 * Get Calories burned
	 */
	private double getCalories(float poid, float constante, float distance) {
		return poid * constante * distance / 1000.00;
	}

	/*
	 * Alert user to enable GPS if disabled
	 */
	private void buildAlertMessageNoGps() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							@SuppressWarnings("unused") final int id) {
						dialog.cancel();
					}
				});
		final AlertDialog alert = builder.create();
		alert.show();
	}

	/*
	 * (Get GPS Update)
	 * 
	 * @see interfaces.GPSCallback#onGPSUpdate(android.location.Location)
	 */
	@Override
	public void onGPSUpdate(Location location) {
		// TODO Auto-generated method stub
		location.getLatitude();
		location.getLongitude();
		speed = location.getSpeed();
		speedString = String.valueOf(roundDecimal(convertSpeed(speed), 2));
		unitString = measurementUnitString(measurement_index);
		((TextView) findViewById(R.id.textViewVitesse)).setText(speedString
				+ " " + unitString);

	}

	/*
	 * Speed converter
	 */
	private double convertSpeed(double speed) {
		return ((speed * Constants.HOUR_MULTIPLIER) * Constants.UNIT_MULTIPLIERS[measurement_index]);
	}

	private String measurementUnitString(int unitIndex) {
		String string = "";

		switch (unitIndex) {
		case Constants.INDEX_KM:
			string = "km/h";
			break;
		case Constants.INDEX_MILES:
			string = "mi/h";
			break;
		}

		return string;
	}

	private double roundDecimal(double value, final int decimalPlace) {
		BigDecimal bd = new BigDecimal(value);

		bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
		value = bd.doubleValue();

		return value;
	}

	/*
	 * InfoWindows in the marker
	 */
	class MyInfoWindowAdapter implements InfoWindowAdapter {

		private final View myContentsView;

		MyInfoWindowAdapter() {
			myContentsView = getLayoutInflater().inflate(
					R.layout.custom_info_contents, null);
		}

		@Override
		public View getInfoContents(Marker marker) {
			// TODO Auto-generated method stub

			textSpeed = (TextView) myContentsView
					.findViewById(R.id.textViewSpeed);
			textTemps = (TextView) myContentsView
					.findViewById(R.id.textViewTemps);
			textCalories = (TextView) myContentsView
					.findViewById(R.id.textViewCalories);
			textSpeed.setText(speedList.get(speedList.size() - 1));
			textTemps.setText(tempsList.get(tempsList.size() - 1));

			// textCalories.setText(String.valueOf(person.getCalories()));

			return myContentsView;
		}

		@Override
		public View getInfoWindow(Marker marker) {

			return null;
		}
	}

	public ArrayList<LatLng> getMarkerPoints() {
		return markerPoints;
	}

	public void setMarkerPoints(ArrayList<LatLng> markerPoints) {
		this.markerPoints = markerPoints;
	}

	/*
	 * get speed average
	 */
	private double speedMoy(List<String> speedList) {

		double speedMoyd = 0;
		for (int i = 0; i < speedList.size() - 1; i++) {
			speedMoyd = speedMoyd + Double.parseDouble(speedList.get(i));
		}
		return speedMoyd / speedList.size();
	}

	/*
	 * get all calories burned
	 */
	private double allCalories(List<String> caloriesList) {
		double caloriesMoy = 0;
		for (int i = 0; i < caloriesList.size() - 1; i++) {
			caloriesMoy = caloriesMoy + Double.parseDouble(caloriesList.get(i));
		}
		return caloriesMoy;

	}

	/*
	 * add running statistics in data base
	 */
	public void addStatistics(double calories, double distance, Runner runner,
			Date runningDate, double speed, String time, String note) {
		RunnerStatistics runnerStatistics = new RunnerStatistics();
		runnerStatistics.setCalories(calories);
		runnerStatistics.setDistance(distance);
		runnerStatistics.setRunner(runner);
		runnerStatistics.setRunningDate(runningDate);
		runnerStatistics.setSpeed(speed);
		runnerStatistics.setTime(time);
		runnerStatistics.setNote(note);
		helper.insertStataistics(runnerStatistics);

	}

	/*
	 * Check if the user don't cheat
	 */
	private boolean fraude() {
		boolean fraude = false;
		if ( deltaX >= 30.0 || deltaY>= 25.0) {
			fraude = true;
		}

		return fraude;
	}

	/*
	 * Note assigned
	 */
	private int note(long time, double vitesse) {
		float note = 0;
		float percent = (float) (0.8 + 0.1894393 * Math.exp(-0.012778 * time) + 0.2989558 * Math
				.exp(-0.1932605 * time));
		float vo = (float) (4.60 + 0.182258 * vitesse + 0.000104 * Math.pow(
				vitesse, 2));

		note = percent / vo;
		return Math.round(note);

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// clean current values
		// displayCleanValues();
		// display the current x,y,z accelerometer values
		displayCurrentValues();
		// display the max x,y,z accelerometer values
		// displayMaxValues();

		// get the change of the x,y,z values of the accelerometer
		deltaX = Math.abs(lastX - event.values[0]);
		deltaY = Math.abs(lastY - event.values[1]);
		deltaZ = Math.abs(lastZ - event.values[2]);

		// if the change is below 2, it is just plain noise
		if (deltaX < 2)
			deltaX = 0;
		if (deltaY < 2)
			deltaY = 0;
		if (deltaZ < 2)
			deltaZ = 0;

		// set the last know values of x,y,z
		lastX = event.values[0];
		lastY = event.values[1];
		lastZ = event.values[2];
		accelerations.add(deltaX);

		if (fraude()) {
			v.vibrate(1000);
			Intent intent = new Intent(getApplication(), FraudActivity.class);
			startActivity(intent);
			finish();
			fraude = true;

		}

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	// display the current x,y,z accelerometer values
	public void displayCurrentValues() {
		// Toast.makeText(getApplicationContext(), Float.toString(deltaX),
		// Toast.LENGTH_SHORT).show();
		// Toast.makeText(getApplicationContext(), Float.toString(deltaY),
		// Toast.LENGTH_SHORT).show();
		// Toast.makeText(getApplicationContext(), Float.toString(deltaZ),
		// Toast.LENGTH_SHORT).show();
	}

	@SuppressLint("SdCardPath")
	public void showDialog() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(MapActivity.this);
		builder1.setMessage("You are not running :)");
		builder1.setCancelable(true);
		builder1.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						recreate();
					}
				});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	@SuppressLint("SdCardPath")
	public void getDatBase() {
		String sourceLocation = "/data/data/com.sample/databases/running.db";// Your
																				// database
																				// path
		String destLocation = "running.db";
		try {
			File sd = Environment.getExternalStorageDirectory();
			if (sd.canWrite()) {
				File source = new File(sourceLocation);
				File dest = new File(sd + "/" + destLocation);
				if (!dest.exists()) {
					dest.createNewFile();
				}
				if (source.exists()) {
					InputStream src = new FileInputStream(source);
					OutputStream dst = new FileOutputStream(dest);
					// Copy the bits from instream to outstream
					byte[] buf = new byte[1024];
					int len;
					while ((len = src.read(buf)) > 0) {
						dst.write(buf, 0, len);
					}
					src.close();
					dst.close();
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}
}
