package com.example.socialaddemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

/**
 * Initialised when the application instance is created.
 * Access Volley RequestQueue & ImageLoader globally from anywhere in the code.
 * @author Mani Selvaraj
 *
 */
public class SocialAdDemoVolley {

	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	private static BitmapLruCache mBitmapCache;

	static void init(Context context) {
		System.out.println("######## init Volley ######### ");
		mRequestQueue = Volley.newRequestQueue(context);
		long size = Runtime.getRuntime().maxMemory()/4;
		mBitmapCache = new BitmapLruCache((int)size);

		mImageLoader = new ImageLoader(mRequestQueue, mBitmapCache);
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		} else {
			throw new IllegalStateException("RequestQueue not initialized");
		}
	}
	
	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		} else {
			throw new IllegalStateException("ImageLoader not initialized");
		}
	}

}
