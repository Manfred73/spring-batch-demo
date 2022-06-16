package nl.craftsmen.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import nl.craftsmen.contact.model.Fieldnames;
import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void expect_construction_of_util_clasess_to_throw_UnsupportedOperationException() throws NoSuchMethodException {
		testUtilClassConstructor(DateUtil.class);
		testUtilClassConstructor(Fieldnames.class);
	}

	private static <T> void testUtilClassConstructor(Class<T> utilClass) throws NoSuchMethodException {
		// GIVEN
		Constructor<T> constructor = utilClass.getDeclaredConstructor();
		assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
		constructor.setAccessible(true);

		// WHEN
		Throwable thrown = catchThrowable(constructor::newInstance);

		// THEN
		assertThat(thrown)
				.isInstanceOf(InvocationTargetException.class)
				.hasCauseInstanceOf(UnsupportedOperationException.class)
				.hasRootCauseMessage("Cannot initialize utility classes");
	}
}
