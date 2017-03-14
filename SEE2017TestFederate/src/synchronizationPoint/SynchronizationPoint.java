package synchronizationPoint;

public enum SynchronizationPoint {

	INITIALIZATION_STARTED("initialization_started"),
	INITIALIZATION_COMPLETED("initialization_completed"),
	OBJECTS_DISCOVERED("objects_discovered"),	
	MTR_RUN("mtr_run"),
	MTR_FREEZE("mtr_freeze"),
	MTR_SHUTDOWN("mtr_shutdown");

	private String value = null;
	private boolean isAnnounced = false;
	private boolean isRegistered = false;
	private boolean federationIsSynchronized = false;

	private SynchronizationPoint(String value) {
		this.value = value;
	}

	public static SynchronizationPoint lookup(String value) {
		for(SynchronizationPoint sp : SynchronizationPoint.values())
			if(sp.value.equalsIgnoreCase(value))
				return sp;
		return null;
	}

	public String getValue() {
		return this.value;
	}
	
	public void isAnnounced(boolean value) {
		this.isAnnounced = value;
	}

	public void federationIsSynchronized(boolean value) {
		this.federationIsSynchronized = value;
	}
	
	public boolean isAnnounced() {
		return isAnnounced;
	}

	public boolean federationIsSynchronized() {
		return federationIsSynchronized;
	}

	public void isRegistered(boolean value) {
		this.isRegistered = value;
		
	}
	
	public boolean isRegistered() {
		return isRegistered;
		
	}

}
