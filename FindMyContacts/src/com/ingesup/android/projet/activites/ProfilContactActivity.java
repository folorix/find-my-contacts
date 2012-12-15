package com.ingesup.android.projet.activites;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.ingesup.android.projet.contact.Contact;
import com.ingesup.android.projet.json.FormatMessageEnvoi;
import com.ingesup.android.projet.json.GestionMessage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// Issue #12 : Affichage des informations du contact
public class ProfilContactActivity extends MapActivity {

    private Integer _idContact;
	private String _adresseServeur;
	private Contact _contact;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        setTitle("Profil du contact");
        
        // récupération des informations de création d'un nouveau contact
        Intent vIntent = getIntent();
        _idContact = (Integer) vIntent.getExtras().get("id");
        _adresseServeur = vIntent.getExtras().getString("serveur");
        
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

        // parametrer l'actionbar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        // ajouter les controles de zoom sur la mapview
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        // ajout des actions sur les boutons "Modifier photo profil" et "Editer profil"
        Button vBoutonModificationPhoto = (Button) findViewById(R.id.btnModifPhoto);
        vBoutonModificationPhoto.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(ProfilContactActivity.this, "Fonction non implementée", Toast.LENGTH_SHORT).show();
			}
		});
        
        Button vBoutonEditionProfil = (Button) findViewById(R.id.btnEditProfil);
        vBoutonEditionProfil.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent vIntent = new Intent(ProfilContactActivity.this, MAJContactActivity.class);
				vIntent.putExtra("id", _idContact);
				vIntent.putExtra("serveur", _adresseServeur);
				startActivity(vIntent);
			}
		});
        
        // recuperation de l'utilisateur 
        JSONObject vMessageDemandeRecuperationUtilisateur = 
        		FormatMessageEnvoi.formatterMessageRecuperationUtilisateur(_idContact);
        
        GestionMessage vGestionnaireMessage = new GestionMessage();
        vGestionnaireMessage.execute(
        		"http://" + _adresseServeur + "/ab_service_mgr/api/mobile/user/" + _idContact,
        		vMessageDemandeRecuperationUtilisateur.toString());
        
        try {
        	JSONObject vMessageReponse = vGestionnaireMessage.get(10, TimeUnit.SECONDS);
        	if(vMessageReponse != null) {
        		_contact = new Contact();
        		
        		if(!vMessageReponse.isNull("nom"))
        			_contact.setNom((String) vMessageReponse.get("nom"));
        		if(!vMessageReponse.isNull("prenom"))
        			_contact.setPrenom((String) vMessageReponse.get("prenom"));
        		if(!vMessageReponse.isNull("numeroRue"))
        			_contact.setNumRue(Integer.parseInt((String) vMessageReponse.get("numeroRue")));
        		if(!vMessageReponse.isNull("nomRue"))
        			_contact.setNomRue((String) vMessageReponse.get("nomRue"));
        		if(!vMessageReponse.isNull("codePostal"))
        			_contact.setCodePostal((String) vMessageReponse.get("codePostal"));
        		if(!vMessageReponse.isNull("ville"))
        			_contact.setVille((String) vMessageReponse.get("ville"));
        		if(!vMessageReponse.isNull("pays"))
        			_contact.setVille((String) vMessageReponse.get("pays"));
        		if(!vMessageReponse.isNull("tel"))
        			_contact.setNumTelephone((String) vMessageReponse.get("tel"));
        		if(!vMessageReponse.isNull("email"))
        			_contact.setEmail((String) vMessageReponse.get("email"));
        		if(!vMessageReponse.isNull("latitude"))
        			_contact.setLatitude(Double.parseDouble((String) vMessageReponse.get("latitude")));
        		if(!vMessageReponse.isNull("longitude"))
        			_contact.setLongitude(Double.parseDouble((String) vMessageReponse.get("longitude")));	
        		
        		// mise a jour de l'interface du contact
                TextView vTvNomPrenom = (TextView) findViewById(R.id.tvNomPrenom);
                TextView vTvAdresse = (TextView) findViewById(R.id.tvAdresse);
                
                vTvNomPrenom.setText(_contact.getNom() + " " + _contact.getPrenom());
                int vNumRue = _contact.getNumRue();
                vTvAdresse.setText((vNumRue>0?vNumRue+", ":"") 
        			        		+ _contact.getNomRue() + " " 
        			        		+ _contact.getCodePostal() + " " 
        			        		+ _contact.getVille() + " " 
        			        		+ _contact.getPays());
        	}
        	else {
        		Log.e(ProfilContactActivity.class.toString(), "Message reponse vide");
        	}
		} catch (InterruptedException e) {
			Log.e(ProfilContactActivity.class.toString(), "Tache de recuperation d'un contact interrompue : " + e.getMessage()); 
		} catch (ExecutionException e) {
			Log.e(ProfilContactActivity.class.toString(), "Impossible de récupérer le message réponse : " + e.getMessage()); 
		} catch (TimeoutException e) {
			Log.e(ProfilContactActivity.class.toString(), "Message réponse non recu : " + e.getMessage());
		} catch (JSONException e) {
			Log.e(ProfilContactActivity.class.toString(), "Impossible de recuperer la valeur d'un des attributs : " + e.getMessage());
		}
        
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	String vNumTelContact = _contact.getNumTelephone();
		String vEmailContact = _contact.getEmail();

		switch(item.getItemId()) {
	    	case R.id.menu_appeler : {	        	
	        	if(!vNumTelContact.equals("")) {
		        	Intent vIntent = new Intent(Intent.ACTION_DIAL);
		    		vIntent.setData(Uri.parse("tel:" + vNumTelContact));
		    		startActivity(vIntent);
	        	}
	        	else {
	        		Toast.makeText(ProfilContactActivity.this, "Pas de numéro de téléphone pour ce contact", Toast.LENGTH_SHORT).show();
	        	}
	        	
	    		break;
	    	}
	    	case R.id.menu_mail : {
	    		if(!vEmailContact.equals("")) {
		    		Intent vIntent = new Intent(Intent.ACTION_SENDTO);
					vIntent.setData(Uri.parse("mailto:" + vEmailContact));
		    		startActivity(vIntent);
	    		}
	    		else {
	        		Toast.makeText(ProfilContactActivity.this, "Pas d'email pour ce contact", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		break;
	    	}
	    	
	    	case R.id.menu_sms : {
	    		if(!vNumTelContact.equals("")) {
		    		Intent vIntent = new Intent(Intent.ACTION_SENDTO);
		    		vIntent.setData(Uri.parse("sms:" + vNumTelContact));
		    		startActivity(vIntent);
	    		}
	    		else {
	        		Toast.makeText(ProfilContactActivity.this, "Pas de numéro de téléphone pour ce contact", Toast.LENGTH_SHORT).show();
	    		}
	    		
	    		break;
	    	}
	    	
	    	case R.id.menu_imprimer_profil : {
	    		// TODO : implementer impression profil
				Toast.makeText(ProfilContactActivity.this, "Fonction non implementée", Toast.LENGTH_SHORT).show();
	    		break;
	    	}
	    	
	    	case R.id.menu_profil_bluetooth : {
	    		// TODO : implementer envoi du profil par bluetooth
				Toast.makeText(ProfilContactActivity.this, "Fonction non implementée", Toast.LENGTH_SHORT).show();
	    		break;
	    	}
	    	
	    	case R.id.menu_supprimer_profil : {
	    		// Issue #20 : Suppression du profil
	        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	            builder.setTitle(R.string.suppression_profil);
	            builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						supprimerContact();
					}
				});
	            builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
	            builder.create().show();

				break;
	    	}
	    	
	    	case R.id.menu_ajouter_a_mes_contacts : {
	    		// TODO : implementer ecriture contacts dans carnet d'adresse du telephone
				Toast.makeText(ProfilContactActivity.this, "Fonction non implementée", Toast.LENGTH_SHORT).show();
	    		break;
	    	}

	    	case android.R.id.home : 
	    		onBackPressed(); break;
	    		
	    	default: Toast.makeText(ProfilContactActivity.this, 
	    			"Menu inconnu : " + item.getItemId() + "-" + item.toString(), 
	    			Toast.LENGTH_SHORT).show();
    	}
    	
    	return super.onOptionsItemSelected(item);
    }

	// Issue #20 : Suppression du profil
	protected void supprimerContact() {
    	JSONObject vMessageDemandeSuppressionContact = FormatMessageEnvoi.formatterMessageSuppressionUtilisateur();
    	GestionMessage vGestionnaireMessage = new GestionMessage();
    	vGestionnaireMessage.execute(
    			"http://" + _adresseServeur + "/ab_service_mgr/api/mobile/delete/" + _idContact,
    			vMessageDemandeSuppressionContact.toString());
    	
    	try {
    		String vEtat = null;
    		String vCodeErreur = null;
			JSONObject vMessageReponseSuppressionContact = vGestionnaireMessage.get(10, TimeUnit.SECONDS);
			if(!vMessageReponseSuppressionContact.isNull("status"))
				vEtat = (String) vMessageReponseSuppressionContact.get("status");
			if(!vMessageReponseSuppressionContact.isNull("errorCode"))
				vCodeErreur = (String) vMessageReponseSuppressionContact.get("errorCode");
			
			boolean vEtatMessage = vEtat!=null?Boolean.parseBoolean(vEtat):false;
			int vValeurCodeErreur = vCodeErreur!=null?Integer.parseInt(vCodeErreur):0;
			
			if(vEtatMessage) {
				Toast.makeText(ProfilContactActivity.this, "Contact " + _contact.getNom() + " " + _contact.getPrenom() + " supprimé avec succès !", Toast.LENGTH_SHORT).show();
				onBackPressed();
			}
			else if(vValeurCodeErreur == 200) {
				Toast.makeText(ProfilContactActivity.this, 
						"Impossible de de supprimer le contact : " + vMessageReponseSuppressionContact.get("errorMsg"),
						Toast.LENGTH_LONG).show();
			}
			
		} catch (InterruptedException e) {
			Log.e(AjoutNouveauContactActivity.class.toString(), "Tache de suppression d'un contact interrompue : " + e.getMessage()); 
		} catch (ExecutionException e) {
			Log.e(AjoutNouveauContactActivity.class.toString(), "Impossible de récupérer le message réponse : " + e.getMessage()); 
		} catch (JSONException e) {
			Log.e(AjoutNouveauContactActivity.class.toString(), "Impossible de recuperer un des attributs du message reponse : " + e.getMessage());
		} catch (TimeoutException e) {
			Log.e(AjoutNouveauContactActivity.class.toString(), "Message retour non recu : " + e.getMessage());
		}
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
