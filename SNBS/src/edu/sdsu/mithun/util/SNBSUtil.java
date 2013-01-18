package edu.sdsu.mithun.util;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import edu.sdsu.mithun.R;
import edu.sdsu.mithun.database.DatabaseConstants;


public class SNBSUtil {
	public static final int FIRE_ALERT=4096;
	public static final int EARTHQUAKE_ALERT=4097;
	public static final int THUNDERSTORM_ALERT=4098;
	public static final int RIOT_ALERT=4099;
	public static final int UNKNOWN_ALERT=4100;
	public static final int[] alertId={FIRE_ALERT,EARTHQUAKE_ALERT,THUNDERSTORM_ALERT,RIOT_ALERT,UNKNOWN_ALERT};
	public static final String[] alertString=new String[]{"Fire Alert","Earthquake Alert","Thunderstorm Alert","Riot Alert","Alert E"};
	
	
	public static final int[] alertNotificationId=new int[]{1111,2222,3333,4444,5555};
	public static int message_counter=(int)(System.currentTimeMillis()%1000);
	public static String getAlertString(int id){
		for(int i=0;i<alertId.length;i++){
			if(id==alertId[i]){
				return alertString[i];
			}
		}
		return "no alert";
	}
	
	public static int getAlertIcon(int id){
		int icon;
		switch(id){
			case FIRE_ALERT: 
						icon=R.drawable.fire_icon;
						break;
			case EARTHQUAKE_ALERT:
						icon=R.drawable.earthquake_icon;
						break;
			case THUNDERSTORM_ALERT:
						icon=R.drawable.thunderstorm_icon;
						break;
			case RIOT_ALERT:
						icon=R.drawable.riot_icon;
						break;
	
			default: icon=R.drawable.icon;
						break;
		}
		return icon;
	}
	
	public static int getNotificationId(int id){
		for(int i=0;i<alertId.length;i++){
			if(id==alertId[i]){
				return alertNotificationId[i];
			}
		}
		return 0;
	}
	
	public static ContentValues getContentValues(Intent intent){
		Bundle extras = intent.getExtras();
		int alertId=extras.getInt(IntentStrings.ALERT_ID);
		int messageId=extras.getInt(IntentStrings.MESSAGE_ID);
		String messageBody=extras.getString(IntentStrings.MESSAGE_BODY);
		long receivedDate=extras.getLong(IntentStrings.RECEIVED_DATE);
		
		ContentValues values = new ContentValues();
        values.put(DatabaseConstants.ALERT_ID, alertId);
        values.put(DatabaseConstants.MESSAGE_BODY, messageBody);
        values.put(DatabaseConstants.RECEIVED_DATE, receivedDate);
        values.put(DatabaseConstants.MESSAGE_ID, messageId+(message_counter++));//for testing purpose added message_counter
        values.put(DatabaseConstants.READ, 0);
        return values;
	}

}
