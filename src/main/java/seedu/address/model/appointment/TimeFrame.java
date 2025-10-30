package seedu.address.model.appointment;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents the timeframe filter for appointment searches.
 * Supports filtering appointments by past, today, or upcoming timeframes.
 */
public enum TimeFrame {
    PAST,
    TODAY,
    UPCOMING;

    public static final String MESSAGE_CONSTRAINTS =
            "Timeframe should be one of: past, today, upcoming (case-insensitive)";

    /**
     * Returns true if a given string is a valid timeframe.
     */
    public static boolean isValidTimeFrame(String test) {
        String normalized = test.trim().toUpperCase();
        try {
            TimeFrame.valueOf(normalized);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Parses a string into a TimeFrame.
     *
     * @param timeFrameStr The timeframe string to parse.
     * @return The corresponding TimeFrame enum value.
     * @throws IllegalArgumentException if the string is not a valid timeframe.
     */
    public static TimeFrame fromString(String timeFrameStr) {
        return TimeFrame.valueOf(timeFrameStr.trim().toUpperCase());
    }

    /**
     * Tests if an appointment matches this timeframe.
     *
     * @param appointment The appointment to test.
     * @return true if the appointment falls within this timeframe.
     */
    public boolean matches(Appointment appointment) {
        requireNonNull(appointment);

        LocalDateTime appointmentDateTime = appointment.getAppointmentDatetime().datetime;
        requireNonNull(appointmentDateTime);

        LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay();
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX);

        switch (this) {
        case PAST:
            return appointmentDateTime.isBefore(startOfToday);
        case TODAY:
            return !appointmentDateTime.isBefore(startOfToday) && !appointmentDateTime.isAfter(endOfToday);
        case UPCOMING:
            return !appointmentDateTime.isBefore(startOfToday);
        default:
            return false;
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
