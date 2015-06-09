package com.example.runningchallenge;


import model.Runner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ressources.Constatntes;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.runningchallenge.app.AppController;

import contentprovider.DatabaseHelper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ClassementActivity extends ActionBarActivity {
	private ProgressDialog pDialog;
	// json response to get runner rank
		private String urlgetRank = Constatntes.BASE_URL + "RunnerScores/%1$s";
		Runner runner;
		DatabaseHelper helper;
		String rank = "";
		TextView tx;
		String firstScore;
		String secondScore;
		String thirdScore;
		TextView gangnant3Text;
		TextView gagnant2Text;
		TextView gagnant1Text;
		// json array response url get winners
				private String apiWinners = Constatntes.BASE_URL + "RunnerScores?count=3";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_classement);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#99000000")));
		Drawable d = getResources().getDrawable(R.drawable.header);
		actionBar.setCustomView(R.layout.actionbar_custom_view_home);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setBackgroundDrawable(d);
		actionBar.setBackgroundDrawable(d);
		ImageButton buttonClass = (ImageButton)findViewById(R.id.actionBarIconClassment);
		buttonClass.setImageDrawable(getResources().getDrawable(R.drawable.retour7));
		buttonClass.getBackground().setAlpha(5); 
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		helper = new DatabaseHelper(getApplicationContext());
		runner = helper.findAllRunner().get(0);
		buttonClass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NavUtils.navigateUpFromSameTask(ClassementActivity.this);
			}
		});

		
//		ImageButton buttonStat = (ImageButton)findViewById(R.id.actionBarIconStat);
//		buttonStat.setVisibility(View.GONE);
		
		 tx = (TextView) findViewById(R.id.textClassement);
		TextView tx1 = (TextView) findViewById(R.id.textClassementTop);
		gangnant3Text = (TextView) findViewById(R.id.gangnant3Text);
		gagnant2Text = (TextView) findViewById(R.id.gagnant2Text);
		gagnant1Text = (TextView) findViewById(R.id.gagnant1Text);
		TextView tx5 = (TextView) findViewById(R.id.textView1);
		TextView tx6 = (TextView) findViewById(R.id.textView3);
		TextView tx7 = (TextView) findViewById(R.id.textView2);

		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/berlin-sans-fb-demi-bold.ttf");
		tx.setTypeface(custom_font);
		tx1.setTypeface(custom_font);
		gangnant3Text.setTypeface(custom_font);
		gagnant2Text.setTypeface(custom_font);
		gagnant1Text.setTypeface(custom_font);
		tx5.setTypeface(custom_font);
		tx6.setTypeface(custom_font);
		tx7.setTypeface(custom_font);
		getFirstWinners();
		getRank(runner.getBackID());
		
	}
	
	public void getRank(String ID) {
		
		String uri = String.format(urlgetRank, ID);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {
							rank = response.getString("RunnerRank");
							tx.setText("Votre classement est : "+rank);
//							Toast.makeText(getApplicationContext(), rank,
//									Toast.LENGTH_LONG).show();

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "Error: ",
									Toast.LENGTH_LONG).show();
							hidepDialog();
						}
						hidepDialog();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TestWS.class.getSimpleName(), "Error: "
								+ error.getMessage());
						Toast.makeText(getApplicationContext(),
								error.getMessage(), Toast.LENGTH_SHORT).show();
						// hide the progress dialog
						hidepDialog();
					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);

	

	}
	
	public void getFirstWinners() {
		showpDialog();

		JsonArrayRequest req = new JsonArrayRequest(apiWinners,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {
							// Parsing json array response
							// loop through each json object
						
							

								JSONObject runner1 = (JSONObject) response
										.get(0);
								firstScore = runner1.getString("BestScore");
								gagnant1Text.setText(firstScore);
								
								JSONObject runner2 = (JSONObject) response
										.get(1);
								secondScore = runner2.getString("BestScore");
								gagnant2Text.setText(secondScore);
								JSONObject runner3 = (JSONObject) response
										.get(2);
								 thirdScore = runner3.getString("BestScore");
								 gangnant3Text.setText(thirdScore);
							

							

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
							hidepDialog();
						}

						
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
