package ca.bc.gov.nrs.vdyp.vri.model;

import java.util.function.Consumer;

import ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies;
import ca.bc.gov.nrs.vdyp.model.LayerType;

public class VriSpecies extends BaseVdypSpecies {

	public VriSpecies(String polygonIdentifier, LayerType layer, String genus, float percentGenus) {
		super(polygonIdentifier, layer, genus, percentGenus);
	}

	public VriSpecies(VriSpecies toCopy) {
		super(toCopy);
	}

	/**
	 * Accepts a configuration function that accepts a builder to configure.
	 *
	 * <pre>
	 * FipSpecies myLayer = FipSpecies.build(builder-&gt; {
			builder.polygonIdentifier(polygonId);
			builder.layerType(LayerType.VETERAN);
			builder.genus("B");
			builder.percentGenus(6f);
	 * })
	 * </pre>
	 *
	 * @param config The configuration function
	 * @return The object built by the configured builder.
	 * @throws IllegalStateException if any required properties have not been set by
	 *                               the configuration function.
	 */
	public static VriSpecies build(Consumer<Builder> config) {
		var builder = new Builder();
		config.accept(builder);
		return builder.build();
	}

	/**
	 * Builds a species and adds it to the layer.
	 *
	 * @param layer  Layer to create the species for.
	 * @param config Configuration function for the builder.
	 * @return the new species.
	 */
	public static VriSpecies build(VriLayer layer, Consumer<Builder> config) {
		var result = build(builder -> {
			builder.polygonIdentifier(layer.getPolygonIdentifier());
			builder.layerType(layer.getLayer());

			config.accept(builder);
		});
		layer.getSpecies().put(result.getGenus(), result);
		return result;
	}

	public static class Builder extends BaseVdypSpecies.Builder<VriSpecies> {

		@Override
		protected VriSpecies doBuild() {
			return new VriSpecies(
					this.polygonIdentifier.get(), //
					this.layer.get(), //
					this.genus.get(), //
					this.percentGenus.get()
			);
		}
	}
}
