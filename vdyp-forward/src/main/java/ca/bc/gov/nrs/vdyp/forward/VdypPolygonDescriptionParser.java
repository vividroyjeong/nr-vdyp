package ca.bc.gov.nrs.vdyp.forward;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygonDescription;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.LineParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.AbstractStreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;

public class VdypPolygonDescriptionParser implements ControlMapValueReplacer<Object, String> {

	private static final String DESCRIPTION = "DESCRIPTION"; // POLYDESC

	@Override
	public ControlKey getControlKey() {
		return ControlKey.FORWARD_GROWTO_INPUT;
	}

	private static Pattern descriptionPattern = Pattern.compile(".*([\\d]{4}$)");

	@Override
	public StreamingParserFactory<VdypPolygonDescription>
			map(String fileName, FileResolver fileResolver, Map<String, Object> control)
					throws IOException, ResourceParseException {
		return () -> {
			var lineParser = new LineParser() {
				@Override
				public boolean isStopLine(String line) {
					return line.substring(0, Math.min(25, line.length())).trim().length() == 0;
				}
			}.strippedString(25, DESCRIPTION);

			var is = fileResolver.resolveForInput(fileName);

			return new AbstractStreamingParser<VdypPolygonDescription>(is, lineParser, control) {

				@Override
				protected VdypPolygonDescription convert(Map<String, Object> entry) throws ResourceParseException {
					var description = (String) entry.get(DESCRIPTION);

					Integer year;
					Matcher matcher = descriptionPattern.matcher(description);
					if (matcher.matches() && matcher.group(1) != null) {
						year = Integer.parseInt(matcher.group(1));
					} else {
						throw new ResourceParseException(
								"Polygon description " + description + " did not end with a four-digit year value."
										+ " Instead, it ended with "
										+ description.substring(description.length() - 4, description.length())
						);
					}

					return new VdypPolygonDescription(description, year);
				}

			};
		};
	}

	@Override
	public ValueParser<Object> getValueParser() {
		return FILENAME;
	}
}
