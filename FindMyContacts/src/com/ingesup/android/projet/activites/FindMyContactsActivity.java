package com.ingesup.android.projet.activites;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import com.ingesup.android.projet.json.AnalyseurMessageReception;
import com.ingesup.android.projet.json.FormatMessageEnvoi;
import com.ingesup.android.projet.json.GestionMessage;
import com.ingesup.android.projet.reponses.ReponseAuthentification;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class FindMyContactsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        
        Button vBoutonValider = (Button) findViewById(R.id.btnValiderLogin);
        vBoutonValider.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				// recuperation des valeurs des champs texte
				TextView vChampLogin =  (TextView) findViewById(R.id.champLogin);
				TextView vChampMotDePasse = (TextView) findViewById(R.id.champMotDePasse);
				TextView vTexteMessageErreur = (TextView) findViewById(R.id.texteErreurLogin);
				vTexteMessageErreur.setText("");
				
				if(isNetworkAvailable()) {
					// Creation du message JSON pour l'authentification
					String vLogin = vChampLogin.getText().toString();
					
					JSONObject vMessageJSON = FormatMessageEnvoi
							.formatterMessageLogin(
									vLogin,
									vChampMotDePasse.getText().toString());
					
					// Envoi du message en tache de fond
					GestionMessage vGestionnaireMessage = new GestionMessage();
					vGestionnaireMessage.execute(
//							"http://192.168.0.71:8080/ab_service_mgr/api/mobile/login",	// home
							"http://10.10.160.230:8080/ab_service_mgr/api/mobile/login",	// school
							vMessageJSON.toString());
					
					// Lecture du message reponse
					try {						
						JSONObject vMessageReponse = vGestionnaireMessage.get(10, TimeUnit.SECONDS);	// attente du message reponse
						
						if(vMessageReponse != null) {
							ReponseAuthentification vMessageReponseAuthentification = 
									AnalyseurMessageReception.analyserReponseAuthentification(vMessageReponse);
							
							if((vMessageReponseAuthentification != null) && (vMessageReponseAuthentification.estAuthentifie())) {
								Toast.makeText(FindMyContactsActivity.this, vLogin + " connecté !", Toast.LENGTH_SHORT).show();
								Intent vIntent = new Intent(FindMyContactsActivity.this, ProfilUtilisateurActivity.class);
								vIntent.putExtra("login", vLogin);
								vIntent.putExtra("jeton", vMessageReponseAuthentification.getJetonSession());
								startActivity(vIntent);
							}
							else {
								vTexteMessageErreur.setText("Login/Mot de passe incorrect");
							}
						}
						else {
							Log.e(FindMyContactsActivity.class.toString(), "Pas de message reponse retourné.");
						}
						
					} catch (InterruptedException e) {
						Log.e(FindMyContactsActivity.class.toString(), "Tache d'envoi de message de connexion interrompue : " + e.getMessage());
					} catch (ExecutionException e) {
						Log.e(FindMyContactsActivity.class.toString(), "Impossible de recuperer le message reponse JSON : " + e.getMessage());
					} catch (TimeoutException e) {
						Log.e(FindMyContactsActivity.class.toString(), "Timeout atteint...");
						vTexteMessageErreur.setText("Serveur indisponible");						
					}
				}
				else {
					Toast.makeText(FindMyContactsActivity.this, "Vérifier votre connexion reseau", Toast.LENGTH_LONG).show();
					Log.d(FindMyContactsActivity.class.toString(), "Vérifier votre connexion reseau");
				}
			}
		});
    }

    // Cette methode verifie l'etat de la connexion reseau
    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        
        return false;
    } 
    
}
