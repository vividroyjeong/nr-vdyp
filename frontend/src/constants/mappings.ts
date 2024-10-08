// Mapping species code and species names
export const SPECIES_Map = {
  AC: 'Poplar',
  AT: 'Aspen',
  B: 'True Fir',
  BA: 'Amabilis Fir',
  BG: 'Grand Fir',
  BL: 'Alpine Fir',
  CW: 'Western Red Cedar',
  DR: 'Red Alder',
  E: 'Birch',
  EA: 'Alaska Paper Birch',
  EP: 'Common Paper Birch',
  FD: 'Douglas Fir',
  H: 'Hemlock',
  HM: 'Mountain Hemlock',
  HW: 'Western Hemlock',
  L: 'Larch',
  LA: 'Alpine Larch',
  LT: 'Tamarack',
  LW: 'Western Larch',
  MB: 'Bigleaf Maple',
  PA: 'Whitebark Pine',
  PF: 'Limber Pine',
  PJ: 'Jack Pine',
  PL: 'Lodgepole Pine',
  PW: 'Western White Pine',
  PY: 'Ponderosa (Yellow) Pine',
  S: 'Spruce',
  SB: 'Black Spruce',
  SE: 'Engelmann Spruce',
  SS: 'Sitka Spruce',
  SW: 'White Spruce',
  YC: 'Yellow Cedar',
}

// Mapping species code and default Site Index Curve name
export const SITE_INDEX_CURVE_MAP = {
  AC: 'Huang, Titus, and Lakusta (1994ac)',
  AT: 'Nigh, Krestov, and Klinka 2002',
  B: 'Chen and Klinka (2000ac)',
  BA: 'Nigh (2009)',
  BG: 'Nigh (2009)',
  BL: 'Chen and Klinka (2000ac)',
  CW: 'Nigh (2000)',
  DR: 'Nigh and Courtin (1998)',
  E: 'Nigh (2009)',
  EA: 'Nigh (2009)',
  EP: 'Nigh (2009)',
  FD: 'Thrower and Goudie (1992ac)',
  H: 'Nigh (1998)',
  HM: 'Means, Campbell, Johnson (1988ac)',
  HW: 'Nigh (1998)',
  L: 'Brisco, Klinka, Nigh 2002',
  LA: 'Brisco, Klinka, Nigh 2002',
  LT: 'Brisco, Klinka, Nigh 2002',
  LW: 'Brisco, Klinka, Nigh 2002',
  MB: 'Nigh and Courtin (1998)',
  PA: 'Thrower (1994)',
  PF: 'Thrower (1994)',
  PJ: 'Huang (1997ac)',
  PL: 'Thrower (1994)',
  PW: 'Curtis, Diaz, and Clendenen (1990ac)',
  PY: 'Nigh (2002)',
  S: 'Goudie (1984ac) (natural)',
  SB: 'Nigh, Krestov, and Klinka 2002',
  SE: 'Nigh (2015)',
  SS: 'Nigh (1997)',
  SW: 'Goudie (1984ac) (natural)',
  YC: 'Nigh (2000)',
}

// Mapping bec zone code and coastal or not
export const BEC_ZONE_COASTAL_MAP: Record<string, boolean> = {
  AT: false,
  BG: false,
  BWBS: false,
  CDF: true,
  CWH: true,
  ESSF: false,
  ICH: false,
  IDF: false,
  MH: true,
  MS: false,
  PP: false,
  SBPS: false,
  SBS: false,
  SWB: false,
}

// BA_LIMIT_COEFFICIENTS stores the basal area limit coefficients for different species (AC, AT, B, etc.).
// Each species has separate coefficient values for coastal and interior regions, represented by 'coeff1' and 'coeff2'.
// These coefficients are used in an exponential equation to calculate the maximum allowable basal area based on species and location.
// A value of -999 indicates that no valid limit exists for the specific region and species combination. (from vdyp.ini)
export const BA_LIMIT_COEFFICIENTS = {
  AC: {
    coastal: { coeff1: 107.240519, coeff2: -14.377881 },
    interior: { coeff1: 118.629456, coeff2: -19.159803 },
  },
  AT: {
    coastal: { coeff1: -999, coeff2: -999 },
    interior: { coeff1: 98.298267, coeff2: -15.823783 },
  },
  B: {
    coastal: { coeff1: 134.265995, coeff2: -10.723979 },
    interior: { coeff1: 103.717551, coeff2: -12.032769 },
  },
  C: {
    coastal: { coeff1: 199.94291, coeff2: -14.931348 },
    interior: { coeff1: 393.75934, coeff2: -35.40266 },
  },
  D: {
    coastal: { coeff1: 107.240519, coeff2: -14.377881 },
    interior: { coeff1: -999, coeff2: -999 },
  },
  E: {
    coastal: { coeff1: 107.240519, coeff2: -14.377881 },
    interior: { coeff1: 118.629456, coeff2: -19.159803 },
  },
  F: {
    coastal: { coeff1: 213.706529, coeff2: -28.643038 },
    interior: { coeff1: 132.594246, coeff2: -20.216383 },
  },
  H: {
    coastal: { coeff1: 144.825311, coeff2: -13.110869 },
    interior: { coeff1: 122.420409, coeff2: -10.923619 },
  },
  L: {
    coastal: { coeff1: -999, coeff2: -999 },
    interior: { coeff1: 119.642742, coeff2: -21.246736 },
  },
  MB: {
    coastal: { coeff1: 107.240519, coeff2: -14.377881 },
    interior: { coeff1: -999, coeff2: -999 },
  },
  PL: {
    coastal: { coeff1: 185.048127, coeff2: -19.900699 },
    interior: { coeff1: 95.118542, coeff2: -12.154888 },
  },
  PW: {
    coastal: { coeff1: -999, coeff2: -999 },
    interior: { coeff1: 158.465684, coeff2: -26.781112 },
  },
  PY: {
    coastal: { coeff1: -999, coeff2: -999 },
    interior: { coeff1: 71.943238, coeff2: -14.264704 },
  },
  S: {
    coastal: { coeff1: 177.814415, coeff2: -13.714547 },
    interior: { coeff1: 96.84127, coeff2: -12.60781 },
  },
}

