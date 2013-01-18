package edu.sdsu.mithun.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import edu.sdsu.mithun.util.SnbsMessage;

public class SnbsDatabaseHelper extends SQLiteOpenHelper {

	
	private static final int DATABASE_VERSION = 1;
	private static SnbsDatabaseHelper mSnbsDatabaseHelper=null;
	private SQLiteDatabase db;
	public static synchronized SnbsDatabaseHelper getDatabaseInstance(Context context){
		if(mSnbsDatabaseHelper==null){
			mSnbsDatabaseHelper=new SnbsDatabaseHelper(context);
		}
		return mSnbsDatabaseHelper;
	}
	
	//Database create SQL statement
	private static final String DATABASE_CREATE = "create table "+DatabaseConstants.MAIN_TABLE_NAME+" ("
			+ DatabaseConstants._ID +" "+DatabaseConstants._ID_TYPE+" ,"
			+ DatabaseConstants.MESSAGE_ID + " "+DatabaseConstants.MESSAGE_ID_TYPE+" ,"
			+ DatabaseConstants.ALERT_ID + " "+DatabaseConstants.ALERT_ID_TYPE+" ,"
			+ DatabaseConstants.MESSAGE_BODY + " "+DatabaseConstants.MESSAGE_BODY_TYPE+" ,"
			+ DatabaseConstants.RECEIVED_DATE +" "+DatabaseConstants.RECEIVED_DATE_TYPE+" ,"
			+ DatabaseConstants.READ +" "+DatabaseConstants.READ_TYPE
			+ ");";
	
	private static final String TABLE_DROP = "DROP TABLE IF EXIST "+DatabaseConstants.MAIN_TABLE_NAME;
	
	private static final String[] TABLE_COLUMNS = new String[] { DatabaseConstants.ALERT_ID,
		 														 DatabaseConstants.MESSAGE_ID,
																 DatabaseConstants.MESSAGE_BODY,
																 DatabaseConstants.RECEIVED_DATE,
																 DatabaseConstants.READ
																 };
	
	public SnbsDatabaseHelper(Context context) {
		super(context, DatabaseConstants.DATABASE_NAME,null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		Log.d("SNBS","now calling execSQL(DB_CREATE)");
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.d("SNBS","came into onUpgrade");
		Log.w(SnbsDatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL(TABLE_DROP);
	}

	public void setSnbsMessageObjectFromCursor(SnbsMessage snbsObject,Cursor cursor){
		snbsObject.setAlertId(cursor.getInt(0));
		snbsObject.setMessageId(cursor.getInt(1));
		snbsObject.setMessageBody(cursor.getString(2));
		snbsObject.setReceivedDate(cursor.getLong(3));
		snbsObject.setRead(cursor.getInt(4));
	}
	
	//will return SnbsMessage objects in array with unique AlertID, for displaying in the inbox
	//sorted based on received time.
	public ArrayList<SnbsMessage> getThreadedRowsAsArrays() {
		Cursor cursor = null;
		this.db = this.getWritableDatabase();
		ArrayList<SnbsMessage> dataList = new ArrayList<SnbsMessage>();

		try {
			cursor = db.query(DatabaseConstants.MAIN_TABLE_NAME, TABLE_COLUMNS, null, null, DatabaseConstants.ALERT_ID, null,
					DatabaseConstants.RECEIVED_DATE+" DESC");
			cursor.moveToFirst();
			Log.d("SNBS","no of rows = "+cursor.getCount());
			if (!cursor.isAfterLast()) {
				do {
					SnbsMessage snbsObject= new SnbsMessage();
					setSnbsMessageObjectFromCursor(snbsObject,cursor);
					dataList.add(snbsObject);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}

		} catch (SQLException e) {
			Log.d("DB Error", e.toString());
			e.printStackTrace();
		}
		cursor.close();
		return dataList;
	}
	
	public void deleteRow(long messageId){
		this.db = this.getWritableDatabase();
		
		try {
			db.delete(DatabaseConstants.MAIN_TABLE_NAME, DatabaseConstants.MESSAGE_ID+"="+messageId,null);
			Log.d("SNBS","row deleted successfully into the table");
			//System.out.println("hellooooo in try bloak");
		} catch (Exception e) {
			e.printStackTrace(); // prints the stack trace to the log
		}
		
	}
	
	//to get all rows corresponding to one Alert (detail view)
	public ArrayList<SnbsMessage> getSnbsMessagesRowsAsArrays(int alertId) {
		
		Cursor cursor = null;
		this.db = this.getWritableDatabase();
		ArrayList<SnbsMessage> dataList = new ArrayList<SnbsMessage>();

		try {
			cursor = db.query(DatabaseConstants.MAIN_TABLE_NAME, TABLE_COLUMNS,  DatabaseConstants.ALERT_ID+" = " + alertId, null, null, null,
					DatabaseConstants.RECEIVED_DATE+" DESC");
			cursor.moveToFirst();
			Log.d("SNBS","no of rows = "+cursor.getCount());
			if (!cursor.isAfterLast()) {
				do {
					SnbsMessage snbsObject= new SnbsMessage();
					setSnbsMessageObjectFromCursor(snbsObject, cursor);
					dataList.add(snbsObject);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}

		} catch (SQLException e) {
			Log.d("DB Error", e.toString());
			e.printStackTrace();
		}
		cursor.close();
		return dataList;
	}
	
	public void setSnbsMessagesAsReadByClass(int alertId) {
		ContentValues values = new ContentValues();
		int read=1;//set all rows to corresponding cmas class as read 
		values.put(DatabaseConstants.READ, read);
		SQLiteDatabase db;
		db = this.getWritableDatabase();
		try {
			String whereClause=DatabaseConstants.ALERT_ID+"="+alertId+" AND "+DatabaseConstants.READ+" = 0";
			System.out.println("where clause = "+whereClause);
			db.update(DatabaseConstants.MAIN_TABLE_NAME, values, whereClause, null);
			System.out.println(alertId+" is all read now");
		} catch (Exception e) {
			e.printStackTrace(); // prints the stack trace to the log
		}
	}

	public int getMessageCountOfAlertType(int alertId) {
		Cursor cursor = null;
		int count=0;
		this.db = this.getWritableDatabase();
		

		try {
			String query="SELECT COUNT(*) FROM "+DatabaseConstants.MAIN_TABLE_NAME+" WHERE "+DatabaseConstants.ALERT_ID+" = " + alertId;
			System.out.println("query = "+query);
			cursor = db.rawQuery( query,null);
			cursor.moveToFirst();
			
			count=cursor.getInt(0);
			System.out.println("no of rows = "+count);
		} catch (SQLException e) {
			Log.d("DB Error", e.toString());
			e.printStackTrace();
		}
		cursor.close();
		return count;
	}
	
	//to get all rows corresponding to one cmasClass (detail view)
	public int getSnbsUnreadByAlertId(int alertId) {
		//ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		Cursor cursor = null;
		int count=0;
		this.db = this.getWritableDatabase();
		
		try {
			String query="SELECT COUNT(*) FROM "+DatabaseConstants.MAIN_TABLE_NAME+" WHERE "+DatabaseConstants.ALERT_ID+" = " 
			+ alertId+" AND "+DatabaseConstants.READ+" = 0";
			System.out.println("query = "+query);
			cursor = db.rawQuery( query,null);
			cursor.moveToFirst();
			
			count=cursor.getInt(0);
			System.out.println("no of rows unread= "+count);
		} catch (SQLException e) {
			Log.d("DB Error", e.toString());
			e.printStackTrace();
		}
		cursor.close();
		return count;
	}	
	

}
