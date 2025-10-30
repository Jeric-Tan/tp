package seedu.address.model.appointment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalAppointments.FIONA_DANIEL_PAST;
import static seedu.address.testutil.TypicalAppointments.FIONA_ELLE_PAST;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.DANIEL_EDITED;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.FIONA_EDITED;
import static seedu.address.testutil.TypicalPersons.GEORGE;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;

public class UniqueAppointmentListTest {

    private final UniqueAppointmentList uniqueAppointmentList = new UniqueAppointmentList();

    @Test
    public void contains_nullAppointmentThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAppointmentList.contains(null));
    }

    @Test
    public void contains_appointmentNotInList_returnsFalse() {
        assertFalse(uniqueAppointmentList.contains(FIONA_DANIEL_PAST));
    }

    @Test
    public void contains_appointmentInList_returnsTrue() {
        uniqueAppointmentList.add(FIONA_DANIEL_PAST);
        assertTrue(uniqueAppointmentList.contains(FIONA_DANIEL_PAST));
    }

    @Test
    public void add_nullAppointment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAppointmentList.add(null));
    }

    @Test
    public void add_duplicateAppointment_throwsDuplicateAppointmentException() {
        uniqueAppointmentList.add(FIONA_DANIEL_PAST);
        assertThrows(DuplicateAppointmentException.class, () -> uniqueAppointmentList.add(FIONA_DANIEL_PAST));
    }

    @Test
    public void set_uniqueAppointmentList_replacesOwnListWithProvidedUniqueAppointmentList() {
        uniqueAppointmentList.add(FIONA_ELLE_PAST);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        expectedUniqueAppointmentList.add(FIONA_DANIEL_PAST);
        uniqueAppointmentList.setAppointments(expectedUniqueAppointmentList);
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void set_nonUniqueAppointmentList_failure() {
        uniqueAppointmentList.add(FIONA_ELLE_PAST);
        List<Appointment> expectedAppointmentList = new ArrayList<>();
        expectedAppointmentList.add(FIONA_DANIEL_PAST);
        expectedAppointmentList.add(FIONA_DANIEL_PAST);
        assertThrows(DuplicateAppointmentException.class, () ->
                uniqueAppointmentList.setAppointments(expectedAppointmentList));
    }

    @Test
    public void updateAppointmentsWithPerson_updateBuyer_success() {
        AppointmentDatetime datetime = new AppointmentDatetime("2025-01-01T00:00");
        Appointment fionaDaniel = new Appointment(datetime, FIONA, DANIEL);
        Appointment fionaDanielEdited = new Appointment(datetime, FIONA, DANIEL_EDITED);

        uniqueAppointmentList.add(fionaDaniel);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        expectedUniqueAppointmentList.add(fionaDanielEdited);
        uniqueAppointmentList.updateAppointmentsWithEditedPerson(DANIEL, DANIEL_EDITED);
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void updateAppointmentsWithPerson_updateSeller_success() {
        AppointmentDatetime datetime = new AppointmentDatetime("2025-01-01T00:00");
        Appointment fionaDaniel = new Appointment(datetime, FIONA, DANIEL);
        Appointment fionaEditedDaniel = new Appointment(datetime, FIONA_EDITED, DANIEL);

        uniqueAppointmentList.add(fionaDaniel);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        expectedUniqueAppointmentList.add(fionaEditedDaniel);
        uniqueAppointmentList.updateAppointmentsWithEditedPerson(FIONA, FIONA_EDITED);
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void updateAppointmentsWithPerson_updateNone_success() {
        AppointmentDatetime datetime = new AppointmentDatetime("2025-01-01T00:00");
        Appointment georgeBenson = new Appointment(datetime, GEORGE, BENSON);

        uniqueAppointmentList.add(georgeBenson);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        expectedUniqueAppointmentList.add(georgeBenson);
        uniqueAppointmentList.updateAppointmentsWithEditedPerson(FIONA, FIONA_EDITED);
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void updateAppointmentsWithPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                uniqueAppointmentList.updateAppointmentsWithEditedPerson(null, FIONA_EDITED));
        assertThrows(NullPointerException.class, () ->
                uniqueAppointmentList.updateAppointmentsWithEditedPerson(FIONA, null));
    }

    @Test
    public void remove_nullAppointment_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueAppointmentList.remove(null));
    }

    @Test
    public void remove_appointmentDoesNotExist_throwsAppointmentNotFoundException() {
        assertThrows(AppointmentNotFoundException.class, () -> uniqueAppointmentList.remove(FIONA_DANIEL_PAST));
    }

    @Test
    public void remove_existingAppointment_removesAppointment() {
        uniqueAppointmentList.add(FIONA_DANIEL_PAST);
        uniqueAppointmentList.remove(FIONA_DANIEL_PAST);
        UniqueAppointmentList expectedUniqueAppointmentList = new UniqueAppointmentList();
        assertEquals(expectedUniqueAppointmentList, uniqueAppointmentList);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueAppointmentList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueAppointmentList.asUnmodifiableObservableList().toString(), uniqueAppointmentList.toString());
    }
}
