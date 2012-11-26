package com.ingesup.android.projet.reponses;

public class ReponseAuthentification {
	private boolean _estAuthentifie;
	private String _jetonSession;

	public boolean estAuthentifie() {
		return _estAuthentifie;
	}

	public String getJetonSession() {
		return _jetonSession;
	}
	
	public void setJetonSession(String pJetonSession) {
		this._jetonSession = pJetonSession;
	}
	
	public void setEstAuthentifie(boolean pEstAuthentifie) {
		this._estAuthentifie = pEstAuthentifie;
	}
	
	@Override
	public String toString() {
		return "Authentification OK - Jeton Session : " + _jetonSession;
	}
}
