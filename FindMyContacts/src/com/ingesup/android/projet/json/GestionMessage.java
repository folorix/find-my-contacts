package com.ingesup.android.projet.json;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.AsyncTask;
import android.util.Log;

public class GestionMessage extends AsyncTask<String, Void, JSONObject>{
		
	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject vMessageReponseJSON = null;
		
		try {
			URL vUrlLogin = new URL(params[0]);
			JSONObject vMessageJSON = new JSONObject(params[1]);
			
			// Envoi du message JSON 
			vMessageReponseJSON = envoyerMessageJSON(vUrlLogin, vMessageJSON);
			
		} catch (MalformedURLException e) {
			Log.e(GestionMessage.class.toString(), "URL formee incorrecte : " + e.getMessage());	
		} catch (JSONException e) {
			Log.e(GestionMessage.class.toString(), "Impossible de creer message JSON : " + e.getMessage());
		}
		
		return vMessageReponseJSON;
	}
	
	/**
	 * Cette methode envoie le message JSON a l'URL indiquee
	 * ATTENTION : Cette methode doit etre executee dans un Thread different
	 *  
	 * @param pUrlLogin l'URL destination
	 * @param pMessageJSON le message a transmettre
	 * @return le message reponse
	 */
	private JSONObject envoyerMessageJSON(URL pUrlLogin, JSONObject pMessageJSON) {
		
		HttpURLConnection vUrlConnexion = null;
		JSONObject vMessageReponse = null;	// Message reponse JSON
		
		try {

			vUrlConnexion = (HttpURLConnection) pUrlLogin.openConnection();			

			vUrlConnexion.setDoOutput(true);
			vUrlConnexion.setDoInput(true);
			vUrlConnexion.setChunkedStreamingMode(0);
			vUrlConnexion.setUseCaches(false);
			vUrlConnexion.setRequestMethod("POST");
			vUrlConnexion.setRequestProperty("Content-Type", "application/json");

			vUrlConnexion.connect();
			
			BufferedOutputStream vFluxEcriture;
			OutputStream vOutputStream = vUrlConnexion.getOutputStream();
			if(vOutputStream != null) {
				vFluxEcriture = new BufferedOutputStream(vOutputStream);
				int c;
				String data = "";
				
				vFluxEcriture.write(pMessageJSON.toString(2).getBytes());		// envoi du message JSON
				vFluxEcriture.close();
				
				// lecture du message reponse
				InputStream vFluxLecture = new BufferedInputStream(vUrlConnexion.getInputStream());
				while ((c = vFluxLecture.read()) != -1) {
					data += (char) c;
				}				
					
				if(data != null && !data.equals("") && !data.equals("null")) {
					JSONTokener object = new JSONTokener(data);
					vMessageReponse = (JSONObject) object.nextValue();					
				}
				
				vFluxLecture.close();
			}
			else {
				Log.e(GestionMessage.class.toString(), "Impossible de recuperer le message reponse");
			}
			
		} catch (ProtocolException e) {
			Log.e(GestionMessage.class.toString(), "Methode de requete non autorise : " + e.getMessage());
		} catch (IOException e) {
			Log.e(GestionMessage.class.toString(), "Erreur sur les flux d'entree/sortie : " + e.getMessage());
		} catch (JSONException e) {
			Log.e(GestionMessage.class.toString(), "Probleme sur les messages JSON : " + e.getMessage());
		} finally {
			if (vUrlConnexion != null)
				vUrlConnexion.disconnect();
		}
		
		return vMessageReponse;
	}

}