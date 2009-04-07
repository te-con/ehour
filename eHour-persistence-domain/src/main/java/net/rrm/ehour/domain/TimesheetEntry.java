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

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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

	private Date updateDate;
	
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
			.append(this.getEntryId(), object.getEntryId()).toComparison();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return new ToStringBuilder(this)
				.append("hours", this.getHours())
				.append("entryId", this.getEntryId())
				.append("comment", this.getComment())
				.append("updateDate", this.getUpdateDate())
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

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate()
	{
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}
}