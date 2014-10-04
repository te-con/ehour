package net.rrm.ehour.config.service;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import net.rrm.ehour.appconfig.EhourSystemConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.LocaleUtil;
import net.rrm.ehour.config.TranslationDiscovery;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.config.dao.BinaryConfigurationDao;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import net.rrm.ehour.persistence.value.ImageLogo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author thies (thies@te-con.nl)
 *         Date: 1/12/11 9:55 PM
 */
public class ConfigurationServiceImplTest {
    private ConfigurationServiceImpl configurationService;
    @Mock
    private ConfigurationDao configDAO;

    @Mock
    private BinaryConfigurationDao binaryConfigDao;

    @Before
    public void setUp() {
        EhourHomeUtil.setEhourHome("src/test/resources");

        configurationService = new ConfigurationServiceImpl();

        MockitoAnnotations.initMocks(this);

        configurationService.setConfigDao(configDAO);
        configurationService.setBinConfigDAO(binaryConfigDao);

        TranslationDiscovery discovery = new TranslationDiscovery(new EhourSystemConfig());
        discovery.setTranslations(Arrays.asList("en", "nl"));

        configurationService.setTranslationDiscovery(discovery);
    }

    @Test
    public void shouldReturnDefaultExcelLogo() {
        when(binaryConfigDao.findById("excelHeaderLogo")).thenReturn(null);

        ImageLogo logo = configurationService.getExcelLogo();

        assertTrue(logo.getImageData().length > 1);
    }

    @Test
    public void shouldGetConfiguration() {
        List<Configuration> configs = Arrays.asList(new Configuration("availableTranslations", "en,nl"),
                new Configuration("completeDayHours", "8"),
                new Configuration("localeCurrency", "nlNl"),
                new Configuration("localeLanguage", "nlNl"),
                new Configuration("localeCountry", "nlNl"),
                new Configuration("showTurnOver", "true"),
                new Configuration("timeZone", "CET"),
                new Configuration("mailFrom", "ik@jij.net"),
                new Configuration("mailSmtp", "localhost"),
                new Configuration("demoMode", "false"),
                new Configuration("splitAdminRole", "true"));

        when(configDAO.findAll()).thenReturn(configs);

        EhourConfigStub configuration = configurationService.getConfiguration();

        assertTrue(configuration.isShowTurnover());
        assertTrue(configuration.isSplitAdminRole());
    }

    @Test
    public void shouldPersistConfiguration() {
        EhourConfigStub stub = new EhourConfigStub();

        stub.setCompleteDayHours(8);

        stub.setLocaleFormatting(LocaleUtil.forLanguageTag("en-US"));
        stub.setDontForceLanguage(true);
        stub.setShowTurnover(true);
        stub.setMailFrom("re");
        stub.setMailSmtp("ee");
        stub.setAuditType(AuditType.WRITE);

        configurationService.persistConfiguration(stub);
        ArgumentCaptor<Configuration> configCapture = ArgumentCaptor.forClass(Configuration.class);

        verify(configDAO, times(23)).persist(configCapture.capture());

        List<Configuration> configurations = configCapture.getAllValues();

        assertThat(new Configuration("mailSmtp", "ee"), isIn(configurations));
    }

    @Test
    public void shouldGetDefaultLogo() {
        ImageLogo logo = configurationService.getExcelLogo();
        assertNotNull(logo);
        assertTrue(logo.getImageData().length > 2);
    }
}
