/**
 * Created on Nov 5, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
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
