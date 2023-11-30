package ca.bc.gov.nrs.vdyp.io.write;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.ControlKeys;
import ca.bc.gov.nrs.vdyp.common.Utils;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
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
						// FORMAT(A25, 1x,A4,1x,A1,I6, I3, I3 , I3 )
						"%-25s %-4s %1s%6d%3d%3d%3d", //
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

	// V7W_AIS
	void writeSpecies(VdypPolygon polygon, VdypLayer layer, VdypSpecies spec) {

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
