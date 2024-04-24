package ca.bc.gov.nrs.vdyp.model;

import java.util.Objects;
import java.util.Optional;

public class GenusDefinition extends AliasedEntity {

	private final Optional<Integer> preference;

	public GenusDefinition(String alias, Optional<Integer> preference, String name) {
		super(alias, name);
		this.preference = preference;
	}

	public Optional<Integer> getPreference() {
		return preference;
	}
	
	@Override
	public int hashCode() {
		return ((this.getAlias().hashCode() * 17) + this.getName().hashCode()) * 17 + preference.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof GenusDefinition that) {
			return Objects.equals(this.preference, that.preference) && super.equals(that);
		} else {
			return false;
		}
	}
}
