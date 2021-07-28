package assignment3;

import java.util.Scanner;

public class Ticket {
	
	private String userID;
	private String movieTitle;
	private String seatNumber;
	private int movieStartTime;
	private int movieFinishTime;
	private static int numOfTicket;
	private int ticketNumber;
	private static Ticket[] allTickets = new Ticket[300];

	
	Scanner scan = new Scanner(System.in);
	
	public Ticket() {
		
	}
	
	public Ticket(Movie movie, String ID, String seatNumber) {
		this.userID = ID;
		this.movieTitle = movie.getTitle();
		this.seatNumber = seatNumber;
		this.movieStartTime = movie.getStartTime();
		this.movieFinishTime = movie.getFinishTime();
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getMovieTitle() {
		return movieTitle;
	}

	public int getMovieStartTime() {
		return movieStartTime;
	}
	
	public int getMovieFinishTime() {
		return movieFinishTime;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public static int getNumOfTicket() {
		return Ticket.numOfTicket;
	}

	public synchronized static void setNumOfTicket(int numOfTicket) {
		Ticket.numOfTicket = numOfTicket;
	}
	
	public int getTicketNumber() {
		return this.ticketNumber;
	}
	
	public void setTicketNumber(int ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	
	public static Ticket[] getAllTickets() {
		return allTickets;
	}
	
	public static void setAllTickets(Ticket[] ticket) {
		allTickets = ticket;
		for(int i = 0; i < Ticket.getNumOfTicket(); i++) {
			allTickets[i] = ticket[i];
		}
	}
	
	
	public synchronized void buyTicket(Movie movie, Member user, String seatNumber) {
		this.userID = user.getID();
		this.movieTitle = movie.getTitle();
		this.movieStartTime = movie.getStartTime();
		this.movieFinishTime = movie.getFinishTime();
		this.seatNumber = seatNumber;
		user.setTicketPerPerson(user.getTicketPerPerson()+1);
	}

	public synchronized static void checkReservation(Member user, Ticket[] allTickets) {
		System.out.println("\n*******예매 목록*******");
		for(int i = 1; i <= user.getTicketPerPerson(); i++) {
			System.out.println("Ticket: " + user.getTickets()[i].getTicketNumber() + " / 제목: " + user.getTickets()[i].getMovieTitle() + " / 상영시간: " 
		+ user.getTickets()[i].getMovieStartTime() + ":00 - " + user.getTickets()[i].getMovieFinishTime() + ":00 / Seat: " + user.getTickets()[i].getSeatNumber());
		}
		System.out.print("\nPress enter to go back to User program");
	}
	
	public synchronized static void checkReservationForManager(Member user, Ticket[] tickets) {
		System.out.println("\n관리자가 발행한 티켓 수 : " + user.getTicketPerPerson());
		System.out.println("매출액 : " + 10000 * user.getTicketPerPerson());
		System.out.println();
		checkReservation(user, tickets);
	}
	
	public static void findTicketOfUser(Ticket[] ticket, String ID) {
		for(int i = 0; i < numOfTicket; i++) {
			if(ticket[i].getUserID().equals(ID)) {
				System.out.println("Ticket : " + i + " / 제목 : " + ticket[i].movieTitle + " / 상영 시간 : " + ticket[i].movieStartTime 
						+ ":00 - " + ticket[i].movieFinishTime + ":00 / Seat : " + ticket[i].seatNumber);
			}
		}
	}
	
	public synchronized static void cancelReservation(Ticket ticket) {
		for(int i = 0; i < Ticket.numOfTicket; i++) {
			if(ticket.getMovieTitle().equals(allTickets[i].getMovieTitle()) 
					&& ticket.getSeatNumber().equals(allTickets[i].getSeatNumber())) {
				for(int j = i; j < Ticket.numOfTicket-1; j++) {
					allTickets[j] = allTickets[j+1];
				}
				Ticket.numOfTicket--;
				break;
			}
		}
	}
}	