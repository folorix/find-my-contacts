package com.ingesup.android.projet.smslistener;

import com.ingesup.android.projet.mail.GMailSender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsListener extends BroadcastReceiver {

	private static final String SUBJECT = "Message du groupe 9 : DAROUSSI Frédéric et DEBLAINE Maxence";

	private SharedPreferences _preferences;
	
	private String msg_from;
	private String msgBody;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			Bundle bundle = intent.getExtras(); // ---get the SMS message passed
												// in---
			SmsMessage[] msgs = null;
			if (bundle != null) {
				// ---retrieve the SMS message received---
				try {
					Object[] pdus = (Object[]) bundle.get("pdus");
					msgs = new SmsMessage[pdus.length];
					for (int i = 0; i < msgs.length; i++) {
						msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
						msg_from = msgs[i].getOriginatingAddress();
						msgBody = msgs[i].getMessageBody();

						Toast.makeText(context,
								"Message de " + msg_from + " : " + msgBody,
								Toast.LENGTH_LONG).show();
						
						new AsyncTask<Void, Void, Void>() {
							
							@Override
							protected Void doInBackground(Void... params) {
								String vUser = _preferences.getString("USER_MAIL_ADDRESS", "");
								String vPassword = _preferences.getString("USER_MAIL_PASSWORD", "");
								
								if(((vUser != null) && (!vUser.equals(""))) && 
										((vPassword != null) && (!vPassword.equals("")))) {
									
									try {
										GMailSender sender = new GMailSender(vUser, vPassword);
										sender.sendMail(
												SUBJECT, 
												"Expediteur: " + msg_from + "\n" + "Message : " + msgBody,
												msg_from,
												vUser);
																				
									} catch (Exception e) {
										Log.e("SendMail", "Impossible d'envoyer le mail : " + e.getMessage() );
									}
									
								}
								else {
									Log.d("Transfert sms vers mail", "Utilisateur et Mot de passe non défini dans les préférences");
								}
								
								return null;
							}
						}.execute();


					}
				} catch (Exception e) {
					// Log.d("Exception caught",e.getMessage());
				}
			}
		}
	}
}