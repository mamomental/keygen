package kr.mamo.web.keygen.service;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import kr.mamo.web.keygen.db.datastore.DatastoreManager;
import kr.mamo.web.keygen.db.datastore.FilterCallback;
import kr.mamo.web.keygen.db.model.User;
import kr.mamo.web.keygen.util.Base64;
import kr.mamo.web.keygen.util.RSA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@Service
public class MasterService {
	private static final String TABLE = "User";
	@Autowired
	DatastoreManager datastoreManager;
	
	@Autowired
	RSA rsa;
	
	@Autowired
	Base64 base64;
	
	public User info() {
		Entity entity = datastoreManager.selectOne(TABLE, new FilterCallback() {
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
		try {
			KeyPair keypair = rsa.generateRSAKeys();
			user.setLevel(User.LEVEL.MASTER.getLevel());
			user.setPublicKey(base64.encode(keypair.getPublic().getEncoded()));
			user.setPrivateKey(base64.encode(keypair.getPrivate().getEncoded()));
			datastoreManager.insert(user.toEntity());
			return true;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean unregister(final String email) {
		User user = info();
		if (null == user) return false;
		
		if (user.getEmail().equals(email)) {
			datastoreManager.delete(TABLE, new FilterCallback() {
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
