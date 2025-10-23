package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.AppointmentTimePredicate;
import seedu.address.model.appointment.TimeFilter;

/**
 * Contains integration tests (interaction with the Model) and unit tests for ListAppointmentsCommand.
 */
public class ListAppointmentsCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_showsAppointments() {
        // Default constructor should use TODAY_AND_UPCOMING filter
        expectedModel.updateFilteredAppointmentList(new AppointmentTimePredicate(TimeFilter.TODAY_AND_UPCOMING));
        CommandResult expectedCommandResult = new CommandResult(
                ListAppointmentsCommand.MESSAGE_SUCCESS, false, false, true, false);
        assertCommandSuccess(new ListAppointmentsCommand(),
                model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_showsAllAppointments() {
        expectedModel.updateFilteredAppointmentList(Model.PREDICATE_SHOW_ALL_APPOINTMENTS);
        CommandResult expectedCommandResult = new CommandResult(
                ListAppointmentsCommand.MESSAGE_SUCCESS, false, false, true, false);
        assertCommandSuccess(new ListAppointmentsCommand(TimeFilter.ALL),
                model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_showsPastAppointments() {
        expectedModel.updateFilteredAppointmentList(new AppointmentTimePredicate(TimeFilter.PAST));
        CommandResult expectedCommandResult = new CommandResult(
                ListAppointmentsCommand.MESSAGE_SUCCESS, false, false, true, false);
        assertCommandSuccess(new ListAppointmentsCommand(TimeFilter.PAST),
                model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_showsTodayAppointments() {
        expectedModel.updateFilteredAppointmentList(new AppointmentTimePredicate(TimeFilter.TODAY));
        CommandResult expectedCommandResult = new CommandResult(
                ListAppointmentsCommand.MESSAGE_SUCCESS, false, false, true, false);
        assertCommandSuccess(new ListAppointmentsCommand(TimeFilter.TODAY),
                model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_showsUpcomingAppointments() {
        expectedModel.updateFilteredAppointmentList(new AppointmentTimePredicate(TimeFilter.UPCOMING));
        CommandResult expectedCommandResult = new CommandResult(
                ListAppointmentsCommand.MESSAGE_SUCCESS, false, false, true, false);
        assertCommandSuccess(new ListAppointmentsCommand(TimeFilter.UPCOMING),
                model, expectedCommandResult, expectedModel);
    }

    @Test
    public void execute_todayAndUpcomingFilter_success() {
        expectedModel.updateFilteredAppointmentList(new AppointmentTimePredicate(TimeFilter.TODAY_AND_UPCOMING));
        CommandResult expectedCommandResult = new CommandResult(
                ListAppointmentsCommand.MESSAGE_SUCCESS, false, false, true, false);
        assertCommandSuccess(new ListAppointmentsCommand(TimeFilter.TODAY_AND_UPCOMING),
                model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals() {
        ListAppointmentsCommand listAllCommand = new ListAppointmentsCommand(TimeFilter.ALL);
        ListAppointmentsCommand listPastCommand = new ListAppointmentsCommand(TimeFilter.PAST);
        ListAppointmentsCommand listDefaultCommand = new ListAppointmentsCommand();

        // same object -> returns true
        assertTrue(listAllCommand.equals(listAllCommand));

        // same values -> returns true
        ListAppointmentsCommand listAllCommandCopy = new ListAppointmentsCommand(TimeFilter.ALL);
        assertTrue(listAllCommand.equals(listAllCommandCopy));

        // different types -> returns false
        assertFalse(listAllCommand.equals(1));

        // null -> returns false
        assertFalse(listAllCommand.equals(null));

        // different time filter -> returns false
        assertFalse(listAllCommand.equals(listPastCommand));

        // default constructor vs specified filter -> returns false if different
        assertFalse(listDefaultCommand.equals(listAllCommand));

        // two default constructors -> returns true
        ListAppointmentsCommand anotherDefaultCommand = new ListAppointmentsCommand();
        assertTrue(listDefaultCommand.equals(anotherDefaultCommand));
    }

    @Test
    public void toStringMethod() {
        ListAppointmentsCommand command = new ListAppointmentsCommand(TimeFilter.ALL);
        String expected = ListAppointmentsCommand.class.getCanonicalName() + "{timeFilter=ALL}";
        assertEquals(expected, command.toString());

        ListAppointmentsCommand defaultCommand = new ListAppointmentsCommand();
        String expectedDefault = ListAppointmentsCommand.class.getCanonicalName() + "{timeFilter=TODAY_AND_UPCOMING}";
        assertEquals(expectedDefault, defaultCommand.toString());
    }
}
