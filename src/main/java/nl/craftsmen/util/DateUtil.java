package nl.craftsmen.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

public final class DateUtil {

    public static final String FORMAT_DATETIME_YYYY_MM_DD_T_HH_MM_SS_SSS = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String FORMAT_DATE_DDMMYYYY = "ddMMyyyy";
    public static final String FORMAT_DATE_DD_MM_YYYY = "dd-MM-yyyy";
    public static final String FORMAT_DATE_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_DATE_EEYYMMDDHHMM = "yyyyMMddHHmm";
    public static final String FORMAT_DATE_D_M_YYYY = "d-M-yyyy";

    private DateUtil() {
        throw new UnsupportedOperationException("Cannot initialize utility classes");
    }

    public static Date parseDateYYYYMMDD(final String dateString) {
        return parseDate(dateString, FORMAT_DATE_YYYY_MM_DD);
    }

    /**
     * Creates a {@link Date} object from the provided dateString if the provided date pattern is valid, else
     * {@code null} is returned.
     *
     * @param dateString   dateString
     * @param parsePattern parsePattern
     * @return date, or {@code null} if provided dateString is not a valid date pattern
     */
    public static Date parseDate(final String dateString, final String parsePattern) {
        if (StringUtils.isBlank(dateString)) {
            return null;
        }
        if (parsePattern == null) {
            throw new DateUtilException("ParsePattern must not be null");
        }
        return toDate(parseLocalDate(dateString, parsePattern));
    }

    public static LocalDate parseLocalDate(String dateString, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        try {
            return LocalDate.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException dtpe) {
            throw new DateUtilException(dtpe.getMessage(), dtpe);
        }
    }

    public static LocalDateTime parseLocalDateTime(String dateString, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        try {
            return LocalDateTime.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException dtpe) {
            throw new DateUtilException(dtpe.getMessage(), dtpe);
        }
    }

    /**
     * Returns the date plus a number of days.
     *
     * @param date              original date
     * @param numberOfDaysToAdd the number of days to add; this cannot be negative
     * @return date plus the numberOfDaysToAdd
     */
    public static Date getDatePlusDays(final Date date, final int numberOfDaysToAdd) {
        final var cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, numberOfDaysToAdd);
        return cal.getTime();
    }

    /**
     * Returns a new date with time set to 00:00:00.000.
     *
     * @param date date
     * @return date without time, i.e. time is 0
     */
    public static Date getDateWithoutTime(final Date date) {
        final var cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static String formatDate(final Date date) {
        return formatDate(date, FORMAT_DATE_YYYY_MM_DD);
    }

    public static String formatDate(final LocalDate date) {
        return formatDate(date, FORMAT_DATE_YYYY_MM_DD);
    }

    public static String formatDate(final Date date, final String format) {
        if (date != null) {
            return formatDate(toLocalDate(date), format);
        }
        return null;
    }

    public static String formatDate(final LocalDateTime date, final String format) {
        if (date != null) {
            final var formatter = DateTimeFormatter.ofPattern(format);
            return formatter.format(date);
        }
        return null;
    }

    public static String formatDate(final LocalDate date, final String format) {
        if (date != null) {
            final var formatter = DateTimeFormatter.ofPattern(format);
            return formatter.format(date);
        }
        return null;
    }

    public static Date toDate(final LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDate ld) {
        if (ld == null) {
            return null;
        }
        return toDate(ld.atStartOfDay());
    }

    public static LocalDateTime toLocalDateTime(final Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return new Date(date.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Checks if {@link Date} value1 is greater than or equal to {@link Date} value2.
     *
     * @param value1 {@link Date} value1 that needs to be checked with {@link Date} value2
     * @param value2 {@link Date} value2 that needs to be checked with {@link Date} value1
     * @return {@code true} if {@link Date} value1 is greater or equal to {@link Date} value2, otherwise {@code false}
     */
    public static boolean greaterThanOrEqualTo(Date value1, Date value2) {
        return value1.compareTo(value2) >= 0;
    }

    /**
     * Checks if {@link Date} value1 is less than or equal to {@link Date} value2.
     *
     * @param value1 {@link Date} value1 that needs to be checked with {@link Date} value2
     * @param value2 {@link Date} value2 that needs to be checked with {@link Date} value1
     * @return {@code true} if {@link Date} value1 is less than or equal to {@link Date} value2, otherwise {@code false}
     */
    public static boolean lessThanOrEqualTo(Date value1, Date value2) {
        return value1.compareTo(value2) <= 0;
    }

    /**
     * Checks if the {@code dateToCheck} is between {@code startDate} and {@code endDate}; both {@code startDate} and
     * {@code endDate} are inclusive.
     *
     * @param startDate   the start date
     * @param endDate     the end date
     * @param dateToCheck the date to check
     * @return {@code true} if the date to check is between the start and end date or on the start or end date,
     * otherwise {@code false}
     */
    public static boolean isBetweenIncluding(Date startDate, Date endDate, Date dateToCheck) {
        return dateToCheck.compareTo(startDate) >= 0 && dateToCheck.compareTo(endDate) <= 0;
    }

    /**
     * Sets the time part of the provided {@link Date} to 23:59:59:000.
     */
    public static Date toCeilingTime(Date date) {
        return date == null ? null : DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
    }
}