// BA_EQUATION_CONSTANTS defines the constant values used in the basal area limit equation.
// These constants (const1, const2, and const3) are applied to calculate the final basal area limit based on species-specific coefficients. (from vdyp.ini)
export const BA_EQUATION_CONSTANTS = {
  const1: 5,
  const2: 1.3,
  const3: -1,
}

export const TPH_LIMIT_COEFFICIENTS = {
  AC: {
    coastal: {
      P10: { a0: 7.5, b0: 0.184064, b1: 0.005592 },
      P90: { a0: 7.5, b0: 0.96373, b1: 0.00453 },
    },
    interior: {
      P10: { a0: 7.5, b0: -0.084114, b1: 0.016436 },
      P90: { a0: 7.5, b0: 0.58714, b1: 0.022826 },
    },
  },
  AT: {
    interior: {
      P10: { a0: 7.5, b0: 0.00544, b1: 0.010618 },
      P90: { a0: 7.5, b0: 0.660157, b1: 0.011754 },
    },
  },
  B: {
    coastal: {
      P10: { a0: 7.5, b0: 0.229925, b1: 0.005735 },
      P90: { a0: 7.5, b0: 1.226133, b1: -0.002427 },
    },
    interior: {
      P10: { a0: 7.5, b0: 0.184201, b1: 0.006065 },
      P90: { a0: 7.5, b0: 1.059981, b1: -0.000686 },
    },
  },
  C: {
    coastal: {
      P10: { a0: 7.5, b0: 0.387454, b1: 0.002709 },
      P90: { a0: 7.5, b0: 1.45061, b1: -0.000679 },
    },
    interior: {
      P10: { a0: 7.5, b0: 0.103056, b1: 0.012318 },
      P90: { a0: 7.5, b0: 0.2699, b1: 0.042869 },
    },
  },
  D: {
    coastal: {
      P10: { a0: 7.5, b0: 0.184064, b1: 0.005592 },
      P90: { a0: 7.5, b0: 0.96373, b1: 0.00453 },
    },
  },
  E: {
    coastal: {
      P10: { a0: 7.5, b0: 0.184064, b1: 0.005592 },
      P90: { a0: 7.5, b0: 0.96373, b1: 0.00453 },
    },
    interior: {
      P10: { a0: 7.5, b0: -0.084114, b1: 0.016436 },
      P90: { a0: 7.5, b0: 0.58714, b1: 0.022826 },
    },
  },
  F: {
    coastal: {
      P10: { a0: 7.5, b0: 0.116002, b1: 0.006594 },
      P90: { a0: 7.5, b0: 0.68269, b1: 0.008622 },
    },
    interior: {
      P10: { a0: 7.5, b0: 0.123477, b1: 0.005786 },
      P90: { a0: 7.5, b0: 1.193114, b1: -0.006459 },
    },
  },
  H: {
    coastal: {
      P10: { a0: 7.5, b0: 0.126113, b1: 0.007561 },
      P90: { a0: 7.5, b0: 1.207655, b1: -0.001023 },
    },
    interior: {
      P10: { a0: 7.5, b0: 0.014342, b1: 0.012198 },
      P90: { a0: 7.5, b0: 0.79931, b1: 0.013942 },
    },
  },
  L: {
    interior: {
      P10: { a0: 7.5, b0: 0.06893, b1: 0.005579 },
      P90: { a0: 7.5, b0: 0.31423, b1: 0.015952 },
    },
  },
  MB: {
    coastal: {
      P10: { a0: 7.5, b0: 0.184064, b1: 0.005592 },
      P90: { a0: 7.5, b0: 0.96373, b1: 0.00453 },
    },
  },
  PL: {
    coastal: {
      P10: { a0: 7.5, b0: -0.083294, b1: 0.014145 },
      P90: { a0: 7.5, b0: 0.938361, b1: -0.003504 },
    },
    interior: {
      P10: { a0: 7.5, b0: -0.083294, b1: 0.014145 },
      P90: { a0: 7.5, b0: 0.938361, b1: -0.003504 },
    },
  },
  PW: {
    interior: {
      P10: { a0: 7.5, b0: 0.031801, b1: 0.007887 },
      P90: { a0: 7.5, b0: 0.909946, b1: -0.005477 },
    },
  },
  PY: {
    interior: {
      P10: { a0: 7.5, b0: 0.267422, b1: 0.009514 },
      P90: { a0: 7.5, b0: 1.922409, b1: -0.008496 },
    },
  },
  S: {
    coastal: {
      P10: { a0: 7.5, b0: 0.16879, b1: 0.008936 },
      P90: { a0: 7.5, b0: 0.8714, b1: 0.011812 },
    },
    interior: {
      P10: { a0: 7.5, b0: 0.124051, b1: 0.007309 },
      P90: { a0: 7.5, b0: 0.910138, b1: 0.002576 },
    },
  },
}

export const TPH_EQUATION_CONSTANTS = {
  const1: 5.0,
  const2: 1.3,
  const3: 0.00007854,
}
