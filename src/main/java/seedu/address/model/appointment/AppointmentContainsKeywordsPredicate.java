package seedu.address.model.appointment;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that an {@code Appointment}'s details match any of the keywords given.
 * Searches across seller name, buyer name, seller address, and appointment datetime.
 * Can optionally filter by timeframe (past, today, or upcoming).
 */
public class AppointmentContainsKeywordsPredicate implements Predicate<Appointment> {
    private final List<String> keywords;
    private final TimeFrame timeFrame;

    /**
     * Constructs a predicate with keywords only.
     *
     * @param keywords The list of keywords to search for.
     */
    public AppointmentContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
        this.timeFrame = null;
    }

    /**
     * Constructs a predicate with keywords and a timeframe filter.
     *
     * @param keywords The list of keywords to search for.
     * @param timeFrame The timeframe to filter by.
     */
    public AppointmentContainsKeywordsPredicate(List<String> keywords, TimeFrame timeFrame) {
        this.keywords = keywords;
        this.timeFrame = timeFrame;
    }

    @Override
    public boolean test(Appointment appointment) {
        // If both keywords and timeframe are empty/not present, return false (no criteria to match)
        if (keywords.isEmpty() && timeFrame == null) {
            return false;
        }

        // Check keyword matching (if keywords are provided)
        boolean matchesKeywords = keywords.isEmpty()
                || keywords.stream().anyMatch(keyword -> matchesAnyField(appointment, keyword));

        // Check timeframe matching (if timeframe is provided)
        boolean matchesTimeFrame = timeFrame == null || timeFrame.matches(appointment);

        // Both conditions must be satisfied
        return matchesKeywords && matchesTimeFrame;
    }

    /**
     * Checks if the keyword matches any field of the appointment.
     */
    private boolean matchesAnyField(Appointment appointment, String keyword) {
        return appointment.containsKeyword(keyword);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AppointmentContainsKeywordsPredicate)) {
            return false;
        }

        AppointmentContainsKeywordsPredicate otherPredicate = (AppointmentContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords)
                && Objects.equals(timeFrame, otherPredicate.timeFrame);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("keywords", keywords)
                .add("timeFrame", timeFrame)
                .toString();
    }
}
