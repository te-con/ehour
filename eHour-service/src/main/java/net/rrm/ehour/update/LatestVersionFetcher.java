package net.rrm.ehour.update;

import com.google.common.base.Optional;

public interface LatestVersionFetcher {
    Optional<String> getLatestVersionNumber(String currentVersion, boolean isScheduled);
}
