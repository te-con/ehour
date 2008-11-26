package net.rrm.ehour.audit.service.dto;

import java.io.Serializable;
import java.util.Date;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.AuditType;
import net.rrm.ehour.domain.User;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author thies
 *
 */
public class AuditReportRequest implements Serializable
{
	private static final long serialVersionUID = -8309219696303534810L;

	private DateRange	dateRange = new DateRange();
	private User		user;
	private AuditType	auditType;
	private Integer		offset;
	private Integer		max;
//	private ProjectAssignment projectAssignment;
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this)
									.append("dateRange", dateRange)
									.append("user", user)
									.append("auditType", auditType)
									.toString();
	}
	
	public boolean isInfiniteStartDate()
	{
		return getDateRange().getDateStart() == null;
	}

	public void setInfiniteStartDate(boolean infinite)
	{
		if (infinite)
		{
			getDateRange().setDateStart(null);
		}
		else if (getDateRange().getDateStart() == null)
		{
			getDateRange().setDateStart(new Date());
		}
	}

	public boolean isInfiniteEndDate()
	{
		return getDateRange().getDateEnd() == null;
	}

	public void setInfiniteEndDate(boolean infinite)
	{
		if (infinite)
		{
			getDateRange().setDateEnd(null);
		}
		else if (getDateRange().getDateEnd() == null)
		{
			getDateRange().setDateEnd(new Date());
		}
	}
	
	
	/**
	 * @return the offset
	 */
	public Integer getOffset()
	{
		return offset;
	}
	/**
	 * @return the max
	 */
	public Integer getMax()
	{
		return max;
	}
	/**
	 * @param offset the offset to set
	 */
	public AuditReportRequest setOffset(Integer offset)
	{
		this.offset = offset;
		return this;
	}
	/**
	 * @param max the max to set
	 */
	public AuditReportRequest setMax(Integer max)
	{
		this.max = max;
		
		return this;
	}
	/**
	 * @return the dateRange
	 */
	public DateRange getDateRange()
	{
		return dateRange;
	}
	/**
	 * @return the user
	 */
	public User getUser()
	{
		return user;
	}
	/**
	 * @return the auditType
	 */
	public AuditType getAuditType()
	{
		return auditType;
	}
	/**
	 * @param dateRange the dateRange to set
	 */
	public void setDateRange(DateRange dateRange)
	{
		this.dateRange = dateRange;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user)
	{
		this.user = user;
	}
	/**
	 * @param auditType the auditType to set
	 */
	public void setAuditType(AuditType auditType)
	{
		this.auditType = auditType;
	}
}
