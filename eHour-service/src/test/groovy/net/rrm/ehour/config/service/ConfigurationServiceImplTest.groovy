package net.rrm.ehour.config.service

import net.rrm.ehour.appconfig.EhourHomeUtil
import net.rrm.ehour.config.EhourConfigStub
import net.rrm.ehour.config.LocaleUtil
import net.rrm.ehour.config.TranslationDiscovery
import net.rrm.ehour.domain.AuditType
import net.rrm.ehour.domain.Configuration
import net.rrm.ehour.persistence.config.dao.BinaryConfigurationDao
import net.rrm.ehour.persistence.config.dao.ConfigurationDao
import net.rrm.ehour.persistence.value.ImageLogo
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

import static org.mockito.Matchers.anyObject
import static org.mockito.Mockito.when

/**
 * @author thies (thies@te-con.nl)
 * Date: 1/12/11 9:55 PM
 */
class ConfigurationServiceImplTest {
    ConfigurationServiceImpl configurationService

    @Mock
    ConfigurationDao configDAO;

    @Mock
    BinaryConfigurationDao binaryConfigDao;

    @Before
    void setUp() {
        EhourHomeUtil.setEhourHome("src/test/resources")

        configurationService = new ConfigurationServiceImpl();

        MockitoAnnotations.initMocks this

        configurationService.setConfigDao(configDAO);
        configurationService.setBinConfigDAO(binaryConfigDao);

        def discovery = new TranslationDiscovery()
        discovery.translations = ["en", "nl"]

        configurationService.translationDiscovery = discovery

    }

    @Test
    void shouldReturnDefaultExcelLogo() {
        when(binaryConfigDao.findById("excelHeaderLogo")).thenReturn(null)

        ImageLogo logo = configurationService.getExcelLogo()

        assert logo.imageData.length > 1
    }

    @Test
    void shouldGetConfiguration() {
        def configs = [new Configuration("availableTranslations", "en,nl"),
                       new Configuration("completeDayHours", "8"),
                       new Configuration("localeCurrency", "nlNl"),
                       new Configuration("localeLanguage", "nlNl"),
                       new Configuration("localeCountry", "nlNl"),
                       new Configuration("showTurnOver", "true"),
                       new Configuration("timeZone", "CET"),
                       new Configuration("mailFrom", "ik@jij.net"),
                       new Configuration("mailSmtp", "localhost"),
                       new Configuration("demoMode", "false"),
                       new Configuration("splitAdminRole", "true")]

        when(configDAO.findAll()).thenReturn(configs)

        def configuration = configurationService.getConfiguration()

        assert configuration.isShowTurnover()
        assert configuration.isSplitAdminRole()
    }

    @Test
    void shouldPersistConfiguration() {
        def stub = new EhourConfigStub(completeDayHours: 8, localeFormatting: LocaleUtil.forLanguageTag("en-US"), dontForceLanguage: true,
                showTurnover: true, mailFrom: "re", mailSmtp: "ee", auditType: AuditType.WRITE)
        when(configDAO.persist(anyObject())).thenReturn(null)

        configurationService.persistConfiguration(stub)
    }

    @Test
    void shouldGetDefaultLogo() {
        def logo = configurationService.excelLogo
        assert logo != null
        assert logo.imageData.length > 2
    }
}
