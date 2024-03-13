package ca.bc.gov.nrs.vdyp.common;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.model.GenusDefinition;

public class GenusDefinitionMap {
	private Map<String, GenusDefinition> genusByAliasMap = new HashMap<>();

	public GenusDefinitionMap(List<GenusDefinition> genusDefinitionList) {

		for (GenusDefinition g : genusDefinitionList) {
			genusByAliasMap.put(g.getAlias(), g);
		}
	}

	public GenusDefinition get(String alias) {
		GenusDefinition g = genusByAliasMap.get(alias);
		if (g == null) {
			throw new IllegalArgumentException(
					MessageFormat.format("Unable to find GenusDefinition for alias {}", alias)
			);
		}
		return g;
	}
}
