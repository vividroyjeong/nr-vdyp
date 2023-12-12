package ca.bc.gov.nrs.vdyp.fip.integeration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import ca.bc.gov.nrs.vdyp.fip.FipControlParserTest;
import ca.bc.gov.nrs.vdyp.fip.FipStart;
import ca.bc.gov.nrs.vdyp.fip.ProcessingException;
import ca.bc.gov.nrs.vdyp.io.FileSystemFileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.ControlFileParserTest;
import ca.bc.gov.nrs.vdyp.io.parse.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.write.ControlFileWriter;

class ITFipStart {

	@TempDir
	Path configDir;

	@TempDir
	Path inputDir;

	@TempDir
	Path outputDir;

	private Path baseControlFile;
	private Path ioControlFile;

	static Path copyResource(Class<?> klazz, String path, Path destination) throws IOException {
		try {
			var resourceUri = klazz.getResource(path);
			assertThat("Could not find resource " + path, resourceUri, notNullValue());
			Path source = Paths.get(resourceUri.toURI());
			Path result = destination.resolve(path);
			Files.copy(source, result);
			return result;
		} catch (URISyntaxException e) {
			Assumptions.abort(e.getMessage());
			return null;
		}

	}

	private static final String[] COE_FILES = new String[] { "Becdef.dat", "SP0DEF_v0.dat", "VGRPDEF1.DAT", "DGRP.DAT",
			"BGRP.DAT", "SIEQN.PRM", "SIAGEMAX.PRM", "GRPBA1.DAT", "GMODBA1.DAT", "FIPSTKR.PRM", "REGBA25.coe",
			"REGDQ26.coe", "UPPERB02.COE", "REGYHLP.COE", "REGYHLPA.COE", "REGYHLPB.DAT", "REGHL.COE", "REGDQI04.COE",
			"COMPLIM.COE", "REGBAC.DAT", "REGDQC.DAT", "REGPR1C.DAT", "REGBA2C.DAT", "REGDQ4C.DAT", "REGHL1C.DAT",
			"REGV1C.DAT", "VTOTREG4.COE", "REGVU.COE", "REGVCU.COE", "REGVDU.COE", "REGVWU.COE", "REGBREAK.COE",
			"VETVOL1.DAT", "VETDQ2.DAT", "REGBAV01.COE", "mod19813.prm" };

	private static final String[] INPUT_FILES = new String[] { "fip_l1.dat", "fip_ls1.dat", "fip_p1.dat" };

	private static final String POLYGON_OUTPUT_NAME = "vri_poly.dat";
	private static final String SPECIES_OUTPUT_NAME = "vri_spec.dat";
	private static final String UTILIZATION_OUTPUT_NAME = "vri_util.dat";

	@BeforeEach
	void init() throws IOException {
		baseControlFile = copyResource(ControlFileParserTest.class, "FIPSTART.CTR", configDir);
		Files.createDirectory(configDir.resolve("coe"));
		for (String filename : COE_FILES) {
			copyResource(ControlFileParserTest.class, "coe/" + filename, configDir);
		}
		for (String filename : INPUT_FILES) {
			copyResource(FipControlParserTest.class, filename, inputDir);
		}

		// Create a second control file pointing to the input and output
		ioControlFile = inputDir.resolve("fip.ctr");
		try (
				var os = Files.newOutputStream(ioControlFile); //
				var writer = new ControlFileWriter(os);
		) {
			writer.writeComment("Generated supplementarty control file for integration testing");
			writer.writeBlank();
			writer.writeComment("Inputs");
			writer.writeBlank();
			writer.writeEntry(11, inputDir.resolve("fip_p1.dat").toString(), "FIP Polygon Input");
			writer.writeEntry(12, inputDir.resolve("fip_l1.dat").toString(), "FIP Layer Input");
			writer.writeEntry(13, inputDir.resolve("fip_ls1.dat").toString(), "FIP Species Input");
			writer.writeBlank();
			writer.writeComment("Outputs");
			writer.writeBlank();
			writer.writeEntry(15, outputDir.resolve(POLYGON_OUTPUT_NAME).toString(), "VRI Polygon Output");
			writer.writeEntry(16, outputDir.resolve(SPECIES_OUTPUT_NAME).toString(), "VRI Species Output");
			writer.writeEntry(18, outputDir.resolve(UTILIZATION_OUTPUT_NAME).toString(), "VRI Utilization Output");

		}
	}

	@Test
	void noControlFile() throws IOException, ResourceParseException, ProcessingException {
		try (var app = new FipStart();) {

			var resolver = new FileSystemFileResolver(configDir);

			Assertions.assertThrows(IllegalArgumentException.class, () -> app.init(resolver));
		}
	}

	@Test
	void controlFileDoesntExist() throws IOException, ResourceParseException, ProcessingException {
		try (var app = new FipStart();) {

			var resolver = new FileSystemFileResolver(configDir);

			Assertions.assertThrows(NoSuchFileException.class, () -> app.init(resolver, "FAKE"));
		}
	}

	public void assertFileExists(Path path) {
		assertTrue(Files.exists(path), path + " does not exist");
	}

	@Test
	void controlFile() throws IOException, ResourceParseException, ProcessingException {
		try (var app = new FipStart();) {

			var resolver = new FileSystemFileResolver(configDir);

			app.init(resolver, baseControlFile.toString(), ioControlFile.toString());

			app.process();

			assertFileExists(outputDir.resolve(POLYGON_OUTPUT_NAME));
			assertFileExists(outputDir.resolve(SPECIES_OUTPUT_NAME));
			assertFileExists(outputDir.resolve(UTILIZATION_OUTPUT_NAME));
		}
	}
}
