package satellite;

public class Main {
	
	static final long TIME_TICK = 1000000;        // microseconds.
    static final boolean HLA_CONSTRAIN_TIME = true;  //set to false for debug mode with no HLA Timing
	
	public static void main(String[] args) {
		System.out.println("Starting MWSU Satellite Federate");
		
		//create federate and connect to federation
		Federate federate = new Federate("127.0.0.1", TIME_TICK, HLA_CONSTRAIN_TIME);
		
		//sleep for 10 seconds
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//resign from federation
		federate.resign();
		
		System.out.println("Stopping MWSU Satellite Federate");
	}

}