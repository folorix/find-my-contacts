package com.ingesup.android.projet.activites;

import java.lang.reflect.Field;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.Toast;

public class ProfilContactActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        
        // Issue #14 (FDA) : Force l'apparition de l'Overflow menu 
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }
        
        ActionBar vActionBar = getActionBar();
        vActionBar.setDisplayShowTitleEnabled(false);
        vActionBar.setDisplayShowHomeEnabled(false);
        
        // ajouter les controles de zoom sur la mapview
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String vItemSelectionne = item.toString();
    	Log.d(ProfilContactActivity.class.toString(), vItemSelectionne);
    	Toast.makeText(this, item.toString() + " sélectionné", Toast.LENGTH_SHORT).show();
    	return super.onOptionsItemSelected(item);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onBackPressed() {
		// #Issue 15 (FDA) : Forçage du retour au profil utilisateur et non à l'écran de création d'un nouvel utilisateur
		Intent intent = new Intent(ProfilContactActivity.this, ProfilUtilisateurActivity .class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
}
