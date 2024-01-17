package ca.bc.gov.nrs.vdyp.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * Used to create a FileResolver that's relative to a given path. If the resolver
 * is asked to resolve a filename that's not absolute, the filename is prefixed 
 * with the given path before resolution.
 */
public class FileSystemRelativeFileResolver implements FileResolver {
	
	private final String path;
	
	public FileSystemRelativeFileResolver(String path) {
		this.path = path;
	}

	@Override
	public InputStream resolve(String filename) throws IOException {
		return new FileInputStream(buildAbsolutePath(filename));
	}

	@Override
	public String toString(String filename) throws IOException {
		return String.format("file:%s", buildAbsolutePath(filename));
	}
	
	private String buildAbsolutePath(String filename) {
		filename = filename.replace('\\', '/');
		
		if (!Path.of(filename).isAbsolute()) {
			filename = Path.of(path, filename).toString();
		}
		
		return filename;
	}
}