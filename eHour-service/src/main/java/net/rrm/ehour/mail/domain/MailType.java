/**
 * Created on Apr 6, 2007
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
