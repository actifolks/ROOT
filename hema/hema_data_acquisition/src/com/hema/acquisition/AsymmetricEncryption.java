package com.hema.acquisition;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class AsymmetricEncryption {

	 	public static byte[] encrypt(byte[] inpBytes, PublicKey key, String xform) throws Exception {
		    Cipher cipher = Cipher.getInstance(xform);
		    cipher.init(Cipher.ENCRYPT_MODE, key);
		    return cipher.doFinal(inpBytes);
		}

	 	public static byte[] decrypt(byte[] inpBytes, PrivateKey key, String xform) throws Exception{
 		    Cipher cipher = Cipher.getInstance(xform);
 		    cipher.init(Cipher.DECRYPT_MODE, key);
 		    return cipher.doFinal(inpBytes);
	 	}
	 	
	 	public static void generateKeys(byte[] dataBytes) throws Exception{
	 		
	 		String xform = "RSA/NONE/PKCS1PADDING";
	 	    // Generate a key-pair
	 	    KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
	 	    kpg.initialize(512); // 512 is the keysize.
	 	    KeyPair kp = kpg.generateKeyPair();
	 	    PublicKey pubk = kp.getPublic();
	 	    PrivateKey prvk = kp.getPrivate();
	 		
	 	    byte[] encBytes = encrypt(dataBytes, pubk, xform);
	 	    byte[] decBytes = decrypt(encBytes, prvk, xform);
	 	   
	 	}
	 	
}
