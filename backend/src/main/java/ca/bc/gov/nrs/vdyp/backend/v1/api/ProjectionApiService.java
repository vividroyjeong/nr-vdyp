package ca.bc.gov.nrs.vdyp.backend.v1.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionDcsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionHcsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.gen.model.ProjectionScsvPostRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;

@ApplicationScoped
/**
 * Implements the projection endpoints. These methods return Responses rather than Response objects because these
 * responses are not JSON objects and contain no links.
 */
public class ProjectionApiService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectionApiService.class);

	public Response projectionDcsvPost(
			@Valid ProjectionDcsvPostRequest projectionDcsvPostRequest, SecurityContext securityContext
	) {
		return Response.serverError().status(501).build();
	}

	public Response projectionHcsvPost(
			@Valid ProjectionHcsvPostRequest projectionHcsvPostRequest, SecurityContext securityContext
	) {
		try {
			logger.info("<projectionHcsvPost");

			var projectionParameters = projectionHcsvPostRequest.getProjectionParameters();
			var selectedDebugOptions = projectionParameters.getSelectedDebugOptions();
			logger.info(selectedDebugOptions.toString());

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zipOut = new ZipOutputStream(baos);

			ZipEntry yieldTableZipEntry = new ZipEntry("Output_YldTbl.csv");
			zipOut.putNextEntry(yieldTableZipEntry);
			var yieldTable = getResourceFile("Output_YldTbl.csv");
			zipOut.write(yieldTable.readAllBytes());

			ZipEntry logOutputEntry = new ZipEntry("Output_Log.txt");
			zipOut.putNextEntry(logOutputEntry);
			var logFile = getResourceFile("Output_Log.txt");
			zipOut.write(logFile.readAllBytes());

			ZipEntry errorOutputZipEntry = new ZipEntry("Output_Error.txt");
			zipOut.putNextEntry(errorOutputZipEntry);
			var errorFile = getResourceFile("Output_Error.txt");
			zipOut.write(errorFile.readAllBytes());

			zipOut.close();

			byte[] resultingByteArray = baos.toByteArray();

			logger.info(">projectionHcsvPost ({} bytes returned)", resultingByteArray.length);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");
			var outputFileName = "vdyp-output-" + java.time.LocalDateTime.now().format(formatter);
			return Response.ok(resultingByteArray).status(Status.CREATED)
					.header("content-disposition", "attachment;filename=\"" + outputFileName + "\"").build();

		} catch (IOException | URISyntaxException e) {
			logger.error(">projectionHcsvPost failure of type {0}", e.getMessage());

			return Response.serverError().status(500)
					.entity(
							"Unable to load canned output file(s)" + e.getMessage() != null ? "; " + e.getMessage() : ""
					).build();
		}
	}

	public Response projectionScsvPost(
			@Valid ProjectionScsvPostRequest projectionScsvPostRequest, SecurityContext securityContext
	) {
		return Response.serverError().status(501).build();
	}

	private InputStream getResourceFile(String fileName) throws URISyntaxException {
		String resourceFilePath = "VDYP7Console-sample-files/hcsv/vdyp-240/" + fileName;
		return getClass().getClassLoader().getResourceAsStream(resourceFilePath);
	}
}
