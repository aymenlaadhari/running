package com.example.runningchallenge;

import model.Runner;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import contentprovider.DatabaseHelper;

public class PreferencesActivity extends ActionBarActivity {
	private TextView age;
	private TextView taille;
	private TextView poid;
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
		final TextView submit  =(TextView)findViewById(R.id.textView4);
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

		 age.setTypeface(type);
		 taille.setTypeface(type);
		 poid.setTypeface(type);
		sharedpreferences = getSharedPreferences(PREFERENCES,
				Context.MODE_PRIVATE);
		ImageButton buttonStat = (ImageButton)findViewById(R.id.actionBarIconStat);
		buttonStat.setVisibility(View.INVISIBLE);
		ImageButton buttonClass = (ImageButton)findViewById(R.id.actionBarIconClassment);
		buttonClass.setVisibility(View.INVISIBLE);

		checkFemme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (homme) {
					checkedFemme.setVisibility(View.VISIBLE);
					checkedHomme.setVisibility(View.GONE);
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
				checkedFemme.setVisibility(View.GONE);
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
					checkedFemme.setVisibility(View.GONE);
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

		if (sharedpreferences.contains(Age)) {
			age.setText(sharedpreferences.getString(Age, ""));

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
				// TODO Auto-generated method stub
				AlertDialog.Builder alert = new AlertDialog.Builder(v
						.getContext());
				final EditText inputAge = new EditText(getApplicationContext());
				inputAge.setText(age.getText().toString());
				inputAge.setInputType(InputType.TYPE_CLASS_NUMBER);
				inputAge.setTextColor(Color.parseColor("#ff5d00"));
				inputAge.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						2) });
				alert.setView(inputAge);

				alert.setTitle("Age")
						.setMessage("Veuillez saisir votre age")
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
										age.setText(inputAge.getText()
												.toString());

									}
								})

						.show();

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

				alert.setTitle("Taille(cm)")
						.setMessage("Veuillez saisir votre taille")
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
				inputPoid.setInputType(InputType.TYPE_CLASS_NUMBER);
				inputPoid.setTextColor(Color.parseColor("#ff5d00"));
				inputPoid
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
								3) });
				alert.setView(inputPoid);

				alert.setTitle("Poid(kg)")
						.setMessage("Veuillez saisir votre poid")
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
				if(checkNotEmpty())
				{
				run(v);
				Intent intentMap = new Intent(PreferencesActivity.this,
						MapActivity.class);
				intentMap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentMap);
				finish();
				}else {
					AlertDialog.Builder builder1 = new AlertDialog.Builder(PreferencesActivity.this);
					builder1.setMessage("Please fill all requirement informations");
					builder1.setCancelable(true);
					builder1.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							});

					AlertDialog alert11 = builder1.create();
					alert11.show();
				}
				
				

			}
		});
	}

	public boolean checkNotEmpty()
	{
		if(age.getText().toString().isEmpty()&& taille.getText().toString().isEmpty()&& poid.getText().toString().isEmpty())
		{
			return false;
		}
		else {
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
		Runner runner = (Runner) helper.findAllRunner().get(0);
		runner.setHeigh(Integer.parseInt(taille.getText().toString()));
		runner.setWeight(Integer.parseInt(poid.getText().toString()));
		helper.updateRunner(runner);
//		helper.close();
		editor.commit();

	}

}
