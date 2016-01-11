package se.novafaen.worktap;

import org.joda.time.Instant;

/**
 * Handles taps, and keep track of tapped time.
 *
 * @since 2016-01-03
 * @author Kristoffer Nilsson
 */
public class TapTime {

    private Long startTime;
    private Long stopTime;
    private Long todayTime;
    private Long weekTime;
    private Long monthTime;

    /**
     * Constructor.
     */
    public TapTime(Long todayTime, Long weekTime, Long monthTime) {
        startTime = null;
        stopTime = null;
        this.todayTime = todayTime;
        this.weekTime = weekTime;
        this.monthTime = monthTime;
    }

    /**
     * Return tap status.
     * @return true if ongoing tap, otherwise false
     */
    public boolean ongoingTap() {
        return startTime != null && stopTime == null;
    }

    /**
     * Reset current tap, keep week and month time.
     */
    public void resetCurrentTap() throws RuntimeException {
        if (stopTime == null) {
            throw new RuntimeException("resetting ongoing tap," +
                    " can only be performed for stopped/finished taps");
        }

        // include current in total
        todayTime += stopTime - startTime;

        startTime = null;
        stopTime = null;
    }

    /**
     * Set tapped time for today.
     * @param time time in milliseconds
     */
    public void setTodayTime(Long time) {
        todayTime = time;
    }

    /**
     * Set tapped time for week, excluding today.
     * @param time time in milliseconds
     */
    public void setWeekTime(Long time) {
        weekTime = time;
    }

    /**
     * Set tapped time for month, excluding today and week.
     * @param time time in milliseconds
     */
    public void setMonthTime(Long time) {
        monthTime = time;
    }

    /**
     * Get tapped time for today (since midnight).
     * @return time in milliseconds
     */
    public Long getTappedToday() {
        if (startTime == null) {
            return todayTime;
        } else {
            if (stopTime == null) {
                return todayTime + (Instant.now().getMillis() - startTime);
            } else {
                return todayTime + (stopTime - startTime);
            }
        }
    }

    /**
     * Get tapped time for this week.
     * @return time in milliseconds
     */
    public Long getTappedWeekTotal() {
        return weekTime + getTappedToday();
    }

    /**
     * Get tapped time for this month, excluding today and week.
     * @return time in milliseconds
     */
    public Long getTappedMonthTotal() {
        return monthTime + getTappedWeekTotal();
    }

    /**
     * Register tap event. If no ongoing tap, start new one. If ongoing tap, stop it.
     * @param tapTime timestamp in milliseconds
     */
    public void registerTap(Long tapTime) throws RuntimeException{
        if (startTime == null) {
            startTime = tapTime;
        } else if (stopTime == null) {
            stopTime = tapTime;
        } else {
            throw new RuntimeException("received event" +
                    ", startTime=" + startTime +
                    ", stopTime=" + stopTime + ", expected either stopTime or both to be null");
        }
    }
}
