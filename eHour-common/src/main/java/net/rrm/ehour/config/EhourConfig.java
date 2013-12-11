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
     *
     * @return
     */
    float getCompleteDayHours();

    /**
     * Show turnover to consultants
     *
     * @return
     */
    boolean isShowTurnover();

    /**
     * Get configured timezone
     *
     * @return
     */
    String getTimeZone();


    TimeZone getTzAsTimeZone();

    /**
     * Get locale based on language and country
     *
     * @return
     */
    Locale getFormattingLocale();

    /**
     * Get locale for the language
     * @return
     */
    Locale getLanguageLocale();

    /**
     * Get configured currency
     *
     * @return
     */
    Locale getCurrency();

    String getCurrencySymbol();

    String getCurrencyCode();

    /**
     * Get available translations
     *
     * @return
     */
    String[] getAvailableTranslations();

    /**
     * Get from address for e-mail sending
     *
     * @return
     */
    String getMailFrom();

    /**
     * Get SMTP server for e-mail sending
     *
     * @return
     */
    String getMailSmtp();


    /**
     * Get the smtp username in case of authenticated smtp
     *
     * @return
     */
    String getSmtpUsername();

    /**
     * Get the smtp password in case of authenticated smtp
     *
     * @return
     */
    String getSmtpPassword();

    /**
     * Get the smtp port
     *
     * @return
     */
    String getSmtpPort();

    /**
     * In demo mode ?
     *
     * @return
     */
    boolean isInDemoMode();

    /**
     * Don't force language ?
     *
     * @return
     */
    boolean isDontForceLanguage();

    /**
     * Is eHour initialized ?
     *
     * @return
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
     *
     * @return
     */
    AuditType getAuditType();


    /**
     * Get rights a PM has
     * @return
     */
    PmProjectMaintenance getPmProjectMaintenance();
}
