package ca.bc.gov.nrs.vdyp.backend.v1.api.projection;

import java.io.InputStream;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.backend.v1.api.impl.exceptions.ProjectionException;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters;

public interface IProjectionRunner {

	void run(Map<String, InputStream> inputStreams);

	Parameters getParameters();

	InputStream getYieldTable() throws ProjectionException;

	InputStream getProgressStream() throws ProjectionException;

	InputStream getErrorStream() throws ProjectionException;

}