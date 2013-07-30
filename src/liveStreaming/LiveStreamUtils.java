package liveStreaming;

public class LiveStreamUtils {

//	public static final String REMOTE_HOST = "http://appablews.jelastic.tsukaeru.net";
	public static final String REMOTE_HOST = "192.168.1.65";
	
	public static final String LAN_ENV = "http://192.168.1.65:8080/webservice/";
	public static final String LIVE_ENV = "http://appablews.jelastic.tsukaeru.net/appablews/";
	
	public static String URLSERVICE = LAN_ENV;
	
	public static String SIGNIN_API = URLSERVICE + "signin";
	public static String SIGNOUT_API = URLSERVICE + "signout";
	public static String GETUSER_API = URLSERVICE + "getusers";
	
	public static String LIVEVOICE_API = URLSERVICE + "livevoice";
	public static String STOPLS_API = URLSERVICE + "stopls";
}
