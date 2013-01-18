package edu.sdsu.mithun.ui;


import java.util.ArrayList;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import edu.sdsu.mithun.R;
import edu.sdsu.mithun.database.SnbsDatabaseHelper;
import edu.sdsu.mithun.util.IntentStrings;
import edu.sdsu.mithun.util.Preferences;
import edu.sdsu.mithun.util.SnbsMessage;



public class ThreadListView extends ListActivity {
    /** Called when the activity is first created. */
	private ProgressDialog mProgressDialog = null;
	private ArrayList<SnbsMessage>threadList = null;

	private ThreadListAdapter mThreadListAdapter = null;
	private Runnable viewSnbsThreads;
	Intent intent;
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	System.out.println("pref called");
    	switch (item.getItemId()) {
        case R.id.settings:
        	startActivity(new Intent(getBaseContext(), Preferences.class));
            return true;
        case R.id.delete:
  
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    	
    	
    	
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        Log.d("SNBS","came into onCreate of ThreadListView");
        setContentView(R.layout.main);
   //      getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.cmas_window_title);
//new change start



        ListView lv = getListView();
  
     // Then you can create a listener like so: 
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
				Log.d("SNBS","onItemClick (Threadlistview)="+pos);

				 intent = new Intent(getBaseContext(), MessageListView.class);
				 
				 SnbsMessage cmasObj = threadList.get(pos);
				 intent.putExtra(IntentStrings.ALERT_ID, cmasObj.getAlertId());
				Log.d("SNBS","touched cmas class = "+threadList.get(pos));
			    startActivityForResult(intent, 1);
			}
		});


//end		
        threadList =new ArrayList<SnbsMessage>();
		
		//create obj array
        this.mThreadListAdapter=new ThreadListAdapter(this, R.layout.row, threadList);
        setListAdapter(mThreadListAdapter);
        
        viewSnbsThreads=new Runnable() {
		
			public void run() {
				getSnbsMessageFromTable();
			}
		};
		Thread thd=new Thread(null, viewSnbsThreads,"LoadInBackground");
        thd.start();
		mProgressDialog=ProgressDialog.show(ThreadListView.this, "Messages Loading..", "retreving data...",true);        
    }
    
    private void getSnbsMessageFromTable(){
	 SnbsDatabaseHelper db ;
	 
		try{
			db = new SnbsDatabaseHelper(this.getApplicationContext());
			this.threadList =   db.getThreadedRowsAsArrays();
			db.close();
		}catch (Exception e) {
    		Log.e("BACKGROUND_PROC",e.getMessage());
		}
    	runOnUiThread(returnRes);
    }
    
    private Runnable returnRes=new Runnable() {
		
		
		public void run() {
			if(threadList!=null&&threadList.size()>0){
				mThreadListAdapter.clear();
				mThreadListAdapter.notifyDataSetChanged();
				for(int i=0;i<threadList.size();i++){
					mThreadListAdapter.add(threadList.get(i));
				}
			}
			mProgressDialog.dismiss();
			mThreadListAdapter.notifyDataSetChanged();
		}
	};
	
	protected void onResume() {
		super.onResume();
		//startActivity(getIntent());finish();
		System.out.println("on resume");
	
		runOnUiThread(viewSnbsThreads);
		
	};
	

}
