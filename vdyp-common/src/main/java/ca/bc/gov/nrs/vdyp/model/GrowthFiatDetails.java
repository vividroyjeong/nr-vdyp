package ca.bc.gov.nrs.vdyp.model;

import java.util.List;

public class GrowthFiatDetails {
	private static final int N_AGES = 4;
	private static final int N_MIXED_COEFFICIENTS = 3;
	
	private final Region region;
	
	private int nAgesSupplied;
	private final Float[] ages;
	private final Float[] coefficients;
	private final Float[] mixedCoefficients;

	/**
	 * Describes a GrowthFiat model.
	 *
	 * @param regionId     either 1 (coast) or 2 (interior)
	 * @param coefficients (11: 0-7 N_AGES pairs of (age, coefficient), 8-10 mixed coefficients)
	 */
	public GrowthFiatDetails(int regionId, List<Float> numbers) {

		region = regionId == 1 ? Region.COASTAL : Region.INTERIOR;

		nAgesSupplied = N_AGES;
		for (int i = 0; i < N_AGES; i++)
			if (numbers.get(i * 2) == 0.0) {
				nAgesSupplied = i;
				break;
			}

		ages = new Float[N_AGES];
		for (int i = 0; i < N_AGES; i++) {
			ages[i] = numbers.get(i * 2);
		}

		coefficients = new Float[N_AGES];
		for (int i = 0; i < N_AGES; i++) {
			coefficients[i] = numbers.get(i * 2 + 1);
		}

		mixedCoefficients = new Float[N_MIXED_COEFFICIENTS];
		for (int i = 0; i < N_MIXED_COEFFICIENTS; i++) {
			mixedCoefficients[i] = numbers.get(2 * N_AGES + i);
		}
	}

	public Region getRegion() {
		return region;
	}

	public int getNAgesSupplied() {
		return nAgesSupplied;
	}

	public Float getAge(int index) {
		if (index < 0 || index >= N_AGES) {
			throw new IllegalArgumentException("GrowthFiatDetails.getAge: index");
		}
		return ages[index];
	}

	public Float getCoefficient(int index) {
		if (index < 0 || index >= N_AGES) {
			throw new IllegalArgumentException("GrowthFiatDetails.getCoefficient: index");
		}
		return coefficients[index];
	}

	public Float getMixedCoefficient(int index) {
		if (index < 0 || index >= N_MIXED_COEFFICIENTS) {
			throw new IllegalArgumentException("GrowthFiatDetails.getMixedCoefficient: index");
		}
		return mixedCoefficients[index];
	}

	public Float[] getAges() {
		return ages;
	}

	public Float[] getCoefficients() {
		return coefficients;
	}

	public Float[] getMixedCoefficients() {
		return mixedCoefficients;
	}
}
