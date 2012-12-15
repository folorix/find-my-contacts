package com.ingesup.android.projet.activites;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import org.json.JSONObject;

import com.ingesup.android.projet.contact.Contact;
import com.ingesup.android.projet.json.FormatMessageEnvoi;
import com.ingesup.android.projet.json.GestionMessage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class AjoutNouveauContactActivity extends Activity {
	
	private String _adresseServeur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.nouveau_contact_layout);
		setTitle("Créer un contact");

		Intent vIntent = getIntent();
        _adresseServeur = (String) vIntent.getExtras().get("serveur");

        // parametrer l'actionbar
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
		final TextView vTfNumRue = (TextView) findViewById(R.id.tfNumRue);
		final TextView vTfNomRue = (TextView) findViewById(R.id.tfNomRue);
		final TextView vTfCodePostal = (TextView) findViewById(R.id.tfCodePostal);
		final TextView vTfVille = (TextView) findViewById(R.id.tfVille);
		final TextView vTfPays = (TextView) findViewById(R.id.tfPays);
		ToggleButton vTogBtnGeoloc = (ToggleButton) findViewById(R.id.togBtnGeolocalisation);
		
		vTogBtnGeoloc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					vTfNumRue.setVisibility(View.VISIBLE);
					vTfNomRue.setVisibility(View.VISIBLE);
					vTfCodePostal.setVisibility(View.VISIBLE);
					vTfVille.setVisibility(View.VISIBLE);
					vTfPays.setVisibility(View.VISIBLE);
				}
				else {
					vTfNumRue.setVisibility(View.GONE);
					vTfNomRue.setVisibility(View.GONE);
					vTfCodePostal.setVisibility(View.GONE);
					vTfVille.setVisibility(View.GONE);
					vTfPays.setVisibility(View.GONE);
				}
			}
		});
		
		Button vBoutonCreerContact = (Button) findViewById(R.id.btnValiderContact);
		vBoutonCreerContact.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// Issue #8 et #10 : Ajout d'un nouvel utilisateur
				TextView vMessageErreur = (TextView) findViewById(R.id.tvErreurAjoutContact);

				try {
					Contact vContact = new Contact();
					
					vContact.setPrenom(((EditText)findViewById(R.id.tfPrenom)).getText().toString());
					vContact.setNom(((EditText)findViewById(R.id.tfNom)).getText().toString());
					vContact.setDateNaissance(((EditText)findViewById(R.id.tfDateNaissance)).getText().toString());
					String vNumRue = ((EditText)findViewById(R.id.tfNumRue)).getText().toString();
					vContact.setNumRue((((vNumRue!=null) && (!vNumRue.equals("")))?Integer.parseInt(vNumRue):0));
					vContact.setNomRue(((EditText)findViewById(R.id.tfNomRue)).getText().toString());
					vContact.setCodePostal(((EditText)findViewById(R.id.tfCodePostal)).getText().toString());
					vContact.setVille(((EditText)findViewById(R.id.tfVille)).getText().toString());
					vContact.setPays(((EditText)findViewById(R.id.tfPays)).getText().toString());
					vContact.setNumTelephone(((EditText)findViewById(R.id.tfTelephone)).getText().toString());
					vContact.setEmail(((EditText)findViewById(R.id.tfEmail)).getText().toString());
					vContact.setGeolocalisation(((ToggleButton)findViewById(R.id.togBtnGeolocalisation)).isChecked());

					JSONObject vMessageAjoutContact = FormatMessageEnvoi.formatterMessageAjoutUtilisateur(vContact);

					// Envoi du message en tache de fond
					GestionMessage vGestionnaireMessage = new GestionMessage();
					vGestionnaireMessage.execute(
							"http://" + _adresseServeur + "/ab_service_mgr/api/mobile/addUser", 
							vMessageAjoutContact.toString());

					// Recuperation du message réponse
					JSONObject vReponseAjoutContact = vGestionnaireMessage.get(10, TimeUnit.SECONDS);
					if(vReponseAjoutContact != null) {
						String vEtat = null; 
						String vCodeErreur = null;
						
						if(!vReponseAjoutContact.isNull("status"))
							vEtat = (String) vReponseAjoutContact.get("status");
						if(!vReponseAjoutContact.isNull("errorCode"))
							vCodeErreur = (String) vReponseAjoutContact.get("errorCode");
						
						boolean vEtatMessage = vEtat!=null?Boolean.parseBoolean(vEtat):false;
						int vValeurCodeErreur = vCodeErreur!=null?Integer.parseInt(vCodeErreur):0;
						
						if(vEtatMessage) {
							Toast.makeText(AjoutNouveauContactActivity.this, "Contact crée avec succès !", Toast.LENGTH_SHORT).show();
							int vIdentifiant = Integer.parseInt((String) vReponseAjoutContact.get("identifiant"));
							Intent vIntent = new Intent(AjoutNouveauContactActivity.this, ProfilContactActivity.class);
							vIntent.putExtra("serveur", _adresseServeur);
							vIntent.putExtra("id", vIdentifiant);
							startActivity(vIntent);
						}
						else if(vValeurCodeErreur == 200) {
							vMessageErreur.setText("Impossible de créer le contact : erreur de saisie ou contact existant");
						}
					}
				} catch (InterruptedException e) {
					Log.e(AjoutNouveauContactActivity.class.toString(), "Tache d'ajout d'un contact interrompue : " + e.getMessage()); 
				} catch (ExecutionException e) {
					Log.e(AjoutNouveauContactActivity.class.toString(), "Impossible de récupérer le message réponse : " + e.getMessage()); 
				} catch (JSONException e) {
					Log.e(AjoutNouveauContactActivity.class.toString(), "Impossible de recuperer un des attributs du message reponse : " + e.getMessage());
				} catch (TimeoutException e) {
					Log.e(AjoutNouveauContactActivity.class.toString(), "Message retour non recu : " + e.getMessage());
				}
			}
		});
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch(item.getItemId()) {
 			 
	    	case android.R.id.home : 
	    		onBackPressed(); break;
				
	    	default : 
	    		Log.e(AjoutNouveauContactActivity.class.toString(), "Menu inconnu : " + item.getItemId());
    	}

    	return super.onOptionsItemSelected(item);
    }
}
