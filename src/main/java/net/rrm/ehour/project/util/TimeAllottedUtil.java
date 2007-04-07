/**
 * Created on Apr 7, 2007
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

package net.rrm.ehour.project.util;

import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.dao.ReportAggregatedDAO;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.util.EhourConstants;

/**
 * Time allotted util class
 **/

public class TimeAllottedUtil
{
	private	ReportAggregatedDAO		reportAggregatedDAO;

	/**
	 * Is time allotted assignment overrun
	 * @param assignment
	 * @return
	 */
	public ProjectAssignmentAggregate isTimeAllottedAssignmentOverrun(ProjectAssignment assignment)
	{
		int assignmentTypeId = assignment.getAssignmentType().getAssignmentTypeId().intValue();
		ProjectAssignmentAggregate aggregate = null;
		
		if (assignmentTypeId == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED)
		{
			aggregate = isFixedAllottedAssignmentOverrun(assignment);
		}
		else if (assignmentTypeId == EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX)
		{
			aggregate = isFlexAllottedAssignmentOverrun(assignment);
		}		
		
		return aggregate;
	}
	

	/**
	 * Check if a fixed allotted assignment is overrun (as in, no more hours left)
	 * @param assignment
	 * @return
	 */
	private ProjectAssignmentAggregate isFixedAllottedAssignmentOverrun(ProjectAssignment assignment)
	{
		boolean	overrun = false;
		
		ProjectAssignmentAggregate aggregate = reportAggregatedDAO.getCumulatedHoursForAssignment(assignment);
		
		if (aggregate != null)
		{
			overrun = (aggregate.getHours().floatValue() >= assignment.getAllottedHours().floatValue());
		}
		
		return overrun ? aggregate : null;
	}
	
	/**
	 * Check if a flex allotted assignment is overrun (as in, no more hours left)
	 * @param assignment
	 * @return
	 */
	private ProjectAssignmentAggregate isFlexAllottedAssignmentOverrun(ProjectAssignment assignment)
	{
		boolean	overrun = false;
		
		ProjectAssignmentAggregate aggregate = reportAggregatedDAO.getCumulatedHoursForAssignment(assignment);

		if (aggregate != null)
		{
			overrun = (aggregate.getHours().floatValue() >= 
							(assignment.getAllottedHours().floatValue() + assignment.getAllowedOverrun().floatValue()));
		}
		
		return overrun ? aggregate : null;
	}
	
	/**
	 * @param reportAggregatedDAO the reportAggregatedDAO to set
	 */
	public void setReportAggregatedDAO(ReportAggregatedDAO reportAggregatedDAO)
	{
		this.reportAggregatedDAO = reportAggregatedDAO;
	}	
}
