package assignment3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class Theater {	

	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
	
		Movie[] movies = new Movie[100];
		Member[] users = new Member[1000];
		Member currentUser = new Member();
		Ticket[] tickets = new Ticket[300];//8개의 영화에 각각 36개의 좌석이 있음. 36*8 = 288이므로 배열 크기 300으로 설정.
	
		readMovieList(movies);	
		readTicketList(tickets, movies);
		readUserList(users, tickets);

		while(true) {
			
			try {
				int input;	
				
				printFirstMenu();
		
				input = scan.nextInt();
				
				//로그인
				if(input == 1) { 
					currentUser = Member.logIn(users);
					while(true) {
						try {
							printSecondMenu();
							input = scan.nextInt();
							//영화 목록 
							if(input == 1) {
								while(true) {
									try {
										printListOfMovie(movies, Movie.getNumOfMovie());
										printThirdMenu();
										input = scan.nextInt();
										//예매 
										if(input == 1) {
											if(currentUser.getTicketPerPerson() == 10 && currentUser.getChkManager() == 0) {
												System.out.println("인당 최대 티켓 구매가능 매수인 10장을 이미 구매하셨습니다. 더이상 티켓 구매는 불가능합니다.");
												continue;
											}
											while(true) {
												
														printListOfMovie_2(movies, Movie.getNumOfMovie());
														input = scan.nextInt();
														if(input < 1 || input > 8) {
															throw new InvalidMenuException(input);
														}
														currentUser.setCurrentMovie(movies[input]);
														
														String seatNum = null;
														if(currentUser.getChkManager() != 0) {
															System.out.println();
															System.out.printf("\"" + movies[input].getTitle() + "\" " + "영화의 좌석 예매 점유율 : %.2f%%\n", movies[input].getShare());															System.out.printf("\"" + movies[input].getTitle() + "\" " + "영화의 총 매출액 : %d\n", movies[input].getTotalSales());
															System.out.println();
														}
														
														if(movies[input].getNumOfReservedSeat() == 36) {
															System.out.println("\n*******좌  석*******");
															for(int i = 0; i < 7; i++) {
																for(int j = 0; j < 7; j++) {
																	System.out.print(movies[input].getSeat()[i][j] + "  ");
																}
																System.out.println();
															}
															System.out.println("*******************");
															
															System.out.println("해당 영화는 매진입니다.");
															System.out.print("예약을 하면 임의의 자리를 예매 받을 수 있습니다. 예약을 진행하시겠습니까? (1. 예 / 2. 아니오) ");
															movies[input].getWaitingMember().add(currentUser);	
															movies[input].getWaitingMember().get(0).setPriority(10);
															scan.nextLine();
															int reserveBooking = scan.nextInt();
															if(reserveBooking == 1) {
																System.out.println("몇 분간 대기하시겠습니까? ");
																scan.nextLine();
																int waitingTime = 0;
																waitingTime = scan.nextInt();
																scan.nextLine();
																currentUser.setWaitingTime(waitingTime);
																currentUser.start();
																System.out.println("예약이 완료되었습니다.");
															}
															
															for(int i = 0; i < Ticket.getNumOfTicket(); i++) {
																Ticket.getAllTickets()[i].setTicketNumber(i);
															}
															
														}
														
														else {
															tickets[Ticket.getNumOfTicket()] = new Ticket();
															System.out.println("\n*******좌  석*******");
															for(int i = 0; i < 7; i++) {
																for(int j = 0; j < 7; j++) {
																	System.out.print(movies[input].getSeat()[i][j] + "  ");
																}
																System.out.println();
															}
															System.out.println("*******************");
															System.out.print("좌석을 선택해주세요(ex: A1): ");
															scan.nextLine();
															seatNum = scan.nextLine();
															if(seatNum.length() != 2) throw new NotExistSeatException(seatNum);
															else if(((int)seatNum.charAt(0) < 65 || (int)seatNum.charAt(0) > 70) || 
																	((int)seatNum.charAt(1) < 49 || (int)seatNum.charAt(1) > 54)) {
																throw new NotExistSeatException(seatNum);
															}
															else if(!movies[input].seatManage(seatNum)) {
																throw new DuplicatedReservationException(seatNum);
															}
														
															movies[input].movieReservation();
															tickets[Ticket.getNumOfTicket()-1].buyTicket(movies[input], currentUser, seatNum);
															System.out.println("제목: " + movies[input].getTitle() + "/상영시간: " 
															+ movies[input].getStartTime() + ":00 - " + movies[input].getFinishTime() + ":00");
															currentUser.getTickets()[currentUser.getTicketPerPerson()] = tickets[Ticket.getNumOfTicket()-1];
														}
													break;
												
											}
											writeTicketList(tickets, movies);
											break;
											
										}
										
										//종료 
										else if(input == 2){
											System.out.println("유저 프로그램으로 돌아갑니다.");
											break;
										}
										//입력 오류
										else {
											throw new InvalidMenuException(input);
										}
									}
									catch(InvalidMenuException e) {
										System.out.println(e.getMessage());
										continue;
									}
									catch(NotExistSeatException e) {
										System.out.println(e.getMessage());
										continue;
									}
									catch(DuplicatedReservationException e) {
										System.out.println(e.getMessage());
										continue;
									}
								}
							}
							//예매 확인 
							else if(input == 2) {
								if(currentUser.getChkManager() == 0) {
									Ticket.checkReservation(currentUser, tickets);
									scan.nextLine();
									if(scan.nextLine() == null) continue;
								}
								else {
									Ticket.checkReservationForManager(currentUser, tickets);
									scan.nextLine();
									if(scan.nextLine() == null) continue;
								}
							}
							//영화관 관리
							else if(input == 3) {
								if(currentUser.getChkManager() == 0) {
									throw new InvalidMenuException();
								}
								
								while(true) {
									
									try {
										printFourthMenu();
										input = scan.nextInt();
										//영화관 정보
										if(input == 1) {
											theaterInfo(movies);
											scan.nextLine();
											if(scan.nextLine() == null){
												break;
											}
										}
										//유저 정보
										else if(input == 2) {
											userInfo(users, tickets);
											scan.nextLine();
											if(scan.nextLine() == null){
												break;
											}
										}
										//종료
										else if(input == 3) {
											System.out.println("유저 프로그램으로 돌아갑니다.");
											break;
										}
										//입력 오류 
										else {
											throw new InvalidMenuException(input);
										}
									}
									catch(InvalidMenuException e) {
										System.out.println(e.getMessage());
										continue;
									}
								}
						
							}
							//예매 취소
							else if(input == 4) {
								movies[1].cancelReservation(movies, tickets, currentUser);
								writeTicketList(tickets, movies);
							}
							//종료
							else if(input == 5) {
								System.out.println("영화 예매 프로그램으로 돌아갑니다.");
								break;
							}
							//입력 오류
							else {
								throw new InvalidMenuException(input);
							}
						}
						catch(InvalidMenuException e) {
							System.out.println(e.getMessage());
							continue;
						}
					}
				}
				//회원가입 
				else if(input == 2) {
					if(Member.getNumOfMember() == 1000) {
						System.out.println("최대 회원 수를 초과하였습니다. 더 이상 회원가입이 불가능합니다.");
						continue;
					}
					users[Member.getNumOfMember()] = new Member();
					users[Member.getNumOfMember()].signUp(users);
					writeUserList(users);
					continue;
				}
				//종료 
				else if(input == 3) {
					System.out.println("영화 예매 프로그램을 종료합니다.");
					break;
				}
				//입력 오류 
				else {
					throw new InvalidMenuException(input);
				}
			}
			catch(InvalidMenuException e) {
				System.out.println(e.getMessage());
				continue;
			}
			catch(InvalidLoginException e) {
				System.out.println(e.getMessage());
				continue;
			}
			catch(DuplicatedIdException e) {
				System.out.println(e.getMessage());
				continue;
			}
		}
	}
	
	public static void printFirstMenu() {
		System.out.println("\n*******영화 예매 프로그램*******");
		System.out.println("1. 로그인");
		System.out.println("2. 회원가입");
		System.out.println("3. 종료");
		System.out.print("메뉴를 선택해 주세요: ");
	}
	
	public static void printSecondMenu() {
		System.out.println("\n*******유저 프로그램*******");
		System.out.println("1. 영화 목록");
		System.out.println("2. 예매 확인");
		System.out.println("3. 영화관 관리");
		System.out.println("4. 예매 취소");
		System.out.println("5. 종료");
		System.out.print("메뉴를 선택해 주세요: ");
	}
	
	public static void printThirdMenu() {
		System.out.println("\n1. 예매");
		System.out.println("2. 종료");
		System.out.print("메뉴를 선택해 주세요: ");
	}
	
	public static void printFourthMenu() {
		System.out.println("\n*******영화관 관리*******");
		System.out.println("1. 영화관 정보");
		System.out.println("2. 유저 정보");
		System.out.println("3. 종료");
		System.out.print("메뉴를 선택해주세요 : ");
	}
	
	public static void printListOfMovie(Movie[] movies, int numOfMovie) {
		System.out.println("\n*******영화 목록*******");
		for(int i = 1; i <= numOfMovie; i++) {
			System.out.println("제목: " + movies[i].getTitle() + " / 상영시간: " 
		+ movies[i].getStartTime() + ":00 - " + movies[i].getFinishTime() + ":00");
		}
		System.out.println();
	}
	
	public static void printListOfMovie_2(Movie[] movies, int numOfMovie) {
		System.out.println("\n*******영화 목록*******");
		for(int i = 1; i <= numOfMovie; i++) {
			System.out.println(i + ". 제목: " + movies[i].getTitle() + " / 상영시간: " 
		+ movies[i].getStartTime() + ":00 - " + movies[i].getFinishTime() + ":00");
		}
		System.out.println();
		System.out.print("예매할 영화를 선택해주세요: ");
	}
	
	public static void readMovieList(Movie[] movies) {
		
		Scanner inputStream = null;
		
		try {
			inputStream = new Scanner(new FileInputStream("MovieList.txt"));
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		
		int tempInt = Integer.parseInt(inputStream.nextLine());
		Movie.setNumOfMovie(tempInt);
	
		String line;
		int i = 1;
		
		while(inputStream.hasNextLine()) {
			line = inputStream.nextLine();
			String[] temp = line.split("/");
			movies[i] = new Movie(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
			i++;
		}
		inputStream.close();
	}
	
	public static void readUserList(Member[] member, Ticket[] ticket) {
		Scanner inputStream = null;
		
		try {
			inputStream = new Scanner(new FileInputStream("UserList.txt"));
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		
		if(inputStream.hasNextLine()) {
			int tempInt = Integer.parseInt(inputStream.nextLine());
			Member.setNumOfMember(tempInt);
		}
		
		String line;
		
		int i = 0;
		
		while(inputStream.hasNextLine()) {
			line = inputStream.nextLine();
			String[] temp = line.split(" ");
			member[i] = new Member(Integer.parseInt(temp[0]), temp[1], temp[2], ticket);
			i++;
		}
		inputStream.close();
		
		for(int l = 0; l < Member.getNumOfMember(); l++) {
			int ticketPerPerson = 0;
			for(int j = 0; j < Ticket.getNumOfTicket(); j++) {
				String userID = ticket[j].getUserID();
				String id = member[l].getID();
				if(userID.equals(id)){
					ticketPerPerson++;
				}
				member[l].setTicketPerPerson(ticketPerPerson);
			}
		}
	}
	
	public static void readTicketList(Ticket[] ticket, Movie[] movies) {
		
		Scanner inputStream = null;
		
		try {
			inputStream = new Scanner(new FileInputStream("TicketList.txt"));
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		//티켓 수
		if(inputStream.hasNextLine()) {
			Ticket.setNumOfTicket(Integer.parseInt(inputStream.nextLine()));
		}
		
		//영화 별 예매 수
		int i = 1;
		while(inputStream.hasNextLine()) {
			int tempInt = Integer.parseInt(inputStream.nextLine());
			movies[i].setNumOfReservedSeat(tempInt);
			if(Ticket.getNumOfTicket() != 0) {
				movies[i].setShare(100*tempInt/36.0);
			}
			movies[i].setTotalSales(10000*tempInt);
			i++;
			if(i == Movie.getNumOfMovie()+1) break;
		}
		
		//티켓 정보
		String line;
		int j = 0;
		while(inputStream.hasNextLine()) {
			line = inputStream.nextLine();
			String[] temp = line.split("/");
			for(int k = 1; k <= Movie.getNumOfMovie(); k++) {
				if(movies[k].getTitle().equals(temp[0])){
					ticket[j] = new Ticket(movies[k], temp[1], temp[2]);
					movies[k].seatManage(temp[2]);
				}
			}
			j++;
		}
		
		for(int k = 0; k < Ticket.getNumOfTicket(); k++) {
			ticket[k].setTicketNumber(k);
		}
	
		Ticket.setAllTickets(ticket);
		
		inputStream.close();
	}

	
	public static void writeMovieList(Movie[] movie) {
		
		String fileName = "MovieList.txt";
		File file = new File(fileName);
		PrintWriter outputStream = null;
		
		try {
			outputStream = new PrintWriter(new FileOutputStream(fileName));
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		outputStream.println(Movie.getNumOfMovie());
		for(int i = 1; i <= Movie.getNumOfMovie(); i++) {
			outputStream.println(movie[i].getTitle() + "/" + movie[i].getStartTime() + "/" + movie[i].getFinishTime());
		}
		outputStream.close();
	}
	
	public static void writeUserList(Member[] user) {
		
		String fileName = "UserList.txt";
		File file = new File(fileName);
		PrintWriter outputStream = null;
		
		try {
			outputStream = new PrintWriter(new FileOutputStream(fileName));
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		outputStream.println(Member.getNumOfMember());
		for(int i = 0; i < Member.getNumOfMember(); i++) {
			outputStream.println(user[i].getChkManager() + " " + user[i].getID() + " " + user[i].getPassword());
		}
		outputStream.close();
	}
	
	public synchronized static void writeTicketList(Ticket[] ticket, Movie[] movie) {
		
		String fileName = "TicketList.txt";
		File file = new File(fileName);
		PrintWriter outputStream = null;

		try {
			outputStream = new PrintWriter(new FileOutputStream(fileName));
		}
		catch(FileNotFoundException e) {
			System.out.println("File not found");
			System.exit(0);
		}
		outputStream.println(Ticket.getNumOfTicket());

		for(int i = 1; i <= Movie.getNumOfMovie(); i++) {
			outputStream.println(movie[i].getNumOfReservedSeat());
		}

		for(int i = 0 ; i < Ticket.getNumOfTicket(); i++) {
			outputStream.println(ticket[i].getMovieTitle() + "/" + ticket[i].getUserID() + "/" + ticket[i].getSeatNumber());
		}
		outputStream.close();
	}

	public static void theaterInfo(Movie[] movie) {
		Movie[] sortedMovie = new Movie[Movie.getNumOfMovie()+1];
		for(int i = 1; i <= Movie.getNumOfMovie(); i++) {
			sortedMovie[i] = movie[i];
		}
		Arrays.sort(sortedMovie,1,Movie.getNumOfMovie()+1);
		System.out.println("\n점유된 전체 좌석 수 : " + Ticket.getNumOfTicket());
		System.out.printf("전체 좌석 예매 점유율: %.2f%%\n", 100*Ticket.getNumOfTicket()/(Movie.getNumOfMovie()*36.0));
		System.out.println("영화관 총 매출액: " + 10000*Ticket.getNumOfTicket());
		System.out.println();
		System.out.println("예매율 현황");
		System.out.println("--------------------------");
		for(int i = 1; i <= Movie.getNumOfMovie() && sortedMovie[i].getNumOfReservedSeat() > 0; i++) {
			System.out.println((i) + "위 : " + sortedMovie[i].getTitle() 
					+ "(좌석수 : " + sortedMovie[i].getNumOfReservedSeat() + ")");
		}
		System.out.println("--------------------------");
		System.out.println("Press enter to go back to Theater Management");
	}

	public static void userInfo(Member[] member, Ticket[] ticket) {
		Scanner scan = new Scanner(System.in);
		String ID;
		System.out.print("\n찾으려는 ID : ");
		ID = scan.nextLine();
		Member foundUser = Member.findUser(member, ID);
		System.out.println();
		System.out.println(foundUser.getID() + " 고객님이 발행한 티켓 수 : " + foundUser.getTicketPerPerson());
		System.out.println("---------------------------------------------------------");
		Ticket.findTicketOfUser(ticket, ID);
		System.out.println("---------------------------------------------------------");
		System.out.println("Press enter to go back to Theater Management");
	}
}