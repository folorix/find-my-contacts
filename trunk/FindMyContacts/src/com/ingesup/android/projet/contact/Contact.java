package com.ingesup.android.projet.contact;

public class Contact {
	
	private String _nom = new String();
	private String _prenom = new String();
	private String _dateNaissance = new String();
	
	private int _numRue;
	private String _nomRue = new String();
	private String _codePostal = new String();
	private String _ville = new String();
	private String _pays = new String();
	
	private String _numTelephone = new String();
	private String _email = new String();

	private boolean _estGeolocalisable = false;
	
	private double _longitude;
	private double _latitude;
	
	public String getNom() {
		return _nom;
	}
	
	public void setNom(String pNom) {
		this._nom = pNom;
	}
	
	public String getPrenom() {
		return _prenom;
	}
	
	public void setPrenom(String pPrenom) {
		this._prenom = pPrenom;
	}
	
	public String getDateNaissance() {
		return _dateNaissance;
	}
	
	public void setDateNaissance(String pDateNaissance) {
		this._dateNaissance = pDateNaissance;
	}
	
	public int getNumRue() {
		return _numRue;
	}
	
	public void setNumRue(int pNumRue) {
		this._numRue = pNumRue;
	}
	
	public String getNomRue() {
		return _nomRue;
	}
	
	public void setNomRue(String pNomRue) {
		this._nomRue = pNomRue;
	}
	
	public String getVille() {
		return _ville;
	}
	
	public void setVille(String pVille) {
		this._ville = pVille;
	}
	
	public String getPays() {
		return _pays;
	}
	
	public void setPays(String pPays) {
		this._pays = pPays;
	}
	
	public String getCodePostal() {
		return _codePostal;
	}
	
	public void setCodePostal(String pCodePostal) {
		this._codePostal = pCodePostal;
	}
	
	public String getNumTelephone() {
		return _numTelephone;
	}
	
	public void setNumTelephone(String pNumTelephone) {
		this._numTelephone = pNumTelephone;
	}
	
	public boolean EstGeolocalisable() {
		return _estGeolocalisable;
	}
	
	public void setGeolocalisation(boolean pEstGeolocalisable) {
		this._estGeolocalisable = pEstGeolocalisable;
	}

	public double getLongitude() {
		return _longitude;
	}

	public double getLatitude() {
		return _latitude;
	}

	public void setLatitude(double pLatitude) {
		_latitude = pLatitude;
	}

	public void setLongitude(double pLongitude) {
		_longitude = pLongitude;
	}

	public String getEmail() {
		return _email;
	}
	
	public void setEmail(String pEmail) {
		_email = pEmail;
	}
}
