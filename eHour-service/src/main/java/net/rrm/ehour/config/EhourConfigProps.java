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
import java.util.Properties;

import net.rrm.ehour.domain.AuditType;

/**
 * Configuration based on .properties file
 **/

public class EhourConfigProps implements EhourConfig
{
	private	Properties	props;

	public void setPropertiesConfig(Properties props)
	{
		this.props = props;
	}
	
	public float getCompleteDayHours()
	{
		return Float.parseFloat(props.getProperty("completeDayHours"));
	}

	public boolean isShowTurnover()
	{
		return Boolean.valueOf(props.getProperty("showTurnOver"));
	}

	public String getTimeZone()
	{
		return props.getProperty("timezone");
	}

	public Locale getCurrency()
	{
		String split = props.getProperty("currency");
		
		String[] splitted = split.split("_");
		
		return new Locale(splitted[0], splitted[1]);

	}

	public String[] getAvailableTranslations()
	{
		return props.getProperty("localeLanguage").split(",");
	}

	public String getMailFrom()
	{
		return props.getProperty("mailFrom");
	}

	public String getMailSmtp()
	{
		return props.getProperty("mailSmtp");
	}
	
	public Locale getLocale()
	{
		String country = props.getProperty("localeCountry");
		String language = props.getProperty("localeLanguage");
		
		return new Locale(language, country);	
	}

	public boolean isInDemoMode()
	{
		return Boolean.valueOf(props.getProperty("demoMode"));
	}

	public boolean isDontForceLanguage()
	{
		return Boolean.valueOf(props.getProperty("dontForceLanguage"));
	}

	public boolean isInitialized()
	{
		Boolean init = Boolean.valueOf(props.getProperty("initialized"));
		
		return init == null ? true : init;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getSmtpPassword()
	 */
	public String getSmtpPassword() {
		return props.getProperty("smtpPassword");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getSmtpUsername()
	 */
	public String getSmtpUsername() {
		return props.getProperty("smtpUsername");
	}
	
	public String getSmtpPort()
	{
		return props.getProperty("smtpPort");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getFirstDayOfWeek()
	 */
	public int getFirstDayOfWeek()
	{
		return Integer.valueOf(props.getProperty("firstDayOfWeek"));
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.config.EhourConfig#getAuditType()
	 */
	public AuditType getAuditType()
	{
		return AuditType.fromString(props.getProperty("auditType"));
	}
	
}
