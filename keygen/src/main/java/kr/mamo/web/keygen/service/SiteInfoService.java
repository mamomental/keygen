package kr.mamo.web.keygen.service;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import kr.mamo.web.keygen.KeygenConstant;
import kr.mamo.web.keygen.db.datastore.DatastoreManager;
import kr.mamo.web.keygen.db.datastore.FilterCallback;
import kr.mamo.web.keygen.db.model.SiteInfo;
import kr.mamo.web.keygen.util.Base64Util;
import kr.mamo.web.keygen.util.DESUtil;
import kr.mamo.web.keygen.util.RSAUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

@Service
public class SiteInfoService {

	@Autowired
	DatastoreManager datastoreManager;
	
	@Autowired
	RSAUtil rsaUtil;
	
	@Autowired
	DESUtil desUtil;
	
	@Autowired
	Base64Util base64Util;
	
	public boolean create(String publicKey, String email, String siteName, String siteId, String sitePw) {
		SiteInfo siteInfo = read(email, siteName);
		if (null != siteInfo) return false;
		
		try {
			siteInfo = new SiteInfo();
			siteInfo.setEmail(email);
			siteInfo.setSiteName(siteName);
			siteInfo.setSiteId(siteId);
			siteInfo.setSitePw(base64Util.encode(rsaUtil.encrypt(rsaUtil.bytesToPubKey(base64Util.decode(publicKey)), sitePw.getBytes(KeygenConstant.keygenCommon.ENCODING))));
			datastoreManager.insert(siteInfo.toEntity());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public List<SiteInfo> list(String privateKey, String encKey, final String email) {
		
		
		List<Entity> entity = datastoreManager.selectList(SiteInfo.TABLE, new FilterCallback() {
			@Override
			public Filter filter() {
				return new FilterPredicate("email", FilterOperator.EQUAL, email);
			}
		});
		PrivateKey pk = null;
		if (!StringUtils.isEmpty(encKey)) {
			pk = rsaUtil.bytesToPrivKey(desUtil.decrypt(desUtil.makeKey(base64Util.decode(encKey)), base64Util.decode(privateKey)));
		}
		
		List<SiteInfo> result = new ArrayList<SiteInfo>();
		if (null != entity && 0 < entity.size()) {
			for (Entity e : entity) {
				SiteInfo si = SiteInfo.build(e);
				if (null != pk) {
					si.setSitePw(new String(rsaUtil.decrypt(pk, base64Util.decode(si.getSitePw()))));
				}
				result.add(si);
			}
		}

		return result;
	}
	
	public SiteInfo read(final String email, final String siteName) {
		Entity entity = datastoreManager.selectOne(SiteInfo.TABLE, new FilterCallback() {
			@Override
			public Filter filter() {
				Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
				Filter siteNameFilter = new FilterPredicate("siteName", FilterOperator.EQUAL, siteName);
				
				return CompositeFilterOperator.and(emailFilter, siteNameFilter);
			}
		});
		
		if (null != entity) {
			return SiteInfo.build(entity);
		}

		return null;
	}
	
	public boolean update(String publicKey, final String email, final String siteName, String siteId, String sitePw) {
		SiteInfo siteInfo = read(email, siteName);
		if (null == siteInfo) return false;
		siteInfo.setSiteId(siteId);
		siteInfo.setSitePw(sitePw);
		datastoreManager.update(SiteInfo.TABLE, new FilterCallback() {
			@Override
			public Filter filter() {
				Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
				Filter siteNameFilter = new FilterPredicate("siteName", FilterOperator.EQUAL, siteName);
				return CompositeFilterOperator.and(emailFilter, siteNameFilter);
			}
		}, siteInfo.toEntity());
		return true;
	}

	public boolean delete(final String email, final String siteName) {
		if (null == siteName) { // all info
			datastoreManager.delete(SiteInfo.TABLE, new FilterCallback() {
				@Override
				public Filter filter() {
					return new FilterPredicate("email", FilterOperator.EQUAL, email);
				}
			});
		} else {
			SiteInfo siteInfo = read(email, siteName);
			if (null == siteInfo) return false;
			
			datastoreManager.delete(SiteInfo.TABLE, new FilterCallback() {
				@Override
				public Filter filter() {
					Filter emailFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
					Filter siteNameFilter = new FilterPredicate("siteName", FilterOperator.EQUAL, siteName);
					
					return CompositeFilterOperator.and(emailFilter, siteNameFilter);
				}
			});
		}

		return true;
	}
}
