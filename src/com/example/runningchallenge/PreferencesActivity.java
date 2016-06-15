package com.example.runningchallenge;

import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import ressources.Constatntes;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.runningchallenge.app.AppController;
import com.google.android.gms.internal.nu;

import model.Runner;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas.VertexMode;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import contentprovider.DatabaseHelper;

public class PreferencesActivity extends ActionBarActivity {
	TextView age;
	private TextView taille;
	private TextView poid;
	boolean isFacebook = true;
	String verif = null;
	String RunnerAge;
	public static final String PREFERENCES = "Prefs";
	public static final String Age = "ageKey";
	public static final String Taille = "tailleKey";
	public static final String Poid = "poidKey";
	public static final String Homme = "hommetKey";
	public static final String Femme = "femmeKey";
	public static final String First = "firstKey";
	public boolean first = false;
	public boolean homme = false;
	public boolean femme = false;
	String birth;
	// url update runner
	private String urlUpdateRunner = Constatntes.BASE_URL + "Runners/%1$s";
	Runner runner;
	// json array response url get runners information
	private String apiRunners = Constatntes.BASE_URL + "Runners";
	DatabaseHelper helper = new DatabaseHelper(this);

	SharedPreferences sharedpreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#99000000")));
		Drawable d = getResources().getDrawable(R.drawable.header);
		actionBar.setCustomView(R.layout.actionbar_custom_view_home);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setBackgroundDrawable(d);
		actionBar.setBackgroundDrawable(d);
		final ImageView checkFemme = (ImageView) findViewById(R.id.imageView3);
		final ImageView checkedFemme = (ImageView) findViewById(R.id.ImageView08);
		final ImageView checkHomme = (ImageView) findViewById(R.id.checkHomme);
		final ImageView checkedHomme = (ImageView) findViewById(R.id.ImageView09);
		final ImageView buttonConfirm = (ImageView) findViewById(R.id.ImageView10);
		final ImageView imageAge = (ImageView) findViewById(R.id.ImageView03);
		final ImageView imageTaille = (ImageView) findViewById(R.id.ImageView05);
		final ImageView imagePoid = (ImageView) findViewById(R.id.ImageView07);
		final TextView textFemme = (TextView) findViewById(R.id.textView3);
		final TextView textHomme = (TextView) findViewById(R.id.textView5);
		final TextView textAge = (TextView) findViewById(R.id.textView1);
		final TextView textTaille = (TextView) findViewById(R.id.TextView01);
		final TextView textPoid = (TextView) findViewById(R.id.textView6);
		final TextView submit = (TextView) findViewById(R.id.textView4);
		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/berlin-sans-fb-demi-bold.ttf");
		textAge.setTypeface(type);
		textHomme.setTypeface(type);
		textFemme.setTypeface(type);
		textPoid.setTypeface(type);
		textTaille.setTypeface(type);
		submit.setTypeface(type);
		age = (TextView) findViewById(R.id.editAge);
		taille = (TextView) findViewById(R.id.editTaille);
		poid = (TextView) findViewById(R.id.editPoid);
		verif = getIntent().getStringExtra("runner");

		age.setTypeface(type);
		taille.setTypeface(type);
		poid.setTypeface(type);
		sharedpreferences = getSharedPreferences(PREFERENCES,
				Context.MODE_PRIVATE);
		runner = helper.findAllRunner().get(0);

		if (runner.isMale()) {
			homme = true;
		} else if (!runner.isMale()) {
			femme = true;
		}

		
		ImageButton buttonClass = (ImageButton) findViewById(R.id.actionBarIconClassment);
		buttonClass.setVisibility(View.INVISIBLE);

