package federate;

import hla.rti1516e.exceptions.*;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import model.interactionClass.MTRMode;
import model.interactionClass.ModeTransitionRequest;
import model.objectClass.ExecutionConfiguration;
import model.objectClass.ExecutionMode;
import satellite.Orbit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import siso.smackdown.frame.FrameType;
import siso.smackdown.frame.ReferenceFrame;
import skf.config.Configuration;
import skf.core.SEEAbstractFederate;
import skf.core.SEEAbstractFederateAmbassador;
import skf.exception.PublishException;
import skf.exception.SubscribeException;
import skf.exception.UnsubscribeException;
import skf.exception.UpdateException;

public class TestFederate extends SEEAbstractFederate implements Observer {
	
	private final static Logger logger = LogManager.getLogger(TestFederate.class);
	
	private String LOCAL_SETTINGS_DESIGNATOR = null;
	
	private ModeTransitionRequest modeTransition = null;
	
	private ExecutionConfiguration exco = null;
	
	private HLAModule hla_module = null;
	
	private ShutdownTask shutdownTask = null;
	
	private long timeCycle;
	private Orbit orbit;

	private double last_theta;
	private double last_radius;

	public TestFederate(SEEAbstractFederateAmbassador seefedamb) throws RTIinternalError {
		super(seefedamb);
		this.modeTransition = new ModeTransitionRequest();
		this.exco = new ExecutionConfiguration();
		this.hla_module  = new HLAModule(seefedamb);
		this.shutdownTask = new ShutdownTask(this);
	}
	
	public void configureAndStart(Configuration config) throws ConnectionFailed, InvalidLocalSettingsDesignator, 
														UnsupportedCallbackModel, CallNotAllowedFromWithinCallback, RTIinternalError, 
														CouldNotCreateLogicalTimeFactory, FederationExecutionDoesNotExist, InconsistentFDD, 
														ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress, RestoreInProgress, NotConnected, 
														MalformedURLException, FederateNotExecutionMember, InstantiationException, 
														IllegalAccessException, NameNotFound, InvalidObjectClassHandle, AttributeNotDefined, 
														ObjectClassNotDefined, SubscribeException, UnsubscribeException, 
														InvalidInteractionClassHandle, InteractionClassNotDefined, InteractionClassNotPublished, 
														InteractionParameterNotDefined, PublishException {
		
		// -------------------- Federation Join Process -------------------- //
		System.out.println("1");
		/*
		 *  1. Start
		 *  Configure the SKF core components
		 */
		super.configure(config);
		super.subscribeSubject(this);

		System.out.println("2");
		/*
		 * 2. Connect to RTI 
		 * 
		 * For VT MAK LOCAL_SETTINGS_DESIGNATOR = "";
		 * For Pitch RTI LOCAL_SETTINGS_DESIGNATOR = "crcHost=" + <crcHost> + "\ncrcPort=" + <crcPort>;
		 */
		LOCAL_SETTINGS_DESIGNATOR = "crcHost="+config.getCrcHost()+"\ncrcPort="+config.getCrcPort();
		super.connectOnRTI(LOCAL_SETTINGS_DESIGNATOR);

		System.out.println("3");
		/**
		 * 3. Join the Federation Execution
		 */
		super.joinIntoFederationExecution();
		
		/**
		 * 4. Enable Asynchronous Delivery 
		 * Already done through the use of the 'configuration.json' file.
		 * Please make sure that in the 'configuration.json' file the 'asynchronousDelivery' attribute is set to 'true'
		 */
		
		
		// -------------------- Federate Initialization Process -------------------- //
		
		/**
		 * 1. Setup RTI Handles
		 * Not needed because it is handled by the skf core components
		 */

		System.out.println("4");
		/**
		 * 2. Subscribe Execution Control Object (ExCO) Class Attributes
		 */
		super.subscribeElement(ExecutionConfiguration.class);

		System.out.println("5");
		/**
		 * 3. Wait for ExCO Discovery
		 */
		//super.waitForElementDiscovery(ExecutionConfiguration.class);

		System.out.println("6");
		/*
		 * 4. Request ExCO Update
		 */
		super.requestAttributeValueUpdate(ExecutionConfiguration.class);

		System.out.println("7");
		/**
		 * 5. Wait for ExCO Update and Check for Mode Transition to Shutdown.
		 * The callback is managed by the 'update' method
		 */
		//super.waitForAttributeValueUpdate(ExecutionConfiguration.class);

		System.out.println("8");
		/**
		 * 6. Publish Mode Transition Request (MTR) Interaction
		 */
		super.publishInteraction(modeTransition);

		System.out.println("9");
		/**
		 * 7. Publish and Subscribe Federate Object Class Attributes and Interaction Classes
		 * 8. Reserve All Federate Object Instance Names
		 * 9. Wait for All Federate Object Instance Name Reservation Success/Failure Callbacks
		 * 10. Register Federate Object Instances
		 * 11. Wait for All Required Objects to be Discovered
		 *
		 * Use here: 
		 * -  super.subscribeElement(objectClass);
		 * -  super.subscribeInteraction(interactionClass);
		 */
		super.subscribeReferenceFrame(FrameType.MoonCentricFixed);
		
		/**
		 * 12. Setup HLA Time Management: Enable Time Constrained Disable Time Regulating
		 * Already done by using the 'configuration.json' file.
		 * Please make sure that in the 'configuration.json' file the:
		 * - 'timeConstrained' attribute is set to 'true'
		 * - 'timeRegulating' attribute is set to 'false'
		 */
		
		/**
		 * 13. Query GALT and Time Advance to GALT
		 * Already done by the skf core components
		 */

		System.out.println("10");
		/**
		 * 14. Goto Execution
		 */
		super.startExecution();
		
		timeCycle = getTime().getFederationExecutionTimeCycle();
		orbit = new Orbit();

		last_theta = orbit.getTrueAnomaly();
		last_radius = orbit.getRadius();
	}
	
