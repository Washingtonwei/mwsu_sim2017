package satellite;

import hla.rti1516e.*;

import java.util.logging.*;

public class Federate extends NullFederateAmbassador {
	private String rtiHost;
	
	private RTIambassador rti_ambassador;
    
    private Connection connection;
	
	private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());
	
	public Federate(String rtiHost, long timetick, boolean HLA_TIME_CONSTRAINED) {
		this.rtiHost = rtiHost;
		
		//attempt to create rti ambassador
		try {
			RtiFactory rti_factory = RtiFactoryFactory.getRtiFactory();
			
			rti_ambassador = rti_factory.getRtiAmbassador();
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}

    	connection = new Connection(this, rti_ambassador, rtiHost);
	}
	
	public void resign() {
		connection.resign(rti_ambassador);
	}
}
