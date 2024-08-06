package vo;

import java.util.List;

public class Seller {
    private List<GuestHouse> houses;

	public Seller(List<GuestHouse> houses) {
		super();
		this.houses = houses;
	}
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
