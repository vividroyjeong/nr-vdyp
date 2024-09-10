package ca.bc.gov.nrs.vdyp.model;

import java.util.Arrays;

/**
 * Stores the debug flag values provided in key 199 of the control files.
 * <p>
 * This class accepts an array of Integers (if null, or the default constructor is used, an empty integer array is
 * used). The array cannot have more than 25 elements, but may have fewer.
 * <p>
 * The class provides one operation: <code>debugValue</code>, which takes the number of debug setting whose value is
 * needed. In keeping with the original FORTRAN code, these values are <b>one-based</b> - so, debug setting "5" is
 * actually stored at <code>settings[4]</code>.
 * <p>
 * It is an error to supply a setting number above 25. If 25 or less but higher than the number of elements in the
 * array, the default value of "0" is returned. Otherwise, the value is read from the array.
 * <p>
 * Instances of class are typically wrapped by an application-specific class that understands the settings in use by the
 * application.
 * <p>
 * See IPSJF155, appendix IX, details.
 */
public class DebugSettings {

	public static final int MAX_DEBUG_SETTINGS = 25;
	private static final int DEFAULT_DEBUG_SETTING = 0;

	final int[] settings;

	/**
	 * Create a DebugSettings instance from the given values. If <code>settings</code> is null, this is equivalent to
	 * calling DebugSettings(new Integer[0]). If an entry in <code>settings</code> is null, the default value (0) is
	 * assigned to that variable. If more than <code>MAX_DEBUG_SETTINGS</code> are supplied, those in excess are
	 * ignored.
	 */
	public DebugSettings(Integer[] settings) {

		if (settings == null) {
			settings = new Integer[0];
		}

		if (settings.length > MAX_DEBUG_SETTINGS) {
			throw new IllegalArgumentException(
					"Debug settings array has length " + settings.length + ", which exceeds the maximum length of "
							+ MAX_DEBUG_SETTINGS
			);
		}

		this.settings = new int[MAX_DEBUG_SETTINGS];

		for (int i = 0; i < MAX_DEBUG_SETTINGS; i++) {
			if (i < settings.length && settings[i] != null) {
				this.settings[i] = settings[i];
			} else {
				this.settings[i] = DEFAULT_DEBUG_SETTING;
			}
		}
	}

	/**
	 * Create a DebugSettings instance with all settings set to zero.
	 */
	public DebugSettings() {
		this(new Integer[0]);
	}

	/**
	 * Return the value of the debug variable with setting number <code>settingNumber</code>. This is a <b>one-based</b>
	 * value.
	 *
	 * @param settingNumber the number of the debug variable whose value is to be returned.
	 * @return as described.
	 */
	public int getValue(int settingNumber) {
		if (settingNumber < 1 || settingNumber > MAX_DEBUG_SETTINGS) {
			throw new IllegalArgumentException(
					"Debug setting number " + settingNumber + " is out of range -" + " must be between 1 and "
							+ MAX_DEBUG_SETTINGS
			);
		}

		return settings[settingNumber - 1];
	}

	/**
	 * @return a copy of the values array, useful when the debug settings need to be modified.
	 */
	public int[] getValues() {
		return Arrays.copyOf(settings, settings.length);
	}
}
