package ca.bc.gov.nrs.vdyp.si32.cfs;

import java.text.MessageFormat;

import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32Enum;
import ca.bc.gov.nrs.vdyp.si32.enumerations.SI32EnumIterator;

/**
 * Enumeration of the Tree Species as defined by the Canadian Forest Service.
 * <ul>
 * <li> cfsSpcs_UNKNOWN<p>
 * Indicates an error condition or an uninitialized state. Should not be used 
 * as a place holder for an actual tree species.
 * <li> cfsSpcs_...<p>
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
	cfsSpcs_UNKNOWN(-1, -1, "Unknown Species", CfsTreeGenus.cfsGenus_UNKNOWN),

	cfsSpcs_Spruce(0, 100, "Spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceBlack(1, 101, "Black Spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceRed(2, 102, "Red Spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceNorway(3, 103, "Norway spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceEnglemann(4, 104, "Englemann spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceWhite(5, 105, "White spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceSitka(6, 106, "Sitka spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceBlackAndRed(7, 107, "Black and red spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceRedAndWhite(8, 108, "Red and white spruce", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_SpruceOther(9, 109, "Other spruce", CfsTreeGenus.cfsGenus_Spruce),
	/* 10 */
	cfsSpcs_SpruceAndBalsamFir(10, 110, "Spruce and balsam fir", CfsTreeGenus.cfsGenus_Spruce),
	cfsSpcs_Pine(11, 200, "Pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineWesternWhite(12, 201, "Western white pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineEasternWhite(13, 202, "Eastern white pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineJack(14, 203, "Jack pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineLodgepole(15, 204, "Lodgepole pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineShore(16, 205, "Shore pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineWhitebark(17, 206, "Whitebark pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineAustrian(18, 207, "Austrian pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PinePonderosa(19, 208, "Ponderosa pine", CfsTreeGenus.cfsGenus_Pine),
	/* 20 */
	cfsSpcs_PineRed(20, 209, "Red pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PinePitch(21, 210, "Pitch pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineScots(22, 211, "Scots pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineMugho(23, 212, "Mugho pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineLimber(24, 213, "Limber pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineJackLodgepoleAndShore(25, 214, "Jack, lodgepole, and shore pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineOther(26, 215, "Other pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineHybridJackLodgepole(27, 216, "Hybrid jack and lodgepole pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_PineWhitebarkAndLimber(28, 217, "Whitebark and limber pine", CfsTreeGenus.cfsGenus_Pine),
	cfsSpcs_Fir(29, 300, "Fir", CfsTreeGenus.cfsGenus_Fir),
	/* 30 */
	cfsSpcs_FirAmabilis(30, 301, "Amabilis fir", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_FirBalsam(31, 302, "Balsam fir", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_FirGrand(32, 303, "Grand fir", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_FirSubalpineOrAlpine(33, 304, "Subalpinefir (or alpine fir)", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_FirBalsamAndAlpine(34, 305, "Balsam and alpine fir", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_FirAmabilisAndGrand(35, 306, "Alpine, amabilis, and grand fir", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_FirJapanese(36, 307, "Japanese fir", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_FirSpruceAndBalsam(37, 320, "Spruce and balsam fir", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_FirBalsamAndSpruce(38, 321, "Balsam fir and spruce", CfsTreeGenus.cfsGenus_Fir),
	cfsSpcs_Hemlock(39, 400, "Hemlock", CfsTreeGenus.cfsGenus_Hemlock),
	/* 40 */
	cfsSpcs_HemlockEastern(40, 401, "Eastern hemlock", CfsTreeGenus.cfsGenus_Hemlock),
	cfsSpcs_HemlockWestern(41, 402, "Western hemlock", CfsTreeGenus.cfsGenus_Hemlock),
	cfsSpcs_HemlockMountain(42, 403, "Mountain hemlock", CfsTreeGenus.cfsGenus_Hemlock),
	cfsSpcs_HemlockWesternAndMountain(43, 404, "Western and mountain hemlock", CfsTreeGenus.cfsGenus_Hemlock),
	cfsSpcs_FirDouglasAndRockyMountain(44, 500,  "Douglas-fir and Rocky Mountain Douglas-fir", CfsTreeGenus.cfsGenus_DouglasFir),
	cfsSpcs_TamarackLarch(45, 600, "Tamarack/Larch", CfsTreeGenus.cfsGenus_Larch),
	cfsSpcs_LarchEuropean(46, 601, "European larch", CfsTreeGenus.cfsGenus_Larch),
	cfsSpcs_Tamarack(47, 602, "Tamarack", CfsTreeGenus.cfsGenus_Larch),
	cfsSpcs_LarchWestern(48, 603, "Western larch", CfsTreeGenus.cfsGenus_Larch),
	cfsSpcs_LarchSubalpine(49, 604, "Subalpine larch", CfsTreeGenus.cfsGenus_Larch),
	/* 50 */
	cfsSpcs_LarchJapanese(50, 605, "Japanese larch", CfsTreeGenus.cfsGenus_Larch),
	cfsSpcs_Cedar(51, 700, "Cedar", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_CedarEasternWhite(52, 701, "Eastern white-cedar", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_CedarWesternRed(53, 702, "Western red cedar", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_CedarAndOtherConifers(54, 703, "Cedar and other conifers", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_Juniper(55, 800, "Juniper", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_CedarEasternRed(56, 801, "Eastern red cedar", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_JuniperRockyMountain(57, 802, "Rocky Mountain juniper", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_Yew(58, 900, "Yew", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_YewWestern(59, 901, "Western yew", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	/* 60 */
	cfsSpcs_Cypress(60, 1000, "Cypress", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_CypressYellow(61, 1001, "Yellow cypress", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_OtherSoftwoodsConifers(62, 1100, "Other softwoods/other conifers", CfsTreeGenus.cfsGenus_UnspecifiedConifers),
	cfsSpcs_TamarackAndCedar(63, 1110, "Tamarack and cedar", CfsTreeGenus.cfsGenus_CedarAndOtherConifers),
	cfsSpcs_UnspecifiedSoftwood(64, 1150, "Unspecified softwood species", CfsTreeGenus.cfsGenus_UnspecifiedConifers),
	cfsSpcs_PoplarAspen(65, 1200, "Poplar/aspen", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_AspenTrembling(66, 1201, "Trembling aspen", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_PoplarEuropeanWhite(67, 1202, "European white poplar", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_BalsamPoplar(68, 1203, "Balsam poplar", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_CottonwoodBlack(69, 1204, "Black cottonwood", CfsTreeGenus.cfsGenus_Poplar),
	/* 70 */
	cfsSpcs_CottonwoodEastern(70, 1205, "Eastern cottonwood", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_AspenLargetooth(71, 1206, "Largetooth aspen", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_PoplarCarolina(72, 1207, "Carolina poplar", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_PoplarLombardy(73, 1208, "Lombardy poplar", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_PoplarHybrid(74, 1209, "Hybrid poplar", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_PoplarOther(75, 1210, "Other poplar", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_PoplarBalsamLargetoothEastern(76, 1211, "Balsam poplar, largetooth aspen and eastern cottonwood", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_PoplarBalsamBlackCottonwood(77, 1212, "Balsam poplar and black cottonwood", CfsTreeGenus.cfsGenus_Poplar),
	cfsSpcs_Birch(78, 1300, "Birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchYellow(79, 1301, "Yellow birch", CfsTreeGenus.cfsGenus_Birch),
	/* 80 */
	cfsSpcs_BirchCherry(80, 1302, "Cherry birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchWhite(81, 1303, "White birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchGray(82, 1304, "Gray birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchAlaskaPaper(83, 1305, "Alaska paper birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchMountainPaper(84, 1306, "Mountain paper birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchOther(85, 1307, "Other birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchAlaskaPaperAndWhite(86, 1308, "Alaska paper and white birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchEuropean(87, 1309, "European birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_BirchWhiteAndGray(88, 1310, "White and gray birch", CfsTreeGenus.cfsGenus_Birch),
	cfsSpcs_Maple(89, 1400, "Maple", CfsTreeGenus.cfsGenus_Maple),
	/* 90 */
	cfsSpcs_MapleSugar(90, 1401, "Sugar maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleBlack(91, 1402, "Black maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleBigleaf(92, 1403, "Bigleaf maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleManitoba(93, 1404, "Manitoba maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleRed(94, 1405, "Red maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleSilver(95, 1406, "Silver maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleNorway(96, 1407, "Norway maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleSugarAndBlack(97, 1408, "Sugar and black maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleOther(98, 1409, "Other maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleStriped(99, 1410, "Striped maple", CfsTreeGenus.cfsGenus_Maple),
	/* 100 */
	cfsSpcs_MapleMountain(100, 1411, "Mountain maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_MapleSilverAndRed(101, 1412, "Silver and red maple", CfsTreeGenus.cfsGenus_Maple),
	cfsSpcs_HardwoodOtherBroadleafOther(102, 1500,	"Other hardwoods/other broad-leaved species", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_HardwoodUnspecified(103, 1550, "Unspecified hardwood species",	CfsTreeGenus.cfsGenus_UnspecifiedBroadleaves),
	cfsSpcs_Hickory(104, 1600, "Hickory", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_HickoryBitternut(105, 1601, "Bitternut hickory", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_HickoryRed(106, 1602, "Red hickory (Pignut hickory)", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_HickoryShagbark(107, 1603, "Shagbark hickory", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_HickoryShellbark(108, 1604,	"Shellbark hickory", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Walnut(109, 1700, "Walnut", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	/* 110 */
	cfsSpcs_Butternut(110, 1701, "Butternut", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_WalnutBlack(111, 1702, "Black walnut", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Alder(112, 1800, "Alder", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AlderSitka(113, 1801, "Sitka alder", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AlderRed(114, 1802, "Red alder", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AlderGreen(115, 1803, "Green alder", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AlderMountain(116, 1804, "Mountain alder", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AlderSpeckled(117, 1805, "Speckled alder", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_IronwoodHopHornbean(118, 1900, "Ironwood (hop-hornbeam)", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_BlueBeech(119, 1950, "Blue-beech (American hornbeam)", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	/* 120 */
	cfsSpcs_Beech(120, 2000, "Beech", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Oak(121, 2100, "Oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakWhite(122, 2101, "White oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakSwampwhite(123, 2102, "Swamp white oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakGarry(124, 2103, "Garry oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakBur(125, 2104, "Bur oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakPin(126, 2105, "Pin oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakChinquapin(127, 2106, "Chinpaquin oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakChestnut(128, 2107, "Chestnut oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakRed(129, 2108, "Red oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	/* 130 */
	cfsSpcs_OakBlack(130, 2109, "Black oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakNorthernPin(131, 2110, "Northern pin oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_OakShumard(132, 2111, "Shumard oak", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Elm(133, 2200, "Elm", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_ElmWhite(134, 2201, "White elm", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_ElmSlippery(135, 2202, "Slippery elm", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_ElmRock(136, 2203, "Rock elm", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_RedMulberry(137, 2300, "Red mulberry", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Tuliptree(138, 2400, "Tulip-tree", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Cucumbertree(139, 2500, "Cucumber-tree", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	/* 140 */
	cfsSpcs_Sassafras(140, 2600, "Sassafras", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Sycamore(141, 2700, "Sycamore", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Cherry(142, 2800, "Cherry", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_CherryBlack(143, 2801, "Black cherry", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_CherryPin(144, 2802, "Pin cherry", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_CherryBitter(145, 2803, "Bitter cherry", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_CherryChoke(146, 2804, "Choke cherry", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Honeylocust(147, 2900, "Honey-locust", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Blacklocust(148, 2901, "Black locust", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Basswood(149, 3000, "Basswood", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	/* 150 */
	cfsSpcs_Blackgum(150, 3100, "Black-gum", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_DogwoodFlowering(151, 3200, "Flowering dogwood", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_DogwoodEasternflowering(152, 3201, "Eastern flowering dogwood", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_DogwoodWesternflowering(153, 3202, "Western flowering dogwood", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_DogwoodAlternateleaf(154, 3203, "Alternate-leaf dogwood", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Arbutus(155, 3300, "Arbutus", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Ash(156, 3400, "Ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AshWhite(157, 3401, "White ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AshBlack(158, 3402, "Black ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AshRed(159, 3403, "Red ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	/* 160 */
	cfsSpcs_AshNorthernred(160, 3404, "Northern red ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AshGreen(161, 3405, "Green ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AshBlue(162, 3406, "Blue ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AshOregon(163, 3407, "Oregon ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_AshPumpkin(164, 3408, "Pumpkin ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Willow(165, 3500, "Willow", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_WillowBlack(166, 3501, "Black willow", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_WillowPeachleaf(167, 3502, "Peachleaf willow", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_WillowPacific(168, 3503, "Pacific willow", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_WillowCrack(169, 3504, "Crack willow", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	/* 170 */
	cfsSpcs_WillowShining(170, 3505, "Shining willow", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_KentuckyCoffeeTree(171, 3600, "Kentucky coffee tree", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Hackberry(172, 3700, "Hackberry", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Serviceberry(173, 3800, "Serviceberry", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_BeakedHazel(174, 3900, "Beaked hazel", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Hawthorn(175, 3910, "Hawthorn", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_CommonWinterberry(176, 3920, "Common winterberry (black-alder)", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_Apple(177, 3930, "Apple", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_MountainHolly(178, 3940, "Mountain-holly", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_SumacStaghorn(179, 3950, "Staghorn sumac", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	/* 180 */
	cfsSpcs_AshMountain(180, 3960, "Mountain ash", CfsTreeGenus.cfsGenus_OtherBroadleaves),
	cfsSpcs_HardwoodTolerant(181, 4000, "Tolerant hardwoods", CfsTreeGenus.cfsGenus_UnspecifiedBroadleaves),
	cfsSpcs_HardwoodIntolerant(182, 5000, "Intolerant hardwoods", CfsTreeGenus.cfsGenus_UnspecifiedBroadleaves);

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
		if (this.equals(cfsSpcs_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return index;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsSpcs_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsSpcs_".length());
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
		return cfsSpcs_HardwoodIntolerant.index - cfsSpcs_Spruce.index + 1;
	}

	public static class Iterator extends SI32EnumIterator<CfsTreeSpecies> {
		public Iterator() {
			super(cfsSpcs_Spruce, cfsSpcs_HardwoodIntolerant, values());
		}
	}
}
