package model.interactionClass;

import skf.model.interaction.annotations.InteractionClass;
import skf.model.interaction.annotations.Parameter;

@InteractionClass(name = "ModeTransitionRequest")
public class ModeTransitionRequest {
	
	@Parameter(name = "execution_mode", coder = MTRModeCoder.class)
	private MTRMode execution_mode = null;
	
	public ModeTransitionRequest(){}

	public ModeTransitionRequest(MTRMode execution_mode) {
		this.execution_mode = execution_mode;
	}

	/**
	 * @return the execution_mode
	 */
	public MTRMode getExecution_mode() {
		return execution_mode;
	}

	/**
	 * @param execution_mode the execution_mode to set
	 */
	public void setExecution_mode(MTRMode execution_mode) {
		this.execution_mode = execution_mode;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((execution_mode == null) ? 0 : execution_mode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModeTransitionRequest other = (ModeTransitionRequest) obj;
		if (execution_mode != other.execution_mode)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ModeTransitionRequest [execution_mode=" + execution_mode + "]";
	}
	
}
