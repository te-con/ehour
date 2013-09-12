package net.rrm.ehour.update;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LatestVersionFetcherHttpImpl implements LatestVersionFetcher {

    private String versionUrl;

    @Autowired
    public LatestVersionFetcherHttpImpl(@Value("${versionUrl:http://ehour.nl/version.json}") String versionUrl) {
        this.versionUrl = versionUrl;
    }

    @Override
    public Optional<String> getLatestVersionNumber() {
        return Optional.absent();
    }
}
