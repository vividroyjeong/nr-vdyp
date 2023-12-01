package ca.bc.gov.nrs.vdyp.io.write;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import ca.bc.gov.nrs.vdyp.common.ControlKeys;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.GenusDefinitionParser;
import ca.bc.gov.nrs.vdyp.model.FipMode;
import ca.bc.gov.nrs.vdyp.model.VdypLayer;
import ca.bc.gov.nrs.vdyp.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.model.VdypSpecies;

/**
 * Write files to be input into VRI Adjust.
 */
public class VriAdjustInputWriter implements Closeable {

	Map<String, Object> controlMap;
	OutputStream polygonFile;
	OutputStream speciesFile;
	OutputStream utilizationFile;

	static final String POLY_IDENTIFIER_FORMAT = "%-25s";
	static final String SPEC_DIST_FORMAT = "%-3s%5.1f";
	static final String DISTANCE_FORMAT = "%6.2f";
	static final String AGE_FORMAT = "%6.1f";

	static final float EMPTY_FLOAT = -9f;
	static final int EMPTY_INT = -9;

	// FORMAT(A25, 1x,A4,1x,A1,I6, I3, I3 , I3 )
	static final String POLY_FORMAT = POLY_IDENTIFIER_FORMAT + " %-4s %1s%6d%3d%3d%3d";

	// FORMAT(A25,1x,A1,1x,I2,1x,A2,1x,A32,2f6.2,3f6.1,I2,I3)
	static final String SPEC_FORMAT = POLY_IDENTIFIER_FORMAT + " %-1s %2d %-2s " + SPEC_DIST_FORMAT.repeat(4)
			+ DISTANCE_FORMAT.repeat(2) + AGE_FORMAT.repeat(3) + "%2d%3d";

	/**
	 * Create a writer for VRI Adjust input files using provided OutputStreams. The
	 * Streams will be closed when the writer is closed.
	 *
	 * @param polygonFile
	 * @param speciesFile
	 * @param utilizationFile
	 * @param controlMap
	 */
	public VriAdjustInputWriter(
			OutputStream polygonFile, OutputStream speciesFile, OutputStream utilizationFile,
			Map<String, Object> controlMap
	) {
		this.controlMap = controlMap;
		this.polygonFile = polygonFile;
		this.speciesFile = speciesFile;
		this.utilizationFile = utilizationFile;
	}

	/**
	 * Create a writer for VRI Adjust input files configured using the given control
	 * map.
	 *
	 * @param polygonFile
	 * @param speciesFile
	 * @param utilizationFile
	 * @param controlMap
	 */
	public VriAdjustInputWriter(Map<String, Object> controlMap, FileResolver resolver) throws IOException {
		this(
				getOutputStream(controlMap, resolver, ControlKeys.VDYP_POLYGON),
				getOutputStream(controlMap, resolver, ControlKeys.VDYP_LAYER_BY_SPECIES),
				getOutputStream(controlMap, resolver, ControlKeys.VDYP_LAYER_BY_SP0_BY_UTIL), controlMap
		);
	}

	static OutputStream getOutputStream(Map<String, Object> controlMap, FileResolver resolver, String key)
			throws IOException {
		String fileName = Utils.expectParsedControl(controlMap, key, String.class);
		return resolver.resolveForOutput(fileName);
	}

	// V7W_AIP
	void writePolygon(VdypPolygon polygon) throws IOException {
		this.polygonFile.write(
				String.format(
						POLY_FORMAT, //
						polygon.getPolygonIdentifier(), //
						polygon.getBiogeoclimaticZone(), //
						polygon.getForestInventoryZone(), //

						polygon.getPercentAvailable().intValue(), //
						polygon.getItg(), //
						polygon.getGrpBa1(), //
						polygon.getModeFip().map(FipMode::getCode).orElse(0)
				).getBytes()
		);
	}

	void writeSpecies(VdypPolygon polygon, VdypLayer layer, VdypSpecies spec) throws IOException {

		// Ensure we have a list of 4 distribution entries
		var specDistributionEntries = Stream.concat(
				spec.getSpeciesPercent().entrySet().stream().sorted(Utils.compareUsing(x -> x.getValue())),
				Stream.generate(() -> new AbstractMap.SimpleEntry<String, Float>("", 0f))
		).limit(4).toList();
		// 082E004    615       1988 P  9 L  LW 100.0     0.0     0.0     0.0 -9.00 -9.00  -9.0  -9.0  -9.0 0 -9
		var specIndex = GenusDefinitionParser.getIndex(spec.getGenus(), controlMap);
		this.speciesFile.write(
				String.format(
						// %-25s %-1s %2d %-2s %-3s%5.1f%-3s%5.1f%-3s%5.1f%-3s%5.1f%6.2f%6.2f%6.1f%6.1f%6.1f%2d%3d
						SPEC_FORMAT, //
						spec.getPolygonIdentifier(), //
						spec.getLayer().getAlias(), //
						specIndex.orElse(0),
						spec.getGenus(), //

						specDistributionEntries.get(0).getKey(), //
						specDistributionEntries.get(0).getValue(), //
						specDistributionEntries.get(1).getKey(), //
						specDistributionEntries.get(1).getValue(), //
						specDistributionEntries.get(2).getKey(), //
						specDistributionEntries.get(2).getValue(), //
						specDistributionEntries.get(3).getKey(), //
						specDistributionEntries.get(3).getValue(), //

						layer.getSiteIndex().orElse(EMPTY_FLOAT), //
						layer.getHeight().orElse(EMPTY_FLOAT), //
						layer.getAgeTotal().orElse(EMPTY_FLOAT), //
						layer.getBreastHeightAge().orElse(EMPTY_FLOAT), //
						layer.getYearsToBreastHeight().orElse(EMPTY_FLOAT), //
						layer.getSiteGenus().map(id -> id.equals(spec.getGenus())).orElse(false) ? 1 : 0, //
						layer.getSiteCurveNumber().orElse(EMPTY_INT)

				).getBytes()
		);

	}

	// V7W_AIU
	void writeUtilization(VdypPolygon polygon, VdypLayer layer, VdypSpecies spec) {

	}

	// VDYP_OUT when JPROGRAM = 1 (FIPSTART) or 3 (VRISTART)
	public void write() {

	}

	@Override
	public void close() throws IOException {
		polygonFile.close();
		speciesFile.close();
		utilizationFile.close();
	}
}