		checkFemme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (homme) {
					checkedFemme.setVisibility(View.VISIBLE);
					checkedHomme.setVisibility(View.INVISIBLE);
					femme = true;
					homme = false;

				} else {
					checkedFemme.setVisibility(View.VISIBLE);
					femme = true;
					homme = false;
				}

			}
		});

		checkedFemme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkedFemme.setVisibility(View.INVISIBLE);
				checkFemme.setVisibility(View.VISIBLE);
				femme = false;

			}
		});

		checkHomme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (femme) {
					checkedHomme.setVisibility(View.VISIBLE);
					checkedFemme.setVisibility(View.INVISIBLE);
					homme = true;
					femme = false;
				} else {
					checkedHomme.setVisibility(View.VISIBLE);
					homme = true;
					femme = false;
				}

			}
		});

		checkedHomme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkedHomme.setVisibility(View.INVISIBLE);
				homme = false;

			}
		});

		if (verif == null) {
			taille.setText(String.valueOf(runner.getHeigh()));
			poid.setText(String.valueOf(runner.getWeight()));
		} else if (verif.equals("updateFacebook")) {
			taille.setText(String.valueOf(runner.getHeigh()));
			poid.setText(String.valueOf(runner.getWeight()));
		} else if (verif.equals("updateTwitter")) {
			isFacebook = false;
			taille.setText(String.valueOf(runner.getHeigh()));
			poid.setText(String.valueOf(runner.getWeight()));
		} else if (verif.equals("createTwitter")) {
			isFacebook = false;
		}
		
		if (sharedpreferences.contains(Taille)) {
			taille.setText(sharedpreferences.getString(Taille, ""));

		}
		if (sharedpreferences.contains(Poid)) {
			poid.setText(sharedpreferences.getString(Poid, ""));

		}
		if (sharedpreferences.contains(Homme)) {
			homme = (sharedpreferences.getBoolean(Homme, false));
		}
		if (sharedpreferences.contains(Femme)) {
			femme = (sharedpreferences.getBoolean(Femme, false));
		}
		if (homme) {
			checkedHomme.setVisibility(View.VISIBLE);

		}
		if (femme) {
			checkedFemme.setVisibility(View.VISIBLE);
		}

		imageAge.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				

				Calendar c = Calendar.getInstance();

				int cyear = c.get(Calendar.YEAR);
				int cmonth = c.get(Calendar.MONTH);
				int cday = c.get(Calendar.DAY_OF_MONTH);
				DatePickerDialog dialog = new DatePickerDialog(
						PreferencesActivity.this, new OnDateSetListener() {

							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {

								String date_selected = String.format("%02d",
										year)
										+ "-"
										+ String.format("%02d", monthOfYear + 1)
										+ "-"
										+ String.format("%02d", dayOfMonth);
								Toast.makeText(getApplicationContext(),
										date_selected, Toast.LENGTH_LONG)
										.show();
								birth = date_selected;
								int age = Calendar.getInstance().get(
										Calendar.YEAR)
										- year;
								if (Calendar.getInstance().get(Calendar.MONTH) < monthOfYear) {
									age--;
								} else if (Calendar.getInstance().get(
										Calendar.MONTH) == monthOfYear
										&& Calendar.getInstance().get(
												Calendar.DAY_OF_MONTH) < dayOfMonth) {
									age--;
								}
								RunnerAge = String.valueOf(age);
								PreferencesActivity.this.age.setText(RunnerAge);

								
							}
						}, cyear, cmonth, cday);
			
				dialog.setTitle("Your birth day");
				dialog.show();

			}
		});

		imageTaille.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(v
						.getContext());
				final EditText inputTaille = new EditText(
						getApplicationContext());
				inputTaille.setText(taille.getText().toString());
				inputTaille.setInputType(InputType.TYPE_CLASS_NUMBER);
				inputTaille.setTextColor(Color.parseColor("#ff5d00"));
				inputTaille
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
								3) });
				alert.setView(inputTaille);

				alert.setTitle("Height(cm)")
						.setMessage("Please fill your height")
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// continue with delete
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == DialogInterface.BUTTON_NEGATIVE)
											dialog.dismiss();
									}
								})
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										taille.setText(inputTaille.getText()
												.toString());

									}
								})

						.show();

			}
		});
		imagePoid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(v
						.getContext());
				final EditText inputPoid = new EditText(getApplicationContext());
				inputPoid.setText(poid.getText().toString());
				inputPoid.setInputType(InputType.TYPE_CLASS_NUMBER
						| InputType.TYPE_NUMBER_FLAG_DECIMAL
						| InputType.TYPE_NUMBER_FLAG_SIGNED);
				inputPoid.setTextColor(Color.parseColor("#ff5d00"));
				inputPoid
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
								5) });
				alert.setView(inputPoid);

				alert.setTitle("Weight(kg)")
						.setMessage("Please fill your weight")
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// continue with delete
									}
								})
						.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										if (which == DialogInterface.BUTTON_NEGATIVE)
											dialog.dismiss();
									}
								})
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										poid.setText(inputPoid.getText()
												.toString());

									}
								})

						.show();

			}
		});
		buttonConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkNotEmpty()) {
					run(v);
					Intent intentMap = new Intent(PreferencesActivity.this,
							MapActivity.class);
					intentMap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intentMap);
					finish();
				} else {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(
							PreferencesActivity.this);
					builder1.setMessage("Please fill all required informations");
					builder1.setCancelable(true);
					builder1.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

					AlertDialog alert11 = builder1.create();
					alert11.show();
				}

			}
		});
	}

	public boolean checkNotEmpty() {
		if (age.getText().toString().isEmpty()
				&& taille.getText().toString().isEmpty()
				&& poid.getText().toString().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void run(View view) {
		String a = age.getText().toString();
		String tail = taille.getText().toString();
		String p = poid.getText().toString();
		Boolean h = homme;
		Boolean f = femme;
		first = true;
		Editor editor = sharedpreferences.edit();
		editor.putString(Age, a);
		editor.putString(Taille, tail);
		editor.putString(Poid, p);
		editor.putBoolean(Homme, h);
		editor.putBoolean(Femme, f);
		editor.putBoolean(First, first);
		runner.setHeigh(Integer.parseInt(taille.getText().toString()));
		runner.setWeight(Float.parseFloat(poid.getText().toString()));
		runner.setBirth(birth);
		helper.updateRunner(runner);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (verif == null) {
			updateRunner(runner.getBackID());
		} else if (verif.equals("updateFacebook")) {
			updateRunner(runner.getBackID());

		} else if (verif.equals("createFacebook")) {
			makePostRunner();
		} else if (verif.equals("updateTwitter")) {
			updateRunner(runner.getBackID());

		} else if (verif.equals("createTwitter")) {
			makePostRunnerTwitter();
		}
		editor.commit();

	}

	/**
	 * Update runner informations on the backOffice
	 */

	public void updateRunner(String ID) {
		String uri = String.format(urlUpdateRunner, ID);
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("Content-Type", "application/json; charset=utf-8");
		params.put("RunnerId", ID);
		params.put("FirstName", runner.getFirstName());
		params.put("LastName", runner.getLastName());
		String birth = runner.getBirth();
		if (birth != null) {

			params.put("Birthday", runner.getBirth());

		} else {
			params.put("Birthday", "1980-09-19");
		}
		if (runner.isMale()) {
			params.put("IsMale", "true");
		} else {
			params.put("IsMale", "false");
		}
		params.put("Height", String.valueOf(runner.getHeigh()));
		params.put("Weight", String.valueOf(runner.getWeight()));
		if (isFacebook) {
			params.put("ExternalID", runner.getFbID());
			params.put("ExternalProvider", "Facebook");
		} else {
			params.put("ExternalID", runner.getTwID());
			params.put("ExternalProvider", "Twitter");
		}

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.PUT, uri,
				new JSONObject(params), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {
							VolleyLog.v("Response:%n %s", response.toString(4));

						} catch (JSONException e) {
							e.printStackTrace();

						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TestWS.class.getSimpleName(), "Error: "
								+ error.getMessage());

						// hide the progress dialog

					}
				});

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(jsonObjReq);

	}

	/**
	 * post a runner
	 */
	public void makePostRunner() {
		final String URL = apiRunners;
		// Post params to be sent to the server
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json; charset=utf-8");
		params.put("RunnerId", "0");

		params.put("FirstName", runner.getFirstName());
		if (isFacebook) {
			params.put("LastName", runner.getLastName());
		} else {
			params.put("LastName", "Not configured for Twitter");
		}

		String birth = runner.getBirth();
		if (birth != null) {

			params.put("Birthday", runner.getBirth());
		} else {
			params.put("Birthday", "1980-09-19");
		}

		if (runner.isMale()) {
			params.put("IsMale", "true");
		} else {
			params.put("IsMale", "false");
		}
		params.put("Height", String.valueOf(runner.getHeigh()));
		params.put("Weight", String.valueOf(runner.getWeight()));
		if (isFacebook) {
			params.put("ExternalID", runner.getFbID());
			params.put("ExternalProvider", "Facebook");
		} else {
			params.put("ExternalID", runner.getTwID());
			params.put("ExternalProvider", "Twitter");
		}

		JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(
				params), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {

					runner.setBackID(response.getString("RunnerId"));
					helper.updateRunner(runner);
					VolleyLog.v("Response:%n %s", response.toString(4));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Error: ", error.getMessage());
			}
		});

		// add the request object to the queue to be executed
		AppController.getInstance().addToRequestQueue(req);

	}

	public void makePostRunnerTwitter() {
		final String URL = apiRunners;
		// Post params to be sent to the server
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json; charset=utf-8");
		params.put("RunnerId", "0");

		params.put("FirstName", runner.getFirstName());

		params.put("LastName", "Not configured for Twitter");
		params.put("Birthday", runner.getBirth());

		params.put("IsMale", "true");

		params.put("Height", String.valueOf(runner.getHeigh()));
		params.put("Weight", String.valueOf(runner.getWeight()));

		params.put("ExternalID", runner.getTwID());
		params.put("ExternalProvider", "Twitter");

		JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(
				params), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {

					Toast.makeText(getApplicationContext(), "data received",
							Toast.LENGTH_LONG).show();
					VolleyLog.v("Response:%n %s", response.toString(4));

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.e("Error: ", error.getMessage());
			}
		});

		// add the request object to the queue to be executed
		AppController.getInstance().addToRequestQueue(req);

	}

}
