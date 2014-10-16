package kr.mamo.web.keygen.db.model;

import kr.mamo.web.keygen.KeygenConstant;
import kr.mamo.web.keygen.db.datastore.EntityInterface;

import com.google.appengine.api.datastore.Entity;

public class SiteInfo implements EntityInterface {
	private long keyId;
	private String email;
	private String siteName;
	private String siteId;
	private String sitePw;	
	
	public long getKeyId() {
		return keyId;
	}
	public void setKeyId(long keyId) {
		this.keyId = keyId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getSitePw() {
		return sitePw;
	}
	public void setSitePw(String sitePw) {
		this.sitePw = sitePw;
	}
	public static SiteInfo build(Entity entity) {
		SiteInfo si = new SiteInfo();
		si.setKeyId(entity.getKey().getId());
		
		Object email = entity.getProperty("email");
		if (null != email) {
			si.setEmail((String)email);
		}
		Object siteName = entity.getProperty("siteName");
		if (null != siteName) {
			si.setSiteName((String)siteName);
		}

		Object siteId = entity.getProperty("siteId");
		if (null != siteId) {
			si.setSiteId((String)siteId);
		}
		Object sitePw = entity.getProperty("sitePw");
		if (null != sitePw) {
			si.setSitePw((String)sitePw);
		}
		return si;
	}
	@Override
	public Entity toEntity() {
		Entity entity = new Entity(KeygenConstant.DataStore.TABLE_SITE_INFO);
		entity.setProperty("email", email);
		entity.setProperty("siteName", siteName);
		entity.setProperty("siteId", siteId);
		entity.setProperty("sitePw", sitePw);
		return entity;
	}
}
