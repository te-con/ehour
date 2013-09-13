package net.rrm.ehour.update;

import com.google.common.base.Optional;

public interface LatestVersionFetcher {
    Optional<Integer> getLatestVersionNumber();
}
