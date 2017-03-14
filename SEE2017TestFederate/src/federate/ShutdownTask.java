package federate;

import hla.rti1516e.exceptions.CallNotAllowedFromWithinCallback;
import hla.rti1516e.exceptions.FederateIsExecutionMember;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateOwnsAttributes;
import hla.rti1516e.exceptions.InvalidResignAction;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.OwnershipAcquisitionPending;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;
import synchronizationPoint.SynchronizationPoint;

public class ShutdownTask implements Runnable {
	
	private TestFederate federate = null;

	public ShutdownTask(TestFederate federate) {
		this.federate  = federate;
	}

	@Override
	public void run() {
		try {
			waitingForAnnouncement(SynchronizationPoint.MTR_SHUTDOWN);

			federate.getHLAModule().achieveSynchronizationPoint(SynchronizationPoint.MTR_SHUTDOWN);

			waitingForSynchronization(SynchronizationPoint.MTR_SHUTDOWN);
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 5. Disconnect The Federate from the RTI
		try {
			federate.diconnectFromRTI();
		} catch (InvalidResignAction | OwnershipAcquisitionPending
				| FederateOwnsAttributes | FederateNotExecutionMember
				| NotConnected | RTIinternalError
				| FederateIsExecutionMember
				| CallNotAllowedFromWithinCallback | SaveInProgress
				| RestoreInProgress e) {
			e.printStackTrace();
		}
		
	}
	
	private void waitingForAnnouncement(SynchronizationPoint sp) throws InterruptedException {
		while(!sp.isAnnounced())
			Thread.sleep(10);
	}

	private void waitingForSynchronization(SynchronizationPoint sp) throws InterruptedException {
		while(!sp.federationIsSynchronized())
			Thread.sleep(10);
	}

}
