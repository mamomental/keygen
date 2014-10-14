package kr.mamo.web.keygen.db.datastore;

import com.google.appengine.api.datastore.Query.Filter;

public interface FilterCallback {
	public Filter filter();
}
