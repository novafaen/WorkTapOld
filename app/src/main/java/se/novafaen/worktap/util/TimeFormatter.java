package se.novafaen.worktap.util;

/**
 * Date formatter helper.
 *
 * @author Kristoffer Nilsson
 * @since 2016-01-03
 */
public class TimeFormatter {
    public static final Long MILLISECONDS_IN_HOUR = 1000L * 60L * 60L;
    public static final Long MILLISECONDS_IN_MINUTES = 1000L * 60L;

    /**
     * Format time in natural format; 88h 88m
     * @param time in milliseconds
     * @return formatted string
     */
    public static String formatTimeNatural(Long time) {
        Long hours = time / MILLISECONDS_IN_HOUR;
        Long minutes = (time - hours * MILLISECONDS_IN_HOUR) / MILLISECONDS_IN_MINUTES;

        return "" + hours + "h " + minutes + "m";
    };

    /**
     * Format time in decimal format; 88.88
     * @param time in milliseconds
     * @return formatted string
     */
    public static String formatTimeDecimal(Long time) {
        Long hours = time / MILLISECONDS_IN_HOUR;
        Long minutes = (time - hours * MILLISECONDS_IN_HOUR) / MILLISECONDS_IN_MINUTES;
        Long percentOfHour = 60L / minutes * 100;

        return "" + hours + "." + percentOfHour;
    };

    /**
     * Format time in clock format; 88:88
     * @param time in milliseconds
     * @return formatted string
     */
    public static String formatTimeClock(Long time) {
        Long hours = time / MILLISECONDS_IN_HOUR;
        Long minutes = (time - hours * MILLISECONDS_IN_HOUR) / MILLISECONDS_IN_MINUTES;

        return "" + hours + ":" + minutes;
    };
}
