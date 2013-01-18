package edu.sdsu.mithun.util;
 
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.sdsu.mithun.R;
 
public class Preferences extends PreferenceActivity {
	 String ListPreference;
     String ringtonePreference;
     String secondEditTextPreference;
     String customPref;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.pref);
        }
 
        private void getPrefs() {
                // Get the xml/preferences.xml preferences
                SharedPreferences prefs = PreferenceManager
                                .getDefaultSharedPreferences(getBaseContext());
                
                ringtonePreference = prefs.getString("pref_key_ringtone",
                                "DEFAULT_RINGTONE_URI");
                secondEditTextPreference = prefs.getString("SecondEditTextPref",
                                "Nothing has been entered");
                // Get the custom preference
                SharedPreferences mySharedPreferences = getSharedPreferences(
                                "myCustomSharedPrefs", Activity.MODE_PRIVATE);
                customPref = mySharedPreferences.getString("myCusomPref", "");
        }
        
        public static boolean isSnbsNotificationEnabled(Context context){
        	SharedPreferences mPrefsSNBSNotification = PreferenceManager
            .getDefaultSharedPreferences(context);
        	boolean snbsNotification = mPrefsSNBSNotification.getBoolean(
                    "pref_key_enable_notifications", false);
        	System.out.println("snbs notification enabled = "+snbsNotification);
        	return snbsNotification;
        }
        public static boolean isSnbsPreferenceEnabled(Context context,String snbsClass){
            SharedPreferences mPrefsSNBSAlertCategory = PreferenceManager.getDefaultSharedPreferences(context);
            boolean snbsAlertFire = mPrefsSNBSAlertCategory.getBoolean(
                    "pref_key_snbs_fire_alert", false);
            boolean snbsAlertEarthquake = mPrefsSNBSAlertCategory.getBoolean(
                    "pref_key_snbs_earthquake_alert", false);
            boolean snbsAlertThunderstrom = mPrefsSNBSAlertCategory.getBoolean(
                    "pref_key_snbs_thunderstrom_alert", false);
            boolean snbsAlertRoit = mPrefsSNBSAlertCategory.getBoolean(
                    "pref_key_snbs_roit_alert", false);
              if (snbsAlertEarthquake && (snbsClass == "Earthquake Alert")) { 
                return true;
                  } else if (snbsAlertThunderstrom && (snbsClass == "Thunderstrom Alert")) { 
                return true;
            } else if (snbsAlertRoit && (snbsClass == "ROIT Alert")) {
                return true;
            } else if(snbsAlertFire && snbsClass == "Fire Alert"){
            	return true;
            }
            
              System.out.println(snbsClass+" not enabled");
       	 return false;
        }
        
        public static boolean canVibrate(Context context){
        	
        	SharedPreferences mPrefsSNBSNotification = PreferenceManager
            .getDefaultSharedPreferences(context);
        	String snbsVibrate = mPrefsSNBSNotification.getString(
                    "pref_key_vibrateWhen", null);
        	if(snbsVibrate.equalsIgnoreCase("Always")){
        		return true;
        	}else if(snbsVibrate.equalsIgnoreCase("Never")){
        		return false;
        	}else{
	        	AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	
	        	switch (am.getRingerMode()) {
	        	    case AudioManager.RINGER_MODE_SILENT:
	        	    case AudioManager.RINGER_MODE_VIBRATE:
	        	    	Log.i("MyApp","Silent mode");
	        	        if(snbsVibrate.equalsIgnoreCase("silent")){
	        	        	return true;
	        	        }else{
	        	        	return false;
	        	        }

	        	    case AudioManager.RINGER_MODE_NORMAL:
	        	        Log.i("MyApp","Normal mode");
	        	        return false;
	        	}
        	}
        	return true;
        }
}