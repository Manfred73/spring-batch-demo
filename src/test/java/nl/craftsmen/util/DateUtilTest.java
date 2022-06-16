package nl.craftsmen.util;

import static nl.craftsmen.util.DateUtil.formatDate;
import static nl.craftsmen.util.DateUtil.toDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Given something with a Date and/or a LocalDate")
class DateUtilTest {

	private static final String MESSAGE_CANNOT_BE_PARSED = "Text 'test' could not be parsed at index 0";

	@Test
	@DisplayName("A Date and a LocalDate should look the same when default formatted")
	void defaultDateFormatTest() {
		final var date = new Date();
		final var localDate = DateUtil.toLocalDate(date);

		final var dateString = DateUtil.formatDate(date);
		final var localDateString = DateUtil.formatDate(localDate);

		assertThat(dateString).isEqualTo(localDateString);
	}

	@Nested
	@DisplayName("When parsing a date-string")
	class ParseDate {

		@Test
		@DisplayName("Format FORMAT_DATE_YYYY_MM_DD can be parsed")
		void parseDateYYYY_MM_DDTest() {
			final var date = DateUtil.parseDate("2016-06-08", DateUtil.FORMAT_DATE_YYYY_MM_DD);
			Assertions.assertThat(DateUtil.toDate(LocalDate.of(2016, 6, 8))).isEqualTo(date);
		}

		@Test
		@DisplayName("Format FORMAT_DATE_YYYY_MM_DD can be parsed via courtesy-method")
		void parseDateYYYY_MM_DD_courtesy_Test() {
			final var date = DateUtil.parseDateYYYYMMDD("2016-06-08");
			Assertions.assertThat(DateUtil.toDate(LocalDate.of(2016, 6, 8))).isEqualTo(date);
		}

		@Test
		@DisplayName("Format FORMAT_DATE_DD_MM_YYYY can be parsed")
		void parseDateDD_MM_YYYYTest() {
			final var date = DateUtil.parseDate("08-06-2016", DateUtil.FORMAT_DATE_DD_MM_YYYY);
			Assertions.assertThat(DateUtil.toDate(LocalDate.of(2016, 6, 8))).isEqualTo(date);
		}

		@Test
		@DisplayName("Format FORMAT_DATE_EEYYMMDDHHMM can be parsed")
		void parseDateEEYYMMDDHHMMTest() {
			final var date = DateUtil.parseDate("201606080000", DateUtil.FORMAT_DATE_EEYYMMDDHHMM);
			Assertions.assertThat(DateUtil.toDate(LocalDate.of(2016, 6, 8))).isEqualTo(date);
		}

		@Test
		@DisplayName("Formaat FORMAT_DATE_D_M_YYYY can be parsed")
		void parseDateD_M_YYYYTest() {
			final var date = DateUtil.parseDate("08-06-2016", DateUtil.FORMAT_DATE_D_M_YYYY);
			Assertions.assertThat(DateUtil.toDate(LocalDate.of(2016, 6, 8))).isEqualTo(date);
		}

		@Test
		@DisplayName("An empty string cannot be parsed")
		void parseDateMetLegeDateString() {
			final var date = DateUtil.parseDate(StringUtils.EMPTY, DateUtil.FORMAT_DATE_D_M_YYYY);
			assertThat(date).isNull();
		}

		@Test
		@DisplayName("DateString with ParsePattern null throws  DateUtilException")
		void parseDateMetParsePatternNullGeeftDateUtilException() {
			final var thrown = catchThrowable(() -> DateUtil.parseDate("08-06-2016", null));
			assertThat(thrown)
					.isInstanceOf(DateUtilException.class)
					.hasMessage("ParsePattern must not be null");
		}
	}

	@Test
	@DisplayName("Format FORMAT_DATE_YYYY_MM_DD can be parsed")
	void parseLocalDateSucces() {
		// GIVEN
		final var dateString = "2016-06-08";
		final var expectedLocalDate = LocalDate.of(2016, 6, 8);

		// WHEN
		final var localDate = DateUtil.parseLocalDate(dateString, DateUtil.FORMAT_DATE_YYYY_MM_DD);

		// THEN
		assertThat(expectedLocalDate).isEqualTo(localDate);
	}

