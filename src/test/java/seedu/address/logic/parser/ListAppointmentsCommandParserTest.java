package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME_FILTER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.ListAppointmentsCommand;
import seedu.address.model.appointment.TimeFilter;

public class ListAppointmentsCommandParserTest {

    private ListAppointmentsCommandParser parser = new ListAppointmentsCommandParser();

    @Test
    public void parse_noArgs_returnsListAppointmentsCommand() {
        // Empty input should return command with default filter (TODAY_AND_UPCOMING)
        assertParseSuccess(parser, "", new ListAppointmentsCommand());
        assertParseSuccess(parser, "   ", new ListAppointmentsCommand());
    }

    @Test
    public void parse_validTimeFilterAll_returnsListAppointmentsCommand() {
        assertParseSuccess(parser, " time/all", new ListAppointmentsCommand(TimeFilter.ALL));
        assertParseSuccess(parser, " time/ALL", new ListAppointmentsCommand(TimeFilter.ALL));
    }

    @Test
    public void parse_validTimeFilterPast_returnsListAppointmentsCommand() {
        assertParseSuccess(parser, " time/past", new ListAppointmentsCommand(TimeFilter.PAST));
        assertParseSuccess(parser, " time/PAST", new ListAppointmentsCommand(TimeFilter.PAST));
    }

    @Test
    public void parse_validTimeFilterToday_returnsListAppointmentsCommand() {
        assertParseSuccess(parser, " time/today", new ListAppointmentsCommand(TimeFilter.TODAY));
        assertParseSuccess(parser, " time/TODAY", new ListAppointmentsCommand(TimeFilter.TODAY));
    }

    @Test
    public void parse_validUpcomingFilter_returnsCommand() {
        assertParseSuccess(parser, " time/upcoming", new ListAppointmentsCommand(TimeFilter.UPCOMING));
        assertParseSuccess(parser, " time/UPCOMING", new ListAppointmentsCommand(TimeFilter.UPCOMING));
    }

    @Test
    public void parse_invalidTimeFilter_throwsParseException() {
        // Invalid time filter values
        assertParseFailure(parser, " time/invalid",
                "Invalid time filter: invalid. Valid options are: all, past, today, upcoming");
        assertParseFailure(parser, " time/",
                "Invalid time filter: . Valid options are: all, past, today, upcoming");
    }

    @Test
    public void parse_invalidPreamble_throwsParseException() {
        // Non-empty preamble should throw exception
        assertParseFailure(parser, "some random text",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentsCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "abc time/all",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentsCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "123 time/past",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListAppointmentsCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleTimeFilters_throwsParseException() {
        // Multiple time filters should not be allowed
        assertParseFailure(parser, " time/all time/past",
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME_FILTER));
        assertParseFailure(parser, " time/today time/upcoming",
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_TIME_FILTER));
    }

    @Test
    public void parse_caseInsensitive_success() {
        // Parser should handle case-insensitive input
        assertParseSuccess(parser, " time/All", new ListAppointmentsCommand(TimeFilter.ALL));
        assertParseSuccess(parser, " time/PaSt", new ListAppointmentsCommand(TimeFilter.PAST));
        assertParseSuccess(parser, " time/ToDaY", new ListAppointmentsCommand(TimeFilter.TODAY));
        assertParseSuccess(parser, " time/UpCoMiNg", new ListAppointmentsCommand(TimeFilter.UPCOMING));
    }

    @Test
    public void parse_extraWhitespaces_success() {
        assertParseSuccess(parser, "   time/past", new ListAppointmentsCommand(TimeFilter.PAST));
    }
}
