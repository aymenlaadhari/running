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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.runningchallenge.app.AppController;
import com.google.gson.JsonObject;

import contentprovider.DatabaseHelper;

public class WebService extends Activity {
	Runner runnerPref;
	Runner runner;
	RunnerStatistics runnerStatistics;
	boolean exist = false;
	boolean isFacebook = true;
	DatabaseHelper helper;
	Constatntes constatntes;
	String uri;
	public String weight;
	public String height;
	// json array response url get runners information
	private String apiRunners = Constatntes.BASE_URL + "Runners";
	// json array response url get statisics
	private String urlgetStatistics = Constatntes.BASE_URL
			+ "RunnerStatistics?externalID=%1$s&externalProvider=%2$s";
	// url post statistics
	private String urlPostStatistics = Constatntes.BASE_URL
			+ "RunnerStatistics?externalID=%1$s&externalProvider=%2$s";
	// url verify exist runner
	private String urlExist = Constatntes.BASE_URL
			+ "Runners?externalID=%1$s&externalProvider=%2$s";
	// json response to get runner rank
	private String urlgetRank = Constatntes.BASE_URL + "RunnerScores/%1$s";
	// url update runner
	private String urlUpdateRunner = Constatntes.BASE_URL + "Runners/%1$s";
	// Progress dialog
	private ProgressDialog pDialog;
	private String jsonResponse;
	JSONObject runnerJson;
	String ID;
	Intent intentPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		pDialog = new ProgressDialog(WebService.this);
		helper = new DatabaseHelper(this);
		intentPreferences = new Intent(this, PreferencesActivity.class);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);
		setContentView(R.layout.webservice_layout);
		String verif = getIntent().getStringExtra("verify");
		if(verif.equals("twitter"))
		{
			isFacebook = false;
		}

		try {

			Thread.sleep(2000);
			runner = helper.findAllRunner().get(0);
			hidepDialog();
			if(isFacebook)
			{
				exist(runner.getFbID());
			}
			else {
				exist(runner.getTwID());
			}
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Method to make json Array request for runners informations where json
	 * response starts wtih [
	 * */
	public void makeGetRunnerInfo() {

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

						} catch (JSONException e) {
							e.printStackTrace();

						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TestWS.class.getSimpleName(), "Error: "
								+ error.getMessage());

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
		params.put("RunnerId", "0");
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
		params.put("RunnerId", "2");// default
									// value
		params.put("RunningDate",
				String.valueOf(runnerStatistics.getRunningDate()));
		params.put("Calories", "4.1");
		params.put("Speed", "5.1");
		params.put("Time", "6.1");
		params.put("Distance", "7.1");
		params.put("Score", "8.1");

		String uri = String.format(urlPostStatistics, ID, provider);
		JsonObjectRequest req = new JsonObjectRequest(uri, new JSONObject(
				params), new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {

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

			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		};
	}

	/**
	 * Method to make json Array request for statistics
	 * 
	 * */
	public void makeGetStat(String ID, String provider) {

		String uri = String.format(urlPostStatistics, ID, provider);

		JsonArrayRequest req = new JsonArrayRequest(uri,
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

						} catch (JSONException e) {
							e.printStackTrace();

						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TestWS.class.getSimpleName(), "Error: "
								+ error.getMessage());

					}
				});

		AppController.getInstance().addToRequestQueue(req);
	}

	/**
	 * verify runner existence
	 */
	public void exist(String ID) {
		runnerPref = new Runner();

		if (isFacebook) {
			uri = String.format(urlExist, ID, "Facebook");

		} else {
			uri = String.format(urlExist, ID, "Twitter");

		}
		showpDialog();

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {
							if (response.getString("Exist").equals("true")) {
								exist = true;

								JSONObject runnerJson = response
										.getJSONObject("Runner");
								runner.setHeigh(Integer.parseInt(runnerJson
										.getString("Height")));
								runner.setWeight(Float.parseFloat(runnerJson
										.getString("Weight")));
								runner.setBackID(runnerJson
										.getString("RunnerId"));

								try {
									helper.updateRunner(runner);
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if(isFacebook)
								{
									intentPreferences.putExtra("runner", "updateFacebook");
									startActivity(intentPreferences);
								}
								else {
									intentPreferences.putExtra("runner", "updateTwitter");
									startActivity(intentPreferences);
								}
								
								finish();
							} else {
								exist = false;
								if(isFacebook)
								{
									intentPreferences.putExtra("runner", "createFacebook");
									startActivity(intentPreferences);
								}
								else {
									intentPreferences.putExtra("runner", "createTwitter");
									startActivity(intentPreferences);
								}
								finish();
							}

						} catch (JSONException e) {
							e.printStackTrace();

						}
						hidepDialog();

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
	 * Update runner informations on the backOffice
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
		params.put("Height", String.valueOf("300"));
		params.put("Weight", String.valueOf("7.1"));
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

	public Runner getRunnerPref() {
		return runnerPref;
	}

	public void setRunnerPref(Runner runnerPref) {
		this.runnerPref = runnerPref;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public JSONObject getRunner(String ID) {

		if (isFacebook) {
			uri = String.format(urlExist, ID, "Facebook");

		} else {
			uri = String.format(urlExist, ID, "Twitter");

		}

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {

							runnerJson = response.getJSONObject("Runner");

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

		return runnerJson;
	}

	public String getExistedWeight(String ID) {
		if (isFacebook) {
			uri = String.format(urlExist, ID, "Facebook");

		} else {
			uri = String.format(urlExist, ID, "Twitter");

		}

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {

							JSONObject runnerJson = response
									.getJSONObject("Runner");
							weight = runnerJson.getString("Weight");

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

		return weight;
	}

	public String getExistedHeight(String ID) {
		if (isFacebook) {
			uri = String.format(urlExist, ID, "Facebook");

		} else {
			uri = String.format(urlExist, ID, "Twitter");

		}

		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri,
				null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());

						try {

							JSONObject runnerJson = response
									.getJSONObject("Runner");
							height = runnerJson.getString("Height");

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

		return height;
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

}
