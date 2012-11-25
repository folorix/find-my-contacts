package com.ingesup.android.projet.activites;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ImpressionRapportBirtActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.impression_rapport_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.impression_rapport_birt_activity, menu);
        return true;
    }
}
