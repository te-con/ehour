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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name="AUDIT")
public class Audit extends DomainObject<Number, Audit>
{
	private static final long serialVersionUID = -5025801585806813596L;

	
	@Id @GeneratedValue
	@Column (name = "AUDIT_ID")
	private Number	auditId;
	
	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User 	user;
	
	@Column(name = "AUDIT_DATE")
	private Date 	date;
	
	@Column(name = "ACTION")
	private String 	action;
	
	@Column(name = "PARAMETERS")
	private String	parameters;
	
	@Column(name = "SUCCESS")
	private Boolean	success;
	
	@Column(name = "USER_FULLNAME")
	private String	userFullName;
	
	@Column(name = "PAGE")
	private String	page;
	private AuditActionType auditActionType;
	
	public String toString()
	{
		return new ToStringBuilder(this)
						.append("auditId", auditId)
						.append("userFullName", userFullName)
						.append("date", date)
						.append("action", action)
						.toString();
	}
	
	/**
	 * @return the page
	 */
	public String getPage()
	{
		return page;
	}

	/**
	 * @param page the page to set
	 */
	public Audit setPage(String page)
	{
		this.page = page;
		return this;
	}

	/**
	 * @return the userName
	 */
	public String getUserFullName()
	{
		return userFullName;
	}

	/**
	 * @param userName the userName to set
	 */
	public Audit setUserFullName(String userFullName)
	{
		this.userFullName = userFullName;
		return this;
	}


	/**
	 * @return the parameters
	 */
	public String getParameters()
	{
		return parameters;
	}

	/**
	 * @return the success
	 */
	public Boolean getSuccess()
	{
		return success;
	}

	/**
	 * @return the auditActionType
	 */
	public AuditActionType getAuditActionType()
	{
		return auditActionType;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public Audit setParameters(String parameters)
	{
		this.parameters =  (parameters != null && parameters.length() > 4096) ? parameters.substring(0, 4095) : parameters;
		return this;
	}

	/**
	 * @param success the success to set
	 */
	public Audit setSuccess(Boolean success)
	{
		this.success = success;
		return this;
	}

	/**
	 * @param auditActionType the auditActionType to set
	 */
	public Audit setAuditActionType(AuditActionType auditActionType)
	{
		this.auditActionType = auditActionType;
		return this;
	}

	public Audit setAction(String action)
	{
		this.action = action;
		return this;
	}

	public Audit setUser(User user)
	{
		this.user = user;
		return this;
	}

	public Audit setDate(Date date)
	{
		this.date = date;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#getPK()
	 */
	@Override
	public Number getPK()
	{
		return auditId;
	}

	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * @return the date
	 */
	public Date getDate()
	{
		return date;
	}

	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}



	/**
	 * @return the auditId
	 */
	public Number getAuditId()
	{
		return auditId;
	}

	/**
	 * @param auditId the auditId to set
	 */
	public void setAuditId(Number auditId)
	{
		this.auditId = auditId;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Audit o)
	{
		return new CompareToBuilder()
			.append(this.getAuditId(), o.getAuditId())
			.toComparison();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#hashCode()
	 */
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder()
					.append(this.getAuditId())
					.toHashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.domain.DomainObject#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		
		if (obj == this)
		{
			return true;
		}
		
		if (obj.getClass() != getClass())
		{
			return false;
		}
		
		Audit other = (Audit) obj;

		return new EqualsBuilder()
				.append(this.getAuditId(), other.getAuditId())
				.isEquals();
	}	

}
