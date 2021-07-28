package assignment3;

public class InvalidMenuException extends Exception{
	
	public InvalidMenuException() {
		super("\nInvalid Access.");
	}
	
	public InvalidMenuException(int message) {
		super("\n" + message + " is an invalid menu number.");
	}
}
