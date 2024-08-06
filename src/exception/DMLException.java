package exception;

public class DMLException extends Exception {
	public DMLException(String msg) {
		super(msg);
	}
	
	public DMLException() {
		this("데이터 추가, 삭제, 수정 시 작업이 제대로 진행되지 않았습니다.");
	}
}
