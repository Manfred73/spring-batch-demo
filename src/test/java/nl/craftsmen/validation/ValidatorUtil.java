package nl.craftsmen.validation;

import java.util.Locale;
import javax.validation.Validation;
import javax.validation.Validator;

public class ValidatorUtil {

	public static Validator getValidator() {
		final var locale = Locale.forLanguageTag("en");
		final var validatorFactory = Validation.buildDefaultValidatorFactory();
		final var localeInterpolator = new LocaleSpecificMessageInterpolator(validatorFactory.getMessageInterpolator(), locale);
		return validatorFactory.usingContext().messageInterpolator(localeInterpolator).getValidator();
	}
}
