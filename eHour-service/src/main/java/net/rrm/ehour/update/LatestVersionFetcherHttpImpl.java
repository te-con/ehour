package net.rrm.ehour.update;

import com.google.common.base.Optional;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LatestVersionFetcherHttpImpl implements LatestVersionFetcher {

    private static final Logger LOGGER = Logger.getLogger(LatestVersionFetcherHttpImpl.class);

    private String versionUrl;

    @Autowired
    public LatestVersionFetcherHttpImpl(@Value("${versionUrl:http://ehour.nl/latest_version.txt}") String versionUrl) {
        this.versionUrl = versionUrl;
    }

    @Override
    public Optional<String> getLatestVersionNumber() {
        HttpClient client = new DefaultHttpClient();

        try {
            LOGGER.info("Fetching latest version number of eHour release from " + versionUrl);

            HttpGet request = new HttpGet(versionUrl);
            request.setHeader("User-Agent", "eHour update client");

            BasicResponseHandler responseHandler = new BasicResponseHandler();
            String response = client.execute(request, responseHandler);

            return Optional.of(response);
        } catch (Exception e) {
            LOGGER.info("Failed to retrieve latest published eHour version.", e);

        }

        return Optional.absent();
    }
}
