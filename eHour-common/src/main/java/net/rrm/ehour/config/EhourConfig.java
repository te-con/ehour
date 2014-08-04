/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General  License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General  License for more details.
 * 
 * You should have received a copy of the GNU General  License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.config;

import net.rrm.ehour.domain.AuditType;

import java.util.Locale;
import java.util.TimeZone;


/**
 * Available configuration parameters
 */

public interface EhourConfig {
    /**
     * Get the amount of hours needed before a day is booked as complete
     */
    float getCompleteDayHours();

    /**
     * Show turnover to users
     */
    boolean isShowTurnover();

    /**
     * Get configured timezone
     */
    String getTimeZone();


    TimeZone getTzAsTimeZone();

    /**
     * Get locale based on language and country
     */
    Locale getFormattingLocale();

    /**
     * Get locale for the language
     */
    Locale getLanguageLocale();

    /**
     * Get configured currency
     */
    Locale getCurrency();

    String getCurrencySymbol();

    String getCurrencyCode();

    /**
     * Get available translations
     */
    String[] getAvailableTranslations();

    /**
     * Get from address for e-mail sending
     */
    String getMailFrom();

    /**
     * Get SMTP server for e-mail sending
     */
    String getMailSmtp();


    /**
     * Get the smtp username in case of authenticated smtp
     */
    String getSmtpUsername();

    /**
     * Get the smtp password in case of authenticated smtp
     */
    String getSmtpPassword();

    /**
     * Get the smtp port
     */
    String getSmtpPort();

    /**
     * In demo mode ?
     */
    boolean isInDemoMode();

    /**
     * Don't force language ?
     */
    boolean isDontForceLanguage();

    /**
     * Is eHour initialized ?
     */
    boolean isInitialized();

    /**
     * Get first day of week
     *
     * @return 1 = sunday, 2 = monday, Calendar.<WEEKDAY />etc.
     */
    int getFirstDayOfWeek();

    /**
     * Get audit type (NONE, WRITE, ALL)
     */
    AuditType getAuditType();

    /**
     * Get rights a PM has
     */
    PmPrivilege getPmPrivilege();

    /**
     * Split admin in manager & admin?
     */
    boolean isSplitAdminRole();

    /**
     * Is sending reminders enabled?
     */
    boolean isReminderEnabled();

    /**
     * How many hours does a user need to have a tracked
     */
    int getReminderMinimalHours();

    /**
     * Reminder mails are cc'ed to (comma separated list)
     */
    String getReminderCC();

    /**
     * When the reminders should be sent, in cronjob format
     */
    String getReminderTime();

    /**
     * Subject of the reminder email
     */
    String getReminderSubject();

    /**
     * The reminder mail body
     */
    String getReminderBody();
}
