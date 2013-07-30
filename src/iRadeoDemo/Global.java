package iRadeoDemo;

public class Global {

	public static final String iRADEO_APIKEY = 
			"a8f7608247a46a672b4f6523267e8f0c";
	public static final String USER_NAME = "livyhoang";
	public static final String PASS_WORD = "appable2013";
	public static final String SERVER_URL = 
			"www.iradeo.com/api/";
	public static final String HTTP_SCHEME = 
			"http://";
	
	public static final String LOGIN_API = 
			HTTP_SCHEME + SERVER_URL + "audio/list?api_key=%sc&player=%s";
	
	public static final String PLAYERLIST_API = 
			HTTP_SCHEME + SERVER_URL + "players/list?api_key=%s";
	
	public static final String AUDIOLIST_API = 
			HTTP_SCHEME + SERVER_URL + "audio/list?api_key=%s&player=%s";
	
	public static final String AUDIOUPLOAD_API = 
			HTTP_SCHEME + SERVER_URL + "audio/upload";
	
	public static final String SIGNUP_API = 
			HTTP_SCHEME + SERVER_URL + "api/sign_up";
			
	public static final String AUDIODELETE_API = 
			HTTP_SCHEME + SERVER_URL + "audio/delete";
}