	//this is where we will propagate satellites
	int i = 0;
	@Override
	protected void doAction() {
		//get the current time cycle, calculate the difference (should always be 1000000) and propagate by that amount of time
		long temp = getTime().getFederationExecutionTimeCycle();
		long timeDifference = temp - timeCycle;
		timeCycle = temp;
		//need to change orbit class so it fits our satellite/moon scenario
		if (orbit != null) {
			//orbits per day / seconds in a day / microseconds in a second * the time difference
			orbit.Propogate(12.62256095 / 86400 / 1000000 * timeDifference);
			System.out.println(String.format("Time: %1$d rDiff: %2$fm tDiff: %3$f°", i, 
					Math.abs(last_radius - orbit.getRadius()), Math.abs(last_theta - orbit.getTrueAnomaly())));
			last_theta = orbit.getTrueAnomaly();
			last_radius = orbit.getRadius();
		}
		/*
		 *  proactive behavior
		 */
		logger.log(Level.INFO, "step: " + (i++));
		
	}

	//this is where we will respond to messages (i think)
	@Override
	public void update(Observable o, Object arg) {
		
		/*
		 *  reactive behavior
		 */
		if(arg instanceof ExecutionConfiguration){
			logger.log(Level.INFO, "**** Update ExecutionConfiguration ****");
			exco = ((ExecutionConfiguration)arg);
			if(exco.getNext_execution_mode() == ExecutionMode.EXEC_MODE_SHUTDOWN)
				new Thread(shutdownTask).start();
			
		}
		
		if(arg instanceof ReferenceFrame){
			logger.log(Level.INFO, "**** Update ReferenceFrame ****");
			logger.log(Level.INFO, (ReferenceFrame)arg);
		}
		
	}

	public HLAModule getHLAModule() {
		return this.hla_module;
	}

	public void sendGoToShutdown() {

		modeTransition.setExecution_mode(MTRMode.MTR_GOTO_SHUTDOWN);
		try {
			super.updateInteraction(modeTransition);
		} catch (UpdateException | InteractionClassNotPublished | InteractionParameterNotDefined | InteractionClassNotDefined | SaveInProgress | RestoreInProgress | FederateNotExecutionMember | NotConnected | RTIinternalError e) {
			e.printStackTrace();
		}

	}

}