	@Test
	@DisplayName("Cannot parse 'test' to LocalDate")
	void parseLocalDateException() {
		// GIVEN
		final var dateString = "test";

		// WHEN
		final var thrown = catchThrowable(() -> DateUtil.parseLocalDate(dateString, DateUtil.FORMAT_DATE_YYYY_MM_DD));

		// THEN
		assertThat(thrown)
				.isInstanceOf(DateUtilException.class)
				.hasMessage(MESSAGE_CANNOT_BE_PARSED);
	}

	@Test
	@DisplayName("Cannot parse format FORMAT_DATE_YYYY_MM_DD")
	void parseLocalDateTimeSucces() {
		// GIVEN
		final var dateString = "201606080000";
		final var expectedLocalDateTime = LocalDateTime.of(2016, 6, 8, 0, 0, 0);

		// WHEN
		final var localDateTime = DateUtil.parseLocalDateTime(dateString, DateUtil.FORMAT_DATE_EEYYMMDDHHMM);

		// THEN
		assertThat(expectedLocalDateTime).isEqualTo(localDateTime);
	}

	@Test
	@DisplayName("Cannot parse 'test' to LocalDateTime")
	void parseLocalDateTimeException() {
		// GIVEN
		final var dateString = "test";

		// WHEN
		final var thrown = catchThrowable(() -> DateUtil.parseLocalDateTime(dateString, DateUtil.FORMAT_DATE_EEYYMMDDHHMM));

		// THEN
		assertThat(thrown)
				.isInstanceOf(DateUtilException.class)
				.hasMessage(MESSAGE_CANNOT_BE_PARSED);
	}

	@Nested
	@DisplayName("When formatting a Date object")
	class FormatDate {

		@Test
		@DisplayName("Format FORMAT_DATE_YYYY_MM_DD can be formatted")
		void formatDateYYYY_MM_DDTest() {
			final var date = DateUtil.toDate(LocalDate.of(2016, 6, 8));
			assertThat("2016-06-08").isEqualTo(DateUtil.formatDate(date, DateUtil.FORMAT_DATE_YYYY_MM_DD));
		}

		@Test
		@DisplayName("Format FORMAT_DATE_DD_MM_YYYY can be formatted")
		void formatDateDD_MM_YYYYTest() {
			final var date = DateUtil.toDate(LocalDate.of(2016, 6, 8));
			assertThat("08-06-2016").isEqualTo(DateUtil.formatDate(date, DateUtil.FORMAT_DATE_DD_MM_YYYY));
		}

		@Test
		@DisplayName("Format FORMAT_DATE_EEYYMMDDHHMM can be formatted")
		void formatDateEEYYMMDDHHMMTest() {
			final var date = DateUtil.toDate(LocalDate.of(2016, 6, 8));
			assertThat("201606080000").isEqualTo(DateUtil.formatDate(DateUtil.toLocalDateTime(date), DateUtil.FORMAT_DATE_EEYYMMDDHHMM));
		}

		@Test
		@DisplayName("Format FORMAT_DATE_D_M_YYYY can be formatted")
		void formatDateD_M_YYYYTest() {
			final var date = DateUtil.toDate(LocalDate.of(2016, 6, 8));
			assertThat("8-6-2016").isEqualTo(DateUtil.formatDate(date, DateUtil.FORMAT_DATE_D_M_YYYY));
		}

		@Test
		@DisplayName("Given a null Date and a date format string, I expect null")
		void formatDateMetDateNullVerwachtNull() {
			final Date date = null;
			final var result = DateUtil.formatDate(date, DateUtil.FORMAT_DATE_D_M_YYYY);
			assertThat(result).isNull();
		}

