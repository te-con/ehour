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
import org.apache.commons.configuration.DatabaseConfiguration;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;

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

    @Autowired
    public EhourConfigJdbc(DataSource datasource) {
        super(datasource, "CONFIGURATION", "config_key", "config_value");

        LOG.info("Configuration loaded from database: " + toString());
    }

    public TimeZone getTzAsTimeZone() {
        return EhourConfigUtil.getTzAsTimeZone(this);
    }

    public float getCompleteDayHours() {
        return this.getFloat("completeDayHours", 8);
    }

    public boolean isShowTurnover() {
        return this.getBoolean("showTurnOver", false);
    }

    public String getTimeZone() {
        return this.getString("timezone");
    }

    @Override
    public Locale getCurrency() {
        return LocaleUtil.currencyForLanguageTag(getString("localeCurrency", "nl-NL"));
    }

    @Override
    public String getCurrencySymbol() {
        return Currency.getInstance(getCurrency()).getSymbol(getCurrency());
    }

    @Override
    public String getCurrencyCode() {
        return Currency.getInstance(getCurrency()).getCurrencyCode();
    }

    public String[] getAvailableTranslations() {
        if (availableTranslations == null) {
            availableTranslations = getString("availableTranslations", "en,nl").split(",");
        }

        return availableTranslations;
    }

    public String getMailFrom() {
        return this.getString("mailFrom", "devnull@devnull.com");
    }

    public String getMailSmtp() {
        return this.getString("mailSmtp", "127.0.0.1");
    }

    public Locale getFormattingLocale() {
        String formattingLocale = this.getString("localeCountry", "en_US");

        if (!formattingLocale.contains("-")) {
            return new Locale(formattingLocale, formattingLocale);
        } else {
            return LocaleUtil.forLanguageTag(formattingLocale);
        }
    }

    @Override
    public Locale getLanguageLocale() {
        String formattingLocale = this.getString("localeLanguage", "en_US");

        if (!formattingLocale.contains("-")) {
            return new Locale(formattingLocale, formattingLocale);
        } else {
            return LocaleUtil.forLanguageTag(formattingLocale);
        }
    }

    public boolean isInDemoMode() {
        if (demoMode == null) {
            demoMode = this.getBoolean("demoMode", false);
        }

        return demoMode;
    }

    public boolean isDontForceLanguage() {
        return this.getBoolean("dontForceLanguage", false);
    }

    public boolean isInitialized() {
        if (initialized == null) {
            initialized = this.getBoolean("initialized", true);
        }

        return initialized;
    }

    public String getSmtpPassword() {
        return this.getString("smtpUsername");
    }

    public String getSmtpUsername() {
        return this.getString("smtpPassword");
    }

    public String getSmtpPort() {
        return this.getString("smtpPort", "25");
    }

    public int getFirstDayOfWeek() {
        return (int) this.getFloat("firstDayOfWeek", 1);
    }

    public AuditType getAuditType() {
        if (auditType == null) {
            auditType = AuditType.fromString(this.getString("auditType", "WRITE"));
        }

        return auditType;
    }

    @Override
    public PmPrivilege getPmPrivilege() {
        String pmPrivilege = this.getString(ConfigurationItem.PM_PRIVILEGE.getDbField(), PmPrivilege.FULL.name());
        return PmPrivilege.valueOf(pmPrivilege);
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
