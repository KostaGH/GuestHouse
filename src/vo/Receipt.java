package vo;

public class Receipt {
    private int reserveNo;
    private int houseNo;
    private int type;
    private String custId;
    private int grade;
    private String sDate;
    private String eDate;
    private String bDate;
    private int totalPrice;
    
	public Receipt(int reserveNo, int houseNo, int type, String custId, int grade, String sDate, String eDate,
			String bDate, int totalPrice) {
		super();
		this.reserveNo = reserveNo;
		this.houseNo = houseNo;
		this.type = type;
		this.custId = custId;
		this.grade = grade;
		this.sDate = sDate;
		this.eDate = eDate;
		this.bDate = bDate;
		this.totalPrice = totalPrice;
	}
	public Receipt() {}
	
	public int getReserveNo() {
		return reserveNo;
	}
	public void setReserveNo(int reserveNo) {
		this.reserveNo = reserveNo;
	}
	public int getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(int houseNo) {
		this.houseNo = houseNo;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public int getGrade() {
		return grade;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
	public String getsDate() {
		return sDate;
	}
	public void setsDate(String sDate) {
		this.sDate = sDate;
	}
	public String geteDate() {
		return eDate;
	}
	public void seteDate(String eDate) {
		this.eDate = eDate;
	}
	public String getbDate() {
		return bDate;
	}
	public void setbDate(String bDate) {
		this.bDate = bDate;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	@Override
	public String toString() {
		return "Receipt [reserveNo=" + reserveNo + ", houseNo=" + houseNo + ", type=" + type + ", custId=" + custId
				+ ", grade=" + grade + ", sDate=" + sDate + ", eDate=" + eDate + ", bDate=" + bDate + ", totalPrice="
				+ totalPrice + "]";
	}
}
