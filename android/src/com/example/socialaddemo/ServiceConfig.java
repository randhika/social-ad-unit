package com.example.socialaddemo;

public class ServiceConfig {

	public enum SERVICES {
		CREATE_DEVICES,
		CREATE_ACCOUNT,
		CREATE_APPSINSTALLED,
		CREATE_CONTACTS,
		GET_SOCIAL_AD
	}

	// Check the IP address of the local server machine
	private static final String PUBLIC_BETA_IP = "10.12.22.68:3000";

	//Change the values accordingly for test server and production server.
	private static final String PUBLIC_SERVER_IP = "ec2-54-242-153-20.compute-1.amazonaws.com:3000";

	private static class URL {

		// Test Development Server
		public static final String CREATE_DEVICE_API = "http://"+PUBLIC_BETA_IP+"/device";
		public static final String CREATE_ACCOUNT_API = "http://"+PUBLIC_BETA_IP+"/account";
		public static final String CREATE_APPS_INSTALLED_API = "http://"+PUBLIC_BETA_IP+"/appsinstalled";
		public static final String CREATE_CONTACTS_API = "http://"+PUBLIC_BETA_IP+"/contacts";
		public static final String FETCH_SOCIAL_AD_API = "http://"+PUBLIC_BETA_IP+"/fetch_social_ad"; 
		
		// Production Server
//		public static final String CREATE_DEVICE_API = "http://"+PUBLIC_SERVER_IP+"/device";
//		public static final String CREATE_ACCOUNT_API = "http://"+PUBLIC_SERVER_IP+"/account";
//		public static final String CREATE_APPS_INSTALLED_API = "http://"+PUBLIC_SERVER_IP+"/appsinstalled";
//		public static final String CREATE_CONTACTS_API = "http://"+PUBLIC_SERVER_IP+"/contacts";
//		public static final String FETCH_SOCIAL_AD_API = "http://"+PUBLIC_SERVER_IP+"/fetch_social_ad";  
	}

	public static String getURL(SERVICES services) {
		String url = "";
		switch (services) {
		case CREATE_DEVICES:
			url = URL.CREATE_DEVICE_API;
			break;
		case CREATE_ACCOUNT:
			url = URL.CREATE_ACCOUNT_API;
			break;
		case CREATE_APPSINSTALLED:
			url = URL.CREATE_APPS_INSTALLED_API;
			break;
		case CREATE_CONTACTS:
			url = URL.CREATE_CONTACTS_API;
			break;
		case GET_SOCIAL_AD:
			url = URL.FETCH_SOCIAL_AD_API;
			break;
		}
		return url;
	}

}
