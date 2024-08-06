package exception;

public class DuplicateNoException extends Exception {
	public DuplicateNoException(String msg) {
		super(msg);
	}
	
	public DuplicateNoException() {
		this("이미 존재하는 데이터입니다.");
	}
}
