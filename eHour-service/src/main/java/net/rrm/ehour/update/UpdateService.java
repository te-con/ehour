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

    private String formattedVersion;
    private Integer sanitizedVersion;

    private LatestVersionFetcher latestVersionFetcher;
    private Future<Optional<String>> latestVersionNumber;

    @SuppressWarnings("UnusedDeclaration")
    public UpdateService() {
    }

    public UpdateService(String formattedVersion, LatestVersionFetcher latestVersionFetcher) {
        this.formattedVersion = formattedVersion;
        Optional<Integer> sanitizedVersionNumber = VersionNumberSanitizer.sanitize(formattedVersion);
        this.sanitizedVersion = sanitizedVersionNumber.or(0);

        this.latestVersionFetcher = latestVersionFetcher;
    }

    @Scheduled(cron ="0 0 3 * * *")
    public void scheduledFetchLatestVersion() {
        fetchLatestVersion(new VersionFetcher(true));
    }

    public void initialFetchLatestVersion() {
        fetchLatestVersion(new VersionFetcher(false));
    }

    private void fetchLatestVersion(VersionFetcher versionFetcher) {
        synchronized (this) {
            if (latestVersionNumber == null || latestVersionNumber.isDone()) {
                latestVersionNumber = EXECUTOR.submit(versionFetcher);
            }
        }
    }


    public synchronized Optional<String> getLatestVersionNumber() {
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

        return sanitizedVersion >= VersionNumberSanitizer.sanitize(latest.or("")).or(0);
    }

    class VersionFetcher implements Callable<Optional<String>> {
        private final boolean isScheduled;

        VersionFetcher(boolean isScheduled) {
            this.isScheduled = isScheduled;
        }

        @Override
        public Optional<String> call() throws Exception {
            return latestVersionFetcher.getLatestVersionNumber(formattedVersion, isScheduled);
        }
    }
}
