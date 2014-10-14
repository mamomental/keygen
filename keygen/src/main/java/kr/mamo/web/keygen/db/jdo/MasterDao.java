package kr.mamo.web.keygen.db.jdo;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

@Repository
public class MasterDao implements Serializable {
	private static final long serialVersionUID = 1886392938586750972L;
	
//	@Autowired
//    private PersistenceManagerFactory persistenceManagerFactory;
//	
//
//
//    public Collection<MasterJdo> findAllMasters() {
//        PersistenceManager persistenceManager = persistenceManagerFactory.getPersistenceManager();
//        Query query = persistenceManager.newQuery(MasterJdo.class);
//        query.setOrdering("id descending");
//        Collection<MasterJdo> masters = (Collection<MasterJdo>) query.execute();
//        return masters;
//    }
}
