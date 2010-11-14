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


import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

public class TimesheetEntry extends DomainObject<TimesheetEntryId, TimesheetEntry>
{
	private static final long serialVersionUID = 3258176976827482751L;

	/** identifier field */
    @Valid
	private TimesheetEntryId entryId;

	/** nullable persistent field */
	private Float hours;
	
	private String comment;

    @NotNull
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

	public boolean isEmptyEntry()
	{
		return StringUtils.isBlank(getComment())
				&& (getHours() == null || getHours().equals(0f));
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

	@Override
	public boolean equals(final Object other)
	{
		if (!(other instanceof TimesheetEntry))
			return false;
		TimesheetEntry castOther = (TimesheetEntry) other;
		return new EqualsBuilder().append(getEntryId(), castOther.getEntryId()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(entryId).toHashCode();
	}
}