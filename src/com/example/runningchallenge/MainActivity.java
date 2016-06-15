package com.example.runningchallenge;

import io.fabric.sdk.android.Fabric;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import model.Runner;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
	private boolean login;
	String facebookId;
	public static final String PREFERENCES = "Prefs";
	SharedPreferences sharedpreferences;
	private static final String TAG = "MainFragment";
	Runner runner;
	DatabaseHelper helper;
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
		login = false;
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		sharedpreferences = this.getSharedPreferences(PREFERENCES,
				Context.MODE_PRIVATE);
		helper = new DatabaseHelper(this);

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
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setReadPermissions(Arrays.asList("user_location",
				"user_birthday", "user_likes"));
		loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
		if(isNetworkAvailable()){
		loginButton.setCallback(new Callback<TwitterSession>() {
			@Override
			public void success(Result<TwitterSession> result) {
				// Do something with result, which provides a TwitterSession for
				// making API calls
				login = true;
				TwitterSession session = Twitter.getSessionManager()
						.getActiveSession();
				TwitterAuthToken authToken = session.getAuthToken();
				String token = authToken.token;
				String secret = authToken.secret;
				session.getUserId();
				session.getUserName();

			}

			@Override
			public void failure(TwitterException exception) {
				// Do something on failure
			}
		});
		}
		else {
			buildAlertMessageNoInternet();
		}

	}

	@SuppressWarnings("deprecation")
	@SuppressLint("CommitPrefEdits")
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			makeRequest(session);
			Log.i(TAG, "Logged in...");
			if (first_time_check()) {
				Request.executeMeRequestAsync(session, new GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub
						runner = new Runner();
						runner.setFirstName(user.getFirstName());
						runner.setLastName(user.getLastName());
						runner.setFbID(user.getId());
						if (user.getProperty("gender").equals("male") ) {
							runner.setMale(true);
						} else {
							runner.setMale(false);
						}
						runner.setBirth(user.getBirthday());
						helper.insertRunner(runner);
					}
				});
				Intent intentPref = new Intent(this, PreferencesActivity.class);

				startActivity(intentPref);
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
		if (sharedpreferences.contains(Age)) {
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
		// uiHelper.onResume();
	}

	private void makeRequest(final Session session) {
		Request request = Request.newMeRequest(session,
				new GraphUserCallback() {

					@SuppressLint("CommitPrefEdits")
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// TODO Auto-generated method stub

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
		if (login) {
			Intent intentMap = new Intent(getApplicationContext(),
					MapActivity.class);
			startActivity(intentMap);
		}
		uiHelper.onActivityResult(requestCode, resultCode, data);
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
										android.provider.Settings.ACTION_WIRELESS_SETTINGS));
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
}
