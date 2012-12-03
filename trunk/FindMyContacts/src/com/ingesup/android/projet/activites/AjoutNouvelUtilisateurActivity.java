package com.ingesup.android.projet.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AjoutNouvelUtilisateurActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.nouvel_utilisateur_layout);
		
		Button vBoutonCreerContact = (Button) findViewById(R.id.btnValiderContact);
		vBoutonCreerContact.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent vIntent = new Intent(AjoutNouvelUtilisateurActivity.this, ProfilContactActivity.class);
				startActivity(vIntent);
			}
		});
	}
}
