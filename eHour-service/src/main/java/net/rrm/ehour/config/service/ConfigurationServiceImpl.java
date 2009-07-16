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

package net.rrm.ehour.config.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import net.rrm.ehour.audit.Auditable;
import net.rrm.ehour.audit.NonAuditable;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.dao.BinaryConfigurationDAO;
import net.rrm.ehour.config.dao.ConfigurationDAO;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.BinaryConfiguration;
import net.rrm.ehour.domain.Configuration;
import net.rrm.ehour.value.ImageLogo;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

/**
 * Configuration service
 **/

public class ConfigurationServiceImpl implements ConfigurationService
{
	private ConfigurationDAO	configDAO;
	private BinaryConfigurationDAO binConfigDAO;
	private	Logger				logger = Logger.getLogger(this.getClass());


	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigurationService#persistExcelLogo(net.rrm.ehour.value.ImageLogo)
	 */
	@Transactional
	@Auditable(actionType=AuditActionType.UPDATE)
	public void persistExcelLogo(ImageLogo logo)
	{
		persistLogo("excelHeader", logo);
	}
	
	private void persistLogo(String prefix, ImageLogo logo)
	{
		BinaryConfiguration logoDomObj = new BinaryConfiguration();
		logoDomObj.setConfigValue(logo.getImageData());
		logoDomObj.setConfigKey(prefix + "Logo");
		binConfigDAO.persist(logoDomObj);
		
		Configuration logoType = new Configuration(prefix + "LogoType", logo.getImageType());
		Configuration logoWidth = new Configuration(prefix + "LogoWidth", Integer.toString(logo.getWidth()));
		Configuration logoHeight = new Configuration(prefix + "LogoHeight", Integer.toString(logo.getHeight()));
		
		configDAO.persist(logoType);
		configDAO.persist(logoWidth);
		configDAO.persist(logoHeight);
	}

	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigurationService#getLogo()
	 */
	@NonAuditable
	public ImageLogo getExcelLogo()
	{
		ImageLogo logo = getPersistedLogo("excelHeader");

		if (logo == null)
		{
			logo = getDefaultExcelLogo();
		}
		
		return logo;
	}
	
	private ImageLogo getPersistedLogo(String prefix)
	{
		BinaryConfiguration logoDomObj = binConfigDAO.findById(prefix + "Logo");
		ImageLogo logo = null;
		
		if (isLogoNotEmpty(logoDomObj))
		{
			logo = new ImageLogo();
			logo.setImageData(logoDomObj.getConfigValue());
			
			Configuration logoType = configDAO.findById(prefix + "LogoType");
			logo.setImageType(logoType.getConfigValue());

			Configuration logoWidth = configDAO.findById(prefix + "LogoWidth");
			logo.setWidth(Integer.parseInt(logoWidth.getConfigValue()));

			Configuration logoHeight = configDAO.findById(prefix + "LogoHeight");
			logo.setHeight(Integer.parseInt(logoHeight.getConfigValue()));
		}

		return logo;
	}

	private boolean isLogoNotEmpty(BinaryConfiguration logoDomObj)
	{
		return (logoDomObj != null && logoDomObj.getConfigValue() != null && logoDomObj.getConfigValue().length > 0);
	}

	private ImageLogo getDefaultExcelLogo()
	{
		byte[] bytes = getDefaultExcelLogoBytes();

		ImageLogo logo = new ImageLogo();
		logo.setImageData(bytes);
		logo.setImageType("png");
		logo.setWidth(499);
		logo.setHeight(120);
		
        return logo;
	}

