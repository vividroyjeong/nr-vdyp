package ca.bc.gov.nrs.vdyp.io.write;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.io.FileResolver;

/**
 * Write files to be input into VRI Adjust.
 */
public class VriAdjustInputWriter extends VdypOutputWriter {

	/**
	 * Create a writer for VRI Adjust input files using provided OutputStreams. The Streams will be closed when the
	 * writer is closed.
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
		super(polygonFile, speciesFile, utilizationFile);
	}

	/**
	 * Create a writer for VRI Adjust input files configured using the given control map.
	 *
	 * @param polygonFile
	 * @param speciesFile
	 * @param utilizationFile
	 * @param controlMap
	 */
	public VriAdjustInputWriter(Map<String, Object> controlMap, FileResolver resolver) throws IOException {
		this(
				getOutputStream(controlMap, resolver, ControlKey.VRI_OUTPUT_VDYP_POLYGON.name()),
				getOutputStream(controlMap, resolver, ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SPECIES.name()),
				getOutputStream(controlMap, resolver, ControlKey.VRI_OUTPUT_VDYP_LAYER_BY_SP0_BY_UTIL.name()),
				controlMap
		);
	}
}
