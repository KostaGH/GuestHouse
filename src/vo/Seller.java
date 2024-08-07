package vo;

import java.util.List;

public class Seller extends User{
    private List<GuestHouse> houses;

	public Seller(String userId, String userName, String pw, String ssn, String phoneNum, int balance) {
		super(userId, userName, pw, ssn, phoneNum, balance);
	}

//	public Seller(String userId, String userName, String pw, String ssn, String phoneNum, int balance, List<GuestHouse> houses) {
//		super();
//		this.houses = houses;
//	}
	public Seller() {}
	
	public List<GuestHouse> getHouses() {
		return houses;
	}
	public void setHouses(List<GuestHouse> houses) {
		this.houses = houses;
	}
	
	@Override
	public String toString() {
		return "Seller [houses=" + houses + "]";
	}
}
