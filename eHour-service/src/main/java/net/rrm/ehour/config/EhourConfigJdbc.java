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
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Config from database
 **/
@Service("eHourConfig")
public class EhourConfigJdbc extends DatabaseConfiguration implements EhourConfig
{
	private	static final Logger LOG = Logger.getLogger(EhourConfigJdbc.class);
	
	private Boolean 	demoMode;
	private String[]	availableTranslations;
	private Boolean		initialized;
	private	AuditType	auditType;
	
	@Autowired 
	public EhourConfigJdbc(DataSource datasource)
	{
		super(datasource, "CONFIGURATION", "config_key", "config_value");
	
		LOG.info("Configuration loaded from database");
	}
	
	public TimeZone getTzAsTimeZone()
	{
		return EhourConfigUtil.getTzAsTimeZone(this);
	}
	
	public float getCompleteDayHours()
	{
		return this.getFloat("completeDayHours", 8);
	}

	public boolean isShowTurnover()
	{
		return this.getBoolean("showTurnOver", true);
	}
	
	public String getTimeZone()
	{
		return this.getString("timezone");
	}

	public Locale getCurrency()
	{
		String split = this.getString("localeCurrency", "nl_NL");
		String[] splitted = split.split("_");
		
		return new Locale(splitted[0], splitted[1]);
	}

	public String[] getAvailableTranslations()
	{
		if (availableTranslations == null)
		{
			availableTranslations = this.getString("availableTranslations", "en,nl").split(",");
		}
		
		return availableTranslations;
	}

	public String getMailFrom()
	{
		return this.getString("mailFrom", "devnull@devnull.com");
	}

	public String getMailSmtp()
	{
		return this.getString("mailSmtp", "127.0.0.1");
	}

	public Locale getLocale()
	{
		String country = this.getString("localeCountry", "US");
		String language = this.getString("localeLanguage", "en");
		
		return new Locale(language, country);
	}

	public boolean isInDemoMode()
	{
		if (demoMode == null)
		{
			demoMode = this.getBoolean("demoMode", false);
		}
		
		return demoMode.booleanValue();
	}

	public boolean isDontForceLanguage()
	{
		return this.getBoolean("dontForceLanguage", false);
	}

	public boolean isInitialized()
	{
		if (initialized == null)
		{
			initialized = this.getBoolean("initialized", true);
		}
		
		return initialized;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.config.EhourConfig#getSmtpPassword()
	 */
	public String getSmtpPassword() {
		return this.getString("smtpUsername");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.config.EhourConfig#getSmtpUsername()
	 */
	public String getSmtpUsername() {
		return this.getString("smtpPassword");
	}

	/**
	 * 
	 */
	public String getSmtpPort() {
		return this.getString("smtpPort", "25");
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.config.EhourConfig#getFirstDayOfWeek()
	 */
	public int getFirstDayOfWeek()
	{
		return (int)this.getFloat("firstDayOfWeek", 1);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.persistence.persistence.config.EhourConfig#getAuditType()
	 */
	public AuditType getAuditType()
	{
		if (auditType == null)
		{
			auditType = AuditType.fromString(this.getString("auditType", "WRITE"));
		}
		
		return auditType;
	}
}