	/**
	 * @return
	 */
	private byte[] getDefaultExcelLogoBytes()
	{
		URL url = Thread.currentThread().getContextClassLoader().getResource("excel_default_logo.png");
		File file = new File(url.getPath());
		
		InputStream is;
		
		byte[] bytes;
		
		try
		{
			is = url.openStream();

			bytes = new byte[(int) file.length()];

			int offset = 0;
			int numRead = 0;
			while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
			{
				offset += numRead;
			}
		} catch (IOException e)
		{
			logger.error("Could not fetch default logo", e);
			bytes = new byte[1];
		}
		return bytes;
	}
	
	
	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigService#getConfiguration()
	 */
	@Transactional
	@NonAuditable
	public EhourConfigStub getConfiguration()
	{
		List<Configuration> configs = configDAO.findAll();
		EhourConfigStub		config = new EhourConfigStub();
		String				key, value;
	
		// spaghetti, anyone?
		for (Configuration configuration : configs)
		{
			key = configuration.getConfigKey();
			value = configuration.getConfigValue();
			
			if (key.equalsIgnoreCase("availableTranslations"))
			{
				config.setAvailableTranslations(value.split(","));
			}
			else if (key.equalsIgnoreCase("completeDayHours"))
			{
				config.setCompleteDayHours(Float.parseFloat(value));
			}
			else if (key.equalsIgnoreCase("localeCurrency"))
			{
				Locale locale;
				
				if (value != null && value.contains("_"))
				{
					String[] split = value.split("_");
					locale = new Locale(split[0], split[1]);
				}
				else
				{
					locale = new Locale("nl", "NL");
				}
				
				config.setCurrency(locale);
			}
			else if (key.equalsIgnoreCase("localeLanguage"))
			{
				config.setLocaleLanguage(value);
			}
			else if (key.equalsIgnoreCase("localeCountry"))
			{
				config.setLocaleCountry(value);
			}
			else if (key.equalsIgnoreCase("showTurnOver"))
			{
				config.setShowTurnover(Boolean.parseBoolean(value));
			}
			else if (key.equalsIgnoreCase("timeZone"))
			{
				config.setTimeZone(value);
			}
			else if (key.equalsIgnoreCase("mailFrom"))
			{
				config.setMailFrom(value);
			}
			else if (key.equalsIgnoreCase("mailSmtp"))
			{
				config.setMailSmtp(value);
			}
			else if (key.equalsIgnoreCase("smtpUsername"))
			{
				config.setSmtpUsername(value);
			}			
			else if (key.equalsIgnoreCase("smtpPassword"))
			{
				config.setSmtpPassword(value);
			}			
			else if (key.equalsIgnoreCase("smtpPort"))
			{
				config.setSmtpPort(value);
			}			
			else if (key.equalsIgnoreCase("demoMode"))
			{
				config.setDemoMode(Boolean.parseBoolean(value));
			}
			else if (key.equalsIgnoreCase("initialized"))
			{
				config.setInitialized(Boolean.parseBoolean(value));
			}
			else if (key.equalsIgnoreCase("firstDayOfWeek"))
			{
				config.setFirstDayOfWeek( (int)(Float.parseFloat(value)));
			}
			else if (key.equalsIgnoreCase("auditType"))
			{
				config.setAuditType(AuditType.fromString(value));
			}
		}
		
		return config;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.config.service.ConfigService#persistConfiguration(java.util.List)
	 */
	@Transactional
	@Auditable(actionType=AuditActionType.UPDATE)
	public void persistConfiguration(EhourConfig config)
	{
		logger.debug("Persisting config");
		persistConfig("localeCurrency", config.getCurrency().getLanguage() + "_" + config.getCurrency().getCountry());
		
		if (config.getCompleteDayHours() != 0)
		{
			persistConfig("completeDayHours", config.getCompleteDayHours());
		}
		
		persistConfig("localeCountry", config.getLocale().getCountry());
		persistConfig("localeLanguage", config.getLocale().getLanguage());
		persistConfig("dontForceLanguage", config.isDontForceLanguage());
		persistConfig("showTurnOver", config.isShowTurnover());
		persistConfig("mailFrom", config.getMailFrom());
		persistConfig("mailSmtp", config.getMailSmtp());
		persistConfig("smtpUsername", config.getSmtpUsername());
		persistConfig("smtpPassword", config.getSmtpPassword());
		persistConfig("smtpPort", config.getSmtpPort());
		persistConfig("initialized", config.isInitialized());
		persistConfig("firstDayOfWeek", config.getFirstDayOfWeek());
		persistConfig("auditType", getAuditType(config).getValue());
	}
	
	private AuditType getAuditType(EhourConfig config)
	{
		if (config.getAuditType() == null)
		{
			return AuditType.WRITE;
		}
		else
		{
			return config.getAuditType(); 
		}
	}
	
	private void persistConfig(String key, String value)
	{
		Configuration config = new Configuration();
		config.setConfigKey(key);
		config.setConfigValue(value == null ? "" : value);
		
		configDAO.persist(config);
	}

	private void persistConfig(String key, boolean value)
	{
		persistConfig(key, Boolean.toString(value));
	}	

	private void persistConfig(String key, float value)
	{
		persistConfig(key, Float.toString(value));
	}	

	/**
	 * @param configDAO the configDAO to set
	 */
	public void setConfigDAO(ConfigurationDAO configDAO)
	{
		this.configDAO = configDAO;
	}
	
	/**
	 * @param binConfigDAO the binConfigDAO to set
	 */
	public void setBinConfigDAO(BinaryConfigurationDAO binConfigDAO)
	{
		this.binConfigDAO = binConfigDAO;
	}
}
