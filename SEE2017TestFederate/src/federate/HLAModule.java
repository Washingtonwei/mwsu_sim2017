package federate;

import skf.core.SEEAbstractFederateAmbassador;
import skf.core.SEERTIAmbassador;
import synchronizationPoint.SynchronizationPoint;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import hla.rti1516e.exceptions.SynchronizationPointLabelNotAnnounced;

public class HLAModule {

	private SEEAbstractFederateAmbassador fed_amb = null;
	private RTIambassador rti_amb = null;

	public HLAModule(SEEAbstractFederateAmbassador fed_amb) throws RTIinternalError {
		this.fed_amb = fed_amb;
		this.rti_amb  = SEERTIAmbassador.getInstance();
	}

	// *********************** SynchronizationPoint ********************
	public void achieveSynchronizationPoint(SynchronizationPoint sp) {
		try {
			rti_amb.synchronizationPointAchieved(sp.getValue());
		} catch (SynchronizationPointLabelNotAnnounced | SaveInProgress
				| RestoreInProgress | FederateNotExecutionMember | NotConnected
				| RTIinternalError e) {
			e.printStackTrace();
		}
	}	

	public void registerSynchronizationPoint(SynchronizationPoint sp) {
		try {
			rti_amb.registerFederationSynchronizationPoint(sp.getValue(), null);
		} catch (SaveInProgress | RestoreInProgress
				| FederateNotExecutionMember | NotConnected | RTIinternalError e) {
			e.printStackTrace();
		}
	}

	public void announceSynchronizationPoint(SynchronizationPoint sp) {
		try {
			fed_amb.announceSynchronizationPoint(sp.getValue(), null);
		} catch (FederateInternalError e) {
			e.printStackTrace();
		}
	}	

}
