/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.timesheet.domain;

import java.io.Serializable;
import java.util.Date;

import net.rrm.ehour.project.domain.ProjectAssignment;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.CompareToBuilder;

public class TimesheetEntryId implements Serializable, Comparable<TimesheetEntryId>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6439918043325585774L;

	/** identifier field */
	private Date entryDate;

	/** identifier field */
	private ProjectAssignment projectAssignment;

	/** full constructor */
	public TimesheetEntryId(Date entryDate, ProjectAssignment projectAssignment)
	{
		this.entryDate = entryDate;
		this.projectAssignment = projectAssignment;
	}

	/** default constructor */
	public TimesheetEntryId()
	{
	}

	public Date getEntryDate()
	{
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate)
	{
		this.entryDate = entryDate;
	}

	@Override
	public String toString()
	{
		return new ToStringBuilder(this).append("entryDate", getEntryDate())
										.append("assignment", getProjectAssignment()).toString();
	}

	@Override
	public boolean equals(Object other)
	{
		if ((this == other))
			return true;
		if (!(other instanceof TimesheetEntryId))
			return false;
		TimesheetEntryId castOther = (TimesheetEntryId) other;
		return new EqualsBuilder().append(this.getEntryDate(), castOther.getEntryDate())
								  .append(this.getProjectAssignment(), castOther.getProjectAssignment()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getEntryDate())
									.append(getProjectAssignment()).toHashCode();
	}

	public ProjectAssignment getProjectAssignment()
	{
		return projectAssignment;
	}

	public void setProjectAssignment(ProjectAssignment projectAssignment)
	{
		this.projectAssignment = projectAssignment;
	}

	/**
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	public int compareTo(TimesheetEntryId object)
	{
		return new CompareToBuilder()
			.append(this.projectAssignment, object.projectAssignment)
			.append(this.entryDate, object.entryDate).toComparison();
	}

}
