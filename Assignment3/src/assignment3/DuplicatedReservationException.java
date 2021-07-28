package assignment3;

public class DuplicatedReservationException extends Exception{

	public DuplicatedReservationException() {
		super("default message");
	}
	
	public DuplicatedReservationException(String message) {
		super("\n" + message + " is already researved.");
	}
}
