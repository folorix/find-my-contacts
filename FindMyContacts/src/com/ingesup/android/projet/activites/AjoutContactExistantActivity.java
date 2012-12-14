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
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Data;
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
public class AjoutContactExistantActivity extends Activity {
	private int _idContact;
	private String _adresseServeur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nouveau_contact_layout);
		
		// recuperation des valeurs de l'intent
		Intent vIntent = getIntent();
		_idContact = Integer.parseInt(vIntent.getExtras().getString("id"));
		_adresseServeur = (String) vIntent.getExtras().get("serveur");
		
        // recuperation de l'utilisateur
		final TextView vTfNom  = (TextView) findViewById(R.id.tfNom);
		final TextView vTfPrenom = (TextView) findViewById(R.id.tfPrenom);
//		final TextView vTfDateNaissance = (TextView) findViewById(R.id.tfDateNaissance);
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
		vTogBtnGeoloc.setChecked(true);

		/* recuperation des informations du contact*/
		
		// recuperation du nom, prenom du contact
		Cursor cursor = getContentResolver().query(Data.CONTENT_URI,
		          new String[] {StructuredName.CONTACT_ID, StructuredName.FAMILY_NAME, StructuredName.GIVEN_NAME},
		          StructuredName.CONTACT_ID + "=?" + " AND "
		                  + StructuredName.MIMETYPE + "='" + StructuredName.CONTENT_ITEM_TYPE + "'",
		          new String[] {String.valueOf(_idContact)}, null);
		cursor.moveToFirst();
		if(cursor.getCount() > 0) {
			vTfNom.setText(cursor.getString(cursor.getColumnIndex(StructuredName.FAMILY_NAME)));
			vTfPrenom.setText(cursor.getString(cursor.getColumnIndex(StructuredName.GIVEN_NAME)));
		}
		cursor.close();
		
		// recuperation de l'adresse du contact
		cursor = getContentResolver().query(StructuredPostal.CONTENT_URI,
		          new String[] {Data.CONTACT_ID, StructuredPostal.FORMATTED_ADDRESS},
		          Data.CONTACT_ID + "=?" + " AND "
		                  + Data.MIMETYPE + "='" + StructuredPostal.CONTENT_ITEM_TYPE + "'",
		          new String[] {String.valueOf(_idContact)}, null);
		cursor.moveToFirst();
		if(cursor.getCount() > 0) {
			vTfNomRue.setText(cursor.getString(cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS)));
		}
		cursor.close();

		// recuperation du numero de telephone
		cursor = getContentResolver().query(Data.CONTENT_URI,
		          new String[] {Data._ID, Phone.NUMBER},
		          Data.CONTACT_ID + "=?" + " AND "
		                  + Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "'",
		          new String[] {String.valueOf(_idContact)}, null);
		cursor.moveToFirst();
		if(cursor.getCount() > 0)
			vTfTel.setText(cursor.getString(cursor.getColumnIndex(Phone.NUMBER)));
		cursor.close();

		// recuperation de l'email
		cursor = getContentResolver().query(Data.CONTENT_URI,
		          new String[] {Data._ID, Email.ADDRESS},
		          Data.CONTACT_ID + "=?" + " AND "
		                  + Data.MIMETYPE + "='" + Email.CONTENT_ITEM_TYPE + "'",
		          new String[] {String.valueOf(_idContact)}, null);
		cursor.moveToFirst();
		if(cursor.getCount() > 0)
			vTfEmail.setText(cursor.getString(cursor.getColumnIndex(Email.ADDRESS)));
		cursor.close();
    			
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
							Toast.makeText(AjoutContactExistantActivity.this, "Contact mis à jour avec succès !", Toast.LENGTH_SHORT).show();
							int vIdentifiant = Integer.parseInt((String) vReponseAjoutContact.get("identifiant"));
							Intent vIntent = new Intent(AjoutContactExistantActivity.this, ProfilContactActivity.class);
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
