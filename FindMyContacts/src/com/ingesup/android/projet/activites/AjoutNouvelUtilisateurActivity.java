package com.ingesup.android.projet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class AjoutNouvelUtilisateurActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.nouvel_utilisateur_layout);
		
		final TextView vTfAdresse = (TextView) findViewById(R.id.tfAdresse);
		final TextView vTfCodePostal = (TextView) findViewById(R.id.tfCodePostal);
		final TextView vTfVille = (TextView) findViewById(R.id.tfVille);
		final TextView vTfPays = (TextView) findViewById(R.id.tfPays);
		ToggleButton vTogBtnGeoloc = (ToggleButton) findViewById(R.id.togBtnGeolocalisation);
		
		vTogBtnGeoloc.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					vTfAdresse.setVisibility(View.VISIBLE);
					vTfCodePostal.setVisibility(View.VISIBLE);
					vTfVille.setVisibility(View.VISIBLE);
					vTfPays.setVisibility(View.VISIBLE);
				}
				else {
					vTfAdresse.setVisibility(View.GONE);
					vTfCodePostal.setVisibility(View.GONE);
					vTfVille.setVisibility(View.GONE);
					vTfPays.setVisibility(View.GONE);
				}
			}
		});
		
		Button vBoutonCreerContact = (Button) findViewById(R.id.btnValiderContact);
		vBoutonCreerContact.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent vIntent = new Intent(AjoutNouvelUtilisateurActivity.this, ProfilContactActivity.class);
				startActivity(vIntent);
			}
		});
	}
}
