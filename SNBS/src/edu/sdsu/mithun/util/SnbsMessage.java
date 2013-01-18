package edu.sdsu.mithun.util;


public class SnbsMessage {
	public int alertId;
	public String alertString;
	public int messageId;
	public String messageBody;
	public String receivedDateString;
	public long receivedDate;
	public int icon;
	int read;
	
    private String moreDetails;
    
 
	String  snbsCategory;
    String  snbsResponse;
    String  snbsSeverity;
    String  snbsUrgency;
    String  snbsCertainity;
	public int getAlertId() {
		return alertId;
	}
	public void setAlertId(int alertId) {
		this.alertId = alertId;
		setAlertString(SNBSUtil.getAlertString(alertId));
	}
	public String getAlertString() {
		return alertString;
	}
	public void setAlertString(String alertString) {
		this.alertString = alertString;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	public String getReceivedDateString() {
		return receivedDateString;
	}
	public void setReceivedDateString(String receivedDateString) {
		this.receivedDateString = receivedDateString;
	}
	public long getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(long receivedDate) {
		this.receivedDate = receivedDate;
		setReceivedDateString(DateTimeFormater.parseTimeInMillisToString(receivedDate));
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public int getRead() {
		return read;
	}
	public void setRead(int read) {
		this.read = read;
	}
    
	public String getSnbsCategory() {
		return snbsCategory;
	}
	public void setSnbsCategory(String snbsCategory) {
		this.snbsCategory = snbsCategory;
	}
	public String getSnbsResponse() {
		return snbsResponse;
	}
	public void setSnbsResponse(String snbsResponse) {
		this.snbsResponse = snbsResponse;
	}
	public String getSnbsSeverity() {
		return snbsSeverity;
	}
	public void setSnbsSeverity(String snbsSeverity) {
		this.snbsSeverity = snbsSeverity;
	}
	public String getSnbsUrgency() {
		return snbsUrgency;
	}
	public void setSnbsUrgency(String snbsUrgency) {
		this.snbsUrgency = snbsUrgency;
	}
	public String getSnbsCertainity() {
		return snbsCertainity;
	}
	public void setSnbsCertainity(String snbsCertainity) {
		this.snbsCertainity = snbsCertainity;
	}
	
	public String getMoreDetails() {
		return moreDetails;
	}
	public void setMoreDetails(String moreDetails) {
		this.moreDetails = moreDetails;
	}
    
}
