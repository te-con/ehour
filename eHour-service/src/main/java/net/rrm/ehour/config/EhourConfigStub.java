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

package net.rrm.ehour.config;

import net.rrm.ehour.domain.AuditType;

import java.io.Serializable;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Stub for config
 */

public class EhourConfigStub implements EhourConfig, Serializable {
    private static final long serialVersionUID = 3017492603595731493L;
    private String[] availableTranslations;
    private float completeDayHours;
    private Locale currency = Locale.US;
    private Locale localeLanguage = Locale.US;
    private Locale localeFormatting = Locale.US;
    private String timeZone;
    private boolean showTurnover;
    private String mailFrom;
    private String mailSmtp;
    private boolean demoMode = false;
    private boolean dontForceLanguage;
    private boolean initialized;
    private String smtpUsername;
    private String smtpPassword;
    private String smtpPort = "25";
    private int firstDayOfWeek = 1;
    private AuditType auditType;
    private String version;
    private PmPrivilege pmPrivilege;

    public TimeZone getTzAsTimeZone() {
        return EhourConfigUtil.getTzAsTimeZone(this);
    }


    /**
     * @return the availableTranslations
     */
    public String[] getAvailableTranslations() {
        return availableTranslations;
    }

    /**
     * @param availableTranslations the availableTranslations to set
     */
    public void setAvailableTranslations(String[] availableTranslations) {
        this.availableTranslations = availableTranslations;
    }

    /**
     * @return the completeDayHours
     */
    public float getCompleteDayHours() {
        return completeDayHours;
    }

    /**
     * @param completeDayHours the completeDayHours to set
     */
    public void setCompleteDayHours(float completeDayHours) {
        this.completeDayHours = completeDayHours;
    }

    /**
     * @return the currency
     */
    public Locale getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(Locale currency) {
        this.currency = currency;
    }

    @Override
    public String getCurrencySymbol() {
        return Currency.getInstance(getCurrency()).getSymbol(getCurrency());
    }

    @Override
    public String getCurrencyCode() {
        return Currency.getInstance(getCurrency()).getCurrencyCode();
    }

    /**
     * @return the showTurnover
     */
    public boolean isShowTurnover() {
        return showTurnover;
    }

    /**
     * @param showTurnover the showTurnover to set
     */
    public void setShowTurnover(boolean showTurnover) {
        this.showTurnover = showTurnover;
    }

    /**
     * @return the timeZone
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * @param timeZone the timeZone to set
     */
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailSmtp() {
        return mailSmtp;
    }

    /**
     * @param mailFrom the mailFrom to set
     */
    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    /**
     * @param mailSmtp the mailSmtp to set
     */
    public void setMailSmtp(String mailSmtp) {
        this.mailSmtp = mailSmtp;
    }

    public Locale getFormattingLocale() {
        return localeFormatting;
    }

    @Override
    public Locale getLanguageLocale() {
        return localeLanguage;
    }

    public void setLocaleLanguage(Locale localeLanguage) {
        this.localeLanguage = localeLanguage;
    }

    public void setLocaleFormatting(Locale localeFormatting) {
        this.localeFormatting = localeFormatting;
    }

    public boolean isInDemoMode() {
        return demoMode;
    }

    /**
     * @param demoMode the demoMode to set
     */
    public void setDemoMode(boolean demoMode) {
        this.demoMode = demoMode;
    }


    public boolean isDontForceLanguage() {
        return dontForceLanguage;
    }

    /**
     * @param dontForceLanguage the dontForceLanguage to set
     */
    public void setDontForceLanguage(boolean dontForceLanguage) {
        this.dontForceLanguage = dontForceLanguage;
    }

    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
     * @return the firstDayOfWeek
     */
    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    /**
     * @param firstDayOfWeek the firstDayOfWeek to set
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    /**
     * @return the auditType
     */
    public AuditType getAuditType() {
        return auditType;
    }

    @Override
    public PmPrivilege getPmPrivilege() {
        return pmPrivilege;
    }

    public void setPmPrivilege(PmPrivilege pmPrivilege) {
        this.pmPrivilege = pmPrivilege;
    }

    /**
     * @param auditType the auditType to set
     */
    public void setAuditType(AuditType auditType) {
        this.auditType = auditType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
