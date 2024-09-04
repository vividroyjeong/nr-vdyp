package ca.bc.gov.nrs.vdyp.io;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.jupiter.api.Test;

public class ZipOutputFileResolverTest {

	@Test
	void testZipOutputFileResolver() throws IOException {

		ZipOutputFileResolver resolver = new ZipOutputFileResolver();

		for (int i = 0; i < 5; i++) {
			OutputStream os = resolver.resolveForOutput("file" + i);
			os.write(String.format("%d", i).getBytes());
		}

		Path zipFile = Files.createTempFile(this.getClass().getName(), ".zip");
		try {
			resolver.generate(zipFile);

			System.out.println("Output zip file written to " + zipFile.toString());

			try (ZipFile zip = new ZipFile(zipFile.toFile())) {
				var entries = zip.entries();

				byte[] buffer = new byte[16];
				while (entries.hasMoreElements()) {
					ZipEntry e = entries.nextElement();

					InputStream is = zip.getInputStream(e);
					int nBytesRead = is.read(buffer, 0, 10);
					assertTrue(nBytesRead == 1);
					String fileNumber = e.getName().substring(e.getName().length() - 1, e.getName().length());
					assertTrue(new String(Arrays.copyOf(buffer, nBytesRead)).equals(fileNumber));
				}
			}
		} finally {
			Files.delete(zipFile);
		}
	}
}
