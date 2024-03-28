package ca.bc.gov.nrs.vdyp.si32;

/**
 * Records all of the information regarding a particular species.
 */
public record structSpeciesTableItem(
	
	/** The short standard code name for the species */
	String sCodeName,
	
	/** The full common name for the species. */
	String sFullName,
	
	/** The Latin name (if known) for the species. */
	String sLatinName,

	/** The species code name associated with the species as a genus. */
	String sGenusName,

	/** The VDYP7 SP0 code name to use with the species. */
	String sSP0Name,
	
	/** 
	 * The corresponding CFS Species for the MoF Species.  The source for this
	 * mapping is found in the document <b>Documents/CFS-Biomass/BCSpcsToCFSSpcs-SAS.txt</b>
	 */
	enumIntCFSTreeSpecies iCFSSpcs,
	
	/**
	 * <ul>
	 * <li>TRUE: indicates the species is a commercial species.
	 * <li>FALSE: indicates the species is not a commercial species.
	 * </ul>
	 */
	boolean bIsCommercial,

	/**
	 * <ul>
	 * <li>TRUE: indicates the species is a deciduous species.
	 * <li>FALSE: indicates the species is a coniferous species.
	 * </ul>
	 */
	boolean bIsDeciduous,

	/**
	 * <ul>
	 * <li>TRUE: indicates the species is a softwood species.
	 * <li>FALSE: indicates the species is a hardwood species.
	 * </ul>
	 */
	boolean bIsSoftwood,
	
	/** Returns the default Crown Closure for a species in a particular part of the province. */
	float[] fDefaultCC,

	/** 
	 * For each species region, identifies the SINDEX curve number currently assigned to 
	 * the species. In certain applications, the standard curve number for a species may be modified to
	 * other experimental or alternative curves rather than the standard curve.
	 */
	int[] iCrntSICurve) 
{}
