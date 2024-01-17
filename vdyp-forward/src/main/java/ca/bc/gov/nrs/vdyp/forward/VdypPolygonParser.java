package ca.bc.gov.nrs.vdyp.forward;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.bc.gov.nrs.vdyp.forward.model.FipMode;
import ca.bc.gov.nrs.vdyp.forward.model.VdypPolygon;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.AbstractStreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.LineParser;
import ca.bc.gov.nrs.vdyp.io.parse.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.ValueParser;
import ca.bc.gov.nrs.vdyp.model.BecDefinition;

public class VdypPolygonParser implements ControlMapValueReplacer<StreamingParserFactory<VdypPolygon>, String> {

	public static final String CONTROL_KEY = "VDYP_POLYGONS";

	private static final String DESCRIPTION = "DESCRIPTION"; // POLYDESC
	private static final String BIOGEOCLIMATIC_ZONE = "BIOGEOCLIMATIC_ZONE"; // BEC
	private static final String FOREST_INVENTORY_ZONE = "FOREST_INVENTORY_ZONE"; // FIZ
	private static final String PERCENT_FOREST_LAND = "PERCENT_FOREST_LAND"; // PCTFLAND
	private static final String INVENTORY_TYPE_GROUP = "INVENTORY_TYPE_GROUP"; // ITG
	private static final String BASAL_AREA_GROUP = "BASAL_AREA_GROUP"; // GRPBA1
	private static final String FIP_MODE = "FIP_MODE"; // MODEfip

	@Override
	public String getControlKey() {
		return CONTROL_KEY;
	}

	private static Pattern descriptionPattern = Pattern.compile(".*([\\d]{4}$)");
	
	@Override
	public StreamingParserFactory<VdypPolygon>
			map(String fileName, FileResolver fileResolver, Map<String, Object> control)
					throws IOException, ResourceParseException {
		return () -> {
			var lineParser = new LineParser()
					.strippedString(25, DESCRIPTION).space(1)
					.value(4, BIOGEOCLIMATIC_ZONE, ValueParser.BEC).space(1)
					.value(1, FOREST_INVENTORY_ZONE, ValueParser.CHARACTER) // TODO: add ValueParser.FIZ
					.value(6, PERCENT_FOREST_LAND, ValueParser.optional(ValueParser.PERCENTAGE))
					.value(3, INVENTORY_TYPE_GROUP, ValueParser.optional(ValueParser.INTEGER))
					.value(3, BASAL_AREA_GROUP, ValueParser.optional(ValueParser.INTEGER))
					.value(3, FIP_MODE, ValueParser.optional(ValueParser.INTEGER));

			var is = fileResolver.resolve(fileName);

			return new AbstractStreamingParser<VdypPolygon>(is, lineParser, control) {

				@SuppressWarnings("unchecked")
				@Override
				protected VdypPolygon convert(Map<String, Object> entry) throws ResourceParseException {
					var description = (String)entry.get(DESCRIPTION);
					var bec = (BecDefinition) entry.get(BIOGEOCLIMATIC_ZONE);
					var fizId = (Character) entry.get(FOREST_INVENTORY_ZONE);
					var percentForestLand = (Float) entry.get(PERCENT_FOREST_LAND);
					var inventoryTypeGroup = (Optional<Integer>) entry.get(INVENTORY_TYPE_GROUP);
					var basalAreaGroup = (Optional<Integer>) entry.get(BASAL_AREA_GROUP);
					var fipMode = (Optional<Integer>) entry.get(FIP_MODE);
					
					Integer year;
					Matcher matcher = descriptionPattern.matcher(description);
					if (matcher.matches() && matcher.group(1) != null) {
						year = Integer.parseInt(matcher.group(1));
					}
					else {
						throw new ResourceParseException("Polygon description " + description + " did not end with a four-digit year value."
								+ " Instead, it ended with " + description.substring(description.length() - 4, description.length()));
					}
					
					return new VdypPolygon(
							description, year, bec, fizId, percentForestLand, inventoryTypeGroup,
							basalAreaGroup, fipMode.flatMap(FipMode::getByCode));
				}

			};
		};
	}

}
