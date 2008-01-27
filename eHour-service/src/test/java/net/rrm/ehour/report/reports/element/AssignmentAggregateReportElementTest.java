/**
 * Created on Jan 27, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.report.reports.element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.util.EhourConstants;

import org.junit.Test;

/**
 * TODO 
 **/

public class AssignmentAggregateReportElementTest
{

	/**
	 * Test method for {@link net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement#getProgressPercentage()}.
	 */
	@Test
	public void testGetProgressPercentageAllotted()
	{
		ProjectAssignment assignment = new ProjectAssignment();
		assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED));
		
		assignment.setAllottedHours(100f);
		AssignmentAggregateReportElement ele = new AssignmentAggregateReportElement();
		ele.setHours(96f);
		ele.setProjectAssignment(assignment);
		
		assertEquals(96, ele.getProgressPercentage(), 0f);
	}

	@Test
	public void testGetProgressPercentageDate()
	{
		ProjectAssignment assignment = new ProjectAssignment();
		assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_DATE));
		
		Calendar startDate = new GregorianCalendar();
		startDate.add(Calendar.DAY_OF_YEAR, -3);

		Calendar endDate = new GregorianCalendar();
		endDate.add(Calendar.DAY_OF_YEAR, 3);

		assignment.setDateStart(startDate.getTime());
		assignment.setDateEnd(endDate.getTime());
		
		AssignmentAggregateReportElement ele = new AssignmentAggregateReportElement();
		ele.setProjectAssignment(assignment);
		
		assertEquals(50, ele.getProgressPercentage(), 2f);
	}
	

	/**
	 * Test method for {@link net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement#getAvailableHours()}.
	 */
	@Test
	public void testGetAvailableHoursFixed()
	{
		ProjectAssignment assignment = new ProjectAssignment();
		assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FIXED));
		
		assignment.setAllottedHours(100f);
		AssignmentAggregateReportElement ele = new AssignmentAggregateReportElement();
		ele.setHours(96f);
		ele.setProjectAssignment(assignment);
		
		assertEquals(4, ele.getAvailableHours(), 0f);
	}
	

	/**
	 * Test method for {@link net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement#getAvailableHours()}.
	 */
	@Test
	public void testGetAvailableHoursFlex()
	{
		ProjectAssignment assignment = new ProjectAssignment();
		assignment.setAssignmentType(new ProjectAssignmentType(EhourConstants.ASSIGNMENT_TIME_ALLOTTED_FLEX));
		
		assignment.setAllottedHours(100f);
		assignment.setAllowedOverrun(40f);
		
		AssignmentAggregateReportElement ele = new AssignmentAggregateReportElement();
		ele.setHours(113f);
		ele.setProjectAssignment(assignment);
		
		assertEquals(27, ele.getAvailableHours(), 0f);
	}	

}
