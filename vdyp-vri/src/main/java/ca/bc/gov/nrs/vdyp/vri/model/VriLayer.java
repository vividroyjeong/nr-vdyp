package ca.bc.gov.nrs.vdyp.vri.model;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import ca.bc.gov.nrs.vdyp.model.BaseVdypLayer;
import ca.bc.gov.nrs.vdyp.model.LayerType;

public class VriLayer extends BaseVdypLayer<VriSpecies, VriSite> {

	private float crownClosure; // FIPL_1/CC_L1 or FIP:_V/CC_V1

	public VriLayer(
			String polygonIdentifier, LayerType layer, Optional<Integer> inventoryTypeGroup, float crownClosure
	) {
		super(polygonIdentifier, layer, inventoryTypeGroup);
		this.crownClosure = crownClosure;
	}

	public float getCrownClosure() {
		return crownClosure;
	}

	public void setCrownClosure(float crownClosure) {
		this.crownClosure = crownClosure;
	}

	/**
	 * Accepts a configuration function that accepts a builder to configure.
	 *
	 * <pre>
	 * FipLayer myLayer = FipLayer.build(builder-&gt; {
			builder.polygonIdentifier(polygonId);
			builder.layerType(LayerType.VETERAN);
			builder.ageTotal(8f);
			builder.yearsToBreastHeight(7f);
			builder.height(6f);

			builder.siteIndex(5f);
			builder.crownClosure(0.9f);
			builder.siteGenus("B");
			builder.siteSpecies("B");
	 * })
	 * </pre>
	 *
	 * @param config The configuration function
	 * @return The object built by the configured builder.
	 * @throws IllegalStateException if any required properties have not been set by
	 *                               the configuration function.
	 */

	public static VriLayer build(Consumer<Builder> config) {
		var builder = new Builder();
		config.accept(builder);
		return builder.build();
	}

	public static VriLayer build(VriPolygon polygon, Consumer<Builder> config) {
		var layer = build(builder -> {
			builder.polygonIdentifier(polygon.getPolygonIdentifier());
			config.accept(builder);
		});
		polygon.getLayers().put(layer.getLayer(), layer);
		return layer;
	}

	public static class Builder extends BaseVdypLayer.Builder<VriLayer, VriSpecies, VriSite> {
		protected Optional<Float> crownClosure = Optional.empty();
		protected Optional<String> siteSpecies = Optional.empty();

		public Builder crownClosure(float crownClosure) {
			this.crownClosure = Optional.of(crownClosure);
			return this;
		}

		public Builder siteSpecies(String siteSpecies) {
			this.siteSpecies = Optional.of(siteSpecies);
			return this;
		}

		@Override
		protected void check(Collection<String> errors) {
			super.check(errors);
			requirePresent(crownClosure, "crownClosure", errors);
			requirePresent(siteSpecies, "siteSpecies", errors);
		}

		@Override
		protected VriLayer doBuild() {

			return (new VriLayer(
					polygonIdentifier.get(), //
					layer.get(), //
					inventoryTypeGroup, //
					crownClosure.get()
			));
		}

		@Override
		protected VriSpecies
				buildSpecies(Consumer<ca.bc.gov.nrs.vdyp.model.BaseVdypSpecies.Builder<VriSpecies>> config) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected VriSite buildSite(Consumer<ca.bc.gov.nrs.vdyp.model.BaseVdypSite.Builder<VriSite>> config) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
