package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME_FILTER;

import seedu.address.logic.commands.ListAppointmentsCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.TimeFilter;

/**
 * Parses input arguments and creates a new ListAppointmentsCommand object
 */
public class ListAppointmentsCommandParser implements Parser<ListAppointmentsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListAppointmentsCommand
     * and returns a ListAppointmentsCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListAppointmentsCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TIME_FILTER);

        // Check if there are any invalid prefixes or arguments
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ListAppointmentsCommand.MESSAGE_USAGE));
        }

        // Reject multiple time/ prefixes (only a single time filter allowed)
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TIME_FILTER);

        // Check if time filter is provided
        if (argMultimap.getValue(PREFIX_TIME_FILTER).isEmpty()) {
            // No time filter specified, use default (today and upcoming)
            return new ListAppointmentsCommand();
        }

        String timeFilterValue = argMultimap.getValue(PREFIX_TIME_FILTER).get().toLowerCase().trim();
        TimeFilter timeFilter = parseTimeFilter(timeFilterValue);

        return new ListAppointmentsCommand(timeFilter);
    }

    /**
     * Parses the time filter string into a TimeFilter enum.
     */
    private TimeFilter parseTimeFilter(String timeFilterStr) throws ParseException {
        switch (timeFilterStr) {
        case "all":
            return TimeFilter.ALL;
        case "past":
            return TimeFilter.PAST;
        case "today":
            return TimeFilter.TODAY;
        case "upcoming":
            return TimeFilter.UPCOMING;
        default:
            throw new ParseException("Invalid time filter: " + timeFilterStr
                    + ". Valid options are: all, past, today, upcoming");
        }
    }
}
