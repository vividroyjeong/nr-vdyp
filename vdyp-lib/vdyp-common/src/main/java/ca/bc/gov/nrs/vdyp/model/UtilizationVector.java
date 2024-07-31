package ca.bc.gov.nrs.vdyp.model;

import java.text.MessageFormat;
import java.util.List;

import ca.bc.gov.nrs.vdyp.common.FloatBinaryOperator;
import ca.bc.gov.nrs.vdyp.common.FloatUnaryOperator;

public class UtilizationVector extends Coefficients {

	public UtilizationVector() {
		super(new float[] { Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN }, UtilizationClass.SMALL.index);
	}

	public UtilizationVector(float small, float all, float uc1, float uc2, float uc3, float uc4) {
		super(new float[] { small, all, uc1, uc2, uc3, uc4 }, UtilizationClass.SMALL.index);
	}

	public UtilizationVector(float small, float all) {
		super(new float[] { small, all }, UtilizationClass.SMALL.index);
	}

	public UtilizationVector(List<Float> coe) {
		super(coe, UtilizationClass.SMALL.index);
		if (coe.size() != 6 && coe.size() != 2) {
			throw new IllegalArgumentException(
					MessageFormat.format("UtilizationVector should be of size 6 or 2 but was {0}", coe)
			);
		}
	}

	public float get(UtilizationClass uc) {
		checkClass(uc);
		return getCoe(uc.index);
	}

	public void set(UtilizationClass uc, float value) {
		checkClass(uc);
		setCoe(uc.index, value);
	}

	public float getSmall() {
		return get(UtilizationClass.SMALL);
	}

	public void setSmall(float value) {
		set(UtilizationClass.SMALL, value);
	}

	public float getAll() {
		return get(UtilizationClass.ALL);
	}

	public void setAll(float value) {
		set(UtilizationClass.ALL, value);
	}

	public float getLarge() {
		return get(UtilizationClass.OVER225);
	}

	public void setLarge(float value) {
		set(UtilizationClass.OVER225, value);
	}

	private void checkClass(UtilizationClass uc) {
		if (uc.index >= this.size() - 1) {
			throw new IllegalArgumentException("Lorey Height vector can only use utilization vectors ALL and SMALL");
		}
	}

	public UtilizationVector pairwise(UtilizationVector coe2, FloatBinaryOperator op) {
		var result = new UtilizationVector(this);
		result.pairwiseInPlace(coe2, (float x, float y, UtilizationClass uc) -> op.applyAsFloat(x, y));
		return result;
	}

	@Override
	public Coefficients scalar(FloatUnaryOperator op) {
		var result = new UtilizationVector(this);
		result.scalarInPlace((float x, UtilizationClass uc) -> op.applyAsFloat(x));
		return result;
	}

	/**
	 * Performs a pairwise operation in place with another UtilizationVector
	 *
	 * @param coe2 another utilization vector
	 * @param op   operation to perform for each pair of coefficients
	 */
	public void pairwiseInPlace(UtilizationVector coe2, BinaryOperatorWithClass op) {
		for (var uc : UtilizationClass.values()) {
			if (uc.index >= this.size() - 1) {
				break;
			}
			set(uc, op.applyAsFloatWithClass(get(uc), coe2.get(uc), uc));
		}
	}

	/**
	 * Performs a pairwise operation with another UtilizationVector and returns the result.
	 *
	 * @param coe2 must have the same size and index offset
	 * @param op   operation to perform for each pair of coefficients
	 */
	public UtilizationVector pairwise(UtilizationVector coe2, BinaryOperatorWithClass op) {
		var result = new UtilizationVector(this);
		result.pairwiseInPlace(coe2, op);
		return result;
	}

	/**
	 * Perform the operation on one particular coefficient in place
	 *
	 * @param op
	 */
	public void scalarInPlace(UtilizationClass uc, UnaryOperatorWithClass op) {
		set(uc, op.applyAsFloatWithClass(get(uc), uc));
	}

	/**
	 * Perform the operation on one particular coefficient in place
	 *
	 * @param op
	 */
	public void scalarInPlace(UtilizationClass uc, FloatUnaryOperator op) {
		set(uc, op.applyAsFloat(get(uc)));
	}

	/**
	 * Perform the operation on each coefficient in place
	 *
	 * @param op
	 */
	public void scalarInPlace(UnaryOperatorWithClass op) {
		for (var uc : UtilizationClass.values()) {
			if (uc.index >= this.size() - 1) {
				break;
			}
			scalarInPlace(uc, op);
		}
	}

	/**
	 * Perform the operation on each coefficient and return the result
	 *
	 * @param op
	 * @return
	 */
	public UtilizationVector scalar(UnaryOperatorWithClass op) {
		var result = new UtilizationVector(this);
		result.scalarInPlace(op);
		return result;
	}

	@FunctionalInterface
	public static interface UnaryOperatorWithClass {

		/**
		 * Applies this operator to the given operand.
		 *
		 * @param value the operand
		 * @param index the index
		 * @return the operator result
		 */
		float applyAsFloatWithClass(float value, UtilizationClass uc);
	}

	@FunctionalInterface
	public static interface BinaryOperatorWithClass {
		/**
		 * Applies this operator to the given operand.
		 *
		 * @param value the operand
		 * @param index the index
		 * @return the operator result
		 */
		float applyAsFloatWithClass(float value1, float value2, UtilizationClass uc);
	}

}
