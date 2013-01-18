package edu.sdsu.mithun.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import edu.sdsu.mithun.R;
import edu.sdsu.mithun.database.SnbsDatabaseHelper;
import edu.sdsu.mithun.util.IntentStrings;
import edu.sdsu.mithun.util.SNBSUtil;
import edu.sdsu.mithun.util.SnbsMessage;

public class MessageListView extends ListActivity{
	private ProgressDialog mProgressDialog = null;
	private ArrayList<SnbsMessage>snbsClassList = null;
	private MessageListAdapter mAdapter = null;
	private Runnable viewRestaurants;
	
	AlertDialog alertDialog;
	Intent intent;
	String snbsMessageDetailView;
	int snbsClass;
	int messageId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.message_view);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.snbs_window_title);
		
		
		System.out.println("came into onCreate of messagelistview");
		intent= getIntent();
		snbsClass = intent.getIntExtra(IntentStrings.ALERT_ID,0);
		System.out.println("now setting snbsMessage "+snbsClass+"as read");
		ImageView windowTitleImage = (ImageView) findViewById(R.id.title_icon);
		windowTitleImage.setImageResource(SNBSUtil.getAlertIcon(snbsClass));
		TextView windowTitleText = (TextView) findViewById(R.id.title_string);
		windowTitleText.setText(SNBSUtil.getAlertString(snbsClass));
		setSnbsClassMessageAsRead(snbsClass);
		 NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		 mNotificationManager.cancel(SNBSUtil.getNotificationId(snbsClass));
	///	TextView title = (TextView) findViewById(R.id.title_string);
	///	title.setText(snbsClass);
/////////////////////////////////////////////////////////////////////////
		
		android.widget.ListView lv = getListView();
		//lv.setSelector(R.drawable.balloon_bg_gray_press);
	
	
	     // Then you can create a listener like so:
		lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v, int 
				     pos, long id) {
				// TODO Auto-generated method stub
				System.out.println("looooooooong click");
				final CharSequence[] items = {"Lock message", "View message details", "Delete message"};
				final View dialogView = v;
				snbsMessageDetailView=new String("Alerting Authority: City of San Diego\nIntensity: Medium");//new String(snbsClassList.get(pos).getCmasMessageDetailView());
				messageId=snbsClassList.get(pos).getMessageId();
				AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
				builder.setTitle("Message options");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				        
				        if((items[item]).equals("View message details")){
				        	System.out.println("view message details");
				        	
							alertDialog = new AlertDialog.Builder(dialogView.getContext()).create();
							 alertDialog.setTitle("Message details");
							
							 
							    //alertDialog.setMessage(snbsMessageDetailView);
							 	alertDialog.setMessage(Html.fromHtml("<font color='yellow'>Alerting Authority:</font> <font color='green'> City of San Diego</font><br/>"+
							 			"<font color='yellow'>Intensity:</font> <font color='green'> Medium</font><br/>"+
							 			"<font color='yellow'>Latitude:</font> <font color='green'> N 32° 46\' 30.2376\"</font><br/>"+
							 			"<font color='yellow'>Longitude:</font> <font color='green'> W 117° 4\' 14.1096\"</font>"+
							 			""));
							    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			
							      public void onClick(DialogInterface dialog, int which) {
							    	  System.out.println("clicked ok now starting new activity");
							    	  
							        return;
			
							    } }); 
							alertDialog.show();
				        }else if((items[item]).equals("Delete message")){
				        	System.out.println("delete message selected");
				        	 SnbsDatabaseHelper db ;
				        	 
				     		try{
				     		  db = new SnbsDatabaseHelper(getApplicationContext());
				               db.deleteRow(messageId);
				     		db.close();

				     	}catch (Exception e) {
				     			// TODO: handle exception
				         		Log.e("BACKGROUND_PROC",e.getMessage());
				     		}
				     	startActivity(getIntent());finish();
				        }
				    }
				});
				AlertDialog alert = builder.create();
				alert.show();

				return false;
			}
		});
	        


	//end		
	        snbsClassList =new ArrayList<SnbsMessage>();
			
			//create snbs obj array
	        this.mAdapter=new MessageListAdapter(this, R.layout.message_view_row, snbsClassList);
	        setListAdapter(mAdapter);
	        
	        viewRestaurants=new Runnable() {
			
				public void run() {
					// TODO Auto-generated method stub
					getCmasMessageFromTable();
				}
			};
			Thread thd=new Thread(null, viewRestaurants,"LoadInBackground");
	        thd.start();
	        
			mProgressDialog=ProgressDialog.show(MessageListView.this, "Cmas Class Loaging..", "retreving data...",true);        
	  
