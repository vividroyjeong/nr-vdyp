package ca.bc.gov.nrs.vdyp.backend.v1.api.projection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.backend.v1.api.impl.exceptions.ProjectionException;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters;
import ca.bc.gov.nrs.vdyp.backend.v1.utils.FileHelper;
import jakarta.validation.Valid;

public class StubProjectionRunner implements IProjectionRunner {

	private final ProjectionState state;
	private final Parameters parameters;

	public StubProjectionRunner(String projectionId, @Valid Parameters parameters) {
		this.state = new ProjectionState(projectionId, parameters);
		this.parameters = parameters;
	}

	@Override
	public void run(Map<String, InputStream> streams) {
		state.getProgressLog().addMessage("Running Projection");

		createInputStream(streams.get("polygon"), streams.get("layers"));

		validate();

		project();
	}

	@Override
	public Parameters getParameters() {
		return parameters;
	}

	private void createInputStream(InputStream polyStream, InputStream layersStream) {
		// TODO Auto-generated method stub
	}

	private void validate() {
		// TODO Auto-generated method stub
	}

	private void project() {
		// TODO Auto-generated method stub
	}

	@Override
	public InputStream getYieldTable() throws ProjectionException {
		try {
			return FileHelper.getStubResourceFile("Output_YldTbl.csv");
		} catch (IOException e) {
			throw new ProjectionException(e);
		}
	}

	@Override
	public InputStream getProgressStream() throws ProjectionException {
		try {
			return FileHelper.getStubResourceFile("Output_Log.txt");
		} catch (IOException e) {
			throw new ProjectionException(e);
		}
	}

	@Override
	public InputStream getErrorStream() throws ProjectionException {
		try {
			return FileHelper.getStubResourceFile("Output_Error.txt");
		} catch (IOException e) {
			throw new ProjectionException(e);
		}
	}
}
