/**
 * Created on Nov 5, 2006
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.config;


/**
 * Available configuration parameters
 **/

public interface EhourConfig
{
	/**
	 * Get the amount of hours needed before a day is booked as complete
	 * @return
	 */
	public int getCompleteDayHours();
	
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
	 * Get configured language
	 * @return
	 */
	public String getLocaleLanguage();
	
	/**
	 * Get configured country
	 * @return
	 */
	public String getLocaleCountry();
	
	/**
	 * Get configured currency
	 * @return
	 */
	public String getCurrency();
	
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
	
}
