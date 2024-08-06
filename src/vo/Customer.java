package vo;

import java.util.List;

public class Customer extends User {
    private String gender;
    private List<Receipt> receipts;
    
	public Customer(String gender, List<Receipt> receipts) {
		super();
		this.gender = gender;
		this.receipts = receipts;
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
		return "Customer [gender=" + gender + ", receipts=" + receipts + "]";
	}
}