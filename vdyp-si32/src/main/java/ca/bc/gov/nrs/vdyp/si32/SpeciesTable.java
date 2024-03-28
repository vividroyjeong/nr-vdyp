package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Maintains all information regarding a particular species.
 * <p>
 * These entries MUST be listed alphabetically by sCodeName. It is essential
 * that this array's contents match {@code enumSP64Enum} exactly.
 * <p>
 * iCrntCurve is left uninitialized until the first request of a site curve
 * is made or assigned at which time defaults will be populated from SINDEX.
 */
public class SpeciesTable {
	
	public static final int UNKNOWN_ENTRY_INDEX = 0;
	public static final String UNKNOWN_ENTRY_CODE_NAME = "??";
	
	public static final structSpeciesTableItem DefaultEntry = new structSpeciesTableItem(
			UNKNOWN_ENTRY_CODE_NAME, "Invalid Species", "Invalid Species", "", "", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, false,
			false, false, new float[] { -1.0f, -1.0f }, new int[] { -1, -1 });
	
	private final List<structSpeciesTableItem> speciesTable = new ArrayList<>();
	private final Map<String, structSpeciesTableItem> speciesByTextMap = new HashMap<>();
	
	private void addSpeciesToTable(structSpeciesTableItem item) {
		
		int index = speciesTable.size();
		if (index != enumSP64Name.forText(item.sCodeName()).ordinal()) {
			throw new IllegalStateException(MessageFormat.format("enumSP64Name's ordinal value for {0}"
					+ " does not match the index the species will be placed in SpeciesTable {1}", item.sCodeName(), index));
		}
		
		speciesTable.add(item);
		speciesByTextMap.put(item.sCodeName(), item);
	}
	
	/**
	 * @param code the two-letter name ("code") of the species.
	 * @return the {@code structSpeciesTableItem} of that name, or {@code DefaultEntry} if not found.
	 */
	public structSpeciesTableItem getByCode(String code) {
		if (speciesByTextMap.containsKey(code)) {
			return speciesByTextMap.get(code);
		} else {
			return DefaultEntry;
		}
	}
	
	public int getNSpecies() {
		return speciesTable.size() - 1;
	};

