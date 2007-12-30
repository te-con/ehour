/**
 * Created on Apr 7, 2007
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

package net.rrm.ehour.project.dto;

import net.rrm.ehour.report.reports.dto.AssignmentAggregateReportElement;

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
	
	private AssignmentAggregateReportElement	aggregate;
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
	public AssignmentAggregateReportElement getAggregate()
	{
		return aggregate;
	}
	/**
	 * @param aggregate the aggregate to set
	 */
	public void setAggregate(AssignmentAggregateReportElement aggregate)
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