////////////////////////////////////////////////////////////////////////
		  /*String msg = intent.getStringExtra("sampleData");

                 String snbsClass =           intent.getStringExtra("snbs_class");
                 String snbsBody=				intent.getStringExtra("snbsBody");
                 String snbsExp =				intent.getStringExtra("convertDate");
                 String  snbsCategory = intent.getStringExtra("snbsCategory");
                 String  snbsResponse = intent.getStringExtra("snbsResponse");
                 String  snbsSeverity = intent.getStringExtra("snbsSeverity");
                 String  snbsUrgency = intent.getStringExtra("snbsUrgency");
                 String  snbsCertainity = intent.getStringExtra("snbsCertainity");
                 
                 StringBuilder str=new StringBuilder();
                 str.append("Type: Text message");
                 str.append("\n");
                 str.append("Service Category: "+snbsClass);
                 str.append("\n");
                 str.append("Received: "+snbsExp);
                 str.append("\n");
                 str.append("Priority: Normal");
                 str.append("\n");
                 str.append("CMAE Category: "+snbsCategory);
                 str.append("\n");
                 str.append("Response: "+snbsResponse);
                 str.append("\n");
                 str.append("Severity: "+snbsSeverity);                 
                 str.append("\n");
                 str.append("Urgency: "+snbsUrgency);
                 str.append("\n");
                 str.append("Cretainity:"+snbsCertainity);
                 snbsMessageDetailView=new String(str);*/
		  
		
		/*TextView mainMessage = (TextView)findViewById(R.id.detailMessage);
		TextView timeStamp =  (TextView)findViewById(R.id.timeStamp);
		TextView expDate =  (TextView)findViewById(R.id.expDate);
		ImageView bgImage = (ImageView) findViewById(R.drawable.balloon_bg_gray);*/
		//LinearLayout ll = (LinearLayout)findViewById(R.id.layout);
		
		/*mainMessage.setText(snbsBody);
		//timeStamp.setText("time stamp here");
		expDate.setText(snbsExp);*/
		
		
	}

	
	  private void getCmasMessageFromTable(){
			 SnbsDatabaseHelper db ;
			 
				try{
		    		
				  db = new SnbsDatabaseHelper(this.getApplicationContext());
				 
		             	  this.snbsClassList =   db.getSnbsMessagesRowsAsArrays(snbsClass);
				db.close();

			}catch (Exception e) {
					// TODO: handle exception
		    		Log.e("BACKGROUND_PROC",e.getMessage());
				}
		    	runOnUiThread(returnRes);
		    }
	  
	  private Runnable returnRes=new Runnable() {
			
			
			public void run() {
				// TODO Auto-generated method stub
				if(snbsClassList!=null&&snbsClassList.size()>0){
					mAdapter.clear();
					mAdapter.notifyDataSetChanged();
					for(int i=0;i<snbsClassList.size();i++){
						mAdapter.add(snbsClassList.get(i));
					}
				}else{
					System.out.println("this alert is empty");
					finish();
				}
				mProgressDialog.dismiss();
				mAdapter.notifyDataSetChanged();
			}
		};
		
		void setSnbsClassMessageAsRead(int alertId){
			SnbsDatabaseHelper db ;
				try{
				  db = new SnbsDatabaseHelper(this);
				 
		             	 db.setSnbsMessagesAsReadByClass(alertId);
		             	System.out.println("set "+alertId+" as read success");
		             	  
				db.close();
			}catch (Exception e) {
		    		Log.e("BACKGROUND_PROC",e.getMessage());

				}
			
		}
		protected void onResume() {
			super.onResume();
			System.out.println("onresume of message list view");
			runOnUiThread(viewRestaurants);
		};
		
		@Override
		public void finish() {
			// TODO Auto-generated method stub
			super.finish();
			System.out.println("finish called");
			setSnbsClassMessageAsRead(snbsClass);
		}
	
}
