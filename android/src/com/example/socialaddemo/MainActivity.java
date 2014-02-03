package com.example.socialaddemo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialaddemo.ServiceConfig.SERVICES;

public class MainActivity extends Activity {
	
	TextView textView;
	ImageView imageView;
	Button createDevice;
	Button createAccount;
	Button createAppsInstalled;
	Button createContacts;
	Button fetchAd;
	RequestQueue mVolleyQueue;
	ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.text);
		
		createDevice = (Button) findViewById(R.id.createDevice);
		createAccount = (Button) findViewById(R.id.createAccount);
		createAppsInstalled = (Button) findViewById(R.id.createAppsinstalled);
		createContacts = (Button) findViewById(R.id.createContacts);
		fetchAd = (Button) findViewById(R.id.fetchAd);
		
		if(SharedPreferenceManager.getBoolean(SharedPreferenceManager.PreferenceKeys.IS_DEVICE_CREATED, false)) {
			createDevice.setEnabled(false);
		}

		if(SharedPreferenceManager.getBoolean(SharedPreferenceManager.PreferenceKeys.IS_ACCOUNT_CREATED, false)) {
			createAccount.setEnabled(false);
		}
		
		createDevice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createDevice();
			}
		});
		
		createAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createAccount();
			}
		});
		
		createAppsInstalled.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//sendAppsInstalled();
				createAccount();
			}
		});
		
		createContacts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchContacts();
			}
		});
		
		fetchAd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchAd();
			}
		});
		
		mProgressDialog = new ProgressDialog(this);
		mVolleyQueue = SocialAdDemoVolley.getRequestQueue();
		//getAccounts();
		//Util.getAppsInstalled(this);
		//fetchContacts();
		//fetchContacts();
	}
	
	
	private void showProgressDialog() {
		mProgressDialog.show();
	}

	private void hideProgressDialog() {
		mProgressDialog.dismiss();
	}
	
	public void createDevice() {
		showProgressDialog();
		String url = ServiceConfig.getURL(SERVICES.CREATE_DEVICES);

		JSONObject postBody = new JSONObject();
		try {
			postBody.put("deviceId",Util.getAndroidDeviceId(this));
			postBody.put("os","android");
		} catch (Exception e) { 
			postBody = null;
		}
		
		System.out.println("######## CreateDevice request body ######## "+postBody.toString());
		
		MyJsonRequest jsonRequest = new MyJsonRequest(Request.Method.POST, 
				url, 
				postBody,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				System.out.println("######## createDevice SUCCESS ######### "+response.toString());
				try {
					JSONObject responseJson = new JSONObject(response.toString());
					if( responseJson.has("id")) {
						String deviceId = responseJson.getString("id");
						SharedPreferenceManager.setString(SharedPreferenceManager.PreferenceKeys.DEVICE_ID, deviceId);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				SharedPreferenceManager.setBoolean(SharedPreferenceManager.PreferenceKeys.IS_DEVICE_CREATED, true);
				hideProgressDialog();
				showMessage("Device Successfully Created ",false);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Ignore the error message. As this operation is carried out in background.
				System.out.println("######## createDevice ERROR ######### "+error);
				mProgressDialog.dismiss();
				showMessage("Some error happened",false);
			}
		});
		
		jsonRequest.setHeader(Constants.HTTP.ACCEPT, Constants.HTTP.APPLICATION_JSON);
		jsonRequest.setHeader(Constants.HTTP.CONTENT_TYPE, Constants.HTTP.APPLICATION_JSON);
		mVolleyQueue.add(jsonRequest);
	}

	public void createAccount() {
		showProgressDialog();
		String url = ServiceConfig.getURL(SERVICES.CREATE_ACCOUNT);

		JSONObject postBody = Util.getAccounts( this);
		System.out.println("######## CreateDevice account body ######## "+postBody.toString());
		
		MyJsonRequest jsonRequest = new MyJsonRequest(Request.Method.POST, 
				url, 
				postBody,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				System.out.println("######## createDevice SUCCESS ######### "+response.toString());
				SharedPreferenceManager.setBoolean(SharedPreferenceManager.PreferenceKeys.IS_ACCOUNT_CREATED, true);
				hideProgressDialog();
				showMessage("Account Successfully Created ",false);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Ignore the error message. As this operation is carried out in background.
				System.out.println("######## createDevice ERROR ######### "+error);
				mProgressDialog.dismiss();
				showMessage("Some error happened",false);
			}
		});
		
		jsonRequest.setHeader(Constants.HTTP.ACCEPT, Constants.HTTP.APPLICATION_JSON);
		jsonRequest.setHeader(Constants.HTTP.CONTENT_TYPE, Constants.HTTP.APPLICATION_JSON);
		mVolleyQueue.add(jsonRequest);
	}
	
	Thread appsInstalledThread = null;
	
	private void sendAppsInstalled() {
		showProgressDialog();
		appsInstalledThread = new Thread(new Runnable() {
			public void run() {
				synchronized (this) {
				final PackageManager packageManager = MainActivity.this.getPackageManager();
				List<PackageInfo> packagesInfo = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_UNINSTALLED_PACKAGES);
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
						    String bitmapString  = Util.BitMapToString(Util.downSize(bitmap,40));
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
						
						String url = ServiceConfig.getURL(SERVICES.CREATE_APPSINSTALLED);
						//System.out.println("######## CreateDevice appsInstalled ######## "+reqBody.toString());
						MyJsonRequest jsonRequest = new MyJsonRequest(Request.Method.POST, 
								url, 
								reqBody,
								new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								System.out.println("######## Send appsInstalled SUCCESS ######### "+response.toString());
								//showMessage("Successfully sent Apps Installed",false);
								//Thread.currentThread().notify();
								//lock.notify();
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								//Ignore the error message. As this operation is carried out in background.
								System.out.println("######## send appsInstalled ERROR ######### "+error);
								//Thread.currentThread().notify();
								//lock.notify();
								showMessage("Some error happened",false);
							}
						});
						
						jsonRequest.setHeader(Constants.HTTP.ACCEPT, Constants.HTTP.APPLICATION_JSON);
						jsonRequest.setHeader(Constants.HTTP.CONTENT_TYPE, Constants.HTTP.APPLICATION_JSON);
						mVolleyQueue.add(jsonRequest);		
						try {
							System.out.println("######## Thread is waiting ######## ");
							wait(3000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				hideProgressDialog();
				}
			}
		});
		appsInstalledThread.start();
	}
	
	Thread contactsThread = null;
	public void fetchContacts() {
		showProgressDialog();
		contactsThread = new Thread(new Runnable () {
			public void run() {
				synchronized (this) {
		
					Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
					String _ID = ContactsContract.Contacts._ID;
					String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
					String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
					String NUMBER_OF_TIMES_CONACED = ContactsContract.Contacts.TIMES_CONTACTED;
					String PHOTO_THUMBNAIL_URI = ContactsContract.Contacts.PHOTO_THUMBNAIL_URI;
					
					Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
					String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
					String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
		
					Uri EmailCONTENT_URI =  ContactsContract.CommonDataKinds.Email.CONTENT_URI;
					String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
					String DATA = ContactsContract.CommonDataKinds.Email.DATA;
		
					StringBuffer output = new StringBuffer();
		
					ContentResolver contentResolver = getContentResolver();
		
					Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);	
		
					boolean canBreak = false;

					// Loop for every contact in the phone
					if (cursor.getCount() > 0) {
		
						while (cursor.moveToNext()) {
							String bitmap = null;
							String phoneNumber = null;
							String email = null;

							JSONObject contactJson = new JSONObject();
							JSONArray contactsArray = new JSONArray();
							
							try {
							String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
							String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
							
							contactJson.put("name",name);
							
							String phoneURI = cursor.getString(cursor.getColumnIndex( PHOTO_THUMBNAIL_URI ));
							String timesContacted = cursor.getString(cursor.getColumnIndex( NUMBER_OF_TIMES_CONACED ));
							System.out.println("###### COntact name ######### "+name+" : " +phoneURI+" : "+timesContacted);
		
							if( phoneURI != null) {
								try {
									Bitmap bm = Util.decodeUri(MainActivity.this,Uri.parse(phoneURI),50);
									bitmap = Util.BitMapToString(bm);
									contactJson.put("bitmap",bitmap);
									//contactJson.put("bitmap","");
								} catch (Exception e) {
									e.printStackTrace();
								}
								canBreak = true;
							}
							output.append("\n First Name:" + name); 
							int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
		
							if (hasPhoneNumber > 0) {
								// Query and loop for every phone number of the contact
								Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
		
								while (phoneCursor.moveToNext()) {
									phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
									output.append("\n Phone number:" + phoneNumber);
								}
		
								contactJson.put("phonenumber",phoneNumber);
								phoneCursor.close();
							}
							
							// Query and loop for every email of the contact
							Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID+ " = ?", new String[] { contact_id }, null);
							while (emailCursor.moveToNext()) {
								email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
								output.append("\nEmail:" + email);
								System.out.println("########## Email ########## "+email);
							}
							contactJson.put("email",email);
							emailCursor.close();
							output.append("\n");
							
							Cursor cur = contentResolver.query(ContactsContract.Data.CONTENT_URI,
															null, CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[] { contact_id }, null);
							
							int indexPhoneType = cur.getColumnIndexOrThrow(Phone.TYPE);
							
							while (cur.moveToNext()) {
							    String phoneType =  cur.getString(indexPhoneType);
							    int type = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
						        String imName = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA));
						        
						        boolean canbreak = false;
						        switch (type) {
							        case ContactsContract.CommonDataKinds.Im.PROTOCOL_SKYPE:
							            System.out.println(" ###### Skype ######## contactID: " + contact_id + " type: " + type
							                    + " imName: " + imName);
							            phoneType = "skype";
							            canbreak = true;
							            break;
							        case ContactsContract.CommonDataKinds.Im.PROTOCOL_GOOGLE_TALK:
							            System.out.println(" ######## Gtalk ######### contactID: " + contact_id + " type: " + type
							                    + " imName: " + imName);
							            phoneType = "gtalk";
							            canbreak = true;
							            break;
							        default:
							            //System.out.println("Other numbers: " + imName);
							            break;
						        }
						        
						        if( phoneType!= null && phoneType.toLowerCase().equals("com.google")) {
						        	phoneType = "google+";
						        	canbreak = true;
						        } else if(phoneType != null && phoneType.toLowerCase().equals("whatsapp")) {
						        	phoneType = "whatsapp";
						        	canbreak = true;
						        }
						        contactJson.put("type",phoneType);
						        contactJson.put("imname",imName);
						        
						        if(canbreak)
						        	break;
						        
							}
							
							contactsArray.put(contactJson);
							
						} catch ( Exception e) {
							e.printStackTrace();
						}
							
						JSONObject reqBody = new JSONObject();
						try {
							String deviceId = SharedPreferenceManager.getString(SharedPreferenceManager.PreferenceKeys.DEVICE_ID, "");
							reqBody.put("deviceId",deviceId);
							reqBody.put("contacts",contactsArray);
						} catch (Exception e ) {
							e.printStackTrace();
						}
						
						System.out.println("########### JSON reqbody is ########## "+reqBody.toString());
						
						String url = ServiceConfig.getURL(SERVICES.CREATE_CONTACTS);
						//System.out.println("######## CreateDevice appsInstalled ######## "+reqBody.toString());
						MyJsonRequest jsonRequest = new MyJsonRequest(Request.Method.POST, 
								url, 
								reqBody,
								new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
								System.out.println("######## Send appsInstalled SUCCESS ######### "+response.toString());
								//showMessage("Successfully sent Apps Installed",false);
								//Thread.currentThread().notify();

								//lock.notify();
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								//Ignore the error message. As this operation is carried out in background.
								System.out.println("######## send appsInstalled ERROR ######### "+error);
								//Thread.currentThread().notify();
								//lock.notify();
								showMessage("Some error happened",false);
							}
						});
						
						jsonRequest.setHeader(Constants.HTTP.ACCEPT, Constants.HTTP.APPLICATION_JSON);
						jsonRequest.setHeader(Constants.HTTP.CONTENT_TYPE, Constants.HTTP.APPLICATION_JSON);
						mVolleyQueue.add(jsonRequest);		
						try {
							System.out.println("######## Thread is waiting ######## ");
							if( bitmap != null)
								wait(2000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						} // End of While
						
						hideProgressDialog();
					}		
				}
			}
		});
		contactsThread.start();
			
	}
	
	private void fetchAd() {
		
		String url = ServiceConfig.getURL(SERVICES.GET_SOCIAL_AD);
		
		JSONObject reqBody = new JSONObject();
		String deviceId = SharedPreferenceManager.getString(SharedPreferenceManager.PreferenceKeys.DEVICE_ID, "");
		if( deviceId.length() == 0) {
			showMessage("Invalid Deviceid found",false);
			return;
		}
		
		try {
			reqBody.put("deviceId",deviceId);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		
		MyJsonRequest jsonRequest = new MyJsonRequest(Request.Method.GET, 
				url, 
				reqBody,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				System.out.println("######## Send appsInstalled SUCCESS ######### "+response.toString());
				//showMessage("Successfully sent Apps Installed",false);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Ignore the error message. As this operation is carried out in background.
				System.out.println("######## send appsInstalled ERROR ######### "+error);
				showMessage("Some error happened",false);
			}
		});
		
		jsonRequest.setHeader(Constants.HTTP.ACCEPT, Constants.HTTP.APPLICATION_JSON);
		jsonRequest.setHeader(Constants.HTTP.CONTENT_TYPE, Constants.HTTP.APPLICATION_JSON);
		mVolleyQueue.add(jsonRequest);	
	}
	
	private void showAd() {
		
		
		
	}
	
	public void onDestroy() {
		super.onDestroy();
		if(appsInstalledThread != null) {
			appsInstalledThread.interrupt();
			appsInstalledThread.destroy();
		}
		
		if(contactsThread != null) {
			contactsThread.interrupt();
			contactsThread.destroy();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showMessage(String message, boolean displayedByLongTime) {
		if (displayedByLongTime) {
			Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.show();
		} else {
			Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.show();
		}
	}

}
