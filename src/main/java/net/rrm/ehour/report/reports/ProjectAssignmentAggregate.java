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

package net.rrm.ehour.report.reports;

import org.apache.commons.lang.builder.ToStringBuilder;

import net.rrm.ehour.project.domain.ProjectAssignment;

/**
 * TODO 
 **/

public class ProjectAssignmentAggregate implements Comparable<ProjectAssignmentAggregate>
{
	private ProjectAssignment 	projectAssignment;
	private Number 				hours;
	private Number 				turnOver;

	/**
	 * empty constructor
	 *
	 */
	public ProjectAssignmentAggregate()
	{
	}

	/**
	 * full constructor
	 * @param projectAssignment
	 * @param hours
	 */
	public ProjectAssignmentAggregate(ProjectAssignment projectAssignment, Number hours, Number turnOver)
	{
		this.hours = hours;
		this.projectAssignment = projectAssignment;
		this.turnOver = turnOver;
	}

	public Number getHours()
	{
		return hours;
	}

	public void setHours(Number hours)
	{
		this.hours = hours;
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
	 * @return the turnOver
	 */
	public Number getTurnOver()
	{
		return turnOver;
	}

	/**
	 * @param turnOver the turnOver to set
	 */
	public void setTurnOver(Number turnOver)
	{
		this.turnOver = turnOver;
	}

	/**
	 * 
	 */
	public int compareTo(ProjectAssignmentAggregate pagO)
	{
		// TODO
		return 0;
	}
	
	public String toString()
	{
		return new ToStringBuilder(this)
			.append("ProjectAssignment", projectAssignment)
			.append("hours", hours)
			.append("turnOver", turnOver)
			.toString();		
	}

}
