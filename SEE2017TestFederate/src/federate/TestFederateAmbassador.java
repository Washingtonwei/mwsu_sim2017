package federate;

import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.SynchronizationPointFailureReason;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.exceptions.RTIinternalError;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import skf.core.SEEAbstractFederateAmbassador;
import synchronizationPoint.SynchronizationPoint;

public class TestFederateAmbassador extends SEEAbstractFederateAmbassador {

	private final static Logger logger = LogManager.getLogger(TestFederateAmbassador.class);


	public TestFederateAmbassador() {
		super();
	}

	// ---------------------- SynchronizationPoint ------------------------
	@Override
	public void synchronizationPointRegistrationFailed(String label, SynchronizationPointFailureReason reason ) {
		SynchronizationPoint sp = SynchronizationPoint.lookup(label);
		if(sp != null)
			System.out.println("Failed to register sync point: " + sp + ", reason: " + reason);
	}

	@Override
	public void synchronizationPointRegistrationSucceeded(String label) {
		SynchronizationPoint sp = SynchronizationPoint.lookup(label);
		if(sp != null){
			sp.isRegistered(true);
			System.out.println("Successfully registered sync point: " + sp);
		}
		else
			throw new IllegalArgumentException("SynchronizationPoint["+sp+"] not defined.");
	}

	@Override
	public void announceSynchronizationPoint(String label, byte[] tag) {
		SynchronizationPoint sp = SynchronizationPoint.lookup(label);
		if(sp != null){
			sp.isAnnounced(true);
			logger.log(Level.INFO, "Synchronization point announced: " + sp);
		}
		else
			throw new IllegalArgumentException("SynchronizationPoint["+sp+"] not defined.");
	}

	@Override
	public void federationSynchronized(String label, FederateHandleSet failed) {
		SynchronizationPoint sp = SynchronizationPoint.lookup(label);
		if(sp != null){
			sp.federationIsSynchronized(true);
			System.out.println("Federation Synchronized: " + sp);
		}
		else
			throw new IllegalArgumentException("SynchronizationPoint["+sp+"] not defined");
	}
	
	public EncoderFactory getEncoderFactory()
	{
		RtiFactory rti_factory = null;
		try {
			rti_factory = RtiFactoryFactory.getRtiFactory();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		EncoderFactory encoder_factory = null;
		try {
			encoder_factory = rti_factory.getEncoderFactory();
		} catch (RTIinternalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encoder_factory;
	}
}
