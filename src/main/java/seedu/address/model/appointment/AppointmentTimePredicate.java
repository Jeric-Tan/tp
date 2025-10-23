package seedu.address.model.appointment;

import java.time.LocalDateTime;
import java.util.function.Predicate;

/**
 * Tests that an {@code Appointment}'s time matches the given time filter.
 */
public class AppointmentTimePredicate implements Predicate<Appointment> {
    private final TimeFilter timeFilter;

    public AppointmentTimePredicate(TimeFilter timeFilter) {
        this.timeFilter = timeFilter;
    }

    @Override
    public boolean test(Appointment appointment) {
        LocalDateTime appointmentTime = appointment.getAppointmentDatetime().datetime;
        LocalDateTime now = LocalDateTime.now();

        switch (timeFilter) {
        case ALL:
            return true;
        case PAST:
            return appointmentTime.isBefore(now);

        case TODAY:
            LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);
            return !appointmentTime.isBefore(startOfDay) && !appointmentTime.isAfter(endOfDay);

        case UPCOMING:
            return appointmentTime.isAfter(now);

        case TODAY_AND_UPCOMING:
            LocalDateTime startOfToday = now.toLocalDate().atStartOfDay();
            return !appointmentTime.isBefore(startOfToday);

        default:
            return true;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AppointmentTimePredicate)) {
            return false;
        }

        AppointmentTimePredicate otherPredicate = (AppointmentTimePredicate) other;
        return timeFilter.equals(otherPredicate.timeFilter);
    }

    @Override
    public String toString() {
        return "AppointmentTimePredicate{timeFilter=" + timeFilter + "}";
    }
}
