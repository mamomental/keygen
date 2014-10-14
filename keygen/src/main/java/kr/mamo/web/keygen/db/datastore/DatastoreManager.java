package kr.mamo.web.keygen.db.datastore;

import java.util.Map;

import kr.mamo.web.keygen.util.RSA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;

@Component
public class DatastoreManager {
	@Autowired
	RSA rsa;

	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public void insert(Entity entity) {
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
	
	public void delete(String tableString, FilterCallback filter) {
		Query query = new Query(tableString);
		query.setFilter(filter.filter());
		Entity entity = datastore.prepare(query).asSingleEntity();
		datastore.delete(entity.getKey());
	}
}
