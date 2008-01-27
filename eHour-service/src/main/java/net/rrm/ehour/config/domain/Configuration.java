/**
 * Created on Mar 19, 2007
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

package net.rrm.ehour.config.domain;

import net.rrm.ehour.domain.DomainObject;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Configuration domain object
 **/

public class Configuration extends DomainObject<String, Configuration>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5457250186090868408L;
	private	String	configKey;
	private	String	configValue;
	
	public Configuration()
	{
		
	}

	public Configuration(String key, String value)
	{
		this.configKey = key;
		this.configValue = value;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#getPK()
	 */
	@Override
	public String getPK()
	{
		return configKey;
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(Configuration object)
	{
		return new CompareToBuilder()
		.append(this.configKey, object.configKey).toComparison();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other)
	{
		Configuration 	castOther;
		
		if (other instanceof Configuration)
		{
			castOther = (Configuration)other;
			return new EqualsBuilder().append(this.configKey, castOther.getConfigKey()).isEquals();
		}
		else
		{
			return false;
		}
	}	
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(configKey).toHashCode();
	}	

	/**
	 * @return the configKey
	 */
	public String getConfigKey()
	{
		return configKey;
	}

	/**
	 * @param configKey the configKey to set
	 */
	public void setConfigKey(String configKey)
	{
		this.configKey = configKey;
	}

	/**
	 * @return the configValue
	 */
	public String getConfigValue()
	{
		return configValue;
	}

	/**
	 * @param configValue the configValue to set
	 */
	public void setConfigValue(String configValue)
	{
		this.configValue = configValue;
	}	
	
}
