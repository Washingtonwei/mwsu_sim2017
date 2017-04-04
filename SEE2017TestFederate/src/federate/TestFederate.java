package federate;

import hla.rti1516e.RTIambassador;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfixedArray;
import hla.rti1516e.encoding.HLAfloat32LE;
import hla.rti1516e.encoding.HLAfloat64LE;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAinteger32LE;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.*;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

import model.interactionClass.MTRMode;
import model.interactionClass.ModeTransitionRequest;
import model.objectClass.ExecutionConfiguration;
import model.objectClass.ExecutionMode;

import org.apache.logging.log4j.*;

import constellation.Constellation;
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
	
	private boolean constellationInitialized = false;

	private final static Logger logger = LogManager.getLogger(TestFederate.class);

	private String LOCAL_SETTINGS_DESIGNATOR = null;

	private ModeTransitionRequest modeTransition = null;

	private ExecutionConfiguration exco = null;

	private HLAModule hla_module = null;

	private ShutdownTask shutdownTask = null;
	
	//encoder to send data
	private EncoderFactory encoder_factory;

	private long timeCycle;
	private Constellation 	constellation;
	public HLAunicodeString 					string_encoder;
	public HLAinteger32LE 					integer_32LE_encoder;
	public HLAfloat64LE 						float_64LE_encoder;
	public HLAfloat32LE 						float_32LE_encoder;
	public HLAboolean 						boolean_encoder;
	public HLAinteger32BE					integer_encoder;
	public HLAfixedArray<HLAfloat64LE>		vector_encoder;
	
	private static volatile boolean _reservationComplete;
	private static volatile boolean _reservationSucceeded;
	private static final Object _reservationSemaphore = new Object();

	public TestFederate(SEEAbstractFederateAmbassador seefedamb) throws RTIinternalError {
		super(seefedamb);
		this.modeTransition = new ModeTransitionRequest();
		this.exco = new ExecutionConfiguration();
		this.hla_module = new HLAModule(seefedamb);
		this.shutdownTask = new ShutdownTask(this);
		this.encoder_factory = ((TestFederateAmbassador)seefedamb).getEncoderFactory();
		this.constellationInitialized = false;
	}

	//TODO: add encoder, decoder, class handle, and publish to configureAndStart()
	public void configureAndStart(Configuration config)
			throws ConnectionFailed, InvalidLocalSettingsDesignator, UnsupportedCallbackModel,
			CallNotAllowedFromWithinCallback, RTIinternalError, CouldNotCreateLogicalTimeFactory,
			FederationExecutionDoesNotExist, InconsistentFDD, ErrorReadingFDD, CouldNotOpenFDD, SaveInProgress,
			RestoreInProgress, NotConnected, MalformedURLException, FederateNotExecutionMember, InstantiationException,
			IllegalAccessException, NameNotFound, InvalidObjectClassHandle, AttributeNotDefined, ObjectClassNotDefined,
			SubscribeException, UnsubscribeException, InvalidInteractionClassHandle, InteractionClassNotDefined,
			InteractionClassNotPublished, InteractionParameterNotDefined, PublishException {

		// -------------------- Federation Join Process -------------------- //
		System.out.println("1");
		/*
		 * 1. Start Configure the SKF core components
		 */
		super.configure(config);
		super.subscribeSubject(this);

		System.out.println("2");
		/*
		 * 2. Connect to RTI
		 * 
		 * For VT MAK LOCAL_SETTINGS_DESIGNATOR = ""; For Pitch RTI
		 * LOCAL_SETTINGS_DESIGNATOR = "crcHost=" + <crcHost> + "\ncrcPort=" +
		 * <crcPort>;
		 */
		LOCAL_SETTINGS_DESIGNATOR = "crcHost=" + config.getCrcHost() + "\ncrcPort=" + config.getCrcPort();
		super.connectOnRTI(LOCAL_SETTINGS_DESIGNATOR);

		System.out.println("3");
		/**
		 * 3. Join the Federation Execution
		 */
		super.joinIntoFederationExecution();

		/**
		 * 4. Enable Asynchronous Delivery Already done through the use of the
		 * 'configuration.json' file. Please make sure that in the
		 * 'configuration.json' file the 'asynchronousDelivery' attribute is set
		 * to 'true'
		 */

		// -------------------- Federate Initialization Process
		// -------------------- //

		/**
		 * 1. Setup RTI Handles Not needed because it is handled by the skf core
		 * components
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
		// super.waitForElementDiscovery(ExecutionConfiguration.class);

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
		// super.waitForAttributeValueUpdate(ExecutionConfiguration.class);

		System.out.println("8");
		/**
		 * 6. Publish Mode Transition Request (MTR) Interaction
		 */
		super.publishInteraction(modeTransition);

		System.out.println("9");
		/**
		 * 7. Publish and Subscribe Federate Object Class Attributes and
		 * Interaction Classes 8. Reserve All Federate Object Instance Names 9.
		 * Wait for All Federate Object Instance Name Reservation
		 * Success/Failure Callbacks 10. Register Federate Object Instances 11.
		 * Wait for All Required Objects to be Discovered
		 *
		 * Use here: - super.subscribeElement(objectClass); -
		 * super.subscribeInteraction(interactionClass);
		 */
		super.subscribeReferenceFrame(FrameType.MoonCentricFixed);

		/**
		 * 12. Setup HLA Time Management: Enable Time Constrained Disable Time
		 * Regulating Already done by using the 'configuration.json' file.
		 * Please make sure that in the 'configuration.json' file the: -
		 * 'timeConstrained' attribute is set to 'true' - 'timeRegulating'
		 * attribute is set to 'false'
		 */

		/**
		 * 13. Query GALT and Time Advance to GALT Already done by the skf core
		 * components
		 */

		System.out.println("10");
		/**
		 * 14. Goto Execution
		 */
		super.startExecution();

		timeCycle = getTime().getFederationExecutionTimeCycle();
		constellation = new Constellation(this, 21);
		constellation.addEntity("tower1", 864370, 6657762, 3637152);
		constellation.addEntity("tower2", -864370, -6657762, -3637152);
	}

	// this is where we will propagate satellites
	int i = 0;

	@Override
	protected void doAction() {
		// get the current time cycle, calculate the difference (should always
		// be 1000000) and propagate by that amount of time
		long temp = getTime().getFederationExecutionTimeCycle();
		long timeDifference = temp - timeCycle;
		timeCycle = temp;
		// need to change orbit class so it fits our satellite/moon scenario
		if (constellation != null) {
			// orbits per day / seconds in a day / microseconds in a second * the time difference
			constellation.Propagate(Math.PI / 360);
		}
		/*
		 * proactive behavior
		 */
		logger.log(Level.INFO, "step: " + (i++));

	}

	// this is where we will respond to messages (i think)
	@Override
	public void update(Observable o, Object arg) {

		/*
		 * reactive behavior
		 */
		if (arg instanceof ExecutionConfiguration) {
			logger.log(Level.INFO, "**** Update ExecutionConfiguration ****");
			exco = ((ExecutionConfiguration) arg);
			if (exco.getNext_execution_mode() == ExecutionMode.EXEC_MODE_SHUTDOWN)
				new Thread(shutdownTask).start();

		}

		if (arg instanceof ReferenceFrame) {
			logger.log(Level.INFO, "**** Update ReferenceFrame ****");
			logger.log(Level.INFO, (ReferenceFrame) arg);
		}

	}

	protected void createEncoders()
	{
		try {
			string_encoder = encoder_factory.createHLAunicodeString();
			integer_32LE_encoder = encoder_factory.createHLAinteger32LE();
			float_64LE_encoder = encoder_factory.createHLAfloat64LE();
			float_32LE_encoder = encoder_factory.createHLAfloat32LE();
			vector_encoder = encoder_factory.createHLAfixedArray(
					encoder_factory.createHLAfloat64LE(), 
					encoder_factory.createHLAfloat64LE(),
					encoder_factory.createHLAfloat64LE() );
		}catch (Exception e) {
			System.out.println("Failed to initialize Encoders.");
			System.out.println(e.getMessage() );
		}
	}
	
	public HLAModule getHLAModule() {
		return this.hla_module;
	}

	public void sendGoToShutdown() 
	{

		modeTransition.setExecution_mode(MTRMode.MTR_GOTO_SHUTDOWN);
		try {
			super.updateInteraction(modeTransition);
		} catch (UpdateException | InteractionClassNotPublished | InteractionParameterNotDefined
				| InteractionClassNotDefined | SaveInProgress | RestoreInProgress | FederateNotExecutionMember
				| NotConnected | RTIinternalError e) {
			e.printStackTrace();
		}

	}
	
	public RTIambassador getAmbassador() {
		return hla_module.getAmbassador();
	}
	
	public boolean isConstellationInitialized()
	{
		return constellationInitialized;
	}
	
	public void setConstellationInitialized(boolean constellationInitialized)
	{
		this.constellationInitialized = constellationInitialized;
	}
	
	//Reserve the object name
	public void reserveObjectInstanceName (String name)
	{
		try{
			_reservationComplete = false;
			synchronized (_reservationSemaphore) 
			{
				hla_module.getAmbassador().reserveObjectInstanceName (name);
				System.out.println("in reserve");
				//wait for response from RTI
				while(!_reservationComplete)
				{
					try{
						_reservationSemaphore.wait();
					}catch (InterruptedException ignored)
					{
						
					}
				}
				System.out.println("after wait");
			}
		}catch(RTIexception e){
			System.out.println("RTI exception when reserving name: " + e.getMessage());
			return;
		}catch (Exception e){
			System.out.println("Illegal name?");
		}
	}


}
