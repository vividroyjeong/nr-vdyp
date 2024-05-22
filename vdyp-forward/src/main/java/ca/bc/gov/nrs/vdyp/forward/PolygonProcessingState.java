package ca.bc.gov.nrs.vdyp.forward;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonLayer;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;

class PolygonProcessingState {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(PolygonProcessingState.class);

	// L1COM1, L1COM4 and L1COM5 - these common blocks mirror BANK1, BANK2 and BANK3 and are initialized 
	// when copied to "active" in ForwardProcessingEngine.
	Bank wallet; 
	
	// L1COM3 - just shadows of fields of L1COM5
	//     AGETOTL1 = wallet.ageTotals[primarySpeciesIndex]
	//     AGEBHL1 = wallet.yearsAtBreastHeight[primarySpeciesIndex]
	//     YTBHL1 = wallet.yearsToBreastHeight[primarySpeciesIndex]
	//     HDL1 = wallet.dominantHeights[primarySpeciesIndex]

	// L1COM2 - equation groups. From the configuration, narrowed to the 
	// polygon's BEC zone.
		
	float[] volumeEquationGroups;
	float[] decayEquationGroups;
	float[] breakageEquationGroups;
	
	// Calculated data - this data is calculated after construction during processing.
	
	
	// Ranking Details - encompasses INXXL1 and INXL1
	private boolean areRankingDetailsSet = false;
	
	// INXXL1
	private int primarySpeciesIndex; // IPOSP
	
	// INXL1
	//     ISPP = wallet.speciesIndices[primarySpeciesIndex]
	//     PCTP = wallet.percentagesOfForestedLand[primarySpeciesIndex]
	private Optional<Integer> secondarySpeciesIndex; // => ISPS (species name) and PCTS (percentage)
	private int inventoryTypeGroup; // ITG
	private int primarySpeciesGroupNumber; // GRPBA1
	private int primarySpeciesStratumNumber; // GRPBA3
	
	
	// Site Curve Numbers - encompasses INXSCV
	private boolean areSiteCurveNumbersSet = false;
	
	// INXSC
	private int[] siteCurveNumbers; // INXSCV 

	
	// Primary Species Details - encompasses L1COM6
	private boolean arePrimarySpeciesDetailsSet = false;
	
	// L1COM6
	private float primarySpeciesDominantHeight; // HD
	private float primarySpeciesSiteIndex; // SI
	private float primarySpeciesTotalAge; // AGETOTP
	private float primarySpeciesAgeAtBreastHeight; // AGEBHP
	private float primarySpeciesAgeToBreastHeight; // YTBHP
	
	// FRBASP0 - FR
	// TODO
	
	// MNSP - MSPL1, MSPLV
	// TODO
	
	@SuppressWarnings("unchecked")
	public PolygonProcessingState(Bank bank, Map<String, Object> controlMap) {
		this.wallet = bank.copy();
		
		MatrixMap2<String, String, Integer> volumeEquationGroupMatrix =
			Utils.expectParsedControl(controlMap, ControlKey.VOLUME_EQN_GROUPS, MatrixMap2.class);		
		MatrixMap2<String, String, Integer> decayEquationGroupMatrix =
				Utils.expectParsedControl(controlMap, ControlKey.DECAY_GROUPS, MatrixMap2.class);
		MatrixMap2<String, String, Integer> breakageEquationGroupMatrix = 
				Utils.expectParsedControl(controlMap, ControlKey.BREAKAGE_GROUPS, MatrixMap2.class);
		
		this.volumeEquationGroups = new float[this.wallet.getNSpecies() + 1];
		this.decayEquationGroups = new float[this.wallet.getNSpecies() + 1];
		this.breakageEquationGroups = new float[this.wallet.getNSpecies() + 1];

		this.volumeEquationGroups[0] = Float.NaN;
		this.decayEquationGroups[0] = Float.NaN;
		this.breakageEquationGroups[0] = Float.NaN;
		
		String becZoneAlias = this.getBecZone().getAlias();
		for (int i = 1; i < this.wallet.getNSpecies() + 1; i++) {
			String speciesName = this.wallet.speciesNames[i];
			this.volumeEquationGroups[i] = volumeEquationGroupMatrix.get(speciesName, becZoneAlias);
			this.decayEquationGroups[i] = decayEquationGroupMatrix.get(speciesName, becZoneAlias);
			this.breakageEquationGroups[i] = breakageEquationGroupMatrix.get(speciesName, becZoneAlias);
		}
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
		if (!areRankingDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesIndex");
		}
		return primarySpeciesIndex;
	}

	public boolean hasSecondarySpeciesIndex() {
		return secondarySpeciesIndex.isPresent();
	}

	public int getSecondarySpeciesIndex() {
		return secondarySpeciesIndex.orElseThrow(() -> new IllegalStateException("unset secondarySpeciesIndex"));
	}

	public int getInventoryTypeGroup() {
		if (!areRankingDetailsSet) {
			throw new IllegalStateException("unset inventoryTypeGroup");
		}
		return inventoryTypeGroup;
	}

	public int getSiteCurveNumber(int n) {
		if (!areSiteCurveNumbersSet) {
			throw new IllegalStateException("unset siteCurveNumbers");
		}
		if (n == 0) {
			// Take this opportunity to initialize siteCurveNumbers[0] from that of the primary species.
			if (!areRankingDetailsSet) {
				throw new IllegalStateException("unset rankingDetails");
			}
			siteCurveNumbers[0] = siteCurveNumbers[primarySpeciesIndex];
		}
		return siteCurveNumbers[n];
	}
	
	public float getPrimarySpeciesDominantHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesDominantHeight");
		}
		return primarySpeciesDominantHeight;
	}

	public float getPrimarySpeciesSiteIndex() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesSiteIndex");
		}
		return primarySpeciesSiteIndex;
	}

	public float getPrimarySpeciesTotalAge() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesTotalAge");
		}
		return primarySpeciesTotalAge;
	}

	public float getPrimarySpeciesAgeAtBreastHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesAgeAtBreastHeight");
		}
		return primarySpeciesAgeAtBreastHeight;
	}

	public float getPrimarySpeciesAgeToBreastHeight() {
		if (!arePrimarySpeciesDetailsSet) {
			throw new IllegalStateException("unset primarySpeciesAgeToBreastHeight");
		}
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
		
		this.siteCurveNumbers = Arrays.copyOf(siteCurveNumbers, siteCurveNumbers.length);
		
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