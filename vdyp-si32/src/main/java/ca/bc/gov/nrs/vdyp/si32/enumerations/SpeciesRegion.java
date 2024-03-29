package ca.bc.gov.nrs.vdyp.si32.enumerations;

/**
 * Enumerates each of the potential regions a species can be found in.
 * <ul>
 * <li>spcsRgn_Coast
 * <li>spcsRgn_Interior
 * </ul>
 */
public enum SpeciesRegion implements SI32Enum<SpeciesRegion> {
	
	spcsRgn_Coast(0), // 
	spcsRgn_Interior(1);

	private int intValue;
	private static java.util.HashMap<Integer, SpeciesRegion> mappings;

	private static java.util.HashMap<Integer, SpeciesRegion> getMappings() {
		if (mappings == null) {
			synchronized (SpeciesRegion.class) {
				if (mappings == null) {
					mappings = new java.util.HashMap<Integer, SpeciesRegion>();
				}
			}
		}
		return mappings;
	}

	private SpeciesRegion(int value) {
		intValue = value;
		getMappings().put(value, this);
	}

	@Override
	public int getValue() {
		return intValue;
	}

	@Override
	public int getIndex() {
		return intValue;
	}
	
	@Override
	public String getText() {
		return this.toString().substring("spcsRgn_".length());
	}

	public static SpeciesRegion forValue(int value) {
		return getMappings().get(value);
	}

	public static int size() {
		return spcsRgn_Interior.intValue - spcsRgn_Coast.intValue + 1;
	}

	public static class Iterator extends SI32EnumIterator<SpeciesRegion> {
		public Iterator() {
			super(spcsRgn_Coast, spcsRgn_Interior, mappings);
		}
	}
}
