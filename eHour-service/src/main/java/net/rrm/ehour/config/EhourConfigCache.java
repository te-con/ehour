package net.rrm.ehour.config;

import net.rrm.ehour.domain.AuditType;

import java.io.Serializable;
import java.util.Locale;
import java.util.TimeZone;

public class EhourConfigCache implements EhourConfig, Serializable {

    private final EhourConfigStub cache;
    private final TimeZone timezone;
    private final String currencySymbol;
    private final String currencyCode;

    public EhourConfigCache(EhourConfig config) {

        cache = new EhourConfigStub();

        cache.setCompleteDayHours(config.getCompleteDayHours());
        cache.setShowTurnover(config.isShowTurnover());
        cache.setTimeZone(config.getTimeZone());
        timezone = cache.getTzAsTimeZone();

        cache.setLocaleFormatting(config.getFormattingLocale());
        cache.setLocaleLanguage(config.getLanguageLocale());
        cache.setCurrency(config.getCurrency());
        currencySymbol = config.getCurrencySymbol();
        currencyCode = config.getCurrencyCode();

        cache.setAvailableTranslations(config.getAvailableTranslations());
        cache.setMailFrom(config.getMailFrom());
        cache.setMailSmtp(config.getMailSmtp());
        cache.setSmtpPort(config.getSmtpPort());
        cache.setSmtpUsername(config.getSmtpUsername());
        cache.setSmtpPassword(config.getSmtpPassword());

        cache.setDemoMode(config.isInDemoMode());
        cache.setDontForceLanguage(config.isDontForceLanguage());
        cache.setInitialized(config.isInitialized());
        cache.setFirstDayOfWeek(config.getFirstDayOfWeek());
        cache.setAuditType(config.getAuditType());

        cache.setPmPrivilege(config.getPmPrivilege());

        cache.setSplitAdminRole(config.isSplitAdminRole());

        cache.setReminderBody(config.getReminderBody());
        cache.setReminderCC(config.getReminderCC());
        cache.setReminderEnabled(config.isReminderEnabled());
        cache.setReminderMinimalHours(config.getReminderMinimalHours());
        cache.setReminderSubject(config.getReminderSubject());
        cache.setReminderTime(config.getReminderTime());
    }

    @Override
    public float getCompleteDayHours() {
        return cache.getCompleteDayHours();
    }

    @Override
    public boolean isShowTurnover() {
        return cache.isShowTurnover();
    }

    @Override
    public String getTimeZone() {
        return cache.getTimeZone();
    }

    @Override
    public TimeZone getTzAsTimeZone() {
        return timezone;
    }

    @Override
    public Locale getFormattingLocale() {
        return cache.getFormattingLocale();
    }

    @Override
    public Locale getLanguageLocale() {
        return cache.getLanguageLocale();
    }

    @Override
    public Locale getCurrency() {
        return cache.getCurrency();
    }

    @Override
    public String getCurrencySymbol() {
        return currencySymbol;
    }

    @Override
    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public String[] getAvailableTranslations() {
        return cache.getAvailableTranslations();
    }

    @Override
    public String getMailFrom() {
        return cache.getMailFrom();
    }

    @Override
    public String getMailSmtp() {
        return cache.getMailSmtp();
    }

    @Override
    public String getSmtpUsername() {
        return cache.getSmtpUsername();
    }

    @Override
    public String getSmtpPassword() {
        return cache.getSmtpPassword();
    }

    @Override
    public String getSmtpPort() {
        return cache.getSmtpPort();
    }

    @Override
    public boolean isInDemoMode() {
        return cache.isInDemoMode();
    }

    @Override
    public boolean isDontForceLanguage() {
        return cache.isDontForceLanguage();
    }

    @Override
    public boolean isInitialized() {
        return cache.isInitialized();
    }

    @Override
    public int getFirstDayOfWeek() {
        return cache.getFirstDayOfWeek();
    }

    @Override
    public AuditType getAuditType() {
        return cache.getAuditType();
    }

    @Override
    public PmPrivilege getPmPrivilege() {
        return cache.getPmPrivilege();
    }

    @Override
    public boolean isSplitAdminRole() {
        return cache.isSplitAdminRole();
    }

    @Override
    public boolean isReminderEnabled() {
        return cache.isReminderEnabled();
    }

    @Override
    public String getReminderTime() {
        return cache.getReminderTime();
    }

    @Override
    public String getReminderSubject() {
        return cache.getReminderSubject();
    }

    @Override
    public String getReminderBody() {
        return cache.getReminderBody();
    }

    @Override
    public int getReminderMinimalHours() {
        return cache.getReminderMinimalHours();
    }

    @Override
    public String getReminderCC() {
        return cache.getReminderCC();
    }
}
