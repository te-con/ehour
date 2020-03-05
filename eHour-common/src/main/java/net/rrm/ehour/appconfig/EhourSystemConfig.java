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

    @Value("${ehour.enableOAuth:false}")
    private boolean enableOAuth;

    @Value("${ehour.oauth2.callbackURI}")
    private String oauthCallbackURI;

    @Value("${ehour.oauth2.hostURL}")
    private String oauthHostURL;

    @Value("${ehour.oauth2.name}")
    private String oauthName;

    @Value("${ehour.oauth2.userAuthorizationUri}")
    private String oauthAuthURL;

    @Value("${ehour.oauth2.accessTokenUri}")
    private String oauthTokenURI;

    @Value("${ehour.oauth2.clientID}")
    private String oauthClientID;

    @Value("${ehour.oauth2.clientSecret}")
    private String oauthClientSecuret;

    @Value("${ehour.oauth2.scope}")
    private String oauthScope;

    @Value("${ehour.disableIndividualReport}")
    private boolean disableIndividualReport = false;

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

    public boolean isEnableOAuth() {
        return enableOAuth;
    }

    public String getOauthCallbackURI() {
        return oauthCallbackURI;
    }

    public String getOauthHostURL() {
        return oauthHostURL;
    }

    public String getOauthName() {
        return oauthName;
    }

    public String getOauthAuthURL() {
        return oauthAuthURL;
    }

    public String getOauthClientID() {
        return oauthClientID;
    }

    public String getOauthClientSecuret() {
        return oauthClientSecuret;
    }

    public String getOauthScope() {
        return oauthScope;
    }

    public String getOauthTokenURI() {
        return oauthTokenURI;
    }

    public boolean isDisableIndividualReport() {
        return disableIndividualReport;
    }

}