		@Test
		@DisplayName("Given a null LocalDateTime and a date format string, I expect null")
		void formatDateMetLocalDateTimeNullVerwachtNull() {
			final LocalDateTime localDateTime = null;
			final var result = DateUtil.formatDate(localDateTime, DateUtil.FORMAT_DATE_D_M_YYYY);
			assertThat(result).isNull();
		}

		@Test
		@DisplayName("Given a null LocalDate and a date format string, I expect null")
		void formatDateMetLocalDateNullVerwachtNull() {
			final LocalDate localDate = null;
			final var result = DateUtil.formatDate(localDate, DateUtil.FORMAT_DATE_D_M_YYYY);
			assertThat(result).isNull();
		}
	}

	@Test
	void greaterThanOrEqualToTrue() {
		assertThat(DateUtil.greaterThanOrEqualTo(DateUtil.toDate(LocalDate.of(2020, 2, 16)), DateUtil.toDate(LocalDate.of(2020, 2, 16)))).isTrue();
		assertThat(DateUtil.greaterThanOrEqualTo(DateUtil.toDate(LocalDate.of(2020, 2, 17)), DateUtil.toDate(LocalDate.of(2020, 2, 16)))).isTrue();
	}

	@Test
	void greaterThanOrEqualToFalse() {
		assertThat(DateUtil.greaterThanOrEqualTo(DateUtil.toDate(LocalDate.of(2020, 2, 16)), DateUtil.toDate(LocalDate.of(2020, 2, 17)))).isFalse();
	}

	@Test
	void lessThanOrEqualToTrue() {
		assertThat(DateUtil.lessThanOrEqualTo(DateUtil.toDate(LocalDate.of(2020, 2, 16)), DateUtil.toDate(LocalDate.of(2020, 2, 17)))).isTrue();
		assertThat(DateUtil.lessThanOrEqualTo(DateUtil.toDate(LocalDate.of(2020, 2, 16)), DateUtil.toDate(LocalDate.of(2020, 2, 16)))).isTrue();
	}

	@Test
	void klessThanOrEqualToFalse() {
		assertThat(DateUtil.lessThanOrEqualTo(DateUtil.toDate(LocalDate.of(2020, 2, 17)), DateUtil.toDate(LocalDate.of(2020, 2, 16)))).isFalse();
	}

