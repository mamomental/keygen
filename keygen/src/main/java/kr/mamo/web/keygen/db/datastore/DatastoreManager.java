package kr.mamo.web.keygen.db.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

@Component
public class DatastoreManager {
	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	public void insert(Entity entity) {
		datastore.put(entity);
	}
	
	public Entity selectOne(String tableString, FilterCallback filter) {
		Query query = new Query(tableString).setFilter(filter.filter());
		
		return datastore.prepare(query).asSingleEntity();
	}
	
	public List<Entity> selectList(String tableString, FilterCallback filter) {
		Query query = new Query(tableString).setFilter(filter.filter());
		List<Entity> result = new ArrayList<Entity>();
		Iterable<Entity> list = datastore.prepare(query).asIterable();
		for (Entity entity : list) {
			result.add(entity);
		}
		return result;
	}
	
	public void update(String tableString, FilterCallback filter, Entity updatedEntity) {
		Entity entity = selectOne(tableString, filter);
		if (null != entity) {
			Map<String, Object> map = updatedEntity.getProperties();
			for (String column : map.keySet()) {
				entity.setProperty(column, map.get(column));
			}
			datastore.put(entity);
		}
	}
	
	public void delete(String tableString, FilterCallback filter) {
		Query query = new Query(tableString);
		query.setFilter(filter.filter());
		Transaction tx = datastore.beginTransaction();
		Iterable<Entity> list = datastore.prepare(query).asIterable();
		for (Entity entity : list) {
			datastore.delete(entity.getKey());
		}
		tx.commit();
	}
}
