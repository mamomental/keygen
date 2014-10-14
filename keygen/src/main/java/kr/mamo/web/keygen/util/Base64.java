package kr.mamo.web.keygen.util;

import org.springframework.stereotype.Component;

@Component
public class Base64 {
	public String encode(byte[] data) {
		return new String(org.apache.commons.codec.binary.Base64.encodeBase64(data));
	}

	public byte[] decode(String data) {
		return org.apache.commons.codec.binary.Base64.decodeBase64(data.getBytes());
	}
}
