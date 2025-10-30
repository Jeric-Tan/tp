package seedu.address.testutil;

import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.AppointmentDatetime;

/**
 * A utility class containing a list of {@code Appointment} objects to be used in tests.
 */
public class TypicalAppointments {

    // Fixed date constants for testing
    // Mock "today" as current date for consistent test results
    public static final String MOCK_TODAY_DATE = LocalDate.now().toString();

    // Appointments - Past (before mock today: 2025-10-29)
    public static final Appointment FIONA_ELLE_PAST = new Appointment(
            new AppointmentDatetime(LocalDate.now().minusMonths(1).toString() + "T12:00"), FIONA, ELLE);
    public static final Appointment FIONA_DANIEL_PAST = new Appointment(
            new AppointmentDatetime(LocalDate.now().minusMonths(2).plusDays(1).toString() + "T12:00"), FIONA, DANIEL);
    public static final Appointment FIONA_NOBUYER_PAST = new Appointment(
            new AppointmentDatetime(LocalDate.now().minusMonths(3).plusDays(2).toString() + "T12:00"), FIONA);
    public static final Appointment CARL_ALICE_PAST = new Appointment(
            new AppointmentDatetime(LocalDate.now().minusDays(14).toString() + "T10:00"), CARL, ALICE);

    // Appointments - Today (mock today: current date)
    public static final Appointment FIONA_BENSON_TODAY = new Appointment(
            new AppointmentDatetime(MOCK_TODAY_DATE + "T15:30"), FIONA, BENSON);

    // Appointments - Upcoming (after mock today: current date)
    public static final Appointment CARL_ELLE_UPCOMING = new Appointment(
            new AppointmentDatetime(LocalDate.now().plusDays(3).toString() + "T14:00"), CARL, ELLE);
    public static final Appointment GEORGE_ALICE_UPCOMING = new Appointment(
            new AppointmentDatetime(LocalDate.now().plusMonths(1).plusDays(16).toString() + "T11:00"), GEORGE, ALICE);

    private TypicalAppointments() {} // prevents instantiation

    /**
     * Returns a list of typical appointments for testing.
     * The list includes appointments from past, today (current date), and upcoming.
     * All appointments use dates relative to the current date to ensure tests pass on any date.
     */
    public static List<Appointment> getTypicalAppointments() {
        return new ArrayList<>(Arrays.asList(
                FIONA_ELLE_PAST, FIONA_DANIEL_PAST, FIONA_NOBUYER_PAST, CARL_ALICE_PAST,
                FIONA_BENSON_TODAY, CARL_ELLE_UPCOMING, GEORGE_ALICE_UPCOMING));
    }
}
