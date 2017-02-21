package satellite;

public class Main {
	
	static final long TIME_TICK = 1000000;        // microseconds.
    static final boolean HLA_CONSTRAIN_TIME = true;  //set to false for debug mode with no HLA Timing
	
	public static void main(String[] args) {
		System.out.println("Starting MWSU Satellite Federate");
		
//		//create federate and connect to federation
//		Federate federate = new Federate("127.0.0.1", TIME_TICK, HLA_CONSTRAIN_TIME);
//		
//		//sleep for 10 seconds
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		//resign from federation
//		federate.resign();
		//7350.24607837
		//7352.01379247
		Orbit orbit = new Orbit();
		double last_theta = orbit.getTrueAnomaly();
		double last_radius = orbit.getRadius();
		for (int i = 0; i < 60; i++) {
			orbit.Propogate(0.00001157407407);
			//if (i%1000 == 0) {
				System.out.println(String.format("Time: %1$d rDiff: %2$fm tDiff: %3$f°", i, 
						Math.abs(last_radius - orbit.getRadius()), Math.abs(last_theta - orbit.getTrueAnomaly())));
				last_theta = orbit.getTrueAnomaly();
				last_radius = orbit.getRadius();
			//}
		}
		
		System.out.println("Stopping MWSU Satellite Federate");
	}

}