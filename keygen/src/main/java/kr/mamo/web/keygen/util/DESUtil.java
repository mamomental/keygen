package kr.mamo.web.keygen.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class DESUtil {
	private static final String ALGORITHM = "DES";
	
	public Key createMessageKey() {
		try {
			KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
			generator.init(new java.security.SecureRandom());
			return generator.generateKey();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	  public byte[] encrypt(Key messageKey, byte[] bytes) {
		  try {
			  Cipher ecipher = Cipher.getInstance(ALGORITHM);
			  ecipher.init(Cipher.ENCRYPT_MODE, messageKey);
			// Encrypt
			return ecipher.doFinal(bytes);
			} catch (javax.crypto.BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
					e.printStackTrace();
			}
		    return null;
		}

    public byte[] decrypt(Key messageKey, byte[] dec) {
        try {
		    Cipher dcipher = Cipher.getInstance(ALGORITHM);
		    dcipher.init(Cipher.DECRYPT_MODE, messageKey);
            // Decrypt
            return dcipher.doFinal(dec);
        } catch (javax.crypto.BadPaddingException e) {
			e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
        return null;
    }

    public Key makeKey(byte[] encodedKey) {
    	return new SecretKeySpec(encodedKey, 0, encodedKey.length, ALGORITHM);
    }
}
