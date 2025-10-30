package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_APPOINTMENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.AppointmentContainsKeywordsPredicate;
import seedu.address.model.appointment.TimeFrame;

/**
 * Contains integration tests (interaction with the Model) for {@code SearchAppointmentCommand}.
 */
public class SearchAppointmentCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        AppointmentContainsKeywordsPredicate firstPredicate =
                new AppointmentContainsKeywordsPredicate(Collections.singletonList("first"));
        AppointmentContainsKeywordsPredicate secondPredicate =
                new AppointmentContainsKeywordsPredicate(Collections.singletonList("second"));

        SearchAppointmentCommand searchFirstCommand = new SearchAppointmentCommand(firstPredicate);
        SearchAppointmentCommand searchSecondCommand = new SearchAppointmentCommand(secondPredicate);

        // same object -> returns true
        assertTrue(searchFirstCommand.equals(searchFirstCommand));

        // same values -> returns true
        SearchAppointmentCommand searchFirstCommandCopy = new SearchAppointmentCommand(firstPredicate);
        assertTrue(searchFirstCommand.equals(searchFirstCommandCopy));

        // different types -> returns false
        assertFalse(searchFirstCommand.equals(1));

        // null -> returns false
        assertFalse(searchFirstCommand.equals(null));

        // different predicate -> returns false
        assertFalse(searchFirstCommand.equals(searchSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noAppointmentFound() {
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 0);
        AppointmentContainsKeywordsPredicate predicate = preparePredicate(" ");
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredAppointmentList());
    }

    @Test
    public void execute_singleKeyword_multipleAppointmentsFound() {
        // Search for "Fiona" who has 4 appointments
        // Should find 4 appointments: FIONA_ELLE_PAST, FIONA_DANIEL_PAST, FIONA_NOBUYER_PAST, BENSON_CARL_PAST
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 4);
        AppointmentContainsKeywordsPredicate predicate = preparePredicate("Fiona");
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_multipleKeywords_multipleAppointmentsFound() {
        // Search for "Elle" or "Fiona" who have a total of 5 appointments
        // Should find 5 appointments: FIONA_ELLE_PAST, FIONA_DANIEL_PAST, FIONA_NOBUYER_PAST,
        // BENSON_CARL_PAST, CARL_ELLE_UPCOMING
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 5);
        AppointmentContainsKeywordsPredicate predicate = preparePredicate("Elle Fiona");
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }



    @Test
    public void execute_timeFramePast_pastAppointmentsFound() {
        // Should find 4 "past" appointments: FIONA_ELLE_PAST, FIONA_DANIEL_PAST, FIONA_NOBUYER_PAST, BENSON_CARL_PAST
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 4);
        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                Collections.emptyList(), TimeFrame.PAST);
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_timeFrameToday_todayAppointmentsFound() {
        // Should find 1 "today" appointment: FIONA_BENSON_TODAY
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 1);
        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                Collections.emptyList(), TimeFrame.TODAY);
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_timeFrameUpcoming_upcomingAppointmentsFound() {
        // Should find 3 (all) "upcoming" appointments: FIONA_BENSON_TODAY, CARL_ELLE_UPCOMING, GEORGE_ALICE_UPCOMING
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 3);
        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                Collections.emptyList(), TimeFrame.UPCOMING);
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_keywordAndTimeFrame_filteredAppointmentsFound() {
        // Should find 1 "upcoming" "George" appointment: GEORGE_ALICE_UPCOMING
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 1);
        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                Collections.singletonList("George"), TimeFrame.UPCOMING);
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_keywordAndTimeFramePast_filteredAppointmentsFound() {
        // Search for "past" "Fiona" appointments
        // Should find 3 appointments: FIONA_ELLE_PAST, FIONA_DANIEL_PAST, FIONA_NOBUYER_PAST
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 3);
        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                Collections.singletonList("Fiona"), TimeFrame.PAST);
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_multipleKeywordsAndTimeFrame_filteredAppointmentsFound() {
        // Search for "Carl" OR "Elle" in upcoming appointments
        // Should find: CARL_ELLE_UPCOMING (1 appointment - has both Carl and Elle)
        String expectedMessage = String.format(MESSAGE_APPOINTMENTS_LISTED_OVERVIEW, 1);
        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                Arrays.asList("Carl", "Elle"), TimeFrame.UPCOMING);
        SearchAppointmentCommand command = new SearchAppointmentCommand(predicate);
        expectedModel.updateFilteredAppointmentList(predicate);
        CommandResult expectedCommandResult = new CommandResult(expectedMessage, false, false, true, false);
        assertCommandSuccess(command, model, expectedCommandResult, expectedModel);
    }

    @Test
    public void toStringMethod() {
        AppointmentContainsKeywordsPredicate predicate = new AppointmentContainsKeywordsPredicate(
                Arrays.asList("keyword"));
        SearchAppointmentCommand searchAppointmentCommand = new SearchAppointmentCommand(predicate);
        String expected = SearchAppointmentCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";
        assertEquals(expected, searchAppointmentCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code AppointmentContainsKeywordsPredicate}.
     */
    private AppointmentContainsKeywordsPredicate preparePredicate(String userInput) {
        return new AppointmentContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
