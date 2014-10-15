package kr.mamo.web.keygen.service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import kr.mamo.web.keygen.db.datastore.DatastoreManager;
import kr.mamo.web.keygen.db.datastore.FilterCallback;
import kr.mamo.web.keygen.db.model.User;
import kr.mamo.web.keygen.util.Base64Util;
import kr.mamo.web.keygen.util.RSAUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@Service
public class MasterService {
	@Autowired
	DatastoreManager datastoreManager;
	
	@Autowired
	RSAUtil rsa;
	
	@Autowired
	Base64Util base64;
	
	public User info() {
		Entity entity = datastoreManager.selectOne(User.TABLE, new FilterCallback() {
			@Override
			public Filter filter() {
				return new FilterPredicate("level", FilterOperator.EQUAL, User.LEVEL.MASTER.getLevel());
			}
		});
		
		if (null != entity) {
			return User.build(entity);
		}

		return null;
	}
	
	public boolean register(String email) {
		User user = info();
		if (null != user) return false;
		
		user = new User();
		user.setEmail(email);
		KeyPair keypair = rsa.generateRSAKeys();
		user.setLevel(User.LEVEL.MASTER.getLevel());
		user.setPublicKey(base64.encode(keypair.getPublic().getEncoded()));
		user.setPrivateKey(base64.encode(keypair.getPrivate().getEncoded()));
		datastoreManager.insert(user.toEntity());
		return true;
	}
	
	public boolean unregister(final String email) {
		User user = info();
		if (null == user) return false;
		
		if (user.getEmail().equals(email)) {
			datastoreManager.delete(User.TABLE, new FilterCallback() {
				@Override
				public Filter filter() {
					return new FilterPredicate("email", FilterOperator.EQUAL, email);
				}
			});
			return true;
		}
		return false;
	}
}
