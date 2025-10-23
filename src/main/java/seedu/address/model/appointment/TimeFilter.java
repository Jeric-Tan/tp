package seedu.address.model.appointment;

/**
 * Represents the time filtering options for appointments.
 */
public enum TimeFilter {
    ALL,
    PAST,
    TODAY,
    UPCOMING,
    TODAY_AND_UPCOMING // Default: combines TODAY + UPCOMING
}
