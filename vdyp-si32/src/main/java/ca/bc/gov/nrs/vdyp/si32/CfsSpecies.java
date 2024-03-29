package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSTreeGenus;
import ca.bc.gov.nrs.vdyp.si32.enumerations.CFSTreeSpecies;

public class CfsSpecies {
	/**
	 * Holds all of the attributes corresponding to each of the species defined by the Canadian Forest Service. The CFS
	 * specified number for the identified species.
	 * <p>
	 * Remarks
	 * <p>
	 * This array must match exactly the ordering of the enumeration 'CFSTreeSpecies'.
	 * <p>
	 * Data for this array is taken from Appendix 7 of the document 'Model_based_volume_to_biomass_CFS.pdf' found in the
	 * folder 'Documents/CFS-Biomass'.
	 * <p>
	 * The list of enumeration constants is automatically generated and copy and pasted into this 
	 * enum definition from the:
	 * <ol>
	 * <li> 'Conversion Param Enum Defn' column of the 
	 * <li> 'DeadConversionFactorsTable' found on the 
	 * <li> 'Derived C Species Table' tab in the
	 * <li> 'BC_Inventory_updates_by_CBMv2bs.xlsx' located in the
	 * <li> 'Documents/CFS-Biomass' folder.
	 * </ol>
	 */
	public record Attributes(
			CFSTreeSpecies cfsSpecies,
			/** The CFS specified name for the identified species */
			String cfsSpcsNm,
			CFSTreeGenus cfsGenus)
	{
		public int cfsSpcsNum() { 
			return cfsSpecies.getCfsSpeciesNumber();
		}
		
		public String cfsSpcsEnumName() {
			return cfsSpecies.toString();
		}
		
		public String cfsGenusEnumName() {
			return cfsGenus.toString();
		}
	}
	
	private static Map<String, Attributes> attributesByName = new HashMap<>();
	
	/**
	 * Perform a case-insensitive search for the attributes of the species with
	 * the given name. If the parameter is null or doesn't match any species,
	 * CFSTreeSpecies.cfsSpcs_UNKNOWN is returned.
	 * 
	 * @param cfsSpeciesName the name of the species to look up
	 * @return as described
	 */
	public static CFSTreeSpecies getSpeciesBySpeciesName(String cfsSpeciesName) {
		
		if (cfsSpeciesName != null) {
			
			Attributes a = attributesByName.get(cfsSpeciesName.toUpperCase());
			if (a != null) {
				return a.cfsSpecies;
			}
		}

		return CFSTreeSpecies.cfsSpcs_UNKNOWN;
	}

	private static Map<CFSTreeSpecies, Attributes> genusBySpecies = new HashMap<>();
	
	/**
	 * Perform a case-insensitive search for the genus of the given species.
	 * If the parameter is null CFSTreeGenus.cfsGenus_UNKNOWN is returned.
	 * 
	 * @param cfsSpecies the name of the species to look up
	 * @return as described
	 */
	public static CFSTreeGenus getGenusBySpecies(CFSTreeSpecies cfsSpecies) {
		
		if (cfsSpecies != null) {
			Attributes a = genusBySpecies.get(cfsSpecies);
			if (a != null) {
				return a.cfsGenus;
			}
		}

		return CFSTreeGenus.cfsGenus_UNKNOWN ;
	}
	
	public static int getSpeciesIndexBySpecies(CFSTreeSpecies cfsSpecies) {
		
		if (cfsSpecies != null) {
			return array[cfsSpecies.getValue()].cfsSpcsNum();
		} else {
			return array[0].cfsSpcsNum();
		}
	}

