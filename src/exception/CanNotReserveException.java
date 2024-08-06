package exception;

public class CanNotReserveException extends Exception {
	public CanNotReserveException(String message) {
		super(message);
	}
	
	public CanNotReserveException() {
		this("예약 가능한 날짜가 아닙니다.");
		
	}
}
