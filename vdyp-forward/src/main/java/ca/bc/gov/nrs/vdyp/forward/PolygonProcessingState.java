package ca.bc.gov.nrs.vdyp.forward;

import java.util.Arrays;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonLayer;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;

class PolygonProcessingState {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PolygonProcessingState.class);

	// L1COM1, L1COM4 and L1COM5 - these common blocks mirror BANK1, BANK2 and BANK3 and are initialized 
	// when copied to "active" in ForwardProcessingEngine.
	Bank wallet; 
	
	// L1COM2
	
	// L1COM3 - just shadows of fields of L1COM5
	//     AGETOTL1 = wallet.ageTotals[primarySpeciesIndex]
	//     AGEBHL1 = wallet.yearsAtBreastHeight[primarySpeciesIndex]
	//     YTBHL1 = wallet.yearsToBreastHeight[primarySpeciesIndex]
	//     HDL1 = wallet.dominantHeights[primarySpeciesIndex]

	// Calculated data - this data is calculated after construction during processing.
	
	
	// Ranking Details - encompasses INXXL1 and INXL1
	private boolean areRankingDetailsSet = false;
	
	// INXXL1
	private int primarySpeciesIndex; // IPOSP
	
	// INXL1
	//     ISPP = wallet.speciesIndices[primarySpeciesIndex]
	//     PCTP = wallet.percentagesOfForestedLand[primarySpeciesIndex]
	Optional<Integer> secondarySpeciesIndex; // => ISPS (species name) and PCTS (percentage)
	int inventoryTypeGroup; // ITG
	int primarySpeciesGroupNumber; // GRPBA1
	int primarySpeciesStratumNumber; // GRPBA3
	
	
	// Site Curve Numbers - encompasses INXSCV
	private boolean areSiteCurveNumbersSet = false;
	
	// INXSC
	/* pp */ int[] siteCurveNumbers; // INXSCV 

	
	// Primary Species Details - encompasses L1COM6
	private boolean arePrimarySpeciesDetailsSet = false;
	
	// L1COM6
	float primarySpeciesDominantHeight; // HD
	float primarySpeciesSiteIndex; // SI
	float primarySpeciesTotalAge; // AGETOTP
	float primarySpeciesAgeAtBreastHeight; // AGEBHP
	float primarySpeciesAgeToBreastHeight; // YTBHP
	
	
	// FRBASP0 - FR
	// TODO
	
	// MNSP - MSPL1, MSPLV
	// TODO
	
	public PolygonProcessingState(Bank bank) {
		this.wallet = bank.copy();
	}

	public int getNSpecies() {
		return wallet.getNSpecies();
	}

	public int[] getIndices() {
		return wallet.getIndices();
	}

	public BecDefinition getBecZone() {
		return wallet.getBecZone();
	}

	public VdypPolygonLayer getLayer() {
		return wallet.getLayer();
	}

	public int getPrimarySpeciesIndex() {
		return primarySpeciesIndex;
	}

	public Optional<Integer> getSecondarySpeciesIndex() {
		return secondarySpeciesIndex;
	}

	public int getInventoryTypeGroup() {
		return inventoryTypeGroup;
	}

	public float getPrimarySpeciesDominantHeight() {
		return primarySpeciesDominantHeight;
	}

	public float getPrimarySpeciesSiteIndex() {
		return primarySpeciesSiteIndex;
	}

	public float getPrimarySpeciesTotalAge() {
		return primarySpeciesTotalAge;
	}

	public float getPrimarySpeciesAgeAtBreastHeight() {
		return primarySpeciesAgeAtBreastHeight;
	}

	public float getPrimarySpeciesAgeToBreastHeight() {
		return primarySpeciesAgeToBreastHeight;
	}

	public void setSpeciesRankingDetails(SpeciesRankingDetails rankingDetails) {
		if (this.areRankingDetailsSet) {
			throw new IllegalStateException("SpeciesRankingDetails can be set once only");
		}
		
		this.primarySpeciesIndex = rankingDetails.primarySpeciesIndex();
		this.secondarySpeciesIndex = rankingDetails.secondarySpeciesIndex();
		this.inventoryTypeGroup = rankingDetails.inventoryTypeGroup();
		
		this.areRankingDetailsSet = true;
	}

	public void setSiteCurveNumbers(int[] siteCurveNumbers) {
		if (this.areSiteCurveNumbersSet) {
			throw new IllegalStateException("SiteCurveNumbers can be set once only");
		}
		
		Arrays.copyOf(siteCurveNumbers, siteCurveNumbers.length);
		
		areSiteCurveNumbersSet = true;
	}

	public void setPrimarySpeciesDetails(PrimarySpeciesDetails details) {
		if (this.arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("PrimarySpeciesDetails can be set once only");
		}
		
		this.primarySpeciesDominantHeight = details.primarySpeciesDominantHeight();
		this.primarySpeciesSiteIndex = details.primarySpeciesSiteIndex();
		this.primarySpeciesTotalAge = details.primarySpeciesTotalAge();
		this.primarySpeciesAgeAtBreastHeight = details.primarySpeciesAgeAtBreastHeight();
		this.primarySpeciesAgeToBreastHeight = details.primarySpeciesAgeToBreastHeight();
		
		// Store these values into the wallet - VHDOM1 lines 182 - 186
		if (wallet.dominantHeights[primarySpeciesIndex] <= 0.0) {
			wallet.dominantHeights[primarySpeciesIndex] = this.primarySpeciesDominantHeight;
		}
		if (wallet.siteIndices[primarySpeciesIndex] <= 0.0) {
			wallet.siteIndices[primarySpeciesIndex] = this.primarySpeciesSiteIndex;
		}
		if (wallet.ageTotals[primarySpeciesIndex] <= 0.0) {
			wallet.ageTotals[primarySpeciesIndex] = this.primarySpeciesTotalAge;
		}
		if (wallet.yearsAtBreastHeight[primarySpeciesIndex] <= 0.0) {
			wallet.yearsAtBreastHeight[primarySpeciesIndex] = this.primarySpeciesAgeAtBreastHeight;
		}
		if (wallet.yearsAtBreastHeight[primarySpeciesIndex] <= 0.0) {
			wallet.yearsAtBreastHeight[primarySpeciesIndex] = this.primarySpeciesAgeToBreastHeight;
		}
		
		this.arePrimarySpeciesDetailsSet = true;
	}
}