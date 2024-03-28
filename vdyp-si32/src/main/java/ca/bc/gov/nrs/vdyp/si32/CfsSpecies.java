package ca.bc.gov.nrs.vdyp.si32;

public class CfsSpecies {
	/**
	 * Holds all of the attributes corresponding to each of the species defined by the Canadian Forest Service. The CFS
	 * specified number for the identified species.
	 * <p>
	 * Remarks
	 * <p>
	 * This array must match exactly the ordering of the enumeration 'enumIntCFSTreeSpecies'.
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
			/** The CFS specified number for the identified species. */
			int cfsSpcsNum, 
			enumIntCFSTreeSpecies cfsSpcsEnum,
			/** The string corresponding to the enumeration constant representing the species. */
			String cfsSpcsEnumName, 
			/** The CFS specified name for the identified species */
			String cfsSpcsNm,
			enumIntCFSTreeGenus cfsGenusEnum, 
			/** The string corresponding to the enumeration constant representing the species. */
			String cfsGenusEnumName)
	{}

	public static final Attributes[] array = {
			new Attributes(
					-1, enumIntCFSTreeSpecies.cfsSpcs_UNKNOWN, "cfsSpcs_UNKNOWN", "Unknown Species",
					enumIntCFSTreeGenus.cfsGenus_UNKNOWN, "cfsGenus_UNKNOWN"
			),
			new Attributes(
					100, enumIntCFSTreeSpecies.cfsSpcs_Spruce, "cfsSpcs_Spruce", "Spruce",
					enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					101, enumIntCFSTreeSpecies.cfsSpcs_SpruceBlack, "cfsSpcs_SpruceBlack", "Black Spruce",
					enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					102, enumIntCFSTreeSpecies.cfsSpcs_SpruceRed, "cfsSpcs_SpruceRed", "Red Spruce",
					enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					103, enumIntCFSTreeSpecies.cfsSpcs_SpruceNorway, "cfsSpcs_SpruceNorway", "Norway spruce",
					enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					104, enumIntCFSTreeSpecies.cfsSpcs_SpruceEnglemann, "cfsSpcs_SpruceEnglemann", "Englemann spruce",
					enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					105, enumIntCFSTreeSpecies.cfsSpcs_SpruceWhite, "cfsSpcs_SpruceWhite", "White spruce",
					enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					106, enumIntCFSTreeSpecies.cfsSpcs_SpruceSitka, "cfsSpcs_SpruceSitka", "Sitka spruce",
					enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					107, enumIntCFSTreeSpecies.cfsSpcs_SpruceBlackAndRed, "cfsSpcs_SpruceBlackAndRed",
					"Black and red spruce", enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					108, enumIntCFSTreeSpecies.cfsSpcs_SpruceRedAndWhite, "cfsSpcs_SpruceRedAndWhite",
					"Red and white spruce", enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					109, enumIntCFSTreeSpecies.cfsSpcs_SpruceOther, "cfsSpcs_SpruceOther", "Other spruce",
					enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					110, enumIntCFSTreeSpecies.cfsSpcs_SpruceAndBalsamFir, "cfsSpcs_SpruceAndBalsamFir",
					"Spruce and balsam fir", enumIntCFSTreeGenus.cfsGenus_Spruce, "cfsGenus_Spruce"
			),
			new Attributes(
					200, enumIntCFSTreeSpecies.cfsSpcs_Pine, "cfsSpcs_Pine", "Pine", enumIntCFSTreeGenus.cfsGenus_Pine,
					"cfsGenus_Pine"
			),
			new Attributes(
					201, enumIntCFSTreeSpecies.cfsSpcs_PineWesternWhite, "cfsSpcs_PineWesternWhite",
					"Western white pine", enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					202, enumIntCFSTreeSpecies.cfsSpcs_PineEasternWhite, "cfsSpcs_PineEasternWhite",
					"Eastern white pine", enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					203, enumIntCFSTreeSpecies.cfsSpcs_PineJack, "cfsSpcs_PineJack", "Jack pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					204, enumIntCFSTreeSpecies.cfsSpcs_PineLodgepole, "cfsSpcs_PineLodgepole", "Lodgepole pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					205, enumIntCFSTreeSpecies.cfsSpcs_PineShore, "cfsSpcs_PineShore", "Shore pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					206, enumIntCFSTreeSpecies.cfsSpcs_PineWhitebark, "cfsSpcs_PineWhitebark", "Whitebark pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					207, enumIntCFSTreeSpecies.cfsSpcs_PineAustrian, "cfsSpcs_PineAustrian", "Austrian pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					208, enumIntCFSTreeSpecies.cfsSpcs_PinePonderosa, "cfsSpcs_PinePonderosa", "Ponderosa pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					209, enumIntCFSTreeSpecies.cfsSpcs_PineRed, "cfsSpcs_PineRed", "Red pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					210, enumIntCFSTreeSpecies.cfsSpcs_PinePitch, "cfsSpcs_PinePitch", "Pitch pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					211, enumIntCFSTreeSpecies.cfsSpcs_PineScots, "cfsSpcs_PineScots", "Scots pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					212, enumIntCFSTreeSpecies.cfsSpcs_PineMugho, "cfsSpcs_PineMugho", "Mugho pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					213, enumIntCFSTreeSpecies.cfsSpcs_PineLimber, "cfsSpcs_PineLimber", "Limber pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					214, enumIntCFSTreeSpecies.cfsSpcs_PineJackLodgepoleAndShore, "cfsSpcs_PineJackLodgepoleAndShore",
					"Jack, lodgepole, and shore pine", enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					215, enumIntCFSTreeSpecies.cfsSpcs_PineOther, "cfsSpcs_PineOther", "Other pine",
					enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					216, enumIntCFSTreeSpecies.cfsSpcs_PineHybridJackLodgepole, "cfsSpcs_PineHybridJackLodgepole",
					"Hybrid jack and lodgepole pine", enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					217, enumIntCFSTreeSpecies.cfsSpcs_PineWhitebarkAndLimber, "cfsSpcs_PineWhitebarkAndLimber",
					"Whitebark and limber pine", enumIntCFSTreeGenus.cfsGenus_Pine, "cfsGenus_Pine"
			),
			new Attributes(
					300, enumIntCFSTreeSpecies.cfsSpcs_Fir, "cfsSpcs_Fir", "Fir", enumIntCFSTreeGenus.cfsGenus_Fir,
					"cfsGenus_Fir"
			),
			new Attributes(
					301, enumIntCFSTreeSpecies.cfsSpcs_FirAmabilis, "cfsSpcs_FirAmabilis", "Amabilis fir",
					enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					302, enumIntCFSTreeSpecies.cfsSpcs_FirBalsam, "cfsSpcs_FirBalsam", "Balsam fir",
					enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					303, enumIntCFSTreeSpecies.cfsSpcs_FirGrand, "cfsSpcs_FirGrand", "Grand fir",
					enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					304, enumIntCFSTreeSpecies.cfsSpcs_FirSubalpineOrAlpine, "cfsSpcs_FirSubalpineOrAlpine",
					"Subalpinefir (or alpine fir)", enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					305, enumIntCFSTreeSpecies.cfsSpcs_FirBalsamAndAlpine, "cfsSpcs_FirBalsamAndAlpine",
					"Balsam and alpine fir", enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					306, enumIntCFSTreeSpecies.cfsSpcs_FirAmabilisAndGrand, "cfsSpcs_FirAmabilisAndGrand",
					"Alpine, amabilis, and grand fir", enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					307, enumIntCFSTreeSpecies.cfsSpcs_FirJapanese, "cfsSpcs_FirJapanese", "Japanese fir",
					enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					320, enumIntCFSTreeSpecies.cfsSpcs_FirSpruceAndBalsam, "cfsSpcs_FirSpruceAndBalsam",
					"Spruce and balsam fir", enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					321, enumIntCFSTreeSpecies.cfsSpcs_FirBalsamAndSpruce, "cfsSpcs_FirBalsamAndSpruce",
					"Balsam fir and spruce", enumIntCFSTreeGenus.cfsGenus_Fir, "cfsGenus_Fir"
			),
			new Attributes(
					400, enumIntCFSTreeSpecies.cfsSpcs_Hemlock, "cfsSpcs_Hemlock", "Hemlock",
					enumIntCFSTreeGenus.cfsGenus_Hemlock, "cfsGenus_Hemlock"
			),
			new Attributes(
					401, enumIntCFSTreeSpecies.cfsSpcs_HemlockEastern, "cfsSpcs_HemlockEastern", "Eastern hemlock",
					enumIntCFSTreeGenus.cfsGenus_Hemlock, "cfsGenus_Hemlock"
			),
			new Attributes(
					402, enumIntCFSTreeSpecies.cfsSpcs_HemlockWestern, "cfsSpcs_HemlockWestern", "Western hemlock",
					enumIntCFSTreeGenus.cfsGenus_Hemlock, "cfsGenus_Hemlock"
			),
			new Attributes(
					403, enumIntCFSTreeSpecies.cfsSpcs_HemlockMountain, "cfsSpcs_HemlockMountain", "Mountain hemlock",
					enumIntCFSTreeGenus.cfsGenus_Hemlock, "cfsGenus_Hemlock"
			),
			new Attributes(
					404, enumIntCFSTreeSpecies.cfsSpcs_HemlockWesternAndMountain, "cfsSpcs_HemlockWesternAndMountain",
					"Western and mountain hemlock", enumIntCFSTreeGenus.cfsGenus_Hemlock, "cfsGenus_Hemlock"
			),
			new Attributes(
					500, enumIntCFSTreeSpecies.cfsSpcs_FirDouglasAndRockyMountain, "cfsSpcs_FirDouglasAndRockyMountain",
					"Douglas-fir and Rocky Mountain Douglas-fir", enumIntCFSTreeGenus.cfsGenus_DouglasFir,
					"cfsGenus_DouglasFir"
			),
			new Attributes(
					600, enumIntCFSTreeSpecies.cfsSpcs_TamarackLarch, "cfsSpcs_TamarackLarch", "Tamarack/Larch",
					enumIntCFSTreeGenus.cfsGenus_Larch, "cfsGenus_Larch"
			),
			new Attributes(
					601, enumIntCFSTreeSpecies.cfsSpcs_LarchEuropean, "cfsSpcs_LarchEuropean", "European larch",
					enumIntCFSTreeGenus.cfsGenus_Larch, "cfsGenus_Larch"
			),
			new Attributes(
					602, enumIntCFSTreeSpecies.cfsSpcs_Tamarack, "cfsSpcs_Tamarack", "Tamarack",
					enumIntCFSTreeGenus.cfsGenus_Larch, "cfsGenus_Larch"
			),
			new Attributes(
					603, enumIntCFSTreeSpecies.cfsSpcs_LarchWestern, "cfsSpcs_LarchWestern", "Western larch",
					enumIntCFSTreeGenus.cfsGenus_Larch, "cfsGenus_Larch"
			),
			new Attributes(
					604, enumIntCFSTreeSpecies.cfsSpcs_LarchSubalpine, "cfsSpcs_LarchSubalpine", "Subalpine larch",
					enumIntCFSTreeGenus.cfsGenus_Larch, "cfsGenus_Larch"
			),
			new Attributes(
					605, enumIntCFSTreeSpecies.cfsSpcs_LarchJapanese, "cfsSpcs_LarchJapanese", "Japanese larch",
					enumIntCFSTreeGenus.cfsGenus_Larch, "cfsGenus_Larch"
			),
			new Attributes(
					700, enumIntCFSTreeSpecies.cfsSpcs_Cedar, "cfsSpcs_Cedar", "Cedar",
					enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers, "cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					701, enumIntCFSTreeSpecies.cfsSpcs_CedarEasternWhite, "cfsSpcs_CedarEasternWhite",
					"Eastern white-cedar", enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers,
					"cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					702, enumIntCFSTreeSpecies.cfsSpcs_CedarWesternRed, "cfsSpcs_CedarWesternRed", "Western red cedar",
					enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers, "cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					703, enumIntCFSTreeSpecies.cfsSpcs_CedarAndOtherConifers, "cfsSpcs_CedarAndOtherConifers",
					"Cedar and other conifers", enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers,
					"cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					800, enumIntCFSTreeSpecies.cfsSpcs_Juniper, "cfsSpcs_Juniper", "Juniper",
					enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers, "cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					801, enumIntCFSTreeSpecies.cfsSpcs_CedarEasternRed, "cfsSpcs_CedarEasternRed", "Eastern red cedar",
					enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers, "cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					802, enumIntCFSTreeSpecies.cfsSpcs_JuniperRockyMountain, "cfsSpcs_JuniperRockyMountain",
					"Rocky Mountain juniper", enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers,
					"cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					900, enumIntCFSTreeSpecies.cfsSpcs_Yew, "cfsSpcs_Yew", "Yew",
					enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers, "cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					901, enumIntCFSTreeSpecies.cfsSpcs_YewWestern, "cfsSpcs_YewWestern", "Western yew",
					enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers, "cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					1000, enumIntCFSTreeSpecies.cfsSpcs_Cypress, "cfsSpcs_Cypress", "Cypress",
					enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers, "cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					1001, enumIntCFSTreeSpecies.cfsSpcs_CypressYellow, "cfsSpcs_CypressYellow", "Yellow cypress",
					enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers, "cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					1100, enumIntCFSTreeSpecies.cfsSpcs_OtherSoftwoodsConifers, "cfsSpcs_OtherSoftwoodsConifers",
					"Other softwoods/other conifers", enumIntCFSTreeGenus.cfsGenus_UnspecifiedConifers,
					"cfsGenus_UnspecifiedConifers"
			),
			new Attributes(
					1110, enumIntCFSTreeSpecies.cfsSpcs_TamarackAndCedar, "cfsSpcs_TamarackAndCedar",
					"Tamarack and cedar", enumIntCFSTreeGenus.cfsGenus_CedarAndOtherConifers,
					"cfsGenus_CedarAndOtherConifers"
			),
			new Attributes(
					1150, enumIntCFSTreeSpecies.cfsSpcs_UnspecifiedSoftwood, "cfsSpcs_UnspecifiedSoftwood",
					"Unspecified softwood species", enumIntCFSTreeGenus.cfsGenus_UnspecifiedConifers,
					"cfsGenus_UnspecifiedConifers"
			),
			new Attributes(
					1200, enumIntCFSTreeSpecies.cfsSpcs_PoplarAspen, "cfsSpcs_PoplarAspen", "Poplar/aspen",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1201, enumIntCFSTreeSpecies.cfsSpcs_AspenTrembling, "cfsSpcs_AspenTrembling", "Trembling aspen",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1202, enumIntCFSTreeSpecies.cfsSpcs_PoplarEuropeanWhite, "cfsSpcs_PoplarEuropeanWhite",
					"European white poplar", enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1203, enumIntCFSTreeSpecies.cfsSpcs_BalsamPoplar, "cfsSpcs_BalsamPoplar", "Balsam poplar",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1204, enumIntCFSTreeSpecies.cfsSpcs_CottonwoodBlack, "cfsSpcs_CottonwoodBlack", "Black cottonwood",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1205, enumIntCFSTreeSpecies.cfsSpcs_CottonwoodEastern, "cfsSpcs_CottonwoodEastern",
					"Eastern cottonwood", enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1206, enumIntCFSTreeSpecies.cfsSpcs_AspenLargetooth, "cfsSpcs_AspenLargetooth", "Largetooth aspen",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1207, enumIntCFSTreeSpecies.cfsSpcs_PoplarCarolina, "cfsSpcs_PoplarCarolina", "Carolina poplar",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1208, enumIntCFSTreeSpecies.cfsSpcs_PoplarLombardy, "cfsSpcs_PoplarLombardy", "Lombardy poplar",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1209, enumIntCFSTreeSpecies.cfsSpcs_PoplarHybrid, "cfsSpcs_PoplarHybrid", "Hybrid poplar",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1210, enumIntCFSTreeSpecies.cfsSpcs_PoplarOther, "cfsSpcs_PoplarOther", "Other poplar",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1211, enumIntCFSTreeSpecies.cfsSpcs_PoplarBalsamLargetoothEastern,
					"cfsSpcs_PoplarBalsamLargetoothEastern", "Balsam poplar, largetooth aspen and eastern cottonwood",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1212, enumIntCFSTreeSpecies.cfsSpcs_PoplarBalsamBlackCottonwood,
					"cfsSpcs_PoplarBalsamBlackCottonwood", "Balsam poplar and black cottonwood",
					enumIntCFSTreeGenus.cfsGenus_Poplar, "cfsGenus_Poplar"
			),
			new Attributes(
					1300, enumIntCFSTreeSpecies.cfsSpcs_Birch, "cfsSpcs_Birch", "Birch",
					enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1301, enumIntCFSTreeSpecies.cfsSpcs_BirchYellow, "cfsSpcs_BirchYellow", "Yellow birch",
					enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1302, enumIntCFSTreeSpecies.cfsSpcs_BirchCherry, "cfsSpcs_BirchCherry", "Cherry birch",
					enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1303, enumIntCFSTreeSpecies.cfsSpcs_BirchWhite, "cfsSpcs_BirchWhite", "White birch",
					enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1304, enumIntCFSTreeSpecies.cfsSpcs_BirchGray, "cfsSpcs_BirchGray", "Gray birch",
					enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1305, enumIntCFSTreeSpecies.cfsSpcs_BirchAlaskaPaper, "cfsSpcs_BirchAlaskaPaper",
					"Alaska paper birch", enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1306, enumIntCFSTreeSpecies.cfsSpcs_BirchMountainPaper, "cfsSpcs_BirchMountainPaper",
					"Mountain paper birch", enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1307, enumIntCFSTreeSpecies.cfsSpcs_BirchOther, "cfsSpcs_BirchOther", "Other birch",
					enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1308, enumIntCFSTreeSpecies.cfsSpcs_BirchAlaskaPaperAndWhite, "cfsSpcs_BirchAlaskaPaperAndWhite",
					"Alaska paper and white birch", enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1309, enumIntCFSTreeSpecies.cfsSpcs_BirchEuropean, "cfsSpcs_BirchEuropean", "European birch",
					enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1310, enumIntCFSTreeSpecies.cfsSpcs_BirchWhiteAndGray, "cfsSpcs_BirchWhiteAndGray",
					"White and gray birch", enumIntCFSTreeGenus.cfsGenus_Birch, "cfsGenus_Birch"
			),
			new Attributes(
					1400, enumIntCFSTreeSpecies.cfsSpcs_Maple, "cfsSpcs_Maple", "Maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1401, enumIntCFSTreeSpecies.cfsSpcs_MapleSugar, "cfsSpcs_MapleSugar", "Sugar maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1402, enumIntCFSTreeSpecies.cfsSpcs_MapleBlack, "cfsSpcs_MapleBlack", "Black maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1403, enumIntCFSTreeSpecies.cfsSpcs_MapleBigleaf, "cfsSpcs_MapleBigleaf", "Bigleaf maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1404, enumIntCFSTreeSpecies.cfsSpcs_MapleManitoba, "cfsSpcs_MapleManitoba", "Manitoba maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1405, enumIntCFSTreeSpecies.cfsSpcs_MapleRed, "cfsSpcs_MapleRed", "Red maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1406, enumIntCFSTreeSpecies.cfsSpcs_MapleSilver, "cfsSpcs_MapleSilver", "Silver maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1407, enumIntCFSTreeSpecies.cfsSpcs_MapleNorway, "cfsSpcs_MapleNorway", "Norway maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1408, enumIntCFSTreeSpecies.cfsSpcs_MapleSugarAndBlack, "cfsSpcs_MapleSugarAndBlack",
					"Sugar and black maple", enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1409, enumIntCFSTreeSpecies.cfsSpcs_MapleOther, "cfsSpcs_MapleOther", "Other maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1410, enumIntCFSTreeSpecies.cfsSpcs_MapleStriped, "cfsSpcs_MapleStriped", "Striped maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1411, enumIntCFSTreeSpecies.cfsSpcs_MapleMountain, "cfsSpcs_MapleMountain", "Mountain maple",
					enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1412, enumIntCFSTreeSpecies.cfsSpcs_MapleSilverAndRed, "cfsSpcs_MapleSilverAndRed",
					"Silver and red maple", enumIntCFSTreeGenus.cfsGenus_Maple, "cfsGenus_Maple"
			),
			new Attributes(
					1500, enumIntCFSTreeSpecies.cfsSpcs_HardwoodOtherBroadleafOther,
					"cfsSpcs_HardwoodOtherBroadleafOther", "Other hardwoods/other broad-leaved species",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1550, enumIntCFSTreeSpecies.cfsSpcs_HardwoodUnspecified, "cfsSpcs_HardwoodUnspecified",
					"Unspecified hardwood species", enumIntCFSTreeGenus.cfsGenus_UnspecifiedBroadleaves,
					"cfsGenus_UnspecifiedBroadleaves"
			),
			new Attributes(
					1600, enumIntCFSTreeSpecies.cfsSpcs_Hickory, "cfsSpcs_Hickory", "Hickory",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1601, enumIntCFSTreeSpecies.cfsSpcs_HickoryBitternut, "cfsSpcs_HickoryBitternut",
					"Bitternut hickory", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1602, enumIntCFSTreeSpecies.cfsSpcs_HickoryRed, "cfsSpcs_HickoryRed",
					"Red hickory (Pignut hickory)", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves,
					"cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1603, enumIntCFSTreeSpecies.cfsSpcs_HickoryShagbark, "cfsSpcs_HickoryShagbark", "Shagbark hickory",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1604, enumIntCFSTreeSpecies.cfsSpcs_HickoryShellbark, "cfsSpcs_HickoryShellbark",
					"Shellbark hickory", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1700, enumIntCFSTreeSpecies.cfsSpcs_Walnut, "cfsSpcs_Walnut", "Walnut",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1701, enumIntCFSTreeSpecies.cfsSpcs_Butternut, "cfsSpcs_Butternut", "Butternut",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1702, enumIntCFSTreeSpecies.cfsSpcs_WalnutBlack, "cfsSpcs_WalnutBlack", "Black walnut",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1800, enumIntCFSTreeSpecies.cfsSpcs_Alder, "cfsSpcs_Alder", "Alder",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1801, enumIntCFSTreeSpecies.cfsSpcs_AlderSitka, "cfsSpcs_AlderSitka", "Sitka alder",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1802, enumIntCFSTreeSpecies.cfsSpcs_AlderRed, "cfsSpcs_AlderRed", "Red alder",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1803, enumIntCFSTreeSpecies.cfsSpcs_AlderGreen, "cfsSpcs_AlderGreen", "Green alder",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1804, enumIntCFSTreeSpecies.cfsSpcs_AlderMountain, "cfsSpcs_AlderMountain", "Mountain alder",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1805, enumIntCFSTreeSpecies.cfsSpcs_AlderSpeckled, "cfsSpcs_AlderSpeckled", "Speckled alder",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1900, enumIntCFSTreeSpecies.cfsSpcs_IronwoodHopHornbean, "cfsSpcs_IronwoodHopHornbean",
					"Ironwood (hop-hornbeam)", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves,
					"cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					1950, enumIntCFSTreeSpecies.cfsSpcs_BlueBeech, "cfsSpcs_BlueBeech",
					"Blue-beech (American hornbeam)", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves,
					"cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2000, enumIntCFSTreeSpecies.cfsSpcs_Beech, "cfsSpcs_Beech", "Beech",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2100, enumIntCFSTreeSpecies.cfsSpcs_Oak, "cfsSpcs_Oak", "Oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2101, enumIntCFSTreeSpecies.cfsSpcs_OakWhite, "cfsSpcs_OakWhite", "White oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2102, enumIntCFSTreeSpecies.cfsSpcs_OakSwampwhite, "cfsSpcs_OakSwampwhite", "Swamp white oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2103, enumIntCFSTreeSpecies.cfsSpcs_OakGarry, "cfsSpcs_OakGarry", "Garry oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2104, enumIntCFSTreeSpecies.cfsSpcs_OakBur, "cfsSpcs_OakBur", "Bur oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2105, enumIntCFSTreeSpecies.cfsSpcs_OakPin, "cfsSpcs_OakPin", "Pin oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2106, enumIntCFSTreeSpecies.cfsSpcs_OakChinquapin, "cfsSpcs_OakChinquapin", "Chinpaquin oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2107, enumIntCFSTreeSpecies.cfsSpcs_OakChestnut, "cfsSpcs_OakChestnut", "Chestnut oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2108, enumIntCFSTreeSpecies.cfsSpcs_OakRed, "cfsSpcs_OakRed", "Red oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2109, enumIntCFSTreeSpecies.cfsSpcs_OakBlack, "cfsSpcs_OakBlack", "Black oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2110, enumIntCFSTreeSpecies.cfsSpcs_OakNorthernPin, "cfsSpcs_OakNorthernPin", "Northern pin oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2111, enumIntCFSTreeSpecies.cfsSpcs_OakShumard, "cfsSpcs_OakShumard", "Shumard oak",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2200, enumIntCFSTreeSpecies.cfsSpcs_Elm, "cfsSpcs_Elm", "Elm",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2201, enumIntCFSTreeSpecies.cfsSpcs_ElmWhite, "cfsSpcs_ElmWhite", "White elm",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2202, enumIntCFSTreeSpecies.cfsSpcs_ElmSlippery, "cfsSpcs_ElmSlippery", "Slippery elm",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2203, enumIntCFSTreeSpecies.cfsSpcs_ElmRock, "cfsSpcs_ElmRock", "Rock elm",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2300, enumIntCFSTreeSpecies.cfsSpcs_RedMulberry, "cfsSpcs_RedMulberry", "Red mulberry",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2400, enumIntCFSTreeSpecies.cfsSpcs_Tuliptree, "cfsSpcs_Tuliptree", "Tulip-tree",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2500, enumIntCFSTreeSpecies.cfsSpcs_Cucumbertree, "cfsSpcs_Cucumbertree", "Cucumber-tree",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2600, enumIntCFSTreeSpecies.cfsSpcs_Sassafras, "cfsSpcs_Sassafras", "Sassafras",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2700, enumIntCFSTreeSpecies.cfsSpcs_Sycamore, "cfsSpcs_Sycamore", "Sycamore",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2800, enumIntCFSTreeSpecies.cfsSpcs_Cherry, "cfsSpcs_Cherry", "Cherry",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2801, enumIntCFSTreeSpecies.cfsSpcs_CherryBlack, "cfsSpcs_CherryBlack", "Black cherry",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2802, enumIntCFSTreeSpecies.cfsSpcs_CherryPin, "cfsSpcs_CherryPin", "Pin cherry",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2803, enumIntCFSTreeSpecies.cfsSpcs_CherryBitter, "cfsSpcs_CherryBitter", "Bitter cherry",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2804, enumIntCFSTreeSpecies.cfsSpcs_CherryChoke, "cfsSpcs_CherryChoke", "Choke cherry",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2900, enumIntCFSTreeSpecies.cfsSpcs_Honeylocust, "cfsSpcs_Honeylocust", "Honey-locust",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					2901, enumIntCFSTreeSpecies.cfsSpcs_Blacklocust, "cfsSpcs_Blacklocust", "Black locust",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3000, enumIntCFSTreeSpecies.cfsSpcs_Basswood, "cfsSpcs_Basswood", "Basswood",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3100, enumIntCFSTreeSpecies.cfsSpcs_Blackgum, "cfsSpcs_Blackgum", "Black-gum",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3200, enumIntCFSTreeSpecies.cfsSpcs_DogwoodFlowering, "cfsSpcs_DogwoodFlowering",
					"Flowering dogwood", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3201, enumIntCFSTreeSpecies.cfsSpcs_DogwoodEasternflowering, "cfsSpcs_DogwoodEasternflowering",
					"Eastern flowering dogwood", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves,
					"cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3202, enumIntCFSTreeSpecies.cfsSpcs_DogwoodWesternflowering, "cfsSpcs_DogwoodWesternflowering",
					"Western flowering dogwood", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves,
					"cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3203, enumIntCFSTreeSpecies.cfsSpcs_DogwoodAlternateleaf, "cfsSpcs_DogwoodAlternateleaf",
					"Alternate-leaf dogwood", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3300, enumIntCFSTreeSpecies.cfsSpcs_Arbutus, "cfsSpcs_Arbutus", "Arbutus",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3400, enumIntCFSTreeSpecies.cfsSpcs_Ash, "cfsSpcs_Ash", "Ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3401, enumIntCFSTreeSpecies.cfsSpcs_AshWhite, "cfsSpcs_AshWhite", "White ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3402, enumIntCFSTreeSpecies.cfsSpcs_AshBlack, "cfsSpcs_AshBlack", "Black ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3403, enumIntCFSTreeSpecies.cfsSpcs_AshRed, "cfsSpcs_AshRed", "Red ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3404, enumIntCFSTreeSpecies.cfsSpcs_AshNorthernred, "cfsSpcs_AshNorthernred", "Northern red ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3405, enumIntCFSTreeSpecies.cfsSpcs_AshGreen, "cfsSpcs_AshGreen", "Green ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3406, enumIntCFSTreeSpecies.cfsSpcs_AshBlue, "cfsSpcs_AshBlue", "Blue ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3407, enumIntCFSTreeSpecies.cfsSpcs_AshOregon, "cfsSpcs_AshOregon", "Oregon ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3408, enumIntCFSTreeSpecies.cfsSpcs_AshPumpkin, "cfsSpcs_AshPumpkin", "Pumpkin ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3500, enumIntCFSTreeSpecies.cfsSpcs_Willow, "cfsSpcs_Willow", "Willow",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3501, enumIntCFSTreeSpecies.cfsSpcs_WillowBlack, "cfsSpcs_WillowBlack", "Black willow",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3502, enumIntCFSTreeSpecies.cfsSpcs_WillowPeachleaf, "cfsSpcs_WillowPeachleaf", "Peachleaf willow",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3503, enumIntCFSTreeSpecies.cfsSpcs_WillowPacific, "cfsSpcs_WillowPacific", "Pacific willow",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3504, enumIntCFSTreeSpecies.cfsSpcs_WillowCrack, "cfsSpcs_WillowCrack", "Crack willow",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3505, enumIntCFSTreeSpecies.cfsSpcs_WillowShining, "cfsSpcs_WillowShining", "Shining willow",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3600, enumIntCFSTreeSpecies.cfsSpcs_KentuckyCoffeeTree, "cfsSpcs_KentuckyCoffeeTree",
					"Kentucky coffee tree", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3700, enumIntCFSTreeSpecies.cfsSpcs_Hackberry, "cfsSpcs_Hackberry", "Hackberry",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3800, enumIntCFSTreeSpecies.cfsSpcs_Serviceberry, "cfsSpcs_Serviceberry", "Serviceberry",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3900, enumIntCFSTreeSpecies.cfsSpcs_BeakedHazel, "cfsSpcs_BeakedHazel", "Beaked hazel",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3910, enumIntCFSTreeSpecies.cfsSpcs_Hawthorn, "cfsSpcs_Hawthorn", "Hawthorn",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3920, enumIntCFSTreeSpecies.cfsSpcs_CommonWinterberry, "cfsSpcs_CommonWinterberry",
					"Common winterberry (black-alder)", enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves,
					"cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3930, enumIntCFSTreeSpecies.cfsSpcs_Apple, "cfsSpcs_Apple", "Apple",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3940, enumIntCFSTreeSpecies.cfsSpcs_MountainHolly, "cfsSpcs_MountainHolly", "Mountain-holly",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3950, enumIntCFSTreeSpecies.cfsSpcs_SumacStaghorn, "cfsSpcs_SumacStaghorn", "Staghorn sumac",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					3960, enumIntCFSTreeSpecies.cfsSpcs_AshMountain, "cfsSpcs_AshMountain", "Mountain ash",
					enumIntCFSTreeGenus.cfsGenus_OtherBroadleaves, "cfsGenus_OtherBroadleaves"
			),
			new Attributes(
					4000, enumIntCFSTreeSpecies.cfsSpcs_HardwoodTolerant, "cfsSpcs_HardwoodTolerant",
					"Tolerant hardwoods", enumIntCFSTreeGenus.cfsGenus_UnspecifiedBroadleaves,
					"cfsGenus_UnspecifiedBroadleaves"
			),
			new Attributes(
					5000, enumIntCFSTreeSpecies.cfsSpcs_HardwoodIntolerant, "cfsSpcs_HardwoodIntolerant",
					"Intolerant hardwoods", enumIntCFSTreeGenus.cfsGenus_UnspecifiedBroadleaves,
					"cfsGenus_UnspecifiedBroadleaves"
			)
		};
}
