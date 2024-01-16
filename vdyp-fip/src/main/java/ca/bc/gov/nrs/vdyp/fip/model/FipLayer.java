package ca.bc.gov.nrs.vdyp.fip.model;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import ca.bc.gov.nrs.vdyp.model.BaseVdypLayer;
import ca.bc.gov.nrs.vdyp.model.LayerType;

public class FipLayer extends BaseVdypLayer<FipSpecies> {

	static final String POLYGON_IDENTIFIER = "POLYGON_IDENTIFIER"; // POLYDESC
	static final String LAYER = "LAYER"; // LAYER
	static final String AGE_TOTAL = "AGE_TOTAL"; // AGETOT
	static final String HEIGHT = "HEIGHT"; // HT
	static final String SITE_INDEX = "SITE_INDEX"; // SI
	static final String CROWN_CLOSURE = "CROWN_CLOSURE"; // CC
	static final String SITE_SP0 = "SITE_SP0"; // SITESP0
	static final String SITE_SP64 = "SITE_SP64"; // SITESP64
	static final String YEARS_TO_BREAST_HEIGHT = "YEARS_TO_BREAST_HEIGHT"; // YTBH
	static final String INVENTORY_TYPE_GROUP = "INVENTORY_TYPE_GROUP"; // ITGFIP
	static final String BREAST_HEIGHT_AGE = "BREAST_HEIGHT_AGE"; // AGEBH

	private float siteIndex; // FIPL_1/SI_L1 or FIPL_V/SI_V1
	private float crownClosure; // FIPL_1/CC_L1 or FIP:_V/CC_V1
	private String siteGenus; // FIPL_1A/SITESP0_L1 or FIPL_VA/SITESP0_L1
	private String siteSpecies; // FIPL_1A/SITESP64_L1 or FIPL_VA/SITESP64_L1

	public FipLayer(
			String polygonIdentifier, LayerType layer, float ageTotal, float yearsToBreastHeight, float height,
			float siteIndex, float crownClosure, String siteGenus, String siteSpecies
	) {
		super(polygonIdentifier, layer, ageTotal, yearsToBreastHeight, height);
		this.siteIndex = siteIndex;
		this.crownClosure = crownClosure;
		this.siteGenus = siteGenus;
		this.siteSpecies = siteGenus;
	}

	public float getSiteIndex() {
		return siteIndex;
	}

	public float getCrownClosure() {
		return crownClosure;
	}

	public String getSiteSp0() {
		return siteGenus;
	}

	public String getSiteSp64() {
		return siteSpecies;
	}

	public void setSiteIndex(float siteIndex) {
		this.siteIndex = siteIndex;
	}

	public void setCrownClosure(float crownClosure) {
		this.crownClosure = crownClosure;
	}

	public void setSiteGenus(String sireSp0) {
		this.siteGenus = sireSp0;
	}

	public void setSiteSpecies(String siteSp64) {
		this.siteSpecies = siteSp64;
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
	public static FipLayer build(Consumer<Builder> config) {
		var builder = new Builder();
		config.accept(builder);
		return builder.build();
	}

	public static class Builder extends BaseVdypLayer.Builder<FipLayer, FipSpecies> {
		protected Optional<Float> siteIndex = Optional.empty();
		protected Optional<Float> crownClosure = Optional.empty();
		protected Optional<String> siteGenus = Optional.empty();
		protected Optional<String> siteSpecies = Optional.empty();

		public Builder siteIndex(float siteIndex) {
			this.siteIndex = Optional.of(siteIndex);
			return this;
		}

		public Builder crownClosure(float crownClosure) {
			this.crownClosure = Optional.of(crownClosure);
			return this;
		}

		public Builder siteGenus(String siteGenus) {
			this.siteGenus = Optional.of(siteGenus);
			return this;
		}

		public Builder siteSpecies(String siteSpecies) {
			this.siteSpecies = Optional.of(siteSpecies);
			return this;
		}

		@Override
		protected void check(Collection<String> errors) {
			super.check(errors);
			requirePresent(siteIndex, "siteIndex", errors);
			requirePresent(crownClosure, "crownClosure", errors);
			requirePresent(siteGenus, "siteGenus", errors);
			requirePresent(siteSpecies, "siteSpecies", errors);

		}

		@Override
		protected FipLayer doBuild() {
			return (new FipLayer(
					polygonIdentifier.get(), //
					layer.get(), //
					ageTotal.get(), //
					yearsToBreastHeight.get(), //
					height.get(), //
					siteIndex.get(), //
					crownClosure.get(), //
					siteGenus.get(), //
					siteSpecies.get()
			));
		}

	}
}
