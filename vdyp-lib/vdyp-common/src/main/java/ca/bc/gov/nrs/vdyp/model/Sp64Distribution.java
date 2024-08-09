package ca.bc.gov.nrs.vdyp.model;

import java.text.MessageFormat;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;

public class Sp64Distribution implements Comparable<Sp64Distribution> {
	
	/** the position of the distribution in the set - a number from 1 to 4. */
	private final int index;
	
	/** the species within the SP0 species whose percentage is defined here */
	private final String genusAlias;
	
	/** the percentage of SP0's basal area allocated to this species */
	private final float percentage;

	/**
	 * Construct a GenusDistribution.
	 *
	 * @param index      the genus's index.
	 * @param genus      the genus. Must not be null.
	 * @param percentage the distribution percentage, which must be in the range 0 .. 100.
	 */
	public Sp64Distribution(int index, String genusAlias, float percentage) {
		if (genusAlias == null) {
			throw new IllegalArgumentException(MessageFormat.format("Alias for index {0} is missing", index));
		}
		
		if (percentage < 0 || percentage > 100) {
			throw new IllegalArgumentException(
					MessageFormat
							.format("Percentage value {0} for index {1} must be between 0 and 100", index, percentage)
			);
		}

		this.index = index;
		this.genusAlias = genusAlias;
		this.percentage = percentage;
	}

	public int getIndex() {
		return index;
	}

	public String getGenusAlias() {
		return genusAlias;
	}

	public Float getPercentage() {
		return percentage;
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Sp64Distribution that) {
			return this.index == that.index && Objects.equals(this.genusAlias, that.genusAlias)
					&& this.percentage == that.percentage;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return ( (index * 17) + genusAlias.hashCode()) * 17 + Float.valueOf(percentage).hashCode();
	}

	@Override
	public int compareTo(Sp64Distribution that) {
		if (that != null) {
			int cr = this.index - that.index;
			if (cr == 0) {
				cr = ObjectUtils.compare(this.genusAlias, that.genusAlias);
				if (cr == 0)
					cr = ObjectUtils.compare(this.percentage, that.percentage);
			}

			return cr;
		} else {
			return 1 /* null is always less than not null */;
		}
	}
}
