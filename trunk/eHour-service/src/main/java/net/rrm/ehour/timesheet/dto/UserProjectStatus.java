/**
 * Created on Apr 8, 2007
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

package net.rrm.ehour.timesheet.dto;

import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

/**
 * Value object for timesheet overview. While the hours in an aggregate
 * reflect only a certain period, totalBookedHours is all hours booked on this assignment
 */

public class UserProjectStatus extends AssignmentAggregateReportElement
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2321889010790629630L;
	private Number	totalBookedHours;

	public UserProjectStatus()
	{
		
	}
	
	public UserProjectStatus(AssignmentAggregateReportElement aggregate)
	{
		this(aggregate, null);
	}
	
	public UserProjectStatus(AssignmentAggregateReportElement aggregate, Number totalBookedHours)
	{
		super(aggregate.getProjectAssignment(), aggregate.getHours());
		this.totalBookedHours = totalBookedHours;
	}
	
	/**
	 * Get fixed hours remaining to book on this project
	 * This is applicable to fixed and flex assignments
	 * @return
	 */
	public Float getFixedHoursRemaining()
	{
		Float	remainder = null;
		
		if (totalBookedHours != null)
		{
			if (getProjectAssignment().getAssignmentType().isAllottedType())
			{
				remainder = new Float(getProjectAssignment().getAllottedHours().floatValue() -
										totalBookedHours.floatValue());
				// Flex assignment that are over their fixed number of hours should report
				// zero fixed remaining.
				if (getProjectAssignment().getAssignmentType().isFlexAllottedType())
				{
					if (remainder.floatValue() < 0) {
						remainder = new Float(0f);
					}
				}
				
			}
		}
		
		return remainder;
	}
	
	/**
	 * Get flex hours remaining to book on this project
	 * @return
	 */
	public Float getFlexHoursRemaining()
	{
		Float	remainder = null;
		
		if (totalBookedHours != null)
		{
			if (getProjectAssignment().getAssignmentType().isFlexAllottedType())
			{
				if (totalBookedHours.floatValue()<= getProjectAssignment().getAllottedHours().floatValue()) {
					remainder = new Float(getProjectAssignment().getAllowedOverrun().floatValue());
				}
				else
				{
					remainder = new Float(getProjectAssignment().getAllottedHours().floatValue() 
										+ getProjectAssignment().getAllowedOverrun().floatValue()
										- totalBookedHours.floatValue());
				}
			}
		}
		
		return remainder;
	}
	
	/**
	 * @return the totalBookedHours
	 */
	public Number getTotalBookedHours()
	{
		return totalBookedHours;
	}

	/**
	 * @param totalBookedHours the totalBookedHours to set
	 */
	public void setTotalBookedHours(Number totalBookedHours)
	{
		this.totalBookedHours = totalBookedHours;
	}
	
}
