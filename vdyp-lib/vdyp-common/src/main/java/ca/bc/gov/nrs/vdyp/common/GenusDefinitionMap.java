package ca.bc.gov.nrs.vdyp.common;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ca.bc.gov.nrs.vdyp.model.GenusDefinition;

public class GenusDefinitionMap {
	private final Map<String, GenusDefinition> genusByAliasMap = new HashMap<>();
	private final Map<String, Integer> indexByAliasMap = new HashMap<>();
	private final Map<Integer, GenusDefinition> genusByIndexMap = new HashMap<>();
	private final int nGenera;
	private final List<String> aliasesSortedByIndex;
	private final List<GenusDefinition> genusDefinitionsSortedByIndex;

	public GenusDefinitionMap(List<GenusDefinition> genusDefinitionList) {

		if (genusDefinitionList != null) {
			nGenera = genusDefinitionList.size();

			List<String> aliases = new ArrayList<>();
			List<GenusDefinition> genusDefinitions = new ArrayList<>();

			for (GenusDefinition g : genusDefinitionList) {
				genusByAliasMap.put(g.getAlias(), g);
				indexByAliasMap.put(g.getAlias(), g.getIndex());
				genusByIndexMap.put(g.getIndex(), g);
				aliases.add(g.getAlias());
				genusDefinitions.add(g);
			}

			aliasesSortedByIndex = aliases.stream()
					.sorted((o1, o2) -> indexByAliasMap.get(o1) - indexByAliasMap.get(o2)).toList();
			genusDefinitionsSortedByIndex = genusDefinitions.stream().sorted((o1, o2) -> o1.getIndex() - o2.getIndex())
					.toList();
		} else {
			aliasesSortedByIndex = new ArrayList<>();
			genusDefinitionsSortedByIndex = new ArrayList<>();
			nGenera = 0;
		}
	}

	public boolean contains(String alias) {
		return genusByAliasMap.get(alias) != null;
	}

	public GenusDefinition getByAlias(String alias) {
		GenusDefinition g = genusByAliasMap.get(alias);
		if (g == null) {
			throw new IllegalArgumentException(
					MessageFormat.format("Unable to find GenusDefinition for alias {0}", alias)
			);
		}
		return g;
	}

	public GenusDefinition getByIndex(int index) {
		GenusDefinition g = genusByIndexMap.get(index);
		if (g == null) {
			throw new IllegalArgumentException(
					MessageFormat.format("Unable to find GenusDefinition for index {0}", index)
			);
		}
		return g;
	}

	public int getIndexByAlias(String alias) {
		return getByAlias(alias).getIndex();
	}

	public int getNGenera() {
		return nGenera;
	}

	public List<GenusDefinition> getGenera() {
		return Collections.unmodifiableList(genusDefinitionsSortedByIndex);
	}

	public List<String> getAllGeneraAliases() {
		return Collections.unmodifiableList(aliasesSortedByIndex);
	}
}
