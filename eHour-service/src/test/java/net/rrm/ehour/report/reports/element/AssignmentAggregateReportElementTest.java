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

package net.rrm.ehour.report.reports.element;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.util.EhourConstants;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

public class AssignmentAggregateReportElementTest
{

	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.report.reports.element.AssignmentAggregateReportElement#getProgressPercentage()}.
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
		
		assertEquals(96, ele.getProgressPercentage().get(), 0f);
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
		
		assertEquals(50, ele.getProgressPercentage().get(), 2f);
	}
	

	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.report.reports.element.AssignmentAggregateReportElement#getAvailableHours()}.
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
		
		assertEquals(4, ele.getAvailableHours().get(), 0f);
	}
	

	/**
	 * Test method for {@link net.rrm.ehour.persistence.persistence.report.reports.element.AssignmentAggregateReportElement#getAvailableHours()}.
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
		
		assertEquals(27, ele.getAvailableHours().get(), 0f);
	}	

}
