package vo;

public class User {
    private String userId;
    private String userName;
    private String pw;
    private String ssn;
    private String phoneNum;
    private int balance;
    
	public User(String userId, String userName, String pw, String ssn, String phoneNum, int balance) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.pw = pw;
		this.ssn = ssn;
		this.phoneNum = phoneNum;
		this.balance = balance;
	}
	public User(String userName, String pw, String phoneNum, String ssn) {
		super();
		this.userName = userName;
		this.pw = pw;
		this.phoneNum = phoneNum;
		this.ssn = ssn;
	}
	public User() {}
	
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	@Override
	public String toString() {
		return "User [userId=" + userId + ", userName=" + userName + ", pw=" + pw + ", ssn=" + ssn + ", phoneNum="
				+ phoneNum + ", balance=" + balance + "]";
	}
}
