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
	
	public static final int UNKNOWN_ENTRY_INDEX = 0;
	public static final String UNKNOWN_ENTRY_CODE_NAME = "??";
	
	public static final SpeciesTableItem DefaultEntry = new SpeciesTableItem(
			UNKNOWN_ENTRY_CODE_NAME, "Invalid Species", "Invalid Species", "", "", CfsTreeSpecies.cfsSpcs_UNKNOWN, false,
			false, false, new float[] { -1.0f, -1.0f }, new int[] { -1, -1 });
	
	private final List<SpeciesTableItem> speciesTable = new ArrayList<>();
	private final Map<String, SpeciesTableItem> speciesByTextMap = new HashMap<>();
	
	private void addSpeciesToTable(SpeciesTableItem item) {
		
		int index = speciesTable.size();
		if (index != SP64Name.forText(item.codeName()).ordinal()) {
			throw new IllegalStateException(MessageFormat.format("enumSP64Name's ordinal value for {0}"
					+ " does not match the index the species will be placed in SpeciesTable {1}", item.codeName(), index));
		}
		
		speciesTable.add(item);
		speciesByTextMap.put(item.codeName(), item);
	}
	
	/**
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
		
		return DefaultEntry;
	}
	
	public int getNSpecies() {
		return speciesTable.size() - 1;
	};

	public SpeciesTable() {
		
		addSpeciesToTable(DefaultEntry);
		
		addSpeciesToTable(new SpeciesTableItem(
				"A", "Aspen/Cottonwood/Poplar", "Populus", "A", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"ABAL", "Silver Fir", "Abies alba", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"ABCO", "White Fir", "Abies concolor", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"AC", "Poplar", "Populus balsamifera", "CT", "AC", CfsTreeSpecies.cfsSpcs_BalsamPoplar, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"ACB", "Balsam Poplar", "Populus balsamifera ssp. balsamifera", "CT", "AC",
				CfsTreeSpecies.cfsSpcs_BalsamPoplar, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"ACT", "Black Cottonwood", "Populus balsamifera spp. trichocarpa", "CT", "AC",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"AD", "Cottonwood (exotic)", "<<unknown>>", "CT", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"AH", "Poplar Cottonwood hybrid", "<<unknown>>", "??", "AC",
				CfsTreeSpecies.cfsSpcs_AspenTrembling, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"AT", "Trembling Aspen", "Populus tremuloides", "A", "AT",
				CfsTreeSpecies.cfsSpcs_AspenTrembling, true, true, false, new float[] { 52.0f, 52.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"AX", "Hybrid Poplars", "Populus ssp.", "CT", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"B", "Balsam", "Abies", "B", "B", CfsTreeSpecies.cfsSpcs_Fir, true, false, true,
				new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BA", "Amabilis/Pacific Silver Fir", "Abies amabilis", "B", "B",
				CfsTreeSpecies.cfsSpcs_FirAmabilis, true, false, true, new float[] { 57.0f, 57.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BAC", "Amabilis fir (coast)", "<<unknown>>", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BAI", "Amabilis fir (interior)", "<<unknown>>", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BB", "Balsam Fir", "Abies balsamea", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BC", "White Fir", "Abies concolor", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BG", "Grand Fir", "Abies grandis", "B", "B", CfsTreeSpecies.cfsSpcs_FirGrand, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BI", "Birch", "Betula", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BL", "Alpine Fir", "Abies lasiocarpa", "B", "B",
				CfsTreeSpecies.cfsSpcs_FirSubalpineOrAlpine, true, false, true, new float[] { 42.0f, 42.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BM", "Shasta Red Fir", "Abies magnifica var. shastensis", "B", "B",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 57.0f, 42.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BN", "Noble fir", "<<unknown>>", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BP", "Noble Fir", "Abies procera", "B", "B", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 57.0f, 42.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"BV", "Silver/Paper Birch", "<<unknown>>", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"C", "Cedar", "Thuja", "C", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true,
				new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"CI", "Incense Cedar", "Calocedrus decurrens", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"COT", "Cottonwood", "<<unknown>>", "CT", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"CP", "Port Orford Cedar", "<<unknown>>", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"CT", "Cottonwood", "<<unknown>>", "CT", "AC", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"CW", "Western Red Cedar", "Thuja plicata", "C", "C", CfsTreeSpecies.cfsSpcs_CedarWesternRed,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"CY", "Yellow Cedar", "<<unknown>>", "C", "Y", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"D", "Alder", "Alnus", "D", "D", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"DF", "Douglas Fir", "Pseudotsuga menziesii", "F", "F", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"DG", "Sitka (green) Alder", "<<unknown>>", "D", "D", CfsTreeSpecies.cfsSpcs_AlderSitka,
				true, true, false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"DM", "Mountain Alder", "<<unknown>>", "D", "D", CfsTreeSpecies.cfsSpcs_AlderSitka, true,
				true, false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"DR", "Red Alder", "Alnus rubra", "D", "D", CfsTreeSpecies.cfsSpcs_AlderRed, true, true,
				false, new float[] { 72.0f, 72.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"E", "Birch", "Betula", "BI", "E", CfsTreeSpecies.cfsSpcs_BirchWhite, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"EA", "Common Paper Birch", "Betula neoalaskana", "BI", "E",
				CfsTreeSpecies.cfsSpcs_BirchAlaskaPaper, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"EB", "Bog Birch", "<<unknown>>", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"EE", "European Birch", "Betula pendula", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"EP", "Silver Paper Birch", "Betula papyrifera", "BI", "E",
				CfsTreeSpecies.cfsSpcs_BirchWhite, true, true, false, new float[] { 61.0f, 61.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"ES", "Silver Birch (exotic)", "Betula pubescens", "BI", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"EW", "Water Birch", "Betula occidentalis", "BI", "E", CfsTreeSpecies.cfsSpcs_BirchWhite,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"EXP", "Alaska x Paper Birch Hybrid", "Betula x. winteri", "BI", "E",
				CfsTreeSpecies.cfsSpcs_BirchAlaskaPaperAndWhite, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"F", "Douglas Fir", "Pseudotsuga", "F", "F", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"FD", "Douglas Fir", "Pseudotsuga menziesii", "F", "F",
				CfsTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"FDC", "Douglas Fir (Coastal)", "Pseudotsuga mensiesii var. menziesii", "F", "F",
				CfsTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"FDI", "Douglas Fir (Interior)", "Pseudotsuga menziesii var. glauca", "F", "F",
				CfsTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, true, false, true,
				new float[] { 48.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"G", "Dogwood", "Cornus", "G", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"GP", "Pacific Dogwood", "Cornus nuttallii", "G", "MB",
				CfsTreeSpecies.cfsSpcs_DogwoodWesternflowering, true, false, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"GR", "Red-Osier Dogwood", "<<unknown>>", "G", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"H", "Hemlock", "Tsuga", "H", "H", CfsTreeSpecies.cfsSpcs_Hemlock, true, false, true,
				new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"HM", "Mountain Hemlock", "Tsuga mertensiana", "H", "H",
				CfsTreeSpecies.cfsSpcs_HemlockMountain, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"HW", "Western Hemlock", "Tsuga heterophylla", "H", "H",
				CfsTreeSpecies.cfsSpcs_HemlockWestern, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"HWC", "Western hemlock (coast)", "<<unknown>>", "H", "H", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"HWI", "Western hemlock (interior)", "<<unknown>>", "H", "H", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 51.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"HXM", "Western/Mountain Hemlock cross", "<<unknown>>", "H", "H",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"IG", "Giant Sequoia", "Sequoiadendron giganteum", "C", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"IS", "Coast Redwood", "Sequoia sempervirens", "C", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"J", "Juniper", "Juniperus", "J", "C", CfsTreeSpecies.cfsSpcs_JuniperRockyMountain, true,
				false, true, new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"JR", "Rocky Mountain Juniper", "Juniperus scopulorum", "J", "C",
				CfsTreeSpecies.cfsSpcs_JuniperRockyMountain, true, false, true, new float[] { 60.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"K", "Cascara", "Rhamnus", "K", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"KC", "Casara", "Rhamnus Purshiana", "K", "E", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"L", "Larch", "Larix", "L", "L", CfsTreeSpecies.cfsSpcs_TamarackLarch, true, true, false,
				new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"LA", "Alpine Larch", "Larix lyallii", "L", "L", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"LE", "Eastern Larch", "<<unknown>>", "??", "L", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"LT", "Tamarack", "Larix laricina", "L", "L", CfsTreeSpecies.cfsSpcs_Tamarack, true, true,
				false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"LW", "Western Larch", "Larix occidentalis", "L", "L", CfsTreeSpecies.cfsSpcs_LarchWestern,
				true, true, false, new float[] { 54.0f, 54.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"M", "Maple", "Acer", "M", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"MB", "Broadleaf Maple", "Acer macrophyllum", "M", "MB", CfsTreeSpecies.cfsSpcs_MapleBigleaf,
				true, true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"ME", "Box elder", "Acer negundo", "??", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"MN", "Norway Maple", "Acer platanoides", "M", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"MR", "Rocky Mountain Maple", "<<unknown>>", "M", "MB", CfsTreeSpecies.cfsSpcs_Maple, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"MS", "Sycamore Maple", "<<unknown>>", "M", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"MV", "Vine Maple", "Acer macrophyllum", "M", "MB", CfsTreeSpecies.cfsSpcs_Maple, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"OA", "Incense cedar", "Calocedrus decurrens", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"OB", "Giant sequoia", "Seqoiadendron giganteum", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"OC", "Coast redwood", "Sequoia sempervirens", "??", "C", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"OD", "European mountain Ash", "Sorbus aucuparia", "??", "MB",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, false, new float[] { 71.0f, 71.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"OE", "Siberian elm", "Ulmus pumila", "??", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"OF", "Common pear", "Pyrus communis", "??", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"OG", "Oregon ash", "Fraxinus latifolia", "??", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"P", "Pine", "Pinus", "PL", "PL", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true,
				new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PA", "Whitebark Pine", "Pinus albicaulis", "PL", "PA", CfsTreeSpecies.cfsSpcs_PineWhitebark,
				true, false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PF", "Limber Pine", "Pinus Flexilis", "PL", "PA", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PJ", "Jack Pine", "Pinus banksiana", "PL", "PL", CfsTreeSpecies.cfsSpcs_PineJack, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PL", "Lodgepole Pine", "Pinus contorta", "PL", "PL", CfsTreeSpecies.cfsSpcs_PineLodgepole,
				true, false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PLC", "Lodgepole Pine (Coastal)", "Pinus contorta var. contorta", "PL", "PL",
				CfsTreeSpecies.cfsSpcs_PineShore, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PLI", "Lodgepole Pine (Interior)", "Pinus contorta var. latifolia", "PL", "PL",
				CfsTreeSpecies.cfsSpcs_PineLodgepole, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PM", "Monterray Pine", "Pinus radiata", "PL", "PW", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PR", "Red Pine", "Pinus Resinosa", "PL", "PW", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PS", "Sugar Pine", "Pinus lambertiana", "PL", "PW", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 55.0f, 55.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PV", "Ponderosa pine", "Pinus ponderosa", "PY", "PY", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 30.0f, 30.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PW", "Western White Pine", "Pinus monticola", "PW", "PW",
				CfsTreeSpecies.cfsSpcs_PineWesternWhite, true, false, true, new float[] { 55.0f, 55.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PXJ", "Lodgepole/Jack Pine Hybrid", "Pinus x. murraybanksiana", "PL", "PL",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 50.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"PY", "Yellow Pine", "Pinus ponderosa", "PY", "PY", CfsTreeSpecies.cfsSpcs_PinePonderosa,
				true, false, true, new float[] { 30.0f, 30.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"Q", "Oak", "Quercus", "Q", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"QE", "English Oak", "Quercus robur", "Q", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"QG", "Garry Oak", "Quercus Garryana", "Q", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"R", "Arbutus", "Arbutus", "R", "MB", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"RA", "Arbutus", "Arbutus menziesii", "R", "MB", CfsTreeSpecies.cfsSpcs_Arbutus, true, true,
				false, new float[] { 71.0f, 71.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"S", "Spruce", "Picea", "S", "S", CfsTreeSpecies.cfsSpcs_Spruce, true, false, true,
				new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SA", "Norway Spruce", "<<unknown>>", "??", "S", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SB", "Black Spruce", "Picea mariana", "S", "S", CfsTreeSpecies.cfsSpcs_SpruceBlack, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SE", "Engelmann Spruce", "Picea engelmannii", "S", "S",
				CfsTreeSpecies.cfsSpcs_SpruceEnglemann, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SI", "Interior Spruce", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SN", "Norway Spruce", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SS", "Sitka Spruce", "Picea sitchensis", "S", "S", CfsTreeSpecies.cfsSpcs_SpruceSitka, true,
				false, true, new float[] { 50.0f, 50.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SW", "White Spruce", "Picea glauca", "S", "S", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 46.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SX", "Spruce Hybrid", "Picea x", "S", "S", CfsTreeSpecies.cfsSpcs_Spruce, true, false, true,
				new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SXB", "SXxSB hybrid", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SXE", "SSxSE hybrid", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SXL", "Sitka/White Spruce Hybrid", "Picea sitchensis x. lutzii", "S", "S",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SXS", "Sitka Spruce Hybrid", "Picea sitchensis x", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SXW", "Engelmann/White Spruce Hybrid", "Picea engelmannii x. glauca", "S", "S",
				CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false, true, new float[] { 50.0f, 46.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"SXX", "SW hybrid", "<<unknown>>", "S", "S", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 50.0f, 46.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"T", "Yew", "Taxus", "T", "H", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, true,
				new float[] { 61.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"TW", "Pacific (western) yew", "Taxus brevifolia", "T", "H",
				CfsTreeSpecies.cfsSpcs_YewWestern, true, true, true, new float[] { 61.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"U", "Apple", "Malus", "U", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"UA", "Apple", "Malus pumila", "U", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"UP", "Crab apple", "Malus fusca", "U", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"V", "Cherry", "Prunus", "V", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"VB", "Bitter Cherry", "Prunus emarginata", "V", "E", CfsTreeSpecies.cfsSpcs_CherryBitter,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"VP", "Pin Cherry", "Prunus pensylvanica", "V", "E", CfsTreeSpecies.cfsSpcs_CherryPin, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"VS", "Sweet Cherry", "Prunus avium", "V", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"VV", "Choke Cherry", "Prunus virginiana", "V", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"W", "Willow", "Salix", "W", "E", CfsTreeSpecies.cfsSpcs_Willow, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"WA", "Peachleaf Willow", "Salix amygdaloides", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"WB", "Bebb's Willow", "Salix bebbiana", "W", "E", CfsTreeSpecies.cfsSpcs_Willow, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"WD", "Pussy Willow", "Salix discolor", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"WI", "Willow", "<<unknown>>", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true, false,
				new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"WP", "Pacific Willow", "Salix lucida", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, true,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"WS", "Scouler's Willow", "Salix scouleriana", "W", "E", CfsTreeSpecies.cfsSpcs_Willow, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"WT", "Sitka Willow", "Salix sitchensis", "W", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"X", "Unknown", "<<unknown>>", "X", "F", CfsTreeSpecies.cfsSpcs_UnspecifiedSoftwood, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"XC", "Unknown conifer", "<<unknown>>", "X", "F", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"XH", "Unknown hardwood", "<<unknown>>", "X", "E", CfsTreeSpecies.cfsSpcs_SpruceWhite, true,
				true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"Y", "Yellow Cedar", "Chamaecyparis", "C", "Y", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"YC", "Yellow Cedar", "Chamaecyparis nootkatensis", "C", "Y",
				CfsTreeSpecies.cfsSpcs_CypressYellow, true, false, true, new float[] { 60.0f, 51.0f },
				new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"YP", "Port Orford", "Chamaecyparis lawsoniana", "C", "Y", CfsTreeSpecies.cfsSpcs_UNKNOWN,
				true, false, true, new float[] { 60.0f, 51.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"Z", "Other Tree", "<<unknown>>", "Z", "E", CfsTreeSpecies.cfsSpcs_UNKNOWN, true, false,
				false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"ZC", "Other tree (conifer)", "<<unknown>>", "Z", "F", CfsTreeSpecies.cfsSpcs_SpruceWhite,
				true, false, true, new float[] { 61.0f, 48.0f }, new int[] { -1, -1 }
		));
		addSpeciesToTable(new SpeciesTableItem(
				"ZH", "Other tree (hardwood)", "<<unknown>>", "Z", "E", CfsTreeSpecies.cfsSpcs_SpruceWhite,
				true, true, false, new float[] { 61.0f, 61.0f }, new int[] { -1, -1 }
		));
	}
}
