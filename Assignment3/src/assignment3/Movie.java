package assignment3;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Movie implements Comparable<Movie>{
	
	private String title;
	private int startTime;
	private int finishTime;
	private int numOfReservedSeat;
	private double share;
	private int totalSales;
	private static int numOfMovie;
	private char[][] seat = new char[10][10];
	private ArrayList<Member> waitingMember = new ArrayList<>();
	
	
	public Movie() {

	}
	
	public Movie(String title, int startTime, int finishTime) {
		this.title = title;
		this.startTime = startTime;
		this.finishTime  = finishTime;
		this.numOfReservedSeat = 0;
		this.share = 0.0;
		this.totalSales = 0;
		seat[0][0] = ' ';
		for(int i = 1; i < 7; i++) {
			seat[0][i] = (char)(i+48);
			seat[i][0] = (char)(i+64);
			for(int j = 1; j < 7; j++) {
				seat[i][j] = 'O';
			}
		}
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public int getStartTime() {
		return this.startTime;
	}
	
	public int getFinishTime() {
		return this.finishTime;
	}
	
	public int getNumOfReservedSeat() {
		return numOfReservedSeat;
	}
	
	public void setNumOfReservedSeat(int numOfReservedSeat) {
		this.numOfReservedSeat = numOfReservedSeat;
	}

	public double getShare() {
		return this.share;
	}


	public void setShare(double share) {
		this.share = share;
	}
	
	public int getTotalSales() {
		return totalSales;
	}
	
	public void setTotalSales(int totalSales) {
		this.totalSales = totalSales;
	}

	public static int getNumOfMovie() {
		return numOfMovie;
	}
	
	public static void setNumOfMovie(int numOfMovie) {
		Movie.numOfMovie = numOfMovie;
	}

	public char[][] getSeat(){
		return seat;
	}
	
	public synchronized ArrayList<Member> getWaitingMember() {
		return waitingMember;
	}

	public synchronized void setWaitingMember(ArrayList<Member> waitingMember) {
		this.waitingMember = waitingMember;
	}

	public synchronized boolean seatManage(String seatNum) {
		int seatRow = (int)seatNum.charAt(0) - 64;
		int seatCol = (int)seatNum.charAt(1) - 48;
		
		if(this.seat[seatRow][seatCol] == 'X') {
			return false;
		}
		else {
			this.seat[seatRow][seatCol] = 'X';
			return true;
		}
	}
	
	public synchronized void movieReservation(){

		this.numOfReservedSeat++;
		Ticket.setNumOfTicket(Ticket.getNumOfTicket()+1);
		this.share = 100*this.numOfReservedSeat/36;
		this.totalSales += 10000;
	}
	
	public synchronized void cancelReservation(Ticket ticket) {
		int seatRow = (int)ticket.getSeatNumber().charAt(0) - 64;
		int seatCol = (int)ticket.getSeatNumber().charAt(1) - 48;
		this.seat[seatRow][seatCol] = 'O';
		this.numOfReservedSeat--;
		this.share = 100*this.numOfReservedSeat/36;
		this.totalSales -= 10000;
	}
	
	public synchronized void cancelReservation(Movie[] movies, Ticket[] allTicket, Member currentUser) {
		Scanner scan = new Scanner(System.in);
		
		if(currentUser.getTicketPerPerson() == 0) {
			System.out.println("예매한 영화가 없습니다.");
			return;
		}
		
		for(int i = 1; i <= currentUser.getTicketPerPerson(); i++) {
			System.out.println((i) + ". Ticket: " + currentUser.getTickets()[i].getTicketNumber() + " / 제목: " + currentUser.getTickets()[i].getMovieTitle() + " / 상영시간: " 
		+ currentUser.getTickets()[i].getMovieStartTime() + ":00 - " + currentUser.getTickets()[i].getMovieFinishTime() + ":00 / Seat: " + currentUser.getTickets()[i].getSeatNumber());
		}
		System.out.print("어떤 티켓을 취소하시겠습니까?(돌아가기: 0) ");
		int cancelNumber = scan.nextInt();
		if(cancelNumber == 0) return;
		Ticket t = currentUser.getTickets()[cancelNumber];
		Ticket.cancelReservation(t);
		cancelReservation(t);
		for(int i = cancelNumber; i < currentUser.getTicketPerPerson(); i++) {
			currentUser.getTickets()[i] = currentUser.getTickets()[i+1];
		}
		currentUser.setTicketPerPerson(currentUser.getTicketPerPerson()-1);
		
		notify();
	
		System.out.println("해당 티켓을 취소하였습니다.");
	}
	
	private synchronized String isCancelled() {
		for(char i = 0; i < 6; i++) {
			for(char j = 0; j < 6; j++) {
				String seat = Character.toString((char)(i+65)) + Character.toString((char)(j+49));
				if(seatManage(seat) == true) {
					return seat;
				}
			}
		}
		return null;
	}
	
	public synchronized void ReservationByBooking() throws InterruptedException {
		
		if(numOfReservedSeat == 36 && waitingMember.get(0).getWaitingTime()!= 0) {
			wait(waitingMember.get(0).getWaitingTime() * 60 * 1000);
		}
		
		BufferedWriter buffer = null;
		try {
			buffer = new BufferedWriter(new FileWriter("Display.txt", true));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintWriter writer = new PrintWriter(buffer, true);
		
		SimpleDateFormat currentTime = new SimpleDateFormat ("yyyy.MM.dd.HH:mm");
		Date time = new Date();
		
		String seat = isCancelled();
		if(seat != null) {
			Ticket.getAllTickets()[Ticket.getNumOfTicket()] = new Ticket();
			movieReservation();
			Ticket.getAllTickets()[Ticket.getNumOfTicket()-1].buyTicket(this, waitingMember.get(0), seat);
			writer.write(currentTime.format(time) + "에 " + waitingMember.get(0).getID() + "님의 " + title + " 영화 " + seat + " 좌석이 예매되었습니다.\n");
			Ticket.getAllTickets()[Ticket.getNumOfTicket()-1].setTicketNumber(Ticket.getNumOfTicket()-1);
		}
		waitingMember.remove(waitingMember.get(0));
		if(waitingMember.size() != 0) {
			waitingMember.get(0).setPriority(10);
		}
		
		writer.flush();
		writer.close();
	}

	@Override
	public int compareTo(Movie o) {
		return o.getNumOfReservedSeat() - this.numOfReservedSeat;
	}
}