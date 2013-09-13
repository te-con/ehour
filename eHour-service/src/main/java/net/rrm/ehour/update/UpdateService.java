package net.rrm.ehour.update;

import com.google.common.base.Optional;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class UpdateService {

    private static final Logger LOGGER = Logger.getLogger(UpdateService.class);
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();

    private Integer thisVersion;

    private LatestVersionFetcher latestVersionFetcher;
    private Future<Optional<String>> latestVersionNumber;

    @SuppressWarnings("UnusedDeclaration")
    public UpdateService() {
    }

    public UpdateService(String thisVersion, LatestVersionFetcher latestVersionFetcher) {
        Optional<Integer> sanitizedVersionNumber = VersionNumberSanitizer.sanitize(thisVersion);
        this.thisVersion = sanitizedVersionNumber.or(0);

        this.latestVersionFetcher = latestVersionFetcher;
    }

    @Scheduled(cron ="0 0 3 * * *")
    public void fetchLatestVersion() {

        synchronized (this) {
            VersionFetcher versionFetcher = new VersionFetcher();

            if (latestVersionNumber == null || latestVersionNumber.isDone()) {
                latestVersionNumber = EXECUTOR.submit(versionFetcher);
            }
        }
    }

    public Optional<String> getLatestVersionNumber() {
        if (isLatestVersionNumberValid()) {
            try {
                return latestVersionNumber.get();
            } catch (Exception e) {
                LOGGER.warn("Failed to retrieve the latest version number", e);
            }
        }

        return Optional.absent();
    }

    private boolean isLatestVersionNumberValid() {
        return latestVersionNumber.isDone() && !latestVersionNumber.isCancelled();
    }

    public boolean isLatestVersion() {
        Optional<String> latest = getLatestVersionNumber();

        return thisVersion >= VersionNumberSanitizer.sanitize(latest.or("")).or(0);
    }

    class VersionFetcher implements Callable<Optional<String>> {

        @Override
        public Optional<String> call() throws Exception {
            return latestVersionFetcher.getLatestVersionNumber();
        }
    }
}
