package com.ingesup.android.projet.json;

import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ingesup.android.projet.contact.Contact;
import com.ingesup.android.projet.outils.Sha1Utils;

public class FormatMessageEnvoi {
	
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
	
	public static JSONObject formatterMessageLogout() {
		return formatterMessageParDefaut();
	}
	
	public static JSONObject formatterMessageAjoutUtilisateur(Contact pContactAAjouter) {
		JSONObject vMessage = formatterMessageParDefaut();
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
			vMessage.accumulate("longitude", pContactAAjouter.getLongitude());
			vMessage.accumulate("latitude", pContactAAjouter.getLatitude());			
		} catch (JSONException e) {
			Log.d(FormatMessageEnvoi.class.toString(), "Impossible de formatter le message d'ajout d'un contact : " + e.getMessage());
		}
		
		return vMessage;
	}
	
	public static JSONObject formatterMessageSuppressionUtilisateur() {
		return formatterMessageParDefaut();
	}
	
	public static JSONObject formatterMessageMajUtilisateur(Contact pContactAMettreAJour, int pIdContact) {
		JSONObject vMessageMajUtilisateur = formatterMessageAjoutUtilisateur(pContactAMettreAJour);
		try {
			vMessageMajUtilisateur.accumulate("identifiant", String.valueOf(pIdContact));
		} catch (JSONException e) {
			Log.d(FormatMessageEnvoi.class.toString(), "Impossible de formatter le message de mise a jour du contact : " + e.getMessage());
		}
		return vMessageMajUtilisateur;
	}
	
	public static JSONObject formatterMessageRecuperationUtilisateur(int pIdContact) {
		return formatterMessageParDefaut();
	}
	
	public static JSONObject formatterMessageRecuperationUtilisateurs() {
		return formatterMessageParDefaut();
	}
	
	public static JSONObject formatterMessageChangementPhotoUtilisateur() {
		return formatterMessageParDefaut();
	}
	
	public static JSONObject formatterMessageRecuperationPhotoUtilisateur() {
		return formatterMessageParDefaut();
	}

	public static JSONObject formatterMessageSuppressionPhotoUtilisateur() {
		return formatterMessageParDefaut();
	}
	
	public static JSONObject formatterMessageServiceRecherche() {
		return formatterMessageParDefaut();
	}
	
	public JSONObject formatterMessageServiceGenerationRapport() {
		return formatterMessageParDefaut();
	}
}
