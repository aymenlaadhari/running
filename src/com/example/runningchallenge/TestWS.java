package com.example.runningchallenge;

import java.util.HashMap;

import model.Runner;
import model.RunnerStatistics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ressources.Constatntes;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.runningchallenge.app.AppController;

import contentprovider.DatabaseHelper;

public class TestWS extends Activity {
	Runner runner;
	RunnerStatistics runnerStatistics;
	boolean exist = true;
	boolean isFacebook = true;
	DatabaseHelper helper;
	Constatntes constatntes;
	String uri;

	// json array response url get runners information
	private String apiRunners = Constatntes.BASE_URL + "Runners";
	// json array response url get statisics
	private String urlgetStatistics = Constatntes.BASE_URL + "RunnerStatistics";
	// url post statistics
	private String urlPostStatistics = Constatntes.BASE_URL
			+ "RunnerStatistics?externalID=%1$s&externalProvider=%2$s";
	// url verify exist runner
	private String urlExist = Constatntes.BASE_URL + "Runners?externalID=%1$s&externalProvider=%2$s";
	// json response to get runner rank
	private String urlgetRank = Constatntes.BASE_URL
			+ "RunnerScores/%1$s";
	// url update runner
	private String urlUpdateRunner = Constatntes.BASE_URL + "Runners/%1$s";
	// Progress dialog
	private ProgressDialog pDialog;
	private String jsonResponse;
	TextView txtResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_ws);

		helper = new DatabaseHelper(getApplicationContext());
		runner = helper.findAllRunner().get(0);
		runnerStatistics = helper.findAllStat().get(
				helper.findAllRunner().size() - 1);
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		txtResponse = (TextView) findViewById(R.id.dtaJson);
		Button buttonPostRunner = (Button) findViewById(R.id.PostRunner);
		buttonPostRunner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				makePostRunner();

			}
		});
		Button buttonGet = (Button) findViewById(R.id.GetRunnerStat);
		buttonGet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				makeGetRunnerInfo();
			}
		});

		Button buttongetScore = (Button) findViewById(R.id.GetRank);
		buttongetScore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getRunnerRank(runner.getBackID()); 
			}
		});

		Button buttonGetStat = (Button) findViewById(R.id.GetStatistics);
		buttonGetStat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				makeGetStat();
			}
		});

		Button postStat = (Button) findViewById(R.id.PostStat);
		postStat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				makePostStatistics(runner.getFbID(),"Facebook");
			

			}
		});
		Button existRunner = (Button) findViewById(R.id.existRunner);
		existRunner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exist(runner.getFbID());

			}
		});

		Button buttonUpdateRunner = (Button) findViewById(R.id.updateRunner);
		buttonUpdateRunner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateRunner(runner.getBackID());

			}
		});

		Button buttonDB = (Button) findViewById(R.id.button1);
		buttonDB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentDB = new Intent(getApplication(),
						AndroidDatabaseManager.class);
				startActivity(intentDB);

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_w, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Method to make json Array request for runners informations where json
	 * response starts wtih [
	 * */
	public void makeGetRunnerInfo() {
		showpDialog();

		JsonArrayRequest req = new JsonArrayRequest(apiRunners,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {
							// Parsing json array response
							// loop through each json object
							jsonResponse = "";
							for (int i = 0; i < response.length(); i++) {

								JSONObject runner = (JSONObject) response
										.get(i);

								String FirstName = runner
										.getString("FirstName");
								String LastName = runner.getString("LastName");
								String Birthday = runner.getString("Birthday");
								String IsMale = runner.getString("IsMale");
								String Height = runner.getString("Height");
								String Weight = runner.getString("Weight");
								String FacebookID = runner
										.getString("FacebookID");

								jsonResponse += "FirstName: " + FirstName
										+ "\n\n";
								jsonResponse += "LastName: " + LastName
										+ "\n\n";
								jsonResponse += "Birthday: " + Birthday
										+ "\n\n";
								jsonResponse += "Height: " + Height + "\n\n";
								jsonResponse += "Weight: " + Weight + "\n\n";
								jsonResponse += "FacebookID: " + FacebookID
										+ "\n\n";
								jsonResponse += "IsMale: " + IsMale + "\n\n\n";

							}

							txtResponse.setText(jsonResponse);

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
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
						hidepDialog();
					}
				});

		AppController.getInstance().addToRequestQueue(req);
	}

	/**
	 * post a runner
	 */
	public void makePostRunner() {
		final String URL = apiRunners;
		// Post params to be sent to the server
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json; charset=utf-8");
		// params.put("RunnerId", String.valueOf(runner.getRunnerID()));
		params.put("FirstName", runner.getFirstName());
		params.put("LastName", runner.getLastName());
		params.put("Birthday", "2015-01-07T00:00:00");
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
					txtResponse.setText(response.toString());
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

	/**
	 * makePostStatistics
	 */
	public void makePostStatistics(String ID, String provider) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("Content-Type", "application/json; charset=utf-8");
		params.put("RunnerStatisticsId", "0");// default value
		params.put("RunnerId", runnerStatistics.getRunner().getBackID());// default value
		params.put("RunningDate",
				String.valueOf(runnerStatistics.getRunningDate()));
		params.put("Calories", String.valueOf(runnerStatistics.getCalories()));
		params.put("Speed", String.valueOf(runnerStatistics.getSpeed()));
		params.put("Time", runnerStatistics.getTime());
		params.put("Distance", String.valueOf(runnerStatistics.getDistance()));
		params.put("Score", "8.1");
		
		String uri = String.format(urlPostStatistics,ID,provider);
		JsonObjectRequest req = new JsonObjectRequest(uri, new JSONObject(
				params), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					txtResponse.setText(response.toString());
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
		

		AppController.getInstance().addToRequestQueue(req);
	}


	
	/**
	 * Method to make request of runner rank with fb Id parameter
	 * 
	 */
	public void getRunnerRank(String ID) {
		String uri = String.format(urlgetRank, ID);
		StringRequest myReq = new StringRequest(Method.GET, uri,
				createMyReqSuccessListener(), createMyReqErrorListener());
		AppController.getInstance().addToRequestQueue(myReq);

	}

	private Response.Listener<String> createMyReqSuccessListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Toast.makeText(getApplicationContext(), response,
						Toast.LENGTH_SHORT).show();
			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getApplicationContext(), error.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		};
	}

	/**
	 * Method to make json Array request for statistics
	 * 
	 * */
	public void makeGetStat() {

		showpDialog();

		JsonArrayRequest req = new JsonArrayRequest(urlgetStatistics,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {
							// Parsing json array response
							// loop through each json object
							jsonResponse = "";
							for (int i = 0; i < response.length(); i++) {

								JSONObject staistic = (JSONObject) response
										.get(i);

								String RunnerId = staistic
										.getString("RunnerId");
								String RunningDate = staistic
										.getString("RunningDate");
								String Calories = staistic
										.getString("Calories");
								String Speed = staistic.getString("Speed");
								String Time = staistic.getString("Time");
								String Distance = staistic
										.getString("Distance");
								String Score = staistic.getString("Score");

								jsonResponse += "RunnerId: " + RunnerId
										+ "\n\n";
								jsonResponse += "RunningDate: " + RunningDate
										+ "\n\n";
								jsonResponse += "Calories: " + Calories
										+ "\n\n";
								jsonResponse += "Speed: " + Speed + "\n\n";
								jsonResponse += "Time: " + Time + "\n\n";
								jsonResponse += "Distance: " + Distance
										+ "\n\n";
								jsonResponse += "Score: " + Score + "\n\n\n";

							}

							txtResponse.setText(jsonResponse);

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
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
						hidepDialog();
					}
				});

		AppController.getInstance().addToRequestQueue(req);
	}

	/**
	 * verify runner existence
	 */
	public boolean exist(String ID) {
		
		if(isFacebook)
		{
			uri = String.format(urlExist,ID,"Facebook");

		}
		else {
			uri = String.format(urlExist,ID,"Twitter");

		}
		

		
		showpDialog();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {
							if (response.getString("Exist").equals("true")) {
								// Parsing json object response
								// response will be a json object
								String FirstName = response
										.getString("FirstName");
								String LastName = response
										.getString("LastName");
								String Birthday = response
										.getString("Birthday");
								String IsMale = response.getString("IsMale");
								String Height = response.getString("Height");
								String Weight = response.getString("Weight");
								String FacebookID = response
										.getString("FacebookID");

								jsonResponse += "FirstName: " + FirstName
										+ "\n\n";
								jsonResponse += "LastName: " + LastName
										+ "\n\n";
								jsonResponse += "Birthday: " + Birthday
										+ "\n\n";
								jsonResponse += "Height: " + Height + "\n\n";
								jsonResponse += "Weight: " + Weight + "\n\n";
								jsonResponse += "FacebookID: " + FacebookID
										+ "\n\n";
								jsonResponse += "IsMale: " + IsMale + "\n\n\n";

								txtResponse.setText(jsonResponse);
							} else {
								exist = false;
								txtResponse.setText(response.getString("Exist")+ "user do not exist");

							}

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: ",
									Toast.LENGTH_LONG).show();
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

		return exist;

	}

	
	
	/**
	 * Update runner informations
	 */

	public void updateRunner(String ID) {
		String uri = String.format(urlUpdateRunner, ID);
		HashMap<String, String> params = new HashMap<String, String>();

		params.put("Content-Type", "application/json; charset=utf-8");
		params.put("RunnerId", ID);
		params.put("FirstName", runner.getFirstName());
		params.put("LastName", runner.getLastName());
		params.put("Birthday", "2015-01-07T00:00:00");
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
		showpDialog();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.PUT, uri,
				new JSONObject(params), new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {
							VolleyLog.v("Response:%n %s", response.toString(4));
							txtResponse.setText(response.toString());

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(),
									"Error: " + e.getMessage(),
									Toast.LENGTH_LONG).show();
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