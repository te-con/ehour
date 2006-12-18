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

import static org.easymock.EasyMock.*;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.report.dto.ProjectReport;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.domain.TimesheetEntryId;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.util.DateUtil;

public class TimesheetServiceTest  extends TestCase
{
	private	TimesheetService	timesheetService;
	private	TimesheetDAO		timesheetDAO;
	private	EhourConfig			config;
	private	ReportService		reportService;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		timesheetService = new TimesheetServiceImpl();

		config = createMock(EhourConfig.class);
		timesheetDAO = createMock(TimesheetDAO.class);
		reportService = createMock(ReportService.class);
		
		((TimesheetServiceImpl)timesheetService).setTimesheetDAO(timesheetDAO);
		((TimesheetServiceImpl)timesheetService).setReportService(reportService);
		((TimesheetServiceImpl)timesheetService).setEhourConfig(config);
	}
	
	/**
	 * 
	 *
	 */
	public void testGetBookedDaysMonthOverview() throws Exception
	{
		List<BookedDay>		daoResults = new ArrayList<BookedDay>();
		List<BookedDay>		results;
		BookedDay	bda, bdb,
					bdResult;
		Calendar	cal;
		cal = new GregorianCalendar(2006, 10, 5);
		
		bda = new BookedDay();
		bda.setDate(new Date(2006 - 1900, 10, 1));
		bda.setHours(new Float(6));

		bdb = new BookedDay();
		bdb.setDate(new Date(2006 - 1900, 10, 2));
		bdb.setHours(new Float(8));
		
		daoResults.add(bdb);	// test sort as well
		daoResults.add(bda);

		expect(timesheetDAO.getBookedHoursperDayInRange(1, DateUtil.calendarToMonthRange(cal)))
				.andReturn(daoResults);
		expect(config.getCompleteDayHours())
				.andReturn(8)
				.times(2);
		
		replay(config);
	
		replay(timesheetDAO);
		
		results = timesheetService.getBookedDaysMonthOverview(1, cal);
		
		verify(timesheetDAO);
		verify(config);
		
		assertEquals(2, results.get(0).getDate().getDate());
	}
	
	/**
	 * 
	 *
	 */
	public void testGetTimesheetOverview() throws Exception
	{
		List<TimesheetEntry>	daoResults = new ArrayList<TimesheetEntry>();	
		List<ProjectReport>		reportResults = new ArrayList<ProjectReport>();
		Calendar	cal = new GregorianCalendar();
		
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
		
		expect(timesheetDAO.getTimesheetEntriesInRange(1, DateUtil.calendarToMonthRange(cal)))
				.andReturn(daoResults);
		
		expect(reportService.getHoursPerAssignmentInRange(1, DateUtil.calendarToMonthRange(cal)))
				.andReturn(reportResults);

		replay(timesheetDAO);
		replay(reportService);
		
		retObj = timesheetService.getTimesheetOverview(1, cal);
		
		verify(timesheetDAO);
		verify(reportService);
		
		List l = (List) retObj.getTimesheetEntries().get(new Integer(2));
		TimesheetEntry e1 = (TimesheetEntry)l.get(0);
		assertEquals(5f, e1.getHours().floatValue(), 0.01);

		l = (List) retObj.getTimesheetEntries().get(new Integer(6));
		TimesheetEntry e2 = (TimesheetEntry)l.get(0);
		assertEquals(3f, e2.getHours().floatValue(), 0.01);
		
		// projects only
		reset(timesheetDAO);
		reset(reportService);
		
		expect(reportService.getHoursPerAssignmentInRange(1, DateUtil.calendarToMonthRange(cal)))
			.andReturn(reportResults);
		
		replay(timesheetDAO);
		replay(reportService);

		timesheetService.getTimesheetOverview(1, cal, true);
		
		verify(timesheetDAO);
		verify(reportService);

		// timesheet entries only
		reset(timesheetDAO);
		reset(reportService);
		
		expect(timesheetDAO.getTimesheetEntriesInRange(1, DateUtil.calendarToMonthRange(cal)))
			.andReturn(daoResults);
		
		replay(timesheetDAO);
		replay(reportService);

		timesheetService.getTimesheetOverview(1, cal, false, true);
		
		verify(timesheetDAO);
		verify(reportService);		
	}
}
