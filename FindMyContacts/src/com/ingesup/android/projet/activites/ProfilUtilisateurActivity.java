package com.ingesup.android.projet.activites;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.ingesup.android.projet.json.FormatMessageEnvoi;
import com.ingesup.android.projet.json.GestionMessage;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ProfilUtilisateurActivity extends MapActivity {

	private String _jetonSession;
	private String _login;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);

        // recuperation des valeurs correspondant au login et au jeton de session
        Intent vIntent = getIntent();
        _jetonSession = (String) vIntent.getExtras().get("jeton");
        _login = (String) vIntent.getExtras().get("login");
        
        // ajouter les controles de zoom sur la mapview
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profil_activity, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Log.d(ProfilUtilisateurActivity.class.toString(), item.toString());
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {    	
    	// creer message JSON de deconnexion
    	JSONObject vMessageDeconnexion = FormatMessageEnvoi.formatterMessageLogout(_jetonSession); 
    	
    	// envoyer ordre de deconnexion
    	GestionMessage vGestionnaireMessage = new GestionMessage();
    	vGestionnaireMessage.execute(
    			"http://192.168.0.71:8080/ab_service_mgr/api/mobile/logout",
    			vMessageDeconnexion.toString());
    	
    	try {
        	// analyse message reponse
    		JSONObject vMessageReponseDeconnexion = vGestionnaireMessage.get(10, TimeUnit.SECONDS);
    		if(vMessageReponseDeconnexion != null) {
				String vEtat = (String) vMessageReponseDeconnexion.get("status");
				boolean vEtatMessage = Boolean.parseBoolean(vEtat);
				Log.d(ProfilUtilisateurActivity.class.toString(), "status = " + vEtatMessage);
				if(vEtatMessage) {
			    	Toast.makeText(ProfilUtilisateurActivity.this, _login + " deconnecté !", Toast.LENGTH_SHORT).show();
			    	_jetonSession = null;
			    	super.onBackPressed();
				}
				else {
					Toast.makeText(ProfilUtilisateurActivity.this, "Deconnexion refusée... ", Toast.LENGTH_SHORT).show();
				}
			}
    		else {
    			Log.d(ProfilUtilisateurActivity.class.toString(), "Pas de message reponse à analyse...");
    		}
			
		} catch (InterruptedException e) {
			Log.e(ProfilUtilisateurActivity.class.toString(), "Tache d'envoi du message de deconnexion interrompue : " + e.getMessage());
		} catch (ExecutionException e) {
			Log.e(ProfilUtilisateurActivity.class.toString(), "Impossible de recuperer le message reponse JSON : " + e.getMessage());
		} catch (TimeoutException e) {
			Log.e(ProfilUtilisateurActivity.class.toString(), "Timeout atteint...");
		} catch (JSONException e) {
			Log.e(ProfilUtilisateurActivity.class.toString(), "Impossible d'analyser la reponse de la demande de deconnexion : " + e.getMessage());
		}
    	
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
