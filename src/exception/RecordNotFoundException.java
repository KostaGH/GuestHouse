package exception;

public class RecordNotFoundException extends Exception {
	public RecordNotFoundException(String msg) {
		super(msg);
	}
	
	public RecordNotFoundException() {
		this("데이터를 찾을 수 없습니다.");
	}
}
