package net.rrm.ehour.audit.service.dto;

import java.io.Serializable;
import java.util.Date;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.AuditType;

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
	private String		name;
	private String		action;
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
									.append("name", name)
									.append("action", action)
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
	 * @param auditType the auditType to set
	 */
	public void setAuditType(AuditType auditType)
	{
		this.auditType = auditType;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public AuditReportRequest setName(String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * @return the action
	 */
	public String getAction()
	{
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action)
	{
		this.action = action;
	}
}
