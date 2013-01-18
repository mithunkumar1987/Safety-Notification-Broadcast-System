package edu.sdsu.mithun.transaction;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import edu.sdsu.mithun.database.DatabaseConstants;
import edu.sdsu.mithun.database.SnbsDatabaseHelper;
import edu.sdsu.mithun.util.DateTimeFormater;
import edu.sdsu.mithun.util.IntentStrings;
import edu.sdsu.mithun.util.Preferences;
import edu.sdsu.mithun.util.SNBSUtil;

public class SNBSReceiverService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("SNBS","came into onCreate of SNBSReceiverService");
		
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Bundle extras = intent.getExtras();
		
		int alertId=extras.getInt(IntentStrings.ALERT_ID);
		int messageId=extras.getInt(IntentStrings.MESSAGE_ID);
		String messageBody=extras.getString(IntentStrings.MESSAGE_BODY);
		long receivedDate=extras.getLong(IntentStrings.RECEIVED_DATE);
		Log.d("SNBS","came into SNBS Receiver Service->on start");
	 	Log.d("SNBS","alertId "+alertId);
	 	Log.d("SNBS","messageId "+messageId);
	 	Log.d("SNBS","messageBody "+messageBody);
	 	Log.d("SNBS","receivedDate "+DateTimeFormater.parseTimeInMillisToString(receivedDate));
	 	
	 	SharedPreferences mPrefsSNBSAlertCategory = PreferenceManager
		.getDefaultSharedPreferences(this);
	 	boolean fireAlertBool = mPrefsSNBSAlertCategory.getBoolean("pref_key_snbs_fire_alert", true);
	 	boolean earthquakeAlertBool = mPrefsSNBSAlertCategory.getBoolean("pref_key_snbs_earthquake_alert", true);
	 	boolean thunderstormAlertBool = mPrefsSNBSAlertCategory.getBoolean("pref_key_snbs_thunderstorm_alert", true);
	 	boolean riotAlertBool = mPrefsSNBSAlertCategory.getBoolean("pref_key_snbs_riot_alert", true);




	 	if (!fireAlertBool && (alertId == SNBSUtil.FIRE_ALERT)) {
	 		Log.d("SNBS","fire alert not enabled");
	 		return;
	 	} else if (!earthquakeAlertBool && (alertId == SNBSUtil.EARTHQUAKE_ALERT)) {
	 		Log.d("SNBS","earthquake alert not enabled");
	 		return;
		} else if (!thunderstormAlertBool&& (alertId == SNBSUtil.THUNDERSTORM_ALERT)) {
			Log.d("SNBS","thunderstorm alert not enabled");
			return;
		} else if (!riotAlertBool && (alertId == SNBSUtil.RIOT_ALERT)) {
			Log.d("SNBS","riot alert not enabled");
			return;//else if (!snbsAlertTest && (tempSms.getSNBSClass() == "Test Alert")) {
		}
	 	
	 	
	 	insertIntoDatabase(intent);
	 	if(Preferences.isSnbsNotificationEnabled(getBaseContext())){
	 		startNotification(getBaseContext(), extras);
	 	}
	 	popUpSnbsMessage(getBaseContext(), extras);
	}
	
	private void startNotification(Context context,Bundle bundleValue){
   	Log.d("SNBS","came into startNotification()");
        Intent intent = new Intent(context, SNBSNotification.class)
                .putExtra(IntentStrings.INTENT_BUNDLE, bundleValue);
        context.startService(intent);
    }
	
	private void insertIntoDatabase(Intent intent){
		ContentValues values=SNBSUtil.getContentValues(intent);
		SQLiteDatabase db=SnbsDatabaseHelper.getDatabaseInstance(getBaseContext()).getWritableDatabase();
		 Log.d("SNBS","now calling insert");
		 db.insert(DatabaseConstants.MAIN_TABLE_NAME, null, values);
	}
	
    private void popUpSnbsMessage(Context context, Bundle bundleValue) {
   	 System.out.println("came into displayCmasMessage()");
        Intent intent = new Intent(context, SnbsPopUpManager.class)
        			.putExtra(IntentStrings.INTENT_BUNDLE, bundleValue);
        context.startService(intent);

    
    }

}
