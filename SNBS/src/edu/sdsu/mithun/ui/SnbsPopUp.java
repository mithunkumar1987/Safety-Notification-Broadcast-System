
package edu.sdsu.mithun.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.PriorityQueue;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.sdsu.mithun.R;
import edu.sdsu.mithun.transaction.SnbsPopUpManager;
import edu.sdsu.mithun.util.SNBSUtil;
import edu.sdsu.mithun.util.SnbsMessage;


public class SnbsPopUp extends Activity{

    private static final String TAG = "SnbsPopUp";
    
    private PriorityQueue<SnbsMessage> mSnbsAlertQueue;

    private int mSnbsAlertID;
    private TextView mSnbsTitle;
    private TextView mSnbsClassTitle;
    private TextView mSnbsDate;
    
    private TextView mSnbsMessage;
    private TextView mSnbsExpirationDateTitle;
    private TextView mSnbsExpirationDate;

    private ImageView mSnbsIcon;
    private String mSnbsMoreDetails;
    AlertDialog alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SNBS","came into onCreate of Snbs pop up");
        if(isPortraitMode())
            setContentView(R.layout.pop_up);
        else
            setContentView(R.layout.pop_up);
        
        mSnbsAlertQueue = SnbsPopUpManager.getPriorityQueue();
        
        mSnbsTitle = (TextView) findViewById(R.id.title);
        mSnbsClassTitle = (TextView) findViewById(R.id.title);
        mSnbsDate = (TextView) findViewById(R.id.received_date);
        
        mSnbsMessage = (TextView) findViewById(R.id.message_body);
        mSnbsExpirationDateTitle = (TextView) findViewById(R.id.received_date);
        mSnbsExpirationDate = (TextView) findViewById(R.id.received_date);
        mSnbsIcon = (ImageView) findViewById(R.id.image);
      
        Button btnConfirm = (Button) findViewById(R.id.more_details);
        btnConfirm.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
            	NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       		 mNotificationManager.cancel(SNBSUtil.getNotificationId(mSnbsAlertID));
                
       		 
       		 alertDialog = new AlertDialog.Builder(v.getContext()).create();
			 alertDialog.setTitle("Message details");
			 alertDialog.setMessage(Html.fromHtml("<font color='yellow'>Alerting Authority:</font> <font color='green'> City of San Diego</font><br/>"+
			 			"<font color='yellow'>Intensity:</font> <font color='green'> Medium</font><br/>"+
			 			"<font color='yellow'>Latitude:</font> <font color='green'> N 32° 46\' 30.2376\"</font><br/>"+
			 			"<font color='yellow'>Longitude:</font> <font color='green'> W 117° 4\' 14.1096\"</font>"+
			 			""));
			 alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

				 public void onClick(DialogInterface dialog, int which) {
					 System.out.println("clicked ok now starting new activity");
					 mSnbsAlertQueue.poll();
			         if (mSnbsAlertQueue.isEmpty())
			        	 finish();
			         else
			             setCmasAlertContent();
				     	return;
				 } }); 
			 	alertDialog.show();
            }
        }); 
    }

    private void setCmasAlertContent() {
        SnbsMessage message = mSnbsAlertQueue.peek();
        mSnbsAlertID=message.alertId;
        mSnbsTitle.setText(R.string.app_name);
        mSnbsClassTitle.setText(message.getAlertString());
        mSnbsDate.setText(parseTimeInMillisToString(System.currentTimeMillis()));
        String messageBody=message.getMessageBody();
        mSnbsMoreDetails=message.getMoreDetails();
        if(messageBody.length()>100){
        	messageBody=messageBody.substring(0, 97)+"...";
        }
        mSnbsMessage.setText(messageBody);
        mSnbsIcon.setImageResource(SNBSUtil.getAlertIcon(message.getAlertId()));
        mSnbsExpirationDate.setText(parseTimeInMillisToString(adjustToCurrentTimeZone(message.getReceivedDate())));
        mSnbsExpirationDateTitle.setVisibility(View.GONE);
        mSnbsExpirationDate.setVisibility(View.GONE);
    }

    private long adjustToCurrentTimeZone(long milliseconds){
        Calendar calendar = Calendar.getInstance();
        long timeZoneOffset = calendar.get(Calendar.ZONE_OFFSET)  + calendar.get(Calendar.DST_OFFSET);
        milliseconds += timeZoneOffset;

        return milliseconds;
    }
    
    private String parseTimeInMillisToString(long milliseconds){
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
        Date date = new Date();
        date.setTime(milliseconds);
        
        return formatter.format(date).toString();
    }
    
    private boolean isPortraitMode() {
        final Configuration configuration = getBaseContext().getResources().getConfiguration();
        
        return configuration.orientation == Configuration.ORIENTATION_PORTRAIT;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (mSnbsAlertQueue.isEmpty())
            finish();
        else
            setCmasAlertContent();
    }
    
    @Override
    protected void onDestroy() {
        stopService(new Intent(getBaseContext(), SnbsPopUpManager.class));
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    
}

