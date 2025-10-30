package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalAppointments.CARL_ALICE_PAST;
import static seedu.address.testutil.TypicalAppointments.CARL_ELLE_UPCOMING;
import static seedu.address.testutil.TypicalAppointments.FIONA_BENSON_TODAY;
import static seedu.address.testutil.TypicalAppointments.FIONA_ELLE_PAST;
import static seedu.address.testutil.TypicalAppointments.GEORGE_ALICE_UPCOMING;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class AppointmentContainsKeywordsPredicateTest {

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        AppointmentContainsKeywordsPredicate firstPredicate =
            new AppointmentContainsKeywordsPredicate(firstPredicateKeywordList);
        AppointmentContainsKeywordsPredicate secondPredicate =
            new AppointmentContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        AppointmentContainsKeywordsPredicate firstPredicateCopy =
            new AppointmentContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_appointmentContainsKeywords_returnsTrue() {
        Appointment testAppointment = FIONA_ELLE_PAST;

        // Single keyword matching seller - delegates to Appointment.containsKeyword()
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(Collections.singletonList("Fiona"));
        assertTrue(predicate.test(testAppointment));

        // Single keyword matching buyer
        predicate = new AppointmentContainsKeywordsPredicate(Collections.singletonList("Elle"));
        assertTrue(predicate.test(testAppointment));

        // Multiple keywords - should return true if ANY keyword matches
        predicate = new AppointmentContainsKeywordsPredicate(
                Arrays.asList("Fiona", "NonExistent"));
        assertTrue(predicate.test(testAppointment));

        // All keywords match
        predicate = new AppointmentContainsKeywordsPredicate(Arrays.asList("Fiona", "Elle"));
        assertTrue(predicate.test(testAppointment));
    }

    @Test
    public void test_appointmentDoesNotContainKeywords_returnsFalse() {
        Appointment testAppointment = FIONA_ELLE_PAST;

        // Empty keyword list - should always return false
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(Collections.emptyList());
        assertFalse(predicate.test(testAppointment));

        // No matching keywords - delegates to Appointment.containsKeyword()
        predicate = new AppointmentContainsKeywordsPredicate(Arrays.asList("NonExistent"));
        assertFalse(predicate.test(testAppointment));

        // Multiple non-matching keywords - should return false if NONE match
        predicate = new AppointmentContainsKeywordsPredicate(
                Arrays.asList("NonExistent", "NotFound"));
        assertFalse(predicate.test(testAppointment));
    }

    @Test
    public void test_timeFrameFilterPast_returnsTrueForPastAppointments() {
        Appointment pastAppointment = CARL_ALICE_PAST;

        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(Collections.emptyList(), TimeFrame.PAST);
        assertTrue(predicate.test(pastAppointment));
    }

    @Test
    public void test_timeFrameFilterToday_returnsTrueForTodayAppointments() {
        Appointment todayAppointment = FIONA_BENSON_TODAY;

        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(Collections.emptyList(), TimeFrame.TODAY);
        assertTrue(predicate.test(todayAppointment));
    }

    @Test
    public void test_timeFrameFilterUpcoming_returnsTrueForUpcomingAppointments() {
        Appointment futureAppointment = CARL_ELLE_UPCOMING;

        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(Collections.emptyList(), TimeFrame.UPCOMING);
        assertTrue(predicate.test(futureAppointment));

        Appointment todayAppointment = FIONA_BENSON_TODAY;
        assertTrue(predicate.test(todayAppointment));
    }

    @Test
    public void test_keywordsAndTimeFrame_returnsTrueWhenBothMatch() {
        Appointment futureAppointment = GEORGE_ALICE_UPCOMING;

        // Both keyword and timeframe match (matches seller GEORGE)
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(
                    Collections.singletonList("George"), TimeFrame.UPCOMING);
        assertTrue(predicate.test(futureAppointment));

        // Both keyword and timeframe match (matches buyer ALICE)
        predicate = new AppointmentContainsKeywordsPredicate(
                    Collections.singletonList("Alice"), TimeFrame.UPCOMING);
        assertTrue(predicate.test(futureAppointment));
    }

    @Test
    public void test_keywordsAndTimeFrame_returnsFalseWhenTimeFrameDoesNotMatch() {
        Appointment pastAppointment = CARL_ALICE_PAST;

        // Keyword matches (ALICE) but timeframe doesn't (looking for upcoming, but it's past)
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(
                    Collections.singletonList("Alice"), TimeFrame.UPCOMING);
        assertFalse(predicate.test(pastAppointment));
    }

    @Test
    public void test_keywordsAndTimeFrame_returnsFalseWhenKeywordDoesNotMatch() {
        Appointment futureAppointment = GEORGE_ALICE_UPCOMING;

        // Timeframe matches (upcoming) but keyword doesn't (NonExistent person)
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(
                    Collections.singletonList("NonExistent"), TimeFrame.UPCOMING);
        assertFalse(predicate.test(futureAppointment));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        AppointmentContainsKeywordsPredicate predicate =
            new AppointmentContainsKeywordsPredicate(keywords, TimeFrame.TODAY);

        String expected = AppointmentContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + ", timeFrame=today}";
        assertEquals(expected, predicate.toString());
    }
}
