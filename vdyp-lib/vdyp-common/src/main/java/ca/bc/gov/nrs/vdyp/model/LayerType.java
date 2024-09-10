package ca.bc.gov.nrs.vdyp.model;

import java.util.Collections;
import java.util.List;

public enum LayerType {
	/**
	 * The primary layer
	 */
	PRIMARY("P", 0),

	/**
	 * The parser is aware of this but it is never implemented
	 */
	SECONDARY("S", null),

	/**
	 * An older layer than the primary layer, also called the "overstory"
	 */
	VETERAN("V", 1);

	public static final List<LayerType> ALL_USED = Collections.unmodifiableList(List.of(PRIMARY, VETERAN));

	private final String alias;
	private final Integer index;

	LayerType(String alias, Integer index) {
		this.alias = alias;
		this.index = index;
	}

	public String getAlias() {
		return alias;
	}

	public int getIndex() {
		if (index == null) {
			throw new UnsupportedOperationException("LayerType " + this.getAlias() + " is not supported");
		}
		return index;
	}
}
