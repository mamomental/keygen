package kr.mamo.web.keygen.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.stereotype.Component;

@Component
public class RSAUtil {
	private static final String KEYPAIR_ALGORITHM = "RSA";
	private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
	private static final int ENCODING_KEY_SIZE = 53;
	private static final int DECODING_KEY_SIZE = 64;

	public KeyPair generateRSAKeys() {
		return generateRSAKeys(512);
	}

	public KeyPair generateRSAKeys(int rsabits) {
		try {
			KeyPairGenerator generator = KeyPairGenerator
					.getInstance(KEYPAIR_ALGORITHM);
			generator.initialize(rsabits);
			return generator.genKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] decrypt(Key key, byte[] data) {
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, key);
			if (data.length > DECODING_KEY_SIZE) {
				return blockCipher(cipher, data, Cipher.DECRYPT_MODE);
			}
			return cipher.doFinal(data);
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} finally {
			if (null != bis) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != bos) {
				try {
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public byte[] encrypt(Key key, byte[] data) {
		ByteArrayInputStream bis = null;
		ByteArrayOutputStream bos = null;
		if (null != key) {
			try {
				Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
				cipher.init(Cipher.ENCRYPT_MODE, key);
				byte[] enc = null;
				if (data.length > ENCODING_KEY_SIZE) {
					enc = blockCipher(cipher, data, Cipher.ENCRYPT_MODE);
				} else {
					enc = cipher.doFinal(data);
				}
				return enc;
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} finally {
				if (null != bis) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (null != bos) {
					try {
						bos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	private byte[] blockCipher(Cipher cipher, byte[] bytes, int mode) {
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		byte[] scrambled = new byte[0];
		byte[] toReturn = new byte[0];

		try {
			// toReturn will hold the total result
			// if we encrypt we use 100 byte long blocks. Decryption requires
			// 128 byte long blocks (because of RSA)
			int length = (mode == Cipher.ENCRYPT_MODE) ? ENCODING_KEY_SIZE
					: DECODING_KEY_SIZE;

			// another buffer. this one will hold the bytes that have to be
			// modified in this step
			byte[] buffer = new byte[length];

			for (int i = 0; i < bytes.length; i++) {

				// if we filled our buffer array we have our block ready for de-
				// or encryption
				if ((i > 0) && (i % length == 0)) {
					// execute the operation
					scrambled = cipher.doFinal(buffer);
					// add the result to our total result.
					toReturn = append(toReturn, scrambled);
					// here we calculate the length of the next buffer required
					int newlength = length;

					// if newlength would be longer than remaining bytes in the
					// bytes array we shorten it.
					if (i + length > bytes.length) {
						newlength = bytes.length - i;
					}
					// clean the buffer array
					buffer = new byte[newlength];
				}
				// copy byte into our buffer.
				buffer[i % length] = bytes[i];
			}

			// this step is needed if we had a trailing buffer. should only
			// happen when encrypting.
			// example: we encrypt 110 bytes. 100 bytes per run means we
			// "forgot" the last 10 bytes. they are in the buffer array
			scrambled = cipher.doFinal(buffer);

			// final step before we can return the modified data.
			toReturn = append(toReturn, scrambled);
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return toReturn;
	}

	private static byte[] append(byte[] prefix, byte[] suffix) {
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i = 0; i < prefix.length; i++) {
			toReturn[i] = prefix[i];
		}
		for (int i = 0; i < suffix.length; i++) {
			toReturn[i + prefix.length] = suffix[i];
		}
		return toReturn;
	}

	public PublicKey bytesToPubKey(byte[] bytes) {
		try {
			return KeyFactory.getInstance(KEYPAIR_ALGORITHM).generatePublic(
					new X509EncodedKeySpec(bytes));
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public PrivateKey bytesToPrivKey(byte[] bytes) {
		try {
			return KeyFactory.getInstance(KEYPAIR_ALGORITHM).generatePrivate(
					new PKCS8EncodedKeySpec(bytes));
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
