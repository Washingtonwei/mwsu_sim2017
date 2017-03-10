package satellite;

import java.util.Scanner;

public class Main {
	
	static final long TIME_TICK = 1000000;        // microseconds.
    static final boolean HLA_CONSTRAIN_TIME = true;  //set to false for debug mode with no HLA Timing
	
	public static void main(String[] args) {
		System.out.println("Starting MWSU Satellite Federate");
		
		//create federate and connect to federation
		Federate federate = new Federate("10.8.0.193", TIME_TICK, HLA_CONSTRAIN_TIME,"8989");
		
		
		//For the SEE 2017 event, the Simulation Ephoc has been set to 04/19/2015 20:00:00 GMT. 
		Boolean done = false;
		String command;
		Scanner sc = new Scanner(System.in);
		while (!done) {
			System.out.print("Enter a command: ");
			command = sc.nextLine();
			switch (command) {
			case "join":
				federate.Join();
				break;
			case "resign":
				federate.Resign();
				break;
			case "gameloop":
				federate.Gameloop();
				break;
			case "quit":
				done = true;
				break;
			default:
				System.out.println("Invalid command");
			}
		}
	
		sc.close();
	
		System.out.println("Stopping MWSU Satellite Federate");
	}

}