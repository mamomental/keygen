package kr.mamo.web.keygen.service;

import java.security.Key;
import java.security.KeyPair;

import kr.mamo.web.keygen.db.datastore.DatastoreManager;
import kr.mamo.web.keygen.db.datastore.FilterCallback;
import kr.mamo.web.keygen.db.model.User;
import kr.mamo.web.keygen.util.Base64Util;
import kr.mamo.web.keygen.util.DESUtil;
import kr.mamo.web.keygen.util.EmailUtil;
import kr.mamo.web.keygen.util.RSAUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@Service
public class UserInfoService {
	@Autowired
	DatastoreManager datastoreManager;
	
	@Autowired
	RSAUtil rsaUtil;
	
	@Autowired
	DESUtil desUtil;
	
	@Autowired
	Base64Util base64Util;
	
	@Autowired
	EmailUtil emailUtil;
	
	public User info(final String email) {
		Entity entity = datastoreManager.selectOne(User.TABLE, new FilterCallback() {
			@Override
			public Filter filter() {
				return new FilterPredicate("email", FilterOperator.EQUAL, email);
			}
		});
		
		if (null != entity) {
			return User.build(entity);
		}

		return null;
	}
	
	public boolean create(String email) {
		User user = info(email);
		if (null != user) return false;
		
		user = new User();
		user.setEmail(email);
		KeyPair keypair = rsaUtil.generateRSAKeys();
		user.setLevel(User.LEVEL.BASIC.getLevel());
		user.setPublicKey(base64Util.encode(keypair.getPublic().getEncoded()));
		Key messageKey = desUtil.createMessageKey();
		user.setPrivateKey(base64Util.encode(desUtil.encrypt(messageKey, keypair.getPrivate().getEncoded())));
		datastoreManager.insert(user.toEntity());
		emailUtil.send(email, email, base64Util.encode(messageKey.getEncoded()));
		return true;
	}
	
	public boolean delete(final String email) {
		User user = info(email);
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
