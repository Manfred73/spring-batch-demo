package nl.craftsmen.validation;

import java.util.Locale;
import javax.validation.MessageInterpolator;

/**
 * Delegates to a MessageInterpolator implementation but enforces a given Locale.
 * Only used for testing purposes when creating your own validation factory in unittests.
 * When running the SpringBoot application, it uses the LocaleConfig configuration to set the locale.
 */
public class LocaleSpecificMessageInterpolator implements MessageInterpolator {
	private final MessageInterpolator defaultInterpolator;
	private final Locale defaultLocale;

	public LocaleSpecificMessageInterpolator(MessageInterpolator interpolator, Locale locale) {
		this.defaultLocale = locale;
		this.defaultInterpolator = interpolator;
	}

	 // Enforces the locale passed to the interpolator.
	@Override
	public String interpolate(String message, Context context) {
		return defaultInterpolator.interpolate(message, context, this.defaultLocale);
	}

	// No real use, implemented for completeness.
	@Override
	public String interpolate(String message, Context context,  Locale locale) {
		return defaultInterpolator.interpolate(message, context, locale);
	}
}