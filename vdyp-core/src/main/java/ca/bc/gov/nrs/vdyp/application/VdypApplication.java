package ca.bc.gov.nrs.vdyp.application;

public abstract class VdypApplication extends VdypComponent {
	public abstract VdypApplicationIdentifier getIdentifier();

	/**
	 * @returns the ordinal of the application's identifier. It will agree with the
	 *          JPROGRAM values from the FORTRAN implementation.
	 */
	public int getId() {
		return getIdentifier().ordinal();
	}
}
