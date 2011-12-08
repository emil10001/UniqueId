package com.feigdev.uniqueid;

import java.security.SecureRandom;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;

public class UniqueId {
	private static final String TAG = "UniqueId";
	SharedPreferences app_preferences;
	SharedPreferences.Editor editor;
	Context context;
	final String UNIQUEID = "unique_id";
	
	/***
	 * Constructor for the class. This will initialize any null variables
	 *  
	 * @param context Need to pass in the current context, usually 'this'
	 */
	public UniqueId(Context context){
	
		this.context = context;
		app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		editor = app_preferences.edit();
	    if (!app_preferences.contains(UNIQUEID)){
	    	this.generateId();
	    }
	}
	
	public void generateId() {
		String value = "";
		app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (app_preferences.contains(UNIQUEID)){
	    	if (!app_preferences.getString(UNIQUEID, "").equals("")){
	    		return;
	    	}
	    }
		try {
			String id = Secure.ANDROID_ID;
			if (id != ""){
				value = UniqueId.computeHash(id);
			}
			else {
				SecureRandom random = new SecureRandom();
				value = UniqueId.computeHash(String.valueOf(random.nextInt((int)System.currentTimeMillis())));
			}
			editor.putString(UNIQUEID, value);
	    	editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void forceGenerateId(){
		String value = "";			
		app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		try {
			SecureRandom random = new SecureRandom();
			value = UniqueId.computeHash(String.valueOf(random.nextInt((int)System.currentTimeMillis())));
			editor.putString(UNIQUEID, value);
	    	editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void generateId(String username, String password){
		String value = "";
		app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		try {
			value = UniqueId.computeHash(username + password);
			editor.putString(UNIQUEID, value);
	    	editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getId() {
		app_preferences = PreferenceManager.getDefaultSharedPreferences(context);
		if (!app_preferences.contains(UNIQUEID)){
	    	generateId();
	    }
		else if (app_preferences.getString(UNIQUEID, "").equals("")){
    		generateId();
    	}
		return app_preferences.getString(UNIQUEID, "");
	}

    public static String computeHash(String x)   
    throws Exception  
    {
       java.security.MessageDigest d =null;
       d = java.security.MessageDigest.getInstance("SHA-1");
       d.reset();
       d.update(x.getBytes());
       byte[] b = d.digest();
    
       StringBuffer sb = new StringBuffer(b.length * 2);
       for (int i = 0; i < b.length; i++){
         int v = b[i] & 0xff;
         if (v < 16) {
           sb.append('0');
         }
         sb.append(Integer.toHexString(v));
       }
       return sb.toString().toUpperCase();
    }
	
}
