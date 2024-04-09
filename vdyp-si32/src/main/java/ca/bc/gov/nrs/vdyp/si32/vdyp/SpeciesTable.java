package ca.bc.gov.nrs.vdyp.si32.vdyp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.si32.cfs.CfsTreeSpecies;

/**
 * Maintains all information for VDYP7 species.
 * <p>
 * These entries MUST be listed alphabetically by sCodeName. It is essential
 * that this array's contents match {@code enumSP64Enum} exactly.
 * <p>
 * iCrntCurve is left uninitialized until the first request of a site curve
 * is made or assigned at which time defaults will be populated from SINDEX.
 */
public class SpeciesTable {
	
	public record SpeciesTableItem(
		/** The index at which this item resides in the table */
		int index,
		SpeciesDetails details 
	) {}
	
	public static final int UNKNOWN_ENTRY_INDEX = 0;
	public static final String UNKNOWN_ENTRY_CODE_NAME = "??";

	public static final String UNKNOWN_ENTRY_FULL_NAME = "Invalid Species";
	public static final String UNKNOWN_ENTRY_LATIN_NAME = "Invalid Species";
	public static final String UNKNOWN_ENTRY_GENUS_NAME = "";
	public static final String UNKNOWN_ENTRY_SP0_NAME_VALUE = "";
	public static final boolean UNKNOWN_ENTRY_IS_COMMERCIAL_VALUE = false;
	public static final boolean UNKNOWN_ENTRY_IS_DECIDUOUS_VALUE = false;
	public static final boolean UNKNOWN_ENTRY_IS_SOFTWOOD_VALUE = false;
	public static final float UNKNOWN_ENTRY_CROWN_CLOSURE_VALUE = -1.0f;
	public static final int UNKNOWN_ENTRY_CURRENT_SI_CURVE_VALUE = -1;
	
	public static final SpeciesDetails DefaultEntry = new SpeciesDetails(
			UNKNOWN_ENTRY_CODE_NAME, UNKNOWN_ENTRY_FULL_NAME, UNKNOWN_ENTRY_LATIN_NAME, UNKNOWN_ENTRY_GENUS_NAME, 
			UNKNOWN_ENTRY_SP0_NAME_VALUE, CfsTreeSpecies.cfsSpcs_UNKNOWN, UNKNOWN_ENTRY_IS_COMMERCIAL_VALUE,
			UNKNOWN_ENTRY_IS_DECIDUOUS_VALUE, UNKNOWN_ENTRY_IS_SOFTWOOD_VALUE, 
			new float[] { UNKNOWN_ENTRY_CROWN_CLOSURE_VALUE, UNKNOWN_ENTRY_CROWN_CLOSURE_VALUE }, 
			new int[] { UNKNOWN_ENTRY_CURRENT_SI_CURVE_VALUE, UNKNOWN_ENTRY_CURRENT_SI_CURVE_VALUE });
	
	public static final SpeciesTableItem DefaultTableItem = new SpeciesTableItem(0, DefaultEntry);
	
	private final List<SpeciesTableItem> speciesTable = new ArrayList<>();
	private final Map<String, SpeciesTableItem> speciesByTextMap = new HashMap<>();
	
	private void addSpeciesToTable(SpeciesDetails item) {
		
		int index = speciesTable.size();
		if (index != SP64Name.forText(item.codeName()).ordinal()) {
			throw new IllegalStateException(MessageFormat.format("enumSP64Name's ordinal value for {0}"
					+ " does not match the index the species will be placed in SpeciesTable {1}", item.codeName(), index));
		}
		
		SpeciesTableItem tableItem = new SpeciesTableItem(index, item);
		
		speciesTable.add(tableItem);
		speciesByTextMap.put(item.codeName(), tableItem);
	}
	
	/**
	 * Performs a case-insensitive search for the species with the given sp64 code name.
	 * 
	 * @param sp64Name the code name of the species to be found.
	 * @return the {@code structSpeciesTableItem} of that name, or {@code DefaultEntry} if not found.
	 */
	public SpeciesTableItem getByCode(String sp64Name) {
		
		if (sp64Name != null) {
			String sp64NameUC = sp64Name.toUpperCase();
			if (speciesByTextMap.containsKey(sp64NameUC)) {
				return speciesByTextMap.get(sp64NameUC);
			} 
		}
		
		return DefaultTableItem;
	}
	
	public int getNSpecies() {
		return speciesTable.size() - 1;
	};

