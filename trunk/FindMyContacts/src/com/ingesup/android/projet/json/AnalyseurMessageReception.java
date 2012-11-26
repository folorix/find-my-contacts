package com.ingesup.android.projet.json;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ingesup.android.projet.reponses.ReponseAuthentification;

public class AnalyseurMessageReception {
	
	public static ReponseAuthentification analyserReponseAuthentification(JSONObject pMessageRecu) {
		if(pMessageRecu == null)
			throw new IllegalArgumentException("Message reponse incorrect");
		
		ReponseAuthentification vReponseAuthentification = new ReponseAuthentification();
		
		try {
			vReponseAuthentification.setJetonSession((String) pMessageRecu.get("sessionToken"));
		} catch (JSONException e) {
			Log.e(AnalyseurMessageReception.class.toString(), "attribut \"sessionToken\" absent");
		}
			
		try {
			vReponseAuthentification.setEstAuthentifie(pMessageRecu.getBoolean("status"));
		} catch (JSONException e) {
			Log.e(AnalyseurMessageReception.class.toString(), "attribut \"status\" absent");
		}
			
		return vReponseAuthentification;
	}
}

