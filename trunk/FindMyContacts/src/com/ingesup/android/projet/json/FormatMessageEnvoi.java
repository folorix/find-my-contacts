package com.ingesup.android.projet.json;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ingesup.android.projet.contact.Contact;
import com.ingesup.android.projet.outils.Sha1Utils;

public class FormatMessageEnvoi {
	
	public static JSONObject formatterMessageParDefaut(String pSessionToken) {
		JSONObject vMessage = new JSONObject();
		
		try {
			vMessage.accumulate("wsCallerId", "ipponTech");
			vMessage.accumulate("timestamp", String.valueOf(Calendar.getInstance().getTimeInMillis()));
			vMessage.accumulate("hash", pSessionToken);
		} catch (JSONException e) {
			Log.d(FormatMessageEnvoi.class.toString(), "Impossible de formatter le message par defaut : " + e.getMessage());
		}
		
		return vMessage;
	}
	
	public static JSONObject formatterMessageParDefaut() {
		JSONObject vMessage = new JSONObject();
		
		try {
			vMessage.accumulate("wsCallerId", "ipponTech");
			long vTimestamp = Calendar.getInstance().getTimeInMillis();
			vMessage.accumulate("timestamp", String.valueOf(vTimestamp));
			vMessage.accumulate("hash", Sha1Utils.sha("secret" + vTimestamp));
		} catch (JSONException e) {
			Log.d(FormatMessageEnvoi.class.toString(), "Impossible de formatter le message par defaut : " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			Log.d(FormatMessageEnvoi.class.toString(), "Impossible de generer la cle de hashage pour le message par defaut : " + e.getMessage());
		}
		
		return vMessage;
	}
	
	public static JSONObject formatterMessageLogin(String pLogin, String pMotDePasse) {
		JSONObject vMessage = formatterMessageParDefaut();
		
		try {
			vMessage.accumulate("identifiant", pLogin);
			vMessage.accumulate("password", pMotDePasse);
		} catch (JSONException e) {
			Log.d(FormatMessageEnvoi.class.toString(), "Impossible de formatter le message d'authentification : " + e.getMessage());
		}
		
		return vMessage;
	}
	
	public static JSONObject formatterMessageLogout(String pSessionToken) {
		return formatterMessageParDefaut();
	}
	
	public static JSONObject formatterMessageAjoutUtilisateur(String pSessionToken, Contact pContactAAjouter) {
		JSONObject vMessage = formatterMessageParDefaut(pSessionToken);
		try {
			vMessage.accumulate("nom", pContactAAjouter.getNom());
			vMessage.accumulate("prenom", pContactAAjouter.getPrenom());
			vMessage.accumulate("dateNaissance", pContactAAjouter.getDateNaissance());
			vMessage.accumulate("numeroRue", pContactAAjouter.getNumRue());
			vMessage.accumulate("nomRue", pContactAAjouter.getNomRue());
			vMessage.accumulate("ville", pContactAAjouter.getVille());
			vMessage.accumulate("codePostal", pContactAAjouter.getCodePostal());
			vMessage.accumulate("pays", pContactAAjouter.getPays());
			vMessage.accumulate("tel", pContactAAjouter.getNumTelephone());
			vMessage.accumulate("email", pContactAAjouter.getEmail());
		} catch (JSONException e) {
			Log.d(FormatMessageEnvoi.class.toString(), "Impossible de formatter le message d'ajout d'un utilisateur : " + e.getMessage());
		}
		
		return null;
	}
	
	public static JSONObject formatterMessageSuppressionUtilisateur(String pSessionToken) {
		return formatterMessageParDefaut(pSessionToken);
	}
	
	public static JSONObject formatterMessageMajUtilisateur(String pSessionToken, Contact pContactAMettreAJour) {
		return formatterMessageAjoutUtilisateur(pSessionToken, pContactAMettreAJour);
	}
	
	public static JSONObject formatterMessageRecuperationUtilisateur(String pSessionToken) {
		return formatterMessageParDefaut(pSessionToken);
	}
	
	public static JSONObject formatterMessageRecuperationUtilisateurs(String pSessionToken) {
		return formatterMessageParDefaut(pSessionToken);
	}
	
	public static JSONObject formatterMessageChangementPhotoUtilisateur(String pSessionToken) {
		return formatterMessageParDefaut(pSessionToken);
	}
	
	public static JSONObject formatterMessageRecuperationPhotoUtilisateur(String pSessionToken) {
		return formatterMessageParDefaut(pSessionToken);
	}

	public static JSONObject formatterMessageSuppressionPhotoUtilisateur(String pSessionToken) {
		return formatterMessageParDefaut(pSessionToken);
	}
	
	public static JSONObject formatterMessageServiceRecherche(String pSessionToken) {
		return formatterMessageParDefaut(pSessionToken);
	}
	
	public JSONObject formatterMessageServiceGenerationRapport(String pSessionToken) {
		return formatterMessageParDefaut(pSessionToken);
	}
}
