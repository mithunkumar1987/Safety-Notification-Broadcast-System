package edu.sdsu.mithun.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import edu.sdsu.mithun.R;
import edu.sdsu.mithun.util.SnbsMessage;

class MessageListAdapter extends ArrayAdapter<SnbsMessage> {

	private ArrayList<SnbsMessage> cmasClassList;
	
	public MessageListAdapter(Context context, int textViewResourceId,
			ArrayList<SnbsMessage> cmasClassList) {
		super(context, textViewResourceId, cmasClassList);
		this.cmasClassList=cmasClassList; 
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;
		if(v==null){
			LayoutInflater vi=(LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=vi.inflate(R.layout.message_view_row, null);
		}
		
		SnbsMessage
		cmasMessage=cmasClassList.get(position);
		
		if(cmasMessage!=null){
			
			TextView mainMessage = (TextView)v.findViewById(R.id.detailMessage);
			TextView timeStamp =  (TextView)v.findViewById(R.id.timeStamp);
			TextView expDate =  (TextView)v.findViewById(R.id.expDate);
			//ImageView bgImage = (ImageView) v.findViewById(R.drawable.balloon_bg_gray);
			//LinearLayout ll = (LinearLayout)v.findViewById(R.id.layout);
			SharedPreferences mPrefsSNBSAlertFont = PreferenceManager
			.getDefaultSharedPreferences(getContext());
		 	String font = mPrefsSNBSAlertFont.getString("listPref", "Default");
			mainMessage.setTextSize(Integer.parseInt(font)*2);
			System.out.println("font == "+font);
			
			mainMessage.setText(cmasMessage.getMessageBody());
			timeStamp.setText("Received: "+cmasMessage.getReceivedDateString());
			expDate.setText("Expiration: "+cmasMessage.getReceivedDateString());
		}
		
		return v;
	}
}
