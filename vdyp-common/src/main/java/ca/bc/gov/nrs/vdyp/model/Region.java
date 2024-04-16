package ca.bc.gov.nrs.vdyp.model;

import java.util.Arrays;
import java.util.Optional;

public enum Region {
	COASTAL('C', 0), INTERIOR('I', 1);

	private final char characterAlias;
	private final int index;

	private Region(char characterAlias, int index) {
		this.characterAlias = characterAlias;
		this.index = index;
	}

	public static Optional<Region> fromAlias(char alias) {
		return Arrays.stream(Region.values()).filter(x -> x.getCharacterAlias() == alias).findFirst();
	}

	public static Optional<Region> fromAlias(String alias) {
		if (alias.length() == 1) {
			return fromAlias(alias.charAt(0));
		}
		return Optional.empty();
	}

	public char getCharacterAlias() {
		return characterAlias;
	}

	public int getIndex() {
		return index;
	}

}
