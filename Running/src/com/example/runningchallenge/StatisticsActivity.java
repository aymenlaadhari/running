package com.example.runningchallenge;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import model.RunnerStatistics;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.runningchallenge.app.AppController;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import contentprovider.DatabaseHelper;

public class StatisticsActivity extends ActionBarActivity {
	WebView webViewCalories, webViewDistance, webViewSpeed;
	String dateDimension = "Months";

	String toastMsg;
	public static Resources ressources;
	double caloriesJanuary, caloriesFebruary, caloriesMars, caloriesApril,
			caloriesMai, caloriesJune, caloriesJuly, caloriesAugust,
			caloriesSeptember, caloriesOctober, caloriesNovember,
			caloriesDecember, distanceMonday, distanceTuesday,
			distanceWednesday, distanceThursday, distanceFriday,
			distanceSaturday, distanceSunday, distanceJanuary,
			distanceFebruary, distanceMars, distanceApril, distanceMai,
			distanceJune, distanceJuly, distanceAugust, distanceSeptember,
			distanceOctober, distanceNovember, distanceDecember, speedMonday,
			speedTuesday, speedWednesday, speedThursday, speedFriday,
			speedSaturday, speedSunday, speedJanuary, speedFebruary, speedMars,
			speedApril, speedMai, speedJune, speedJuly, speedAugust,
			speedSeptember, speedOctober, speedNovember, speedDecember;
	double caloriesTest = 1.2;
	List<RunnerStatistics> runnerStatistics;
	String tableauDays[] = { "Monday", "Tuesday", "Wednesday", "Thursday",
			"Friday", "Saturday", "Sunday" };
	String tableauMonths[] = { "January", "Fabruary", "Mars", "April", "May",
			"June", "July", "August", "September", "October", "November",
			"December" };
	DatabaseHelper helper;
	List<String> days, months, years;
	List<Double> caloriesDays, speedsDays, distanceDays, caloriesMonths,
			speedsMonths, distanceMonths;
	private ProgressDialog pDialog;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics_app);
		ressources = getResources();
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#99000000")));
		Drawable d = getResources().getDrawable(R.drawable.header);
		actionBar.setCustomView(R.layout.actionbar_custom_view_home);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(d);
		ImageButton buttonClass = (ImageButton) findViewById(R.id.actionBarIconClassment);
		buttonClass.setImageDrawable(getResources().getDrawable(
				R.drawable.retour7));
		buttonClass.getBackground().setAlpha(20);
		helper = new DatabaseHelper(getApplicationContext());
		caloriesDays = new ArrayList<Double>();
		distanceDays = new ArrayList<Double>();
		speedsDays = new ArrayList<Double>();
		caloriesMonths = new ArrayList<Double>();
		distanceMonths = new ArrayList<Double>();
		speedsMonths = new ArrayList<Double>();
		runnerStatistics = helper.findAllStat();
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Getting data...");
		pDialog.setCancelable(false);

		buttonClass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NavUtils.navigateUpFromSameTask(StatisticsActivity.this);
			}
		});

		setCaloriesDays(runnerStatistics);
		setDistanceDays(runnerStatistics);
		setSpeedDays(runnerStatistics);

		/**
		 * Detect screen
		 */

		int screenSize = getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK;

		switch (screenSize) {
		case Configuration.SCREENLAYOUT_SIZE_LARGE:
			toastMsg = "Large screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_NORMAL:
			toastMsg = "Normal screen";
			break;
		case Configuration.SCREENLAYOUT_SIZE_SMALL:
			toastMsg = "Small screen";
			break;
		default:
			toastMsg = "Screen size is neither large, normal or small";
		}
		// Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();

		/**
		 * WebView Calories
		 */
		webViewCalories = (WebView) findViewById(R.id.webCalories);
		webViewCalories.setBackgroundColor(Color.TRANSPARENT);
		webViewCalories.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		WebSettings webSettingsCalories = webViewCalories.getSettings();
		webSettingsCalories.setJavaScriptEnabled(true);
		webViewCalories.requestFocusFromTouch();
		webViewCalories.getSettings().setBuiltInZoomControls(false);
		webViewCalories.getSettings().setSupportZoom(false);
		webViewCalories.setInitialScale(68);
		webViewCalories.getSettings().setUseWideViewPort(true);
		webViewCalories.requestFocusFromTouch();
		loadContentChartCalories("days");
		/**
		 * WebView Distance
		 */
		webViewDistance = (WebView) findViewById(R.id.webDistance);
		webViewDistance.setBackgroundColor(Color.TRANSPARENT);
		webViewDistance.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		WebSettings webSettingsDistance = webViewDistance.getSettings();
		webSettingsDistance.setJavaScriptEnabled(true);
		webViewDistance.requestFocusFromTouch();
		webViewDistance.getSettings().setBuiltInZoomControls(false);
		webViewDistance.getSettings().setSupportZoom(false);
		webViewDistance.setInitialScale(68);
		webViewDistance.getSettings().setUseWideViewPort(true);
		webViewDistance.requestFocusFromTouch();
		loadContentChartDistance("days");
		/**
		 * WebView Speed
		 */
		webViewSpeed = (WebView) findViewById(R.id.webSpeed);
		webViewSpeed.setBackgroundColor(Color.TRANSPARENT);
		webViewSpeed.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
		WebSettings webSettingsVitesse = webViewSpeed.getSettings();
		webSettingsVitesse.setJavaScriptEnabled(true);
		webViewSpeed.requestFocusFromTouch();
		webViewSpeed.getSettings().setBuiltInZoomControls(false);
		webViewSpeed.getSettings().setSupportZoom(false);
		webViewSpeed.setInitialScale(68);
		webViewSpeed.getSettings().setUseWideViewPort(true);
		webViewSpeed.requestFocusFromTouch();
		loadContentChartSpeed("days");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_app, menu);
		return true;
	}

	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_year) {

		} else if (id == R.id.action_month) {
			setCaloriesMonth(runnerStatistics);
			setDistanceMonth(runnerStatistics);
			setSpeedMonth(runnerStatistics);
			loadContentChartCalories("months");
			loadContentChartDistance("months");
			loadContentChartSpeed("months");

		} else if (id == R.id.action_day) {
			setCaloriesDays(runnerStatistics);
			setDistanceDays(runnerStatistics);
			setSpeedDays(runnerStatistics);
			loadContentChartCalories("days");
			loadContentChartDistance("days");
			loadContentChartSpeed("days");

		} else if (id == R.id.action_db) {
			Intent intentDB = new Intent(getApplication(),
					AndroidDatabaseManager.class);
			startActivity(intentDB);

		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * Set calories
	 */
	public void setCaloriesDays(List<RunnerStatistics> runnerStatistics) {
		Double sommeCalmond = 0.0;
		Double sommeCaltue = 0.0;
		Double sommeCalwed = 0.0;
		Double sommeCalmthu = 0.0;
		Double sommeCalfri = 0.0;
		Double sommeCalsat = 0.0;
		Double sommeCalsun = 0.0;

		for (int i = 0; i < runnerStatistics.size(); i++) {
			if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Mon")) {
				sommeCalmond = sommeCalmond
						+ runnerStatistics.get(i).getCalories();
			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Tue")) {
				sommeCaltue = sommeCaltue
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Wed")) {
				sommeCalwed = sommeCalwed
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Thu")) {
				sommeCalmthu = sommeCalmthu
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Fri")) {
				sommeCalfri = sommeCalfri
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Sat")) {
				sommeCalsat = sommeCalsat
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Sun")) {
				sommeCalsun = sommeCalsun
						+ runnerStatistics.get(i).getCalories();
			}
		}
		caloriesDays.add(0, sommeCalmond);
		caloriesDays.add(1, sommeCaltue);
		caloriesDays.add(2, sommeCalwed);
		caloriesDays.add(3, sommeCalmthu);
		caloriesDays.add(4, sommeCalfri);
		caloriesDays.add(5, sommeCalsat);
		caloriesDays.add(6, sommeCalsun);

	}

	public void setCaloriesMonth(List<RunnerStatistics> runnerStatistics) {
		Double sommeCaljan = 0.0;
		Double sommeCalfeb = 0.0;
		Double sommeCalmar = 0.0;
		Double sommeCalapr = 0.0;
		Double sommeCalmai = 0.0;
		Double sommeCaljun = 0.0;
		Double sommeCaljul = 0.0;
		Double sommeCalaug = 0.0;
		Double sommeCalsep = 0.0;
		Double sommeCaloct = 0.0;
		Double sommeCalnov = 0.0;
		Double sommeCaldec = 0.0;

		for (int i = 0; i < runnerStatistics.size(); i++) {
			if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jan")) {
				sommeCaljan = sommeCaljan
						+ runnerStatistics.get(i).getCalories();
			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Feb")) {
				sommeCalfeb = sommeCalfeb
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Mar")) {
				sommeCalmar = sommeCalmar
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Apr")) {
				sommeCalapr = sommeCalapr
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("May")) {
				sommeCalmai = sommeCalmai
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jun")) {
				sommeCaljun = sommeCaljun
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jul")) {
				sommeCaljul = sommeCaljul
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Aug")) {
				sommeCalaug = sommeCalaug
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Oct")) {
				sommeCaloct = sommeCaloct
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Nov")) {
				sommeCalnov = sommeCalnov
						+ runnerStatistics.get(i).getCalories();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Dec")) {
				sommeCaldec = sommeCaldec
						+ runnerStatistics.get(i).getCalories();
			}

		}
		caloriesMonths.add(0, sommeCaljan);
		caloriesMonths.add(1, sommeCalfeb);
		caloriesMonths.add(2, sommeCalmar);
		caloriesMonths.add(3, sommeCalapr);
		caloriesMonths.add(4, sommeCalmai);
		caloriesMonths.add(5, sommeCaljun);
		caloriesMonths.add(6, sommeCaljul);
		caloriesMonths.add(7, sommeCalaug);
		caloriesMonths.add(8, sommeCalsep);
		caloriesMonths.add(9, sommeCaloct);
		caloriesMonths.add(10, sommeCalnov);
		caloriesMonths.add(11, sommeCaldec);
	}

	/**
	 * 
	 * Set distance
	 */
	public void setDistanceDays(List<RunnerStatistics> runnerStatistics) {
		Double sommeCalmond = 0.0;
		Double sommeCaltue = 0.0;
		Double sommeCalwed = 0.0;
		Double sommeCalmthu = 0.0;
		Double sommeCalfri = 0.0;
		Double sommeCalsat = 0.0;
		Double sommeCalsun = 0.0;

		for (int i = 0; i < runnerStatistics.size(); i++) {
			if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Mon")) {
				sommeCalmond = sommeCalmond
						+ runnerStatistics.get(i).getDistance();
			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Tue")) {
				sommeCaltue = sommeCaltue
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Wed")) {
				sommeCalwed = sommeCalwed
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Thu")) {
				sommeCalmthu = sommeCalmthu
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Fri")) {
				distanceFriday = distanceFriday
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Sat")) {
				sommeCalsat = sommeCalsat
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Sun")) {
				sommeCalsun = sommeCalsun
						+ runnerStatistics.get(i).getDistance();

			}

		}
		distanceDays.add(0, sommeCalmond);
		distanceDays.add(1, sommeCaltue);
		distanceDays.add(2, sommeCalwed);
		distanceDays.add(3, sommeCalmthu);
		distanceDays.add(4, sommeCalfri);
		distanceDays.add(5, sommeCalsat);
		distanceDays.add(6, sommeCalsun);
	}

	public void setDistanceMonth(List<RunnerStatistics> runnerStatistics) {
		Double sommeCaljan = 0.0;
		Double sommeCalfeb = 0.0;
		Double sommeCalmar = 0.0;
		Double sommeCalapr = 0.0;
		Double sommeCalmai = 0.0;
		Double sommeCaljun = 0.0;
		Double sommeCaljul = 0.0;
		Double sommeCalaug = 0.0;
		Double sommeCalsep = 0.0;
		Double sommeCaloct = 0.0;
		Double sommeCalnov = 0.0;
		Double sommeCaldec = 0.0;

		for (int i = 0; i < runnerStatistics.size(); i++) {
			if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jan")) {
				sommeCaljan = sommeCaljan
						+ runnerStatistics.get(i).getDistance();
			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Feb")) {
				sommeCalfeb = sommeCalfeb
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Mar")) {
				sommeCalmar = sommeCalmar
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Apr")) {
				sommeCalapr = sommeCalapr
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("May")) {
				sommeCalmai = sommeCalmai
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jun")) {
				sommeCaljun = sommeCaljun
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jul")) {
				sommeCaljul = sommeCaljul
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Aug")) {
				sommeCalaug = sommeCalaug
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Oct")) {
				sommeCaloct = sommeCaloct
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Nov")) {
				sommeCalnov = sommeCalnov
						+ runnerStatistics.get(i).getDistance();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Dec")) {
				sommeCaldec = sommeCaldec
						+ runnerStatistics.get(i).getDistance();

			}

		}
		distanceMonths.add(0, sommeCaljan);
		distanceMonths.add(1, sommeCalfeb);
		distanceMonths.add(2, sommeCalmar);
		distanceMonths.add(3, sommeCalapr);
		distanceMonths.add(4, sommeCalmai);
		distanceMonths.add(5, sommeCaljun);
		distanceMonths.add(6, sommeCaljul);
		distanceMonths.add(7, sommeCalaug);
		distanceMonths.add(8, sommeCalsep);
		distanceMonths.add(9, sommeCaloct);
		distanceMonths.add(10, sommeCalnov);
		distanceMonths.add(11, sommeCaldec);
	}

	/**
	 * Set speed
	 */

	public void setSpeedDays(List<RunnerStatistics> runnerStatistics) {
		Double sommeCalmond = 0.0;
		Double sommeCaltue = 0.0;
		Double sommeCalwed = 0.0;
		Double sommeCalmthu = 0.0;
		Double sommeCalfri = 0.0;
		Double sommeCalsat = 0.0;
		Double sommeCalsun = 0.0;

		for (int i = 0; i < runnerStatistics.size(); i++) {
			if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Mon")) {
				sommeCalmond = sommeCalmond
						+ runnerStatistics.get(i).getSpeed();
			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Tue")) {
				sommeCaltue = sommeCaltue + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Wed")) {
				sommeCalwed = sommeCalwed + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Thu")) {
				sommeCalmthu = sommeCalmthu
						+ runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Fri")) {
				distanceFriday = distanceFriday
						+ runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Sat")) {
				sommeCalsat = sommeCalsat + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Sun")) {
				sommeCalsun = sommeCalsun + runnerStatistics.get(i).getSpeed();

			}

		}
		speedsDays.add(0, sommeCalmond);
		speedsDays.add(1, sommeCaltue);
		speedsDays.add(2, sommeCalwed);
		speedsDays.add(3, sommeCalmthu);
		speedsDays.add(4, sommeCalfri);
		speedsDays.add(5, sommeCalsat);
		speedsDays.add(6, sommeCalsun);
	}

	public void setSpeedMonth(List<RunnerStatistics> runnerStatistics) {
		Double sommeCaljan = 0.0;
		Double sommeCalfeb = 0.0;
		Double sommeCalmar = 0.0;
		Double sommeCalapr = 0.0;
		Double sommeCalmai = 0.0;
		Double sommeCaljun = 0.0;
		Double sommeCaljul = 0.0;
		Double sommeCalaug = 0.0;
		Double sommeCalsep = 0.0;
		Double sommeCaloct = 0.0;
		Double sommeCalnov = 0.0;
		Double sommeCaldec = 0.0;

		for (int i = 0; i < runnerStatistics.size(); i++) {
			if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jan")) {
				sommeCaljan = sommeCaljan + runnerStatistics.get(i).getSpeed();
			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Feb")) {
				sommeCalfeb = sommeCalfeb + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Mar")) {
				sommeCalmar = sommeCalmar + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Apr")) {
				sommeCalapr = sommeCalapr + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("May")) {
				sommeCalmai = sommeCalmai + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jun")) {
				sommeCaljun = sommeCaljun + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Jul")) {
				sommeCaljul = sommeCaljul + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Aug")) {
				sommeCalaug = sommeCalaug + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Oct")) {
				sommeCaloct = sommeCaloct + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Nov")) {
				sommeCalnov = sommeCalnov + runnerStatistics.get(i).getSpeed();

			} else if (runnerStatistics.get(i).getRunningDate().toString()
					.contains("Dec")) {
				sommeCaldec = sommeCaldec + runnerStatistics.get(i).getSpeed();

			}

		}
		speedsMonths.add(0, sommeCaljan);
		speedsMonths.add(1, sommeCalfeb);
		speedsMonths.add(2, sommeCalmar);
		speedsMonths.add(3, sommeCalapr);
		speedsMonths.add(4, sommeCalmai);
		speedsMonths.add(5, sommeCaljun);
		speedsMonths.add(6, sommeCaljul);
		speedsMonths.add(7, sommeCalaug);
		speedsMonths.add(8, sommeCalsep);
		speedsMonths.add(9, sommeCaloct);
		speedsMonths.add(10, sommeCalnov);
		speedsMonths.add(11, sommeCaldec);
	}

	/**
	 * 
	 * Content chart calories
	 */
	public void loadContentChartCalories(String period) {
		InputStream is;
		try {
			is = ressources.getAssets().open("ChartCaloriesDays.html");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String str = new String(buffer);

			if (toastMsg.equals("Large screen")) {
				str = str.replace("%DATAW%", "780px");
				str = str.replace("%DATAH%", "250px");
				if (period.equals("days")) {
					str = str.replace("%DATA%",
							createJsonDataDays(caloriesDays));
				} else if (period.equals("months")) {

					str = str.replace("%DATA%",
							createJsonDataMonth(caloriesMonths));
				}

			} else if (toastMsg.equals("Normal screen")) {
				str = str.replace("%DATAW%", "1000px");
				str = str.replace("%DATAH%", "420px");
				if (period.equals("days")) {
					str = str.replace("%DATA%",
							createJsonDataDays(caloriesDays));
				} else if (period.equals("months")) {

					str = str.replace("%DATA%",
							createJsonDataMonth(caloriesMonths));
				}
			}

			webViewCalories.loadDataWithBaseURL("file:///android_asset/", str,
					"text/html", "utf-8", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Content chart distance
	 */
	public void loadContentChartDistance(String period) {
		InputStream is;
		try {
			is = ressources.getAssets().open("ChartFileDay.html");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String str = new String(buffer);
			if (toastMsg.equals("Large screen")) {
				str = str.replace("%DATAW%", "780px");
				str = str.replace("%DATAH%", "250px");
				if (period.equals("days")) {
					str = str.replace("%DATA%",
							createJsonDataDays(distanceDays));
				} else if (period.equals("months")) {

					str = str.replace("%DATA%",
							createJsonDataMonth(distanceMonths));
				}

			} else if (toastMsg.equals("Normal screen")) {
				str = str.replace("%DATAW%", "1000px");
				str = str.replace("%DATAH%", "400px");
				if (period.equals("days")) {
					str = str.replace("%DATA%",
							createJsonDataDays(distanceDays));
				} else if (period.equals("months")) {

					str = str.replace("%DATA%",
							createJsonDataMonth(distanceMonths));
				}

			}

			webViewDistance.loadDataWithBaseURL("file:///android_asset/", str,
					"text/html", "utf-8", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Content chart speed
	 */
	public void loadContentChartSpeed(String period) {
		InputStream is;
		try {
			is = ressources.getAssets().open("ChartFileDay.html");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String str = new String(buffer);
			if (toastMsg.equals("Large screen")) {
				str = str.replace("%DATAW%", "780px");
				str = str.replace("%DATAH%", "250px");
				if (period.equals("days")) {
					str = str.replace("%DATA%", createJsonDataDays(speedsDays));
				} else if (period.equals("months")) {

					str = str.replace("%DATA%",
							createJsonDataMonth(speedsMonths));
				}

			} else if (toastMsg.equals("Normal screen")) {
				str = str.replace("%DATAW%", "1000px");
				str = str.replace("%DATAH%", "400px");
				if (period.equals("days")) {
					str = str.replace("%DATA%", createJsonDataDays(speedsDays));
				} else if (period.equals("months")) {

					str = str.replace("%DATA%",
							createJsonDataMonth(speedsMonths));
				}
			}

			webViewSpeed.loadDataWithBaseURL("file:///android_asset/", str,
					"text/html", "utf-8", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Get the json data form
	 * 
	 */
	public String createJsonDataDays(List<Double> values) {
		JsonArray array = new JsonArray();
		for (int i = 0; i < tableauDays.length; i++) {
			JsonObject element = new JsonObject();
			element.addProperty("category", tableauDays[i]);
			if (values.get(i) != null) {
				element.addProperty("column-1", values.get(i));
				array.add(element);
			}
		}

		return array.toString();

	}

	public String createJsonDataMonth(List<Double> values) {
		JsonArray array = new JsonArray();
		for (int i = 0; i < tableauMonths.length; i++) {
			JsonObject element = new JsonObject();
			element.addProperty("category", tableauMonths[i]);
			if (values.get(i) != null) {
				element.addProperty("column-1", values.get(i));
				array.add(element);
			}
		}
		return array.toString();
	}

	/**
	 * get stat by date
	 */

	public void makeGetStatByDate(String ID, String date) {

		showpDialog();
		String url = "http://runningchallange5950.azurewebsites.net/api/RunnerStatistics?runnerId=%1$s&period=Date&date=%2$s";
		String uri = String.format(url, ID, date);

		JsonArrayRequest req = new JsonArrayRequest(uri,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						Toast.makeText(getApplicationContext(), "Success!",
								Toast.LENGTH_SHORT).show();

						hidepDialog();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TestWS.class.getSimpleName(), "Error: "
								+ error.getMessage());
						Toast.makeText(getApplicationContext(),
								"Failed getting data", Toast.LENGTH_SHORT)
								.show();
						hidepDialog();
					}
				});

		AppController.getInstance().addToRequestQueue(req);
	}

	/**
	 * get stat by period
	 */

	public void makeGetStatByPeriod(String ID, String startDate, String endDate) {

		showpDialog();
		String url = "http://runningchallange5950.azurewebsites.net/api/RunnerStatistics?runnerId=%1$s&period=Period&date=%2$s&endPeriodDate=%3$s";
		String uri = String.format(url, ID, startDate, endDate);

		JsonArrayRequest req = new JsonArrayRequest(uri,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						Toast.makeText(getApplicationContext(), "Success!",
								Toast.LENGTH_SHORT).show();

						hidepDialog();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TestWS.class.getSimpleName(), "Error: "
								+ error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
						hidepDialog();
					}
				});

		AppController.getInstance().addToRequestQueue(req);
	}

	/**
	 * SHow and hide a dialog progress bar
	 */
	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
