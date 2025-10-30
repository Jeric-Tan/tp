package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIMEFRAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.SearchAppointmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;
import seedu.address.model.appointment.TimeFrame;

/**
 * Parses input arguments and creates a new SearchAppointmentCommand object
 */
public class SearchAppointmentCommandParser implements Parser<SearchAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SearchAppointmentCommand
     * and returns a SearchAppointmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public SearchAppointmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TIMEFRAME);

        String preamble;
        TimeFrame timeFrame;
        List<String> keywords;

        preamble = argMultimap.getPreamble().trim();

        if (argMultimap.getValue(PREFIX_TIMEFRAME).isPresent()) {
            String timeValueStr = argMultimap.getValue(PREFIX_TIMEFRAME).get().trim();
            String[] timeValueParts = timeValueStr.split("\\s+");

            timeFrame = ParserUtil.parseTimeFrame(timeValueParts[0]);
            keywords = combineKeywords(preamble, extractAdditionalKeywords(timeValueParts));
        } else {
            timeFrame = null;
            keywords = combineKeywords(preamble, new ArrayList<>());
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_TIMEFRAME);

        if (keywords.isEmpty() && timeFrame == null) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchAppointmentCommand.MESSAGE_USAGE));
        }

        if (timeFrame != null) {
            return new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(keywords, timeFrame));
        } else {
            return new SearchAppointmentCommand(new AppointmentContainsKeywordsPredicate(keywords));
        }
    }

    /**
     * Extracts additional keywords from the timeframe value parts.
     * If there are words after the timeframe, they are treated as keywords.
     * @param timeValueParts The parts of the timeframe value split by whitespace
     * @return A list of additional keywords
     */
    private List<String> extractAdditionalKeywords(String[] timeValueParts) {
        if (timeValueParts.length > 1) {
            return Arrays.asList(Arrays.copyOfRange(timeValueParts, 1, timeValueParts.length));
        }
        return new ArrayList<>();
    }

    /**
     * Combines preamble keywords with additional keywords from the timeframe value.
     * @param preamble The preamble string containing keywords
     * @param additionalKeywords Additional keywords from the timeframe value
     * @return A list of all combined keywords
     */
    private List<String> combineKeywords(String preamble, List<String> additionalKeywords) {
        List<String> allKeywords = new ArrayList<>();
        if (!preamble.isEmpty()) {
            allKeywords.addAll(Arrays.asList(preamble.split("\\s+")));
        }
        allKeywords.addAll(additionalKeywords);
        return allKeywords;
    }
}
