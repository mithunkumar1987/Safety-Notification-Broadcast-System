package edu.sdsu.mithun.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.sdsu.mithun.R;
import edu.sdsu.mithun.database.SnbsDatabaseHelper;
import edu.sdsu.mithun.util.SNBSUtil;
import edu.sdsu.mithun.util.SnbsMessage;

class ThreadListAdapter extends ArrayAdapter<SnbsMessage> {
	private ArrayList<SnbsMessage> snbsList;
	
	public ThreadListAdapter(Context context, int textViewResourceId,
			ArrayList<SnbsMessage> snbsList) {
		super(context, textViewResourceId, snbsList);
		this.snbsList=snbsList;
		
	}
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;
		if(v==null){
			LayoutInflater vi=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=vi.inflate(R.layout.row, null);
		}
		SnbsMessage snbsMessage=snbsList.get(position);
		if(snbsMessage!=null){
			ImageView logo=(ImageView)v.findViewById(R.id.icon);
			TextView tname=(TextView)v.findViewById(R.id.cmas_class);
			TextView tdescription=(TextView)v.findViewById(R.id.bottom_text);
			TextView receivedTime=(TextView)v.findViewById(R.id.cmas_date);
			
			if(tname!=null){
				logo.setImageResource(SNBSUtil.getAlertIcon(snbsMessage.getAlertId()));
				tname.setText(snbsMessage.getAlertString()+" ("+Integer.toString(getCount(snbsMessage.getAlertId()))+")");
				String messageBody=snbsMessage.getMessageBody();
				if(messageBody.length()>20){
					messageBody=messageBody.substring(0, 18)+"...";
				}
				
				tdescription.setText(messageBody);
				receivedTime.setText(snbsMessage.getReceivedDateString());

				if(getUnreadCount(snbsMessage.alertId)>0){
					tname.setTypeface(null, Typeface.BOLD);
				}else{
					tname.setTypeface(null, Typeface.NORMAL);
				}
			}
		}
		return v;
	}
	
	int getCount(int alertId){
			 SnbsDatabaseHelper db ;
			 int count;
				try{
				  db = new SnbsDatabaseHelper(getContext());
				 
		             	  count=   db.getMessageCountOfAlertType(alertId);  
				db.close();
			}catch (Exception e) {
		    		Log.e("BACKGROUND_PROC",e.getMessage());
		    		count =0;
				}
			Log.d("SNBS","alertId="+alertId+" count ="+count);
		return count;
	}
	
	
	int getUnreadCount(int alertId){
		SnbsDatabaseHelper db ;
		 int count;
			try{
			  db = new SnbsDatabaseHelper(getContext());
	          count=  db.getSnbsUnreadByAlertId(alertId);
	          db.close();
		}catch (Exception e) {
	    		Log.e("BACKGROUND_PROC",e.getMessage());
	    		count =0;
			}
		System.out.println(alertId+" unread count ="+count);
	return count;
}
	/*
	int getAllUnreadCount(){
		return getUnreadCount("%");
	}*/
	
}
