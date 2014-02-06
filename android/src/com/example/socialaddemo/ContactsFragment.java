package com.example.socialaddemo;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.socialaddemo.ServiceConfig.SERVICES;

public class ContactsFragment extends Fragment {

	TextView textView;
	ImageView imageView;
	Button createDevice;
	Button createAccount;
	Button createAppsInstalled;
	Button createContacts;
	Button fetchAd;
	Button fetchAd1;
	RequestQueue mVolleyQueue;
	ProgressDialog mProgressDialog;
	Context mContext;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		this.setHasOptionsMenu(true);
	}
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
    
        View v = inflater.inflate(R.layout.fragment_contact_layout, null);
        textView = (TextView) v.findViewById(R.id.text);
		createDevice = (Button) v.findViewById(R.id.createDevice);
		createAccount = (Button) v.findViewById(R.id.createAccount);
		createAppsInstalled = (Button) v.findViewById(R.id.createAppsinstalled);
		createContacts = (Button) v.findViewById(R.id.createContacts);
		fetchAd = (Button) v.findViewById(R.id.fetchAd);
		fetchAd1 = (Button) v.findViewById(R.id.fetchAd1);
		
		if(SharedPreferenceManager.getBoolean(SharedPreferenceManager.PreferenceKeys.IS_DEVICE_CREATED, false)) {
			createDevice.setEnabled(false);
		}

		if(SharedPreferenceManager.getBoolean(SharedPreferenceManager.PreferenceKeys.IS_ACCOUNT_CREATED, false)) {
			createAccount.setEnabled(false);
		}

		if(SharedPreferenceManager.getBoolean(SharedPreferenceManager.PreferenceKeys.IS_APPS_SENT, false)) {
			createAppsInstalled.setEnabled(false);
		}

		if(SharedPreferenceManager.getBoolean(SharedPreferenceManager.PreferenceKeys.IS_CONTACTS_SENT, false)) {
			createContacts.setEnabled(false);
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
				sendAppsInstalled();
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
				fetchAd(false);
			}
		});

		fetchAd1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				fetchAd(true);
			}
		}); 
		
		mContext = this.getActivity();
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage("Please wait");
		mVolleyQueue = SocialAdDemoVolley.getRequestQueue();
		//getAccounts();
		//Util.getAppsInstalled(this);
		//fetchContacts();
		//fetchContacts();
		return v;
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
			postBody.put("deviceId",Util.getAndroidDeviceId(mContext));
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
						SharedPreferenceManager.setBoolean(SharedPreferenceManager.PreferenceKeys.IS_DEVICE_CREATED, true);
						createDevice.setEnabled(false);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
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

		JSONObject postBody = Util.getAccounts(mContext);
		System.out.println("######## CreateDevice account body ######## "+postBody.toString());
		
		MyJsonRequest jsonRequest = new MyJsonRequest(Request.Method.POST, 
				url, 
				postBody,
				new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				System.out.println("######## createDevice SUCCESS ######### "+response.toString());
				SharedPreferenceManager.setBoolean(SharedPreferenceManager.PreferenceKeys.IS_ACCOUNT_CREATED, true);
				createAccount.setEnabled(false);
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
				final PackageManager packageManager = mContext.getPackageManager();
				List<PackageInfo> packagesInfo = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES | PackageManager.GET_UNINSTALLED_PACKAGES);
				
				for (PackageInfo pi : packagesInfo) {
					JSONObject appsJson = new JSONObject();
					JSONArray installedArray = new JSONArray();
					if (pi.packageName != null && pi.packageName.contains("com.android") == false &&
							pi.packageName.contains("com.htc") == false &&
							pi.packageName.equals("android") == false &&
							pi.packageName.contains("com.google.android") == false) {
						System.out.println("########## packageName ######## "+pi.packageName);
						System.out.println("########## name ######## "+packageManager.getApplicationLabel(pi.applicationInfo));
						
						try {
							Drawable icon = packageManager.getApplicationIcon(pi.applicationInfo);
						    Bitmap bitmap = ((BitmapDrawable)icon).getBitmap(); 
						    String bitmapString  = Util.BitMapToString(Util.downSize(bitmap,40));
							appsJson.put("name",packageManager.getApplicationLabel(pi.applicationInfo) );
							appsJson.put("package", pi.packageName);
							//appsJson.put("appicon", bitmapString);
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
							//wait(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				hideProgressDialog();
				SharedPreferenceManager.setBoolean(SharedPreferenceManager.PreferenceKeys.IS_APPS_SENT, true);
				ContactsFragment.this.getActivity().runOnUiThread(new Runnable() {
					public void run() {
						createAppsInstalled.setEnabled(false);
					}
				});
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
		
					ContentResolver contentResolver = mContext.getContentResolver();
		
					Cursor cursor = contentResolver.query(CONTENT_URI, null,
							ContactsContract.Contacts.IN_VISIBLE_GROUP + "=1", 
							null, null);	

					// Loop for every contact in the phone
					if (cursor.getCount() > 0) {
						System.out.println("########### total count ########## "+cursor.getCount());
						int count = 0;
						while (cursor.moveToNext()) {
							String bitmap = null;
							String phoneNumber = null;
							String email = null;

							JSONObject contactJson = new JSONObject();
							JSONArray contactsArray = new JSONArray();
							
							try {
							String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
							String name = cursor.getString(cursor.getColumnIndex( DISPLAY_NAME ));
							
							contactJson.put("contact_id",contact_id);
							contactJson.put("name",name);
							
							String phoneURI = cursor.getString(cursor.getColumnIndex( PHOTO_THUMBNAIL_URI ));
							String timesContacted = cursor.getString(cursor.getColumnIndex( NUMBER_OF_TIMES_CONACED ));
							System.out.println("###### COntact name ######### "+contact_id+" : "+name+" : " +phoneURI+" : "+timesContacted);
		
							if( phoneURI != null) {
								try {
									//Bitmap bm = Util.decodeUri(MainActivity.this,Uri.parse(phoneURI),50);
									//bitmap = Util.BitMapToString(bm);
									//contactJson.put("bitmap",bitmap);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							output.append("\n First Name:" + name); 
							int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
		
							if (hasPhoneNumber > 0) {
								try {
								// Query and loop for every phone number of the contact
								Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);
		
								while (phoneCursor.moveToNext()) {
									phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
									output.append("\n Phone number:" + phoneNumber);
								}
		
								contactJson.put("phonenumber",phoneNumber);
								phoneCursor.close();
								} catch ( Exception e) {
									e.printStackTrace();
								}
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
							//if( bitmap != null)
								//wait(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						} // End of While
						
						hideProgressDialog();
						SharedPreferenceManager.setBoolean(SharedPreferenceManager.PreferenceKeys.IS_CONTACTS_SENT, true);
						ContactsFragment.this.getActivity().runOnUiThread(new Runnable() {
							public void run() {
								createContacts.setEnabled(false);
							}
						});

					}		
				}
			}
		});
		contactsThread.start();
			
	}
	
	private void fetchAd(final boolean showAd1) {
		
		String deviceId = SharedPreferenceManager.getString(SharedPreferenceManager.PreferenceKeys.DEVICE_ID, "");
		if( deviceId.length() == 0) {
			showMessage("Invalid Deviceid found",false);
			return;
		}
		
		String url = ServiceConfig.getURL(SERVICES.GET_SOCIAL_AD)+"/?deviceId="+deviceId;
		
		MyStringRequest jsonRequest = new MyStringRequest(Request.Method.GET, 
				url, 
				null,
				new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				System.out.println("######## fetchAd SUCCESS ######### "+response.toString());
				//showMessage("Successfully sent Apps Installed",false);
				try {
					JSONArray responseArray = new JSONArray(response);
					if( showAd1 ) {
						showAd1(response);
					} else {
						showAd(response);
					}
				} catch ( Exception e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				//Ignore the error message. As this operation is carried out in background.
				System.out.println("######## fetchAd ERROR ######### "+error);
				showMessage("Some error happened",false);
			}
		});
		
		jsonRequest.setHeader(Constants.HTTP.ACCEPT, Constants.HTTP.APPLICATION_JSON);
		jsonRequest.setHeader(Constants.HTTP.CONTENT_TYPE, Constants.HTTP.APPLICATION_JSON);
		System.out.println("########## Sent GET fetchAd ########### ");
		mVolleyQueue.add(jsonRequest);	
	}
	
	AlertDialog dialog = null;
	private void showAd(String response) {

		try {
			JSONArray responseArray = new JSONArray(response);
			JSONObject resultJson = (JSONObject) responseArray.get(0);
			
			JSONObject appInstalledJson = resultJson.getJSONObject("appinstalled");
			final String appPackageName = appInstalledJson.getString("package");
			final String appName = appInstalledJson.getString("name");
		
			JSONObject contactJson = resultJson.getJSONObject("contact");
			String contactName = contactJson.getString("name");
			String contact_id = contactJson.getString("contact_id");
			String phoneURI = "content://com.android.contacts/contacts/"+contact_id+"/photo";
			
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		    LayoutInflater inflater = ContactsFragment.this.getActivity().getLayoutInflater();
		    View layout = inflater.inflate(R.layout.social_ad_layout, null);
		    String messageFormat = getResources().getString(R.string.social_ad_alert);  
		    String alertMsg = String.format(messageFormat, contactName, appName); 

		    TextView tx = (TextView) layout.findViewById(R.id.alert);
		    tx.setText(alertMsg);
		    tx.setTypeface(FontProvider.getHelveticaLightFont(mContext));
	    
		    ImageView profile = (RoundedImageView) layout.findViewById(R.id.friendicon);
		    
			Bitmap bm = Util.decodeUri(mContext,Uri.parse(phoneURI),50);
			profile.setImageBitmap(bm);
			
		    Button install = (Button) layout.findViewById(R.id.installnow);
		    install.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					try {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
					} catch (android.content.ActivityNotFoundException anfe) {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
					}
					dialog.dismiss();
				}
			});
	    		
		    ImageView close = (ImageView) layout.findViewById(R.id.close);
		    close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		    
		    builder.setView(layout);
		    
		    dialog = builder.create();
		    dialog.show();
		
		} catch ( Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void showAd1(String response) {
	
		try {
			JSONArray responseArray = new JSONArray(response);
			
			String contactName1 = null;
			String contactName2 = null;
			
			String phoneURI1 = null;
			String phoneURI2 = null;
			
			String contact_id1 = null;
			String contact_id2 = null;
			
			JSONObject resultJson = (JSONObject) responseArray.get(0);
			
			JSONObject appInstalledJson = resultJson.getJSONObject("appinstalled");
			final String appPackageName1 = appInstalledJson.getString("package");
			final String appName1 = appInstalledJson.getString("name");
		
			JSONObject contactJson = resultJson.getJSONObject("contact");
			contactName1 = contactJson.getString("name");
			contact_id1 = contactJson.getString("contact_id");
			phoneURI1 = "content://com.android.contacts/contacts/"+contact_id1+"/photo";

			resultJson = (JSONObject) responseArray.get(1);
			
			appInstalledJson = resultJson.getJSONObject("appinstalled");
			final String appPackageName2 = appInstalledJson.getString("package");
			final String appName2 = appInstalledJson.getString("name");
			
			contactJson = resultJson.getJSONObject("contact");
			contactName2 = contactJson.getString("name");
			contact_id2 = contactJson.getString("contact_id");
			phoneURI2 = "content://com.android.contacts/contacts/"+contact_id2+"/photo";

			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		    LayoutInflater inflater = ContactsFragment.this.getActivity().getLayoutInflater();
		    View layout = inflater.inflate(R.layout.social_ad_layout1, null);
		    String messageFormat = getResources().getString(R.string.social_ad_alert);  
		    
		    String alertMsg1 = String.format(messageFormat, contactName1, appName1); 
	
		    TextView tx = (TextView) layout.findViewById(R.id.alert);
		    tx.setText(alertMsg1);
		    tx.setTypeface(FontProvider.getHelveticaLightFont(mContext));
		    
		    ImageView profile = (ImageView) layout.findViewById(R.id.friendicon);
			Bitmap bm = Util.decodeUri(mContext,Uri.parse(phoneURI1),50);
			profile.setImageBitmap(bm);
			
		    Button install = (Button) layout.findViewById(R.id.installnow);
		    install.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					try {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName1)));
					} catch (android.content.ActivityNotFoundException anfe) {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName1)));
					}
					dialog.dismiss();
				}
			});
		    
		    TextView tx1 = (TextView) layout.findViewById(R.id.alert1);
		    String alertMsg2 = String.format(messageFormat, contactName2, appName2); 
		    tx1.setText(alertMsg2);
		    tx1.setTypeface(FontProvider.getHelveticaLightFont(mContext));
		    
		    ImageView profile1 = (ImageView) layout.findViewById(R.id.friendicon1);
			bm = Util.decodeUri(mContext,Uri.parse(phoneURI2),50);
			profile1.setImageBitmap(bm);
			
		    install = (Button) layout.findViewById(R.id.installnow1);
		    install.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					
					try {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName2)));
					} catch (android.content.ActivityNotFoundException anfe) {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName2)));
					}
					dialog.dismiss();
				}
			});
	    		
		    /*ImageView close = (ImageView) layout.findViewById(R.id.close);
		    close.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});*/
		    
		    builder.setView(layout);		    
		    dialog = builder.create();
		    dialog.show();
		
		} catch ( Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	public void onDestroy() {
		super.onDestroy();
		if(appsInstalledThread != null) {
			appsInstalledThread.interrupt();
			//appsInstalledThread.destroy();
		}
		
		if(contactsThread != null) {
			contactsThread.interrupt();
			//contactsThread.destroy();
		}
	}
	
	public void showMessage(String message, boolean displayedByLongTime) {
		if (displayedByLongTime) {
			Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.show();
		} else {
			Toast toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.BOTTOM, 0, 0);
			toast.show();
		}
	}      
}
