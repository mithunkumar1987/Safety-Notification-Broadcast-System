package edu.sdsu.mithun.transaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony.Sms.Intents;
import android.telephony.SmsMessage;
import android.util.Log;
import edu.sdsu.mithun.util.DateTimeFormater;
import edu.sdsu.mithun.util.IntentStrings;
public class SNBSBroadcastReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(Context context, Intent intent) {
		SmsMessage[] msgs = Intents.getMessagesFromIntent(intent);
		SmsMessage SnbsSms = msgs[0];
		if(!SnbsSms.getisCMAS()){
			Log.d("SNBS","not SNBS message, so return");
			return;
		}
	 	Log.d("SNBS","message.getisSNBS()"+SnbsSms.getisCMAS());
	 	Log.d("SNBS","message.getisSNBS()"+SnbsSms.getCMASClassInt());
	 	Log.d("SNBS","message.getMessageBody()"+SnbsSms.getMessageBody());
	 	Log.d("SNBS","message.getBearData().messageId"+SnbsSms.getBearData().messageId);
	 	Log.d("SNBS","message.getTimestampMillis()"+
	 			DateTimeFormater.parseTimeInMillisToString(System.currentTimeMillis()));
		Log.d("SNBS","intent action = "+intent.getAction());
		Intent serviceIntent = new Intent(context, SNBSReceiverService.class);
		serviceIntent.putExtra(IntentStrings.ALERT_ID,SnbsSms.getCMASClassInt());
		serviceIntent.putExtra(IntentStrings.MESSAGE_ID, SnbsSms.getBearData().messageId);
		serviceIntent.putExtra(IntentStrings.MESSAGE_BODY, SnbsSms.getMessageBody());
		serviceIntent.putExtra(IntentStrings.RECEIVED_DATE, System.currentTimeMillis());
		context.startService(serviceIntent);
	}
}
