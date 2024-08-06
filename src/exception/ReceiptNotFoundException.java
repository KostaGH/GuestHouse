package exception;

public class ReceiptNotFoundException extends Exception {
	public ReceiptNotFoundException(String message) {
		super(message);
	}
	
	public ReceiptNotFoundException() {
		this("예약 내역을 찾을 수 없습니다.");
		
	}
}
