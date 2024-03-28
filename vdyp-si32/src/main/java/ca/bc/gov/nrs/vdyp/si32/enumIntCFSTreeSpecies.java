package ca.bc.gov.nrs.vdyp.si32;

import java.text.MessageFormat;

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
public enum enumIntCFSTreeSpecies implements SI32Enum<enumIntCFSTreeSpecies> {
	cfsSpcs_UNKNOWN(-1),

	cfsSpcs_Spruce(0), // 100
	cfsSpcs_SpruceBlack(1), // 101
	cfsSpcs_SpruceRed(2), // 102
	cfsSpcs_SpruceNorway(3), // 103
	cfsSpcs_SpruceEnglemann(4), // 104
	cfsSpcs_SpruceWhite(5), // 105
	cfsSpcs_SpruceSitka(6), // 106
	cfsSpcs_SpruceBlackAndRed(7), // 107
	cfsSpcs_SpruceRedAndWhite(8), // 108
	cfsSpcs_SpruceOther(9), // 109
	/* 10 */
	cfsSpcs_SpruceAndBalsamFir(10), // 110
	cfsSpcs_Pine(11), // 200
	cfsSpcs_PineWesternWhite(12), // 201
	cfsSpcs_PineEasternWhite(13), // 202
	cfsSpcs_PineJack(14), // 203
	cfsSpcs_PineLodgepole(15), // 204
	cfsSpcs_PineShore(16), // 205
	cfsSpcs_PineWhitebark(17), // 206
	cfsSpcs_PineAustrian(18), // 207
	cfsSpcs_PinePonderosa(19), // 208
	/* 20 */
	cfsSpcs_PineRed(20), // 209
	cfsSpcs_PinePitch(21), // 210
	cfsSpcs_PineScots(22), // 211
	cfsSpcs_PineMugho(23), // 212
	cfsSpcs_PineLimber(24), // 213
	cfsSpcs_PineJackLodgepoleAndShore(25), // 214
	cfsSpcs_PineOther(26), // 215
	cfsSpcs_PineHybridJackLodgepole(27), // 216
	cfsSpcs_PineWhitebarkAndLimber(28), // 217
	cfsSpcs_Fir(29), // 300
	/* 30 */
	cfsSpcs_FirAmabilis(30), // 301
	cfsSpcs_FirBalsam(31), // 302
	cfsSpcs_FirGrand(32), // 303
	cfsSpcs_FirSubalpineOrAlpine(33), // 304
	cfsSpcs_FirBalsamAndAlpine(34), // 305
	cfsSpcs_FirAmabilisAndGrand(35), // 306
	cfsSpcs_FirJapanese(36), // 307
	cfsSpcs_FirSpruceAndBalsam(37), // 320
	cfsSpcs_FirBalsamAndSpruce(38), // 321
	cfsSpcs_Hemlock(39), // 400
	/* 40 */
	cfsSpcs_HemlockEastern(40), // 401
	cfsSpcs_HemlockWestern(41), // 402
	cfsSpcs_HemlockMountain(42), // 403
	cfsSpcs_HemlockWesternAndMountain(43), // 404
	cfsSpcs_FirDouglasAndRockyMountain(44), // 500
	cfsSpcs_TamarackLarch(45), // 600
	cfsSpcs_LarchEuropean(46), // 601
	cfsSpcs_Tamarack(47), // 602
	cfsSpcs_LarchWestern(48), // 603
	cfsSpcs_LarchSubalpine(49), // 604
	/* 50 */
	cfsSpcs_LarchJapanese(50), // 605
	cfsSpcs_Cedar(51), // 700
	cfsSpcs_CedarEasternWhite(52), // 701
	cfsSpcs_CedarWesternRed(53), // 702
	cfsSpcs_CedarAndOtherConifers(54), // 703
	cfsSpcs_Juniper(55), // 800
	cfsSpcs_CedarEasternRed(56), // 801
	cfsSpcs_JuniperRockyMountain(57), // 802
	cfsSpcs_Yew(58), // 900
	cfsSpcs_YewWestern(59), // 901
	/* 60 */
	cfsSpcs_Cypress(60), // 1000
	cfsSpcs_CypressYellow(61), // 1001
	cfsSpcs_OtherSoftwoodsConifers(62), // 1100
	cfsSpcs_TamarackAndCedar(63), // 1110
	cfsSpcs_UnspecifiedSoftwood(64), // 1150
	cfsSpcs_PoplarAspen(65), // 1200
	cfsSpcs_AspenTrembling(66), // 1201
	cfsSpcs_PoplarEuropeanWhite(67), // 1202
	cfsSpcs_BalsamPoplar(68), // 1203
	cfsSpcs_CottonwoodBlack(69), // 1204
	/* 70 */
	cfsSpcs_CottonwoodEastern(70), // 1205
	cfsSpcs_AspenLargetooth(71), // 1206
	cfsSpcs_PoplarCarolina(72), // 1207
	cfsSpcs_PoplarLombardy(73), // 1208
	cfsSpcs_PoplarHybrid(74), // 1209
	cfsSpcs_PoplarOther(75), // 1210
	cfsSpcs_PoplarBalsamLargetoothEastern(76), // 1211
	cfsSpcs_PoplarBalsamBlackCottonwood(77), // 1212
	cfsSpcs_Birch(78), // 1300
	cfsSpcs_BirchYellow(79), // 1301
	/* 80 */
	cfsSpcs_BirchCherry(80), // 1302
	cfsSpcs_BirchWhite(81), // 1303
	cfsSpcs_BirchGray(82), // 1304
	cfsSpcs_BirchAlaskaPaper(83), // 1305
	cfsSpcs_BirchMountainPaper(84), // 1306
	cfsSpcs_BirchOther(85), // 1307
	cfsSpcs_BirchAlaskaPaperAndWhite(86), // 1308
	cfsSpcs_BirchEuropean(87), // 1309
	cfsSpcs_BirchWhiteAndGray(88), // 1310
	cfsSpcs_Maple(89), // 1400
	/* 90 */
	cfsSpcs_MapleSugar(90), // 1401
	cfsSpcs_MapleBlack(91), // 1402
	cfsSpcs_MapleBigleaf(92), // 1403
	cfsSpcs_MapleManitoba(93), // 1404
	cfsSpcs_MapleRed(94), // 1405
	cfsSpcs_MapleSilver(95), // 1406
	cfsSpcs_MapleNorway(96), // 1407
	cfsSpcs_MapleSugarAndBlack(97), // 1408
	cfsSpcs_MapleOther(98), // 1409
	cfsSpcs_MapleStriped(99), // 1410
	/* 100 */
	cfsSpcs_MapleMountain(100), // 1411
	cfsSpcs_MapleSilverAndRed(101), // 1412
	cfsSpcs_HardwoodOtherBroadleafOther(102), // 1500
	cfsSpcs_HardwoodUnspecified(103), // 1550
	cfsSpcs_Hickory(104), // 1600
	cfsSpcs_HickoryBitternut(105), // 1601
	cfsSpcs_HickoryRed(106), // 1602
	cfsSpcs_HickoryShagbark(107), // 1603
	cfsSpcs_HickoryShellbark(108), // 1604
	cfsSpcs_Walnut(109), // 1700
	/* 110 */
	cfsSpcs_Butternut(110), // 1701
	cfsSpcs_WalnutBlack(111), // 1702
	cfsSpcs_Alder(112), // 1800
	cfsSpcs_AlderSitka(113), // 1801
	cfsSpcs_AlderRed(114), // 1802
	cfsSpcs_AlderGreen(115), // 1803
	cfsSpcs_AlderMountain(116), // 1804
	cfsSpcs_AlderSpeckled(117), // 1805
	cfsSpcs_IronwoodHopHornbean(118), // 1900
	cfsSpcs_BlueBeech(119), // 1950
	/* 120 */
	cfsSpcs_Beech(120), // 2000
	cfsSpcs_Oak(121), // 2100
	cfsSpcs_OakWhite(122), // 2101
	cfsSpcs_OakSwampwhite(123), // 2102
	cfsSpcs_OakGarry(124), // 2103
	cfsSpcs_OakBur(125), // 2104
	cfsSpcs_OakPin(126), // 2105
	cfsSpcs_OakChinquapin(127), // 2106
	cfsSpcs_OakChestnut(128), // 2107
	cfsSpcs_OakRed(129), // 2108
	/* 130 */
	cfsSpcs_OakBlack(130), // 2109
	cfsSpcs_OakNorthernPin(131), // 2110
	cfsSpcs_OakShumard(132), // 2111
	cfsSpcs_Elm(133), // 2200
	cfsSpcs_ElmWhite(134), // 2201
	cfsSpcs_ElmSlippery(135), // 2202
	cfsSpcs_ElmRock(136), // 2203
	cfsSpcs_RedMulberry(137), // 2300
	cfsSpcs_Tuliptree(138), // 2400
	cfsSpcs_Cucumbertree(139), // 2500
	/* 140 */
	cfsSpcs_Sassafras(140), // 2600
	cfsSpcs_Sycamore(141), // 2700
	cfsSpcs_Cherry(142), // 2800
	cfsSpcs_CherryBlack(143), // 2801
	cfsSpcs_CherryPin(144), // 2802
	cfsSpcs_CherryBitter(145), // 2803
	cfsSpcs_CherryChoke(146), // 2804
	cfsSpcs_Honeylocust(147), // 2900
	cfsSpcs_Blacklocust(148), // 2901
	cfsSpcs_Basswood(149), // 3000
	/* 150 */
	cfsSpcs_Blackgum(150), // 3100
	cfsSpcs_DogwoodFlowering(151), // 3200
	cfsSpcs_DogwoodEasternflowering(152), // 3201
	cfsSpcs_DogwoodWesternflowering(153), // 3202
	cfsSpcs_DogwoodAlternateleaf(154), // 3203
	cfsSpcs_Arbutus(155), // 3300
	cfsSpcs_Ash(156), // 3400
	cfsSpcs_AshWhite(157), // 3401
	cfsSpcs_AshBlack(158), // 3402
	cfsSpcs_AshRed(159), // 3403
	/* 160 */
	cfsSpcs_AshNorthernred(160), // 3404
	cfsSpcs_AshGreen(161), // 3405
	cfsSpcs_AshBlue(162), // 3406
	cfsSpcs_AshOregon(163), // 3407
	cfsSpcs_AshPumpkin(164), // 3408
	cfsSpcs_Willow(165), // 3500
	cfsSpcs_WillowBlack(166), // 3501
	cfsSpcs_WillowPeachleaf(167), // 3502
	cfsSpcs_WillowPacific(168), // 3503
	cfsSpcs_WillowCrack(169), // 3504
	/* 170 */
	cfsSpcs_WillowShining(170), // 3505
	cfsSpcs_KentuckyCoffeeTree(171), // 3600
	cfsSpcs_Hackberry(172), // 3700
	cfsSpcs_Serviceberry(173), // 3800
	cfsSpcs_BeakedHazel(174), // 3900
	cfsSpcs_Hawthorn(175), // 3910
	cfsSpcs_CommonWinterberry(176), // 3920
	cfsSpcs_Apple(177), // 3930
	cfsSpcs_MountainHolly(178), // 3940
	cfsSpcs_SumacStaghorn(179), // 3950
	/* 180 */
	cfsSpcs_AshMountain(180), // 3960
	cfsSpcs_HardwoodTolerant(181), // 4000
	cfsSpcs_HardwoodIntolerant(182); // 5000

	private int intValue;
	private static java.util.HashMap<Integer, enumIntCFSTreeSpecies> mappings;

	private static java.util.HashMap<Integer, enumIntCFSTreeSpecies> getMappings() {
		if (mappings == null) {
			synchronized (enumIntCFSTreeSpecies.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, enumIntCFSTreeSpecies>();
				}
			}
		}
		return mappings;
	}

	private enumIntCFSTreeSpecies(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		if (this.equals(cfsSpcs_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue;
	}
	
	@Override
	public String getText() {
		if (this.equals(cfsSpcs_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("cfsSpcs_".length());
	}

	public static enumIntCFSTreeSpecies forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return cfsSpcs_HardwoodIntolerant.intValue - cfsSpcs_Spruce.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<enumIntCFSTreeSpecies> {
		public Iterator() {
			super(cfsSpcs_Spruce, cfsSpcs_HardwoodIntolerant, mappings);
		}
	}
}
