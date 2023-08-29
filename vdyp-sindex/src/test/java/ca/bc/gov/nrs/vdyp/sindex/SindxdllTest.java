package ca.bc.gov.nrs.vdyp.sindex;

import ca.bc.gov.nrs.vdyp.common_calculators.Age2Age;
import ca.bc.gov.nrs.vdyp.common_calculators.Height2SiteIndex;
import ca.bc.gov.nrs.vdyp.common_calculators.SiteIndex2Age;
import ca.bc.gov.nrs.vdyp.common_calculators.SiteIndexYears2BreastHeight;
import ca.bc.gov.nrs.vdyp.common_calculators.custom_exceptions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.closeTo;
import org.junit.jupiter.api.*;

class SindxdllTest {
	/*
	 * establishment types
	 */
	private static final int SI_ESTAB_NAT = 0;
	private static final int SI_ESTAB_PLA = 1;

	/*
	 * site index estimation (from height and age) types
	 */
// private static final int SI_EST_ITERATE = 0; Unused
// private static final int SI_EST_DIRECT  = 1; Unused

	/*
	 * error codes as return values from functions
	 */
	private static final int SI_ERR_LT13 = -1;
	private static final int SI_ERR_GI_MIN = -2;
	private static final int SI_ERR_GI_MAX = -3;
	private static final int SI_ERR_NO_ANS = -4;
	private static final int SI_ERR_CURVE = -5;
	private static final int SI_ERR_CLASS = -6;
	private static final int SI_ERR_FIZ = -7;
	private static final int SI_ERR_CODE = -8;
	private static final int SI_ERR_GI_TOT = -9;
	private static final int SI_ERR_SPEC = -10;
	private static final int SI_ERR_AGE_TYPE = -11;
// private static final int SI_ERR_ESTAB     = -12; Replaced with Java exception

//These are taken from sindex.h (since it was missing everywhere else). These were not defined in the orginal sindxdll.c

	/* define species and equation indices */
	private static final int SI_SPEC_A = 0;
	private static final int SI_SPEC_ABAL = 1;
	private static final int SI_SPEC_ABCO = 2;
	private static final int SI_SPEC_AC = 3;
	private static final int SI_SPEC_ACB = 4;
	private static final int SI_SPEC_ACT = 5;
	private static final int SI_SPEC_AD = 6;
	private static final int SI_SPEC_AH = 7;
	private static final int SI_SPEC_AT = 8;
	private static final int SI_SPEC_AX = 9;
	private static final int SI_SPEC_B = 10;
	private static final int SI_SPEC_BA = 11;
	private static final int SI_SPEC_BB = 12;
	private static final int SI_SPEC_BC = 13;
	private static final int SI_SPEC_BG = 14;
	private static final int SI_SPEC_BI = 15;
	private static final int SI_SPEC_BL = 16;
	private static final int SI_SPEC_BM = 17;
	private static final int SI_SPEC_BP = 18;
	private static final int SI_SPEC_C = 19;
	private static final int SI_SPEC_CI = 20;
	private static final int SI_SPEC_CP = 21;
	private static final int SI_SPEC_CW = 22;
	private static final int SI_SPEC_CWC = 23;
	private static final int SI_SPEC_CWI = 24;
	private static final int SI_SPEC_CY = 25;
	private static final int SI_SPEC_D = 26;
	private static final int SI_SPEC_DG = 27;
	private static final int SI_SPEC_DM = 28;
	private static final int SI_SPEC_DR = 29;
	private static final int SI_SPEC_E = 30;
	private static final int SI_SPEC_EA = 31;
	private static final int SI_SPEC_EB = 32;
	private static final int SI_SPEC_EE = 33;
	private static final int SI_SPEC_EP = 34;
	private static final int SI_SPEC_ES = 35;
	private static final int SI_SPEC_EW = 36;
	private static final int SI_SPEC_EXP = 37;
	private static final int SI_SPEC_FD = 38;
	private static final int SI_SPEC_FDC = 39;
	private static final int SI_SPEC_FDI = 40;
	private static final int SI_SPEC_G = 41;
	private static final int SI_SPEC_GP = 42;
	private static final int SI_SPEC_GR = 43;
	private static final int SI_SPEC_H = 44;
	private static final int SI_SPEC_HM = 45;
	private static final int SI_SPEC_HW = 46;
	private static final int SI_SPEC_HWC = 47;
	private static final int SI_SPEC_HWI = 48;
	private static final int SI_SPEC_HXM = 49;
	private static final int SI_SPEC_IG = 50;
	private static final int SI_SPEC_IS = 51;
	private static final int SI_SPEC_J = 52;
	private static final int SI_SPEC_JR = 53;
	private static final int SI_SPEC_K = 54;
	private static final int SI_SPEC_KC = 55;
	private static final int SI_SPEC_L = 56;
	private static final int SI_SPEC_LA = 57;
	private static final int SI_SPEC_LE = 58;
	private static final int SI_SPEC_LT = 59;
	private static final int SI_SPEC_LW = 60;
	private static final int SI_SPEC_M = 61;
	private static final int SI_SPEC_MB = 62;
	private static final int SI_SPEC_ME = 63;
	private static final int SI_SPEC_MN = 64;
	private static final int SI_SPEC_MR = 65;
	private static final int SI_SPEC_MS = 66;
	private static final int SI_SPEC_MV = 67;
	private static final int SI_SPEC_OA = 68;
	private static final int SI_SPEC_OB = 69;
	private static final int SI_SPEC_OC = 70;
	private static final int SI_SPEC_OD = 71;
	private static final int SI_SPEC_OE = 72;
	private static final int SI_SPEC_OF = 73;
	private static final int SI_SPEC_OG = 74;
	private static final int SI_SPEC_P = 75;
	private static final int SI_SPEC_PA = 76;
	private static final int SI_SPEC_PF = 77;
	private static final int SI_SPEC_PJ = 78;
	private static final int SI_SPEC_PL = 79;
	private static final int SI_SPEC_PLC = 80;
	private static final int SI_SPEC_PLI = 81;
	private static final int SI_SPEC_PM = 82;
	private static final int SI_SPEC_PR = 83;
	private static final int SI_SPEC_PS = 84;
	private static final int SI_SPEC_PW = 85;
	private static final int SI_SPEC_PXJ = 86;
	private static final int SI_SPEC_PY = 87;
	private static final int SI_SPEC_Q = 88;
	private static final int SI_SPEC_QE = 89;
	private static final int SI_SPEC_QG = 90;
	private static final int SI_SPEC_R = 91;
	private static final int SI_SPEC_RA = 92;
	private static final int SI_SPEC_S = 93;
	private static final int SI_SPEC_SA = 94;
	private static final int SI_SPEC_SB = 95;
	private static final int SI_SPEC_SE = 96;
	private static final int SI_SPEC_SI = 97;
	private static final int SI_SPEC_SN = 98;
	private static final int SI_SPEC_SS = 99;
	private static final int SI_SPEC_SW = 100;
	private static final int SI_SPEC_SX = 101;
	private static final int SI_SPEC_SXB = 102;
	private static final int SI_SPEC_SXE = 103;
	private static final int SI_SPEC_SXL = 104;
	private static final int SI_SPEC_SXS = 105;
	private static final int SI_SPEC_SXW = 106;
	private static final int SI_SPEC_SXX = 107;
	private static final int SI_SPEC_T = 108;
	private static final int SI_SPEC_TW = 109;
	private static final int SI_SPEC_U = 110;
	private static final int SI_SPEC_UA = 111;
	private static final int SI_SPEC_UP = 112;
	private static final int SI_SPEC_V = 113;
	private static final int SI_SPEC_VB = 114;
	private static final int SI_SPEC_VP = 115;
	private static final int SI_SPEC_VS = 116;
	private static final int SI_SPEC_VV = 117;
	private static final int SI_SPEC_W = 118;
	private static final int SI_SPEC_WA = 119;
	private static final int SI_SPEC_WB = 120;
	private static final int SI_SPEC_WD = 121;
	private static final int SI_SPEC_WI = 122;
	private static final int SI_SPEC_WP = 123;
	private static final int SI_SPEC_WS = 124;
	private static final int SI_SPEC_WT = 125;
	private static final int SI_SPEC_X = 126;
	private static final int SI_SPEC_XC = 127;
	private static final int SI_SPEC_XH = 128;
	private static final int SI_SPEC_Y = 129;
	private static final int SI_SPEC_YC = 130;
	private static final int SI_SPEC_YP = 131;
	private static final int SI_SPEC_Z = 132;
	private static final int SI_SPEC_ZC = 133;
	private static final int SI_SPEC_ZH = 134;
	private static final int SI_MAX_SPECIES = 135;

	private static final int SI_ACB_HUANGAC = 97;
	private static final int SI_ACB_HUANG = 0;
	private static final int SI_ACT_THROWERAC = 103;
	private static final int SI_ACT_THROWER = 1;
	private static final int SI_AT_CHEN = 74;
	private static final int SI_AT_CIESZEWSKI = 3;
	private static final int SI_AT_GOUDIE = 4;
	private static final int SI_AT_HUANG = 2;
	private static final int SI_AT_NIGH = 92;
	private static final int SI_BA_DILUCCA = 5;
	private static final int SI_BA_KURUCZ82AC = 102;
	private static final int SI_BA_KURUCZ82 = 8;
	private static final int SI_BA_KURUCZ86 = 7;
	private static final int SI_BA_NIGHGI = 117;
	private static final int SI_BA_NIGH = 118;
	private static final int SI_BL_CHENAC = 93;
	private static final int SI_BL_CHEN = 73;
	private static final int SI_BL_KURUCZ82 = 10;
	private static final int SI_BL_THROWERGI = 9;
	private static final int SI_BP_CURTISAC = 94;
	private static final int SI_BP_CURTIS = 78;
	private static final int SI_CWC_BARKER = 12;
	private static final int SI_CWC_KURUCZAC = 101;
	private static final int SI_CWC_KURUCZ = 11;
	private static final int SI_CWC_NIGH = 122;
	private static final int SI_CWI_NIGH = 77;
	private static final int SI_CWI_NIGHGI = 84;
	private static final int SI_DR_HARRING = 14;
	private static final int SI_DR_NIGH = 13;
	private static final int SI_EP_NIGH = 116;
	private static final int SI_FDC_BRUCEAC = 100;
	private static final int SI_FDC_BRUCE = 16;
	private static final int SI_FDC_COCHRAN = 17;
	private static final int SI_FDC_KING = 18;
	private static final int SI_FDC_NIGHGI = 15;
	private static final int SI_FDC_NIGHTA = 88;
	private static final int SI_FDI_HUANG_NAT = 21;
	private static final int SI_FDI_HUANG_PLA = 20;
	private static final int SI_FDI_MILNER = 22;
	private static final int SI_FDI_MONS_DF = 26;
	private static final int SI_FDI_MONS_GF = 27;
	private static final int SI_FDI_MONS_SAF = 30;
	private static final int SI_FDI_MONS_WH = 29;
	private static final int SI_FDI_MONS_WRC = 28;
	private static final int SI_FDI_NIGHGI = 19;
	private static final int SI_FDI_THROWERAC = 96;
	private static final int SI_FDI_THROWER = 23;
	private static final int SI_FDI_VDP_MONT = 24;
	private static final int SI_FDI_VDP_WASH = 25;
	private static final int SI_HM_MEANSAC = 95;
	private static final int SI_HM_MEANS = 86;
	private static final int SI_HWC_BARKER = 33;
	private static final int SI_HWC_FARR = 32;
	private static final int SI_HWC_NIGHGI99 = 79;
	private static final int SI_HWC_WILEYAC = 99;
	private static final int SI_HWC_WILEY = 34;
	private static final int SI_HWC_WILEY_BC = 35;
	private static final int SI_HWC_WILEY_MB = 36;
	private static final int SI_HWI_NIGH = 37;
	private static final int SI_HWI_NIGHGI = 38;
	private static final int SI_LW_MILNER = 39;
	private static final int SI_LW_NIGH = 90;
	private static final int SI_LW_NIGHGI = 82;
	private static final int SI_PJ_HUANG = 113;
	private static final int SI_PJ_HUANGAC = 114;
	private static final int SI_PLI_CIESZEWSKI = 47;
	private static final int SI_PLI_DEMPSTER = 50;
	private static final int SI_PLI_GOUDIE_DRY = 48;
	private static final int SI_PLI_GOUDIE_WET = 49;
	private static final int SI_PLI_HUANG_NAT = 44;
	private static final int SI_PLI_HUANG_PLA = 43;
	private static final int SI_PLI_MILNER = 46;
	private static final int SI_PLI_NIGHGI97 = 42;
	private static final int SI_PLI_NIGHTA98 = 41;
	private static final int SI_PLI_THROWER = 45;
	private static final int SI_PLI_THROWNIGH = 40;
	private static final int SI_PL_CHEN = 76;
	private static final int SI_PW_CURTISAC = 98;
	private static final int SI_PW_CURTIS = 51;
	private static final int SI_PY_HANNAC = 104;
	private static final int SI_PY_HANN = 53;
	private static final int SI_PY_MILNER = 52;
	private static final int SI_PY_NIGH = 107;
	private static final int SI_PY_NIGHGI = 108;
	private static final int SI_SB_CIESZEWSKI = 55;
	private static final int SI_SB_DEMPSTER = 57;
	private static final int SI_SB_HUANG = 54;
	private static final int SI_SB_KER = 56;
	private static final int SI_SB_NIGH = 91;
	private static final int SI_SE_CHENAC = 105;
	private static final int SI_SE_CHEN = 87;
	private static final int SI_SE_NIGHGI = 120;
	private static final int SI_SE_NIGH = 121;
	private static final int SI_SS_BARKER = 62;
	private static final int SI_SS_FARR = 61;
	private static final int SI_SS_GOUDIE = 60;
	private static final int SI_SS_NIGH = 59;
	private static final int SI_SS_NIGHGI99 = 80;
	private static final int SI_SW_CIESZEWSKI = 67;
	private static final int SI_SW_DEMPSTER = 72;
	private static final int SI_SW_GOUDIE_NAT = 71;
	private static final int SI_SW_GOUDIE_NATAC = 106;
	private static final int SI_SW_GOUDIE_PLA = 70;
	private static final int SI_SW_GOUDIE_PLAAC = 112;
	private static final int SI_SW_GOUDNIGH = 85;
	private static final int SI_SW_HU_GARCIA = 119;
	private static final int SI_SW_HUANG_NAT = 65;
	private static final int SI_SW_HUANG_PLA = 64;
	private static final int SI_SW_KER_NAT = 69;
	private static final int SI_SW_KER_PLA = 68;
	private static final int SI_SW_NIGHGI2004 = 115;
	private static final int SI_SW_NIGHTA = 83;
	private static final int SI_SW_THROWER = 66;
	private static final int SI_MAX_CURVES = 123;

