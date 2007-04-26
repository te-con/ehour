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

package net.rrm.ehour.project.dto;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;

/**
 * ProjectAssignment status 
 **/

public class AssignmentStatus
{
	/**
	 * A flex assignment has 3 phases: 
	 *  - booked hours before allotted hours mark (IN_ALLOTTED_PHASE)
	 * 	- over  the allotted hours mark but before the overrun mark (IN_OVERRUN_PHASE)
	 *  - over the overrun mark, no more hours can be booked and mail should be sent (OVER_OVERRUN_PHASE)
	 *  
	 * A fixed assignment has 2 phases:
	 *  - booked hours before allotted hours mark (IN_ALLOTTED_PHASE)
	 *  - over the alloted hours mark, no more hours can be booked and mail should be sent (OVER_ALLOTTED_PHASE)
	 */

	public final static int IN_OVERRUN_PHASE = 1;
	public final static int IN_ALLOTTED_PHASE = 2;
	public final static int OVER_ALLOTTED_PHASE = 3;
	public final static int OVER_OVERRUN_PHASE = 3;
	public final static int IN_DATERANGE_PHASE = 4;
	public final static int OUT_DATERANGE_PHASE = 4;
	
	private ProjectAssignmentAggregate	aggregate;
	private int assignmentPhase;
	
	/**
	 * 
	 * @return
	 */
	public boolean isAssignmentBookable()
	{
		return (assignmentPhase == IN_DATERANGE_PHASE || 
				assignmentPhase == IN_OVERRUN_PHASE || 
				assignmentPhase == IN_ALLOTTED_PHASE);
	}
	
	/**
	 * @return the aggregate
	 */
	public ProjectAssignmentAggregate getAggregate()
	{
		return aggregate;
	}
	/**
	 * @param aggregate the aggregate to set
	 */
	public void setAggregate(ProjectAssignmentAggregate aggregate)
	{
		this.aggregate = aggregate;
	}
	/**
	 * @return the assignmentPhase
	 */
	public int getAssignmentPhase()
	{
		return assignmentPhase;
	}
	/**
	 * @param assignmentPhase the assignmentPhase to set
	 */
	public void setAssignmentPhase(int assignmentPhase)
	{
		this.assignmentPhase = assignmentPhase;
	}
}
