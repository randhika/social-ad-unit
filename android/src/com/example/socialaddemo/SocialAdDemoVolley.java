package com.example.socialaddemo;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Initialised when the application instance is created.
 * Access Volley RequestQueue & ImageLoader globally from anywhere in the code.
 * @author Mani Selvaraj
 *
 */
public class SocialAdDemoVolley {

	private static RequestQueue mRequestQueue;

	static void init(Context context) {
		System.out.println("######## init Volley ######### ");
		mRequestQueue = Volley.newRequestQueue(context);
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
}