	private static final String[][] si_curve_notes = { {
			/* SI_ACB_HUANG */
			"Huang Shongming, Stephen J. Titus and Tom W. Lakusta. 1994."
					+ "Ecologically based site index curves and tables for major Alberta tree species. "
					+ "Ab. Envir. Prot., Land For. Serv., For. Man. Division,Tech. Rep. 307-308, Edmonton, Ab.",
			"The height-age (site index) curves were developed from stem analysis of 148 balsam "
					+ "poplar (Populus balsamifera spp. balsamifera) + trees from different geographic "
					+ "regions of Alberta. Site index ranged from about 10 to 28 m at 50 years "
					+ "breast-height age and included trees up to 130 years old.", },
			{
					/* SI_ACT_THROWER */
					"J. S. Thrower and Associates Ltd. 1992. Height-age/site-index curves for Black "
							+ "Cottonwood in British Columbia. Ministry of Forests, Inventory Branch. Project "
							+ "92-07-IB, 21p.",
					"The height-age (site index) curves were developed from 25 stem analysis plots of "
							+ "black cottonwood (Populus balsamifera spp. trichocarpa) located in three "
							+ "geographic regions of coastal British Columbia. Site index ranged from about 15 "
							+ "to 35 m at 50 years breast-height age and included trees up to 150 years old.", },
			{
					/* SI_AT_HUANG */
					"", /* see ACB_HUANG */
					"The height-age (site index) curves were developed from stem analysis of 757 "
							+ "trembling aspen (Populus tremuloides) trees from different geographic regions of "
							+ "Alberta. Site index ranged from about 10 to 26 m at 50 years breast-height age "
							+ "and included trees up to 138 years old.", },
			{
					/* SI_AT_CIESZEWSKI */
					"Cieszewski, Chris J. and Imre E. Bella. 1991. Polymorphic height and site index "
							+ "curves for the major tree species in Alberta. For. Can. NW Reg. North. For."
							+ "Cent, For. Manage. Note 51, Edmonton, Alberta.",
					"The height-age (site index) curves were developed from stem analysis of 276 "
							+ "dominant and co-dominant trembling aspen trees located throughout Alberta. Site "
							+ "index ranged from about 8 to 25m at 50 years breast-height age and included trees "
							+ "up to 140 years old.", },
			{
					/* SI_AT_GOUDIE */
					"Alberta Forest Service. 1985. Alberta phase 3 forest inventory: yield tables for "
							+ "unmanaged stands. ENR Rep. No. Dep. 60a.",
					"The height-age (site index) curves were developed from stem analysis of 207 "
							+ "dominant and co-dominant trembling aspen trees located throughout Alberta. Site "
							+ "index ranged from about 9 to 24 m at 50 years breast-height age and included trees "
							+ "up to 90 years old.", },
			{
					/* SI_BA_DILUCCA */
					"Di Lucca, Carlos M. 1992. Height-age/site-index curves for coastal Amabilis fir "
							+ "(Abies amabilis) in British Columbia. B.C. Ministry of Forests, Research Branch, "
							+ "Unpublish Tech. Report.",
					"The height-age (site index) polymorphic curves were developed from stem analysis "
							+ "of 199 undamaged, dominant Abies amabilis trees from 50 plots located "
							+ "throughout the coastal region of British Columbia. Plot ages ranged from 50 to "
							+ "160 years at breast height and site index ranged from 11 to 34 m.", },
			{
					/* SI_BB_KER */
					"Ker, M. F. and C. Bowling. 1991. Polymorphic site index equations for four "
							+ "New Brunswick softwood species. Can. J. For. Res. 21:728-732.",
					"The data for this curve consist of 456 trees taken from 12 m radius plots (3 "
							+ "or 4 trees per plot) established in mature and overmature stands in New "
							+ "Brunswick. The trees ranged in age from 50 to 125 years at breast height and "
							+ "ranged in site index from 3.6 m to 20.4 m at 50 years breast height age. "
							+ "Most trees suffered some minor slowing of growth due to an outbreak of spruce "
							+ "budworm.", },
			{
					/* SI_BA_KURUCZ86 */
					"Kurucz, John F. 1986. Report on Project 930-4. Site Index curve extension for "
							+ "Abies amabilis, MacMillan Bloedel Ltd., Resource Economics Section, "
							+ "Woodlands Services, Nanaimo, BC. 27 p.",
					"MacMillan Bloedel has developed site index curves for Amabilis fir (Abies "
							+ "amabilis) in 1982 using stem analyzed sample tree data obtained from immature "
							+ "and young-mature stands. These curves have been fitted to give best results "
							+ "during 0 to 150 years range of growth projections. Occasionally, prediction is "
							+ "required for a longer time period (0 to 400+ years). Attaching site index to old-"
							+ "mature stands in an inventory is a good example. From the various options "
							+ "considered, the best solution - to extend the curves to 400+ years - was found in "
							+ "recompiling the 1982 basic data with a new height-growth function.", },
			{
					/* SI_BA_KURUCZ82 */
					"Kurucz, John F. 1982. Report on Project 933-3. Polymorphic site-index curves "
							+ "for balsam -Abies amabilis- in coastal British Columbia, MacMillan Bloedel Ltd., "
							+ "Resource Economics Section, Woodlands Services, Rep. on Project 933-3. 24 p."
							+ "app. Nanaimo, BC.",
					"The height-age (site index) curves were developed from stem analysis of 199 "
							+ "undamaged, dominant Amabilis fir (Abies amabilis) trees from 50 plots located "
							+ "throughout the coastal region of British Columbia. Plot ages ranged from 50 to "
							+ "160 years at breast height and site index ranged from 11 to 34 m. The "
							+ "discontinuity in the height-age curve at age 50 is caused by the adjustment "
							+ "equation to reduce bias at ages below 50 and is exaggerated by extending the "
							+ "equation beyond the range of the site index from which it was developed.", },
			{
					/* SI_BL_THROWERGI */
					"Thrower, James S. 1997. Development of a Growth Intercept Model for Interior Balsam. ",
					"Based on balsam trees from 18 plots in the ESSF zone, and 37 plots outside "
							+ "of the ESSF. Top height ranged from 4.0 to 29.7m, breast-height age ranged from "
							+ "50 to 193 years, and site index ranged from 3.4 to 23.4m.", },
			{
					/* SI_BL_KURUCZ82 */
					"", /* see BA_KURUCZ82 */
					"The height-age (site index) curves were developed from stem analysis of 199 "
							+ "undamaged, dominant Abies amabilis trees from 50 plots located throughout the "
							+ "coastal region of British Columbia. Plot ages ranged from 50 to 160 years at "
							+ "breast height and site index ranged from 11 to 34 m." + "The discontinuity in the "
							+ "height-age curve at age 50 is caused by the adjustment equation to reduce bias at "
							+ "ages below 50 and is exaggerated by extending the equation beyond the range of "
							+ "the site index from which it was developed. The years to breast height function "
							+ "was developed by the Research Branch from interior balsam data.", },
			{
					/* SI_CWC_KURUCZ */
					"This 1985 formulation is an updated version of the curves given in 1978 by "
							+ "Kurucz 1978. Kurucz, John F. 1978. Preliminary, polymorphic site index curves "
							+ "for western redcedar (Thuja plicata Donn) in coastal British Columbia. "
							+ "MacMillan Bloedel For. Res. Note No. 3. 14 p. + appendix.",
					"The height-age (site index) curves were developed from stem analysis of "
							+ "undamaged, dominant and co-dominant trees located in approximately 50 stands "
							+ "throughout Vancouver Island and the mid-coast region of the mainland. The "
							+ "sample trees ranged in breast-height age from 33 to 285 years and in site index "
							+ "from 8 to 37 m. Kurucz suggested using this formulation with caution for breast-"
							+ "height ages less than 10 years and for site indexes greater than 37 m.", },
			{
					/* SI_CWC_BARKER */
					"Barker, John E. 1983. Site index relationships for sitka spruce, western hemlock, "
							+ "western redcedar and red alder, Moresby tree SI_farm license #24, Queen Charlotte "
							+ "Islands. Unpub. Final Rep. on Section 88 project #HR07034 submitted to Inv. Br., "
							+ "Min. For. 14 p.",
					"", },
			{
					/* SI_DR_NIGH */
					"Nigh, G.D. and P.J. Courtin. 1998 Height models for red alder (Alnus rubra "
							+ "Bong.) in British Columbia. New For. 16:59-70.",
					"The height-age equation was developed from stem analysis of 30 - 0.04 ha "
							+ "plots from natural red alder stands in tthe CWH biogeoclimatic zone in "
							+ "British Columbia. Breast height ages ranged up to 54 years and site index "
							+ "ranged from about 15 to 28 m (at 25 years breast height age). Conversions "
							+ "from a breast height age 25 site index to a breast height are 50 site index "
							+ "are derived from the height-age model. Site index can be calculated directly "
							+ "by inverting the height-age model. A years to breast height model was also "
							+ "developed from the same data.", },
			{
					/* SI_DR_HARRING */
					"Harrington, Constance A. and Robert O. Curtis. 1986. Height growth and site "
							+ "index curves for red alder. U.S. Dep. Agric. For. Serv. Res. Pap. PNW-358. 14 " + "p.",
					"The height-age equation was developed from stem analysis of 156 undamaged, "
							+ "dominant and co-dominant trees from natural red alder stands in western "
							+ "Washington and northwestern Oregon. Ages ranged up to 80 years (total age) and "
							+ "site index ranged from about 8 to 23 m (at 20 years total age). The height-age "
							+ "equation performs poorly for estimating site index below about site index 20."
							+ "Harrington and Curtis developed an equation for directly estimating site index at "
							+ "20 years total age, but our conversion to site index at 50 years breast-height age "
							+ "was not suitable for field application. "
							+ "The height equation assumes a constant of 2 years to reach breast height. This "
							+ "may be 1 or 2 years more on poor sites and less on good sites.", },
			{
					/* SI_FDC_NIGHGI */
					"Nigh, Gordon D. 1997. Coastal Douglas-fir growth intercept model. B.C. Min."
							+ "For., Res. Br., Victoria B.C. Res. Rep. 10.",
					"The growth intercept models were developed from 47 stem analysis plots located "
							+ "in the Coastal Western Hemlock and Coastal Douglas-fir biogeoclimatic zones."
							+ "Plots ranged in site index from about 15 to 46 m, and the growth intercepts ranged "
							+ "from about 22 to 108 cm. The models can be used throughout coastal British "
							+ "Columbia.", },
			{
					/* SI_FDC_BRUCE */
					"Bruce, David. 1981. Consistent height-growth and growth-rate estimates for "
							+ "remeasured plots. For. Sci. 27:711-725.",
					"The site index (height-age) curves were developed from remeasured Douglas-fir "
							+ "(Pseudotsuga menziesii) permanent sample plots in Washington, Oregon, and "
							+ "British Columbia. The plots covered a wide range of sites up to about 80 years "
							+ "breast-height age for both natural and planted stands. Tests have shown that these "
							+ "curves reasonably portray the height growth of dominant, undamaged second- and "
							+ "old-growth trees on coastal British Columbia. Bruce's curves are very similar to "
							+ "those given by J. E. King (1966. Site index curves for Douglas-fir in the Pacific "
							+ "Northwest. Weyerhaeuser Co., For. Res. Cent. For. Pap. 8. 49p.).", },
			{
					/* SI_FDC_COCHRAN */
					"Cochran, P. H. 1979. Site index and height growth curves for managed, even-"
							+ "aged stands of white or grand fir east of the cascades in Oregon and Washington. "
							+ "USDA For. Serv. Res. Pap. PNW-252, Portland, Or.",
					"Height growth and site index curves and equations for managed, even-aged stands "
							+ "of Douglas-fir ( +Pseudotsuga menziesii+ [Mirb] Franco ) east of the Cascade "
							+ "Range in Oregon and Washington are presented. Data were collected in stands "
							+ "where height growth apparently has not been suppressed by high density or top "
							+ "damage.", },
			{
					/* SI_FDC_KING */
					"King, James E. 1966. Site index curves for Douglas-fir in the Pacific Northwest."
							+ "Weyerhaeuser For. Pap. No 8, Weyerhaeuser Forestry Paper No. 8, Centralia, " + "WA.",
					"The data for this curve came from 85 plots located in pure Douglas-fir "
							+ "stands in western Washington state. Plot sizes were chosen to include 50 "
							+ "trees, of which the 10 largest dbh trees were chosen as site (sample) trees. "
							+ "Instead of conventional stem analysis, heights were measured at 5 year "
							+ "intervals on standing trees. The breast height ages of the plots ranged from "
							+ "28 to 135 years.", },
			{
					/* SI_FDI_NIGHGI */
					"Nigh, G.D. (1997). Interior Douglas-fir growth intercept models. Res. Br.,"
							+ "B.C. Min. Forests, Victoria, B.C. Ext. Note. 12.",
					"The growth intercept models were developed from 72 stem analysis plots located "
							+ "throughout the interior of British Columbia. Plots ranged in site index from "
							+ "about 10 to 29 m, and the growth intercepts ranged from about 10 to 64 cm. The "
							+ "models can be used throughout the interior of British Columbia", },
			{
					/* SI_FDI_HUANG_PLA */
					"", /* see ACB_HUANG */
					"", /* see FDI_HUANG_PLA */
			}, {
					/* SI_FDI_HUANG_NAT */
					"", /* see ACB_HUANG */
					"The height-age (site index) curves were developed from stem analysis of 66 "
							+ "interior Douglas-fir (Pseudotsuga menziesii) trees from different geographic "
							+ "regions of Alberta. Site index ranged from about 6 to 18 m at 50 years breast-"
							+ "height age and included trees up to 138 years old.", },
			{
					/* SI_FDI_MILNER */
					"Milner, Kelsey S. 1992. Site index and height growth curves for Ponderosa pine, "
							+ "Western larch, Lodgepole pine, and Douglas-fir in Western Montana. West. J."
							+ "Appl. For. 7(1):9-14.",
					"The site index (height-age) curves were developed from stem analysis of 129 "
							+ "dominant trees in 46 plots located in even-aged Douglas-fir stands throughout "
							+ "western Montana. The curves were developed from plots ranging in site index "
							+ "from 8 to 28 m and up to 80 years breast-height age.", },
			{
					/* SI_FDI_THROWER */
					"Thrower, James S. and James W. Goudie. 1992. Estimating dominant height and "
							+ "site index for even-aged interior Douglas-fir in British Columbia. West. J. Appl."
							+ "For. 7(1):20-25.",
					"The site index curves were developed from stem analysis of 262 dominant trees in "
							+ "68 plots located in even-aged Douglas-fir stands throughout the interior of British "
							+ "Columbia. The curves were developed from plots ranging in site index from 8 to "
							+ "30 m and up to 100 years breast-height age. On high sites, 30 m and greater, the "
							+ "curves may over-estimate height growth at older ages.", },
			{
					/* SI_FDI_VDP_MONT */
					"Vander Ploeg, James L. and James A. Moore. 1989. Comparison and "
							+ "Development of Height Growth and Site Index Curves for Douglas-Fir in the "
							+ "Inland Northwest. West. J. Appl. For. 4(3):85-88.",
					"The site index (height-age) curves were developed from stem analysis of 578 "
							+ "dominant trees in 89 plots located in even-aged Douglas-fir stands throughout "
							+ "Inland northwest. These curves were developed for central Washington and "
							+ "Montana from plots ranging in site index from 13 to 31 m and up to 100 years "
							+ "breast-height age.", },
			{
					/* SI_FDI_VDP_WASH */
					"", /* see FDI_VDP_MONT */
					"", /* see FDI_VDP_MONT */
			}, {
					/* SI_FDI_MONS_DF */
					"Monserud, Robert A. 1984. Height growth and site index curves for inland "
							+ "Douglas-fir based on stem analysis data and forest habitat type. For. Sci."
							+ "30:943-965.",
					"The site index (height-age) curves were developed from stem analysis in 135 plots "
							+ "located in both even- and uneven-aged Douglas-fir habitat series throughout the "
							+ "northern Rocky Mountains. The curves were developed from plots ranging in "
							+ "site index from 8 to 30 m and up to 200 years breast-height age.", },
			{
					/* SI_FDI_MONS_GF */
					"", /* see FDI_MONS_DF */
					"", /* see FDI_MONS_DF */
			}, {
					/* SI_FDI_MONS_WRC */
					"", /* see FDI_MONS_DF */
					"", /* see FDI_MONS_DF */
			}, {
					/* SI_FDI_MONS_WH */
					"", /* see FDI_MONS_DF */
					"", /* see FDI_MONS_DF */
			}, {
					/* SI_FDI_MONS_SAF */
					"", /* see FDI_MONS_DF */
					"", /* see FDI_MONS_DF */
			}, {
					/* SI_HWC_NIGHGI */
					"Nigh, Gordon D. 1996. Growth intercept models for species without distinct "
							+ "annual branch whorls: western hemlock. Can. J. For. Res. 26: 1407-1415 (1996).",
					"The growth intercept models were developed from 46 stem analysis plots located "
							+ "in the Western Hemlock biogeoclimatic zone. Plots ranged in site index "
							+ "from about 7 to 40 m, and the growth intercepts ranged from about 10 to 100 cm. "
							+ "The models can be used throughout coastal British Columbia.", },
			{
					/* SI_HWC_FARR */
					"Farr, W.A. 1984. Site index and height growth curves for unmanaged "
							+ "even-aged stands of western hemlock and Sitka spruce in southeast Alaska. "
							+ "U.S.D.A. For. Serv. Res. Pap. PNW-326.",
					"The data for these western hemlock curves come from 57 sample plots located "
							+ "in natural, well-stocked, even-aged stands of western hemlock and Sitka "
							+ "spruce throughout southeast Alaska. Seventeen plots were 1/3 - 1/2 acre in "
							+ "size and three trees of quadratic mean diameter among the dominants and "
							+ "co-dominants were stem analyzed. The remaining forty plots were 1/5 acre in "
							+ "size and trees representative of the 40 largest dbh per acre were sectioned. "
							+ "Plots ranged in breast height age from approximately 45 to 180 years of age. "
							+ "Site index ranged from approximately 41 to 120 feet.", },
			{
					/* SI_HWC_BARKER */
					"", /* see CWC_BARKER */
					"", },
			{
					/* SI_HWC_WILEY */
					"Wiley, Kenneth N. 1978. Site index tables for western hemlock in the "
							+ "Pacific Northwest. Weyerhaeuser Co., For. Res. Cent. For. Pap. 17. 28 p.",
					"The site index (height-age) curves were developed from stem analysis data "
							+ "collected from 90 plots in Washington and Oregon. The plots ranged from site "
							+ "index 18 to 40 m and from about 60 to 130 years breast-height age. The height-"
							+ "age equation should not be used for ages less than 10 years. In British Columbia, "
							+ "MacMillan Bloedel Ltd. calibrated these curves to better represent the local "
							+ "growing conditions.", },
			{
					/* SI_HWC_WILEY_BC */
					"", /* see HWC_WILEY */
					"The site index (height-age) curves were developed from stem analysis data "
							+ "collected from 90 plots in Washington and Oregon. The plots ranged from site "
							+ "index 18 to 40 m and from about 60 to 130 years breast-height age. The height-"
							+ "age equation should not be used for ages less than 10 years. In British Columbia, "
							+ "MacMillan Bloedel Ltd. calibrated these curves to better represent the local "
							+ "growing conditions.", },
			{
					/* SI_HWC_WILEY_MB */
					"", /* see HWC_WILEY */
					"The site index (height-age) curves were developed from stem analysis data "
							+ "collected from 90 plots in Washington and Oregon. The plots ranged from site "
							+ "index 18 to 40 m and from about 60 to 130 years breast-height age. The height-"
							+ "age equation should not be used for ages less than 10 years. In British Columbia, "
							+ "MacMillan Bloedel Ltd. calibrated these curves to better represent the local "
							+ "growing conditions.", },
			{
					/* SI_HWI_NIGH */
					"Nigh, G. D. 1998. A system for estimating height and site index "
							+ "of western hemlock in the interior of British Columbia. "
							+ "For. Chron. 74(4): 588-596.",
					"The height-age (site index) curves were developed from 44 stem "
							+ "analysis plots located throughout the ICH biogeoclimatic zone in British "
							+ "Columbia. Three dominant or codominant, undamaged, healthy top height "
							+ "trees were sampled in each plot. Plot breast height ages ranged from 50 "
							+ "to 241 years, site index ranged from 5.7m (at bha 50) to 25.2m and top "
							+ "height ranged up to 36.7m. The years-to-breast-height function should be "
							+ "used with caution in stands with a site index below 10m.", },
			{
					/* SI_HWI_NIGHGI */
					"Nigh, G. D. 1998. A system for estimating height and site index "
							+ "of western hemlock in the interior of British Columbia. "
							+ "For. Chron. 74(4): 588-596.",
					"The growth intercept models were developed from 44 stem analysis plots "
							+ "plots located throughout the ICH biogeoclimatic zone in British Columbia. "
							+ "Plot site index ranged from 5.7m (at bha 50) to 25.2m and growth intercepts "
							+ "ranged from about 10 to 50 cm. the models can be used throughout the "
							+ "interior of British Columbia.", },
			{
					/* SI_LW_MILNER */
					"", /* see FDI_MILNER */
					"The height-age (site index) curves were developed from stem analysis of western "
							+ "larch trees in 37 plots located throughout Western Montana. Site index ranged "
							+ "from 15 to 30 m. The abnormal shape of the height-age curves at young ages and "
							+ "low sites is the result of extending the curves beyond the range of the data from "
							+ "which they were developed. Accordingly, the site curves should not be used "
							+ "below a site index of 10 m and 30 years of age. The years-to-breast-height "
							+ "function was developed by the Research Branch from interior western larch data.", },
			{
					/* SI_PLI_THROWNIGH */
					"Nigh, G.D. 1999. Smoothing top height estimates from two lodgepole pine "
							+ "height models. B.C. Min. For., Res. Br., Victoria, B.C. Ext. Note 30.",
					"The Thrower (1994) and Nigh and Love (1999) Pl curves are spliced together "
							+ "by using the Nigh/Love curve below breast height age 0, the Thrower curve "
							+ "above breast height 2, and linearly interpolating heights between breast "
							+ "height age 0 and 2.", },
			{
					/* SI_PLI_NIGHTA98 */
					"Nigh, G.D. and B.A. Love. 1999. A model for estimating juvenile height "
							+ "of lodgepole pine. For. Ecol. Manage. 123: 157-166.",
					"The juvenile height-age model was developed from 46 stem analysis plots "
							+ "ranging from 12 to 24 years (total age) and 19 to 23 m in site index. The "
							+ "plots were established in the Bulkley valley. Four trees in each plot were "
							+ "stem analyzed by splitting the bole and measuring height growth from the "
							+ "terminal bud scars. This model is specifically designed to estimate juvenile "
							+ "height growth from germination up to total age 15, years to breast height, "
							+ "and green-up ages.", },
			{
					/* SI_PLI_NIGHGI97 */
					"Nigh, G.D. (1997). Revised growth intercept models for lodgepole pine: "
							+ "comparing northern and southern models. Res. Br., B.C. Min. Forests, "
							+ "Victoria, B.C. Ext. Note. Rep. 11.",
					"The growth intercept models were developed from 90 stem analysis plots located "
							+ "throughout British Columbia. Plots ranged in site index from about 12 to 26m, "
							+ "and the growth intercepts ranged from about 20 to 85 cm. The models can be "
							+ "used throughout the interior of British Columbia.", },
			{
					/* SI_PLI_HUANG_PLA */
					"", /* see ACB_HUANG */
					"The height-age (site index) curves were developed from stem analysis of 1417 "
							+ "lodgepole pine (Pinus contorta) trees from different geographic regions of Alberta. "
							+ "Site index ranged from about 6 to 22 m at 50 years breast-height age and included "
							+ "trees up to 168 years old.", },
			{
					/* SI_PLI_HUANG_NAT */
					"", /* see ACB_HUANG */
					"", /* see PLI_HUANG_PLA */
			}, {
					/* SI_PLI_THROWER */
					"J.S. Thrower and Associates Ltd. 1994. Revised height-age curves for lodgepole "
							+ "pine and interior spruce in British Columbia. Report to the Res. Br., B.C. "
							+ "Min. For., Victoria, B.C. 27 p.",
					"The height-age models were developed from 106 plots established throughout "
							+ "the interior of British Columbia. Ages ranged from 50 to 130 years at breast "
							+ "height. The site indices of the plots ranged from 6 to 27 m at breast height "
							+ "age 50. A years to breast height model was also developed. These curves "
							+ "replace the ones by Goudie (1984). There is little difference between the two "
							+ "curves; however, the new models are developed from data collected in British "
							+ "Columbia.", },
			{
					/* SI_PLI_MILNER */
					"", /* see FDI_MILNER */
					"The height-age (site index) curves were developed from stem analysis of trees in "
							+ "39 lodgepole pine (Pinus contorta) plots located throughout Western Montana. "
							+ "Site index ranged from 9 to 26 m.", },
			{
					/* SI_PLI_CIESZEWSKI */
					"", /* see AT_CIESZEWSKI */
					"The height-age (site index) curves were developed from stem analysis of 188 dominant and "
							+ "co-dominant lodgepole pine (Pinus contorta) trees located throughout Alberta and Eastern "
							+ "British Columbia. Plots ranged in site index from about 8 to 35 m at 50 years breast height, "
							+ "and in age up to 260 years.", },
			{
					/* SI_PLI_GOUDIE_DRY */
					"Goudie, James W. 1984. Height growth and site index curves for lodgepole pine "
							+ "and white spruce and interim managed stand yield tables for lodgepole pine in "
							+ "British Columbia. B.C. Min. For., Res. Br. Unpubl. Rep. 75 p.",
					"The height-age (site index) curves were developed from stem analysis of 188 "
							+ "dominant and co-dominant trees located throughout Alberta and Eastern British "
							+ "Columbia. Plots ranged in site index from about 6 to 22 m at 50 years breast "
							+ "height, and in age from 10 to 150 years.", },
			{
					/* SI_PLI_GOUDIE_WET */
					"", /* see PLI_GOUDIE_DRY */
					"", /* see PLI_GOUDIE_DRY */
			}, {
					/* SI_PLI_DEMPSTER */
					"", /* see AT_GOUDIE */
					"The height-age (site index) curves were developed from stem analysis of 1433 "
							+ "dominant and co-dominant lodgepole pine (Pinus contorta) trees located "
							+ "throughout Alberta and Eastern British Columbia. Plots ranged in site index from "
							+ "about 5 to 21 m at 50 years breast height, and in age up to 175 years.", },
			{
					/* SI_PW_CURTIS */
					"Curtis, Robert O., N. M. Diaz, and G. W. Clendenen. 1990. Height growth and "
							+ "site index curves for western white pine in the Cascade Range of Western "
							+ "Washington and Oregon. U.S. Dep. Agric. For. Serv. Res. Pap. RNW-PR-423." + "14 p.",
					"The height-age (site index) curves were developed from stem analysis of 38 "
							+ "dominant and co-dominant western white pine trees located throughout the "
							+ "Cascade Range of Washington and Oregon. Site index ranged from about 9 to 31 "
							+ "m at 50 years breast height and included trees up to 200 years old.", },
			{
					/* SI_PY_MILNER */
					"", /* see FDI_MILNER */
					"The height-age (site index) curves were developed from stem analysis of trees in "
							+ "31 plots located throughout Western Montana. Site index ranged from 12 to 26 m.", },
			{
					/* SI_PY_HANN */
					"Hann, D. W. and J. A. Scrivani. 1987. Dominant height growth and site index "
							+ "equations for Douglas-fir and ponderosa pine in southwest Oregon. Oreg. State "
							+ "Univ. For. Res. Lab., Corvallis Oreg., Res. Bull. 59. 13 p.",
					"The height-age (site index curves) were developed from stem analysis of 41 trees "
							+ "located throughout southwest Oregon. Selected trees came from natural, even-"
							+ "and uneven-aged, second-growth stands. Site index ranged from 19 to 34 m and "
							+ "from about 50 to 148 years breast-height age. Most stem analysis trees were "
							+ "under 120 years.", },
			{
					/* SI_SB_HUANG */
					"", /* see ACB_HUANG */
					"", },
			{
					/* SI_SB_CIESZEWSKI */
					"", /* see AT_CIESZEWSKI */
					"The height-age (site index) curves were developed from stem analysis of 282 "
							+ "dominant and co-dominant black spruce (Picea mariana) trees located throughout "
							+ "Alberta regions. Site index ranged from about 9 to 16 m at 50 years breast height "
							+ "and included trees up to 190 years old.", },
			{
					/* SI_SB_KER */
					"Ker, M. F. and C. Bowling. 1991. Polymorphic site index equations for four "
							+ "New Brunswick softwood species. Can. J. For. Res. 21:728-732.",
					"The data for this curve consist of 354 trees taken from 12 m radius plots (3 "
							+ "or 4 trees per plot) established in mature and overmature stands in New "
							+ "Brunswick. The trees ranged in age from 50 to 203 years at breast height and "
							+ "ranged in site index from 3.5 m to 17.3 m at 50 years breast height age. "
							+ "Most trees suffered some minor slowing of growth due to an outbreak of spruce "
							+ "budworm.", },
			{
					/* SI_SB_DEMPSTER */
					"", /* see AT_GOUDIE */
					"The height-age (site index) curves were developed from stem analysis of 143 "
							+ "dominant and co-dominant black spruce (Picea mariana) trees located in "
							+ "temporary and sample plots throughout Alberta regions. Site index ranged from "
							+ "about 8 to 18 m at 50 years breast height and included trees up to 175 years old.", },
			{
					/* SI_SS_NIGHGI */
					"Nigh, Gordon D. 1996. A variable growth intercept model for Sitka spruce. "
							+ "B.C. Min. For., Res. Br., Victoria, B.C. Ext. Note 03",
					"The growth intercept models were developed from 38 stem analysis plots located "
							+ "in the Coastal Western Hemlock biogeoclimatic zone. Plots ranged in site index "
							+ "from about 16 to 40 m, and the growth intercepts ranged from about 20 to 90 cm. "
							+ "The models can be used throughout coastal British Columbia.", },
			{
					/* SI_SS_NIGH */
					"Nigh, Gordon D. 1997. A Sitka spruce height-age model with improved extrapolation properties. "
							+ "For. Chron. 73(3): 363-369.",
					"The height-age (site index) curves were developed from 40 stem analysis plots "
							+ "established in ecologically uniform areas of Sitka spruce stands in the Queen "
							+ "Charlotte Islands. All plots were in the submontane wet hypermaritime Coast "
							+ "Western Hemlock (CWHwh1) biogeoclimatic variant. Plot ages ranged from 50 "
							+ "to 121 years at breast-height and site index from 13.6 to 40.3 m.", },
			{
					/* SI_SS_GOUDIE */
					"Barker, J. E. and J. W. Goudie. 1987. Site index curves for Sitka spruce. B.C."
							+ "Min. For., Res. Branch, Victoria, B.C.",
					"The height-age (site index) curves were developed from stem analysis of trees in "
							+ "48 plots located throughout the Queen Charlotte Islands. The trees ranged in "
							+ "breast-height age up to 150 years and in site index from 17 to 38 m.", },
			{
					/* SI_SS_FARR */
					"", /* see HWC_FARR */
					"", },
			{
					/* SI_SS_BARKER */
					"", /* see CWC_BARKER */
					"", },
			{
					/* SI_SW_NIGHGI */
					"Nigh, Gordon D. 1996. Variable growth intercept models for spruce in the Sub- "
							+ "Boreal Spruce and Engelmann Spruce - Subalpine Fir biogeoclimatic zones of "
							+ "British Columbia. Research Report 05, B.C. Ministry of Forests, Research "
							+ "Branch. 20 p.",
					"The growth intercept models were developed from 45 stem analysis plots located "
							+ "in the Sub-Boreal Spruce and the Engelmann Spruce - Subalpine Fir "
							+ "biogeoclimatic zones. Plots ranged in site index from about 10 to 26 m, and the "
							+ "growth intercepts ranged from about 15 to 60 cm. Until further data are available, "
							+ "the models can be used throughout British Columbia.", },
			{
					/* SI_SW_HUANG_PLA */
					"", /* see ACB_HUANG */
					"", },
			{
					/* SI_SW_HUANG_NAT */
					"", /* see ACB_HUANG */
					"", /* see SW_HUANG_PLA */
			}, {
					/* SI_SW_THROWER */
					"", /* see PLI_THROWER */
					"", },
			{
					/* SI_SW_CIESZEWSKI */
					"", /* see AT_CIESZEWSKI */
					"The height-age (site index) curves were developed from stem analysis of 698 "
							+ "dominant and co-dominant white spruce trees located throughout Alberta. Site "
							+ "index ranged from about 7 to 41 m at 50 years breast-height age and included trees "
							+ "up to 250 years old.", },
			{
					/* SI_SW_KER_PLA */
					"", /* see SB_KER */
					"The data for this curve consist of 234 trees taken from 12 m radius plots (3 "
							+ "or 4 trees per plot) established in mature and overmature stands in New "
							+ "Brunswick. The trees ranged in age from 50 to 182 years at breast height and "
							+ "ranged in site index from 3.1 m to 21.2 m at 50 years breast height age. "
							+ "Most trees suffered some minor slowing of growth due to an outbreak of spruce "
							+ "budworm.", },
			{
					/* SI_SW_KER_NAT */
					"", /* see SB_KER */
					"", /* see SW_KER_PLA */
			}, {
					/* SI_SW_GOUDIE_PLA */
					"", /* see PLI_GOUDIE_DRY */
					"The height-age (site index) curves were developed from stem analysis of 157 "
							+ "dominant and co-dominant trees located throughout Alberta and eastern British "
							+ "Columbia. Plots ranged in site index from about 3 to 24 m at 50 years breast "
							+ "height, and in age from 10 to 130 years.", },
			{
					/* SI_SW_GOUDIE_NAT */
					"", /* see PLI_GOUDIE_DRY */
					"", /* see SW_GOUDIE_PLA */
			}, {
					/* SI_SW_DEMPSTER */
					"", /* see AT_GOUDIE */
					"Notes: The height-age (site index) curves were developed from stem analysis of "
							+ "207 dominant and co-dominant trembling aspen trees located throughout Alberta. "
							+ "Site index ranged from about 9 to 24 m at 50 years breast-height age and "
							+ "included trees up to 90 years old.", },
			{
					/* SI_BL_CHEN */
					"Chen, H.Y.H, and K. Klinka. 2000. Height growth models for high-elevation "
							+ "subalpine fir, Engelmann, spruce, and lodgepole pine in British Columbia. "
							+ "West. J. Appl. For. 15: 62-69.",
					"The data for these curves come from 165 plots located in the ESSF zone of "
							+ "British Columbia. The plots were 20 x 20 m (0.04 ha) and the three largest "
							+ "dbh trees of the target species were felled and stem analyzed. The plots "
							+ "ranged in age from 51 to 217 years at breast height and ranged in site index "
							+ "from 2.7 to 21.8 m.", },
			{
					/* SI_AT_CHEN */
					"Chen, H.Y.H., K. Klinka, and R.D. Kabzems. 1998. Height growth and site "
							+ "index models for trembling aspen (Populus tremuloides Michx.) in northern "
							+ "British Columbia. Forest Ecology and Management 102:157-165.",
					"33 naturally established, undamaged, closed-canopy stands were sampled over "
							+ "a wide range of sites in the Boreal White and Black Spruce zone of British "
							+ "Columbia. The site index curve is recommended to be used across the eastern "
							+ "portion of the Boreal White and Black Spruce zone for estimating site index "
							+ "of aspen stands aged 15 - 70 years at breast-height.", },
			{
					/* SI_DR_CHEN */
					"Chen, Han Y. H. 1999.", "", },
			{
					/* SI_PL_CHEN */
					"Chen, H.Y.H, and K. Klinka. 2000. Height growth models for high-elevation "
							+ "subalpine fir, Engelmann, spruce, and lodgepole pine in British Columbia."
							+ "West. J. Appl. For. 15: 62-69.",
					"The data for these curves come from 67 plots located in the ESSF zone of "
							+ "British Columbia. The plots were 20 x 20 m (0.04 ha) and the three largest "
							+ "dbh trees of the target species were felled and stem analyzed. The plots "
							+ "ranged in age from 50 to 114 years at breast height and ranged in site index "
							+ "from 7.8 to 20.4 m.", },
			{
					/* SI_CWI_NIGH */
					"Nigh, G.D. 2000. Western redcedar site index models for the interior of "
							+ "British Columbia. B.C. Min. For., Res. Br., Victoria, B.C. Res. Rep. 18. 24 p.",
					"The site index (height-age) and growth intercept models for western redcedar "
							+ "in the interior of British Columbia were developed from 46 stem analysis "
							+ "plots established in ecologically uniform areas in the northern and southern "
							+ "portions of the ICH biogeoclimatic zone and the IDF zone. Plot ages ranged "
							+ "from 67 to 146 years at breast height and site index ranged from 10.50 to "
							+ "23.89 m. A years-to-breast-height function was also developed with these data.", },
			{
					/* SI_BP_CURTIS */
					"Curtis, R.O. 1990. Site index curves from stem analyses - methodology "
							+ "effects and a new technique applied to noble fir. USDA For. Serv., PNW Res."
							+ "Stn. Unpubl. Rep.",
					"The height-age (site index) curves were developed from stem analysis of 54 "
							+ "trees taken from mixed species stands from Oregon and Washington. The sample "
							+ "trees ranged in breast height age up to 240 years and in site index from "
							+ "approximately 8 m to 40 m.", },
			{
					/* SI_HWC_NIGHGI99 */
					"Nigh, G.D. 1999. Revised growth intercept models for coastal western "
							+ "hemlock, Sitka spruce, and interior spruce. B.C. Min. For., Res. Br.,"
							+ "Victoria, B.C. Exten. Note 37. 8 p.",
					"The western hemlock growth intercept models were developed from 46 stem "
							+ "analysis plots established in ecologically uniform areas throughout the CWH "
							+ "biogeoclimatic zone. Plot ages ranged from 50 to 173 years at breast-height "
							+ "and site index from 7.7 to 38.1 m. These models were updated from the "
							+ "original (1996) models to reflect changes in the growth intercept modelling "
							+ "technique.", },
			{
					/* SI_SS_NIGHGI99 */
					"Nigh, G.D. 1999. Revised growth intercept models for coastal western "
							+ "hemlock, Sitka spruce, and interior spruce. B.C. Min. For., Res. Br.,"
							+ "Victoria, B.C. Exten. Note 37. 8 p.",
					"The Sitka spruce growth intercept models were developed from 38 stem analysis "
							+ "plots established in ecologically uniform areas of Sitka spruce stands in the "
							+ "Queen Charlotte Islands. All plots were in the submontane wet hypermaritime "
							+ "Coast Western Hemlock (CWHwh1) biogeoclimatic variant. Plot ages ranged from "
							+ "50 to 121 years at breast-height and site index from 13.6 to 40.3 m. These "
							+ "models were updated from the original (1996) models to reflect changes in the "
							+ "growth intercept modelling technique.", },
			{
					/* SI_SW_NIGHGI99 */
					"Nigh, G.D. 1999. Revised growth intercept models for coastal western "
							+ "hemlock, Sitka spruce, and interior spruce. B.C. Min. For., Res. Br.,"
							+ "Victoria, B.C. Exten. Note 37. 8 p.",
					"The interior spruce growth intercept models were developed from 87 stem "
							+ "analysis plots established throughout British Colulmbia. The plots were "
							+ "established under three different projects. Plot ages ranged from 50 to 209 "
							+ "years at breast-height and site index from 5.98 to 25.52 m. These models were "
							+ "updated from the original (1996) models to reflect changes in the growth "
							+ "intercept modelling technique.", },
			{
					/* SI_LW_NIGHGI */
					"Nigh, G.D., D. Brisco, and D. New. 1999. Growth intercept models for "
							+ "western larch. B.C. Min. For., Res. Br., Victoria, B.C. Exten. Note 38." + "4 p.",
					"The western larch growth intercept models were developed from 99 stem "
							+ "analysis plots established by the University of British Columbia for a larch "
							+ "productivity study. The plots were established to cover the geographic range "
							+ "of western larch in British Columbia. Plot site indexes ranged from 9.7 to "
							+ "27.01 m.", },
			{
					/* SI_SW_NIGHTA */
					"Nigh, G.D. and B.A. Love. 2000. Juvenile height development in interior "
							+ "spruce stands of British Columbia. West. J. Appl. For. 15: 117-121.",
					"The juvenile height model for interior spruce was developed from 39 stem "
							+ "analysis plots established in ecologically uniform areas in the SBSmc2, "
							+ "ICHmc1, ICHmc2, and ESSFmc biogeoclimatic subzones. Plot ages (total) ranged "
							+ "from 17 to 33 years and site index ranged from 19.62 to 25.47 m. Functions "
							+ "for years to breast height and green-up age were derived from this model.", },
			{
					/* SI_CWI_NIGHGI */
					"", /* see CWI_NIGH */
					"", /* see CWI_NIGH */
			}, {
					/* SI_SW_GOUDNIGH */
					"Nigh, G.D. and B.A. Love. 2000. Juvenile height development in interior "
							+ "spruce stands of British Columbia. West. J. Appl. For. 15: 117-121."
							+ "Goudie, J.W. 1984. Height growth and site index curves for lodgepole pine and "
							+ "white spruce and interim managed stand yield tables for lodgepole pine in "
							+ "British Columbia. B.C. Min. For., Res. Br. Unpubl. Rep. 75 p.",
					"These curves result from the splicing together of the juvenile height curves "
							+ "by Nigh and Love (2000) and the height-age curves by Goudie (1984).", },
			{
					/* SI_HM_MEANS */
					"Means, J.E., M.H. Campbell, and G.P. Johnson. 1988. Preliminary "
							+ "height-growth and site-index curves for moutain hemlock. FIR Report " + "10(1): 8-9.",
					"The height-age curves for mountain hemlock were developed from 95 trees "
							+ "sampled in the Cascade mountains in Washington and Oregon. The stands from "
							+ "which the trees were sampled were unmanaged, and the trees were dominant or "
							+ "co-dominant with no signs of stem breakage or suppression. Most of the "
							+ "sample trees were between 150 and 350 years of age and the site index ranged "
							+ "from 3 to 15 m (mean 8 m). The years to breast height function for coastal "
							+ "western hemlock is being used for mountain hemlock.", },
			{
					/* SI_SE_CHEN */
					"Chen, H.Y.H, and K. Klinka. 2000. Height growth models for high-elevation "
							+ "subalpine fir, Engelmann, spruce, and lodgepole pine in British Columbia. "
							+ "West. J. Appl. For. 15: 62-69.",
					"The data for these curves come from 87 plots located in the ESSF zone of "
							+ "British Columbia. The plots were 20 x 20 m (0.04 ha) and the three largest "
							+ "dbh trees of the target species were felled and stem analyzed. The plots "
							+ "ranged in age from 50 to 164 years at breast height and ranged in site index "
							+ "from 5.2 to 25.0 m.", },
			{
					/* SI_FDC_NIGHTA */
					"Nigh, G.D. and M.G. Mitchell. 2003. Development of height-age models for "
							+ "estimating juvenile height of coastal Douglas-fir in British Columbia. "
							+ "West. J. Appl. For. 18: 207-212.",
					"The juvenile height models for coastal Douglas-fir were developed from 100 "
							+ "trees located throughout the range of Douglas-fir on the coast of British "
							+ "Columbia. The data come from 100 - 0.01 ha plots; one site tree was sampled "
							+ "from each plot. Each site tree was split and its height growth was measured "
							+ "from the pith nodes. The ages of the trees ranged from 15 to 42 years in total "
							+ "age, and the site index ranged from 16 to 44.5 m at breast height age 50. "
							+ "The curves are restricted for use from total age 0 to total age 25. There are "
							+ "no restrictions in the range of site index, but should be used cautiously "
							+ "outside the range of sampled site indices. "
							+ "There are accompanying years to breast height and green-up age (years "
							+ "to 3 m height) models.", },
			{
					/* SI_FDC_BRUCENIGH */
					"Nigh, G.D. and M.G. Mitchell. 2003. Development of height-age models for "
							+ "estimating juvenile height of coastal Douglas-fir in British Columbia. "
							+ "West. J. Appl. For. 18: 207-212."
							+ "Bruce, David. 1981. Consistent height-growth and growth-rate estimates for "
							+ "remeasured plots. For. Sci. 27:711-725."
							+ "Nigh, G.D. and K.R. Polsson. 2002. Splicing height curves. B.C. Min. For.,"
							+ "Res. Br., Victoria, B.C. Exten. Note 60.",
					"The Bruce curves were developed from re-measured PSPs in Washington, Oregon, "
							+ "and B.C. The plots covered a wide range of sites up to about 80 yrs bha for "
							+ "both natural and planted stands. The Nigh / Mitchell curves were developed "
							+ "from 104 plots located in juvenile managed stands in southwestern B.C. "
							+ "These two models were spliced together. Note that the final spliced "
							+ "models differ slightly from the Nigh / Polsson publication.", },
			{
					/* SI_LW_NIGH */
					"Brisco, D., K. Klinka, and G. Nigh. 2002. Height growth models for western "
							+ "larch in British Columbia. West. J. Appl. For. 17: 66-74.",
					"The western larch height-age curves were developed from 105 - 0.04 ha plots "
							+ "established throughout the range of western larch in British Columbia. Three "
							+ "trees were sampled in each plot. The stem analysis data were collected as "
							+ "part of a larch productivity study conducted by researchers at the "
							+ "University of British Columbia. The ages of the plots ranged from 45 to 134 "
							+ "years at breast height and the site indices ranged from 9.7 m to 27.1 m. "
							+ "These curves are based on the Chapman-Richards function.", },
			{
					/* SI_SB_NIGH */
					"Nigh, G.D., P.V. Krestov, and K. Klinka. 2002. Height growth of black spruce "
							+ "in British Columbia. For. Chron. 78: 306-313.",
					"The data for the black spruce height-age curves consist of 91 stem analysis "
							+ "plots established as part of a black spruce productivity study by researchers "
							+ "at UBC. These plots are located in the BWBS and SBS biogeoclimatics zone of "
							+ "British Columbia. The breast height ages of the plots range up to 174 years "
							+ "and their site index range is from 4.98 m up to 17.09 m. These curves can be "
							+ "used for black spruce throughout British Columbia.", },
			{
					/* SI_AT_NIGH */
					"Nigh, G.D., P.V. Krestov, and K. Klinka. 2002. Trembling aspen height-age "
							+ "models for British Columbia. Northwest Sci. Vol. 36, No. 3.",
					"The 135 plots for the trembling aspen height-age curves come from a trembling "
							+ "aspen productivity study done by researchers at UBC. The plots were "
							+ "established in the BWBS, SBS, SBPS, IDF, MS, and ICH biogeoclimatic zones. "
							+ "They range in age from 50 to 177 years at breast height, and from site "
							+ "indexes 5.60 m to 29.56 m. These curves are recommended for use throughout "
							+ "British Columbia.", },
			{
					/* SI_BL_CHENAC */
					"Chen, H.Y.H, and K. Klinka. 2000. Height growth models for high-elevation "
							+ "subalpine fir, Engelmann, spruce, and lodgepole pine in British Columbia."
							+ "West. J. Appl. For. 15: 62-69.",
					"The data for these curves come from 165 plots located in the ESSF zone of "
							+ "British Columbia. The plots were 20 x 20 m (0.04 ha) and the three largest "
							+ "dbh trees of the target species were felled and stem analyzed. The plots "
							+ "ranged in age from 51 to 217 years at breast height and ranged in site index "
							+ "from 2.7 to 21.8 m. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_BP_CURTISAC */
					"Curtis, R.O. 1990. Site index curves from stem analyses - methodology "
							+ "effects and a new technique applied to noble fir. USDA For. Serv., PNW Res. "
							+ "Stn. Unpubl. Rep.",
					"The height-age (site index) curves were developed from stem analysis of 54 "
							+ "trees taken from mixed species stands from Oregon and Washington. The sample "
							+ "trees ranged in breast height age up to 240 years and in site index from "
							+ "approximately 8 m to 40 m. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.37 to 0.5,1.37.", },
			{
					/* SI_HM_MEANSAC */
					"Means, J.E., M.H. Campbell, and G.P. Johnson. 1988. Preliminary "
							+ "height-growth and site-index curves for moutain hemlock. FIR Report " + "10(1): 8-9.",
					"The height-age curves for mountain hemlock were developed from 95 trees "
							+ "sampled in the Cascade mountains in Washington and Oregon. The stands from "
							+ "which the trees were sampled were unmanaged, and the trees were dominant or "
							+ "co-dominant with no signs of stem breakage or suppression. Most of the "
							+ "sample trees were between 150 and 350 years of age and the site index ranged "
							+ "from 3 to 15 m (mean 8 m). The years to breast height function for coastal "
							+ "western hemlock is being used for mountain hemlock. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.37 to 0.5,1.37.", },
			{
					/* SI_FDI_THROWERAC */
					"Thrower, James S. and James W. Goudie. 1992. Estimating dominant height and "
							+ "site index for even-aged interior Douglas-fir in British Columbia. West. J. Appl."
							+ "For. 7(1):20-25.",
					"The site index curves were developed from stem analysis of 262 dominant trees in "
							+ "68 plots located in even-aged Douglas-fir stands throughout the interior of British "
							+ "Columbia. The curves were developed from plots ranging in site index from 8 to "
							+ "30 m and up to 100 years breast-height age. On high sites, 30 m and greater, the "
							+ "curves may over-estimate height growth at older ages. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_ACB_HUANGAC */
					"Huang Shongming, Stephen J. Titus and Tom W. Lakusta. 1994. "
							+ "Ecologically based site index curves and tables for major " + "Alberta tree species. "
							+ "Ab. Envir. Prot., Land For. Serv., For. Man. Division, "
							+ "Tech. Rep. 307-308, Edmonton, Ab.",
					"The height-age (site index) curves were developed from stem "
							+ "analysis of 148 balsam poplar (Populus balsamifera spp. balsamifera) "
							+ "trees from different geographic regions of Alberta. "
							+ "Site index ranged from about 10 to 28 m at 50 years "
							+ "breast-height age and included trees up to 130 years old. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_PW_CURTISAC */
					"Curtis, Robert O., N. M. Diaz, and G. W. Clendenen. 1990. Height growth and "
							+ "site index curves for western white pine in the Cascade Range of Western "
							+ "Washington and Oregon. U.S. Dep. Agric. For. Serv. Res. Pap. RNW-PR-423." + "14 p.",
					"The height-age (site index) curves were developed from stem analysis of 38 "
							+ "dominant and co-dominant western white pine trees located throughout the "
							+ "Cascade Range of Washington and Oregon. Site index ranged from about 9 to 31 "
							+ "m at 50 years breast height and included trees up to 200 years old. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.37 to 0.5,1.37.", },
			{
					/* SI_HWC_WILEYAC */
					"Wiley, Kenneth N. 1978. Site index tables for western hemlock in the "
							+ "Pacific Northwest. Weyerhaeuser Co., For. Res. Cent. For. Pap. 17. 28 p.",
					"The site index (height-age) curves were developed from stem analysis data "
							+ "collected from 90 plots in Washington and Oregon. The plots ranged from site "
							+ "index 18 to 40 m and from about 60 to 130 years breast-height age. The height-"
							+ "age equation should not be used for ages less than 10 years. In British Columbia, "
							+ "MacMillan Bloedel Ltd. calibrated these curves to better represent the local "
							+ "growing conditions. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.37 to 0.5,1.37.", },
			{
					/* SI_FDC_BRUCEAC */
					"Bruce, David. 1981. Consistent height-growth and growth-rate estimates for "
							+ "remeasured plots. For. Sci. 27:711-725.",
					"The site index (height-age) curves were developed from remeasured Douglas-fir "
							+ "(Pseudotsuga menziesii) permanent sample plots in Washington, Oregon, and "
							+ "British Columbia. The plots covered a wide range of sites up to about 80 years "
							+ "breast-height age for both natural and planted stands. Tests have shown that these "
							+ "curves reasonably portray the height growth of dominant, undamaged second- and "
							+ "old-growth trees on coastal British Columbia. Bruce's curves are very similar to "
							+ "those given by J. E. King (1966. Site index curves for Douglas-fir in the Pacific "
							+ "Northwest. Weyerhaeuser Co., For. Res. Cent. For. Pap. 8. 49p.). "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.37 to 0.5,1.37.", },
			{
					/* SI_CWC_KURUCZAC */
					"This 1985 formulation is an updated version of the curves given in 1978 by "
							+ "Kurucz 1978. Kurucz, John F. 1978. Preliminary, polymorphic site index curves "
							+ "for western redcedar (Thuja plicata Donn) in coastal British Columbia."
							+ "MacMillan Bloedel For. Res. Note No. 3. 14 p. + appendix.",
					"The height-age (site index) curves were developed from stem analysis of "
							+ "undamaged, dominant and co-dominant trees located in approximately 50 stands "
							+ "throughout Vancouver Island and the mid-coast region of the mainland. The "
							+ "sample trees ranged in breast-height age from 33 to 285 years and in site index "
							+ "from 8 to 37 m. Kurucz suggested using this formulation with caution for breast-"
							+ "height ages less than 10 years and for site indexes greater than 37 m. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_BA_KURUCZ82AC */
					"Kurucz, John F. 1982. Report on Project 933-3. Polymorphic site-index curves "
							+ "for balsam -Abies amabilis- in coastal British Columbia, MacMillan Bloedel Ltd.,"
							+ "Resource Economics Section, Woodlands Services, Rep. on Project 933-3. 24 p."
							+ "app. Nanaimo, BC.",
					"The height-age (site index) curves were developed from stem analysis of 199 "
							+ "undamaged, dominant Amabilis fir (Abies amabilis) trees from 50 plots located "
							+ "throughout the coastal region of British Columbia. Plot ages ranged from 50 to "
							+ "160 years at breast height and site index ranged from 11 to 34 m. The "
							+ "discontinuity in the height-age curve at age 50 is caused by the adjustment "
							+ "equation to reduce bias at ages below 50 and is exaggerated by extending the "
							+ "equation beyond the range of the site index from which it was developed. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_ACT_THROWERAC */
					"J. S. Thrower and Associates Ltd. 1992. Height-age/site-index curves for Black "
							+ "Cottonwood in British Columbia. Ministry of Forests, Inventory Branch. Project "
							+ "92-07-IB, 21p.",
					"The height-age (site index) curves were developed from 25 stem analysis plots of "
							+ "black cottonwood (Populus balsamifera spp. trichocarpa) located in three "
							+ "geographic regions of coastal British Columbia. Site index ranged from about 15 "
							+ "to 35 m at 50 years breast-height age and included trees up to 150 years old. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_PY_HANNAC */
					"Hann, D. W. and J. A. Scrivani. 1987. Dominant height growth and site index "
							+ "equations for Douglas-fir and ponderosa pine in southwest Oregon. Oreg. State "
							+ "Univ. For. Res. Lab., Corvallis Oreg., Res. Bull. 59. 13 p.",
					"The height-age (site index curves) were developed from stem analysis of 41 trees "
							+ "located throughout southwest Oregon. Selected trees came from natural, even-"
							+ "and uneven-aged, second-growth stands. Site index ranged from 19 to 34 m and "
							+ "from about 50 to 148 years breast-height age. Most stem analysis trees were "
							+ "under 120 years. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.37 to 0.5,1.37.", },
			{
					/* SI_SE_CHENAC */
					"Chen, H.Y.H, and K. Klinka. 2000. Height growth models for high-elevation "
							+ "subalpine fir, Engelmann, spruce, and lodgepole pine in British Columbia. "
							+ "West. J. Appl. For. 15: 62-69.",
					"The data for these curves come from 87 plots located in the ESSF zone of "
							+ "British Columbia. The plots were 20 x 20 m (0.04 ha) and the three largest "
							+ "dbh trees of the target species were felled and stem analyzed. The plots "
							+ "ranged in age from 50 to 164 years at breast height and ranged in site index "
							+ "from 5.2 to 25.0 m. "
							+ "Note: the formulation was modified in 2003 to move the age,height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_SW_GOUDIE_NATAC */
					"Goudie, James W. 1984. Height growth and site index curves for lodgepole pine "
							+ "and white spruce and interim managed stand yield tables for lodgepole pine in "
							+ "British Columbia. B.C. Min. For., Res. Br. Unpubl. Rep. 75 p.",
					"The height-age (site index) curves were developed from stem analysis of 188 "
							+ "dominant and co-dominant trees located throughout Alberta and Eastern British "
							+ "Columbia. Plots ranged in site index from about 6 to 22 m at 50 years breast "
							+ "height, and in age from 10 to 150 years. "
							+ "Note: the formulation was modified in 2004 to move the age,height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_PY_NIGH */
					"Nigh, G.D. 2004. A comparison of fitting techniques for ponderosa pine height-age "
							+ "models in British Columbia. Ann. For. Sci. 61: 609-615."
							+ "Nigh, G.D. 2002. Growth intercept, years-to-breast-height, and juvenile height "
							+ "growth models for ponderosa pine. Res. Br., B.C. Min. For., Victoria, B.C. Tech. Rep. 2.",
					"The hybrid model used herein consists of a Juvenile Height Growth model spliced to a "
							+ "Site Index model, at breast height. These models were developed from 80 ponderosa pine "
							+ "stem analysis plots. The plots were distributed across the range of ponderosa pine in "
							+ "British Columbia, specifically from the BG, PP, IDF, and ICH biogeoclimatic zones. The "
							+ "site index for these plots ranged from 5.01 m to 24.78 m and the ages ranged from 74 to "
							+ "227 years at breast height.", },
			{
					/* SI_PY_NIGHGI */
					"Nigh, G.D. 2002. Growth intercept, years-to-breast-height, and juvenile height "
							+ "growth models for ponderosa pine. Res. Br., B.C. Min. For., Victoria, B.C. Tech. Rep. 2.",
					"", },
			{
					/* SI_PLI_NIGHTA2004 */
					"Nigh, G.D. 2004. Juvenile height models for lodgepole pine and "
							+ "interior spruce: validation of existing models and development of "
							+ "new models. B.C. Min. For., Res. Br., Victoria, B.C. Res. Rep. 25.",
					"New juvenile height models for lodgepole pine were developed with data "
							+ "collected from the BWBS, ESSF, ICH, IDF, MS, SBS, and SBPS biogeoclimatic zones. The data "
							+ "included 65 plots. The models extend the "
							+ "geographic and site index range of the original juvenile height models. These models are "
							+ "applicable for estimating stands up to total age 15."
							+ "The site index range is 16.25 to 24.78 m.", },
			{
					/* SI_SE_NIGHTA */
					"Nigh, G.D. 2004. Juvenile height models for lodgepole pine and "
							+ "interior spruce: validation of existing models and development of "
							+ "new models. B.C. Min. For., Res. Br., Victoria, B.C. Res. Rep. 25.",
					"New juvenile height models for interior spruce were developed with data "
							+ "collected from the BWBS, ESSF, ICH, IDF, MS, SBS, and SBPS biogeoclimatic zones. The data "
							+ "included 57 plots. The models extend the "
							+ "geographic and site index range of the original juvenile height models. These models are "
							+ "applicable for estimating stands up to total age 20. "
							+ "The site index range is 17.01 to 30.48 m.", },
			{
					/* SI_SW_NIGHTA2004 */
					"Nigh, G.D. 2004. Juvenile height models for lodgepole pine and "
							+ "interior spruce: validation of existing models and development of "
							+ "new models. B.C. Min. For., Res. Br., Victoria, B.C. Res. Rep. 25.",
					"New juvenile height models for interior spruce were developed with data "
							+ "collected from the BWBS, ESSF, ICH, IDF, MS, SBS, and SBPS biogeoclimatic zones. The data "
							+ "included 57 plots. The models extend the"
							+ "geographic and site index range of the original juvenile height models. These models are "
							+ "applicable for estimating stands up to total age 20. "
							+ "The site index range is 17.01 to 30.48 m.", },
			{
					/* SI_SW_GOUDIE_PLAAC */
					"Goudie, James W. 1984. Height growth and site index curves for lodgepole pine "
							+ "and white spruce and interim managed stand yield tables for lodgepole pine in "
							+ "British Columbia. B.C. Min. For., Res. Br. Unpubl. Rep. 75 p.",
					"The height-age (site index) curves were developed from stem analysis of 188 "
							+ "dominant and co-dominant trees located throughout Alberta and Eastern British "
							+ "Columbia. Plots ranged in site index from about 6 to 22 m at 50 years breast "
							+ "height, and in age from 10 to 150 years."
							+ "Note: the formulation was modified in 2004 to move the age, height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_PJ_HUANG */
					"Huang, S. Subregion-based compatible height and site index models for young"
							+ "and mature stands in Alberta: revisions and summaries (Part II)."
							+ "Alberta Environmental Protection. Land and Forest Service."
							+ "Forest Management Research Note No. 10.",
					"Subregion-based compatible height and site index models expressed in the "
							+ "form of H=f(SI,age) were developed for major Alberta tree species. All models "
							+ "fitted the data reasonably well across the full range of breast height age "
							+ "classes. They can be used for growth intercept models for young trees/stands, "
							+ "juvenile height and site index models, and regular height and site index "
							+ "models for mature trees/stands.", },
			{
					/* SI_PJ_HUANGAC */
					"Huang, S. Subregion-based compatible height and site index models for young "
							+ "and mature stands in Alberta: revisions and summaries (Part II)."
							+ "Alberta Environmental Protection. Land and Forest Service."
							+ "Forest Management Research Note No. 10.",
					"Subregion-based compatible height and site index models expressed in the "
							+ "form of H=f(SI,age) were developed for major Alberta tree species. All models "
							+ "fitted the data reasonably well across the full range of breast height age "
							+ "classes. They can be used for growth intercept models for young trees/stands, "
							+ "juvenile height and site index models, and regular height and site index "
							+ "models for mature trees/stands. "
							+ "Note: the formulation was modified in 2004 to move the age, height origin from "
							+ "0,1.3 to 0.5,1.3.", },
			{
					/* SI_SW_NIGHGI2004 */
					"Nigh, G.D. 2004. Growth intercept and site series-based estimates of site "
							+ "index for white spruce in the boreal white and black spruce biogeoclimatic "
							+ "zone. B.C. Min. For., Res. Br.," + "Victoria, B.C. Tech. Rep. 013. 8 p.",
					"", },
			{
					/* SI_EP_NIGH */
					"Nigh, G.D., K.D. Thomas, K. Yearsley, and J. Wang.  2009. Site-dependent height"
							+ "-age models for paper birch in British Columbia. Northwest Sci. 83: 253-261.",
					"These height-age index curves were developed from stem analysis of 168 dominant "
							+ "trees in 61 plots located in even-aged stands dominated by paper birch in the SBS, "
							+ "ICH, and IDF biogeoclimatic zones in the interior of British Columbia. The curves "
							+ "were developed from plots ranging in site index from 11 to 26 m and up to 125 years breast-height age.", },
			{
					/* SI_BA_NIGHGI */
					"Nigh, G.D. 2009. Amabilis fir height-age and growth intercept models for British Columbia."
							+ "B.C. Min. For. Range, For. Sci. Prog., Victoria, B.C. Res. Rep. 30. www.for.gov.bc.ca/hfd/pubs/Docs/Rr/Rr30.htm",
					"The height-age (site index) curves were developed from stem analysis of 74 plots of undamaged, "
							+ "dominant amabilis fir (Abies amabilis) located throughout the coastal region of British Columbia. "
							+ "Plot ages ranged from 50 to 220 years at breast height and site index ranged from 11 to 36 m. The "
							+ "data set used to develop these models includes the Kurucz (1982) data and new data collected in 2008.", },
			{
					/* SI_BA_NIGH */
					"", "", },
			{
					/* SI_SW_HU_GARCIA */
					"", "", },
			{
					/* SI_SE_NIGHGI */
					"Nigh, G.D. (2014). An Errors-in-Variable Model with Correlated Errors:"
							+ "Engelmann Spruce Growth Intercept Models. For. Anal. Inv. Br., B.C."
							+ "Min. For., Lands, Nat. Resour. Oper., Victoria, B.C. Tech. Rep. 084. ",
					"The growth intercept models were developed from 84 stem analysis plots "
							+ "located throughout the range of the Engelmann Spruce � Subalpine Fir "
							+ "(ESSF) biogeoclimatic zone of British Columbia. Plots ranged in site "
							+ "index from about 6 to 24 m. The models can be used to estimate site index "
							+ "throughout the ESSF zone in British Columbia.", },
			{
					/* SI_SE_NIGH */
					"Nigh, G. 2015. Engelmann spruce site index models: a comparison of model functions "
							+ "and parameterizations. PLoS ONE 10(4): e0124079. doi: 10.1371/journal.pone.0124079.",
					"The curves were developed from 84 Engelmann spruce trees located throughout the range "
							+ "of the ESSF biogeoclimatic zone. The age of the sample trees ranged from 70 to 255 years "
							+ "at breast height and their heights ranged from 7.84 to 40.79 m. The range in site index was 5.58 to 24.22 m.", },
			{
					/* SI_CWC_NIGH */
					"Nigh, G.D. 2016. Revised site index models for western redcedar for coastal British Columbia. Prov."
							+ "B.C., Victoria, B.C. Tech. Rep. 105.",
					"The site index models were developed from the stem analysis of 63 trees from 4 sources of data. "
							+ "Pseudo-height/age data were obtained from the Kurucz (1978) site index models and were supplemented "
							+ "with data from a wood quality study, and with data kindly donated by McMillan-Bloedel and Radwan and"
							+ "Harrington. The pseudo-data were generated so that the ages and site indexes corresponded to the "
							+ "original Kurucz data set. The other data were from trees less than 95 years old. The original g-GADA formulation "
							+ "of this model required iterating to estimate one of the model parameters. An ad hoc equation to predict this "
							+ "parameter from site index was developed and implemented.", },
			{
					// The following is conditional code for #ifdef HOOP but I have removed the
					// condition. From what I can tell
					// it never would have triggered, so this may be able to be removec entirely
					/* SI_PJ_KER */
					"", /* same as SI_SB_KER */
					"The data for this curve consist of 114 trees taken from 12 m radius plots (3 "
							+ "or 4 trees per plot) established in mature and overmature stands in New "
							+ "Brunswick. The trees ranged in age from 51 to 175 years at breast height and "
							+ "ranged in site index from 7.2 m to 21.0 m at 50 years breast height age." } };

	private static final double ERROR_TOLERANCE = 0.00001;

	@Test
	void testVersionNumber() {
		short expectedValue = 151;
		short actualValue = Sindxdll.VersionNumber();
		assertEquals(expectedValue, actualValue);
	}

	@Test
	void testFirstSpecies() {
		short expectedValue = 0;
		short actualValue = Sindxdll.FirstSpecies();
		assertEquals(expectedValue, actualValue);
	}

	@Nested
	class NextSpeciesTest {
		@Test
		void testValidIndex() {
			short inputIndex = 2; // Choose a valid index for testing
			short expectedOutput = (short) (inputIndex + 1);

			short actualOutput = Sindxdll.NextSpecies(inputIndex);

			assertEquals(expectedOutput, actualOutput, "NextSpecies should return the next species index");
		}

		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.NextSpecies(invalidIndex),
					"NextSpecies should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.NextSpecies(invalidIndex),
					"NextSpecies should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testLastIndex() {
			short lastIndex = 134; // Use the value of SI_SPEC_END for testing
			assertThrows(
					NoAnswerException.class, () -> Sindxdll.NextSpecies(lastIndex),
					"NextSpecies should throw NoAnswerException for last defined species index"
			);
		}
	}

