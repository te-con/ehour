/**
 * Created on Apr 6, 2007
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

package net.rrm.ehour.mail.domain;

import net.rrm.ehour.domain.DomainObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 *  
 **/

public class MailType extends DomainObject<Integer, MailType>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1173342235391507529L;
	private	Integer	mailTypeId;
	private	String	mailType;
	
	public MailType()
	{
		
	}
	
	public MailType(Integer mailTypeId)
	{
		this.mailTypeId = mailTypeId;
	}
	
	/**
	 * @return the mailType
	 */
	public String getMailType()
	{
		return mailType;
	}
	/**
	 * @param mailType the mailType to set
	 */
	public void setMailType(String mailType)
	{
		this.mailType = mailType;
	}
	/**
	 * @return the mailTypeId
	 */
	public Integer getMailTypeId()
	{
		return mailTypeId;
	}
	/**
	 * @param mailTypeId the mailTypeId to set
	 */
	public void setMailTypeId(Integer mailTypeId)
	{
		this.mailTypeId = mailTypeId;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object)
	{
		if (!(object instanceof MailType))
		{
			return false;
		}
		MailType rhs = (MailType) object;
		return new EqualsBuilder().appendSuper(super.equals(object)).append(this.mailTypeId, rhs.mailTypeId).append(this.mailType, rhs.mailType).isEquals();
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#hashCode()
	 */
	public int hashCode()
	{
		return new HashCodeBuilder(732615599, 2083936031).appendSuper(super.hashCode()).append(this.mailTypeId).append(this.mailType).toHashCode();
	}

	
	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#getPK()
	 */
	@Override
	public Integer getPK()
	{
		return mailTypeId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MailType o)
	{
		// TODO Auto-generated method stub
		return mailType.compareTo(o.getMailType());
	}
}
