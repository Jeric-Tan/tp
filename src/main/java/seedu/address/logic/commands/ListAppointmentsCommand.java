package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_APPOINTMENTS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.appointment.AppointmentTimePredicate;
import seedu.address.model.appointment.TimeFilter;

/**
 * Lists appointments in the address book to the user, filtered by time if specified,
 * by default shows today and upcoming appointments.
 */
public class ListAppointmentsCommand extends Command {

    public static final String COMMAND_WORD = "lap";

    public static final String MESSAGE_SUCCESS = "Listed all active appointments";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists appointments.\n"
        + "Parameters: [time/TIME_FILTER]\n"
        + "TIME_FILTER can be: all, past, today, upcoming\n"
        + "If no time filter is specified, shows today and upcoming appointments.\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " (shows today and upcoming appointments)\n"
        + "  " + COMMAND_WORD + " time/past (shows past appointments)\n";

    private final TimeFilter timeFilter;

    /**
     * Creates a ListAppointmentsCommand with default time filter (today and upcoming).
     */
    public ListAppointmentsCommand() {
        this.timeFilter = TimeFilter.TODAY_AND_UPCOMING;
    }

    /**
     * Creates a ListAppointmentsCommand with the specified time filter.
     */
    public ListAppointmentsCommand(TimeFilter timeFilter) {
        this.timeFilter = timeFilter;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        if (timeFilter == TimeFilter.ALL) {
            // Show all appointments
            model.updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        } else {
            // Apply time filter
            model.updateFilteredAppointmentList(new AppointmentTimePredicate(timeFilter));
        }

        return new CommandResult(MESSAGE_SUCCESS, false, false, true, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof ListAppointmentsCommand)) {
            return false;
        }

        ListAppointmentsCommand otherCommand = (ListAppointmentsCommand) other;
        return (timeFilter == null && otherCommand.timeFilter == null)
                || (timeFilter != null && timeFilter.equals(otherCommand.timeFilter));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("timeFilter", timeFilter)
                .toString();
    }
}