	@Nested
	class SpecCodeTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					IllegalArgumentException.class, () -> Sindxdll.SpecCode(invalidIndex),
					"SpecCode should throw IllegalArgumentException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					IllegalArgumentException.class, () -> Sindxdll.SpecCode(invalidIndex),
					"SpecCode should throw IllegalArgumentException for invalid index"
			);
		}

		@Test
		void testValidIndex() {
			short validIndex = 0;
			String expectedResult = "A";
			String actualResult = Sindxdll.SpecCode(validIndex);

			assertEquals(actualResult, expectedResult);
		}
	}

	@Nested
	class SpecNameTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					IllegalArgumentException.class, () -> Sindxdll.SpecName(invalidIndex),
					"SpecName should throw IllegalArgumentException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					IllegalArgumentException.class, () -> Sindxdll.SpecName(invalidIndex),
					"SpecName should throw IllegalArgumentException for invalid index"
			);
		}

		@Test
		void testValidIndex() {
			short validIndex = 0;
			String expectedResult = "Aspen";
			String actualResult = Sindxdll.SpecName(validIndex);

			assertEquals(actualResult, expectedResult);
		}
	}

	@Nested
	class SpecUseTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.SpecUse(invalidIndex),
					"SpecUse should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.SpecUse(invalidIndex),
					"SpecUse should throw SpeciesErrorException for invalid index"
			);
		}

		private void testHelper(int inputIndex, int expectedValue) { // helper function to reduce repetive code
			short actualValue = Sindxdll.SpecUse((short) inputIndex);
			assertEquals((int) actualValue, expectedValue);
		}

		@Test
		void testSI_SPEC_A() {
			testHelper(SI_SPEC_A, 0x00);
		}

		@Test
		void testSI_SPEC_ABAL() {
			testHelper(SI_SPEC_ABAL, 0x00);
		}

		@Test
		void testSI_SPEC_ABCO() {
			testHelper(SI_SPEC_ABCO, 0x00);
		}

		@Test
		void testSI_SPEC_AC() {
			testHelper(SI_SPEC_AC, 0x04);
		}

		@Test
		void testSI_SPEC_ACB() {
			testHelper(SI_SPEC_ACB, 0x07);
		}

		@Test
		void testSI_SPEC_ACT() {
			testHelper(SI_SPEC_ACT, 0x04);
		}

		@Test
		void testSI_SPEC_AD() {
			testHelper(SI_SPEC_AD, 0x00);
		}

		@Test
		void testSI_SPEC_AH() {
			testHelper(SI_SPEC_AH, 0x00);
		}

		@Test
		void testSI_SPEC_AT() {
			testHelper(SI_SPEC_AT, 0x06);
		}

		@Test
		void testSI_SPEC_AX() {
			testHelper(SI_SPEC_AX, 0x00);
		}

		@Test
		void testSI_SPEC_B() {
			testHelper(SI_SPEC_B, 0x00);
		}

		@Test
		void testSI_SPEC_BA() {
			testHelper(SI_SPEC_BA, 0x05);
		}

		@Test
		void testSI_SPEC_BB() {
			testHelper(SI_SPEC_BB, 0x00);
		}

		@Test
		void testSI_SPEC_BC() {
			testHelper(SI_SPEC_BC, 0x00);
		}

		@Test
		void testSI_SPEC_BG() {
			testHelper(SI_SPEC_BG, 0x00);
		}

		@Test
		void testSI_SPEC_BI() {
			testHelper(SI_SPEC_BI, 0x00);
		}

		@Test
		void testSI_SPEC_BL() {
			testHelper(SI_SPEC_BL, 0x06);
		}

		@Test
		void testSI_SPEC_BM() {
			testHelper(SI_SPEC_BM, 0x00);
		}

		@Test
		void testSI_SPEC_BP() {
			testHelper(SI_SPEC_BP, 0x05);
		}

		@Test
		void testSI_SPEC_C() {
			testHelper(SI_SPEC_C, 0x00);
		}

		@Test
		void testSI_SPEC_CI() {
			testHelper(SI_SPEC_CI, 0x00);
		}

		@Test
		void testSI_SPEC_CP() {
			testHelper(SI_SPEC_CP, 0x00);
		}

		@Test
		void testSI_SPEC_CW() {
			testHelper(SI_SPEC_CW, 0x05);
		}

		@Test
		void testSI_SPEC_CWC() {
			testHelper(SI_SPEC_CWC, 0x05);
		}

		@Test
		void testSI_SPEC_CWI() {
			testHelper(SI_SPEC_CWI, 0x06);
		}

		@Test
		void testSI_SPEC_CY() {
			testHelper(SI_SPEC_CY, 0x01);
		}

		@Test
		void testSI_SPEC_D() {
			testHelper(SI_SPEC_D, 0x00);
		}

		@Test
		void testSI_SPEC_DG() {
			testHelper(SI_SPEC_DG, 0x00);
		}

		@Test
		void testSI_SPEC_DM() {
			testHelper(SI_SPEC_DM, 0x02);
		}

		@Test
		void testSI_SPEC_DR() {
			testHelper(SI_SPEC_DR, 0x05);
		}

		@Test
		void testSI_SPEC_E() {
			testHelper(SI_SPEC_E, 0x00);
		}

		@Test
		void testSI_SPEC_EA() {
			testHelper(SI_SPEC_EA, 0x02);
		}

		@Test
		void testSI_SPEC_EB() {
			testHelper(SI_SPEC_EB, 0x02);
		}

		@Test
		void testSI_SPEC_EE() {
			testHelper(SI_SPEC_EE, 0x02);
		}

		@Test
		void testSI_SPEC_EP() {
			testHelper(SI_SPEC_EP, 0x06);
		}

		@Test
		void testSI_SPEC_ES() {
			testHelper(SI_SPEC_ES, 0x02);
		}

		@Test
		void testSI_SPEC_EW() {
			testHelper(SI_SPEC_EW, 0x02);
		}

		@Test
		void testSI_SPEC_EXP() {
			testHelper(SI_SPEC_EXP, 0x02);
		}

		@Test
		void testSI_SPEC_FD() {
			testHelper(SI_SPEC_FD, 0x05);
		}

		@Test
		void testSI_SPEC_FDC() {
			testHelper(SI_SPEC_FDC, 0x05);
		}

		@Test
		void testSI_SPEC_FDI() {
			testHelper(SI_SPEC_FDI, 0x06);
		}

		@Test
		void testSI_SPEC_G() {
			testHelper(SI_SPEC_G, 0x01);
		}

		@Test
		void testSI_SPEC_GP() {
			testHelper(SI_SPEC_GP, 0x01);
		}

		@Test
		void testSI_SPEC_GR() {
			testHelper(SI_SPEC_GR, 0x01);
		}

		@Test
		void testSI_SPEC_H() {
			testHelper(SI_SPEC_H, 0x00);
		}

		@Test
		void testSI_SPEC_HM() {
			testHelper(SI_SPEC_HM, 0x05);
		}

		@Test
		void testSI_SPEC_HW() {
			testHelper(SI_SPEC_HW, 0x05);
		}

		@Test
		void testSI_SPEC_HWC() {
			testHelper(SI_SPEC_HWC, 0x05);
		}

		@Test
		void testSI_SPEC_HWI() {
			testHelper(SI_SPEC_HWI, 0x06);
		}

		@Test
		void testSI_SPEC_HXM() {
			testHelper(SI_SPEC_HXM, 0x00);
		}

		@Test
		void testSI_SPEC_IG() {
			testHelper(SI_SPEC_IG, 0x00);
		}

		@Test
		void testSI_SPEC_IS() {
			testHelper(SI_SPEC_IS, 0x00);
		}

		@Test
		void testSI_SPEC_J() {
			testHelper(SI_SPEC_J, 0x02);
		}

		@Test
		void testSI_SPEC_JR() {
			testHelper(SI_SPEC_JR, 0x02);
		}

		@Test
		void testSI_SPEC_K() {
			testHelper(SI_SPEC_K, 0x00);
		}

		@Test
		void testSI_SPEC_KC() {
			testHelper(SI_SPEC_KC, 0x00);
		}

		@Test
		void testSI_SPEC_L() {
			testHelper(SI_SPEC_L, 0x00);
		}

		@Test
		void testSI_SPEC_LA() {
			testHelper(SI_SPEC_LA, 0x02);
		}

		@Test
		void testSI_SPEC_LE() {
			testHelper(SI_SPEC_LE, 0x02);
		}

		@Test
		void testSI_SPEC_LT() {
			testHelper(SI_SPEC_LT, 0x02);
		}

		@Test
		void testSI_SPEC_LW() {
			testHelper(SI_SPEC_LW, 0x06);
		}

		@Test
		void testSI_SPEC_M() {
			testHelper(SI_SPEC_M, 0x00);
		}

		@Test
		void testSI_SPEC_MB() {
			testHelper(SI_SPEC_MB, 0x01);
		}

		@Test
		void testSI_SPEC_ME() {
			testHelper(SI_SPEC_ME, 0x00);
		}

		@Test
		void testSI_SPEC_MN() {
			testHelper(SI_SPEC_MN, 0x00);
		}

		@Test
		void testSI_SPEC_MR() {
			testHelper(SI_SPEC_MR, 0x00);
		}

		@Test
		void testSI_SPEC_MS() {
			testHelper(SI_SPEC_MS, 0x00);
		}

		@Test
		void testSI_SPEC_MV() {
			testHelper(SI_SPEC_MV, 0x00);
		}

		@Test
		void testSI_SPEC_OA() {
			testHelper(SI_SPEC_OA, 0x00);
		}

		@Test
		void testSI_SPEC_OB() {
			testHelper(SI_SPEC_OB, 0x00);
		}

		@Test
		void testSI_SPEC_OC() {
			testHelper(SI_SPEC_OC, 0x00);
		}

		@Test
		void testSI_SPEC_OD() {
			testHelper(SI_SPEC_OD, 0x00);
		}

		@Test
		void testSI_SPEC_OE() {
			testHelper(SI_SPEC_OE, 0x00);
		}

		@Test
		void testSI_SPEC_OF() {
			testHelper(SI_SPEC_OF, 0x00);
		}

		@Test
		void testSI_SPEC_OG() {
			testHelper(SI_SPEC_OG, 0x00);
		}

		@Test
		void testSI_SPEC_P() {
			testHelper(SI_SPEC_P, 0x02);
		}

		@Test
		void testSI_SPEC_PA() {
			testHelper(SI_SPEC_PA, 0x02);
		}

		@Test
		void testSI_SPEC_PF() {
			testHelper(SI_SPEC_PF, 0x02);
		}

		@Test
		void testSI_SPEC_PJ() {
			testHelper(SI_SPEC_PJ, 0x02);
		}

		@Test
		void testSI_SPEC_PL() {
			testHelper(SI_SPEC_PL, 0x06);
		}

		@Test
		void testSI_SPEC_PLC() {
			testHelper(SI_SPEC_PLC, 0x01);
		}

		@Test
		void testSI_SPEC_PLI() {
			testHelper(SI_SPEC_PLI, 0x06);
		}

		@Test
		void testSI_SPEC_PM() {
			testHelper(SI_SPEC_PM, 0x00);
		}

		@Test
		void testSI_SPEC_PR() {
			testHelper(SI_SPEC_PR, 0x00);
		}

		@Test
		void testSI_SPEC_PS() {
			testHelper(SI_SPEC_PS, 0x00);
		}

		@Test
		void testSI_SPEC_PW() {
			testHelper(SI_SPEC_PW, 0x04);
		}

		@Test
		void testSI_SPEC_PXJ() {
			testHelper(SI_SPEC_PXJ, 0x02);
		}

		@Test
		void testSI_SPEC_PY() {
			testHelper(SI_SPEC_PY, 0x06);
		}

		@Test
		void testSI_SPEC_Q() {
			testHelper(SI_SPEC_Q, 0x00);
		}

		@Test
		void testSI_SPEC_QE() {
			testHelper(SI_SPEC_QE, 0x00);
		}

		@Test
		void testSI_SPEC_QG() {
			testHelper(SI_SPEC_QG, 0x01);
		}

		@Test
		void testSI_SPEC_R() {
			testHelper(SI_SPEC_R, 0x01);
		}

		@Test
		void testSI_SPEC_RA() {
			testHelper(SI_SPEC_RA, 0x01);
		}

		@Test
		void testSI_SPEC_S() {
			testHelper(SI_SPEC_S, 0x00);
		}

		@Test
		void testSI_SPEC_SA() {
			testHelper(SI_SPEC_SA, 0x02);
		}

		@Test
		void testSI_SPEC_SB() {
			testHelper(SI_SPEC_SB, 0x06);
		}

		@Test
		void testSI_SPEC_SE() {
			testHelper(SI_SPEC_SE, 0x06);
		}

		@Test
		void testSI_SPEC_SI() {
			testHelper(SI_SPEC_SI, 0x02);
		}

		@Test
		void testSI_SPEC_SN() {
			testHelper(SI_SPEC_SN, 0x02);
		}

		@Test
		void testSI_SPEC_SS() {
			testHelper(SI_SPEC_SS, 0x05);
		}

		@Test
		void testSI_SPEC_SW() {
			testHelper(SI_SPEC_SW, 0x06);
		}

		@Test
		void testSI_SPEC_SX() {
			testHelper(SI_SPEC_SX, 0x06);
		}

		@Test
		void testSI_SPEC_SXB() {
			testHelper(SI_SPEC_SXB, 0x02);
		}

		@Test
		void testSI_SPEC_SXE() {
			testHelper(SI_SPEC_SXE, 0x01);
		}

		@Test
		void testSI_SPEC_SXL() {
			testHelper(SI_SPEC_SXL, 0x01);
		}

		@Test
		void testSI_SPEC_SXS() {
			testHelper(SI_SPEC_SXS, 0x01);
		}

		@Test
		void testSI_SPEC_SXW() {
			testHelper(SI_SPEC_SXW, 0x02);
		}

		@Test
		void testSI_SPEC_SXX() {
			testHelper(SI_SPEC_SXX, 0x02);
		}

		@Test
		void testSI_SPEC_T() {
			testHelper(SI_SPEC_T, 0x00);
		}

		@Test
		void testSI_SPEC_TW() {
			testHelper(SI_SPEC_TW, 0x00);
		}

		@Test
		void testSI_SPEC_U() {
			testHelper(SI_SPEC_U, 0x00);
		}

		@Test
		void testSI_SPEC_UA() {
			testHelper(SI_SPEC_UA, 0x00);
		}

		@Test
		void testSI_SPEC_UP() {
			testHelper(SI_SPEC_UP, 0x00);
		}

		@Test
		void testSI_SPEC_V() {
			testHelper(SI_SPEC_V, 0x00);
		}

		@Test
		void testSI_SPEC_VB() {
			testHelper(SI_SPEC_VB, 0x00);
		}

		@Test
		void testSI_SPEC_VP() {
			testHelper(SI_SPEC_VP, 0x00);
		}

		@Test
		void testSI_SPEC_VS() {
			testHelper(SI_SPEC_VS, 0x00);
		}

		@Test
		void testSI_SPEC_VV() {
			testHelper(SI_SPEC_VV, 0x00);
		}

		@Test
		void testSI_SPEC_W() {
			testHelper(SI_SPEC_W, 0x00);
		}

		@Test
		void testSI_SPEC_WA() {
			testHelper(SI_SPEC_WA, 0x00);
		}

		@Test
		void testSI_SPEC_WB() {
			testHelper(SI_SPEC_WB, 0x00);
		}

		@Test
		void testSI_SPEC_WD() {
			testHelper(SI_SPEC_WD, 0x00);
		}

		@Test
		void testSI_SPEC_WI() {
			testHelper(SI_SPEC_WI, 0x00);
		}

		@Test
		void testSI_SPEC_WP() {
			testHelper(SI_SPEC_WP, 0x00);
		}

		@Test
		void testSI_SPEC_WS() {
			testHelper(SI_SPEC_WS, 0x00);
		}

		@Test
		void testSI_SPEC_WT() {
			testHelper(SI_SPEC_WT, 0x00);
		}

		@Test
		void testSI_SPEC_X() {
			testHelper(SI_SPEC_X, 0x00);
		}

		@Test
		void testSI_SPEC_XC() {
			testHelper(SI_SPEC_XC, 0x00);
		}

		@Test
		void testSI_SPEC_XH() {
			testHelper(SI_SPEC_XH, 0x00);
		}

		@Test
		void testSI_SPEC_Y() {
			testHelper(SI_SPEC_Y, 0x00);
		}

		@Test
		void testSI_SPEC_YC() {
			testHelper(SI_SPEC_YC, 0x01);
		}

		@Test
		void testSI_SPEC_YP() {
			testHelper(SI_SPEC_YP, 0x00);
		}

		@Test
		void testSI_SPEC_Z() {
			testHelper(SI_SPEC_Z, 0x00);
		}

		@Test
		void testSI_SPEC_ZC() {
			testHelper(SI_SPEC_ZC, 0x00);
		}

		@Test
		void testSI_SPEC_ZH() {
			testHelper(SI_SPEC_ZH, 0x00);
		}
	}

	@Nested
	class DefCurveTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefCurve(invalidIndex),
					"DefCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefCurve(invalidIndex),
					"DefCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testLastSpeciesIndex() {
			short lastIndex = 134; // Choose the last index for testing
			assertThrows(
					NoAnswerException.class, () -> Sindxdll.DefCurve(lastIndex),
					"DefCurve should throw NoAnswerException for last index"
			);
		}

		@Test
		void testValidIndex() {
			short validIndex = 0;
			short expectValue = -4; // SI_ERR_NO_ANS
			short actualOutput = Sindxdll.DefCurve(validIndex);

			assertEquals(actualOutput, expectValue);
		}
	}

	@Nested
	class DefGICurveTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefGICurve(invalidIndex),
					"DefGICurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefGICurve(invalidIndex),
					"DefGICurve should throw SpeciesErrorException for invalid index"
			);
		}

		private void testHelper(int inputIndex, int expectedValue) { // helper function to reduce repetive
																		// code
			short actualValue = Sindxdll.DefGICurve((short) inputIndex);
			assertEquals(actualValue, expectedValue);
		}

		@Test
		void testSI_SPEC_BA() {
			testHelper(SI_SPEC_BA, SI_BA_NIGHGI);
		}

		@Test
		void testBL() {
			testHelper(SI_SPEC_BL, SI_BL_THROWERGI);
		}

		@Test
		void testCWI() {
			testHelper(SI_SPEC_CWI, SI_CWI_NIGHGI);
		}

		@Test
		void testFDC() {
			testHelper(SI_SPEC_FDC, SI_FDC_NIGHGI);
		}

		@Test
		void testFDI() {
			testHelper(SI_SPEC_FDI, SI_FDI_NIGHGI);
		}

		@Test
		void testHWC() {
			testHelper(SI_SPEC_HWC, SI_HWC_NIGHGI99);
		}

		@Test
		void testHWI() {
			testHelper(SI_SPEC_HWI, SI_HWI_NIGHGI);
		}

		@Test
		void testLW() {
			testHelper(SI_SPEC_LW, SI_LW_NIGHGI);
		}

		@Test
		void testPLI() {
			testHelper(SI_SPEC_PLI, SI_PLI_NIGHGI97);
		}

		@Test
		void testPY() {
			testHelper(SI_SPEC_PY, SI_PY_NIGHGI);
		}

		@Test
		void testSE() {
			testHelper(SI_SPEC_SE, SI_SE_NIGHGI);
		}

		@Test
		void testSS() {
			testHelper(SI_SPEC_SS, SI_SS_NIGHGI99);
		}

		@Test
		void testSW() {
			testHelper(SI_SPEC_SW, SI_SW_NIGHGI2004);
		}

		@Test
		void testIndexNotInSwitch() {
			short invalidIndex = 10; // Choose an index that could feasibly exist but isn't in the switch for testing
			assertThrows(
					NoAnswerException.class, () -> Sindxdll.DefGICurve(invalidIndex),
					"DefGICurve should throw NoAnswerException for invalid index"
			);
		}
	}

	@Nested
	class DefCurveEstTest {
		@BeforeAll
		static void beforeAll() {
			System.out.println("Before all tests of DefCurveEst");
		}

		@AfterAll
		static void afterAll() {
			System.out.println("After all tests of DefCurveEst");
		}

		@Test
		void testEstTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefCurveEst(invalidIndex, (short) 0),
					"DefCurveEst should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.DefCurveEst(invalidIndex, (short) 0),
					"DefGICurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testInvalidSpeciesIndex() {
			assertThrows(SpeciesErrorException.class, () -> {
				Sindxdll.DefCurveEst((short) SI_MAX_SPECIES, (short) SI_ESTAB_NAT);
			});
		}

		@Test
		void testValidSpeciesIndexAndEstabSI_ESTAB_NAT() {
			short result = Sindxdll.DefCurveEst((short) 100, (short) SI_ESTAB_NAT);
			assertEquals(SI_SW_GOUDIE_NATAC, result);
		}

		@Test
		void testValidSpeciesIndexAndEstabSI_ESTAB_PLA() {
			short result = Sindxdll.DefCurveEst((short) 100, (short) SI_ESTAB_PLA);
			assertEquals(SI_SW_GOUDIE_PLAAC, result);
		}

		@Test
		void testInvalidEstablishment() {
			assertThrows(EstablishmentErrorException.class, () -> {
				Sindxdll.DefCurveEst((short) SI_SPEC_SW, (short) -1);
			});
		}

		@Test
		void testNoCurvesDefined() {
			assertThrows(NoAnswerException.class, () -> {
				Sindxdll.DefCurveEst((short) 1, (short) SI_ESTAB_NAT);
			});
		}

		@Test
		void testDefaultCase() {
			short result = Sindxdll.DefCurveEst((short) 4, (short) 0);
			assertEquals(SI_ACB_HUANGAC, result);
		}
	}

	@Nested
	class FirstCurveTest {
		@Test
		void testTooSmallIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.FirstCurve(invalidIndex),
					"FirstCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.FirstCurve(invalidIndex),
					"FirstCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testNoCurvesDefined() {
			short invalidIndex = 0;
			assertThrows(
					NoAnswerException.class, () -> Sindxdll.FirstCurve(invalidIndex),
					"FirstCurve should throw NoAnswerException for invalid index"
			);
		}

		@Test
		void testDefaultCase() {
			short validIndex = 4;
			short result = Sindxdll.FirstCurve(validIndex);
			assertEquals(result, 97); // SI_ACB_START
		}
	}

	@Nested
	class NextCurveTest {
		@Test
		void testTooSmallSPIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.NextCurve(invalidIndex, (short) 0),
					"NextCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooBigSPIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					SpeciesErrorException.class, () -> Sindxdll.NextCurve(invalidIndex, (short) 0),
					"NextCurve should throw SpeciesErrorException for invalid index"
			);
		}

		@Test
		void testTooSmallCUIndex() {
			short invalidIndex = -1; // Choose an invalid index for testing
			assertThrows(
					CurveErrorException.class, () -> Sindxdll.NextCurve((short) 0, invalidIndex),
					"NextCurve should throw CurveErrorException for invalid index"
			);
		}

		@Test
		void testTooBigCUIndex() {
			short invalidIndex = SI_MAX_CURVES; // Choose an invalid index for testing
			assertThrows(
					CurveErrorException.class, () -> Sindxdll.NextCurve((short) 0, invalidIndex),
					"NextCurve should throw CurveErrorException for invalid index"
			);
		}

		@Test
		void testCurveSpeciesMismatch() {
			short spIndex = 0;
			short cuIndex = 0;
			assertThrows(CurveErrorException.class, () -> Sindxdll.NextCurve(spIndex, cuIndex));
		}

		@Test
		void testSI_ACB_HUANGAC() {
			short sp_index = SI_SPEC_ACB;
			short cu_index = SI_ACB_HUANGAC;

			short expectResut = SI_ACB_HUANG;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_ACB_HUANG() {
			short cu_index = SI_ACB_HUANG;
			short sp_index = SI_SPEC_ACB;
			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_ACT_THROWERAC() {
			short sp_index = SI_SPEC_ACT;
			short cu_index = SI_ACT_THROWERAC;

			short expectResut = SI_ACT_THROWER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_ACT_THROWER() {
			short sp_index = SI_SPEC_ACT;
			short cu_index = SI_ACT_THROWER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_AT_NIGH() {
			short sp_index = SI_SPEC_AT;
			short cu_index = SI_AT_NIGH;

			short expectResut = SI_AT_CHEN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_AT_CHEN() {
			short sp_index = SI_SPEC_AT;
			short cu_index = SI_AT_CHEN;

			short expectResut = SI_AT_HUANG;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_AT_HUANG() {
			short sp_index = SI_SPEC_AT;
			short cu_index = SI_AT_HUANG;

			short expectResut = SI_AT_CIESZEWSKI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_AT_CIESZEWSKI() {
			short sp_index = SI_SPEC_AT;
			short cu_index = SI_AT_CIESZEWSKI;

			short expectResut = SI_AT_GOUDIE;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_AT_GOUDIE() {
			short sp_index = SI_SPEC_AT;
			short index = SI_AT_GOUDIE;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, index));
		}

		@Test
		void testSI_BA_NIGHGI() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_NIGHGI;

			short expectResut = SI_BA_NIGH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_NIGH() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_NIGH;

			short expectResut = SI_BA_KURUCZ82AC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_KURUCZ82AC() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_KURUCZ82AC;

			short expectResut = SI_BA_DILUCCA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_DILUCCA() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_DILUCCA;

			short expectResut = SI_BA_KURUCZ86;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_KURUCZ86() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_KURUCZ86;

			short expectResut = SI_BA_KURUCZ82;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BA_KURUCZ82() {
			short sp_index = SI_SPEC_BA;
			short cu_index = SI_BA_KURUCZ82;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_BL_CHENAC() {
			short sp_index = SI_SPEC_BL;
			short cu_index = SI_BL_CHENAC;

			short expectResut = SI_BL_CHEN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BL_CHEN() {
			short sp_index = SI_SPEC_BL;
			short cu_index = SI_BL_CHEN;

			short expectResut = SI_BL_THROWERGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BL_THROWERGI() {
			short sp_index = SI_SPEC_BL;
			short cu_index = SI_BL_THROWERGI;

			short expectResut = SI_BL_KURUCZ82;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BL_KURUCZ82() {
			short sp_index = SI_SPEC_BL;
			short cu_index = SI_BL_KURUCZ82;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_BP_CURTISAC() {
			short sp_index = SI_SPEC_BP;
			short cu_index = SI_BP_CURTISAC;

			short expectResut = SI_BP_CURTIS;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_BP_CURTIS() {
			short sp_index = SI_SPEC_BP;
			short cu_index = SI_BP_CURTIS;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_CWC_NIGH() {
			short sp_index = SI_SPEC_CWC;
			short cu_index = SI_CWC_NIGH;

			short expectResut = SI_CWC_KURUCZAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_CWC_KURUCZAC() {
			short sp_index = SI_SPEC_CWC;
			short cu_index = SI_CWC_KURUCZAC;

			short expectResut = SI_CWC_KURUCZ;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_CWC_KURUCZ() {
			short sp_index = SI_SPEC_CWC;
			short cu_index = SI_CWC_KURUCZ;

			short expectResut = SI_CWC_BARKER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_CWC_BARKER() {
			short sp_index = SI_SPEC_CWC;
			short cu_index = SI_CWC_BARKER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_CWI_NIGH() {
			short sp_index = SI_SPEC_CWI;
			short cu_index = SI_CWI_NIGH;

			short expectResut = SI_CWI_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_CWI_NIGHGI() {
			short sp_index = SI_SPEC_CWI;
			short cu_index = SI_CWI_NIGHGI;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_DR_NIGH() {
			short sp_index = SI_SPEC_DR;
			short cu_index = SI_DR_NIGH;

			short expectResut = SI_DR_HARRING;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_DR_HARRING() {
			short sp_index = SI_SPEC_DR;
			short cu_index = SI_DR_HARRING;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_EP_NIGH() {
			short sp_index = SI_SPEC_EP;
			short cu_index = SI_EP_NIGH;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_FDC_BRUCEAC() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_BRUCEAC;

			short expectResut = SI_FDC_NIGHTA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_NIGHTA() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_NIGHTA;

			short expectResut = SI_FDC_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_NIGHGI() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_NIGHGI;

			short expectResut = SI_FDC_BRUCE;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_BRUCE() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_BRUCE;

			short expectResut = SI_FDC_COCHRAN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_COCHRAN() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_COCHRAN;

			short expectResut = SI_FDC_KING;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDC_KING() {
			short sp_index = SI_SPEC_FDC;
			short cu_index = SI_FDC_KING;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_FDI_THROWERAC() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_THROWERAC;

			short expectResut = SI_FDI_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_NIGHGI() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_NIGHGI;

			short expectResut = SI_FDI_HUANG_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_HUANG_PLA() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_HUANG_PLA;

			short expectResut = SI_FDI_HUANG_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_HUANG_NAT() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_HUANG_NAT;

			short expectResut = SI_FDI_MILNER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MILNER() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MILNER;

			short expectResut = SI_FDI_THROWER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_THROWER() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_THROWER;

			short expectResut = SI_FDI_VDP_MONT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_VDP_MONT() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_VDP_MONT;

			short expectResut = SI_FDI_VDP_WASH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_VDP_WASH() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_VDP_WASH;

			short expectResut = SI_FDI_MONS_DF;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_DF() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_DF;

			short expectResut = SI_FDI_MONS_GF;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_GF() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_GF;

			short expectResut = SI_FDI_MONS_WRC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_WRC() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_WRC;

			short expectResut = SI_FDI_MONS_WH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_WH() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_WH;

			short expectResut = SI_FDI_MONS_SAF;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_FDI_MONS_SAF() {
			short sp_index = SI_SPEC_FDI;
			short cu_index = SI_FDI_MONS_SAF;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_HM_MEANSAC() {
			short sp_index = SI_SPEC_HM;
			short cu_index = SI_HM_MEANSAC;

			short expectResut = SI_HM_MEANS;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HM_MEANS() {
			short sp_index = SI_SPEC_HM;
			short cu_index = SI_HM_MEANS;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_HWC_WILEYAC() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_WILEYAC;

			short expectResut = SI_HWC_NIGHGI99;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_NIGHGI99() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_NIGHGI99;

			short expectResut = SI_HWC_FARR;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_FARR() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_FARR;

			short expectResut = SI_HWC_BARKER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_BARKER() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_BARKER;

			short expectResut = SI_HWC_WILEY;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_WILEY() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_WILEY;

			short expectResut = SI_HWC_WILEY_BC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_WILEY_BC() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_WILEY_BC;

			short expectResut = SI_HWC_WILEY_MB;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWC_WILEY_MB() {
			short sp_index = SI_SPEC_HWC;
			short cu_index = SI_HWC_WILEY_MB;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_HWI_NIGH() {
			short sp_index = SI_SPEC_HWI;
			short cu_index = SI_HWI_NIGH;

			short expectResut = SI_HWI_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_HWI_NIGHGI() {
			short sp_index = SI_SPEC_HWI;
			short cu_index = SI_HWI_NIGHGI;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_LW_NIGH() {
			short sp_index = SI_SPEC_LW;
			short cu_index = SI_LW_NIGH;

			short expectResut = SI_LW_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_LW_NIGHGI() {
			short sp_index = SI_SPEC_LW;
			short cu_index = SI_LW_NIGHGI;

			short expectResut = SI_LW_MILNER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_LW_MILNER() {
			short sp_index = SI_SPEC_LW;
			short cu_index = SI_LW_MILNER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_PJ_HUANG() {
			short sp_index = SI_SPEC_PJ;
			short cu_index = SI_PJ_HUANG;

			short expectResut = SI_PJ_HUANGAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PJ_HUANGAC() {
			short sp_index = SI_SPEC_PJ;
			short cu_index = SI_PJ_HUANGAC;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_PL_CHEN() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PL_CHEN;

			short expectResut = SI_PLI_THROWNIGH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_THROWNIGH() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_THROWNIGH;

			short expectResut = SI_PLI_NIGHTA98;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_NIGHTA98() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_NIGHTA98;

			short expectResut = SI_PLI_NIGHGI97;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_NIGHGI97() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_NIGHGI97;

			short expectResut = SI_PLI_HUANG_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_HUANG_PLA() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_HUANG_PLA;

			short expectResut = SI_PLI_HUANG_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_HUANG_NAT() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_HUANG_NAT;

			short expectResut = SI_PLI_THROWER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_THROWER() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_THROWER;

			short expectResut = SI_PLI_MILNER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_MILNER() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_MILNER;

			short expectResut = SI_PLI_CIESZEWSKI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_CIESZEWSKI() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_CIESZEWSKI;

			short expectResut = SI_PLI_GOUDIE_DRY;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_GOUDIE_DRY() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_GOUDIE_DRY;

			short expectResut = SI_PLI_GOUDIE_WET;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_GOUDIE_WET() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_GOUDIE_WET;

			short expectResut = SI_PLI_DEMPSTER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PLI_DEMPSTER() {
			short sp_index = SI_SPEC_PLI;
			short cu_index = SI_PLI_DEMPSTER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_PW_CURTISAC() {
			short sp_index = SI_SPEC_PW;
			short cu_index = SI_PW_CURTISAC;

			short expectResut = SI_PW_CURTIS;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PW_CURTIS() {
			short sp_index = SI_SPEC_PW;
			short cu_index = SI_PW_CURTIS;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_PY_NIGH() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_NIGH;

			short expectResut = SI_PY_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PY_NIGHGI() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_NIGHGI;

			short expectResut = SI_PY_HANNAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PY_HANNAC() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_HANNAC;

			short expectResut = SI_PY_MILNER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PY_MILNER() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_MILNER;

			short expectResut = SI_PY_HANN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_PY_HANN() {
			short sp_index = SI_SPEC_PY;
			short cu_index = SI_PY_HANN;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_SB_NIGH() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_NIGH;

			short expectResut = SI_SB_HUANG;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SB_HUANG() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_HUANG;

			short expectResut = SI_SB_CIESZEWSKI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SB_CIESZEWSKI() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_CIESZEWSKI;

			short expectResut = SI_SB_KER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SB_KER() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_KER;

			short expectResut = SI_SB_DEMPSTER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SB_DEMPSTER() {
			short sp_index = SI_SPEC_SB;
			short cu_index = SI_SB_DEMPSTER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_SE_CHENAC() {
			short sp_index = SI_SPEC_SE;
			short cu_index = SI_SE_CHENAC;

			short expectResut = SI_SE_CHEN;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SE_CHEN() {
			short sp_index = SI_SPEC_SE;
			short cu_index = SI_SE_CHEN;

			short expectResut = SI_SE_NIGHGI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SE_NIGHGI() {
			short sp_index = SI_SPEC_SE;
			short cu_index = SI_SE_NIGHGI;

			short expectResut = SI_SE_NIGH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SE_NIGH() {
			short sp_index = SI_SPEC_SE;
			short cu_index = SI_SE_NIGH;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_SS_NIGHGI99() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_NIGHGI99;

			short expectResut = SI_SS_NIGH;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SS_NIGH() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_NIGH;

			short expectResut = SI_SS_GOUDIE;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SS_GOUDIE() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_GOUDIE;

			short expectResut = SI_SS_FARR;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SS_FARR() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_FARR;

			short expectResut = SI_SS_BARKER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SS_BARKER() {
			short sp_index = SI_SPEC_SS;
			short cu_index = SI_SS_BARKER;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}

		@Test
		void testSI_SW_GOUDNIGH() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDNIGH;

			short expectResut = SI_SW_HU_GARCIA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_HU_GARCIA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_HU_GARCIA;

			short expectResut = SI_SW_NIGHTA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_NIGHTA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_NIGHTA;

			short expectResut = SI_SW_NIGHGI2004;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_NIGHGI2004() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_NIGHGI2004;

			short expectResut = SI_SW_HUANG_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_HUANG_PLA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_HUANG_PLA;

			short expectResut = SI_SW_HUANG_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_HUANG_NAT() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_HUANG_NAT;

			short expectResut = SI_SW_THROWER;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_THROWER() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_THROWER;

			short expectResut = SI_SW_CIESZEWSKI;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_CIESZEWSKI() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_CIESZEWSKI;

			short expectResut = SI_SW_KER_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_KER_PLA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_KER_PLA;

			short expectResut = SI_SW_KER_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_KER_NAT() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_KER_NAT;

			short expectResut = SI_SW_GOUDIE_PLAAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_GOUDIE_PLAAC() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDIE_PLAAC;

			short expectResut = SI_SW_GOUDIE_PLA;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_GOUDIE_PLA() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDIE_PLA;

			short expectResut = SI_SW_GOUDIE_NATAC;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_GOUDIE_NATAC() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDIE_NATAC;

			short expectResut = SI_SW_GOUDIE_NAT;
			short actualResult = Sindxdll.NextCurve(sp_index, cu_index);

			assertEquals(actualResult, expectResut);
		}

		@Test
		void testSI_SW_GOUDIE_NAT() {
			short sp_index = SI_SPEC_SW;
			short cu_index = SI_SW_GOUDIE_NAT;

			assertThrows(NoAnswerException.class, () -> Sindxdll.NextCurve(sp_index, cu_index));
		}
	}

	@Nested
	class CurveNameTest {

		@Test
		void testInvalidIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					CurveErrorException.class, () -> Sindxdll.CurveName(invalidIndex),
					"CurveName should throw IllegalArgumentException for invalid index"
			);
		}

		@Test
		void testValidIndex() {
			short validIndex = 0;
			String expectedResult = "Huang, Titus, and Lakusta (1994)";
			String actualResult = Sindxdll.CurveName(validIndex);

			assertEquals(actualResult, expectedResult);
		}
	}

	@Nested
	class CurveUseTest {
		@Test
		void testInvalidIndex() {
			short invalidIndex = 135; // Choose an invalid index for testing
			assertThrows(
					CurveErrorException.class, () -> Sindxdll.CurveUse(invalidIndex),
					"CurveUse should throw IllegalArgumentException for invalid index"
			);
		}

		@Test
		void testValidIndex() {
			short validIndex = 0;
			short expectedResult = 5;
			short actualResult = Sindxdll.CurveUse(validIndex);

			assertEquals(actualResult, expectedResult);
		}
	}

	@Nested
	class HtAgeToSITest {
		@Test
		void testHtAgeToSIError() {
			double[] site = new double[2];
			assertThrows(
					LessThan13Exception.class, () -> Sindxdll.HtAgeToSI((short) 0, 0.0, (short) 1, 1.2, (short) 0, site)
			);
		}

		@Test
		void testHtAgeToSIValid() {
			double[] site = new double[2];
			double height = 2;
			double age = 1;

			double expectedResult = 0;
			double actualResult = Sindxdll.HtAgeToSI((short) SI_FDI_THROWER, age, (short) 1, height, (short) 1, site);
			double expectedSiteValue = 0.39 + 0.3104 * height + 33.3828 * height / age;

			assertEquals(actualResult, expectedResult);
			assertEquals(site[0], expectedSiteValue, ERROR_TOLERANCE);
		}
	}

	@Nested
	class HtSIToAgeTest {
		@Test
		void testHtSIToAgeError() {
			double[] site = new double[2];
			assertThrows(
					LessThan13Exception.class, () -> Sindxdll.HtSIToAge((short) 0, 0.0, (short) 1, 1.2, (short) 0, site)
			);
		}

		@Test
		void testHtSIToAgeValid() {
			double[] site = new double[2];
			double site_height = 1.5;
			double site_index = 25.0;

			double y2bh = 13.25 - site_index / 6.096;
			double x1 = site_index / 30.48;
			double x2 = -0.477762 + x1 * (-0.894427 + x1 * (0.793548 - x1 * 0.171666));
			double x3 = SiteIndex2Age.ppow(50.0 + y2bh, x2);
			double x4 = SiteIndex2Age.llog(1.372 / site_index) / (SiteIndex2Age.ppow(y2bh, x2) - x3);
			x1 = SiteIndex2Age.llog(site_height / site_index) / x4 + x3;

			double actualResult = Sindxdll
					.HtSIToAge((short) SI_FDC_BRUCE, site_height, (short) 1, site_index, 12.0, site);
			double expectedSiteValue = (SiteIndex2Age.ppow(x1, 1 / x2)) - y2bh;
			double expectedResult = 0;

			assertEquals(actualResult, expectedResult);
			assertEquals(site[0], expectedSiteValue, ERROR_TOLERANCE);
		}
	}

	@Nested
	class AgeSIToHtTest {
		@Test
		void testAgeSIToHtError() {
			double[] site = new double[2];
			assertThrows(
					LessThan13Exception.class, () -> Sindxdll.AgeSIToHt((short) 0, 0.0, (short) 1, 1.2, (short) 0, site)
			);
		}

		@Test
		void testAgeSIToHtValid() {
			double[] site = new double[2];

			double actualResult = Sindxdll.AgeSIToHt((short) SI_HWC_WILEY, 0.0, (short) 1, 1.31, 1.0, site);
			double expectedSiteValue = 1.37;
			double expectedResult = 0;

			assertEquals(actualResult, expectedResult);
			assertEquals(site[0], expectedSiteValue, ERROR_TOLERANCE);
		}
	}

	@Nested
	class AgeSIToHtSmoothTest {
		// TODO Empty
	}

	@Nested
	class Y2BHTest {
		@Test
		void testY2BHError() {
			double[] site = new double[2];
			assertThrows(LessThan13Exception.class, () -> Sindxdll.Y2BH((short) 0, 1.0, site));
		}

		@Test
		void testY2BHValid() {
			double[] y2bh = new double[2];
			short cu_index = SI_FDC_BRUCE;
			double site_index = 1.3;// normal case

			double expectedY2BHValue = 13.25 - site_index / 6.096;
			double actualResult = Sindxdll.Y2BH(cu_index, site_index, y2bh);
			double expectedResult = 0;

			assertEquals(actualResult, expectedResult);
			assertEquals(y2bh[0], expectedY2BHValue, ERROR_TOLERANCE);
		}

		@Test
		void testY2BH05Error() {
			double[] site = new double[2];
			assertThrows(LessThan13Exception.class, () -> Sindxdll.Y2BH05((short) 0, 1.0, site));
		}

		@Test
		void testY2BH05Valid() {
			double[] y2bh = new double[2];
			short cu_index = SI_PW_CURTIS;
			double site_index = 3.5;

			double expectedY2BHValue = ((int) (2.0 + 2.1578 + 110.76 / site_index)) + 0.5;
			double actualResult = Sindxdll.Y2BH05(cu_index, site_index, y2bh);
			double expectedResult = 0;

			assertEquals(actualResult, expectedResult);
			assertEquals(y2bh[0], expectedY2BHValue, ERROR_TOLERANCE);
		}
	}

	@Nested
	class SIToSITest {
		@Test
		void testValidConversion() {
			double[] site2 = new double[1];
			short result = Sindxdll.SIToSI((short) SI_SPEC_AT, 10.0, (short) SI_SPEC_SW, site2);
			assertEquals(0, result);
			assertEquals(11.782, site2[0], ERROR_TOLERANCE); // Make sure the value is close enough
		}

		@Test
		void testInvalidSourceSpeciesIndex() {
			double[] site2 = new double[1];
			assertThrows(SpeciesErrorException.class, () -> {
				Sindxdll.SIToSI((short) -1, 10.0, (short) 2, site2);
			});
			assertEquals(SI_ERR_SPEC, site2[0]);

			site2[0] = 100;
			assertThrows(SpeciesErrorException.class, () -> {
				Sindxdll.SIToSI((short) 135, 10.0, (short) 2, site2);
			});
			assertEquals(SI_ERR_SPEC, site2[0]);
		}

		@Test
		void testInvalidTargetSpeciesIndex() {
			double[] site2 = new double[1];
			assertThrows(SpeciesErrorException.class, () -> {
				Sindxdll.SIToSI((short) 1, 10.0, (short) -1, site2);
			});
			assertEquals(SI_ERR_SPEC, site2[0]);

			site2[0] = 100;
			assertThrows(SpeciesErrorException.class, () -> {
				Sindxdll.SIToSI((short) 1, 10.0, (short) 135, site2);
			});
			assertEquals(SI_ERR_SPEC, site2[0]);
		}

		@Test
		void testNoConversionDefined() {
			double[] site2 = new double[1];
			assertThrows(NoAnswerException.class, () -> {
				Sindxdll.SIToSI((short) SI_SPEC_AT, 10.0, (short) SI_SPEC_AT, site2);
			});
			assertEquals(SI_ERR_NO_ANS, site2[0]);
		}
	}

	@Nested
	class SCToSITest {
		@Test
		void testSCToSIError() {
			double[] site = new double[1];
			assertThrows(ClassErrorException.class, () -> Sindxdll.SCToSI((short) SI_SPEC_ACT, 'A', 'A', site));
		}

		@Test
		void testSCToSIValid() {
			double[] site = new double[1];
			short expectedResult = 0;
			short expectedSiteValue = 26;
			short actualResult = Sindxdll.SCToSI((short) SI_SPEC_MB, 'G', 'A', site);

			assertEquals(actualResult, expectedResult);
			assertEquals(site[0], expectedSiteValue);
		}

	}

	@Nested
	class SpecMapTest {
		// TODO Empty
	}

	@Nested
	class SpecRemapTest {
		// TODO Empty
	}

	@Nested
	class CurveSourceTest {
		@Test
		void testInvalidCurveIndex() {
			assertThrows(IllegalArgumentException.class, () -> Sindxdll.CurveSource((short) -1));

			assertThrows(IllegalArgumentException.class, () -> Sindxdll.CurveSource((short) 140));
		}

		boolean testHelperFunction(short cu_index, short search_index) {
			String actualResult = Sindxdll.CurveSource(cu_index);
			String expectedResult = si_curve_notes[search_index][0];

			return actualResult.equals(expectedResult);
		}

		@Test
		void testSI_BA_NIGH() {
			short cu_index = SI_BA_NIGH;
			short search_index = SI_BA_NIGHGI;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_CWI_NIGHGI() {
			short cu_index = SI_CWI_NIGHGI;
			short search_index = SI_CWI_NIGH;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_AT_HUANG() {
			short[] cu_indices = { SI_AT_HUANG, SI_SB_HUANG, SI_FDI_HUANG_PLA, SI_FDI_HUANG_NAT, SI_PLI_HUANG_PLA,
					SI_PLI_HUANG_NAT, SI_SW_HUANG_PLA, SI_SW_HUANG_NAT };
			short cu_index = SI_ACB_HUANG;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_PLI_CIESZEWSKI_SI_SB_CIESZEWSKI_SI_SW_CIESZEWSKI() {
			short[] cu_indices = { SI_PLI_CIESZEWSKI, SI_SB_CIESZEWSKI, SI_SW_CIESZEWSKI };
			short cu_index = SI_AT_CIESZEWSKI;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_PLI_DEMPSTER_SB_DEMPSTER_SW_DEMPSTER() {
			short[] cu_indices = { SI_PLI_DEMPSTER, SI_SB_DEMPSTER, SI_SW_DEMPSTER };
			short cu_index = SI_AT_GOUDIE;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_SW_KER_PLA_SW_KER_NAT() {
			short[] cu_indices = { SI_SW_KER_PLA, SI_SW_KER_NAT };
			short cu_index = SI_SB_KER;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_BL_KURUCZ82() {
			short cu_index = SI_BL_KURUCZ82;
			short search_index = SI_BA_KURUCZ82;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_HWC_BARKER_SS_BARKER() {
			short[] cu_indices = { SI_HWC_BARKER, SI_SS_BARKER };
			short cu_index = SI_CWC_BARKER;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_LW_MILNER_PLI_MILNER_PY_MILNER() {
			short[] cu_indices = { SI_LW_MILNER, SI_PLI_MILNER, SI_PY_MILNER };
			short cu_index = SI_FDI_MILNER;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_FDI_VDP_WASH() {
			short cu_index = SI_FDI_VDP_WASH;
			short search_index = SI_FDI_VDP_MONT;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_FDI_MONS_GF_FDI_MONS_WRC_FDI_MONS_WH_FDI_MONS_SAF() {
			short[] cu_indices = { SI_FDI_MONS_GF, SI_FDI_MONS_WRC, SI_FDI_MONS_WH, SI_FDI_MONS_SAF };
			short cu_index = SI_FDI_MONS_DF;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_SS_FARR() {
			short cu_index = SI_SS_FARR;
			short search_index = SI_HWC_FARR;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_HWC_WILEY_BC_HWC_WILEY_MB() {
			short[] cu_indices = { SI_HWC_WILEY_BC, SI_HWC_WILEY_MB };
			short cu_index = SI_HWC_WILEY;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_SW_THROWER() {
			short cu_index = SI_SW_THROWER;
			short search_index = SI_PLI_THROWER;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_PLI_GOUDIE_WET_SW_GOUDIE_PLA_SW_GOUDIE_NAT() {
			short[] cu_indices = { SI_PLI_GOUDIE_WET, SI_SW_GOUDIE_PLA, SI_SW_GOUDIE_NAT };
			short cu_index = SI_PLI_GOUDIE_DRY;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_PLI_THROWER() { // not in the switch statement but valid index
			short cu_index = SI_PLI_THROWER;
			short search_index = SI_PLI_THROWER;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

	}

	@Nested
	class CurveSourceNotesTest {
		@Test
		void testInvalidCurveIndex() {
			assertThrows(IllegalArgumentException.class, () -> Sindxdll.CurveNotes((short) -1));

			assertThrows(IllegalArgumentException.class, () -> Sindxdll.CurveNotes((short) 140));
		}

		boolean testHelperFunction(short cu_index, short search_index) {
			String actualResult = Sindxdll.CurveNotes(cu_index);
			String expectedResult = si_curve_notes[search_index][1];

			return actualResult.equals(expectedResult);
		}

		@Test
		void testSI_BA_NIGH() {
			short cu_index = SI_BA_NIGH;
			short search_index = SI_BA_NIGHGI;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_CWI_NIGHGI() {
			short cu_index = SI_CWI_NIGHGI;
			short search_index = SI_CWI_NIGH;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_FDI_HUANG_NAT() {
			short cu_index = SI_FDI_HUANG_NAT;
			short search_index = SI_FDI_HUANG_PLA;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_PLI_HUANG_NAT() {
			short cu_index = SI_PLI_HUANG_NAT;
			short search_index = SI_PLI_HUANG_PLA;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_SW_HUANG_NAT() {
			short cu_index = SI_SW_HUANG_NAT;
			short search_index = SI_SW_HUANG_PLA;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_SW_KER_NAT() {
			short cu_index = SI_SW_KER_NAT;
			short search_index = SI_SW_KER_PLA;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_FDI_VDP_WASH() {
			short cu_index = SI_FDI_VDP_WASH;
			short search_index = SI_FDI_VDP_MONT;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_FDI_MONS_GF_FDI_MONS_WRC_FDI_MONS_WH_FDI_MONS_SAF() {
			short[] cu_indices = { SI_FDI_MONS_GF, SI_FDI_MONS_WRC, SI_FDI_MONS_WH, SI_FDI_MONS_SAF };
			short cu_index = SI_FDI_MONS_DF;
			for (short index : cu_indices) {
				assertTrue(testHelperFunction(index, cu_index));
			}
		}

		@Test
		void testSI_PLI_GOUDIE_WET() {
			short cu_index = SI_PLI_GOUDIE_WET;
			short search_index = SI_PLI_GOUDIE_DRY;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_SW_GOUDIE_NAT() {
			short cu_index = SI_SW_GOUDIE_NAT;
			short search_index = SI_SW_GOUDIE_PLA;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_PY_NIGHGI() {
			short cu_index = SI_PY_NIGHGI;
			short search_index = SI_PY_NIGH;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

		@Test
		void testSI_PLI_THROWER() { // not in the switch statement but valid index
			short cu_index = SI_PLI_THROWER;
			short search_index = SI_PLI_THROWER;
			assertTrue(testHelperFunction(cu_index, search_index));
		}

	}

	@Nested
	class AgeToAgeTest {
		@Test
		void testAgeToAgeError() {
			double[] age = new double[1];
			assertThrows(
					AgeTypeErrorException.class,
					() -> Sindxdll.AgeToAge((short) SI_ACB_HUANGAC, 0.0, (short) 1, 0.0, age, (short) 1)
			);
		}

		@Test
		void testAge2AgeValid() {
			double[] age = new double[1];

			double expectedAgeValue = 3.0;
			double actualResult = Sindxdll.AgeToAge((short) SI_AT_NIGH, 1.5, (short) 1, 2, age, (short) 0);
			double expectedResult = 0;

			assertEquals(actualResult, expectedResult);
			assertEquals(age[0], expectedAgeValue, ERROR_TOLERANCE);
		}

	}

	@Nested
	class CurveToSpeciesTest {
		private static final int[] si_curve_intend = { SI_SPEC_ACB, /* SI_ACB_HUANG */
				SI_SPEC_ACT, /* SI_ACT_THROWER */
				SI_SPEC_AT, /* SI_AT_HUANG */
				SI_SPEC_AT, /* SI_AT_CIESZEWSKI */
				SI_SPEC_AT, /* SI_AT_GOUDIE */
				SI_SPEC_BA, /* SI_BA_DILUCCA */
				SI_SPEC_BA, /* should be SI_SPEC_BB */ /* SI_BB_KER */
				SI_SPEC_BA, /* SI_BA_KURUCZ86 */
				SI_SPEC_BA, /* SI_BA_KURUCZ82 */
				SI_SPEC_BL, /* SI_BL_THROWERGI */
				SI_SPEC_BL, /* SI_BL_KURUCZ82 */
				SI_SPEC_CWC, /* SI_CWC_KURUCZ */
				SI_SPEC_CWC, /* SI_CWC_BARKER */
				SI_SPEC_DR, /* SI_DR_NIGH */
				SI_SPEC_DR, /* SI_DR_HARRING */
				SI_SPEC_FDC, /* SI_FDC_NIGHGI */
				SI_SPEC_FDC, /* SI_FDC_BRUCE */
				SI_SPEC_FDC, /* SI_FDC_COCHRAN */
				SI_SPEC_FDC, /* SI_FDC_KING */
				SI_SPEC_FDI, /* SI_FDI_NIGHGI */
				SI_SPEC_FDI, /* SI_FDI_HUANG_PLA */
				SI_SPEC_FDI, /* SI_FDI_HUANG_NAT */
				SI_SPEC_FDI, /* SI_FDI_MILNER */
				SI_SPEC_FDI, /* SI_FDI_THROWER */
				SI_SPEC_FDI, /* SI_FDI_VDP_MONT */
				SI_SPEC_FDI, /* SI_FDI_VDP_WASH */
				SI_SPEC_FDI, /* SI_FDI_MONS_DF */
				SI_SPEC_FDI, /* SI_FDI_MONS_GF */
				SI_SPEC_FDI, /* SI_FDI_MONS_WRC */
				SI_SPEC_FDI, /* SI_FDI_MONS_WH */
				SI_SPEC_FDI, /* SI_FDI_MONS_SAF */
				SI_SPEC_HWC, /* SI_HWC_NIGHGI */
				SI_SPEC_HWC, /* SI_HWC_FARR */
				SI_SPEC_HWC, /* SI_HWC_BARKER */
				SI_SPEC_HWC, /* SI_HWC_WILEY */
				SI_SPEC_HWC, /* SI_HWC_WILEY_BC */
				SI_SPEC_HWC, /* SI_HWC_WILEY_MB */
				SI_SPEC_HWI, /* SI_HWI_NIGH */
				SI_SPEC_HWI, /* SI_HWI_NIGHGI */
				SI_SPEC_LW, /* SI_LW_MILNER */
				SI_SPEC_PLI, /* SI_PLI_THROWNIGH */
				SI_SPEC_PLI, /* SI_PLI_NIGHTA98 */
				SI_SPEC_PLI, /* SI_PLI_NIGHGI97 */
				SI_SPEC_PLI, /* SI_PLI_HUANG_PLA */
				SI_SPEC_PLI, /* SI_PLI_HUANG_NAT */
				SI_SPEC_PLI, /* SI_PLI_THROWER */
				SI_SPEC_PLI, /* SI_PLI_MILNER */
				SI_SPEC_PLI, /* SI_PLI_CIESZEWSKI */
				SI_SPEC_PLI, /* SI_PLI_GOUDIE_DRY */
				SI_SPEC_PLI, /* SI_PLI_GOUDIE_WET */
				SI_SPEC_PLI, /* SI_PLI_DEMPSTER */
				SI_SPEC_PW, /* SI_PW_CURTIS */
				SI_SPEC_PY, /* SI_PY_MILNER */
				SI_SPEC_PY, /* SI_PY_HANN */
				SI_SPEC_SB, /* SI_SB_HUANG */
				SI_SPEC_SB, /* SI_SB_CIESZEWSKI */
				SI_SPEC_SB, /* SI_SB_KER */
				SI_SPEC_SB, /* SI_SB_DEMPSTER */
				SI_SPEC_SS, /* SI_SS_NIGHGI */
				SI_SPEC_SS, /* SI_SS_NIGH */
				SI_SPEC_SS, /* SI_SS_GOUDIE */
				SI_SPEC_SS, /* SI_SS_FARR */
				SI_SPEC_SS, /* SI_SS_BARKER */
				SI_SPEC_SW, /* SI_SW_NIGHGI */
				SI_SPEC_SW, /* SI_SW_HUANG_PLA */
				SI_SPEC_SW, /* SI_SW_HUANG_NAT */
				SI_SPEC_SW, /* SI_SW_THROWER */
				SI_SPEC_SW, /* SI_SW_CIESZEWSKI */
				SI_SPEC_SW, /* SI_SW_KER_PLA */
				SI_SPEC_SW, /* SI_SW_KER_NAT */
				SI_SPEC_SW, /* SI_SW_GOUDIE_PLA */
				SI_SPEC_SW, /* SI_SW_GOUDIE_NAT */
				SI_SPEC_SW, /* SI_SW_DEMPSTER */
				SI_SPEC_BL, /* SI_BL_CHEN */
				SI_SPEC_AT, /* SI_AT_CHEN */
				SI_SPEC_DR, /* SI_DR_CHEN */
				SI_SPEC_PLI, /* SI_PL_CHEN */
				SI_SPEC_CWI, /* SI_CWI_NIGH */
				SI_SPEC_BP, /* SI_BP_CURTIS */
				SI_SPEC_HWC, /* SI_HWC_NIGHGI99 */
				SI_SPEC_SS, /* SI_SS_NIGHGI99 */
				SI_SPEC_SW, /* SI_SW_NIGHGI99 */
				SI_SPEC_LW, /* SI_LW_NIGHGI */
				SI_SPEC_SW, /* SI_SW_NIGHTA */
				SI_SPEC_CWI, /* SI_CWI_NIGHGI */
				SI_SPEC_SW, /* SI_SW_GOUDNIGH */
				SI_SPEC_HM, /* SI_HM_MEANS */
				SI_SPEC_SE, /* SI_SE_CHEN */
				SI_SPEC_FDC, /* SI_FDC_NIGHTA */
				SI_SPEC_FDC, /* SI_FDC_BRUCENIGH */
				SI_SPEC_LW, /* SI_LW_NIGH */
				SI_SPEC_SB, /* SI_SB_NIGH */
				SI_SPEC_AT, /* SI_AT_NIGH */
				SI_SPEC_BL, /* SI_BL_CHENAC */
				SI_SPEC_BP, /* SI_BP_CURTISAC */
				SI_SPEC_HM, /* SI_HM_MEANSAC */
				SI_SPEC_FDI, /* SI_FDI_THROWERAC */
				SI_SPEC_ACB, /* SI_ACB_HUANGAC */
				SI_SPEC_PW, /* SI_PW_CURTISAC */
				SI_SPEC_HWC, /* SI_HWC_WILEYAC */
				SI_SPEC_FDC, /* SI_FDC_BRUCEAC */
				SI_SPEC_CWC, /* SI_CWC_KURUCZAC */
				SI_SPEC_BA, /* SI_BA_KURUCZ82AC */
				SI_SPEC_ACT, /* SI_ACT_THROWERAC */
				SI_SPEC_PY, /* SI_PY_HANNAC */
				SI_SPEC_SE, /* SI_SE_CHENAC */
				SI_SPEC_SW, /* SI_SW_GOUDIE_NATAC */
				SI_SPEC_PY, /* SI_PY_NIGH */
				SI_SPEC_PY, /* SI_PY_NIGHGI */
				SI_SPEC_PLI, /* SI_PLI_NIGHTA2004 */
				SI_SPEC_SE, /* SI_NIGHTA */
				SI_SPEC_SW, /* SI_NIGHTA2004 */
				SI_SPEC_SW, /* SI_GOUDIE_PLAAC */
				SI_SPEC_PJ, /* SI_HUANG */
				SI_SPEC_PJ, /* SI_HUANGAC */
				SI_SPEC_SW, /* SI_SW_NIGHGI2004 */
				SI_SPEC_EP, /* SI_EP_NIGH */
				SI_SPEC_BA, /* SI_BA_NIGHGI */
				SI_SPEC_BA, /* SI_BA_NIGH */
				SI_SPEC_SW, /* SI_SW_HU_GARCIA */
				SI_SPEC_SE, /* SI_SE_NIGHGI */
				SI_SPEC_SE, /* SI_SE_NIGH */
				SI_SPEC_CWC /* SI_CWC_NIGH */
		};

		@Test
		void testCurveToSpeciesError() {
			assertThrows(CurveErrorException.class, () -> Sindxdll.CurveToSpecies((short) 140));

			assertThrows(CurveErrorException.class, () -> Sindxdll.CurveToSpecies((short) -1));
		}

		@Test
		void testCurveToSpeciesValid() {
			double actualResult = Sindxdll.CurveToSpecies((short) 0);
			double expectedResult = SI_SPEC_ACB;

			assertEquals(actualResult, expectedResult);

		}

	}

}