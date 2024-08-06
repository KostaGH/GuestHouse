package vo;

import java.util.List;

public class GuestHouse {
    private int houseNo;
    private String sellerId;
    private String housePhone;
    private String houseName;
    private String location;
    private double avgGrade;
    private List<Room> rooms;
    
	public GuestHouse(int houseNo, String sellerId, String housePhone, String houseName, String location,
			List<Room> rooms) {
		super();
		this.houseNo = houseNo;
		this.sellerId = sellerId;
		this.housePhone = housePhone;
		this.houseName = houseName;
		this.location = location;
		this.rooms = rooms;
	}
	public GuestHouse(String sellerId, String housePhone, String houseName, String location,
			List<Room> rooms) {
		super();
		this.sellerId = sellerId;
		this.housePhone = housePhone;
		this.houseName = houseName;
		this.location = location;
		this.rooms = rooms;
	}
	public GuestHouse() {}
	
	
	
	public GuestHouse(int houseNo, String sellerId, String housePhone, String houseName, String location,
			double avgGrade, List<Room> rooms) {
		super();
		this.houseNo = houseNo;
		this.sellerId = sellerId;
		this.housePhone = housePhone;
		this.houseName = houseName;
		this.location = location;
		this.avgGrade = avgGrade;
		this.rooms = rooms;
	}
	public int getHouseNo() {
		return houseNo;
	}
	public void setHouseNo(int houseNo) {
		this.houseNo = houseNo;
	}
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}
	public String getHousePhone() {
		return housePhone;
	}
	public void setHousePhone(String housePhone) {
		this.housePhone = housePhone;
	}
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<Room> getRooms() {
		return rooms;
	}
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
	
	@Override
	public String toString() {
		return "GuestHouse [houseNo=" + houseNo + ", sellerId=" + sellerId + ", housePhone=" + housePhone + ", houseName="
				+ houseName + ", location=" + location +((avgGrade!=0) ? ", avgGrade=" + avgGrade : "") + ", rooms=" + rooms + "]";
	}
}
