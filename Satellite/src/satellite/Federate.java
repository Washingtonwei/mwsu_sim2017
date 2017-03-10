package satellite;

import hla.rti1516e.*;

import java.util.logging.*;

public class Federate extends NullFederateAmbassador {
	private String rtiHost;
	private String rtiPort;
	
	private RTIambassador rti_ambassador;
    
    private Connection connection;
	
	private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());

	private LogicalTime currentTime;
	private LogicalTimeInterval deltaTime;
	
	public Federate(String rtiHost, long timetick, boolean HLA_TIME_CONSTRAINED,String rtiPort) {
		this.rtiHost = rtiHost;
		this.rtiPort = rtiPort;
		//attempt to create rti ambassador
		try {
			RtiFactory rti_factory = RtiFactoryFactory.getRtiFactory();
			
			rti_ambassador = rti_factory.getRtiAmbassador();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}

    	connection = new Connection();
	}
	
	public void Gameloop() {
		try {
			rti_ambassador.timeAdvanceRequest(currentTime);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void timeAdvanceGrant(LogicalTime theTime) {
		try {
			
			deltaTime = theTime.distance(currentTime);
			System.out.println("grant i"+ deltaTime);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		currentTime = theTime;
		System.out.println("grant " + currentTime);
	}
	
	public void Join() {
		connection.Join(this, rti_ambassador, rtiHost,rtiPort);
		try {
			rti_ambassador.enableTimeConstrained();
			currentTime = rti_ambassador.queryGALT().time;
			System.out.println("join " + currentTime);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void Resign() {
		connection.Resign(rti_ambassador);
	}
}