	private static final Attributes[] array = {
			new Attributes(
					CFSTreeSpecies.cfsSpcs_UNKNOWN, "Unknown Species",
					CFSTreeGenus.cfsGenus_UNKNOWN
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Spruce, "Spruce",
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceBlack, "Black Spruce",
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceRed, "Red Spruce",
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceNorway, "Norway spruce",
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceEnglemann, "Englemann spruce",
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceWhite, "White spruce",
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceSitka, "Sitka spruce",
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceBlackAndRed, "Black and red spruce", 
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceRedAndWhite, "Red and white spruce", 
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceOther, "Other spruce",
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SpruceAndBalsamFir, "Spruce and balsam fir", 
					CFSTreeGenus.cfsGenus_Spruce
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Pine, "Pine", CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineWesternWhite, "Western white pine", 
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineEasternWhite, "Eastern white pine", 
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineJack, "Jack pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineLodgepole, "Lodgepole pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineShore, "Shore pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineWhitebark, "Whitebark pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineAustrian, "Austrian pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PinePonderosa, "Ponderosa pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineRed, "Red pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PinePitch, "Pitch pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineScots, "Scots pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineMugho, "Mugho pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineLimber, "Limber pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineJackLodgepoleAndShore, "Jack, lodgepole, and shore pine", 
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineOther, "Other pine",
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineHybridJackLodgepole, "Hybrid jack and lodgepole pine", 
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PineWhitebarkAndLimber, "Whitebark and limber pine", 
					CFSTreeGenus.cfsGenus_Pine
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Fir, "Fir", CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirAmabilis, "Amabilis fir",
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirBalsam, "Balsam fir",
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirGrand, "Grand fir",
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirSubalpineOrAlpine, "Subalpinefir (or alpine fir)", 
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirBalsamAndAlpine, "Balsam and alpine fir", 
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirAmabilisAndGrand, "Alpine, amabilis, and grand fir", 
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirJapanese, "Japanese fir",
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirSpruceAndBalsam, "Spruce and balsam fir", 
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirBalsamAndSpruce, "Balsam fir and spruce", 
					CFSTreeGenus.cfsGenus_Fir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Hemlock, "Hemlock",
					CFSTreeGenus.cfsGenus_Hemlock
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HemlockEastern, "Eastern hemlock",
					CFSTreeGenus.cfsGenus_Hemlock
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HemlockWestern, "Western hemlock",
					CFSTreeGenus.cfsGenus_Hemlock
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HemlockMountain, "Mountain hemlock",
					CFSTreeGenus.cfsGenus_Hemlock
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HemlockWesternAndMountain, "Western and mountain hemlock", 
					CFSTreeGenus.cfsGenus_Hemlock
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, "Douglas-fir and Rocky Mountain Douglas-fir", 
					CFSTreeGenus.cfsGenus_DouglasFir
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_TamarackLarch, "Tamarack/Larch",
					CFSTreeGenus.cfsGenus_Larch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_LarchEuropean, "European larch",
					CFSTreeGenus.cfsGenus_Larch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Tamarack, "Tamarack",
					CFSTreeGenus.cfsGenus_Larch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_LarchWestern, "Western larch",
					CFSTreeGenus.cfsGenus_Larch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_LarchSubalpine, "Subalpine larch",
					CFSTreeGenus.cfsGenus_Larch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_LarchJapanese, "Japanese larch",
					CFSTreeGenus.cfsGenus_Larch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Cedar, "Cedar",
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CedarEasternWhite, "Eastern white-cedar", 
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CedarWesternRed, "Western red cedar",
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CedarAndOtherConifers, "Cedar and other conifers", 
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Juniper, "Juniper",
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CedarEasternRed, "Eastern red cedar",
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_JuniperRockyMountain, "Rocky Mountain juniper", 
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Yew, "Yew",
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_YewWestern, "Western yew",
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Cypress, "Cypress",
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CypressYellow, "Yellow cypress",
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OtherSoftwoodsConifers, "Other softwoods/other conifers", 
					CFSTreeGenus.cfsGenus_UnspecifiedConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_TamarackAndCedar, "Tamarack and cedar", 
					CFSTreeGenus.cfsGenus_CedarAndOtherConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_UnspecifiedSoftwood, "Unspecified softwood species", 
					CFSTreeGenus.cfsGenus_UnspecifiedConifers
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PoplarAspen, "Poplar/aspen",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AspenTrembling, "Trembling aspen",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PoplarEuropeanWhite, "European white poplar", 
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BalsamPoplar, "Balsam poplar",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CottonwoodBlack, "Black cottonwood",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CottonwoodEastern, "Eastern cottonwood", 
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AspenLargetooth, "Largetooth aspen",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PoplarCarolina, "Carolina poplar",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PoplarLombardy, "Lombardy poplar",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PoplarHybrid, "Hybrid poplar",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PoplarOther, "Other poplar",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PoplarBalsamLargetoothEastern, "Balsam poplar, largetooth aspen and eastern cottonwood",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_PoplarBalsamBlackCottonwood, "Balsam poplar and black cottonwood",
					CFSTreeGenus.cfsGenus_Poplar
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Birch, "Birch", CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchYellow, "Yellow birch", CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchCherry, "Cherry birch",	CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchWhite, "White birch", CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchGray, "Gray birch",	CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchAlaskaPaper, "Alaska paper birch", 
					CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchMountainPaper, "Mountain paper birch", 
					CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchOther, "Other birch", CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchAlaskaPaperAndWhite, "Alaska paper and white birch", 
					CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchEuropean, "European birch",	
					CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BirchWhiteAndGray, "White and gray birch", 
					CFSTreeGenus.cfsGenus_Birch
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Maple, "Maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleSugar, "Sugar maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleBlack, "Black maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleBigleaf, "Bigleaf maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleManitoba, "Manitoba maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleRed, "Red maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleSilver, "Silver maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleNorway, "Norway maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleSugarAndBlack, "Sugar and black maple", 
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleOther, "Other maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleStriped, "Striped maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleMountain, "Mountain maple",
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MapleSilverAndRed, "Silver and red maple", 
					CFSTreeGenus.cfsGenus_Maple
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HardwoodOtherBroadleafOther,	"Other hardwoods/other broad-leaved species",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HardwoodUnspecified, "Unspecified hardwood species", 
					CFSTreeGenus.cfsGenus_UnspecifiedBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Hickory, "Hickory", CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HickoryBitternut, "Bitternut hickory", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HickoryRed, "Red hickory (Pignut hickory)", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HickoryShagbark, "Shagbark hickory",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HickoryShellbark,
					"Shellbark hickory", CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Walnut, "Walnut",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Butternut, "Butternut",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_WalnutBlack, "Black walnut",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Alder, "Alder",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AlderSitka, "Sitka alder",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AlderRed, "Red alder",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AlderGreen, "Green alder",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AlderMountain, "Mountain alder",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AlderSpeckled, "Speckled alder",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_IronwoodHopHornbean, "Ironwood (hop-hornbeam)", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BlueBeech, "Blue-beech (American hornbeam)", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Beech, "Beech",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Oak, "Oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakWhite, "White oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakSwampwhite, "Swamp white oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakGarry, "Garry oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakBur, "Bur oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakPin, "Pin oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakChinquapin, "Chinpaquin oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakChestnut, "Chestnut oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakRed, "Red oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakBlack, "Black oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakNorthernPin, "Northern pin oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_OakShumard, "Shumard oak",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Elm, "Elm",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_ElmWhite, "White elm",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_ElmSlippery, "Slippery elm",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_ElmRock, "Rock elm",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_RedMulberry, "Red mulberry",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Tuliptree, "Tulip-tree",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Cucumbertree, "Cucumber-tree",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Sassafras, "Sassafras",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Sycamore, "Sycamore",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Cherry, "Cherry",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CherryBlack, "Black cherry",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CherryPin, "Pin cherry",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CherryBitter, "Bitter cherry",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CherryChoke, "Choke cherry",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Honeylocust, "Honey-locust",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Blacklocust, "Black locust",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Basswood, "Basswood",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Blackgum, "Black-gum",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_DogwoodFlowering, "Flowering dogwood", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_DogwoodEasternflowering,	"Eastern flowering dogwood", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_DogwoodWesternflowering,	"Western flowering dogwood", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_DogwoodAlternateleaf, "Alternate-leaf dogwood", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Arbutus, "Arbutus",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Ash, "Ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshWhite, "White ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshBlack, "Black ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshRed, "Red ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshNorthernred, "Northern red ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshGreen, "Green ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshBlue, "Blue ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshOregon, "Oregon ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshPumpkin, "Pumpkin ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Willow, "Willow",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_WillowBlack, "Black willow",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_WillowPeachleaf, "Peachleaf willow",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_WillowPacific, "Pacific willow",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_WillowCrack, "Crack willow",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_WillowShining, "Shining willow",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_KentuckyCoffeeTree, "Kentucky coffee tree", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Hackberry, "Hackberry",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Serviceberry, "Serviceberry",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_BeakedHazel, "Beaked hazel",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Hawthorn, "Hawthorn",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_CommonWinterberry, "Common winterberry (black-alder)", 
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_Apple, "Apple",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_MountainHolly, "Mountain-holly",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_SumacStaghorn, "Staghorn sumac",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_AshMountain, "Mountain ash",
					CFSTreeGenus.cfsGenus_OtherBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HardwoodTolerant, "Tolerant hardwoods", 
					CFSTreeGenus.cfsGenus_UnspecifiedBroadleaves
			),
			new Attributes(
					CFSTreeSpecies.cfsSpcs_HardwoodIntolerant, "Intolerant hardwoods", 
					CFSTreeGenus.cfsGenus_UnspecifiedBroadleaves
			)
		};
	
	static {
		// Verify that the array is in increasing CFSTreeSpecies.cfsSpeciesNumber order.
		for (int i = 0; i < array.length; i++) {
			if (array[i].cfsSpecies.ordinal() != i) {
				throw new IllegalStateException(MessageFormat.format("CfsSpecies.array[{}].cfsSpecies.ordinal() != {}", i, i));
			}
		}

		// Build the lookup assistance maps
		for (int i = 0; i < array.length; i++) {
			attributesByName.put(array[i].cfsSpcsNm.toUpperCase(), array[i]);
			genusBySpecies.put(array[i].cfsSpecies, array[i]);
		}
	}
}
