package ca.bc.gov.nrs.vdyp.si32.enumerations;

import java.text.MessageFormat;

/**
 * An enumeration holding each of the valid SP0 Species known to VDYP7.
 * <p>
 * <b>sp64_UNKNOWN</b> represents an error condition or an uninitialized state. 
 * This value should not be used as a species constant.
 */
public enum SP64Name implements SI32Enum<SP64Name> {
	sp64_UNKNOWN(0),

	sp64_A(1), //
	sp64_ABAL(2), //
	sp64_ABCO(3), //
	sp64_AC(4), //
	sp64_ACB(5), //
	sp64_ACT(6), //
	sp64_AD(7), //
	sp64_AH(8), //
	sp64_AT(9), //
	sp64_AX(10), //
	sp64_B(11), //
	sp64_BA(12), //
	sp64_BAC(13), //
	sp64_BAI(14), //
	sp64_BB(15), //
	sp64_BC(16), //
	sp64_BG(17), //
	sp64_BI(18), //
	sp64_BL(19), //
	sp64_BM(20),
	sp64_BN(21), //
	sp64_BP(22), //
	sp64_BV(23), //
	sp64_C(24), //
	sp64_CI(25), //
	sp64_COT(26), //
	sp64_CP(27), //
	sp64_CT(28), //
	sp64_CW(29), //
	sp64_CY(30), //
	sp64_D(31), //
	sp64_DF(32), //
	sp64_DG(33), //
	sp64_DM(34), //
	sp64_DR(35), //
	sp64_E(36), //
	sp64_EA(37), //
	sp64_EB(38), //
	sp64_EE(39), //
	sp64_EP(40), //
	sp64_ES(41), //
	sp64_EW(42), //
	sp64_EXP(43), //
	sp64_F(44), //
	sp64_FD(45), //
	sp64_FDC(46), //
	sp64_FDI(47), //
	sp64_G(48), //
	sp64_GP(49), //
	sp64_GR(50), //
	sp64_H(51), //
	sp64_HM(52), //
	sp64_HW(53), //
	sp64_HWC(54), //
	sp64_HWI(55), //
	sp64_HXM(56), //
	sp64_IG(57), //
	sp64_IS(58), //
	sp64_J(59), //
	sp64_JR(60), //
	sp64_K(61), //
	sp64_KC(62), //
	sp64_L(63), //
	sp64_LA(64), //
	sp64_LE(65), //
	sp64_LT(66), //
	sp64_LW(67), //
	sp64_M(68), //
	sp64_MB(69), //
	sp64_ME(70), //
	sp64_MN(71), //
	sp64_MR(72), //
	sp64_MS(73), //
	sp64_MV(74), //
	sp64_OA(75), //
	sp64_OB(76), //
	sp64_OC(77), //
	sp64_OD(78), //
	sp64_OE(79), //
	sp64_OF(80), //
	sp64_OG(81), //
	sp64_P(82), //
	sp64_PA(83), //
	sp64_PF(84), //
	sp64_PJ(85), //
	sp64_PL(86), //
	sp64_PLC(87), //
	sp64_PLI(88), //
	sp64_PM(89), //
	sp64_PR(90), //
	sp64_PS(91), //
	sp64_PV(92), //
	sp64_PW(93), //
	sp64_PXJ(94), //
	sp64_PY(95), //
	sp64_Q(96), //
	sp64_QE(97), //
	sp64_QG(98), //
	sp64_R(99), //
	sp64_RA(100), //
	sp64_S(101), //
	sp64_SA(102), //
	sp64_SB(103), //
	sp64_SE(104), //
	sp64_SI(105), //
	sp64_SN(106), //
	sp64_SS(107), //
	sp64_SW(108), //
	sp64_SX(109), //
	sp64_SXB(110), //
	sp64_SXE(111), //
	sp64_SXL(112), //
	sp64_SXS(113), //
	sp64_SXW(114), //
	sp64_SXX(115), //
	sp64_T(116), //
	sp64_TW(117), //
	sp64_U(118), //
	sp64_UA(119), //
	sp64_UP(120), //
	sp64_V(121), //
	sp64_VB(122), //
	sp64_VP(123), //
	sp64_VS(124), //
	sp64_VV(125), //
	sp64_W(126), //
	sp64_WA(127), //
	sp64_WB(128), //
	sp64_WD(129), //
	sp64_WI(130), //
	sp64_WP(131), //
	sp64_WS(132), //
	sp64_WT(133), //
	sp64_X(134), //
	sp64_XC(135), //
	sp64_XH(136), //
	sp64_Y(137), //
	sp64_YC(138), //
	sp64_YP(139), //
	sp64_Z(140), //
	sp64_ZC(141), //
	sp64_ZH(142);

	private int intValue;
	private static java.util.HashMap<Integer, SP64Name> mappings;

	private static java.util.HashMap<Integer, SP64Name> getMappings() {
		if (mappings == null) {
			synchronized (SP64Name.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, SP64Name>();
				}
			}
		}
		return mappings;
	}

	private SP64Name(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		if (this.equals(sp64_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat.format("Cannot call getIndex on {} as it's not a standard member of the enumeration", this));
		}
		
		return intValue - 1;
	}
	
	@Override
	public String getText() {
		if (this.equals(sp64_UNKNOWN)) {
			throw new UnsupportedOperationException(MessageFormat
					.format("Cannot call getText on {} as it's not a standard member of the enumeration", this));
		}
		
		return this.toString().substring("sp64_".length());
	}

	/**
	 * Return the enumeration value corresponding to the given text. If none
	 * exists, {@code sp64_UNKNOWN} is returned.
	 * @param text the text value == the enumeration text minus the "sp64_" prefix.
	 * @return as described
	 */
	public static SP64Name forText(String text) {
		
		if (text != null) {
			try {
				return SP64Name.valueOf(SP64Name.class, "sp64_" + text.toUpperCase());
			} catch (IllegalArgumentException e) {
				return sp64_UNKNOWN;
			}
		} else {
			return sp64_UNKNOWN;
		}
	}

	public static SP64Name forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return sp64_ZH.intValue - sp64_A.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<SP64Name> {
		public Iterator() {
			super(sp64_A, sp64_ZH, mappings);
		}
	}
}
