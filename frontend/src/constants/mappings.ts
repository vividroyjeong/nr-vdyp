// Mapping species code and species names
export const speciesMap = {
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
export const siteIndexCurveMap = {
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

// EQUATION_CONSTANTS defines the constant values used in the basal area limit equation.
// These constants (const1, const2, and const3) are applied to calculate the final basal area limit based on species-specific coefficients. (from vdyp.ini)
export const EQUATION_CONSTANTS = {
  const1: 5,
  const2: 1.3,
  const3: -1,
}
