package ca.bc.gov.nrs.vdyp.si32.tangible;

// ----------------------------------------------------------------------------------------
// Copyright © 2006 - 2024 Tangible Software Solutions, Inc.
// This class can be used by anyone provided that the copyright notice remains intact.
//
// This class provides the ability to initialize and delete array elements.
// ----------------------------------------------------------------------------------------
public final class TangibleArrays
{
	public static boolean[] initializeWithDefaultSWBoolInstances(int length)
	{
		return new boolean[length];
	}

	public static <T extends java.io.Closeable> void deleteArray(T[] array)
	{
	}
}
