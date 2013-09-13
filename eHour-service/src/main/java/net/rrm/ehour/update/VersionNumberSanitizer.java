package net.rrm.ehour.update;

import com.google.common.base.Optional;
import org.apache.commons.lang.StringUtils;

public abstract class VersionNumberSanitizer {
    public static Optional<Integer> sanitize(String version) {
        if (version == null) {
            return Optional.absent();
        }

        String stripped = version.replaceAll("\\D", "");

        if (StringUtils.isEmpty(stripped)) {
            return Optional.absent();
        }

        try {
            return Optional.of(Integer.valueOf(StringUtils.rightPad(stripped, 3, '0')));
        } catch (NumberFormatException nfe) {
            return Optional.absent();
        }
    }
}
