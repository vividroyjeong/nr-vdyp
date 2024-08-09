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

class Sp64DistributionSetTest {

	private static String aAlias;
	private static String bAlias;
	private static Sp64Distribution a;
	private static Sp64Distribution b;

	@BeforeAll
	static void beforeAll() {
		aAlias = "A";
		bAlias = "B";
		a = new Sp64Distribution(1, aAlias, 75f);
		b = new Sp64Distribution(1, bAlias, 25f);
	}

	@Test
	void testSp64DistributionConstruction() {
		assertThrows(IllegalArgumentException.class, () -> new Sp64Distribution(1, aAlias, -8.0f));
		assertThrows(IllegalArgumentException.class, () -> new Sp64Distribution(1, aAlias, 120.0f));
		assertThrows(IllegalArgumentException.class, () -> new Sp64Distribution(1, null, 50.0f));
	}

	@Test
	void testSp64DistributionComparison1() {
		assertNotEquals(a, null);

		assertNotEquals(a, b);
		assertTrue(a.compareTo(a) == 0);
		assertTrue(a.compareTo(b) < 0);
		assertTrue(b.compareTo(a) > 0);
		assertTrue(a.compareTo(null) > 0);
	}

	@Test
	void testSp64DistributionComparison2() {
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
	void testSp64DistributionAccessors() {
		var gd1 = new Sp64Distribution(0, aAlias, 70.0f);
		assertThat(gd1.getGenusAlias(), is(aAlias));
		assertThat(gd1.getIndex(), is(0));
		assertThat(gd1.getPercentage(), is(70.0f));
	}

	@Test
	void testSp64DistributionSetConstruction() {
		var gd1 = new Sp64Distribution(1, aAlias, 70.0f);
		var gd1a = new Sp64Distribution(2, aAlias, 30.0f);
		var gd2 = new Sp64Distribution(2, bAlias, 30.0f);
		var gd2a = new Sp64Distribution(2, bAlias, 25.0f);

		assertNotNull(new Sp64DistributionSet(2, List.of(gd1, gd2)));
		var sp64Set1 = new Sp64DistributionSet(3, List.of(gd1, gd2a));
		assertNotNull(sp64Set1);
		assertTrue(sp64Set1.getSpeciesDistribution(1).get().equals(gd1));
		assertTrue(sp64Set1.getSpeciesDistribution(2).get().equals(gd2a));

		// Same sp64 aliases
		assertThrows(IllegalArgumentException.class, () -> new Sp64DistributionSet(2, List.of(gd1, gd1a)));

		// maxIndex too low
		assertThrows(IllegalArgumentException.class, () -> new Sp64DistributionSet(1, List.of(gd1, gd2)));

		// same indicies
		assertThrows(IllegalArgumentException.class, () -> new Sp64DistributionSet(2, List.of(gd1a, gd2a)));
	}

	@Test
	void testSp64DistributionSetAccessor() {
		var sd1 = new Sp64Distribution(1, aAlias, 70.0f);
		var sd2 = new Sp64Distribution(2, bAlias, 30.0f);

		var set = new Sp64DistributionSet(4, List.of(sd1, sd2));

		assertThat(set.getSpeciesDistribution(1), isA(Optional.class));
		assertThat(set.getSpeciesDistribution(1).get(), is(sd1));
		assertThat(set.getSpeciesDistribution(2), isA(Optional.class));
		assertThat(set.getSpeciesDistribution(2).get(), is(sd2));
		assertThat(set.getSpeciesDistribution(3), is(Optional.empty()));
		assertThat(set.getSpeciesDistribution(4), is(Optional.empty()));
		assertThrows(IllegalArgumentException.class, () -> set.getSpeciesDistribution(5));

		assertThat(set.getSp64DistributionList().size(), is(set.getSize()));
		assertThat(set.getSp64DistributionMap().size(), is(set.getSize()));
	}
}
