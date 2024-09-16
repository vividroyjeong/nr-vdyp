package ca.bc.gov.nrs.vdyp.io.parse.coe;

import java.util.Collection;
import java.util.List;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.parse.coe.base.BaseCoefficientParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseValidException;
import ca.bc.gov.nrs.vdyp.model.Coefficients;
import ca.bc.gov.nrs.vdyp.model.ComponentSizeLimits;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2;
import ca.bc.gov.nrs.vdyp.model.MatrixMap2Impl;
import ca.bc.gov.nrs.vdyp.model.Region;

/**
 * Parse a datafile with species component size limits.
 *
 * Each line contains:
 * <ol>
 * <li>(cols 0-1) a Species key
 * <li>(col 3) a region indicator (either C or I)
 * <li>(cols 4-9, 10-15, 16-21, 22-27) - float * 4 - coefficients
 * </ol>
 * <ul>
 * <li>Coefficient 1 is High value for HL
 * <li>Coefficient 2 is High value for DQ
 * <li>Coefficient 3 is low value for DQ/HL
 * <li>Coefficient 4 is high value for DQ/HL
 * </ul>
 * All lines are read; there is no provision for blank lines.
 * <p>
 * The result is a {@link MatrixMap2} of ComponentSizeLimits, indexed first by Species and then Region.
 * <p>
 * FIP Control index: 061
 * <p>
 * Example file: coe/COMPLIM.COE
 *
 * @author Kevin Smith, Vivid Solutions
 * @see BaseCoefficientParser
 */
public class ComponentSizeParser extends
		BaseCoefficientParser<Coefficients, ComponentSizeLimits, MatrixMap2<String, Region, ComponentSizeLimits>> {

	public ComponentSizeParser() {
		super(2, ControlKey.SPECIES_COMPONENT_SIZE_LIMIT);
		this.speciesKey().space(1).regionKey().coefficients(4, 6);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected MatrixMap2<String, Region, ComponentSizeLimits> createMap(List<Collection<?>> keyRanges) {
		return new MatrixMap2Impl<>(
				(Collection<String>) keyRanges.get(0), (Collection<Region>) keyRanges.get(1),
				(k1, k2) -> wrapCoefficients(getCoefficients())
		);
	}

	@Override
	protected Coefficients getCoefficients(List<Float> coefficients) {
		return new Coefficients(coefficients, 1);
	}

	@Override
	protected ComponentSizeLimits wrapCoefficients(Coefficients coefficients) {
		return new ComponentSizeLimits(
				coefficients.getCoe(1), coefficients.getCoe(2), coefficients.getCoe(3), coefficients.getCoe(4)
		);
	}

	@Override
	protected void
			validate(MatrixMap2<String, Region, ComponentSizeLimits> result, int parsed, List<Collection<?>> keyRanges)
					throws ResourceParseValidException {
		var expected = keyRanges.stream().mapToInt(Collection::size).reduce(1, (x, y) -> x * y);
		if (expected != parsed) {
			throw new ResourceParseValidException("Expected " + expected + " records but there were " + parsed);
		}
	}

}
