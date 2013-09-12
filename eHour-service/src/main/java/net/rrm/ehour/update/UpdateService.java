package net.rrm.ehour.update;

import com.google.common.base.Optional;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

public class UpdateService {

    private static final Logger LOGGER = Logger.getLogger(UpdateService.class);

    private String thisVersion;

    private LatestVersionFetcher latestVersionFetcher;
    private Optional<String> latestVersionNumber;

    public UpdateService(String thisVersion, LatestVersionFetcher latestVersionFetcher) {
        this.thisVersion = thisVersion;
        this.latestVersionFetcher = latestVersionFetcher;
    }

    @Scheduled(cron ="0 0 3 * * *")
    public void fetchLatestVersion() {
        LOGGER.info("Fetching latest version from");

        synchronized (this) {
            latestVersionNumber = latestVersionFetcher.getLatestVersionNumber();
        }
    }

    public boolean isLatestVersion() {
        return !latestVersionNumber.isPresent() || latestVersionNumber.get().equalsIgnoreCase(thisVersion);
    }
}
