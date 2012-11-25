package com.ingesup.android.projet.reponses;

public class ReponseAuthentification {
	private final boolean _estAuthentifie;
	private final String _jetonSession;
	
	public ReponseAuthentification(String pJetonSession, boolean pEtat) {
		_estAuthentifie = pEtat;
		_jetonSession = pJetonSession;
	}

	public boolean estAuthentifie() {
		return _estAuthentifie;
	}

	public String getJetonSession() {
		return _jetonSession;
	}
	
	@Override
	public String toString() {
		return "Authentification OK - Jeton Session : " + _jetonSession;
	}
}
