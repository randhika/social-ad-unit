package com.example.socialaddemo;

import android.content.SharedPreferences;

public class SharedPreferenceManager {
	private static SharedPreferences preferences;

	public static class PreferenceKeys {
		public static String FACEBOOK_ID = "fb_id";
		public static String IS_DEVICE_CREATED = "is_device_created";
		public static String IS_ACCOUNT_CREATED = "is_account_created";
		public static String IS_APPS_SENT = "is_apps_sent";
		public static String IS_CONTACTS_SENT = "is_contacts_sent";
		public static String DEVICE_ID = "device_id";
		
		public static String IS_DEVICE_CREATED1 = "is_device_created1";
		public static String IS_ACCOUNT_CREATED1 = "is_account_created1";
		public static String IS_APPS_SENT1 = "is_apps_sent1";
		public static String IS_CONTACTS_SENT1 = "is_contacts_sent1";
		public static String DEVICE_ID1 = "device_id1";
		
	}

	public static void initializePreferenceManager(SharedPreferences _preferences) {
		preferences = _preferences;
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return preferences.getBoolean(key, defaultValue);
	}
	public static void setBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static String getString(String key,String defaultValue) {
		return preferences.getString(key,defaultValue);
	}
	public static void setString(String key, String value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

}
