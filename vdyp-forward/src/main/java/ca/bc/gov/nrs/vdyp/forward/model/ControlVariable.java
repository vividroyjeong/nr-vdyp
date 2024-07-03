package ca.bc.gov.nrs.vdyp.forward.model;

public enum ControlVariable {
	GROW_TARGET_1(1), 
	COMPAT_VAR_OUTPUT_2(2), 
	COMPAT_VAR_APPLICATION_3(3), 
	OUTPUT_FILES_4(4),
	ALLOW_COMPAT_VAR_CALCS_5(5), 
	UPDATE_DURING_GROWTH_6(6);
	
	public final int variableNumber;
	
	ControlVariable(int variableNumber) {
		this.variableNumber = variableNumber;
	}
}