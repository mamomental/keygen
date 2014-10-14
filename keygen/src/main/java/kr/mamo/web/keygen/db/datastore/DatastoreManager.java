package kr.mamo.web.keygen.db.datastore;

import java.util.List;
import java.util.Map;

import kr.mamo.web.keygen.util.RSA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

@Component
public class DatastoreManager {
	@Autowired
	RSA rsa;

	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public void create(String tableString, String keyString, Map<String, Object> map) {
		Key key = KeyFactory.createKey(tableString, keyString);
		Entity entity = new Entity(tableString, key);
		
		for (String column : map.keySet()) {
			entity.setProperty(column, map.get(column));
		}
		datastore.put(entity);
	}
	
	public Entity selectOne(String tableString, FilterCallback filter) {
		Query query = new Query(tableString);
		
		query.setFilter(filter.filter());
		
		return datastore.prepare(query).asSingleEntity();
	}
	
	public void update(String tableString, Map<String, Object> map) {
		Query query = new Query(tableString);
		
		for (String column : map.keySet()) {
			query.addFilter(column, FilterOperator.EQUAL, map.get(column));
		}
		PreparedQuery pq = datastore.prepare(query);
		Entity entity = pq.asSingleEntity();
		
		for (String column : map.keySet()) {
			entity.setProperty(column, map.get(column));
		}
		datastore.put(entity);
	}
	
	public void delete(String tableString, String keyString, Map<String, Object> map) {
		Query query = new Query(tableString);
		
		for (String column : map.keySet()) {
			query.addFilter(column, FilterOperator.EQUAL, map.get(column));
		}
		PreparedQuery pq = datastore.prepare(query);
		Entity entity = pq.asSingleEntity();
		datastore.delete(entity.getKey());
	}
}
