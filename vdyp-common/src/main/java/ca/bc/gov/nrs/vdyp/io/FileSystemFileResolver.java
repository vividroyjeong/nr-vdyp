package ca.bc.gov.nrs.vdyp.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileSystemFileResolver implements FileResolver {

	@Override
	public InputStream resolveForInput(String filename) throws IOException {
		return new FileInputStream(filename);
	}

	@Override
	public OutputStream resolveForOutput(String filename) throws IOException {
		return new FileOutputStream(filename);
	}

	@Override
	public String toString(String filename) throws IOException {
		return String.format("file:%s", filename);
	}

}