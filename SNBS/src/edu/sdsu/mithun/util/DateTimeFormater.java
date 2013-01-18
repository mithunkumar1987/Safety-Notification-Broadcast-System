package edu.sdsu.mithun.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeFormater {
	
    public static String parseTimeInMillisToString(long milliseconds, String format){

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = new Date();
        date.setTime(milliseconds);
        
        return formatter.format(date).toString();
    }
    
    public static String parseTimeInMillisToString(long milliseconds){
    	return parseTimeInMillisToString(milliseconds,"MM/dd/yy hh:mm a");
    }
}
