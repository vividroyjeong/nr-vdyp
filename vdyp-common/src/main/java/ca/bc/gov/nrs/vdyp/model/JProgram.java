package ca.bc.gov.nrs.vdyp.model;

public enum JProgram {
	FIP_START(true, 1), // 1
	VRI_START(true, 3), // 3
	VDYP(false, 6), // 6
	VDYP_BACK(false, 7), // 7
	VRI_ADJUST(false, 8); // 8

	JProgram(boolean isStart, int index) {
		this.isStart = isStart;
		this.index = index;
	}

	public boolean isStart() {
		return isStart;
	}

	public int getIndex() {
		return index;
	}

	private final boolean isStart;
	private final int index;
}
