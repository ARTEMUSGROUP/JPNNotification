package beans;

public class NotificationBean {

	private int notifyId;
    private String notifyScac;
    private String billNo;
    private String status;
    private String statusCode;
    private String statusDesc;
    private String notifyStatus;
    private String entryDate;
    private String fireBaseToken;
	
    public int getNotifyId() {
		return notifyId;
	}
	public void setNotifyId(int notifyId) {
		this.notifyId = notifyId;
	}
	public String getNotifyScac() {
		return notifyScac;
	}
	public void setNotifyScac(String notifyScac) {
		this.notifyScac = notifyScac;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public String getNotifyStatus() {
		return notifyStatus;
	}
	public void setNotifyStatus(String notifyStatus) {
		this.notifyStatus = notifyStatus;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getFireBaseToken() {
		return fireBaseToken;
	}
	public void setFireBaseToken(String fireBaseToken) {
		this.fireBaseToken = fireBaseToken;
	} 
    
}
