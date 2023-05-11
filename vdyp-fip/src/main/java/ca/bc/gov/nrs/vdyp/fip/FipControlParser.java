package ca.bc.gov.nrs.vdyp.fip;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;

import ca.bc.gov.nrs.vdyp.io.parse.ControlFileParser;
import ca.bc.gov.nrs.vdyp.io.parse.ResourceParseException;

/**
 * Parser for FIP control files
 * 
 * @author Kevin Smith, Vivid Solutions
 *
 */
public class FipControlParser {
	
	ControlFileParser controlParser = new ControlFileParser();
	
	Map<String, ?> parse (Path inputFile) throws IOException {
		try(var is = Files.newInputStream(inputFile)) {
			return parse(is, fileName->Files.newInputStream(inputFile.resolveSibling(fileName)));
		}
	}
	
	Map<String, ?> parse (Class<?> klazz, String resourceName) throws IOException {
		try(var is = klazz.getResourceAsStream(resourceName)) {
			return parse(is, ioExceptionOnNull(klazz::getResourceAsStream));
		}
	}
	
	Map<String, ?> parse(InputStream is, FileResolver fileResolver) throws IOException, ResourceParseException {
		var map = controlParser.parse(is);
		return map;
	}
	
	@FunctionalInterface
	static interface FileResolver {
		InputStream resolve(String filename) throws IOException;
	}
	
	static FileResolver ioExceptionOnNull(Function<String, InputStream> baseResolver) {
		return fileName->{
			var result = baseResolver.apply(fileName);
			if(result==null) {
				throw new IOException("Could not find "+fileName);
			}
			return result;
		};
	}
}
