/**
 * Created on 26-feb-2007
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

package net.rrm.ehour.web.timesheet.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.project.domain.ProjectAssignmentType;
import net.rrm.ehour.project.util.ProjectAssignmentUtil;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.dto.WeekOverview;
import net.rrm.ehour.web.timesheet.dto.Timesheet;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO 
 **/

public class TimesheetFormAssemblerTest
{
	private	WeekOverview	weekOverview;
	private	TimesheetFormAssembler	ass;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		weekOverview = new WeekOverview();
		
		TimesheetEntry	entryA = DummyDataGenerator.getTimesheetEntry(1, new Date(2007 - 1900, 5 -1 , 5), 4.0f);
		TimesheetEntry	entryB = DummyDataGenerator.getTimesheetEntry(2, new Date(2007 - 1900, 5 -1 , 6), 4.5f);
		TimesheetEntry	entryC = DummyDataGenerator.getTimesheetEntry(2, new Date(2007 - 1900, 5 -1 , 7), 5.0f);
		entryA.getEntryId().getProjectAssignment().setDateStart(new Date(2007 - 1900, 1 -1 , 7));
		entryA.getEntryId().getProjectAssignment().setDateEnd(new Date(2008 - 1900, 1 -1 , 7));
		entryB.getEntryId().getProjectAssignment().setDateStart(new Date(2007 - 1900, 1 -1 , 7));
		entryB.getEntryId().getProjectAssignment().setDateEnd(new Date(2008 - 1900, 1 -1 , 7));
		entryC.getEntryId().getProjectAssignment().setDateStart(new Date(2007 - 1900, 1 -1 , 7));
		entryC.getEntryId().getProjectAssignment().setDateEnd(new Date(2008 - 1900, 1 -1 , 7));
		
		List<TimesheetEntry> entries = new ArrayList<TimesheetEntry>();
		entries.add(entryA);
		entries.add(entryB);
		entries.add(entryC);
		
		weekOverview.setTimesheetEntries(entries);
		
		weekOverview.setWeekRange(new DateRange(new Date(2007 - 1900, 5 - 1, 2),new Date(2007 - 1900, 5 - 1, 8)));
		
		List<ProjectAssignment> pas = new ArrayList<ProjectAssignment>();
		ProjectAssignment pa1 = DummyDataGenerator.getProjectAssignment(2);
		pa1.setDateStart(new Date(2007 - 1900, 1 -1 , 7));
		pa1.setDateEnd(new Date(2008 - 1900, 1 -1 , 7));
		pa1.setAssignmentType(new ProjectAssignmentType(0));
		pas.add(pa1);

		ProjectAssignment pa2 = DummyDataGenerator.getProjectAssignment(3);
		pa2.setDateStart(new Date(2007 - 1900, 1 -1 , 7));
		pa2.setDateEnd(new Date(2008 - 1900, 1 -1 , 7));
		pa2.setAssignmentType(new ProjectAssignmentType(0));
		pas.add(pa2);
		weekOverview.setProjectAssignments(pas);
		
		ass = new TimesheetFormAssembler();
	}

	/**
	 * Test method for {@link net.rrm.ehour.web.timesheet.util.TimesheetFormAssembler#createTimesheetForm(net.rrm.ehour.timesheet.dto.WeekOverview)}.
	 */
	@Test
	public void testCreateTimesheetForm()
	{
		Timesheet sheet = ass.createTimesheetForm(weekOverview);
		assertEquals(3, sheet.getTimesheetRows().size());
	}

	/**
	 * Test method for {@link net.rrm.ehour.web.timesheet.util.TimesheetFormAssembler#mergeUnbookedAssignments(net.rrm.ehour.timesheet.dto.WeekOverview, java.util.Map)}.
	 */
	@Test
	public void testMergeUnbookedAssignments()
	{
		Map<ProjectAssignment, Map<Date, TimesheetEntry>> resMap = ass.createAssignmentMap(weekOverview);
		ass.mergeUnbookedAssignments(weekOverview, resMap);
		
		assertEquals(3, resMap.keySet().size());
	}

	/**
	 * Test method for {@link net.rrm.ehour.web.timesheet.util.TimesheetFormAssembler#createAssignmentMap(net.rrm.ehour.timesheet.dto.WeekOverview)}.
	 */
	@Test
	public void testCreateAssignmentMap()
	{
		Map<ProjectAssignment, Map<Date, TimesheetEntry>> resMap = ass.createAssignmentMap(weekOverview);
		assertEquals(2, resMap.keySet().size());
	}

}
