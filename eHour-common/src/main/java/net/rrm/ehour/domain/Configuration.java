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

package net.rrm.ehour.domain;


import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Configuration domain object
 **/

public class Configuration extends DomainObject<String, Configuration>
{
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

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Configuration object)
	{
		return new CompareToBuilder()
		.append(this.getConfigKey(), object.getConfigKey()).toComparison();
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
			return new EqualsBuilder().append(this.getConfigKey(), castOther.getConfigKey()).isEquals();
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
		return new HashCodeBuilder().append(getConfigKey()).toHashCode();
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
