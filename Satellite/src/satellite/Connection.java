package satellite;

import hla.rti1516e.*;
import hla.rti1516e.exceptions.*;

import java.net.URL;
import java.util.logging.*;

public class Connection {
//	private static final int CRC_PORT = 9625;
	private static final String FEDERATION_NAME = "SEE 2017";
	private String federateName;
	private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());
	
	public Connection() {
		
	}

	public void Join(FederateAmbassador federateAmbassador, RTIambassador rtiAmbassador, String rtiHost, String rtiPort) {
		try {
			System.out.println("Connecting to pRTI");
			
			//connect to pRTI
			String settingsDesignator = "crcHost=" + rtiHost + "\ncrcPort=" + rtiPort;
			rtiAmbassador.connect(federateAmbassador, CallbackModel.HLA_IMMEDIATE, settingsDesignator);
			
			//destroy any existing federation execution
//			try {
//				rtiAmbassador.destroyFederationExecution(FEDERATION_NAME);
//			} catch (Exception e) {
//				//ignore, this just means there was no existing federation execution to destroy
//			}
			
			//Setup FOM urls
			String URL_PREFIX = "file:/";
			String workingDir = System.getProperty("user.dir");
			String FOM_DIR = "FOMs/";
			URL[] FOM_MODULES;
			FOM_MODULES = new URL[]{
					new URL(URL_PREFIX + workingDir + "/" + FOM_DIR + "SISO_SpaceFOM_management.xml"),
					new URL(URL_PREFIX + workingDir + "/" + FOM_DIR + "SISO_SpaceFOM_entity.xml"),
					new URL(URL_PREFIX + workingDir + "/" + FOM_DIR + "SISO_SpaceFOM_environment.xml"),
					new URL(URL_PREFIX + workingDir + "/" + FOM_DIR + "SISO_SpaceFOM_switches.xml"),	
			};
			
			//attempt to create the federation execution
//			try {
//				rtiAmbassador.createFederationExecution(FEDERATION_NAME, FOM_MODULES, "HLAinteger64Time");
//			} catch (FederationExecutionAlreadyExists e) {
//				//ignore, someone else already created the federation execution
//			}
//			
			//attempt to connect to the federation
			federateName = "MWSU_Satellite";
			try {
				rtiAmbassador.joinFederationExecution(federateName, "PhysicalEntity", FEDERATION_NAME, FOM_MODULES);
				System.out.println("Joined Federation");
			} catch (Exception e) {
				System.out.println("Failed to connect to federation");
	            LOGGER.log(Level.SEVERE, e.toString());
			}
			
			//attempt to enable asynchronous delivery
			try {
				rtiAmbassador.enableAsynchronousDelivery();
			} catch (RTIinternalError rti_error) {
				System.out.println("Failed to connect to enable asynchronous delivery(1)");
	            LOGGER.log(Level.SEVERE, rti_error.getMessage());
            } catch (Exception e) {
				System.out.println("Failed to connect to enable asynchronous delivery(2)");
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
			
		} catch (Exception e) {
			System.out.println("Failed to setup connection");
            LOGGER.log(Level.SEVERE, e.getMessage());
		}
	}

    public void Resign(RTIambassador rtiAmbassador) {
        try {
            rtiAmbassador.resignFederationExecution(ResignAction.DELETE_OBJECTS_THEN_DIVEST);
//            try {
//                rtiAmbassador.destroyFederationExecution(FEDERATION_NAME);
//            } catch (FederatesCurrentlyJoined ignored) {
//            	//ignore, not currently part of a federation execution
//            }
            rtiAmbassador.disconnect();
            rtiAmbassador = null;
        } catch (Exception e) {
        	System.out.println("Failed to resign from federation execution");
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }
}
