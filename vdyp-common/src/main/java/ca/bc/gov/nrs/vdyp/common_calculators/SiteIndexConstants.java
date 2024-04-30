package ca.bc.gov.nrs.vdyp.common_calculators;

public interface SiteIndexConstants {
	
	/* establishment types */
	int SI_ESTAB_NAT = 0;
	int SI_ESTAB_PLA = 1;

	/* site index estimation (from height and age) types */
	int SI_EST_ITERATE = 0;
	int SI_EST_DIRECT = 1;
	
	/* age types */
	int SI_AT_TOTAL = 0;
	int SI_AT_BREAST = 1;

	/* forest regions */
	short FIZ_UNKNOWN = 0;
	short FIZ_COAST = 1;
	short FIZ_INTERIOR = 2;
}
