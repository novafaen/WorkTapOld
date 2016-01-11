package se.novafaen.worktap;


import org.joda.time.Instant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class TapTimeTest {

    private final long ONE_MINUTE = 60001; // milliseconds

    @Before
    public void setup() {

    }

    @After
    public void tearDown() {

    }

    @Test
    public void testInitiateWithoutTime() throws Exception {
        // GIVEN no time is recorded
        TapTime tapTime = new TapTime(0L, 0L, 0L);

        // WHEN get all time
        long timeToday = tapTime.getTappedToday();
        long timeWeek = tapTime.getTappedWeekTotal();
        long timeMonth = tapTime.getTappedMonthTotal();

        // THEN returned time is zero
        assertTrue(timeToday == 0L);
        assertTrue(timeWeek == 0L);
        assertTrue(timeMonth == 0L);
    }

    @Test
    public void testInitiateWithTime() throws Exception {
        // GIVEN some time is recorded
        long today = 123L, week = 456L, month = 789L;
        TapTime tapTime = new TapTime(today, week, month);

        // WHEN get all time
        Long timeToday = tapTime.getTappedToday();
        Long timeWeek = tapTime.getTappedWeekTotal();
        Long timeMonth = tapTime.getTappedMonthTotal();

        // THEN returned time is same as recorded
        assertTrue(timeToday == today);
        assertTrue(timeWeek == today + week);
        assertTrue(timeMonth == today + week + month);
    }

    @Test
    public void testRegisterFirstTap() {
        // GIVEN some time is recorded
        long today = 123L, week = 456L, month = 789L;
        TapTime tapTime = new TapTime(today, week, month);

        // WHEN tap event is registered
        long timestamp = Instant.now().getMillis();
        tapTime.registerTap(timestamp - ONE_MINUTE); // one minute in the past

        // THEN status is ongoing
        assertTrue(tapTime.ongoingTap());

        // AND time registered increase
        assertTrue(tapTime.getTappedToday() > today);
        assertTrue(tapTime.getTappedWeekTotal() > today + week);
        assertTrue(tapTime.getTappedMonthTotal() > today + week + month);
    }

    @Test
    public void testRegisterSecondTap() {
        // GIVEN some time is recorded
        long today = 123L, week = 456L, month = 789L;
        TapTime tapTime = new TapTime(today, week, month);

        // AND status is ongoing
        long timestamp = Instant.now().getMillis();
        tapTime.registerTap(timestamp - ONE_MINUTE); // one minute in the past

        // WHEN tap event is registered
        tapTime.registerTap(timestamp);

        // THEN status is stopped
        assertFalse(tapTime.ongoingTap());

        // AND time registered increase
        assertTrue(tapTime.getTappedToday() == today + ONE_MINUTE);
        assertTrue(tapTime.getTappedWeekTotal() == today + week + ONE_MINUTE);
        assertTrue(tapTime.getTappedMonthTotal() == today + week + month + ONE_MINUTE);
    }

    @Test
    public void testRegisterThirdTapException() {
        // GIVEN some time is recorded
        long today = 123L, week = 456L, month = 789L;
        TapTime tapTime = new TapTime(today, week, month);

        // AND two tap event is registered
        long timestamp = Instant.now().getMillis();
        tapTime.registerTap(timestamp - 2 * ONE_MINUTE);
        tapTime.registerTap(timestamp - ONE_MINUTE);

        // WHEN tap event is registered
        boolean exceptionThrown = false;
        try {
            tapTime.registerTap(timestamp);
        } catch (RuntimeException exc) {
            exceptionThrown = true;
        }

        // THEN exception is thrown
        assertTrue(exceptionThrown);
    }

    @Test
    public void testResetCurrentTapNoOngoing() {
        // GIVEN some time is recorded
        long today = 123L, week = 456L, month = 789L;
        TapTime tapTime = new TapTime(today, week, month);

        // AND status is finished
        long timestamp = Instant.now().getMillis();
        tapTime.registerTap(timestamp - ONE_MINUTE);
        tapTime.registerTap(timestamp);

        // WHEN current tap is reset
        tapTime.resetCurrentTap();

        // THEN time registered increase
        assertTrue(tapTime.getTappedToday() == today + ONE_MINUTE);
        assertTrue(tapTime.getTappedWeekTotal() == today + week + ONE_MINUTE);
        assertTrue(tapTime.getTappedMonthTotal() == today + week + month + ONE_MINUTE);

        // AND status is stopped
        assertFalse(tapTime.ongoingTap());
    }

    @Test
    public void testResetCurrentTapOngoing() {
        // GIVEN some time is recorded
        long today = 123L, week = 456L, month = 789L;
        TapTime tapTime = new TapTime(today, week, month);

        // AND status is ongoing
        long timestamp = Instant.now().getMillis();
        tapTime.registerTap(timestamp - ONE_MINUTE);

        // WHEN reset current tap
        boolean exceptionThrown = false;
        try {
            tapTime.resetCurrentTap();
        } catch (RuntimeException exc) {
            exceptionThrown = true;
        }

        // THEN exception is thrown\
        assertTrue(exceptionThrown);
    }

    @Test
    public void testSetTime() {
        // GIVEN some time is recorded
        long today = 123L, week = 456L, month = 789L;
        TapTime tapTime = new TapTime(today, week, month);

        // WHEN time is set
        long newToday = 321L, newWeek = 654L, newMonth = 987L;
        tapTime.setTodayTime(newToday);
        tapTime.setWeekTime(newWeek);
        tapTime.setMonthTime(newMonth);

        // THEN time is changed
        assertTrue(tapTime.getTappedToday() == newToday);
        assertTrue(tapTime.getTappedWeekTotal() == newWeek + newToday);
        assertTrue(tapTime.getTappedMonthTotal() == newMonth + newWeek + newToday);

    }
}