package assignment3;

public class InvalidLoginException extends Exception{
	
	public InvalidLoginException() {
		super("\nLogin failed.");
	}
}
