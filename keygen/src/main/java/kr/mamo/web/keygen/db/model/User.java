package kr.mamo.web.keygen.db.model;

import kr.mamo.web.keygen.db.datastore.EntityInterface;

import com.google.appengine.api.datastore.Entity;

public class User implements EntityInterface {
	public static final String TABLE = "User";
	
	private String email;
	private int level = LEVEL.ANONYMOUS.getLevel();
	private String publicKey;
	private String privateKey;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public void setLevel(LEVEL level) {
		this.level = level.getLevel();
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
	public static User build(Entity entity) {
		User user = new User();
		Object email = entity.getProperty("email");
		if (null != email) {
			user.setEmail((String)email);
		}
		Object level = entity.getProperty("level");
		if (null != level) {
			user.setLevel((int) (long)level);
		}

		Object pbk = entity.getProperty("publicKey");
		if (null != pbk) {
			user.setPublicKey((String)pbk);
		}
		Object prk = entity.getProperty("privateKey");
		if (null != prk) {
			user.setPrivateKey((String)prk);
		}
		return user;
	}
	@Override
	public Entity toEntity() {
		Entity entity = new Entity(TABLE, email);
		entity.setProperty("email", email);
		entity.setProperty("level", level);
		entity.setProperty("publicKey", publicKey);
		entity.setProperty("privateKey", privateKey);
		return entity;
	}
	
	public enum LEVEL {
			MASTER (10, "master")
		,	BASIC (1, "basic")
		,	ANONYMOUS (0, "anonymous")
		;
		int level;
		String description;
		
		private LEVEL(int level, String description) {
			this.level = level;
			this.description = description;
		}

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
		public static LEVEL fromLevel(int level) {
			for (LEVEL l : LEVEL.values()) {
				if (l.getLevel() == level) {
					return l;
				}
			}
			return null;
		}

	}
}
