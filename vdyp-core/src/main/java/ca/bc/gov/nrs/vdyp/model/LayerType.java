package ca.bc.gov.nrs.vdyp.model;

public enum LayerType {
	/**
	 * The primary layer
	 */
	PRIMARY,
	/**
	 * The parser is aware of this but it is never implemented
	 */
	SECONDARY,
	/**
	 * An older layer than the primary layer, also called the "overstory"
	 */
	VETERAN
}
