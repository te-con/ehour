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
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Configuration domain object specifically for binary values (images and stuff)
 * Created on Apr 2, 2009, 4:11:26 PM
 * 
 * @author Thies Edeling (thies@te-con.nl)
 * 
 */
@Entity
@Table(name = "CONFIGURATION_BIN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)

public class BinaryConfiguration extends DomainObject<String, BinaryConfiguration>
{
	private static final long serialVersionUID = -2162032227850306610L;
                                                                                                                                               
    @Id
    @Column(name = "CONFIG_KEY", length = 255)
    @NotNull
	private String configKey;

    @Lob
    @Column(name = "CONFIG_VALUE")
	private byte[] configValue;
	
	/**
	 * @param configKey
	 *            the configKey to set
	 */
	public void setConfigKey(String configKey)
	{
		this.configKey = configKey;
	}

	/**
	 * @return the configKey
	 */
	public String getConfigKey()
	{
		return configKey;
	}

	/**
	 * @param configValue
	 *            the configValue to set
	 */
	public void setConfigValue(byte[] configValue)
	{
		this.configValue = configValue.clone();
	}

	/**
	 * @return the configValue
	 */
	public byte[] getConfigValue()
	{
		return configValue;
	}



	/* (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other)
	{
		BinaryConfiguration castOther;

		if (other instanceof BinaryConfiguration)
		{
			castOther = (BinaryConfiguration) other;
			return new EqualsBuilder().append(this.getConfigKey(), castOther.getConfigKey()).isEquals();
		} else
		{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#getPK()
	 */
	@Override
	public String getPK()
	{
		return configKey;
	}

	/* (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getConfigKey()).toHashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(BinaryConfiguration object)
	{
		return new CompareToBuilder()
				.append(this.getConfigKey(), object.getConfigKey()).toComparison();
	}


}
