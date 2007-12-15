package net.rrm.ehour.timesheet.domain;

import net.rrm.ehour.domain.DomainObject;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;

public class TimesheetEntry extends DomainObject<TimesheetEntryId, TimesheetEntry>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3258176976827482751L;

	/** identifier field */
	private TimesheetEntryId entryId;

	/** nullable persistent field */
	private Float hours;
	
	private String comment;

	/** full constructor */
	public TimesheetEntry(TimesheetEntryId entryId, Float hours)
	{
		this.entryId = entryId;
		this.hours = hours;
	}

	/** default constructor */
	public TimesheetEntry()
	{
	}

	/** minimal constructor */
	public TimesheetEntry(TimesheetEntryId entryId)
	{
		this.entryId = entryId;
	}

	public TimesheetEntryId getEntryId()
	{
		return this.entryId;
	}

	public void setEntryId(TimesheetEntryId entryId)
	{
		this.entryId = entryId;
	}

	public Float getHours()
	{
		return this.hours;
	}

	public void setHours(Float hours)
	{
		this.hours = hours;
	}



	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if (!(other instanceof TimesheetEntry))
			return false;
		TimesheetEntry castOther = (TimesheetEntry) other;
		return new EqualsBuilder().append(this.getEntryId(), castOther.getEntryId()).isEquals();
	}

	public int hashCode()
	{
		return new HashCodeBuilder().append(getEntryId()).toHashCode();
	}

	@Override
	public TimesheetEntryId getPK()
	{
		return entryId;
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(TimesheetEntry object)
	{
		return new CompareToBuilder()
			.append(this.entryId, object.entryId).toComparison();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this)
				.append("hours", this.hours)
				.append("entryId", this.entryId)
				.append("comment", this.comment)
				.toString();
	}

	public String getComment()
	{
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}
}