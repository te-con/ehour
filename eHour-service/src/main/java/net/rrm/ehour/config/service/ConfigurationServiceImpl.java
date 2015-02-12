/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.config.service;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import net.rrm.ehour.audit.annot.Auditable;
import net.rrm.ehour.audit.annot.NonAuditable;
import net.rrm.ehour.config.*;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.BinaryConfiguration;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.persistence.config.dao.BinaryConfigurationDao;
import net.rrm.ehour.persistence.config.dao.ConfigurationDao;
import net.rrm.ehour.persistence.value.ImageLogo;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Configuration service
 */
@Service("configurationService")
public class ConfigurationServiceImpl implements ConfigurationService {
    private static final String EXCEL_DEFAULT_LOGO = "excel_default_logo.png";

    private static final long serialVersionUID = -8862725896852558151L;

    @Autowired
    private ConfigurationDao configDao;

    @Autowired
    private BinaryConfigurationDao binConfigDAO;

    @Autowired
    private TranslationDiscovery translationDiscovery;

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(ConfigurationServiceImpl.class);

    @Transactional
    @Auditable(actionType = AuditActionType.UPDATE)
    public void persistExcelLogo(ImageLogo logo) {
        persistLogo("excelHeader", logo);
    }

    private void persistLogo(String prefix, ImageLogo logo) {
        BinaryConfiguration logoDomObj = new BinaryConfiguration();
        logoDomObj.setConfigValue(logo.getImageData());
        logoDomObj.setConfigKey(prefix + "Logo");
        binConfigDAO.persist(logoDomObj);

        Configuration logoType = new Configuration(prefix + "LogoType", logo.getImageType());
        Configuration logoWidth = new Configuration(prefix + "LogoWidth", Integer.toString(logo.getWidth()));
        Configuration logoHeight = new Configuration(prefix + "LogoHeight", Integer.toString(logo.getHeight()));

        configDao.persist(logoType);
        configDao.persist(logoWidth);
        configDao.persist(logoHeight);
    }

    @NonAuditable
    @Transactional
    public ImageLogo getExcelLogo() {
        ImageLogo logo = getPersistedLogo("excelHeader");

        if (logo == null) {
            LOGGER.debug("No logo found in database, using default logo.");
            logo = getDefaultExcelLogo();
        }

        return logo;
    }

    private ImageLogo getPersistedLogo(String prefix) {
        BinaryConfiguration logoDomObj = binConfigDAO.findById(prefix + "Logo");
        ImageLogo logo = null;

        if (isLogoNotEmpty(logoDomObj)) {
            logo = new ImageLogo();
            logo.setImageData(logoDomObj.getConfigValue());

            Configuration logoType = configDao.findById(prefix + "LogoType");
            logo.setImageType(logoType.getConfigValue());

            Configuration logoWidth = configDao.findById(prefix + "LogoWidth");
            logo.setWidth(Integer.parseInt(logoWidth.getConfigValue()));

            Configuration logoHeight = configDao.findById(prefix + "LogoHeight");
            logo.setHeight(Integer.parseInt(logoHeight.getConfigValue()));
        }

        return logo;
    }

    private boolean isLogoNotEmpty(BinaryConfiguration logoDomObj) {
        return (logoDomObj != null && logoDomObj.getConfigValue() != null && logoDomObj.getConfigValue().length > 0);
    }

    private ImageLogo getDefaultExcelLogo() {
        byte[] bytes = getDefaultExcelLogoBytes();

        ImageLogo logo = new ImageLogo();
        logo.setImageData(bytes);
        logo.setImageType("png");
        logo.setWidth(499);
        logo.setHeight(120);

        return logo;
    }

    private byte[] getDefaultExcelLogoBytes() {
        byte[] bytes;
        final File file = EhourHomeUtil.getFileInConfDir(EXCEL_DEFAULT_LOGO);

        if (!file.exists()) {
            LOGGER.error("default logo not found");
        }
        try (FileInputStream is = new FileInputStream(file)) {
            bytes = new byte[(int) file.length()];

            int offset = 0;
            int numRead;
            while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
        } catch (IOException e) {
            LOGGER.error("Could not fetch default logo", e);
            bytes = new byte[0];
        }

        return bytes;
    }

