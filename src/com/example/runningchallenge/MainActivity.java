package com.example.runningchallenge;

import io.fabric.sdk.android.Fabric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import ressources.Constatntes;

import model.Runner;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.runningchallenge.app.AppController;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import contentprovider.DatabaseHelper;

public class MainActivity extends FragmentActivity {
	public static final String Age = "ageKey";
	private UiLifecycleHelper uiHelper;
	private static final String TWITTER_KEY = "aTRXhmfxCbDC91XKUDBZC0xuf";
	private static final String TWITTER_SECRET = "xYbK0bX9NRkm4EqPGT2rfexvuOzx9HTO9YRGfht0dMbO4M7CxN";
	private TwitterLoginButton loginButton;
	private boolean login = false;

	public static final String Taille = "tailleKey";
	public static final String Poid = "poidKey";
	boolean isFacebook = true;
	boolean exist;
	boolean twitterConfig = false;
	private ProgressDialog pDialog;
	String uri;
	String facebookId;
	String twitterID;
	Intent intentVeryf;
	LoginButton authButton;
	TwitterSession sessionTwitter;
	// url verify exist runner
	private String urlExist = Constatntes.BASE_URL
			+ "Runners?externalID=%1$s&externalProvider=%2$s";
	public static final String PREFERENCES = "Prefs";
	SharedPreferences sharedpreferences;
	private static final String TAG = "MainFragment";
	Runner runner;
	DatabaseHelper helper = new DatabaseHelper(this);
	WebService webService = new WebService();
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,
				TWITTER_SECRET);
		Fabric.with(this, new Twitter(authConfig));

		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		sharedpreferences = this.getSharedPreferences(PREFERENCES,
				Context.MODE_PRIVATE);
		intentVeryf = new Intent(this, WebService.class);

		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Please wait...");
		pDialog.setCancelable(false);

		try {
			PackageInfo info = getPackageManager().getPackageInfo(
					"com.example.runningchallenge",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.d("KeyHash:",
						Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

		setContentView(R.layout.activity_main);
		authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("user_location",
				"user_birthday", "user_likes"));
		loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

		if (sharedpreferences.contains("Twitter")) {
			authButton.setVisibility(View.INVISIBLE);
		}
		
		if (sharedpreferences.contains("Facebook")) {
			RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			rel_btn.bottomMargin = 200;
//			authButton.setLayoutParams(rel_btn);
			
			loginButton.setVisibility(View.INVISIBLE);

		}
		if (isNetworkAvailable()) {
			loginButton.setCallback(new Callback<TwitterSession>() {
				@Override
				public void success(Result<TwitterSession> result) {
					// Do something with result, which provides a TwitterSession
					// for
					// making API calls
					login = true;
					sessionTwitter = Twitter.getSessionManager()
							.getActiveSession();

					TwitterAuthToken authToken = sessionTwitter.getAuthToken();
					String token = authToken.token;
					String secret = authToken.secret;
					if (first_time_check()) {
						runner = new Runner();
						runner.setFirstName(sessionTwitter.getUserName());
						runner.setTwID(String.valueOf(sessionTwitter
								.getUserId()));
						helper.insertRunner(runner);
						Toast.makeText(
								getApplicationContext(),
								"Twitter login, redirecting to configuration....",
								Toast.LENGTH_LONG).show();

						if (first_time_check()) {
							twitterConfig = true;
						}
						Editor editor = sharedpreferences.edit();
						editor.putString("Twitter", "ok");
						editor.commit();
					}

				}

				@Override
				public void failure(TwitterException exception) {
					// Do something on failure
				}
			});
		} else {
			buildAlertMessageNoInternet();
		}

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("CommitPrefEdits")
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		session = Session.getActiveSession();

		if (state.isOpened()) {
			makeRequest(session);
			Log.i(TAG, "Logged in...");
			Editor editor = sharedpreferences.edit();
			editor.putString("Facebook", "ok");
			editor.commit();
			if (first_time_check()) {
				Request.executeMeRequestAsync(session, new GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						runner = new Runner();

						runner.setFirstName(user.getFirstName());
						runner.setLastName(user.getLastName());
						runner.setFbID(user.getId());
						if (user.getProperty("gender").equals("male")) {
							runner.setMale(true);
						} else {
							runner.setMale(false);
						}
						runner.setBirth(user.getBirthday());
						helper.insertRunner(runner);
					}
				});
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				intentVeryf.putExtra("verify", "facebook");
				startActivity(intentVeryf);

			} else {
				Intent intentMap = new Intent(this, MapActivity.class);
				startActivity(intentMap);
			}

		} else if (state.isClosed()) {
			finish();
			// Editor editor = preferences.edit();
			// editor.commit();
			Log.i(TAG, "Logged out...");
		}
	}

	private boolean first_time_check() {
		/*
		 * Checking Shared Preferences if the user had pressed the remember me
		 * button last time he logged in
		 */
		final boolean first;
		if (sharedpreferences.contains(Taille)) {
			first = false;

		} else {
			first = true;
		}
		return first;
	}

	@Override
	public void onResume() {
		super.onResume();
		// For scenarios where the main activity is launched and user
		// session is not null, the session state change notification
		// may not be triggered. Trigger it if it's open/closed.
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		if (sharedpreferences.contains("Twitter")) {
			authButton.setVisibility(View.INVISIBLE);
		}
		if (sharedpreferences.contains("Facebook")) {
			loginButton.setVisibility(View.INVISIBLE);
		}

	}

	private void makeRequest(final Session session) {

		Request request = Request.newMeRequest(session,
				new GraphUserCallback() {

					@SuppressLint("CommitPrefEdits")
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						facebookId = user.getId();

						if (session == Session.getActiveSession()) {
							if (user != null) {
								facebookId = user.getId();

							}
						}
						if (response.getError() != null) {
							// Handle error
						}
					}
				});
		request.executeAsync();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loginButton.onActivityResult(requestCode, resultCode, data);
		if (login && twitterConfig) {
			intentVeryf.putExtra("verify", "twitter");
			startActivity(intentVeryf);

		} else if (login) {
			Intent intentMap = new Intent(getApplicationContext(),
					MapActivity.class);
			startActivity(intentMap);
		}
		uiHelper.onActivityResult(requestCode, resultCode, data);
		if (sharedpreferences.contains("Twitter")) {
			authButton.setVisibility(View.INVISIBLE);
		}
		if (sharedpreferences.contains("Facebook")) {
			RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

//			rel_btn.bottomMargin = 200;
			loginButton.setVisibility(View.INVISIBLE);
//			authButton.setLayoutParams(rel_btn);
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	private boolean isNetworkAvailable() {

		ConnectivityManager connectivityManager = (ConnectivityManager) MainActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	private void buildAlertMessageNoInternet() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				"Your internet Network seems to be disabled, do you want to enable it?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(
									@SuppressWarnings("unused") final DialogInterface dialog,
									@SuppressWarnings("unused") final int id) {
								startActivity(new Intent(
										android.provider.Settings.ACTION_WIFI_SETTINGS));
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

	/**
	 * verify runner existence
	 */
	public boolean exist(String ID) {
		if (isFacebook) {
			uri = String.format(urlExist, ID, "Facebook");
		} else {
			uri = String.format(urlExist, ID, "Twitter");
		}
		showpDialog();
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET, uri,
				null, new com.android.volley.Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						Log.d(TestWS.class.getSimpleName(), response.toString());
						try {
							if (response.getString("Exist").equals("true")) {
								exist = true;

							} else {
								exist = false;

							}

						} catch (JSONException e) {
							e.printStackTrace();
							Toast.makeText(getApplicationContext(), "Error: ",
									Toast.LENGTH_LONG).show();
						}
						hidepDialog();

					}
				}, new com.android.volley.Response.ErrorListener() {

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

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

	public void verifyRunnerFB() {
		if (exist) {
			Toast.makeText(getApplicationContext(), "User exist, so update",
					Toast.LENGTH_LONG).show();

			Editor editor = sharedpreferences.edit();

			String weight = webService.getExistedWeight(helper.findAllRunner()
					.get(0).getFbID());
			String height = webService.getExistedHeight(helper.findAllRunner()
					.get(0).getFbID());

			editor.putString(Poid, weight);
			editor.putString(Taille, height);

			editor.commit();

			Intent intentPref = new Intent(MainActivity.this,
					PreferencesActivity.class);

			startActivity(intentPref);
		} else {

			Toast.makeText(getApplicationContext(),
					"User does not exist so create", Toast.LENGTH_LONG).show();
			Intent intentPref = new Intent(MainActivity.this,
					PreferencesActivity.class);
			startActivity(intentPref);
		}
	}
}
