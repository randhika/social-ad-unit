package com.example.socialaddemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SocialAdDemoApplication extends Application {

	private static Context applicationContext;
	private SharedPreferences sharedPreferences;
	
	public void onCreate() {
		super.onCreate();		
		applicationContext = this.getApplicationContext();
		SocialAdDemoVolley.init(applicationContext);
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferenceManager.initializePreferenceManager(sharedPreferences);
	}
	
    public static Context getContext() {
    	return applicationContext;
    }

}
