package constellation;

import federate.TestFederate;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.ObjectClassNotDefined;
import hla.rti1516e.exceptions.ObjectClassNotPublished;
import hla.rti1516e.exceptions.ObjectInstanceNameInUse;
import hla.rti1516e.exceptions.ObjectInstanceNameNotReserved;
import hla.rti1516e.exceptions.RTIexception;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

public class Satellite extends Entity {


	private Orbit orbit;
	
	private AttributeHandleSet attributeSet;
	
	private AttributeHandle attributeEntityName;
	private AttributeHandle attributeStatus;
	private AttributeHandle attributeParentRefFrame;
	private AttributeHandle attributePosition;
	private AttributeHandle attributeVelocity;
	
	private AttributeHandle attributeTime;
	private AttributeHandle attributeEntityType;

	private TestFederate federate;

	private ObjectClassHandle classHandle;
	private ObjectInstanceHandle instanceHandle; 
	
	protected AttributeHandleValueMap attributeValues;
	
	private ReferenceFrame referenceFrame = ReferenceFrame.MoonCentricInertial;
	private String status = "Okay";
	
	
	public Satellite(TestFederate federate, double semiMajorAxis, double eccentricity, double inclination, double argOfPerigree, double RAAN, double trueAnomaly, String name, int id) {
		super(null, name, id);
		orbit = new Orbit(semiMajorAxis, eccentricity, inclination, argOfPerigree, RAAN, trueAnomaly);
		location = orbit.getLocation();
		this.federate = federate;
		instanceHandle = null;
	}
	
	public void Propagate(double timeDiff) {
		orbit.propagateOrbit(timeDiff);
		location = orbit.getLocation();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Orbit getOrbit() {
		return orbit;
	}

	public void setOrbit(Orbit orbit) {
		this.orbit = orbit;
	}
	
	public void setHlaAttributes()
	{
		try {
			//Get a handle to the PhysicalEntity class
			RTIambassador rtiAmbassador = federate.getAmbassador();
			classHandle = rtiAmbassador.getObjectClassHandle("PhysicalEntity");
			//Get handles to all the ReferenceFrame attributes.
			attributeEntityName = rtiAmbassador.getAttributeHandle(classHandle, "name");
			attributeStatus = rtiAmbassador.getAttributeHandle(classHandle, "status");
			attributeParentRefFrame = rtiAmbassador.getAttributeHandle(classHandle, "parent_reference_frame");
			attributePosition = rtiAmbassador.getAttributeHandle(classHandle, "center_of_mass");
			//attributeVelocity = rtiAmbassador.getAttributeHandle(classHandle, "velocity");
			//attributeTime = rtiAmbassador.getAttributeHandle(classHandle, "time");
			attributeEntityType = rtiAmbassador.getAttributeHandle(classHandle, "type");
			
			//generate an attribute handle set
			attributeSet = rtiAmbassador.getAttributeHandleSetFactory().create();
			attributeSet.add(attributeEntityName);
			attributeSet.add(attributeStatus);
			attributeSet.add(attributeParentRefFrame);
			attributeSet.add(attributePosition);
			//attributeSet.add(attributeVelocity);
			//attributeSet.add(attributeTime);
			attributeSet.add(attributeEntityType);
			
		} catch (Exception e) 
		{
			System.out.println("Failed to initialize Satellite.");
			System.out.println(e.getMessage() );
		}
		
		if (!federate.isConstellationInitialized()) 
		{
			try{
				RTIambassador rtiAmbassador = federate.getAmbassador();
				System.out.println("Satellite not yet initialized for HLA. Publishing Class Attributes ...");
				rtiAmbassador.publishObjectClassAttributes(classHandle, attributeSet);
				federate.setConstellationInitialized(true);
				
			}catch (Exception e) 
			{
				System.out.println("Failed to Satellite Radio Class Attributes");
				System.out.println(e.getMessage() );
			}
		}
	}
	
	
	//Register the objects
	public void registerObjectInstance()
	{
		try {
			instanceHandle = federate.getAmbassador().registerObjectInstance(classHandle, name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		//add Radio to Federate
		System.out.println("Satellite Registered ObjectInstanceHandle : " + instanceHandle);
	}
		
	public void postAttributes()
	{
		try {
			attributeValues = federate.getAmbassador().getAttributeHandleValueMapFactory().create(1);
		} catch (FederateNotExecutionMember e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotConnected e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//federate.float_64LE_encoder.setValue(federate.getTime().getFederateExecutionTimeCycle());
		//attributeValues.put(attributeTime, federate.float_64LE_encoder.toByteArray());
		//encode Name, and put into AttributeValues Set
		federate.string_encoder.setValue(getName());
		attributeValues.put(attributeEntityName, federate.string_encoder.toByteArray());
		//encode parent ref frame
		federate.string_encoder.setValue(referenceFrame.toString());
		attributeValues.put(attributeParentRefFrame, federate.string_encoder.toByteArray());
		
		federate.vector_encoder.get(0).setValue(orbit.getPosition()[0]);
		federate.vector_encoder.get(1).setValue(orbit.getPosition()[1]);
		federate.vector_encoder.get(2).setValue(orbit.getPosition()[2]);
		attributeValues.put(attributePosition, federate.vector_encoder.toByteArray());
		federate.string_encoder.setValue(status);
		attributeValues.put(attributeStatus,  federate.string_encoder.toByteArray());
		federate.string_encoder.setValue("PhysicalEntity");
		attributeValues.put(attributeEntityType, federate.string_encoder.toByteArray());
		try{
			federate.getAmbassador().updateAttributeValues(instanceHandle, attributeValues, null);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
