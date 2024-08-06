package exception;

public class HouseNotFoundException extends Exception {
	public HouseNotFoundException(String message) {
		super(message);
	}
	
	public HouseNotFoundException() {
		this("게스트 하우스를 찾을 수 없습니다.");
		
	}
}
