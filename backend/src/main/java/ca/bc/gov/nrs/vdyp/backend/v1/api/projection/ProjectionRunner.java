package ca.bc.gov.nrs.vdyp.backend.v1.api.projection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.backend.v1.api.impl.exceptions.ProjectionException;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters;
import ca.bc.gov.nrs.vdyp.backend.v1.utils.FileHelper;
import jakarta.validation.Valid;

public class ProjectionRunner implements IProjectionRunner {

	private final ProjectionState state;
	private final Parameters parameters;

	public ProjectionRunner(String projectionId, @Valid Parameters parameters) {
		this.state = new ProjectionState(projectionId, parameters);
		this.parameters = parameters;
	}

	@Override
	public void run(Map<String, InputStream> streams) {
		state.getProgressLog().addMessage("Running Projection");

		validate();
		createInputStream(streams.get("polygon"), streams.get("layers"));
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
		// TODO: For now...
		try {
			return FileHelper.getStubResourceFile("Output_YldTbl.csv");
		} catch (IOException e) {
			throw new ProjectionException(e);
		}
	}

	@Override
	public InputStream getProgressStream() {
		return state.getProgressLog().getAsStream();
	}

	@Override
	public InputStream getErrorStream() {
		return state.getErrorLog().getAsStream();
	}
}
