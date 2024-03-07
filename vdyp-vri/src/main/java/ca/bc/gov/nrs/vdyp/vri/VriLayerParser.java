package ca.bc.gov.nrs.vdyp.vri;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import ca.bc.gov.nrs.vdyp.common.ControlKey;
import ca.bc.gov.nrs.vdyp.common.ValueOrMarker;
import ca.bc.gov.nrs.vdyp.vri.model.VriLayer;
import ca.bc.gov.nrs.vdyp.io.EndOfRecord;
import ca.bc.gov.nrs.vdyp.io.FileResolver;
import ca.bc.gov.nrs.vdyp.io.parse.common.LineParser;
import ca.bc.gov.nrs.vdyp.io.parse.common.ResourceParseException;
import ca.bc.gov.nrs.vdyp.io.parse.control.ControlMapValueReplacer;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.AbstractStreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.GroupingStreamingParser;
import ca.bc.gov.nrs.vdyp.io.parse.streaming.StreamingParserFactory;
import ca.bc.gov.nrs.vdyp.io.parse.value.ControlledValueParser;
import ca.bc.gov.nrs.vdyp.io.parse.value.ValueParser;
import ca.bc.gov.nrs.vdyp.model.LayerType;

public class VriLayerParser
		implements ControlMapValueReplacer<StreamingParserFactory<Map<LayerType, VriLayer>>, String> {

	static final String LAYER = "LAYER"; // LAYER
	static final String AGE_TOTAL = "AGE_TOTAL"; // AGETOT
	static final String HEIGHT = "HEIGHT"; // HT
	static final String SITE_INDEX = "SITE_INDEX"; // SI
	static final String CROWN_CLOSURE = "CROWN_CLOSURE"; // CC
	static final String SITE_SP0 = "SITE_SP0"; // SITESP0
	static final String SITE_SP64 = "SITE_SP64"; // SITESP64
	static final String YEARS_TO_BREAST_HEIGHT = "YEARS_TO_BREAST_HEIGHT"; // YTBH
	static final String STOCKING_CLASS = "STOCKING_CLASS"; // STK
	static final String INVENTORY_TYPE_GROUP = "INVENTORY_TYPE_GROUP"; // ITGFIP
	static final String BREAST_HEIGHT_AGE = "BREAST_HEIGHT_AGE"; // AGEBH
	static final String SITE_CURVE_NUMBER = "SITE_CURVE_NUMBER"; // SCN

	@Override
	public ControlKey getControlKey() {
		return ControlKey.VRI_YIELD_LAYER_INPUT;
	}

	@Override
	public StreamingParserFactory<Map<LayerType, VriLayer>>
			map(String fileName, FileResolver fileResolver, Map<String, Object> control)
					throws IOException, ResourceParseException {
		return () -> {
			var lineParser = new LineParser() //
					.strippedString(25, VriPolygonParser.POLYGON_IDENTIFIER).space(1) //
					.value(
							1, LAYER,
							ValueParser.valueOrMarker(
									ValueParser.LAYER,
									ValueParser.optionalSingleton("Z"::equals, EndOfRecord.END_OF_RECORD)
							)
					) //
					.floating(4, AGE_TOTAL) //
					.floating(5, HEIGHT) //
					.floating(5, SITE_INDEX) //
					.floating(5, CROWN_CLOSURE) //
					.space(3) //
					.value(2, SITE_SP0, ControlledValueParser.optional(ValueParser.GENUS)) //
					.value(3, SITE_SP64, ControlledValueParser.optional(ValueParser.STRING)) //
					.floating(5, YEARS_TO_BREAST_HEIGHT) //
					.value(1, STOCKING_CLASS, ValueParser.optional(ValueParser.CHARACTER)) //
					.space(2) //
					.value(4, INVENTORY_TYPE_GROUP, ValueParser.optional(ValueParser.INTEGER)) //
					.space(1) //
					.value(6, BREAST_HEIGHT_AGE, ValueParser.optional(ValueParser.FLOAT)) //
					.value(3, SITE_CURVE_NUMBER, ValueParser.optional(ValueParser.INTEGER));

			var is = fileResolver.resolveForInput(fileName);

			var delegateStream = new AbstractStreamingParser<ValueOrMarker<Optional<VriLayer>, EndOfRecord>>(
					is, lineParser, control
			) {

				@SuppressWarnings({ "unchecked" })
				@Override
				protected ValueOrMarker<Optional<VriLayer>, EndOfRecord> convert(Map<String, Object> entry) {
					var polygonId = (String) entry.get(VriPolygonParser.POLYGON_IDENTIFIER);
					var layer = (ValueOrMarker<Optional<LayerType>, EndOfRecord>) entry.get(LAYER);
					var crownClosure = (float) entry.get(CROWN_CLOSURE);

					var vmBuilder = new ValueOrMarker.Builder<Optional<VriLayer>, EndOfRecord>();
					return layer.handle(l -> {
						VriLayer fipLayer = VriLayer.build(flBuilder -> {
							flBuilder.polygonIdentifier(polygonId);
							flBuilder.layerType(LayerType.VETERAN);

							flBuilder.crownClosure(crownClosure);
						});

						return vmBuilder.value(Optional.of(fipLayer));

					}, vmBuilder::marker);
				}

			};

			return new GroupingStreamingParser<Map<LayerType, VriLayer>, ValueOrMarker<Optional<VriLayer>, EndOfRecord>>(
					delegateStream
			) {

				@Override
				protected boolean skip(ValueOrMarker<Optional<VriLayer>, EndOfRecord> nextChild) {
					return nextChild.getValue().map(x -> x.map(layer -> {
						return false;
					}).orElse(true)) // If the layer is not present (Unknown layer type) ignore
							.orElse(false); // If it's a marker, let it through so the stop method can see it.
				}

				@Override
				protected boolean stop(ValueOrMarker<Optional<VriLayer>, EndOfRecord> nextChild) {
					return nextChild.isMarker();
				}

				@Override
				protected Map<LayerType, VriLayer>
						convert(List<ValueOrMarker<Optional<VriLayer>, EndOfRecord>> children) {
					return children.stream().map(ValueOrMarker::getValue).map(Optional::get) // Should never be empty as
							// we've filtered out
							// markers
							.flatMap(Optional::stream) // Skip if empty (and unknown layer type)
							.collect(Collectors.toMap(VriLayer::getLayer, x -> x));
				}

			};
		};
	}

	@Override
	public ValueParser<Object> getValueParser() {
		return FILENAME;
	}
}
