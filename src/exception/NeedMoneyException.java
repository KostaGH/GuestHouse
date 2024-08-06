package exception;

public class NeedMoneyException extends Exception {
	public NeedMoneyException(String message) {
		super(message);
	}
	
	public NeedMoneyException() {
		this("잔액이 부족합니다.");
		
	}
}
