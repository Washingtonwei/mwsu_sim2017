package model.interactionClass;

public enum MTRMode {

	/**
	 * The run mode requested.  
	 * There are only 3 valid Mode Transition Request (MTR) mode values: 
	 * MTR_GOTO_RUN, MTR_GOTO_FREEZE, MTR_GOTO_SHUTDOWN.  
	 * Of these three valid mode requests, only 7 combinations of current 
	 * execution mode and requested mode are valid: 
	 * 1. EXEC_MODE_UNINITIALIZED -> EXEC_MODE_SHUTDOWN 
	 * 2. EXEC_MODE_INITIALIZED -> EXEC_MODE_FREEZE 
	 * 3. EXEC_MODE_INITIALIZED -> EXEC_MODE_SHUTDOWN 
	 * 4. EXEC_MODE_RUNNING -> EXEC_MODE_FREEZE 
	 * 5. EXEC_MODE_RUNNING -> EXEC_MODE_SHUTDOWN 
	 * 6. EXEC_MODE_FREEZE -> EXEC_MODE_RUNNING 
	 * 7. EXEC_MODE_FREEZE -> EXEC_MODE_SHUTDOWN
	 */

	MTR_GOTO_RUN((short)2), 
	MTR_GOTO_FREEZE((short)3), 
	MTR_GOTO_SHUTDOWN((short)4);
	
	private short value;
	
	private MTRMode(short value){
		this.value = value;
	}
	
	public short getValue(){
		return value;
	}
	
	public static MTRMode lookup(short value){
		for(MTRMode mode : MTRMode.values())
			if(mode.value == value)
				return mode;
		return null;
	}

}
