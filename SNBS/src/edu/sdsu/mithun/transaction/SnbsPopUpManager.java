
package edu.sdsu.mithun.transaction;

import android.app.Service;
import android.os.Bundle;
import android.os.IBinder;
import java.util.Comparator;
import java.util.PriorityQueue;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.sdsu.mithun.ui.SnbsPopUp;
import edu.sdsu.mithun.util.IntentStrings;
import edu.sdsu.mithun.util.SNBSUtil;
import edu.sdsu.mithun.util.SnbsMessage;

public class SnbsPopUpManager extends Service {
    private static final String TAG = "SnbsPopUpManager";
    private TelephonyManager mTelephonyManager;
    private CmasDisplayPhoneStateListener mCmasDisplayPhoneStateListener;

    private static final int CMAS_ALERT_QUEUE_SIZE = 200;
    private static boolean  mWasInCall = false;
    private static boolean  mWasInDataActivity = false;
    
    private static Comparator<SnbsMessage> sCmasClassComparator = new Comparator<SnbsMessage>() {
        public int compare(SnbsMessage msg1, SnbsMessage msg2) {
             return Long.signum(msg2.getReceivedDate() - msg1.getReceivedDate());
        }
    };

    private static PriorityQueue<SnbsMessage> sCmasDisplayQueue = new PriorityQueue<SnbsMessage>(
            CMAS_ALERT_QUEUE_SIZE, sCmasClassComparator);
    
    @Override
    public void onCreate() {
            
        super.onCreate();
        System.out.println("CmasDisplayManagerService started");
        Log.d(TAG, "[onCreate]...");
        mCmasDisplayPhoneStateListener = new CmasDisplayPhoneStateListener();
        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        mTelephonyManager.listen(mCmasDisplayPhoneStateListener, 
                PhoneStateListener.LISTEN_CALL_STATE | 
                PhoneStateListener.LISTEN_DATA_ACTIVITY);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SNBS", "[onStartCommand]..of manager.");
        createCmasMessage(intent);
        display();
        return super.onStartCommand(intent, flags, startId);
    }

    private void createCmasMessage(Intent intent){
        Bundle bundle = (intent.getExtras()).getBundle(IntentStrings.INTENT_BUNDLE);
        SnbsMessage snbsMessage=new SnbsMessage();
		snbsMessage.setAlertId(bundle.getInt(IntentStrings.ALERT_ID));
		snbsMessage.setMessageBody(bundle.getString(IntentStrings.MESSAGE_BODY));
		snbsMessage.setReceivedDate(bundle.getLong(IntentStrings.RECEIVED_DATE));
		snbsMessage.setMoreDetails("Alerting Authority : City of San Diego \n Intensity : Medium");
		
        sCmasDisplayQueue.add(snbsMessage);     
    }

    private String parseTimeInMillisToString(long milliseconds){

        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        date.setTime(milliseconds);
        
        return formatter.format(date).toString();
    }

    public void display(){
        if(sCmasDisplayQueue.isEmpty())
            return;
        
        Context context = getBaseContext();
        int callState = mTelephonyManager.getCallState();
        int dataActivity = mTelephonyManager.getDataActivity();
        
        Log.d(TAG, "[display] - CallState: " + callState);
        Log.d(TAG, "[display] - DataActivity: " + dataActivity);
        
        if(callState == TelephonyManager.CALL_STATE_IDLE && 
                (dataActivity == TelephonyManager.DATA_ACTIVITY_DORMANT || 
                        dataActivity == TelephonyManager.DATA_ACTIVITY_NONE)){
            Intent smsAlertIntent = new Intent(context, SnbsPopUp.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            Log.d("SNBS","now calling startActivity(sbns pop up)");
            context.startActivity(smsAlertIntent);
        }
    }

    public static PriorityQueue<SnbsMessage> getPriorityQueue(){
        
        return sCmasDisplayQueue;
    }
    
    @Override
    public void onDestroy() {
        Log.d(TAG, "CmasDisplayService:onDestroy - queue cleared...");
        mTelephonyManager.listen(mCmasDisplayPhoneStateListener, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }
    
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    private class CmasDisplayPhoneStateListener extends PhoneStateListener{
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "[onCallStateChanged] CALL_STATE_IDLE");
                    if(mWasInCall){
                        display();
                        mWasInCall = false;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    mWasInCall = true;
                    Log.d(TAG, "[onCallStateChanged] CALL_STATE_OFFHOOK");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    mWasInCall = true;
                    Log.d(TAG, "[onCallStateChanged] CALL_STATE_RINGING");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onDataActivity(int direction) {
            super.onDataActivity(direction);
            
            switch(direction){
               case TelephonyManager.DATA_ACTIVITY_DORMANT:
                   Log.d(TAG, "[onDataActivity] DATA_ACTIVITY_DORMANT");
                   if(mWasInDataActivity){
                       display();
                       mWasInDataActivity = false;
                   }
                   break;
               case TelephonyManager.DATA_ACTIVITY_NONE:
                   mWasInDataActivity = true;
                   Log.d(TAG, "[onDataActivity] DATA_ACTIVITY_NONE");
                   break;
               case TelephonyManager.DATA_ACTIVITY_IN:
                   mWasInDataActivity = true;
                   Log.d(TAG, "[onDataActivity] DATA_ACTIVITY_IN");
                   break;
               case TelephonyManager.DATA_ACTIVITY_OUT:
                   mWasInDataActivity = true;
                   Log.d(TAG, "[onDataActivity] DATA_ACTIVITY_OUT");
                   break;
               case TelephonyManager.DATA_ACTIVITY_INOUT:
                   mWasInDataActivity = true;
                   Log.d(TAG, "[onDataActivity] DATA_ACTIVITY_INOUT");
                   break;
               default:
                   break;
            }
        }
    }

}

