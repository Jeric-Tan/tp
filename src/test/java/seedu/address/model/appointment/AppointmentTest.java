package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAppointments.FIONA_DANIEL_PAST;
import static seedu.address.testutil.TypicalAppointments.FIONA_ELLE_PAST;
import static seedu.address.testutil.TypicalAppointments.FIONA_NOBUYER_PAST;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.DANIEL_EDITED;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.FIONA_EDITED;
import static seedu.address.testutil.TypicalPersons.GEORGE;

import org.junit.jupiter.api.Test;

public class AppointmentTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Appointment(null, null, null));
    }

    @Test
    public void isPersonSeller() {
        Appointment appointment = FIONA_ELLE_PAST;
        assertTrue(appointment.isPersonSeller(FIONA));

        // null is false
        assertFalse(appointment.isPersonSeller(null));

        // Elle is the buyer, not the seller
        assertFalse(appointment.isPersonSeller(ELLE));

        // George is not involved in this appointment
        assertFalse(appointment.isPersonSeller(GEORGE));
    }

    @Test
    public void isPersonBuyer() {
        Appointment appointment = FIONA_ELLE_PAST;
        assertTrue(appointment.isPersonBuyer(ELLE));

        // null is false
        assertFalse(appointment.isPersonBuyer(null));

        // Fiona is the seller, not the buyer
        assertFalse(appointment.isPersonBuyer(FIONA));

        // George is not involved in this appointment
        assertFalse(appointment.isPersonBuyer(GEORGE));
    }

    @Test
    public void updatedWithEditedPerson_personIsSeller_returnsAppointment() {
        // Test with buyer - use FIONA_DANIEL_PAST
        Appointment appointment = FIONA_DANIEL_PAST;
        Appointment newAppointment = appointment.updatedWithEditedPerson(FIONA, FIONA_EDITED);
        // Check that seller was updated
        assertTrue(newAppointment.isPersonSeller(FIONA_EDITED));
        assertTrue(newAppointment.isPersonBuyer(DANIEL));
        // Original should remain unchanged
        assertTrue(appointment.isPersonSeller(FIONA));
        assertTrue(appointment.isPersonBuyer(DANIEL));

        // Test without buyer - use FIONA_NOBUYER_PAST
        appointment = FIONA_NOBUYER_PAST;
        newAppointment = appointment.updatedWithEditedPerson(FIONA, FIONA_EDITED);
        // Check that seller was updated
        assertTrue(newAppointment.isPersonSeller(FIONA_EDITED));
        assertFalse(newAppointment.isPersonBuyer(DANIEL));
        // Original should remain unchanged
        assertTrue(appointment.isPersonSeller(FIONA));
    }

    @Test
    public void updatedWithEditedPerson_personIsBuyer_returnsAppointment() {
        Appointment appointment = FIONA_DANIEL_PAST;
        Appointment newAppointment = appointment.updatedWithEditedPerson(DANIEL, DANIEL_EDITED);
        // Check that buyer was updated
        assertTrue(newAppointment.isPersonSeller(FIONA));
        assertTrue(newAppointment.isPersonBuyer(DANIEL_EDITED));
        // Original should remain unchanged
        assertTrue(appointment.isPersonSeller(FIONA));
        assertTrue(appointment.isPersonBuyer(DANIEL));
    }

    @Test
    public void updatedWithEditedPerson_personIsNotParticipant_returnsSameAppointment() {
        Appointment appointment = FIONA_ELLE_PAST;
        Appointment newAppointment = appointment.updatedWithEditedPerson(DANIEL, DANIEL_EDITED);
        // Should return same appointment since DANIEL is not a participant
        assertEquals(appointment, newAppointment);
    }

    @Test
    public void equals() {
        AppointmentDatetime appointmentDatetime = new AppointmentDatetime("2025-01-01T00:00");
        Appointment appointment = new Appointment(appointmentDatetime, BENSON, ALICE);

        // same values -> returns true
        appointmentDatetime = new AppointmentDatetime("2025-01-01T00:00");
        assertTrue(appointment.equals(new Appointment(appointmentDatetime, BENSON, ALICE)));

        // same object -> returns true
        assertTrue(appointment.equals(appointment));

        // null -> returns false
        assertFalse(appointment.equals(null));

        // different types -> returns false
        assertFalse(appointment.equals(5.0f));

        // different datetime -> returns false
        appointmentDatetime = new AppointmentDatetime("2025-02-01T00:00");
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, BENSON, ALICE)));

        // different seller -> returns false
        appointmentDatetime = new AppointmentDatetime("2025-01-01T00:00");
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, CARL, ALICE)));

        // different buyer -> returns false
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, BENSON, CARL)));

        // no buyer -> returns false
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, BENSON)));

        // appointment with no buyer
        appointment = new Appointment(appointmentDatetime, BENSON);

        // same values -> returns true
        assertTrue(appointment.equals(new Appointment(appointmentDatetime, BENSON)));

        // different values -> returns false
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, ALICE)));

        // compare to appointment with buyer -> returns false
        assertFalse(appointment.equals(new Appointment(appointmentDatetime, BENSON, ALICE)));


    }

    @Test
    public void containsKeyword() {
        Appointment appointment = FIONA_ELLE_PAST; // FIONA (seller) and ELLE (buyer), date is 1 month ago

        // Seller name matching (Fiona Kunz)
        assertTrue(appointment.containsKeyword("Fiona"));
        assertTrue(appointment.containsKeyword("Kunz"));

        // Buyer name matching (Elle Meyer)
        assertTrue(appointment.containsKeyword("Elle"));
        assertTrue(appointment.containsKeyword("Meyer"));

        // Seller details matching (from FIONA)
        assertTrue(appointment.containsKeyword("9482427")); // Fiona's phone
        assertTrue(appointment.containsKeyword("lydia@example.com")); // Fiona's email
        assertTrue(appointment.containsKeyword("tokyo")); // part of "little tokyo" address
        assertTrue(appointment.containsKeyword("HDB_J")); // Fiona's property type
        assertTrue(appointment.containsKeyword("seller")); // Fiona's role

        // Buyer details matching (from ELLE)
        assertTrue(appointment.containsKeyword("9482224")); // Elle's phone
        assertTrue(appointment.containsKeyword("werner@example.com")); // Elle's email
        assertTrue(appointment.containsKeyword("michegan")); // part of "michegan ave" address
        assertTrue(appointment.containsKeyword("HDB_5")); // Elle's property type
        assertTrue(appointment.containsKeyword("buyer")); // Elle's role

        // Appointment datetime matching - use the actual datetime from the appointment
        String appointmentDatetimeString = appointment.appointmentDatetime.toString();
        assertTrue(appointment.containsKeyword(appointmentDatetimeString));
        // Partial datetime should not match
        assertFalse(appointment.containsKeyword(appointmentDatetimeString.substring(0, 10))); // Date only
        assertFalse(appointment.containsKeyword("12:00")); // Time only

        // Case insensitive matching
        assertTrue(appointment.containsKeyword("FIONA"));
        assertTrue(appointment.containsKeyword("elle"));
        assertTrue(appointment.containsKeyword("TOKYO"));

        // Non-matching keywords
        assertFalse(appointment.containsKeyword("Charlie"));
        assertFalse(appointment.containsKeyword("nonexistent"));
        assertFalse(appointment.containsKeyword("2024"));

        // Partial word matching should not work (StringUtil requires word boundaries)
        assertFalse(appointment.containsKeyword("Fio"));
        assertFalse(appointment.containsKeyword("Mey"));
    }

}
