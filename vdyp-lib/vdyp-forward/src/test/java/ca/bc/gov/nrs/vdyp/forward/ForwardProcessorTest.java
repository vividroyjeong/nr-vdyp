package ca.bc.gov.nrs.vdyp.forward;

import static ca.bc.gov.nrs.vdyp.forward.ForwardPass.PASS_1;
import static ca.bc.gov.nrs.vdyp.forward.ForwardPass.PASS_2;
import static ca.bc.gov.nrs.vdyp.forward.ForwardPass.PASS_3;
import static ca.bc.gov.nrs.vdyp.forward.ForwardPass.PASS_4;
import static ca.bc.gov.nrs.vdyp.forward.ForwardPass.PASS_5;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.nrs.vdyp.application.ProcessingException;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.ZipOutputFileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.test.TestUtils;

class ForwardProcessorTest {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ForwardProcessorTest.class);

	private static Set<ForwardPass> vdypPassSet = new HashSet<>(Arrays.asList(PASS_1, PASS_2, PASS_3, PASS_4, PASS_5));

	@Test
	void test() throws IOException, ResourceParseException, ProcessingException {

		ForwardProcessor fp = new ForwardProcessor();

		FileResolver inputFileResolver = TestUtils.fileResolver(TestUtils.class);

		var outputResolver = new ZipOutputFileResolver();

		fp.run(inputFileResolver, outputResolver, List.of("VDYP.CTR"), vdypPassSet);

		Path resourceDirectory = Paths.get("src", "test", "resources", "output");
		Files.createDirectories(resourceDirectory);

		Path zipFile = Paths.get(resourceDirectory.toString(), this.getClass().getSimpleName() + ".zip");
		Files.deleteIfExists(zipFile);

		outputResolver.generate(zipFile);

		InputStream os = outputResolver.generateStream();

		byte[] zipFileBytes = Files.readAllBytes(zipFile);
		byte[] zipStreamBytes = os.readAllBytes();

		assertTrue(zipFileBytes.length == zipStreamBytes.length);
		assertTrue(Arrays.equals(zipFileBytes, zipStreamBytes));

	}
}
