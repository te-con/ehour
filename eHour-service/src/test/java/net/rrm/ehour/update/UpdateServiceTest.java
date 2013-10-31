package net.rrm.ehour.update;

import com.google.common.base.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateServiceTest {
    @Mock
    private LatestVersionFetcher latestVersionFetcher;

    private UpdateService updateService;

    @Before
    public void setUp() throws Exception {
        updateService = new UpdateService("1.2", latestVersionFetcher);
    }

    @Test
    public void should_fetch_latest_version() throws Exception {
        updateService.scheduledFetchLatestVersion();

        Thread.sleep(500);

        verify(latestVersionFetcher).getLatestVersionNumber("1.2", true);
    }

    @Test
    public void should_say_version_is_latest_when_it_failed_to_retrieve() throws Exception {
        when(latestVersionFetcher.getLatestVersionNumber("1.2", true)).thenReturn(Optional.<String>absent());
        updateService.scheduledFetchLatestVersion();

        Thread.sleep(500);

        assertTrue(updateService.isLatestVersion());
        verify(latestVersionFetcher).getLatestVersionNumber("1.2", true);
    }

    @Test
    public void should_say_version_is_not_latest_when_version_dont_match() throws Exception {
        when(latestVersionFetcher.getLatestVersionNumber("1.2", true)).thenReturn(Optional.of("1.3"));
        updateService.scheduledFetchLatestVersion();

        Thread.sleep(500);

        verify(latestVersionFetcher).getLatestVersionNumber("1.2", true);
        assertFalse(updateService.isLatestVersion());
    }

    @Test
    public void should_say_version_is_not_latest_when_4_digit_version_is_lower_than_2_digit_version() throws Exception {
        updateService = new UpdateService("1.2.2.1", latestVersionFetcher);

        when(latestVersionFetcher.getLatestVersionNumber("1.2.2.1", true)).thenReturn(Optional.of("1.3"));
        updateService.scheduledFetchLatestVersion();

        Thread.sleep(500);

        verify(latestVersionFetcher).getLatestVersionNumber("1.2.2.1", true);
        assertFalse(updateService.isLatestVersion());
    }

    @Test
    public void should_say_version_is_latest_when_versions_match() throws Exception {
        when(latestVersionFetcher.getLatestVersionNumber("1.2", true)).thenReturn(Optional.of("1.2"));
        updateService.scheduledFetchLatestVersion();

        Thread.sleep(500);

        assertTrue(updateService.isLatestVersion());
        verify(latestVersionFetcher).getLatestVersionNumber("1.2", true);
    }

    @Test
    public void should_say_version_is_latest_when_current_is_nightly() throws Exception {
        updateService = new UpdateService("1.3-SNAPSHOT", latestVersionFetcher);
        when(latestVersionFetcher.getLatestVersionNumber("1.3-SNAPSHOT", true)).thenReturn(Optional.of("1.2"));
        updateService.scheduledFetchLatestVersion();

        Thread.sleep(1500);

        assertTrue(updateService.isLatestVersion());
        verify(latestVersionFetcher).getLatestVersionNumber("1.3-SNAPSHOT", true);
    }
}
