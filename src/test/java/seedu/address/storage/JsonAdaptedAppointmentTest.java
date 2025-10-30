package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedAppointment.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAppointments.FIONA_DANIEL_PAST;
import static seedu.address.testutil.TypicalAppointments.FIONA_NOBUYER_PAST;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.FIONA;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.appointment.AppointmentDatetime;
import seedu.address.model.person.Person;

public class JsonAdaptedAppointmentTest {
    private static final String INVALID_APPOINTMENT_DATETIME = "2025-13-01T12:00";

    private static final String VALID_APPOINTMENT_DATETIME = "2025-01-01T12:00";

    @Test
    public void toModelType_validAppointmentDatetime_returnsAppointment() throws Exception {
        JsonAdaptedAppointment appointment = new JsonAdaptedAppointment(FIONA_DANIEL_PAST);
        assertEquals(FIONA_DANIEL_PAST, appointment.toModelType(FIONA, DANIEL));
    }

    @Test
    public void toModelType_invalidAppointmentDatetime_throwsIllegalValueException() {
        JsonAdaptedAppointment appointment = new JsonAdaptedAppointment(INVALID_APPOINTMENT_DATETIME,
                FIONA.getPhone().value, DANIEL.getPhone().value);
        String expectedMessage = AppointmentDatetime.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, () -> appointment.toModelType(FIONA, DANIEL));
    }

    @Test
    public void toModelType_nullAppointment_throwsIllegalValueException() {
        JsonAdaptedAppointment appointment = new JsonAdaptedAppointment(null,
                FIONA.getPhone().value, DANIEL.getPhone().value);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, AppointmentDatetime.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> appointment.toModelType(FIONA, DANIEL));
    }

    @Test
    public void toModelType_nullSeller_throwsIllegalValueException() {
        JsonAdaptedAppointment appointment = new JsonAdaptedAppointment(VALID_APPOINTMENT_DATETIME,
                null, DANIEL.getPhone().value);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Person.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, () -> appointment.toModelType(null, DANIEL));
    }

    @Test
    public void toModelType_nullBuyer_returnsAppointment() throws Exception {
        JsonAdaptedAppointment appointment = new JsonAdaptedAppointment(
                FIONA_NOBUYER_PAST.appointmentDatetime.toString(),
                FIONA.getPhone().value, null);
        assertEquals(FIONA_NOBUYER_PAST, appointment.toModelType(FIONA, null));
    }
}
