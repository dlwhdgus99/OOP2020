package assignment3;

public class DuplicatedIdException extends Exception{
	
	public DuplicatedIdException() {
		super("default message");
	}
	
	public DuplicatedIdException(String message) {
		super("\n" + message + " already exists.");
	}
}
