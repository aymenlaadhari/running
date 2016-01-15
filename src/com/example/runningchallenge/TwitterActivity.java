package com.example.runningchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class TwitterActivity extends Fragment {
	private static final String TWITTER_KEY = "aTRXhmfxCbDC91XKUDBZC0xuf";
	private static final String TWITTER_SECRET = "xYbK0bX9NRkm4EqPGT2rfexvuOzx9HTO9YRGfht0dMbO4M7CxN";
	private TwitterLoginButton loginButton;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY,
				TWITTER_SECRET);
		Fabric.with(getActivity(), new Twitter(authConfig));
		View view = inflater.inflate(R.layout.activity_main, container, false);
		loginButton = (TwitterLoginButton) view
				.findViewById(R.id.twitter_login_button);
		loginButton.setCallback(new Callback<TwitterSession>() {
			@Override
			public void success(Result<TwitterSession> result) {
				// Do something with result, which provides a TwitterSession for
				// making API calls
			}

			@Override
			public void failure(TwitterException exception) {
				// Do something on failure
			}
		});
		return view;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Pass the activity result to the login button.
		loginButton.onActivityResult(requestCode, resultCode, data);
	}

}
