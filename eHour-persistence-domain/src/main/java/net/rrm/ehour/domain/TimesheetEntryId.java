/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.domain;

import java.io.Serializable;
import java.util.Date;


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

	private Integer displayOrder = 1;
	
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
															.append("assignment", getProjectAssignment())
															.append("displayOrder", getDisplayOrder())
															.toString();
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
									.append(this.getDisplayOrder(), castOther.getDisplayOrder())
								  .append(this.getProjectAssignment(), castOther.getProjectAssignment()).isEquals();
	}

	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(getEntryDate())
									.append(getDisplayOrder())
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
			.append(this.entryDate, object.entryDate)
			.append(this.displayOrder, object.displayOrder)
			.toComparison();
	}

	public Integer getDisplayOrder()
	{
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder)
	{
		this.displayOrder = displayOrder;
	}

}
