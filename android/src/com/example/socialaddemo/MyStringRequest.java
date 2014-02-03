package com.example.socialaddemo;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

/**
 * Custom Implementation of com.android.volley.toolbox.StringRequest 
 * to handle the headers.
 * @author Mani Selvaraj
 *
 */

public class MyStringRequest extends StringRequest{

	private Map<String, String> headers = new HashMap<String, String>();
	
	public MyStringRequest(int method, String url, String requestBody,Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, requestBody, listener, errorListener);
	}
	
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return headers;
	}
	
	public void setHeader(String title, String content) {
		 headers.put(title, content);
	}

}
