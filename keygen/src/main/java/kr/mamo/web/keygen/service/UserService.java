package kr.mamo.web.keygen.service;

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
public class UserService {
	private static final String TABLE = "User";
	@Autowired
	DatastoreManager datastoreManager;
	
	@Autowired
	RSA rsa;
	
	@Autowired
	Base64 base64;
	
	public User info(final String email) {
		Entity entity = datastoreManager.selectOne(TABLE, new FilterCallback() {
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
}
