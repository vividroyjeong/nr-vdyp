package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * Enumeration of the Tree Species as defined by the Canadian Forest Service.
 * <ul>
 * <li>UNKNOWN<p>
 * Indicates an error condition or an uninitialized state. Should not be used 
 * as a place holder for an actual tree species.
 * <li>others<p>
 * </ul>
 * Species names are defined in Appendix 7 of the document 'Model_based_volume_to_biomass.pdf' 
 * found in 'Documents/CFS-Biomass'.
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
public enum CfsTreeSpecies implements SI32Enum<CfsTreeSpecies> {
	UNKNOWN(-1, -1, "Unknown Species", CfsTreeGenus.UNKNOWN),

	Spruce(0, 100, "Spruce", CfsTreeGenus.SPRUCE),
	SpruceBlack(1, 101, "Black Spruce", CfsTreeGenus.SPRUCE),
	SpruceRed(2, 102, "Red Spruce", CfsTreeGenus.SPRUCE),
	SpruceNorway(3, 103, "Norway spruce", CfsTreeGenus.SPRUCE),
	SpruceEnglemann(4, 104, "Englemann spruce", CfsTreeGenus.SPRUCE),
	SpruceWhite(5, 105, "White spruce", CfsTreeGenus.SPRUCE),
	SpruceSitka(6, 106, "Sitka spruce", CfsTreeGenus.SPRUCE),
	SpruceBlackAndRed(7, 107, "Black and red spruce", CfsTreeGenus.SPRUCE),
	SpruceRedAndWhite(8, 108, "Red and white spruce", CfsTreeGenus.SPRUCE),
	SpruceOther(9, 109, "Other spruce", CfsTreeGenus.SPRUCE),
	/* 10 */
	SpruceAndBalsamFir(10, 110, "Spruce and balsam fir", CfsTreeGenus.SPRUCE),
	Pine(11, 200, "Pine", CfsTreeGenus.PINE),
	PineWesternWhite(12, 201, "Western white pine", CfsTreeGenus.PINE),
	PineEasternWhite(13, 202, "Eastern white pine", CfsTreeGenus.PINE),
	PineJack(14, 203, "Jack pine", CfsTreeGenus.PINE),
	PineLodgepole(15, 204, "Lodgepole pine", CfsTreeGenus.PINE),
	PineShore(16, 205, "Shore pine", CfsTreeGenus.PINE),
	PineWhitebark(17, 206, "Whitebark pine", CfsTreeGenus.PINE),
	PineAustrian(18, 207, "Austrian pine", CfsTreeGenus.PINE),
	PinePonderosa(19, 208, "Ponderosa pine", CfsTreeGenus.PINE),
	/* 20 */
	PineRed(20, 209, "Red pine", CfsTreeGenus.PINE),
	PinePitch(21, 210, "Pitch pine", CfsTreeGenus.PINE),
	PineScots(22, 211, "Scots pine", CfsTreeGenus.PINE),
	PineMugho(23, 212, "Mugho pine", CfsTreeGenus.PINE),
	PineLimber(24, 213, "Limber pine", CfsTreeGenus.PINE),
	PineJackLodgepoleAndShore(25, 214, "Jack, lodgepole, and shore pine", CfsTreeGenus.PINE),
	PineOther(26, 215, "Other pine", CfsTreeGenus.PINE),
	PineHybridJackLodgepole(27, 216, "Hybrid jack and lodgepole pine", CfsTreeGenus.PINE),
	PineWhitebarkAndLimber(28, 217, "Whitebark and limber pine", CfsTreeGenus.PINE),
	Fir(29, 300, "Fir", CfsTreeGenus.FIR),
	/* 30 */
	FirAmabilis(30, 301, "Amabilis fir", CfsTreeGenus.FIR),
	FirBalsam(31, 302, "Balsam fir", CfsTreeGenus.FIR),
	FirGrand(32, 303, "Grand fir", CfsTreeGenus.FIR),
	FirSubalpineOrAlpine(33, 304, "Subalpinefir (or alpine fir)", CfsTreeGenus.FIR),
	FirBalsamAndAlpine(34, 305, "Balsam and alpine fir", CfsTreeGenus.FIR),
	FirAmabilisAndGrand(35, 306, "Alpine, amabilis, and grand fir", CfsTreeGenus.FIR),
	FirJapanese(36, 307, "Japanese fir", CfsTreeGenus.FIR),
	FirSpruceAndBalsam(37, 320, "Spruce and balsam fir", CfsTreeGenus.FIR),
	FirBalsamAndSpruce(38, 321, "Balsam fir and spruce", CfsTreeGenus.FIR),
	Hemlock(39, 400, "Hemlock", CfsTreeGenus.HEMLOCK),
	/* 40 */
	HemlockEastern(40, 401, "Eastern hemlock", CfsTreeGenus.HEMLOCK),
	HemlockWestern(41, 402, "Western hemlock", CfsTreeGenus.HEMLOCK),
	HemlockMountain(42, 403, "Mountain hemlock", CfsTreeGenus.HEMLOCK),
	HemlockWesternAndMountain(43, 404, "Western and mountain hemlock", CfsTreeGenus.HEMLOCK),
	FirDouglasAndRockyMountain(44, 500,  "Douglas-fir and Rocky Mountain Douglas-fir", CfsTreeGenus.DOUGLAS_FIR),
	TamarackLarch(45, 600, "Tamarack/Larch", CfsTreeGenus.LARCH),
	LarchEuropean(46, 601, "European larch", CfsTreeGenus.LARCH),
	Tamarack(47, 602, "Tamarack", CfsTreeGenus.LARCH),
	LarchWestern(48, 603, "Western larch", CfsTreeGenus.LARCH),
	LarchSubalpine(49, 604, "Subalpine larch", CfsTreeGenus.LARCH),
	/* 50 */
	LarchJapanese(50, 605, "Japanese larch", CfsTreeGenus.LARCH),
	Cedar(51, 700, "Cedar", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	CedarEasternWhite(52, 701, "Eastern white-cedar", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	CedarWesternRed(53, 702, "Western red cedar", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	CedarAndOtherConifers(54, 703, "Cedar and other conifers", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	Juniper(55, 800, "Juniper", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	CedarEasternRed(56, 801, "Eastern red cedar", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	JuniperRockyMountain(57, 802, "Rocky Mountain juniper", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	Yew(58, 900, "Yew", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	YewWestern(59, 901, "Western yew", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	/* 60 */
	Cypress(60, 1000, "Cypress", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	CypressYellow(61, 1001, "Yellow cypress", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	OtherSoftwoodsConifers(62, 1100, "Other softwoods/other conifers", CfsTreeGenus.UNSPECIFIED_CONIFERS),
	TamarackAndCedar(63, 1110, "Tamarack and cedar", CfsTreeGenus.CEDAR_AND_OTHER_CONIFERS),
	UnspecifiedSoftwood(64, 1150, "Unspecified softwood species", CfsTreeGenus.UNSPECIFIED_CONIFERS),
	PoplarAspen(65, 1200, "Poplar/aspen", CfsTreeGenus.POPLAR),
	AspenTrembling(66, 1201, "Trembling aspen", CfsTreeGenus.POPLAR),
	PoplarEuropeanWhite(67, 1202, "European white poplar", CfsTreeGenus.POPLAR),
	BalsamPoplar(68, 1203, "Balsam poplar", CfsTreeGenus.POPLAR),
	CottonwoodBlack(69, 1204, "Black cottonwood", CfsTreeGenus.POPLAR),
	/* 70 */
	CottonwoodEastern(70, 1205, "Eastern cottonwood", CfsTreeGenus.POPLAR),
	AspenLargetooth(71, 1206, "Largetooth aspen", CfsTreeGenus.POPLAR),
	PoplarCarolina(72, 1207, "Carolina poplar", CfsTreeGenus.POPLAR),
	PoplarLombardy(73, 1208, "Lombardy poplar", CfsTreeGenus.POPLAR),
	PoplarHybrid(74, 1209, "Hybrid poplar", CfsTreeGenus.POPLAR),
	PoplarOther(75, 1210, "Other poplar", CfsTreeGenus.POPLAR),
	PoplarBalsamLargetoothEastern(76, 1211, "Balsam poplar, largetooth aspen and eastern cottonwood", CfsTreeGenus.POPLAR),
	PoplarBalsamBlackCottonwood(77, 1212, "Balsam poplar and black cottonwood", CfsTreeGenus.POPLAR),
	Birch(78, 1300, "Birch", CfsTreeGenus.BIRCH),
	BirchYellow(79, 1301, "Yellow birch", CfsTreeGenus.BIRCH),
	/* 80 */
	BirchCherry(80, 1302, "Cherry birch", CfsTreeGenus.BIRCH),
	BirchWhite(81, 1303, "White birch", CfsTreeGenus.BIRCH),
	BirchGray(82, 1304, "Gray birch", CfsTreeGenus.BIRCH),
	BirchAlaskaPaper(83, 1305, "Alaska paper birch", CfsTreeGenus.BIRCH),
	BirchMountainPaper(84, 1306, "Mountain paper birch", CfsTreeGenus.BIRCH),
	BirchOther(85, 1307, "Other birch", CfsTreeGenus.BIRCH),
	BirchAlaskaPaperAndWhite(86, 1308, "Alaska paper and white birch", CfsTreeGenus.BIRCH),
	BirchEuropean(87, 1309, "European birch", CfsTreeGenus.BIRCH),
	BirchWhiteAndGray(88, 1310, "White and gray birch", CfsTreeGenus.BIRCH),
	Maple(89, 1400, "Maple", CfsTreeGenus.MAPLE),
	/* 90 */
	MapleSugar(90, 1401, "Sugar maple", CfsTreeGenus.MAPLE),
	MapleBlack(91, 1402, "Black maple", CfsTreeGenus.MAPLE),
	MapleBigleaf(92, 1403, "Bigleaf maple", CfsTreeGenus.MAPLE),
	MapleManitoba(93, 1404, "Manitoba maple", CfsTreeGenus.MAPLE),
	MapleRed(94, 1405, "Red maple", CfsTreeGenus.MAPLE),
	MapleSilver(95, 1406, "Silver maple", CfsTreeGenus.MAPLE),
	MapleNorway(96, 1407, "Norway maple", CfsTreeGenus.MAPLE),
	MapleSugarAndBlack(97, 1408, "Sugar and black maple", CfsTreeGenus.MAPLE),
	MapleOther(98, 1409, "Other maple", CfsTreeGenus.MAPLE),
	MapleStriped(99, 1410, "Striped maple", CfsTreeGenus.MAPLE),
	/* 100 */
	MapleMountain(100, 1411, "Mountain maple", CfsTreeGenus.MAPLE),
	MapleSilverAndRed(101, 1412, "Silver and red maple", CfsTreeGenus.MAPLE),
	HardwoodOtherBroadleafOther(102, 1500,	"Other hardwoods/other broad-leaved species", CfsTreeGenus.OTHER_BROAD_LEAVES),
	HardwoodUnspecified(103, 1550, "Unspecified hardwood species",	CfsTreeGenus.UNSPECIFIED_BROAD_LEAVES),
	Hickory(104, 1600, "Hickory", CfsTreeGenus.OTHER_BROAD_LEAVES),
	HickoryBitternut(105, 1601, "Bitternut hickory", CfsTreeGenus.OTHER_BROAD_LEAVES),
	HickoryRed(106, 1602, "Red hickory (Pignut hickory)", CfsTreeGenus.OTHER_BROAD_LEAVES),
	HickoryShagbark(107, 1603, "Shagbark hickory", CfsTreeGenus.OTHER_BROAD_LEAVES),
	HickoryShellbark(108, 1604,	"Shellbark hickory", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Walnut(109, 1700, "Walnut", CfsTreeGenus.OTHER_BROAD_LEAVES),
	/* 110 */
	Butternut(110, 1701, "Butternut", CfsTreeGenus.OTHER_BROAD_LEAVES),
	WalnutBlack(111, 1702, "Black walnut", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Alder(112, 1800, "Alder", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AlderSitka(113, 1801, "Sitka alder", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AlderRed(114, 1802, "Red alder", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AlderGreen(115, 1803, "Green alder", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AlderMountain(116, 1804, "Mountain alder", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AlderSpeckled(117, 1805, "Speckled alder", CfsTreeGenus.OTHER_BROAD_LEAVES),
	IronwoodHopHornbean(118, 1900, "Ironwood (hop-hornbeam)", CfsTreeGenus.OTHER_BROAD_LEAVES),
	BlueBeech(119, 1950, "Blue-beech (American hornbeam)", CfsTreeGenus.OTHER_BROAD_LEAVES),
	/* 120 */
	Beech(120, 2000, "Beech", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Oak(121, 2100, "Oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakWhite(122, 2101, "White oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakSwampwhite(123, 2102, "Swamp white oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakGarry(124, 2103, "Garry oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakBur(125, 2104, "Bur oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakPin(126, 2105, "Pin oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakChinquapin(127, 2106, "Chinpaquin oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakChestnut(128, 2107, "Chestnut oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakRed(129, 2108, "Red oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	/* 130 */
	OakBlack(130, 2109, "Black oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakNorthernPin(131, 2110, "Northern pin oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	OakShumard(132, 2111, "Shumard oak", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Elm(133, 2200, "Elm", CfsTreeGenus.OTHER_BROAD_LEAVES),
	ElmWhite(134, 2201, "White elm", CfsTreeGenus.OTHER_BROAD_LEAVES),
	ElmSlippery(135, 2202, "Slippery elm", CfsTreeGenus.OTHER_BROAD_LEAVES),
	ElmRock(136, 2203, "Rock elm", CfsTreeGenus.OTHER_BROAD_LEAVES),
	RedMulberry(137, 2300, "Red mulberry", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Tuliptree(138, 2400, "Tulip-tree", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Cucumbertree(139, 2500, "Cucumber-tree", CfsTreeGenus.OTHER_BROAD_LEAVES),
	/* 140 */
	Sassafras(140, 2600, "Sassafras", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Sycamore(141, 2700, "Sycamore", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Cherry(142, 2800, "Cherry", CfsTreeGenus.OTHER_BROAD_LEAVES),
	CherryBlack(143, 2801, "Black cherry", CfsTreeGenus.OTHER_BROAD_LEAVES),
	CherryPin(144, 2802, "Pin cherry", CfsTreeGenus.OTHER_BROAD_LEAVES),
	CherryBitter(145, 2803, "Bitter cherry", CfsTreeGenus.OTHER_BROAD_LEAVES),
	CherryChoke(146, 2804, "Choke cherry", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Honeylocust(147, 2900, "Honey-locust", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Blacklocust(148, 2901, "Black locust", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Basswood(149, 3000, "Basswood", CfsTreeGenus.OTHER_BROAD_LEAVES),
	/* 150 */
	Blackgum(150, 3100, "Black-gum", CfsTreeGenus.OTHER_BROAD_LEAVES),
	DogwoodFlowering(151, 3200, "Flowering dogwood", CfsTreeGenus.OTHER_BROAD_LEAVES),
	DogwoodEasternflowering(152, 3201, "Eastern flowering dogwood", CfsTreeGenus.OTHER_BROAD_LEAVES),
	DogwoodWesternflowering(153, 3202, "Western flowering dogwood", CfsTreeGenus.OTHER_BROAD_LEAVES),
	DogwoodAlternateleaf(154, 3203, "Alternate-leaf dogwood", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Arbutus(155, 3300, "Arbutus", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Ash(156, 3400, "Ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AshWhite(157, 3401, "White ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AshBlack(158, 3402, "Black ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AshRed(159, 3403, "Red ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	/* 160 */
	AshNorthernred(160, 3404, "Northern red ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AshGreen(161, 3405, "Green ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AshBlue(162, 3406, "Blue ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AshOregon(163, 3407, "Oregon ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	AshPumpkin(164, 3408, "Pumpkin ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Willow(165, 3500, "Willow", CfsTreeGenus.OTHER_BROAD_LEAVES),
	WillowBlack(166, 3501, "Black willow", CfsTreeGenus.OTHER_BROAD_LEAVES),
	WillowPeachleaf(167, 3502, "Peachleaf willow", CfsTreeGenus.OTHER_BROAD_LEAVES),
	WillowPacific(168, 3503, "Pacific willow", CfsTreeGenus.OTHER_BROAD_LEAVES),
	WillowCrack(169, 3504, "Crack willow", CfsTreeGenus.OTHER_BROAD_LEAVES),
	/* 170 */
	WillowShining(170, 3505, "Shining willow", CfsTreeGenus.OTHER_BROAD_LEAVES),
	KentuckyCoffeeTree(171, 3600, "Kentucky coffee tree", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Hackberry(172, 3700, "Hackberry", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Serviceberry(173, 3800, "Serviceberry", CfsTreeGenus.OTHER_BROAD_LEAVES),
	BeakedHazel(174, 3900, "Beaked hazel", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Hawthorn(175, 3910, "Hawthorn", CfsTreeGenus.OTHER_BROAD_LEAVES),
	CommonWinterberry(176, 3920, "Common winterberry (black-alder)", CfsTreeGenus.OTHER_BROAD_LEAVES),
	Apple(177, 3930, "Apple", CfsTreeGenus.OTHER_BROAD_LEAVES),
	MountainHolly(178, 3940, "Mountain-holly", CfsTreeGenus.OTHER_BROAD_LEAVES),
	SumacStaghorn(179, 3950, "Staghorn sumac", CfsTreeGenus.OTHER_BROAD_LEAVES),
	/* 180 */
	AshMountain(180, 3960, "Mountain ash", CfsTreeGenus.OTHER_BROAD_LEAVES),
	HardwoodTolerant(181, 4000, "Tolerant hardwoods", CfsTreeGenus.UNSPECIFIED_BROAD_LEAVES),
	HardwoodIntolerant(182, 5000, "Intolerant hardwoods", CfsTreeGenus.UNSPECIFIED_BROAD_LEAVES);

	private final int index;
	private final int cfsSpeciesNumber;
	private final String cfsSpeciesName;
	private final CfsTreeGenus cfsTreeGenus;
	
	private CfsTreeSpecies(int index, int cfsSpeciesNumber, String cfsSpeciesName, CfsTreeGenus cfsTreeGenus) {
		this.index = index;
		this.cfsSpeciesNumber = cfsSpeciesNumber;
		this.cfsSpeciesName = cfsSpeciesName;
		this.cfsTreeGenus = cfsTreeGenus;
	}

	@Override
	public int getIndex() {
		return index;
	}
	
	public int getCfsSpeciesNumber() {
		return cfsSpeciesNumber;
	}

	public String getCfsSpeciesName() {
		return cfsSpeciesName;
	}

	public CfsTreeGenus getCfsTreeGenus() {
		return cfsTreeGenus;
	}

	@Override
	public int getOffset() {
		if (this.equals(UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {0} as it's not a standard member of the enumeration", this));
		}
		
		return index;
	}
	
	@Override
	public String getText() {
		if (this.equals(UNKNOWN)) {
			return "";
		}
		
		return this.toString();
	}

	/**
	 * Returns the enumeration constant with the given index.
	 * @param index the value in question
	 * @return the enumeration value, unless no enumeration constant has the given 
	 * 	   <code>index</code> in which case <code>null</code> is returned.
	 */
	public static CfsTreeSpecies forIndex(int index) {
		for (CfsTreeSpecies e: CfsTreeSpecies.values()) {
			if (index == e.index)
				return e;
		}
		
		return null;
	}

	/**
	 * @return the number of non-housekeeping entries in the enumeration
	 */
	public static int size() {
		return HardwoodIntolerant.index - Spruce.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<CfsTreeSpecies> {
		public Iterator() {
			super(Spruce, HardwoodIntolerant, values());
		}
	}
}
