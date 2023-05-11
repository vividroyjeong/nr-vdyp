package ca.bc.gov.nrs.vdyp.io.parse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A parser for a multi-line recource
 * 
 * @author Kevin Smith, Vivid Solutions
 *
 * @param <T>
 */
@FunctionalInterface
public interface ResourceParser<T> {
	/**
	 * Parse an InputStream
	 * @param is
	 * @return The parsed resource
	 * @throws IOException if there is an error communicating with the input stream
	 * @throws ResourceParseException if there is a problem with the content of the resource
	 */
	T parse(InputStream is) throws IOException, ResourceParseException;
	
	/**
	 * Parse a resource from the classpath
	 * @param klazz
	 * @param resourcePath
	 * @return The parsed resource
	 * @throws IOException if there is an error communicating with the input stream
	 * @throws ResourceParseException if there is a problem with the content of the resource
	 */
	default T parse (Class<?> klazz, String resourcePath) throws IOException, ResourceParseException {
		try (var is = klazz.getResourceAsStream(resourcePath)) {
			return parse(is);
		}
	}
	
	/**
	 * Parse a resource from a file
	 * @param resourcePath
	 * @return The parsed resource
	 * @throws IOException if there is an error communicating with the input stream
	 * @throws ResourceParseException if there is a problem with the content of the resource
	 */
	default T parse (Path resourcePath) throws IOException, ResourceParseException {
		try (InputStream is = Files.newInputStream(resourcePath)) {
			return parse(is);
		}
	}
}
