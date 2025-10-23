package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class AppointmentTimePredicateTest {

    // Create test appointments with different times
    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final LocalDateTime PAST_TIME = NOW.minusDays(1);
    private static final LocalDateTime TODAY_MORNING = NOW.toLocalDate().atTime(9, 0);
    private static final LocalDateTime TODAY_EVENING = NOW.toLocalDate().atTime(18, 0);
    private static final LocalDateTime FUTURE_TIME = NOW.plusDays(1);

    private final Appointment pastAppointment = createAppointment(PAST_TIME);
    private final Appointment todayMorningAppointment = createAppointment(TODAY_MORNING);
    private final Appointment todayEveningAppointment = createAppointment(TODAY_EVENING);
    private final Appointment futureAppointment = createAppointment(FUTURE_TIME);

    private Appointment createAppointment(LocalDateTime dateTime) {
        return new Appointment(
                new AppointmentDatetime(dateTime.toString()),
                ALICE, BOB);
    }

    @Test
    public void equals() {
        AppointmentTimePredicate allPredicate = new AppointmentTimePredicate(TimeFilter.ALL);
        AppointmentTimePredicate pastPredicate = new AppointmentTimePredicate(TimeFilter.PAST);

        // same object -> returns true
        assertTrue(allPredicate.equals(allPredicate));

        // same values -> returns true
        AppointmentTimePredicate allPredicateCopy = new AppointmentTimePredicate(TimeFilter.ALL);
        assertTrue(allPredicate.equals(allPredicateCopy));

        // different types -> returns false
        assertFalse(allPredicate.equals(1));

        // null -> returns false
        assertFalse(allPredicate.equals(null));

        // different time filter -> returns false
        assertFalse(allPredicate.equals(pastPredicate));
    }

    @Test
    public void test_allFilter_returnsTrue() {
        AppointmentTimePredicate predicate = new AppointmentTimePredicate(TimeFilter.ALL);

        // ALL filter should return true for all appointments
        assertTrue(predicate.test(pastAppointment));
        assertTrue(predicate.test(todayMorningAppointment));
        assertTrue(predicate.test(todayEveningAppointment));
        assertTrue(predicate.test(futureAppointment));
    }

    @Test
    public void test_pastFilter_correctFiltering() {
        AppointmentTimePredicate predicate = new AppointmentTimePredicate(TimeFilter.PAST);

        // PAST filter should only return true for appointments before now
        assertTrue(predicate.test(pastAppointment));

        // Today's appointments depend on current time
        if (TODAY_MORNING.isBefore(NOW)) {
            assertTrue(predicate.test(todayMorningAppointment));
        } else {
            assertFalse(predicate.test(todayMorningAppointment));
        }

        if (TODAY_EVENING.isBefore(NOW)) {
            assertTrue(predicate.test(todayEveningAppointment));
        } else {
            assertFalse(predicate.test(todayEveningAppointment));
        }

        assertFalse(predicate.test(futureAppointment));
    }

    @Test
    public void test_todayFilter_correctFiltering() {
        AppointmentTimePredicate predicate = new AppointmentTimePredicate(TimeFilter.TODAY);

        // TODAY filter should only return true for appointments on current date
        assertFalse(predicate.test(pastAppointment));
        assertTrue(predicate.test(todayMorningAppointment));
        assertTrue(predicate.test(todayEveningAppointment));
        assertFalse(predicate.test(futureAppointment));
    }

    @Test
    public void test_upcomingFilter_correctFiltering() {
        AppointmentTimePredicate predicate = new AppointmentTimePredicate(TimeFilter.UPCOMING);

        // UPCOMING filter should only return true for appointments after now
        assertFalse(predicate.test(pastAppointment));

        // Today's appointments depend on current time
        if (TODAY_MORNING.isAfter(NOW)) {
            assertTrue(predicate.test(todayMorningAppointment));
        } else {
            assertFalse(predicate.test(todayMorningAppointment));
        }

        if (TODAY_EVENING.isAfter(NOW)) {
            assertTrue(predicate.test(todayEveningAppointment));
        } else {
            assertFalse(predicate.test(todayEveningAppointment));
        }

        assertTrue(predicate.test(futureAppointment));
    }

    @Test
    public void test_todayAndUpcomingFilter_correctFiltering() {
        AppointmentTimePredicate predicate = new AppointmentTimePredicate(TimeFilter.TODAY_AND_UPCOMING);

        // TODAY_AND_UPCOMING filter should return true for appointments from start of
        // today onwards
        assertFalse(predicate.test(pastAppointment));
        assertTrue(predicate.test(todayMorningAppointment));
        assertTrue(predicate.test(todayEveningAppointment));
        assertTrue(predicate.test(futureAppointment));
    }

    @Test
    public void test_boundaryConditions() {
        // Test appointments exactly at start and end of day
        LocalDateTime startOfToday = NOW.toLocalDate().atStartOfDay();
        LocalDateTime endOfToday = NOW.toLocalDate().atTime(23, 59, 59);

        Appointment startOfDayAppointment = createAppointment(startOfToday);
        Appointment endOfDayAppointment = createAppointment(endOfToday);

        AppointmentTimePredicate todayPredicate = new AppointmentTimePredicate(TimeFilter.TODAY);
        AppointmentTimePredicate todayAndUpcomingPredicate = new AppointmentTimePredicate(
                TimeFilter.TODAY_AND_UPCOMING);

        // Both should be included in TODAY filter
        assertTrue(todayPredicate.test(startOfDayAppointment));
        assertTrue(todayPredicate.test(endOfDayAppointment));

        // Both should be included in TODAY_AND_UPCOMING filter
        assertTrue(todayAndUpcomingPredicate.test(startOfDayAppointment));
        assertTrue(todayAndUpcomingPredicate.test(endOfDayAppointment));
    }

    @Test
    public void toString_test() {
        AppointmentTimePredicate allPredicate = new AppointmentTimePredicate(TimeFilter.ALL);
        assertEquals("AppointmentTimePredicate{timeFilter=ALL}", allPredicate.toString());

        AppointmentTimePredicate pastPredicate = new AppointmentTimePredicate(TimeFilter.PAST);
        assertEquals("AppointmentTimePredicate{timeFilter=PAST}", pastPredicate.toString());

        AppointmentTimePredicate todayPredicate = new AppointmentTimePredicate(TimeFilter.TODAY);
        assertEquals("AppointmentTimePredicate{timeFilter=TODAY}", todayPredicate.toString());

        AppointmentTimePredicate upcomingPredicate = new AppointmentTimePredicate(TimeFilter.UPCOMING);
        assertEquals("AppointmentTimePredicate{timeFilter=UPCOMING}", upcomingPredicate.toString());

        AppointmentTimePredicate todayAndUpcomingPredicate = new AppointmentTimePredicate(
                TimeFilter.TODAY_AND_UPCOMING);
        assertEquals("AppointmentTimePredicate{timeFilter=TODAY_AND_UPCOMING}",
                todayAndUpcomingPredicate.toString());
    }
}