	public SpeciesTable() {
		
		addSpeciesToTable(DefaultEntry);
		
		addSpeciesToTable(new structSpeciesTableItem(
				"A", "Aspen/Cottonwood/Poplar", "Populus", "A", "AC", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"ABAL", "Silver Fir", "Abies alba", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"ABCO", "White Fir", "Abies concolor", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"AC", "Poplar", "Populus balsamifera", "CT", "AC", enumIntCFSTreeSpecies.cfsSpcs_BalsamPoplar, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"ACB", "Balsam Poplar", "Populus balsamifera ssp. balsamifera", "CT", "AC",
				enumIntCFSTreeSpecies.cfsSpcs_BalsamPoplar, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"ACT", "Black Cottonwood", "Populus balsamifera spp. trichocarpa", "CT", "AC",
				enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"AD", "Cottonwood (exotic)", "<<unknown>>", "CT", "AC", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"AH", "Poplar Cottonwood hybrid", "<<unknown>>", "??", "AC",
				enumIntCFSTreeSpecies.cfsSpcs_AspenTrembling, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"AT", "Trembling Aspen", "Populus tremuloides", "A", "AT",
				enumIntCFSTreeSpecies.cfsSpcs_AspenTrembling, true, true, false, new float[] { 52.0f, 52.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"AX", "Hybrid Poplars", "Populus ssp.", "CT", "AC", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"B", "Balsam", "Abies", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_Fir, true, false, true,
				new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BA", "Amabilis/Pacific Silver Fir", "Abies amabilis", "B", "B",
				enumIntCFSTreeSpecies.cfsSpcs_FirAmabilis, true, false, true, new float[] { 57.0f, 57.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BAC", "Amabilis fir (coast)", "<<unknown>>", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BAI", "Amabilis fir (interior)", "<<unknown>>", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BB", "Balsam Fir", "Abies balsamea", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BC", "White Fir", "Abies concolor", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BG", "Grand Fir", "Abies grandis", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_FirGrand, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BI", "Birch", "Betula", "BI", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BL", "Alpine Fir", "Abies lasiocarpa", "B", "B",
				enumIntCFSTreeSpecies.cfsSpcs_FirSubalpineOrAlpine, true, false, true, new float[] { 42.0f, 42.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BM", "Shasta Red Fir", "Abies magnifica var. shastensis", "B", "B",
				enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 57.0f, 42.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BN", "Noble fir", "<<unknown>>", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BP", "Noble Fir", "Abies procera", "B", "B", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"BV", "Silver/Paper Birch", "<<unknown>>", "BI", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"C", "Cedar", "Thuja", "C", "C", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, true,
				new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"CI", "Incense Cedar", "Calocedrus decurrens", "??", "C", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"COT", "Cottonwood", "<<unknown>>", "CT", "AC", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"CP", "Port Orford Cedar", "<<unknown>>", "??", "C", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"CT", "Cottonwood", "<<unknown>>", "CT", "AC", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"CW", "Western Red Cedar", "Thuja plicata", "C", "C", enumIntCFSTreeSpecies.cfsSpcs_CedarWesternRed,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"CY", "Yellow Cedar", "<<unknown>>", "C", "Y", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"D", "Alder", "Alnus", "D", "D", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"DF", "Douglas Fir", "Pseudotsuga menziesii", "F", "F", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"DG", "Sitka (green) Alder", "<<unknown>>", "D", "D", enumIntCFSTreeSpecies.cfsSpcs_AlderSitka,
				true, true, false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"DM", "Mountain Alder", "<<unknown>>", "D", "D", enumIntCFSTreeSpecies.cfsSpcs_AlderSitka, true,
				true, false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"DR", "Red Alder", "Alnus rubra", "D", "D", enumIntCFSTreeSpecies.cfsSpcs_AlderRed, true, true,
				false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"E", "Birch", "Betula", "BI", "E", enumIntCFSTreeSpecies.cfsSpcs_BirchWhite, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"EA", "Common Paper Birch", "Betula neoalaskana", "BI", "E",
				enumIntCFSTreeSpecies.cfsSpcs_BirchAlaskaPaper, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"EB", "Bog Birch", "<<unknown>>", "BI", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"EE", "European Birch", "Betula pendula", "BI", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"EP", "Silver Paper Birch", "Betula papyrifera", "BI", "E",
				enumIntCFSTreeSpecies.cfsSpcs_BirchWhite, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"ES", "Silver Birch (exotic)", "Betula pubescens", "BI", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"EW", "Water Birch", "Betula occidentalis", "BI", "E", enumIntCFSTreeSpecies.cfsSpcs_BirchWhite,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"EXP", "Alaska x Paper Birch Hybrid", "Betula x. winteri", "BI", "E",
				enumIntCFSTreeSpecies.cfsSpcs_BirchAlaskaPaperAndWhite, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"F", "Douglas Fir", "Pseudotsuga", "F", "F", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"FD", "Douglas Fir", "Pseudotsuga menziesii", "F", "F",
				enumIntCFSTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"FDC", "Douglas Fir (Coastal)", "Pseudotsuga mensiesii var. menziesii", "F", "F",
				enumIntCFSTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"FDI", "Douglas Fir (Interior)", "Pseudotsuga menziesii var. glauca", "F", "F",
				enumIntCFSTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 48.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"G", "Dogwood", "Cornus", "G", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"GP", "Pacific Dogwood", "Cornus nuttallii", "G", "MB",
				enumIntCFSTreeSpecies.cfsSpcs_DogwoodWesternflowering, true, false, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"GR", "Red-Osier Dogwood", "<<unknown>>", "G", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"H", "Hemlock", "Tsuga", "H", "H", enumIntCFSTreeSpecies.cfsSpcs_Hemlock, true, false, true,
				new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"HM", "Mountain Hemlock", "Tsuga mertensiana", "H", "H",
				enumIntCFSTreeSpecies.cfsSpcs_HemlockMountain, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"HW", "Western Hemlock", "Tsuga heterophylla", "H", "H",
				enumIntCFSTreeSpecies.cfsSpcs_HemlockWestern, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"HWC", "Western hemlock (coast)", "<<unknown>>", "H", "H", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"HWI", "Western hemlock (interior)", "<<unknown>>", "H", "H", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 51.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"HXM", "Western/Mountain Hemlock cross", "<<unknown>>", "H", "H",
				enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"IG", "Giant Sequoia", "Sequoiadendron giganteum", "C", "C", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"IS", "Coast Redwood", "Sequoia sempervirens", "C", "C", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"J", "Juniper", "Juniperus", "J", "C", enumIntCFSTreeSpecies.cfsSpcs_JuniperRockyMountain, true,
				false, true, new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"JR", "Rocky Mountain Juniper", "Juniperus scopulorum", "J", "C",
				enumIntCFSTreeSpecies.cfsSpcs_JuniperRockyMountain, true, false, true, new float[] { 60.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"K", "Cascara", "Rhamnus", "K", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"KC", "Casara", "Rhamnus Purshiana", "K", "E", enumIntCFSTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"L", "Larch", "Larix", "L", "L", enumIntCFSTreeSpecies.cfsSpcs_TamarackLarch, true, true, false,
				new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"LA", "Alpine Larch", "Larix lyallii", "L", "L", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"LE", "Eastern Larch", "<<unknown>>", "??", "L", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"LT", "Tamarack", "Larix laricina", "L", "L", enumIntCFSTreeSpecies.cfsSpcs_Tamarack, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"LW", "Western Larch", "Larix occidentalis", "L", "L", enumIntCFSTreeSpecies.cfsSpcs_LarchWestern,
				true, true, false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"M", "Maple", "Acer", "M", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"MB", "Broadleaf Maple", "Acer macrophyllum", "M", "MB", enumIntCFSTreeSpecies.cfsSpcs_MapleBigleaf,
				true, true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"ME", "Box elder", "Acer negundo", "??", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"MN", "Norway Maple", "Acer platanoides", "M", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"MR", "Rocky Mountain Maple", "<<unknown>>", "M", "MB", enumIntCFSTreeSpecies.cfsSpcs_Maple, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"MS", "Sycamore Maple", "<<unknown>>", "M", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"MV", "Vine Maple", "Acer macrophyllum", "M", "MB", enumIntCFSTreeSpecies.cfsSpcs_Maple, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"OA", "Incense cedar", "Calocedrus decurrens", "??", "C", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"OB", "Giant sequoia", "Seqoiadendron giganteum", "??", "C", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"OC", "Coast redwood", "Sequoia sempervirens", "??", "C", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"OD", "European mountain Ash", "Sorbus aucuparia", "??", "MB",
				enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, false, new float[] { 71.0f, 71.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"OE", "Siberian elm", "Ulmus pumila", "??", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"OF", "Common pear", "Pyrus communis", "??", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"OG", "Oregon ash", "Fraxinus latifolia", "??", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"P", "Pine", "Pinus", "PL", "PL", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, true,
				new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PA", "Whitebark Pine", "Pinus albicaulis", "PL", "PA", enumIntCFSTreeSpecies.cfsSpcs_PineWhitebark,
				true, false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PF", "Limber Pine", "Pinus Flexilis", "PL", "PA", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PJ", "Jack Pine", "Pinus banksiana", "PL", "PL", enumIntCFSTreeSpecies.cfsSpcs_PineJack, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PL", "Lodgepole Pine", "Pinus contorta", "PL", "PL", enumIntCFSTreeSpecies.cfsSpcs_PineLodgepole,
				true, false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PLC", "Lodgepole Pine (Coastal)", "Pinus contorta var. contorta", "PL", "PL",
				enumIntCFSTreeSpecies.cfsSpcs_PineShore, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PLI", "Lodgepole Pine (Interior)", "Pinus contorta var. latifolia", "PL", "PL",
				enumIntCFSTreeSpecies.cfsSpcs_PineLodgepole, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PM", "Monterray Pine", "Pinus radiata", "PL", "PW", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PR", "Red Pine", "Pinus Resinosa", "PL", "PW", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PS", "Sugar Pine", "Pinus lambertiana", "PL", "PW", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PV", "Ponderosa pine", "Pinus ponderosa", "PY", "PY", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 30.0f, 30.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PW", "Western White Pine", "Pinus monticola", "PW", "PW",
				enumIntCFSTreeSpecies.cfsSpcs_PineWesternWhite, true, false, true, new float[] { 55.0f, 55.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PXJ", "Lodgepole/Jack Pine Hybrid", "Pinus x. murraybanksiana", "PL", "PL",
				enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"PY", "Yellow Pine", "Pinus ponderosa", "PY", "PY", enumIntCFSTreeSpecies.cfsSpcs_PinePonderosa,
				true, false, true, new float[] { 30.0f, 30.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"Q", "Oak", "Quercus", "Q", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"QE", "English Oak", "Quercus robur", "Q", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"QG", "Garry Oak", "Quercus Garryana", "Q", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"R", "Arbutus", "Arbutus", "R", "MB", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"RA", "Arbutus", "Arbutus menziesii", "R", "MB", enumIntCFSTreeSpecies.cfsSpcs_Arbutus, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"S", "Spruce", "Picea", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_Spruce, true, false, true,
				new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SA", "Norway Spruce", "<<unknown>>", "??", "S", enumIntCFSTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SB", "Black Spruce", "Picea mariana", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_SpruceBlack, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SE", "Engelmann Spruce", "Picea engelmannii", "S", "S",
				enumIntCFSTreeSpecies.cfsSpcs_SpruceEnglemann, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SI", "Interior Spruce", "<<unknown>>", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SN", "Norway Spruce", "<<unknown>>", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SS", "Sitka Spruce", "Picea sitchensis", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_SpruceSitka, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SW", "White Spruce", "Picea glauca", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SX", "Spruce Hybrid", "Picea x", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_Spruce, true, false, true,
				new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SXB", "SXxSB hybrid", "<<unknown>>", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SXE", "SSxSE hybrid", "<<unknown>>", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SXL", "Sitka/White Spruce Hybrid", "Picea sitchensis x. lutzii", "S", "S",
				enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SXS", "Sitka Spruce Hybrid", "Picea sitchensis x", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SXW", "Engelmann/White Spruce Hybrid", "Picea engelmannii x. glauca", "S", "S",
				enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"SXX", "SW hybrid", "<<unknown>>", "S", "S", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"T", "Yew", "Taxus", "T", "H", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, true,
				new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"TW", "Pacific (western) yew", "Taxus brevifolia", "T", "H",
				enumIntCFSTreeSpecies.cfsSpcs_YewWestern, true, true, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"U", "Apple", "Malus", "U", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"UA", "Apple", "Malus pumila", "U", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"UP", "Crab apple", "Malus fusca", "U", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"V", "Cherry", "Prunus", "V", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"VB", "Bitter Cherry", "Prunus emarginata", "V", "E", enumIntCFSTreeSpecies.cfsSpcs_CherryBitter,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"VP", "Pin Cherry", "Prunus pensylvanica", "V", "E", enumIntCFSTreeSpecies.cfsSpcs_CherryPin, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"VS", "Sweet Cherry", "Prunus avium", "V", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"VV", "Choke Cherry", "Prunus virginiana", "V", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"W", "Willow", "Salix", "W", "E", enumIntCFSTreeSpecies.cfsSpcs_Willow, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"WA", "Peachleaf Willow", "Salix amygdaloides", "W", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"WB", "Bebb's Willow", "Salix bebbiana", "W", "E", enumIntCFSTreeSpecies.cfsSpcs_Willow, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"WD", "Pussy Willow", "Salix discolor", "W", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"WI", "Willow", "<<unknown>>", "W", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"WP", "Pacific Willow", "Salix lucida", "W", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"WS", "Scouler's Willow", "Salix scouleriana", "W", "E", enumIntCFSTreeSpecies.cfsSpcs_Willow, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"WT", "Sitka Willow", "Salix sitchensis", "W", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"X", "Unknown", "<<unknown>>", "X", "F", enumIntCFSTreeSpecies.cfsSpcs_UnspecifiedSoftwood, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"XC", "Unknown conifer", "<<unknown>>", "X", "F", enumIntCFSTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"XH", "Unknown hardwood", "<<unknown>>", "X", "E", enumIntCFSTreeSpecies.cfsSpcs_SpruceWhite, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"Y", "Yellow Cedar", "Chamaecyparis", "C", "Y", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"YC", "Yellow Cedar", "Chamaecyparis nootkatensis", "C", "Y",
				enumIntCFSTreeSpecies.cfsSpcs_CypressYellow, true, false, true, new float[] { 60.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"YP", "Port Orford", "Chamaecyparis lawsoniana", "C", "Y", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"Z", "Other Tree", "<<unknown>>", "Z", "E", enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"ZC", "Other tree (conifer)", "<<unknown>>", "Z", "F", enumIntCFSTreeSpecies.cfsSpcs_SpruceWhite,
				true, false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new structSpeciesTableItem(
				"ZH", "Other tree (hardwood)", "<<unknown>>", "Z", "E", enumIntCFSTreeSpecies.cfsSpcs_SpruceWhite,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
	}
}
