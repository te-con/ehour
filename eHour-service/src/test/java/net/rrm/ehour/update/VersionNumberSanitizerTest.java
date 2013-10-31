package net.rrm.ehour.update;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class VersionNumberSanitizerTest {
    @Test
    public void should_convert_1_0_0_to_1000() {
        assertEquals(1000, VersionNumberSanitizer.sanitize("1.0.0").get().intValue());
    }

    @Test
    public void should_discard_any_non_numbers() {
        assertEquals(1000, VersionNumberSanitizer.sanitize("1.0.0XX").get().intValue());
    }

    @Test
    public void should_discard_SNAPSHOT() {
        assertEquals(1000, VersionNumberSanitizer.sanitize("1.0.0-SNAPSHOT").get().intValue());
    }

    @Test
    public void should_append_0_for_short_version_numbers() {
        assertEquals(1300, VersionNumberSanitizer.sanitize("1.3").get().intValue());
    }

    @Test
    public void should_convert_1_2_2_21_to_1221() {
        assertEquals(1221, VersionNumberSanitizer.sanitize("1.2.2.1").get().intValue());
    }

    @Test
    public void should_not_trip_on_invalid_numbers() {
        assertFalse(VersionNumberSanitizer.sanitize("xxx").isPresent());
    }

    @Test
    public void should_handle_nulls() {
        assertFalse(VersionNumberSanitizer.sanitize(null).isPresent());
    }
}
