package kr.mamo.web.keygen.service;

import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import kr.mamo.web.keygen.datastore.DatastoreManager;
import kr.mamo.web.keygen.model.Master;
import kr.mamo.web.keygen.util.RSA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.Entity;

@Service
public class MasterService {
	private static final String TABLE = "Master";
	@Autowired
	DatastoreManager datastoreManager;
	
	@Autowired
	RSA rsa;
	
	@PostConstruct
	public void init() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "master");
		List<Entity> list = datastoreManager.read(TABLE, map, 1);
		if (null == list || 0 == list.size()) {
			try {
				KeyPair keypair = rsa.generateRSAKeys();
//				map.put("publicKey", keypair.getPublic().getEncoded());
//				map.put("privateKey", keypair.getPrivate().getEncoded());
				map.put("publicKey", new String(keypair.getPublic().getEncoded(), "UTF-8"));
				map.put("privateKey", new String(keypair.getPrivate().getEncoded(), "UTF-8"));
				datastoreManager.create(TABLE, "master", map);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Master info() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "master");
		List<Entity> list = datastoreManager.read(TABLE, map, 1);
		if (null != list & 1 == list.size()) {
			Entity entity = list.get(0);
			return Master.build(entity);
		}
		return null;
	}
	
	public void register(String email) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "master");
		map.put("email", email);
		datastoreManager.update(TABLE, map);
	}
}
