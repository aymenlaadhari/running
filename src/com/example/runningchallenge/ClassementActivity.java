package com.example.runningchallenge;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class ClassementActivity extends ActionBarActivity {
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
		buttonClass.getBackground().setAlpha(32); 
		buttonClass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NavUtils.navigateUpFromSameTask(ClassementActivity.this);
			}
		});

		
		ImageButton buttonStat = (ImageButton)findViewById(R.id.actionBarIconStat);
		buttonStat.setVisibility(View.GONE);
		
		TextView tx = (TextView) findViewById(R.id.textClassement);
		TextView tx1 = (TextView) findViewById(R.id.textClassementTop);
		TextView tx2 = (TextView) findViewById(R.id.gangnant3Text);
		TextView tx3 = (TextView) findViewById(R.id.gagnant2Text);
		TextView tx4 = (TextView) findViewById(R.id.gagnant1Text);
		TextView tx5 = (TextView) findViewById(R.id.textView1);
		TextView tx6 = (TextView) findViewById(R.id.textView3);
		TextView tx7 = (TextView) findViewById(R.id.textView2);

		Typeface custom_font = Typeface.createFromAsset(getAssets(),
				"fonts/berlin-sans-fb-demi-bold.ttf");
		tx.setTypeface(custom_font);
		tx1.setTypeface(custom_font);
		tx2.setTypeface(custom_font);
		tx3.setTypeface(custom_font);
		tx4.setTypeface(custom_font);
		tx5.setTypeface(custom_font);
		tx6.setTypeface(custom_font);
		tx7.setTypeface(custom_font);
	}
}
