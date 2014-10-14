package kr.mamo.web.keygen.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class RSATest {
	@Test
	public void test() {
		RSAUtil rsa = new RSAUtil();
		KeyPair keypair;
		try {
			keypair = rsa.generateRSAKeys();
			String pbk = new String(keypair.getPublic().getEncoded(), "UTF-8");
			String prk = new String(keypair.getPrivate().getEncoded(), "UTF-8");
			System.out.println("pbk : " + pbk.length() + ", " + pbk);
			System.out.println("prk : " + prk.length() + ", " + prk);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
}
