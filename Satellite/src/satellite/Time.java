package satellite;

import java.util.logging.*;
import org.joda.time.*;

import hla.rti1516e.time.*;

public class Time {

	private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());
	
	private static final long LOOKAHEAD_USEC = 1000000;
	
	private final DateTime simulationEphoc;

	private HLAinteger64Interval lookaheadInterval = null;
	private HLAinteger64Time logical_time = null;
	private long federateTime;

	private HLAinteger64TimeFactory time_factory = null;
	private long executionCounter;
	
	public Time(DateTime simulationEphoc, ) {
		this.simulationEphoc = new DateTime(simulationEphoc);
		this.time_factory = (HLAinteger64TimeFactory) Federate.getInstance().getTimeFactory();
	}
	
}
