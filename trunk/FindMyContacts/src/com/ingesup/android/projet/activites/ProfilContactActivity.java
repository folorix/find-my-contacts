package com.ingesup.android.projet.activites;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ProfilContactActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        
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
	
}
