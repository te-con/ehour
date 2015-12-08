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

package net.rrm.ehour.persistence.ehourconfig;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigUtil;
import net.rrm.ehour.config.LocaleUtil;
import net.rrm.ehour.config.PmPrivilege;
import net.rrm.ehour.domain.AuditType;
import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.joda.time.DateTimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

import static net.rrm.ehour.config.ConfigurationItem.*;

/**
 * Config from database
 */
@Service("eHourConfig")
public class EhourConfigJdbc extends DatabaseConfiguration implements EhourConfig {
    private static final Logger LOG = Logger.getLogger(EhourConfigJdbc.class);

    private Boolean demoMode;
    private String[] availableTranslations;
    private Boolean initialized;
    private AuditType auditType;

    private String systemConfiguredTimezone = DateTimeZone.getDefault().getID();

    @Autowired
    public EhourConfigJdbc(SessionFactory sessionFactory) {
        super(SessionFactoryUtils.getDataSource(sessionFactory), "CONFIGURATION", "config_key", "config_value");

        setDelimiterParsingDisabled(true);

        LOG.info("Configuration loaded from database: " + toString());
    }

    @Override
    public TimeZone getTzAsTimeZone() {
        return EhourConfigUtil.getTzAsTimeZone(this);
    }

    @Override
    public float getCompleteDayHours() {
        return this.getFloat(COMPLETE_DAY_HOURS.getDbField(), 8);
    }

    @Override
    public boolean isShowTurnover() {
        return this.getBoolean(SHOW_TURNOVER.getDbField(), false);
    }

    @Override
    public String getTimeZone() { return this.getString(TIMEZONE.getDbField(), systemConfiguredTimezone); }

    @Override
    public Locale getCurrency() {
        return LocaleUtil.currencyForLanguageTag(getString(LOCALE_CURRENCY.getDbField(), "nl-NL"));
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
    public String[] getAvailableTranslations() {
        if (availableTranslations == null) {
            availableTranslations = getString(AVAILABLE_TRANSLATIONS.getDbField(), "en,nl").split(",");
        }

        return availableTranslations;
    }

    @Override
    public String getMailFrom() {
        return this.getString(MAIL_FROM.getDbField(), "devnull@devnull.com");
    }

    @Override
    public String getMailSmtp() {
        return this.getString(MAIL_SMTP.getDbField(), "127.0.0.1");
    }

    @Override
    public Locale getFormattingLocale() {
        String formattingLocale = this.getString(LOCALE_COUNTRY.getDbField(), "en_US");

        if (!formattingLocale.contains("-")) {
            return new Locale(formattingLocale, formattingLocale);
        } else {
            return LocaleUtil.forLanguageTag(formattingLocale);
        }
    }

    @Override
    public Locale getLanguageLocale() {
        String formattingLocale = this.getString(LOCALE_LANGUAGE.getDbField(), "en_US");

        if (!formattingLocale.contains("-")) {
            return new Locale(formattingLocale, formattingLocale);
        } else {
            return LocaleUtil.forLanguageTag(formattingLocale);
        }
    }

    @Override
    public boolean isInDemoMode() {
        if (demoMode == null) {
            demoMode = this.getBoolean(DEMO_MODE.getDbField(), false);
        }

        return demoMode;
    }

    @Override
    public boolean isDontForceLanguage() {
        return this.getBoolean(DONT_FORCE_LANGUAGE.getDbField(), false);
    }

    @Override
    public boolean isInitialized() {
        if (initialized == null) {
            initialized = this.getBoolean(INITIALIZED.getDbField(), true);
        }

        return initialized;
    }

    @Override
    public String getSmtpPassword() {
        return this.getString(MAIL_SMTP_PASSWORD.getDbField());
    }

    @Override
    public String getSmtpUsername() {
        return this.getString(MAIL_SMTP_USERNAME.getDbField());
    }

    @Override
    public String getSmtpPort() {
        return this.getString(MAIL_SMTP_PORT.getDbField(), "25");
    }

    @Override
    public int getFirstDayOfWeek() {
        return (int) this.getFloat(FIRST_DAY_OF_WEEK.getDbField(), 1);
    }

    @Override
    public AuditType getAuditType() {
        if (auditType == null) {
            auditType = AuditType.fromString(this.getString(AUDIT_TYPE.getDbField(), "WRITE"));
        }

        return auditType;
    }

    @Override
    public PmPrivilege getPmPrivilege() {
        String pmPrivilege = this.getString(PM_PRIVILEGE.getDbField(), PmPrivilege.FULL.name());
        return PmPrivilege.valueOf(pmPrivilege);
    }

    @Override
    public boolean isSplitAdminRole() {
        return this.getBoolean(SPLIT_ADMIN_ROLE.getDbField(), false);
    }

    @Override
    public boolean isReminderEnabled() {
        return this.getBoolean(REMINDER_ENABLED.getDbField(), false);
    }

    @Override
    public String getReminderTime() {
        return this.getString(REMINDER_TIME.getDbField(), "0 30 17 * * FRI");
    }

    @Override
    public String getReminderSubject() {
        return this.getString(REMINDER_SUBJECT.getDbField(), "");
    }

    @Override
    public String getReminderBody() {
        return this.getString(REMINDER_BODY.getDbField(), "");
    }

    @Override
    public int getReminderMinimalHours() {
        return  (int) this.getFloat(REMINDER_MIN_HOURS.getDbField(), 40);
    }

    @Override
    public String getReminderCC() {
        return this.getString(REMINDER_CC.getDbField(), "");
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("completeDayHours", getCompleteDayHours())
                .append("isShowTurnover", isShowTurnover())
                .append("currencySymbol", getCurrencySymbol())
                .append("currencyCode", getCurrencyCode()).toString();
    }
}
