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
import org.joda.time.DateTimeZone;

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
    private String timeZone = DateTimeZone.getDefault().getID();
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
    private boolean splitAdminRole;

    private boolean reminderEnabled;
    private int reminderMinimalHours;
    private String reminderTime;
    private String reminderSubject;
    private String reminderBody;
    private String reminderCC;

    private boolean     ldapSynced;

    @Override
    public TimeZone getTzAsTimeZone() {
        return EhourConfigUtil.getTzAsTimeZone(this);
    }

    @Override
    public String[] getAvailableTranslations() {
        return availableTranslations;
    }

    public void setAvailableTranslations(String[] availableTranslations) {
        this.availableTranslations = availableTranslations;
    }

    @Override
    public float getCompleteDayHours() {
        return completeDayHours;
    }

    public void setCompleteDayHours(float completeDayHours) {
        this.completeDayHours = completeDayHours;
    }

    @Override
    public Locale getCurrency() {
        return currency;
    }

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

    @Override
    public boolean isShowTurnover() {
        return showTurnover;
    }

    public void setShowTurnover(boolean showTurnover) {
        this.showTurnover = showTurnover;
    }

    @Override
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String getMailFrom() {
        return mailFrom;
    }

    @Override
    public String getMailSmtp() {
        return mailSmtp;
    }

    public void setMailFrom(String mailFrom) {
        this.mailFrom = mailFrom;
    }

    public void setMailSmtp(String mailSmtp) {
        this.mailSmtp = mailSmtp;
    }

    @Override
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

    @Override
    public boolean isInDemoMode() {
        return demoMode;
    }

    public void setDemoMode(boolean demoMode) {
        this.demoMode = demoMode;
    }

    @Override
    public boolean isDontForceLanguage() {
        return dontForceLanguage;
    }

    public void setDontForceLanguage(boolean dontForceLanguage) {
        this.dontForceLanguage = dontForceLanguage;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    @Override
    public String getSmtpUsername() {
        return smtpUsername;
    }

    public void setSmtpUsername(String smtpUsername) {
        this.smtpUsername = smtpUsername;
    }

    @Override
    public String getSmtpPassword() {
        return smtpPassword;
    }

    public void setSmtpPassword(String smtpPassword) {
        this.smtpPassword = smtpPassword;
    }

    @Override
    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    @Override
    public int getFirstDayOfWeek() {
        return firstDayOfWeek;
    }

    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
    }

    @Override
    public AuditType getAuditType() {
        return auditType;
    }

    @Override
    public PmPrivilege getPmPrivilege() {
        return pmPrivilege;
    }

    @Override
    public boolean isSplitAdminRole() {
        return splitAdminRole;
    }

    @Override
    public boolean isReminderEnabled() {
        return reminderEnabled;
    }

    public void setReminderEnabled(boolean reminderEnabled) {
        this.reminderEnabled = reminderEnabled;
    }

    @Override
    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    @Override
    public String getReminderSubject() {
        return reminderSubject;
    }

    public void setReminderSubject(String reminderSubject) {
        this.reminderSubject = reminderSubject;
    }

    @Override
    public String getReminderBody() {
        return reminderBody;
    }

    public void setReminderBody(String reminderBody) {
        this.reminderBody = reminderBody;
    }

    @Override
    public int getReminderMinimalHours() {
        return reminderMinimalHours;
    }

    public void setReminderMinimalHours(int reminderMinimalHours) {
        this.reminderMinimalHours = reminderMinimalHours;
    }

    @Override
    public String getReminderCC() {
        return reminderCC;
    }

    public void setReminderCC(String reminderCC) {
        this.reminderCC = reminderCC;
    }

    public void setSplitAdminRole(boolean splitAdminRole) {
        this.splitAdminRole = splitAdminRole;
    }

    public void setPmPrivilege(PmPrivilege pmPrivilege) {
        this.pmPrivilege = pmPrivilege;
    }

    public void setAuditType(AuditType auditType) {
        this.auditType = auditType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isLdapSynced() {
        return ldapSynced;
    }

    public void setLdapSynced(boolean ldapSynced) {
        this.ldapSynced = ldapSynced;
    }
}
