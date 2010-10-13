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

import java.util.Locale;

import net.rrm.ehour.domain.AuditType;


/**
 * Available configuration parameters
 **/

public interface EhourConfig
{
	/**
	 * Get the amount of hours needed before a day is booked as complete
	 * @return
	 */
	public float getCompleteDayHours();
	
	/**
	 * Show turnover to consultants
	 * @return
	 */
	public boolean isShowTurnover();
	
	/**
	 * Get configured timezone
	 * @return
	 */
	public String getTimeZone();

	/**
	 * Get locale based on language and country
	 * @return
	 */
	public Locale getLocale();
	
	/**
	 * Get configured currency
	 * @return
	 */
	public Locale getCurrency();
	
	/**
	 * Get available translations
	 * @return
	 */
	public String[] getAvailableTranslations();
	
	/**
	 * Get from address for e-mail sending
	 * @return
	 */
	public String getMailFrom();
	
	/**
	 * Get SMTP server for e-mail sending
	 * @return
	 */
	public String getMailSmtp();
	
	
	/**
	 * Get the smtp username in case of authenticated smtp
	 * @return
	 */
	public String getSmtpUsername();
	
	/**
	 * Get the smtp password in case of authenticated smtp
	 * @return
	 */
	public String getSmtpPassword();

	/**
	 * Get the smtp port
	 * @return
	 */
	public String getSmtpPort();
	
	/**
	 * In demo mode ?
	 * @return
	 */
	public boolean isInDemoMode();
	
	/**
	 * Don't force language ?
	 * @return
	 */
	public boolean isDontForceLanguage();
	
	/**
	 * Is eHour initialized ?
	 * @return
	 */
	public boolean isInitialized();
	
	/**
	 * Get first day of week
	 * @return 0 = sunday, 1 = monday, etc.
	 */
	public int getFirstDayOfWeek();
	
	/**
	 * Get audit type (NONE, WRITE, ALL)
	 * @return
	 */
	public AuditType getAuditType();
}
