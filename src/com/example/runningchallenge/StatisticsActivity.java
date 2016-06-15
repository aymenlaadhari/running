package com.example.runningchallenge;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.facebook.Session;

public class StatisticsActivity extends ActionBarActivity {
	WebView webView;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics_app);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#99000000")));
		Drawable d=getResources().getDrawable(R.drawable.header); 
		actionBar.setCustomView(R.layout.actionbar_custom_view_home);
		actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(d);
	    ImageButton buttonClass = (ImageButton)findViewById(R.id.actionBarIconClassment);
		buttonClass.setImageDrawable(getResources().getDrawable(R.drawable.retour7));
		buttonClass.getBackground().setAlpha(32); 
		buttonClass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NavUtils.navigateUpFromSameTask(StatisticsActivity.this);
			}
		});

		
		ImageButton buttonStat = (ImageButton)findViewById(R.id.actionBarIconStat);
		buttonStat.setVisibility(View.GONE);

		webView = (WebView) findViewById(R.id.web);
		String content = "<html>"
				+ "  <head>"
				+ "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
				+ "    <script type=\"text/javascript\">"
				+ "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
				+ "      google.setOnLoadCallback(drawChart);"
				+ "      function drawChart() {"
				+ "        var data = google.visualization.arrayToDataTable(["
				+ "          ['Year', 'Sales', 'Expenses'],"
				+ "          ['2010',  1000,      400],"
				+ "          ['2011',  1170,      460],"
				+ "          ['2012',  660,       1120],"
				+ "          ['2013',  1030,      540]"
				+ "        ]);"
				+ "        var options = {"
				+ "          title: 'Truiton Performance',"
				+ "          hAxis: {title: 'Year', titleTextStyle: {color: 'red'}}"
				+ "        };"
				+ "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
				+ "        chart.draw(data, options);"
				+ "      }"
				+ "    </script>"
				+ "  </head>"
				+ "  <body>"
				+ "    <div id=\"chart_div\" style=\"width: auto; height:auto;\"></div>"
				+ "    <img style=\"padding: 0; margin: 0 0 0 330px; display: block;\" src=\"truiton.png\"/>"
				+ "  </body>" + "</html>";
		String content1 = "<html>"
				+ "  <head>"
				+ "    <script type=\"text/javascript\" src=\"jsapi.js\"></script>"
				+ "    <script type=\"text/javascript\">"
				+ "      google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});"
				+ "      google.setOnLoadCallback(drawChart);"
				+ "      function drawChart() {"
				+ "        var data = google.visualization.arrayToDataTable(["
				+ "          ['Year', 'Sales', 'Expenses'],"
				+ "          ['2010',  1000,      400],"
				+ "          ['2011',  1170,      460],"
				+ "          ['2012',  660,       1120],"
				+ "          ['2013',  1030,      540]"
				+ "        ]);"
				+ "        var options = {"
				+ "          title: 'Truiton Performance',"
				+ "          hAxis: {title: 'Year', titleTextStyle: {color: 'red'}}"
				+ "        };"
				+ "        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));"
				+ "        chart.draw(data, options);"
				+ "      }"
				+ "    </script>"
				+ "  </head>"
				+ "  <body>"
				+ "    <div id=\"chart_div\" style=\"width: auto; height:auto;\"></div>"
				+ "    <img style=\"padding: 0; margin: 0 0 0 330px; display: block;\" src=\"truiton.png\"/>"
				+ "  </body>" + "</html>";
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.requestFocusFromTouch();
		webView.loadDataWithBaseURL("file:///android_asset/", content,
				"text/html", "utf-8", null);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setSupportZoom(false);
		webView.setInitialScale(68);
		webView.getSettings().setUseWideViewPort(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_app, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		

		return super.onOptionsItemSelected(item);
	}
}
