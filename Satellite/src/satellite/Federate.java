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
	private TimeQueryReturn startingGALT;
	// private Time time = null;
	
	private boolean connected = false;
	private boolean advancing = false;
	private boolean regulating = false;
	private boolean constrained = false;

	public Federate(String rtiHost, String rtiPort, long timetick, boolean HLA_TIME_CONSTRAINED) {
		this.rtiHost = rtiHost;
		this.rtiPort = rtiPort;
		// attempt to create rti ambassador
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
			if (currentTime != null) {
				rti_ambassador.timeAdvanceRequest(currentTime);
			} else {
				System.out.println(
						"can't do that, time is null (did you get initial time or is the time regulating federate not running?)");
			}
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	public boolean advanceToCurrentHLAtime() {
		try {
			startingGALT = rti_ambassador.queryGALT();
		} catch (Exception e) {
			return false;
		}

		if (startingGALT.timeIsValid) {
			try {
				rti_ambassador.timeAdvanceRequest(startingGALT.time);
			} catch (Exception e) {
				return true;
			}
		} else {
			return false;
		}
		
		while (!isAdvancing()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) {
				LOGGER.log(Level.SEVERE, e.getMessage());
			}
		}
	}

	public void timeAdvanceGrant(LogicalTime theTime) {
		try {

			deltaTime = theTime.distance(currentTime);
			System.out.println("grant i" + deltaTime);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
		currentTime = theTime;
		System.out.println("grant " + currentTime);
	}

	public void Join() {
		connection.Join(this, rti_ambassador, rtiHost, rtiPort);
		try {
			rti_ambassador.enableTimeConstrained();
			setAdvancing(true);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

	public void Resign() {
		connection.Resign(rti_ambassador);
	}

	public RTIambassador getInstance() {
		return rti_ambassador;
	}

	public boolean isConnected() {
		return connection.isConnected();
	}
	
	public boolean isAdvancing() {
		return advancing;
	}
	
	public void setAdvancing(boolean advancing) {
		this.advancing = advancing;
	}
}
