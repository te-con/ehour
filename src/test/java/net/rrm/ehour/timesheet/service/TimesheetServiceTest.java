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

package net.rrm.ehour.timesheet.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.report.dto.ProjectReport;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.domain.TimesheetEntryId;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.util.EhourConfig;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class TimesheetServiceTest  extends MockObjectTestCase
{
	private	TimesheetService	timesheetService;
	private	Mock				dao;
	private	Mock				reportService;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		timesheetService = new TimesheetServiceImpl();

		dao = new Mock(TimesheetDAO.class);
		reportService = new Mock(ReportService.class);
	
		((TimesheetServiceImpl)timesheetService).setTimesheetDAO((TimesheetDAO) dao.proxy());
		((TimesheetServiceImpl)timesheetService).setReportService((ReportService) reportService.proxy());
	}
	
	/**
	 * 
	 *
	 */
	public void testGetBookedDaysMonthOverview() throws Exception
	{
		List		daoResults = new ArrayList();
		List		results;
		BookedDay	bda, bdb,
					bdResult;
		Calendar	cal;
		EhourConfig config = new EhourConfig();
		
		bda = new BookedDay();
		bda.setDate(new Date(2006 - 1900, 10, 1));
		bda.setHours(new Float(6));

		bdb = new BookedDay();
		bdb.setDate(new Date(2006 - 1900, 10, 2));
		bdb.setHours(new Float(8));
		
		daoResults.add(bdb);	// test sort as well
		daoResults.add(bda);
		
		config.setCompleteDayHours(8);
		
		dao.expects(once())
		   .method("getBookedHoursperDayInRange")
		   .will(returnValue(daoResults));		

		((TimesheetServiceImpl)timesheetService).setEhourConfiguration(config);

		cal = new GregorianCalendar(2006, 10, 5);
		
		results = timesheetService.getBookedDaysMonthOverview(new Integer(1), cal);
		
		bdResult = (BookedDay)results.get(0);
		
		assertEquals(1, bdResult.getDate().getDate());
	}
	
	/**
	 * 
	 *
	 */
	public void testGetTimesheetOverview() throws Exception
	{
		List	daoResults = new ArrayList();	
		List	reportResults = new ArrayList();
		TimesheetOverview	retObj;
		TimesheetEntry		entryA, entryB;
		TimesheetEntryId	idA, idB;
		
		idA = new TimesheetEntryId(new Date(2006 - 1900, 10 - 1, 2), null);
		entryA = new TimesheetEntry();
		entryA.setEntryId(idA);
		entryA.setHours(new Float(5));
		daoResults.add(entryA);

		idB = new TimesheetEntryId(new Date(2006 - 1900, 10 - 1, 6), null);
		entryB = new TimesheetEntry();
		entryB.setEntryId(idB);
		entryB.setHours(new Float(3));
		daoResults.add(entryB);
		
		reportResults.add(new ProjectReport());
		
		dao.expects(once())
		   .method("getTimesheetEntriesInRange")
		   .will(returnValue(daoResults));
		
		reportService.expects(once())
					 .method("getHoursPerAssignmentInRange")
					 .will(returnValue(reportResults));
		
		retObj = timesheetService.getTimesheetOverview(new Integer(1), new GregorianCalendar());
		
		List l = (List) retObj.getTimesheetEntries().get(new Integer(2));
		TimesheetEntry e1 = (TimesheetEntry)l.get(0);
		assertEquals(5f, e1.getHours().floatValue(), 0.01);

		l = (List) retObj.getTimesheetEntries().get(new Integer(6));
		TimesheetEntry e2 = (TimesheetEntry)l.get(0);
		assertEquals(3f, e2.getHours().floatValue(), 0.01);
	}
}
