package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.SearchAppointmentCommand;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;
import seedu.address.model.appointment.TimeFrame;

/**
 * Contains unit tests for SearchAppointmentCommandParser.
 */
public class SearchAppointmentCommandParserTest {

    private SearchAppointmentCommandParser parser = new SearchAppointmentCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SearchAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsSearchAppointmentCommand() {
        // no leading and trailing whitespaces
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedSearchAppointmentCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_singleKeyword_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Fiona")));
        assertParseSuccess(parser, "Fiona", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_multipleKeywords_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Alice", "Bob", "Charlie")));
        assertParseSuccess(parser, "Alice Bob Charlie", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_dateTimeKeyword_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("2025-01-01T12:00")));
        assertParseSuccess(parser, "2025-01-01T12:00", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_mixedKeywords_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Alice", "2025-01-01", "tokyo")));
        assertParseSuccess(parser, "Alice 2025-01-01 tokyo", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_specialCharacters_returnsSearchAppointmentCommand() {
        SearchAppointmentCommand expectedSearchAppointmentCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Block", "123,", "Street")));
        assertParseSuccess(parser, "Block 123, Street", expectedSearchAppointmentCommand);
    }

    @Test
    public void parse_onlyTimeFrame_returnsSearchAppointmentCommand() {
        // only past appointments
        SearchAppointmentCommand expectedPastCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Collections.emptyList(), TimeFrame.PAST));
        assertParseSuccess(parser, " tf/past", expectedPastCommand);

        // only today appointments
        SearchAppointmentCommand expectedTodayCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Collections.emptyList(), TimeFrame.TODAY));
        assertParseSuccess(parser, " tf/today", expectedTodayCommand);

        // only upcoming appointments
        SearchAppointmentCommand expectedUpcomingCommand =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Collections.emptyList(), TimeFrame.UPCOMING));
        assertParseSuccess(parser, " tf/upcoming", expectedUpcomingCommand);
    }

    @Test
    public void parse_keywordsWithTimeFrame_returnsSearchAppointmentCommand() {
        // keywords before tf/
        SearchAppointmentCommand expectedCommand1 =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Alice", "Bob"), TimeFrame.TODAY));
        assertParseSuccess(parser, "Alice Bob tf/today", expectedCommand1);

        // keywords after tf/
        SearchAppointmentCommand expectedCommand2 =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Alice", "Bob"), TimeFrame.UPCOMING));
        assertParseSuccess(parser, " tf/upcoming Alice Bob", expectedCommand2);

        // keywords before and after tf/
        SearchAppointmentCommand expectedCommand3 =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Arrays.asList("Alice", "Bob"), TimeFrame.PAST));
        assertParseSuccess(parser, "Alice tf/past Bob", expectedCommand3);
    }

    @Test
    public void parse_invalidTimeFrame_throwsParseException() {
        assertParseFailure(parser, " tf/invalid", TimeFrame.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " tf/tomorrow", TimeFrame.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_caseInsensitiveTimeFrame_returnsSearchAppointmentCommand() {
        // lowercase
        SearchAppointmentCommand expectedCommand1 =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Collections.emptyList(), TimeFrame.PAST));
        assertParseSuccess(parser, " tf/past", expectedCommand1);

        // uppercase
        SearchAppointmentCommand expectedCommand2 =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Collections.emptyList(), TimeFrame.TODAY));
        assertParseSuccess(parser, " tf/TODAY", expectedCommand2);

        // mixed case
        SearchAppointmentCommand expectedCommand3 =
                new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(
                        Collections.emptyList(), TimeFrame.UPCOMING));
        assertParseSuccess(parser, " tf/UpCoMiNg", expectedCommand3);
    }

    @Test
    public void parse_duplicateTimeFramePrefix_throwsParseException() {
        assertParseFailure(parser, " tf/past tf/today",
                Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_TIMEFRAME));
    }
}
