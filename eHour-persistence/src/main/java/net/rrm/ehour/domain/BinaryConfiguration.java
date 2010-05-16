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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.Hibernate;

/**
 * Configuration domain object specifically for binary values (images and stuff)
 * Created on Apr 2, 2009, 4:11:26 PM
 * 
 * @author Thies Edeling (thies@te-con.nl)
 * 
 */
public class BinaryConfiguration extends DomainObject<String, BinaryConfiguration>
{
	private static final long serialVersionUID = -2162032227850306610L;

	private String configKey;
	private byte[] configValue;
	private String metadata;
	
	
	/** Don't invoke this. Used by Hibernate only. */
	public void setValueBlob(Blob imageBlob)
	{
		this.configValue = this.toByteArray(imageBlob);
	}

	/** Don't invoke this. Used by Hibernate only. */
	public Blob getValueBlob()
	{
		return Hibernate.createBlob(this.getConfigValue());
	}

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
		this.configValue = configValue;
	}

	/**
	 * @return the configValue
	 */
	public byte[] getConfigValue()
	{
		return configValue;
	}

	private byte[] toByteArray(Blob fromBlob)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try
		{
			return toByteArrayImpl(fromBlob, baos);
		} catch (SQLException e)
		{
			throw new RuntimeException(e);
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		} finally
		{
			if (baos != null)
			{
				try
				{
					baos.close();
				} catch (IOException ex)
				{
				}
			}
		}
	}

	private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos) throws SQLException, IOException
	{
		byte[] buf = new byte[4000];
		InputStream is = fromBlob.getBinaryStream();
		try
		{
			for (;;)
			{
				int dataSize = is.read(buf);

				if (dataSize == -1)
					break;
				baos.write(buf, 0, dataSize);
			}
		} finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				} catch (IOException ex)
				{
				}
			}
		}
		return baos.toByteArray();
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

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(String metadata)
	{
		this.metadata = metadata;
	}

	/**
	 * @return the metadata
	 */
	public String getMetadata()
	{
		return metadata;
	}
}
