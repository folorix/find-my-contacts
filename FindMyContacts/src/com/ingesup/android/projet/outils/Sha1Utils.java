package com.ingesup.android.projet.outils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1Utils {

	/**
	 * encryptage SHA-1
	 * 
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha(String text) throws NoSuchAlgorithmException {
		// Convertion SHA-1 pour le hash
		MessageDigest md = MessageDigest.getInstance("SHA-1"); //$NON-NLS-1$
		md.update((text).getBytes());
		String hashRetour = new String(convertToHex(md.digest()));

		return hashRetour;
	}

	/**
	 * Methode de conversion byte en String hexa
	 * 
	 * @param data
	 * @return
	 */
	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (byte element : data) {
			int halfbyte = (element >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9)) {
					buf.append((char) ('0' + halfbyte));
				} else {
					buf.append((char) ('a' + (halfbyte - 10)));
				}
				halfbyte = element & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}
}
