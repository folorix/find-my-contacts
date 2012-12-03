package com.ingesup.android.projet.activites;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.ingesup.android.projet.json.FormatMessageEnvoi;
import com.ingesup.android.projet.json.GestionMessage;

import android.net.Uri;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ProfilUtilisateurActivity extends MapActivity {

	private String _jetonSession;
	private String _login;
	
	private MyLocationOverlay _maPosition = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil_layout);

        // recuperation des valeurs correspondant au login et au jeton de session
        Intent vIntent = getIntent();
        _jetonSession = (String) vIntent.getExtras().get("jeton");
        _login = (String) vIntent.getExtras().get("login");
        
        // parametrer l'actionbar
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        
        // ajouter les controles de zoom sur la mapview
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        // affichage de la position de l'utilisateur
        _maPosition = new MyLocationOverlay(getApplicationContext(), mapView);
        mapView.getOverlays().add(_maPosition);
        _maPosition.enableMyLocation();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profil, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
    		case R.id.menu_ajouter : 
    								AlertDialog.Builder builder = new AlertDialog.Builder(this);
						            builder.setTitle(R.string.titre_boite_dialogue_ajout_contacts);
						            builder.setItems(R.array.options_ajout_contacts, new DialogInterface.OnClickListener() {
						                   public void onClick(DialogInterface dialog, int which) {
						                	   switch (which) {
											case 0: {
														Intent vIntent = new Intent(ProfilUtilisateurActivity.this, AjoutNouvelUtilisateurActivity.class);
														startActivity(vIntent);
													}
													break;
											case 1: {
														Intent vIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("content://contacts/people/"));
														startActivity(vIntent);
													}
													break;	
											default: Log.e(ProfilUtilisateurActivity.class.toString(), "Index de l'item selectionne dans la boite de dialogue inconnu : " + which);
												break;
											}
						                   }
						            });
						            builder.create().show();
    								break;
	    	case R.id.menu_rapport : Toast.makeText(this, item.toString() + " sélectionné", Toast.LENGTH_SHORT).show();
	    							 break;
	    							 
	    	case android.R.id.home : case R.id.menu_deconnexion : onBackPressed(); break;
	    	
	    	case R.id.menu_rechercher_contacts: Intent vIntent = new Intent(ProfilUtilisateurActivity.this, RechercheContactsActivity.class);
												startActivity(vIntent);
												break;
												
	    	default : Log.e(ProfilUtilisateurActivity.class.toString(), "Menu inconnu : " + item.getItemId());
    	}

    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
    	// Issue #1 (FDA) : Ajout de la Popup de confirmation avant déconnexion de l'utilisateur
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.deconnexion);
        builder.setPositiveButton(R.string.oui, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
		    	deconnexion();
			}
		});
        builder.setNegativeButton(R.string.non, new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        builder.create().show();
    }

	private void deconnexion() {
		// creer message JSON de deconnexion
    	JSONObject vMessageDeconnexion = FormatMessageEnvoi.formatterMessageLogout(_jetonSession); 
    	
    	// envoyer ordre de deconnexion
    	GestionMessage vGestionnaireMessage = new GestionMessage();
    	vGestionnaireMessage.execute(
//				"http://192.168.0.71:8080/ab_service_mgr/api/mobile/logout",	// home
//				"http://10.10.160.230:8080/ab_service_mgr/api/mobile/logout",	// school
				"http://10.68.218.19:8080/ab_service_mgr/api/mobile/logout",	// work
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
	
	@Override
	protected void onResume() {
	    super.onResume();
	    // quand l'activite est réouverte, on rafraichit la position
	    _maPosition.enableMyLocation();
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    // quand l'activite est en pause on desactive la recherche de position
	    _maPosition.disableMyLocation();
	}

}
