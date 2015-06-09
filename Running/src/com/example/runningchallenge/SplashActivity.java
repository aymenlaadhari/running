package com.example.runningchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		//Programmer des runnables pour être exécuté aprés certain moment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Passage vers la deuxieme activité apres 3 secondes
//                Intent i = new Intent(SplashActivity.this,SettingActivity.class);
//                startActivity(i);
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
                
                //fermer l'activité en cours
                finish();
            }
        }, 2000);
	}
}