	@Test
	@DisplayName("Given a null LocalDate, I expect null as a result")
	void toDateWithLocalDateNullExpectNull() {
		// GIVEN
		final LocalDate localDate = null;

		// WHEN
		final var result = DateUtil.toDate(localDate);

		// THEN
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("Given a null Date, I expect null as a result")
	void toLocalDateTimeWithDateNullExpectNull() {
		// GIVEN
		final Date date = null;

		// WHEN
		final var result = DateUtil.toLocalDateTime(date);

		// THEN
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("Given a null Date, I expect null as a result")
	void toLocalateWithDateNullExpectNull() {
		// GIVEN
		final Date date = null;

		// WHEN
		final var result = DateUtil.toLocalDate(date);

		// THEN
		assertThat(result).isNull();
	}

	@Test
	void isBetweenTrue() {
		final var systemDate = DateUtil.toDate(LocalDateTime.of(2020, 4, 8, 12, 0, 0));
		final var dateToCheck = DateUtil.toDate(LocalDateTime.of(2020, 4, 1, 12, 0, 0));
		final var startDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDate).minusDays(31));
		final var endDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDate).plusDays(31));
		assertThat(DateUtil.isBetweenIncluding(startDate, endDate, dateToCheck)).isTrue();
	}

	@Test
	void isBetweenTrueDateToCheckEqualsStartDate() {
		final var systemDatum = DateUtil.toDate(LocalDateTime.of(2020, 4, 8, 12, 0, 0));
		final var dateToCheck = DateUtil.toDate(LocalDateTime.of(2020, 3, 8, 12, 0, 0));
		final var startDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDatum).minusDays(31));
		final var endDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDatum).plusDays(31));
		assertThat(DateUtil.isBetweenIncluding(startDate, endDate, dateToCheck)).isTrue();
	}

	@Test
	void isBetweenTrueDateToCheckEqualsEndDate() {
		final var systemDatum = DateUtil.toDate(LocalDateTime.of(2020, 4, 8, 12, 0, 0));
		final var dateToCheck = DateUtil.toDate(LocalDateTime.of(2020, 5, 9, 12, 0, 0));
		final var startDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDatum).minusDays(31));
		final var endDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDatum).plusDays(31));
		assertThat(DateUtil.isBetweenIncluding(startDate, endDate, dateToCheck)).isTrue();
	}

	@Test
	void isBetweenFalseDateToCheckBeforeStartDate() {
		final var systemDatum = DateUtil.toDate(LocalDateTime.of(2020, 4, 8, 12, 0, 0));
		final var dateToCheck = DateUtil.toDate(LocalDateTime.of(2020, 3, 7, 12, 0, 0));
		final var startDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDatum).minusDays(31));
		final var endDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDatum).plusDays(31));
		assertThat(DateUtil.isBetweenIncluding(startDate, endDate, dateToCheck)).isFalse();
	}

	@Test
	void isBetweenFalseDateToCheckAfterEndDate() {
		final var systemDatum = DateUtil.toDate(LocalDateTime.of(2020, 4, 8, 12, 0, 0));
		final var dateToCheck = DateUtil.toDate(LocalDateTime.of(2020, 5, 10, 12, 0, 0));
		final var startDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDatum).minusDays(31));
		final var endDate = DateUtil.toDate(DateUtil.toLocalDateTime(systemDatum).plusDays(31));
		assertThat(DateUtil.isBetweenIncluding(startDate, endDate, dateToCheck)).isFalse();
	}

	@Test
	void toCeilingTimeNull() {
		assertThat(DateUtil.toCeilingTime(null)).isNull();
	}

	@Test
	void toCeilingTime() {
		final var date = DateUtil.toDate(LocalDateTime.of(2020, 8, 28, 0, 0, 0, 0));

		final var actualDate = DateUtil.toCeilingTime(date);

		final var formatter = DateTimeFormatter.ofPattern(DateUtil.FORMAT_DATETIME_YYYY_MM_DD_T_HH_MM_SS_SSS);
		final var expectedDate = LocalDateTime.of(2020, 8, 28, 23, 59, 59, 999999999).format(formatter);
		assertThat(expectedDate).isEqualTo(DateUtil.toLocalDateTime(actualDate).format(formatter));
	}

	@Test
	@DisplayName("Given a date of 01-01-2022 and a number of days of 2 to add, I expect a result date of 01-03-2022")
	void getDatePlusDays() {
		// GIVEN
		final var date = DateUtil.toDate(LocalDate.of(2022, 1, 1));

		// WHEN
		final var result = DateUtil.getDatePlusDays(date, 2);

		// THEN
		assertThat(DateUtil.toLocalDate(result)).isEqualTo(LocalDate.of(2022, 1, 3));
	}

	@Test
	@DisplayName("Given a date with time, when calling getDateWithoutTime, I expect a date without time set to 0")
	void getDateWithoutTime() {
		// GIVEN
		final var date = DateUtil.toDate(LocalDateTime.of(2022, 1, 1, 9, 10, 10, 5994959));

		// WHEN
		final var result = DateUtil.getDateWithoutTime(date);

		// THEN
		assertThat(DateUtil.toLocalDate(result)).isEqualTo(LocalDate.of(2022, 1, 1));
		final var cal = Calendar.getInstance();
		cal.setTime(result);
		assertThat(cal)
				.extracting(
						c -> c.get(Calendar.HOUR_OF_DAY),
						c -> c.get(Calendar.MINUTE),
						c -> c.get(Calendar.SECOND),
						c -> c.get(Calendar.MILLISECOND))
				.allSatisfy(i -> assertThat(i).isEqualTo(0));
	}
}
