package kr.mamo.web.keygen;

public class KeygenConstant {
	public interface keygenCommon {
		String ENCODING = "UTF-8";
	}
	
	public interface keygenRequest {
		String URL = "url";
		String CURRENT_USER = "currentUser";
		String CURRENT_USER_LEVEL = "level";
	}
	
	public interface DataStore {
		String TABLE_USER_INFO = "UserInfo";
		String TABLE_SITE_INFO = "SiteInfo";
	}
}
