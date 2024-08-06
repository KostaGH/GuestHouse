package vo;

public class Sales { //vo 추가 - 매출 (지남)
    private String houseName;
    private long sales;
    
	public Sales(String houseName, long sales) {
		super();
		this.houseName = houseName;
		this.sales = sales;
	}
	public Sales() {}
	
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public long getSales() {
		return sales;
	}
	public void setSales(int sales) {
		this.sales = sales;
	}
	
	@Override
	public String toString() {
		return "Sales [houseName=" + houseName + ", sales=" + sales + "]";
	}
}
