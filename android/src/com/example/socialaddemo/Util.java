package com.example.socialaddemo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

public class Util {

	public static JSONObject getAccounts(Context context) {
		AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
		Account[] list = manager.getAccounts();

		//mani.siddharth13@gmail.com:com.google
		//smanikandan14@gmail.com:com.google
		//13202008820:com.whatsapp
		//smani14:com.skype.contacts.sync
		//smanikandan14:com.twitter.android.auth.login
		
	
		JSONArray accountsArray = new JSONArray();
		
		for(Account account : list ) {
			System.out.println("######## Account name is ####### "+account.name+":"+account.type);
			JSONObject accountJson = new JSONObject();
			try {
				accountJson.put("name", account.name);
				boolean canAdd = false;
				if(account.type.contains("com.google")) {
					accountJson.put("type", "gmail");
					canAdd = true;
				} else if(account.type.contains("com.whatsapp")) {
					accountJson.put("type", "whatsapp");
					canAdd = true;
				} else if(account.type.contains("com.skype")) {
					accountJson.put("type", "skype");
					canAdd = true;
				} else if(account.type.contains("com.twitter")) {
					accountJson.put("type", "twitter");
					canAdd = true;
				}
				
				if(canAdd) {
					accountsArray.put(accountJson);
				}
				
			} catch (JSONException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		}
		
		JSONObject reqBody = new JSONObject();
		try {
			String deviceId = SharedPreferenceManager.getString(SharedPreferenceManager.PreferenceKeys.DEVICE_ID, "");
			reqBody.put("deviceId",deviceId);
			reqBody.put("accounts",accountsArray);
		} catch (Exception e ) {
			e.printStackTrace();
		}
		
		System.out.println("######## ReqBoduy is ###### "+reqBody.toString());
		return reqBody;
	}
	
	public static List<JSONObject> getAppsInstalled(Context context) {
		
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> packagesInfo = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_UNINSTALLED_PACKAGES);
		List<JSONObject> list = new ArrayList<JSONObject>();
		JSONArray installedArray = new JSONArray();
		for (PackageInfo pi : packagesInfo) {
			JSONObject appsJson = new JSONObject();
			if (pi.packageName != null && pi.packageName.contains("com.android") == false &&
					pi.packageName.contains("com.google.android") == false) {
				System.out.println("########## packageName ######## "+pi.packageName);
				System.out.println("########## name ######## "+packageManager.getApplicationLabel(pi.applicationInfo));
				try {
					Drawable icon = packageManager.getApplicationIcon(pi.applicationInfo);
				    Bitmap bitmap = ((BitmapDrawable)icon).getBitmap(); 
				    String bitmapString  = BitMapToString(bitmap);
					appsJson.put("name",packageManager.getApplicationLabel(pi.applicationInfo) );
					appsJson.put("package", pi.packageName);
					appsJson.put("appicon", bitmapString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				installedArray.put(appsJson);

				JSONObject reqBody = new JSONObject();
				try {
					String deviceId = SharedPreferenceManager.getString(SharedPreferenceManager.PreferenceKeys.DEVICE_ID, "");
					reqBody.put("deviceId",deviceId);
					reqBody.put("installed",installedArray);
				} catch (Exception e ) {
					e.printStackTrace();
				}
				list.add(reqBody);
				break;
			}
		}
		
		return list;
		
	}
	
	public static String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
	}
	
	public static Bitmap StringToBitMap(String encodedString){
	     try{
	       byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
	       Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	       return bitmap;
	     }catch(Exception e){
	       e.getMessage();
	       return null;
	     }
	}	
	
	public static Bitmap decodeUri(Context context, Uri selectedImage,int reqSize) throws FileNotFoundException {
		// Decode image size
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);

		// Find the correct scale value. It should be the power of 2.
		int width_tmp = options.outWidth, height_tmp = options.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < reqSize
					|| height_tmp / 2 < reqSize) {
				break;
			}
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		// Decode with inSampleSize
		options = new BitmapFactory.Options();
		options.inSampleSize = scale;
		return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, options);
	}

	public static Bitmap downSize(Bitmap bm,int reqSize) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) reqSize) / width;
	    float scaleHeight = ((float) reqSize) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;		
	}
	
	public static String getAndroidDeviceId(Context context) {
		String deviceId = null;
		try {
			// Try to get the ANDROID_DEVICE_ID
			deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
			Log.d("Util", "No permissions to get device id, continuing... " + e.getMessage());
		}
		if (deviceId != null) {
			Log.d("Util","Found an AndroidDeviceId (IMEI): " + deviceId);
		} else {
			Log.d("util","Could not retrieve an AndroidDeviceId (IMEI): " + deviceId);
		}
		return deviceId;
	}
}
