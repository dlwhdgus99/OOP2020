package assignment3;

public class NotExistSeatException extends Exception{
	
	public NotExistSeatException() {
		super("default message");
	}

	public NotExistSeatException(String message) {
		super("\n" + message + " does not exist.");
	}
}
