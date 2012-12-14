package com.ingesup.android.projet.activites;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

// Issue #18 (FDA) : Gestion des préférences de l'application
public class PreferencesActivity extends PreferenceActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public void onBackPressed() {

		SharedPreferences vPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		// sauvegarder la valeur du serveur
		Editor vEditeur = vPreferences.edit();
		vEditeur.putString("SERVEUR", vPreferences.getString("serveur", ""));
		vEditeur.putString("USER_MAIL_ADDRESS", vPreferences.getString("user_mail_address", ""));
		vEditeur.putString("USER_MAIL_PASSWORD", vPreferences.getString("user_mail_password", ""));
		vEditeur.commit();
		
		super.onBackPressed();
	}
}
