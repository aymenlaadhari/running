package com.example.runningchallenge;

import java.util.Arrays;
import model.Runner;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import contentprovider.DatabaseHelper;

@SuppressWarnings("deprecation")
public class MainFragment extends Fragment {
	private static final String TAG = "MainFragment";
	public static final String PREFERENCES = "Prefs";
	SharedPreferences sharedpreferences;
	Runner runner;
	String facebookId;
	DatabaseHelper helper;

	public static final String Age = "ageKey";
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	private UiLifecycleHelper uiHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,

	Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_main, container, false);
		LoginButton authButton = (LoginButton) view
				.findViewById(R.id.authButton);
		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("user_location",
				"user_birthday", "user_likes"));
		authButton.setBackgroundResource(R.drawable.champtablet7);
		authButton.setText("Facebook");
		sharedpreferences = this.getActivity().getSharedPreferences(
				PREFERENCES, Context.MODE_PRIVATE);
		helper = new DatabaseHelper(getActivity());
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

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
						runner.setSexe(user.asMap().get("gender").toString());
						runner.setBirth(user.getBirthday());
						helper.insertRunner(runner);								
					}
				});
				Intent intentPref = new Intent(getActivity(),
						PreferencesActivity.class);
				
				startActivity(intentPref);
			} else {
				Intent intentMap = new Intent(getActivity(), MapActivity.class);
				startActivity(intentMap);
			}

		} else if (state.isClosed()) {
			getActivity().finish();
			// Editor editor = preferences.edit();
			// editor.commit();
			Log.i(TAG, "Logged out...");
		}
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
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
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

	public String buildUserInfoDisplay(GraphUser user) {
		StringBuilder userInfo = new StringBuilder("");

		// Example: typed access (name)
		// - no special permissions required
		userInfo.append(String.format("Name: %s\n\n", user.getName()));
		// Example: typed access (lastName)
		// - no special permissions required
		userInfo.append(String.format("LastName: %s\n\n", user.getLastName()));

		// Example: typed access (Gender)
		userInfo.append(String.format("Gender: %s\n\n",
				user.asMap().get("gender").toString()));
		// Example: typed access (FbId)
		userInfo.append(String.format("FbID: %s\n\n", user.getId()));

		// Example: typed access (birthday)
		// - requires user_birthday permission
		userInfo.append(String.format("Birthday: %s\n\n", user.getBirthday()));
		// Example: access via property name (locale)
		// - no special permissions required
		userInfo.append(String.format("Locale: %s\n\n",
				user.getProperty("locale")));

		return userInfo.toString();
	}

}
