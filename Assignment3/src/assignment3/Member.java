package assignment3;

import java.util.Scanner;


public class Member extends Thread{

	private String ID;
	private String password;
	private int chkManager;
	private int ticketPerPerson;
	private static int numOfMember = 0;
	private Ticket[] tickets = new Ticket[300];
	private Movie currentMovie;
	private int waitingTime;
	
	public Member() {
		
	}
	
	public Member(int chkManager, String ID, String password, Ticket[] tickets) {
		this.chkManager = chkManager;
		this.ID = ID;
		this.password = password;
		int numOfReservedTicket = 0;
		for(int i = 0; i < Ticket.getNumOfTicket(); i++) {
			if(tickets[i].getUserID().equals(this.ID)) {
				this.tickets[++numOfReservedTicket] = tickets[i];
			}
		}
		this.ticketPerPerson = numOfReservedTicket;
	}
	
	public String getID() {
		return ID;
	}

	public String getPassword() {
		return password;
	}
	
	public int getChkManager() {
		return chkManager;
	}
	
	public int getTicketPerPerson() {
		return ticketPerPerson;
	}
	
	public void setTicketPerPerson(int ticketPerPerson) {
		this.ticketPerPerson = ticketPerPerson;
	}
	
	public static int getNumOfMember() {
		return numOfMember;
	}

	public static void setNumOfMember(int numOfMember) {
		Member.numOfMember = numOfMember;
	}
	
	public void setCurrentMovie(Movie movie) {
		this.currentMovie = movie;
	}
	
	public Ticket[] getTickets() {
		return tickets;
	}
	
	public int getWaitingTime() {
		return waitingTime;
	}
	
	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}

	public static Member logIn(Member[] members) throws InvalidLoginException {
		
		String ID;
		String password;
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("\n*******로그인*******");
		System.out.print("ID: ");
		ID = scan.nextLine();
		System.out.print("Password: ");
		password = scan.nextLine();
		
		for(int i = 0; i < numOfMember; i++) {
			if(members[i].getID().equals(ID) && members[i].getPassword().equals(password)) {
				return members[i];
			}
		}
		throw new InvalidLoginException();
	}
	
	public void signUp(Member[] members) throws DuplicatedIdException {
		
		String ID;
		String password;
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("\n*******회원가입*******");
		System.out.print("ID: ");
		ID = scan.nextLine();
		for(int i = 0; i < numOfMember; i++) {
			if(members[i].getID().equals(ID)) {
				throw new DuplicatedIdException(ID);
			}
		}
		System.out.print("password: ");
		password = scan.nextLine();
		
		System.out.print("Are you manager? : ");
		chkManager = scan.nextInt();
		
		this.ID = ID;
		this.password = password;
		
		numOfMember++;
	}
	
	public static Member findUser(Member[] member, String ID) {
		for(int i = 0; i < numOfMember; i++) {
			if(member[i].getID().equals(ID) && member[i].chkManager == 0) {
				return member[i];
			}
		}
		return null;
	}
	
	public void run(){
		try {
			currentMovie.ReservationByBooking();
			tickets[ticketPerPerson] = Ticket.getAllTickets()[Ticket.getNumOfTicket()-1];
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
