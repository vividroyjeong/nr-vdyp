package ca.bc.gov.nrs.vdyp.io.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@FunctionalInterface
public interface ResourceParser<T> {
	T parse(InputStream is) throws IOException, ResourceParseException;
	
	default T parse (Class<?> klazz, String resourcePath) throws IOException, ResourceParseException {
		try (var is = klazz.getResourceAsStream(resourcePath)) {
			return parse(is);
		}
	}
	
	default T parse (Path resourcePath) throws IOException, ResourceParseException {
		try (InputStream is = Files.newInputStream(resourcePath)) {
			return parse(is);
		}
	}
}
