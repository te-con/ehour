package net.rrm.ehour.domain;

import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Audit extends DomainObject<Number, Audit>
{
	private static final long serialVersionUID = -5025801585806813596L;

	private Number	auditId;
	private User 	user;
	private Date 	date;
	private String 	action;
	private String	parameters;
	private Boolean	success;
	private String	userName;
	private String	page;
	
	public String toString()
	{
		return new ToStringBuilder(this)
						.append("auditId", auditId)
						.append("user", user)
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
	public String getUserName()
	{
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public Audit setUserName(String userName)
	{
		this.userName = userName;
		return this;
	}

	private AuditActionType auditActionType;
	

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
		this.parameters = parameters;
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
