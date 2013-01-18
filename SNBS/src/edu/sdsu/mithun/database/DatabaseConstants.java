package edu.sdsu.mithun.database;

import android.net.Uri;

public class DatabaseConstants {
	
	public static final String DATABASE_NAME = "snbs_database";
	public static final String MAIN_TABLE_NAME = "snbs_table";
	
	public static final String _ID = "_id";
	public static final String _ID_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
	
	public static final String MESSAGE_ID = "message_id";
	public static final String MESSAGE_ID_TYPE = "INTEGER UNIQUE NOT NULL";
	
	public static final String ALERT_ID = "alert_id";
	public static final String ALERT_ID_TYPE = "INTEGER NOT NULL";
	
	public static final String MESSAGE_BODY = "message_body";
	public static final String MESSAGE_BODY_TYPE = "TEXT NOT NULL";
	
	public static final String RECEIVED_DATE = "received_date";
	public static final String RECEIVED_DATE_TYPE = "LONG NOT NULL";
	
	public static final String READ = "read";
	public static final String READ_TYPE = "INTEGER NOT NULL";
		
	public static final Uri CONTENT_URI = Uri.parse("content://edu.sdsu.mithun/" + MAIN_TABLE_NAME);
}
