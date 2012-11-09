package com.ingesup.android.projet;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ProfilUtilisateurActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profil_activity, menu);
        return true;
    }
}
