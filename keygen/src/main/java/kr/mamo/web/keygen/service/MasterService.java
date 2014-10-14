package kr.mamo.web.keygen.service;

import java.util.HashMap;
import java.util.Map;

import kr.mamo.web.keygen.db.datastore.DatastoreManager;
import kr.mamo.web.keygen.db.datastore.FilterCallback;
import kr.mamo.web.keygen.db.model.User;
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
	
	public User info() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "master");
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
	
	public void register(String email) {
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "master");
		map.put("email", email);
		datastoreManager.update(TABLE, map);
	}
}
