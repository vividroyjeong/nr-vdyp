package ca.bc.gov.nrs.vdyp.backend.v1.api.projection;

import org.slf4j.event.Level;

import ca.bc.gov.nrs.vdyp.backend.v1.api.impl.messaging.IMessageLog;
import ca.bc.gov.nrs.vdyp.backend.v1.api.impl.messaging.MessageLog;
import ca.bc.gov.nrs.vdyp.backend.v1.api.impl.messaging.NullMessageLog;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.Parameters;
import jakarta.validation.Valid;

public class ProjectionState {

	private final String projectionId;
	private final Parameters params;

	private final IMessageLog progressLog;
	private final IMessageLog errorLog;

	public ProjectionState(String projectionId, @Valid Parameters params) {

		this.projectionId = projectionId;
		this.params = params;

		boolean errorLoggingEnabled = this.params.getSelectedExecutionOptions()
				.contains(Parameters.SelectedExecutionOptionsEnum.DO_ENABLE_ERROR_LOGGING);
		boolean progressLoggingEnabled = this.params.getSelectedExecutionOptions()
				.contains(Parameters.SelectedExecutionOptionsEnum.DO_ENABLE_PROGRESS_LOGGING);

		if (errorLoggingEnabled) {
			errorLog = new MessageLog(Level.ERROR);
		} else {
			errorLog = new NullMessageLog(Level.ERROR);
		}

		if (progressLoggingEnabled) {
			progressLog = new MessageLog(Level.INFO);
		} else {
			progressLog = new NullMessageLog(Level.INFO);
		}
	}

	public String getProjectionId() {
		return projectionId;
	}

	public IMessageLog getProgressLog() {
		return progressLog;
	}

	public IMessageLog getErrorLog() {
		return errorLog;
	}
}
