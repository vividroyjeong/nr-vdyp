package ca.bc.gov.nrs.vdyp.backend.v1.api;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.jboss.logmanager.ExtLogRecord.FormatStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.backend.v1.model.ProjectionDcsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.model.ProjectionHcsvPostRequest;
import ca.bc.gov.nrs.vdyp.backend.v1.model.ProjectionScsvPostRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

public class ProjectionApiService {

	private static final Logger logger = LoggerFactory.getLogger(ProjectionApiService.class);

	public Response projectionDcsvPost(
			@Valid ProjectionDcsvPostRequest projectionDcsvPostRequest, SecurityContext securityContext
	) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	public Response projectionHcsvPost(
			@Valid ProjectionHcsvPostRequest projectionHcsvPostRequest, SecurityContext securityContext
	) throws NotFoundException {
		try {
			logger.info("<projectionHcsvPost");

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zipOut = new ZipOutputStream(baos);

			ZipEntry yieldTableZipEntry = new ZipEntry("Output_YldTbl.csv");
			zipOut.putNextEntry(yieldTableZipEntry);
			var yieldTablePath = getResourceFile("Output_YldTbl.csv");
			zipOut.write(Files.readAllBytes(yieldTablePath));

			ZipEntry logOutputEntry = new ZipEntry("Output_Log.txt");
			zipOut.putNextEntry(logOutputEntry);
			var logFilePath = getResourceFile("Output_Log.txt");
			zipOut.write(Files.readAllBytes(logFilePath));

			ZipEntry errorOutputZipEntry = new ZipEntry("Output_Error.txt");
			zipOut.putNextEntry(errorOutputZipEntry);
			var errorFilePath = getResourceFile("Output_Error.txt");
			zipOut.write(Files.readAllBytes(errorFilePath));

			zipOut.close();

			byte[] resultingByteArray = baos.toByteArray();

			logger.info(">projectionHcsvPost ({} bytes returned)", resultingByteArray.length);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH.mm.ss");
			var outputFileName = "vdyp-output-" + java.time.LocalDateTime.now().format(formatter);
			return Response.ok(resultingByteArray)
					.header("content-disposition", "attachment;filename=\"" + outputFileName + "\"").build();

		} catch (IOException | URISyntaxException e) {
			logger.error(">projectionHcsvPost failure of type {0}", e.getMessage());

			return Response.serverError().entity("Unable to load canned output file(s)").build();
		}
	}

	public Response projectionScsvPost(
			@Valid ProjectionScsvPostRequest projectionScsvPostRequest, SecurityContext securityContext
	) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	private Path getResourceFile(String fileName) throws URISyntaxException {
		String resourceFilePath = "VDYP7Console-sample-files/hcsv/vdyp-240/" + fileName;
		URL resourceUrl = getClass().getClassLoader().getResource(resourceFilePath);
		return Path.of(resourceUrl.toURI());
	}
}
