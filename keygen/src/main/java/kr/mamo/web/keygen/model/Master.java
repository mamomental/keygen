package kr.mamo.web.keygen.model;

import com.google.appengine.api.datastore.Entity;

public class Master {
	private String name;
	private String publicKey;
	private String privateKey;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	public static Master build(Entity entity) {
		Master master = new Master();
		master.setName((String)entity.getProperty("name"));
		master.setPublicKey((String)entity.getProperty("publicKey"));
		master.setPrivateKey((String)entity.getProperty("privateKey"));
		return master;
	}
}
