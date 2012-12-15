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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

// Issue #17 : Edition d'un contact
public class MAJContactActivity extends Activity {
	private int _idContact;
	private String _adresseServeur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouveau_contact_layout);
		setTitle("Editer un contact");
		
		// recuperation des valeurs de l'intent
		Intent vIntent = getIntent();
		_idContact = (Integer) vIntent.getExtras().get("id");
		_adresseServeur = (String) vIntent.getExtras().get("serveur");
		
        // recuperation de l'utilisateur
		final TextView vTfNom  = (TextView) findViewById(R.id.tfNom);
		final TextView vTfPrenom = (TextView) findViewById(R.id.tfPrenom);
		final TextView vTfDateNaissance = (TextView) findViewById(R.id.tfDateNaissance);
		final TextView vTfNumRue = (TextView) findViewById(R.id.tfNumRue);
		final TextView vTfNomRue = (TextView) findViewById(R.id.tfNomRue);
		final TextView vTfCodePostal = (TextView) findViewById(R.id.tfCodePostal);
		final TextView vTfVille = (TextView) findViewById(R.id.tfVille);
		final TextView vTfPays = (TextView) findViewById(R.id.tfPays);
		final TextView vTfTel = (TextView) findViewById(R.id.tfTelephone);
		final TextView vTfEmail = (TextView) findViewById(R.id.tfEmail);
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

		Button vBoutonValider = (Button) findViewById(R.id.btnValiderContact);
		vBoutonValider.setText("Modifier");
		
        JSONObject vMessageDemandeRecuperationUtilisateur = 
        		FormatMessageEnvoi.formatterMessageRecuperationUtilisateur(_idContact);
        
        GestionMessage vGestionnaireMessage = new GestionMessage();
        vGestionnaireMessage.execute(
        		"http://" + _adresseServeur + "/ab_service_mgr/api/mobile/user/" + _idContact,
        		vMessageDemandeRecuperationUtilisateur.toString());
        
        try {
        	JSONObject vMessageReponse = vGestionnaireMessage.get(10, TimeUnit.SECONDS);
        	if(vMessageReponse != null) {
        		
                // mise a jour des champs de la vue
        		if(!vMessageReponse.isNull("nom"))
        			vTfNom.setText((String)vMessageReponse.get("nom"));
        		if(!vMessageReponse.isNull("prenom"))
        			vTfPrenom.setText((String) vMessageReponse.get("prenom"));
        		if(!vMessageReponse.isNull("dateNaissance"))
        			vTfDateNaissance.setText((String) vMessageReponse.get("dateNaissance"));        		
        		if(!vMessageReponse.isNull("numeroRue")) {
        			String vNumRue = (String) vMessageReponse.get("numeroRue");
        			if(Integer.parseInt(vNumRue) > 0)
        				vTfNumRue.setText(vNumRue);
        			else
        				vTfNumRue.setText("");
        		}
        		if(!vMessageReponse.isNull("nomRue"))
        			vTfNomRue.setText((String) vMessageReponse.get("nomRue"));
        		if(!vMessageReponse.isNull("codePostal"))
        			vTfCodePostal.setText((String) vMessageReponse.get("codePostal"));
        		if(!vMessageReponse.isNull("ville"))
        			vTfVille.setText((String) vMessageReponse.get("ville"));
        		if(!vMessageReponse.isNull("pays"))
        			vTfPays.setText((String) vMessageReponse.get("pays"));
        		
        		if(!vMessageReponse.isNull("tel"))
        			vTfTel.setText((String) vMessageReponse.get("tel"));
        		if(!vMessageReponse.isNull("email"))
        			vTfEmail.setText((String) vMessageReponse.get("email"));
        		
        		vTogBtnGeoloc.setChecked(true);
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
        
		
		// onClick sur bouton creer : mettre a jour contact
		Button vBoutonCreerContact = (Button) findViewById(R.id.btnValiderContact);
		vBoutonCreerContact.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// Mise a jour de du contact
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

					JSONObject vMessageAjoutContact = FormatMessageEnvoi.formatterMessageMajUtilisateur(vContact, _idContact);

					// Envoi du message en tache de fond
					GestionMessage vGestionnaireMessage = new GestionMessage();
					vGestionnaireMessage.execute(
							"http://" + _adresseServeur + "/ab_service_mgr/api/mobile/updateUser", 
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
							Toast.makeText(MAJContactActivity.this, "Contact mis à jour avec succès !", Toast.LENGTH_SHORT).show();
							int vIdentifiant = Integer.parseInt((String) vReponseAjoutContact.get("identifiant"));
							Intent vIntent = new Intent(MAJContactActivity.this, ProfilContactActivity.class);
							vIntent.putExtra("serveur", _adresseServeur);
							vIntent.putExtra("id", vIdentifiant);
							vIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
							vIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(vIntent);
						}
						else if(vValeurCodeErreur == 200) {
							vMessageErreur.setText("Impossible de mettre à jour le contact : erreur de saisie ou contact existant");
						}
					}
				} catch (InterruptedException e) {
					Log.e(AjoutNouveauContactActivity.class.toString(), "Tache de mise à jour d'un contact interrompue : " + e.getMessage()); 
				} catch (ExecutionException e) {
					Log.e(AjoutNouveauContactActivity.class.toString(), "Impossible de récupérer le message réponse : " + e.getMessage()); 
				} catch (JSONException e) {
					Log.e(AjoutNouveauContactActivity.class.toString(), "Impossible de recuperer un des attributs du message reponse : " + e.getMessage());
				} catch (TimeoutException e) {
					Log.e(AjoutNouveauContactActivity.class.toString(), "Message réponse non recu : " + e.getMessage());
				}
			}
		});

	}
}
