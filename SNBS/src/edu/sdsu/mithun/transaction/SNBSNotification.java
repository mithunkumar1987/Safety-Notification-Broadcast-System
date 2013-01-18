package edu.sdsu.mithun.transaction;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.ListView;
import edu.sdsu.mithun.R;
import edu.sdsu.mithun.ui.ThreadListView;
import edu.sdsu.mithun.util.IntentStrings;
import edu.sdsu.mithun.util.Preferences;
import edu.sdsu.mithun.util.SNBSUtil;

public class SNBSNotification extends Service{
	static int NOTIFICATION_ID;
	private static final long[] SNBS_VIBRATE_PATTERN =  
    {0,200, 50, 500, 50, 200, 50, 500, 50, 200, 50, 2000 };
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("SNBS","came into onCreate() on SNBSNotification");
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("SNBS","came into onStartCommand() of SNBSNotification");
		Bundle bundle = (intent.getExtras()).getBundle(IntentStrings.INTENT_BUNDLE);
		int alertId=bundle.getInt(IntentStrings.ALERT_ID);
		String messageBody=bundle.getString(IntentStrings.MESSAGE_BODY);
		
		String alertString=SNBSUtil.getAlertString(alertId);
		
		Log.d("SNBS","bundle.get alertint"+alertId);
    	startNotification(alertId,alertString,messageBody);
		return super.onStartCommand(intent, flags, startId);
	}
	
	protected void startNotification (int alertID,String title, String message) {
		   String ns = Context.NOTIFICATION_SERVICE;
		   NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		   
	
		   int icon = SNBSUtil.getAlertIcon(alertID);
		   NOTIFICATION_ID=SNBSUtil.getNotificationId(alertID);
		   CharSequence tickerText = title+":"+message;
		   long when = System.currentTimeMillis();

		   Notification notification = new Notification(icon, tickerText, when);

		   Context context = getApplicationContext();
		   
		   
		   System.out.println("phone settings vibrate is = "+Settings.System.getString(getContentResolver(),
		            Settings.System.VIBRATE_ON));
		   AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		   System.out.println("audio manager ="+am.getRingerMode());
		   CharSequence contentTitle = title;
		   CharSequence contentText = message;
		   Intent notificationIntent = new Intent(this, ThreadListView.class);
		   PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		   if(Preferences.canVibrate(getBaseContext())){
				notification.vibrate=SNBS_VIBRATE_PATTERN;
		   }
		   //notification.defaults |= Notification.DEFAULT_VIBRATE;
		   //notification.defaults|=Notification.DEFAULT_SOUND;

		   //check if phone is in loud mode or not
		   Log.d("SNBS","getPackageName= "+getPackageName());
		   Log.d("SNBS","uri="+Uri.parse("R.raw.alert"/*"file:///sdcard/Ashiqui.mp3"*/));
		   notification.sound= Uri.parse("file:///sdcard/alert.mp3");
		   
		   notification.flags = Notification.FLAG_AUTO_CANCEL;
		   notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		   mNotificationManager.notify(NOTIFICATION_ID, notification);
		}
}
