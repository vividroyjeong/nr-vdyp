package ca.bc.gov.nrs.vdyp.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GenusDistributionSetTest {

	private static String aAlias;
	private static String bAlias;

	@BeforeAll
	static void beforeAll() {
		aAlias = "A";
		bAlias = "B";
	}

	@Test
	void testGenusDistributionConstruction() {
		assertThrows(IllegalArgumentException.class, () -> new Sp64Distribution(1, aAlias, -8.0f));
		assertThrows(IllegalArgumentException.class, () -> new Sp64Distribution(1, aAlias, 120.0f));
		assertThrows(IllegalArgumentException.class, () -> new Sp64Distribution(1, null, 50.0f));
	}

	@Test
	void testGenusDefinitionComparison() {
		assertNotEquals(aAlias, null);

		assertNotEquals(aAlias, bAlias);
		assertEquals(aAlias.hashCode(), aAlias.hashCode());
		assertTrue(aAlias.compareTo(aAlias) == 0);
		assertTrue(aAlias.compareTo(bAlias) < 0);
		assertTrue(bAlias.compareTo(aAlias) > 0);
		assertTrue(aAlias.compareTo(null) > 0);
	}

	@Test
	void testGenusDistributionComparison() {
		var gd1 = new Sp64Distribution(0, aAlias, 70.0f);
		var gd2 = new Sp64Distribution(0, bAlias, 30.0f);

		assertNotEquals(gd1, gd2);
		assertNotEquals(gd1, null);
		assertNotEquals(null, gd2);

		assertTrue(gd1.compareTo(gd1) == 0);
		assertTrue(gd1.compareTo(gd2) < 0);
		assertTrue(gd2.compareTo(gd1) > 0);
		assertTrue(gd1.compareTo(null) > 0);

		assertEquals(gd1.hashCode(), gd1.hashCode());
	}

	@Test
	void testGenusDistribution() {
		var gd1 = new Sp64Distribution(0, aAlias, 70.0f);
		assertThat(gd1.getGenusAlias(), is(aAlias));
		assertThat(gd1.getIndex(), is(0));
		assertThat(gd1.getPercentage(), is(70.0f));
	}

	@Test
	void testGenusDistributionSetConstruction() {
		var gd1 = new Sp64Distribution(0, aAlias, 70.0f);
		var gd1a = new Sp64Distribution(1, aAlias, 30.0f);
		var gd2 = new Sp64Distribution(1, bAlias, 30.0f);
		var gd2a = new Sp64Distribution(1, bAlias, 25.0f);

		assertNotNull(new Sp64DistributionSet(List.of(gd1, gd2)));
		assertNotNull(new Sp64DistributionSet(List.of(gd1, gd2)));
		assertThrows(IllegalArgumentException.class, () -> new Sp64DistributionSet(List.of(gd1, gd1a)));

		assertThrows(IllegalArgumentException.class, () -> new Sp64DistributionSet(List.of(gd1, gd2)));
		assertThrows(IllegalArgumentException.class, () -> new Sp64DistributionSet(List.of(gd1, gd2a)));
	}

	@Test
	void testGenusDistributionSetAccessor() {
		var gd1 = new Sp64Distribution(0, aAlias, 70.0f);
		var gd2 = new Sp64Distribution(1, bAlias, 30.0f);

		var gds = new Sp64DistributionSet(List.of(gd1, gd2));

		assertThat(gds.getSpeciesDistribution(0), isA(Optional.class));
		assertThat(gds.getSpeciesDistribution(0).get(), is(gd1));
		assertThat(gds.getSpeciesDistribution(1), isA(Optional.class));
		assertThat(gds.getSpeciesDistribution(1).get(), is(gd2));
		assertThat(gds.getSpeciesDistribution(2), is(Optional.empty()));
		assertThat(gds.getSpeciesDistribution(3), is(Optional.empty()));
		assertThrows(IllegalArgumentException.class, () -> gds.getSpeciesDistribution(4));
	}
}
