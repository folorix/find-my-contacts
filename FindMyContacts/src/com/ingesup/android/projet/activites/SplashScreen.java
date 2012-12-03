package com.ingesup.android.projet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

// Issue #3 : Splash Screen
public class SplashScreen extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
            	finish();	// empeche de revenir sur l'ecran splash screen lorsqu'on clique sur BACK à l'écran de login
            	Intent vIntent = new Intent(SplashScreen.this, FindMyContactsActivity.class);
                startActivity(vIntent);
            }
        }, 2000);

    }
}