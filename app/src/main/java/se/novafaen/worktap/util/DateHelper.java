package se.novafaen.worktap.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;

/**
 * Collection of date utils.
 *
 * @since 2016-01-03
 * @author Kristoffer Nilsson
 */
public class DateHelper {

    /**
     * Day numbers, MONDAY=1, ..., SUNDAY=7
     */
    public enum Day {
        MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6), SUNDAY(7);

        private int value;

        private Day(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * Get start of day (midnight) of today, using the device timezone.
     * @return timestamp in milliseconds
     */
    private static DateTime getStartOfDayTodayDefaultTimezone() {
        return new Instant()
                .toDateTime()
                .withZone(DateTimeZone.getDefault())
                .toLocalDate()
                .toDateTimeAtStartOfDay();
    }

    /**
     * Get start of day (midnight) of a day in the past, today minus amount of days.
     * Note: This function use device time zone.
     * @param days amount of days back in the past
     * @return timestamp in milliseconds
     */
    public static long getStartOfDayMinusDays(Integer days) {
        return getStartOfDayTodayDefaultTimezone()
                .minusDays(days)
                .toDate()
                .getTime();
    }

    /**
     * Get start of day (midnight) of a today.
     * Note: This function use device time zone.
     * @return timestamp in milliseconds
     */
    public static long getStartOfDayToday() {
        return getStartOfDayMinusDays(0);
    }

    /**
     * Get start of day (midnight) in the past, since specific week day.
     * @param day weekday enum.
     * @return timestamp in milliseconds
     */
    public static long getStartOfDaySinceWeekday(Day day) {
        int todayNumber = getStartOfDayTodayDefaultTimezone().getDayOfWeek();
        int requestedDayNumber = day.getValue();

        if (requestedDayNumber < todayNumber) {
            return getStartOfDayMinusDays(todayNumber - requestedDayNumber);
        } else {
            return getStartOfDayMinusDays(7 - requestedDayNumber + todayNumber);
        }
    }

    public static long getStartOfDaySinceMonthStart() {
        int todayDayOfMonth = new Instant().toDateTime().getDayOfMonth();

        return getStartOfDayMinusDays(todayDayOfMonth - 1); // if first, start today (i.e. 0)
    }
}
