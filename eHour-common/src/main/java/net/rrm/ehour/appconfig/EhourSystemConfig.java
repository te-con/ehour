package net.rrm.ehour.appconfig;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Ehour System Config represents the config found in ehour.properties, a more low-level config for eHour
 */
@Service
public class EhourSystemConfig {
    @Value("${ehour.enableBookWholeWeek:true}")
    private boolean bookWholeWeekEnabled = true;

    @Value("${ehour.enableMail:true}")
    private boolean enableMail = true;

    @Value("${ehour.configurationType}")
    private String configurationType;

    @Value("${EHOUR_HOME}")
    private String eHourHome;

    @Value("${ehour.translations}")
    private String translationsDir;

    @Value("${ehour.disableAuth:false}")
    private boolean disableAuth = false;

    public EhourSystemConfig() {
    }

    public EhourSystemConfig(String eHourHome, String translationsDir) {
        this.eHourHome = eHourHome;
        this.translationsDir = translationsDir;
    }

    @PostConstruct
    public void sanitizeConfig() {

    }

    public boolean isDisableAuth() {
        return disableAuth;
    }

    public boolean isBookWholeWeekEnabled() {
        return bookWholeWeekEnabled;
    }

    public boolean isEnableMail() {
        return enableMail;
    }

    public String getConfigurationType() {
        return configurationType;
    }

    public String getEhourHome() {
        return eHourHome;
    }

    public String getTranslationsDir() {
        return translationsDir;
    }
}
