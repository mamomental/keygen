package kr.mamo.web.keygen.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

@Component
public class RSAUtil {
	public KeyPair generateRSAKeys() throws NoSuchAlgorithmException {
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(512);
		return generator.genKeyPair();
	}
}
