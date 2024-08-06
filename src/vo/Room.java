package vo;

public class Room {
    private int type;
    private int price;
    
	public Room(int type, int price) {
		super();
		this.type = type;
		this.price = price;
	}
	public Room() {}
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "Room [type=" + type + ", price=" + price + "]";
	}
}