    @Transactional
    @NonAuditable
    public EhourConfigStub getConfiguration() {
        List<Configuration> configs = configDao.findAll();
        EhourConfigStub config = new EhourConfigStub();

        List<String> translations = translationDiscovery.getTranslations();

        if (translations != null) {
            config.setAvailableTranslations(translations.toArray(new String[translations.size()]));
        }

        for (Configuration configuration : configs) {
            String key = configuration.getConfigKey();
            String value = configuration.getConfigValue();

            if (key.equalsIgnoreCase(ConfigurationItem.COMPLETE_DAY_HOURS.getDbField())) {
                config.setCompleteDayHours(Float.parseFloat(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.LOCALE_CURRENCY.getDbField())) {
                config.setCurrency(LocaleUtil.forLanguageTag(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.LOCALE_LANGUAGE.getDbField())) {
                config.setLocaleLanguage(LocaleUtil.forLanguageTag(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.LOCALE_COUNTRY.getDbField())) {
                config.setLocaleFormatting(LocaleUtil.forLanguageTag(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.SHOW_TURNOVER.getDbField())) {
                config.setShowTurnover(Boolean.parseBoolean(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.TIMEZONE.getDbField())) {
                config.setTimeZone(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.MAIL_FROM.getDbField())) {
                config.setMailFrom(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.MAIL_SMTP.getDbField())) {
                config.setMailSmtp(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.MAIL_SMTP_USERNAME.getDbField())) {
                config.setSmtpUsername(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.MAIL_SMTP_PASSWORD.getDbField())) {
                config.setSmtpPassword(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.MAIL_SMTP_PORT.getDbField())) {
                config.setSmtpPort(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.DEMO_MODE.getDbField())) {
                config.setDemoMode(Boolean.parseBoolean(value));
            } else if (key.equalsIgnoreCase((ConfigurationItem.INITIALIZED.getDbField()))) {
                config.setInitialized(Boolean.parseBoolean(value));
            } else if (key.equalsIgnoreCase((ConfigurationItem.FIRST_DAY_OF_WEEK.getDbField()))) {
                config.setFirstDayOfWeek((int) (Float.parseFloat(value)));
            } else if (key.equalsIgnoreCase((ConfigurationItem.AUDIT_TYPE.getDbField()))) {
                config.setAuditType(AuditType.fromString(value));
            } else if (key.equalsIgnoreCase((ConfigurationItem.VERSION.getDbField()))) {
                config.setVersion(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.PM_PRIVILEGE.getDbField())) {
                config.setPmPrivilege(PmPrivilege.valueOf(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.SPLIT_ADMIN_ROLE.getDbField())) {
                config.setSplitAdminRole(Boolean.parseBoolean(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.REMINDER_ENABLED.getDbField())) {
                config.setReminderEnabled(Boolean.parseBoolean(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.REMINDER_CC.getDbField())) {
                config.setReminderCC(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.REMINDER_BODY.getDbField())) {
                config.setReminderBody(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.REMINDER_MIN_HOURS.getDbField())) {
                config.setReminderMinimalHours((int) Float.parseFloat(value));
            } else if (key.equalsIgnoreCase(ConfigurationItem.REMINDER_SUBJECT.getDbField())) {
                config.setReminderSubject(value);
            } else if (key.equalsIgnoreCase(ConfigurationItem.REMINDER_TIME.getDbField())) {
                config.setReminderTime(value);
            }
        }

        return config;
    }

    @Override
    public List<Configuration> findAllConfiguration() {
        return configDao.findAll();
    }

    @Transactional
    @Auditable(actionType = AuditActionType.UPDATE)
    public void persistConfiguration(EhourConfig config) {
        LOGGER.debug("Persisting config");
        persistConfig(ConfigurationItem.LOCALE_CURRENCY.getDbField(), LocaleUtil.toLanguageTag((config.getCurrency())));

        if (config.getCompleteDayHours() != 0) {
            persistConfig(ConfigurationItem.COMPLETE_DAY_HOURS.getDbField(), config.getCompleteDayHours());
        }

        persistConfig(ConfigurationItem.LOCALE_COUNTRY.getDbField(), LocaleUtil.toLanguageTag(config.getFormattingLocale()));
        persistConfig(ConfigurationItem.LOCALE_LANGUAGE.getDbField(), LocaleUtil.toLanguageTag(config.getLanguageLocale()));
        persistConfig(ConfigurationItem.DONT_FORCE_LANGUAGE.getDbField(), config.isDontForceLanguage());
        persistConfig(ConfigurationItem.SHOW_TURNOVER.getDbField(), config.isShowTurnover());
        persistConfig(ConfigurationItem.MAIL_FROM.getDbField(), config.getMailFrom());
        persistConfig(ConfigurationItem.MAIL_SMTP.getDbField(), config.getMailSmtp());
        persistConfig(ConfigurationItem.MAIL_SMTP_USERNAME.getDbField(), config.getSmtpUsername());
        persistConfig(ConfigurationItem.MAIL_SMTP_PASSWORD.getDbField(), config.getSmtpPassword());
        persistConfig(ConfigurationItem.MAIL_SMTP_PORT.getDbField(), config.getSmtpPort());
        persistConfig(ConfigurationItem.INITIALIZED.getDbField(), config.isInitialized());
        persistConfig(ConfigurationItem.FIRST_DAY_OF_WEEK.getDbField(), config.getFirstDayOfWeek());
        persistConfig(ConfigurationItem.AUDIT_TYPE.getDbField(), getAuditType(config).getValue());

        persistConfig(ConfigurationItem.PM_PRIVILEGE.getDbField(), getPmPrivilege(config).name());

        persistConfig(ConfigurationItem.SPLIT_ADMIN_ROLE.getDbField(), config.isSplitAdminRole());

        persistConfig(ConfigurationItem.REMINDER_BODY.getDbField(), config.getReminderBody());
        persistConfig(ConfigurationItem.REMINDER_CC.getDbField(), config.getReminderCC());
        persistConfig(ConfigurationItem.REMINDER_ENABLED.getDbField(), config.isReminderEnabled());
        persistConfig(ConfigurationItem.REMINDER_MIN_HOURS.getDbField(), config.getReminderMinimalHours());
        persistConfig(ConfigurationItem.REMINDER_SUBJECT.getDbField(), config.getReminderSubject());
        persistConfig(ConfigurationItem.REMINDER_TIME.getDbField(), config.getReminderTime());

        persistConfig(ConfigurationItem.TIMEZONE.getDbField(), config.getTimeZone());
    }

    private AuditType getAuditType(EhourConfig config) {
        if (config.getAuditType() == null) {
            return AuditType.WRITE;
        } else {
            return config.getAuditType();
        }
    }

    private PmPrivilege getPmPrivilege(EhourConfig config) {
        if (config.getPmPrivilege() == null) {
            return PmPrivilege.FULL;
        } else {
            return config.getPmPrivilege();
        }
    }

    private void persistConfig(String key, String value) {
        Configuration config = new Configuration();
        config.setConfigKey(key);
        config.setConfigValue(value == null ? "" : value);

        configDao.persist(config);
    }

    private void persistConfig(String key, boolean value) {
        persistConfig(key, Boolean.toString(value));
    }

    private void persistConfig(String key, float value) {
        persistConfig(key, Float.toString(value));
    }

    public void setConfigDao(ConfigurationDao configDao) {
        this.configDao = configDao;
    }

    public void setBinConfigDAO(BinaryConfigurationDao binaryConfigDAO) {
        this.binConfigDAO = binaryConfigDAO;
    }

    public void setTranslationDiscovery(TranslationDiscovery translationDiscovery) {
        this.translationDiscovery = translationDiscovery;
    }
}
