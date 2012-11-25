package com.ingesup.android.projet.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ingesup.android.projet.reponses.ReponseAuthentification;

public class AnalyseurMessageReception {
	
	public static ReponseAuthentification analyserReponseAuthentification(JSONObject pMessageRecu) {
		ReponseAuthentification vReponseAuthentification = null;
		
		try {
			String vJetonSession = (String) pMessageRecu.get("sessionToken");
			boolean vEtat = pMessageRecu.getBoolean("status");
			
			vReponseAuthentification = new ReponseAuthentification(vJetonSession, vEtat);
			
		} catch (JSONException e) {
			Log.d(AnalyseurMessageReception.class.toString(), "Impossible d'analyser la reponse de la demande d'authentification : " + e.getMessage());
		}
		
		return vReponseAuthentification;
	}
}