	public SpeciesTable() {
		
		addSpeciesToTable(DefaultEntry);
		
		addSpeciesToTable(new SpeciesDetails(
				"A", "Aspen/Cottonwood/Poplar", "Populus", "A", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"ABAL", "Silver Fir", "Abies alba", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"ABCO", "White Fir", "Abies concolor", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"AC", "Poplar", "Populus balsamifera", "CT", "AC", CfsTreeSpecies.cfsSpcs_BalsamPoplar, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"ACB", "Balsam Poplar", "Populus balsamifera ssp. balsamifera", "CT", "AC",
				CfsTreeSpecies.cfsSpcs_BalsamPoplar, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"ACT", "Black Cottonwood", "Populus balsamifera spp. trichocarpa", "CT", "AC",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"AD", "Cottonwood (exotic)", "<<unknown>>", "CT", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"AH", "Poplar Cottonwood hybrid", "<<unknown>>", "??", "AC",
				CfsTreeSpecies.cfsSpcs_AspenTrembling, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"AT", "Trembling Aspen", "Populus tremuloides", "A", "AT",
				CfsTreeSpecies.cfsSpcs_AspenTrembling, true, true, false, new float[] { 52.0f, 52.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"AX", "Hybrid Poplars", "Populus ssp.", "CT", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"B", "Balsam", "Abies", "B", "B", CfsTreeSpecies.cfsSpcs_Fir, true, false, true,
				new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BA", "Amabilis/Pacific Silver Fir", "Abies amabilis", "B", "B",
				CfsTreeSpecies.cfsSpcs_FirAmabilis, true, false, true, new float[] { 57.0f, 57.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BAC", "Amabilis fir (coast)", "<<unknown>>", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BAI", "Amabilis fir (interior)", "<<unknown>>", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BB", "Balsam Fir", "Abies balsamea", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BC", "White Fir", "Abies concolor", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BG", "Grand Fir", "Abies grandis", "B", "B", CfsTreeSpecies.cfsSpcs_FirGrand, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BI", "Birch", "Betula", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BL", "Alpine Fir", "Abies lasiocarpa", "B", "B",
				CfsTreeSpecies.cfsSpcs_FirSubalpineOrAlpine, true, false, true, new float[] { 42.0f, 42.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BM", "Shasta Red Fir", "Abies magnifica var. shastensis", "B", "B",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 57.0f, 42.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BN", "Noble fir", "<<unknown>>", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BP", "Noble Fir", "Abies procera", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"BV", "Silver/Paper Birch", "<<unknown>>", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"C", "Cedar", "Thuja", "C", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true,
				new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"CI", "Incense Cedar", "Calocedrus decurrens", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"COT", "Cottonwood", "<<unknown>>", "CT", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"CP", "Port Orford Cedar", "<<unknown>>", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"CT", "Cottonwood", "<<unknown>>", "CT", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"CW", "Western Red Cedar", "Thuja plicata", "C", "C", CfsTreeSpecies.cfsSpcs_CedarWesternRed,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"CY", "Yellow Cedar", "<<unknown>>", "C", "Y", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"D", "Alder", "Alnus", "D", "D", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"DF", "Douglas Fir", "Pseudotsuga menziesii", "F", "F", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"DG", "Sitka (green) Alder", "<<unknown>>", "D", "D", CfsTreeSpecies.cfsSpcs_AlderSitka,
				true, true, false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"DM", "Mountain Alder", "<<unknown>>", "D", "D", CfsTreeSpecies.cfsSpcs_AlderSitka, true,
				true, false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"DR", "Red Alder", "Alnus rubra", "D", "D", CfsTreeSpecies.cfsSpcs_AlderRed, true, true,
				false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"E", "Birch", "Betula", "BI", "E", CfsTreeSpecies.cfsSpcs_BirchWhite, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"EA", "Common Paper Birch", "Betula neoalaskana", "BI", "E",
				CfsTreeSpecies.cfsSpcs_BirchAlaskaPaper, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"EB", "Bog Birch", "<<unknown>>", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"EE", "European Birch", "Betula pendula", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"EP", "Silver Paper Birch", "Betula papyrifera", "BI", "E",
				CfsTreeSpecies.cfsSpcs_BirchWhite, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"ES", "Silver Birch (exotic)", "Betula pubescens", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"EW", "Water Birch", "Betula occidentalis", "BI", "E", CfsTreeSpecies.cfsSpcs_BirchWhite,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"EXP", "Alaska x Paper Birch Hybrid", "Betula x. winteri", "BI", "E",
				CfsTreeSpecies.cfsSpcs_BirchAlaskaPaperAndWhite, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"F", "Douglas Fir", "Pseudotsuga", "F", "F", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"FD", "Douglas Fir", "Pseudotsuga menziesii", "F", "F",
				CfsTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"FDC", "Douglas Fir (Coastal)", "Pseudotsuga mensiesii var. menziesii", "F", "F",
				CfsTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"FDI", "Douglas Fir (Interior)", "Pseudotsuga menziesii var. glauca", "F", "F",
				CfsTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 48.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"G", "Dogwood", "Cornus", "G", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"GP", "Pacific Dogwood", "Cornus nuttallii", "G", "MB",
				CfsTreeSpecies.cfsSpcs_DogwoodWesternflowering, true, false, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"GR", "Red-Osier Dogwood", "<<unknown>>", "G", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"H", "Hemlock", "Tsuga", "H", "H", CfsTreeSpecies.cfsSpcs_Hemlock, true, false, true,
				new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"HM", "Mountain Hemlock", "Tsuga mertensiana", "H", "H",
				CfsTreeSpecies.cfsSpcs_HemlockMountain, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"HW", "Western Hemlock", "Tsuga heterophylla", "H", "H",
				CfsTreeSpecies.cfsSpcs_HemlockWestern, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"HWC", "Western hemlock (coast)", "<<unknown>>", "H", "H", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"HWI", "Western hemlock (interior)", "<<unknown>>", "H", "H", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 51.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"HXM", "Western/Mountain Hemlock cross", "<<unknown>>", "H", "H",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"IG", "Giant Sequoia", "Sequoiadendron giganteum", "C", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"IS", "Coast Redwood", "Sequoia sempervirens", "C", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"J", "Juniper", "Juniperus", "J", "C", CfsTreeSpecies.cfsSpcs_JuniperRockyMountain, true,
				false, true, new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"JR", "Rocky Mountain Juniper", "Juniperus scopulorum", "J", "C",
				CfsTreeSpecies.cfsSpcs_JuniperRockyMountain, true, false, true, new float[] { 60.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"K", "Cascara", "Rhamnus", "K", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"KC", "Casara", "Rhamnus Purshiana", "K", "E", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"L", "Larch", "Larix", "L", "L", CfsTreeSpecies.cfsSpcs_TamarackLarch, true, true, false,
				new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"LA", "Alpine Larch", "Larix lyallii", "L", "L", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"LE", "Eastern Larch", "<<unknown>>", "??", "L", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"LT", "Tamarack", "Larix laricina", "L", "L", CfsTreeSpecies.cfsSpcs_Tamarack, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"LW", "Western Larch", "Larix occidentalis", "L", "L", CfsTreeSpecies.cfsSpcs_LarchWestern,
				true, true, false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"M", "Maple", "Acer", "M", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"MB", "Broadleaf Maple", "Acer macrophyllum", "M", "MB", CfsTreeSpecies.cfsSpcs_MapleBigleaf,
				true, true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"ME", "Box elder", "Acer negundo", "??", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"MN", "Norway Maple", "Acer platanoides", "M", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"MR", "Rocky Mountain Maple", "<<unknown>>", "M", "MB", CfsTreeSpecies.cfsSpcs_Maple, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"MS", "Sycamore Maple", "<<unknown>>", "M", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"MV", "Vine Maple", "Acer macrophyllum", "M", "MB", CfsTreeSpecies.cfsSpcs_Maple, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"OA", "Incense cedar", "Calocedrus decurrens", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"OB", "Giant sequoia", "Seqoiadendron giganteum", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"OC", "Coast redwood", "Sequoia sempervirens", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"OD", "European mountain Ash", "Sorbus aucuparia", "??", "MB",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, false, new float[] { 71.0f, 71.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"OE", "Siberian elm", "Ulmus pumila", "??", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"OF", "Common pear", "Pyrus communis", "??", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"OG", "Oregon ash", "Fraxinus latifolia", "??", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"P", "Pine", "Pinus", "PL", "PL", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true,
				new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PA", "Whitebark Pine", "Pinus albicaulis", "PL", "PA", CfsTreeSpecies.cfsSpcs_PineWhitebark,
				true, false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PF", "Limber Pine", "Pinus Flexilis", "PL", "PA", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PJ", "Jack Pine", "Pinus banksiana", "PL", "PL", CfsTreeSpecies.cfsSpcs_PineJack, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PL", "Lodgepole Pine", "Pinus contorta", "PL", "PL", CfsTreeSpecies.cfsSpcs_PineLodgepole,
				true, false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PLC", "Lodgepole Pine (Coastal)", "Pinus contorta var. contorta", "PL", "PL",
				CfsTreeSpecies.cfsSpcs_PineShore, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PLI", "Lodgepole Pine (Interior)", "Pinus contorta var. latifolia", "PL", "PL",
				CfsTreeSpecies.cfsSpcs_PineLodgepole, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PM", "Monterray Pine", "Pinus radiata", "PL", "PW", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PR", "Red Pine", "Pinus Resinosa", "PL", "PW", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PS", "Sugar Pine", "Pinus lambertiana", "PL", "PW", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PV", "Ponderosa pine", "Pinus ponderosa", "PY", "PY", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 30.0f, 30.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PW", "Western White Pine", "Pinus monticola", "PW", "PW",
				CfsTreeSpecies.cfsSpcs_PineWesternWhite, true, false, true, new float[] { 55.0f, 55.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PXJ", "Lodgepole/Jack Pine Hybrid", "Pinus x. murraybanksiana", "PL", "PL",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"PY", "Yellow Pine", "Pinus ponderosa", "PY", "PY", CfsTreeSpecies.cfsSpcs_PinePonderosa,
				true, false, true, new float[] { 30.0f, 30.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"Q", "Oak", "Quercus", "Q", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"QE", "English Oak", "Quercus robur", "Q", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"QG", "Garry Oak", "Quercus Garryana", "Q", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"R", "Arbutus", "Arbutus", "R", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"RA", "Arbutus", "Arbutus menziesii", "R", "MB", CfsTreeSpecies.cfsSpcs_Arbutus, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"S", "Spruce", "Picea", "S", "S", CfsTreeSpecies.cfsSpcs_Spruce, true, false, true,
				new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SA", "Norway Spruce", "<<unknown>>", "??", "S", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SB", "Black Spruce", "Picea mariana", "S", "S", CfsTreeSpecies.cfsSpcs_SpruceBlack, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SE", "Engelmann Spruce", "Picea engelmannii", "S", "S",
				CfsTreeSpecies.cfsSpcs_SpruceEnglemann, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SI", "Interior Spruce", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SN", "Norway Spruce", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SS", "Sitka Spruce", "Picea sitchensis", "S", "S", CfsTreeSpecies.cfsSpcs_SpruceSitka, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SW", "White Spruce", "Picea glauca", "S", "S", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SX", "Spruce Hybrid", "Picea x", "S", "S", CfsTreeSpecies.cfsSpcs_Spruce, true, false, true,
				new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SXB", "SXxSB hybrid", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SXE", "SSxSE hybrid", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SXL", "Sitka/White Spruce Hybrid", "Picea sitchensis x. lutzii", "S", "S",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SXS", "Sitka Spruce Hybrid", "Picea sitchensis x", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SXW", "Engelmann/White Spruce Hybrid", "Picea engelmannii x. glauca", "S", "S",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"SXX", "SW hybrid", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"T", "Yew", "Taxus", "T", "H", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, true,
				new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"TW", "Pacific (western) yew", "Taxus brevifolia", "T", "H",
				CfsTreeSpecies.cfsSpcs_YewWestern, true, true, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"U", "Apple", "Malus", "U", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"UA", "Apple", "Malus pumila", "U", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"UP", "Crab apple", "Malus fusca", "U", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"V", "Cherry", "Prunus", "V", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"VB", "Bitter Cherry", "Prunus emarginata", "V", "E", CfsTreeSpecies.cfsSpcs_CherryBitter,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"VP", "Pin Cherry", "Prunus pensylvanica", "V", "E", CfsTreeSpecies.cfsSpcs_CherryPin, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"VS", "Sweet Cherry", "Prunus avium", "V", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"VV", "Choke Cherry", "Prunus virginiana", "V", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"W", "Willow", "Salix", "W", "E", CfsTreeSpecies.cfsSpcs_Willow, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"WA", "Peachleaf Willow", "Salix amygdaloides", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"WB", "Bebb's Willow", "Salix bebbiana", "W", "E", CfsTreeSpecies.cfsSpcs_Willow, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"WD", "Pussy Willow", "Salix discolor", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"WI", "Willow", "<<unknown>>", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"WP", "Pacific Willow", "Salix lucida", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"WS", "Scouler's Willow", "Salix scouleriana", "W", "E", CfsTreeSpecies.cfsSpcs_Willow, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"WT", "Sitka Willow", "Salix sitchensis", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"X", "Unknown", "<<unknown>>", "X", "F", CfsTreeSpecies.cfsSpcs_UnspecifiedSoftwood, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"XC", "Unknown conifer", "<<unknown>>", "X", "F", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"XH", "Unknown hardwood", "<<unknown>>", "X", "E", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"Y", "Yellow Cedar", "Chamaecyparis", "C", "Y", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"YC", "Yellow Cedar", "Chamaecyparis nootkatensis", "C", "Y",
				CfsTreeSpecies.cfsSpcs_CypressYellow, true, false, true, new float[] { 60.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"YP", "Port Orford", "Chamaecyparis lawsoniana", "C", "Y", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"Z", "Other Tree", "<<unknown>>", "Z", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"ZC", "Other tree (conifer)", "<<unknown>>", "Z", "F", CfsTreeSpecies.cfsSpcs_SpruceWhite,
				true, false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesDetails(
				"ZH", "Other tree (hardwood)", "<<unknown>>", "Z", "E", CfsTreeSpecies.cfsSpcs_SpruceWhite,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
	}
}
