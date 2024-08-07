package vo;

import java.util.List;

public class Customer extends User {
    private String gender;
    private List<Receipt> receipts;
    
	public Customer(String userId, String userName, String pw, String ssn, String phoneNum, int balance, String gender) {
		super(userId, userName, pw, ssn, phoneNum, balance);
		this.gender = gender;
	}
	public Customer() {}

	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<Receipt> getReceipts() {
		return receipts;
	}
	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}
	
	@Override
	public String toString() {
		return super.toString() + "Customer [gender=" + gender + ", receipts=" + receipts + "]";
	}
}