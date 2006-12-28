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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.dto.ProjectReport;
import net.rrm.ehour.report.service.ReportService;
import net.rrm.ehour.timesheet.dao.TimesheetCommentDAO;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.timesheet.domain.TimesheetComment;
import net.rrm.ehour.timesheet.domain.TimesheetEntry;
import net.rrm.ehour.timesheet.domain.TimesheetEntryId;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.util.DateUtil;

public class TimesheetServiceTest  extends TestCase
{
	private	TimesheetService	timesheetService;
	private	TimesheetDAO		timesheetDAO;
	private TimesheetCommentDAO	timesheetCommentDAO;
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
		timesheetCommentDAO = createMock(TimesheetCommentDAO.class);
		
		((TimesheetServiceImpl)timesheetService).setTimesheetDAO(timesheetDAO);
		((TimesheetServiceImpl)timesheetService).setReportService(reportService);
		((TimesheetServiceImpl)timesheetService).setEhourConfig(config);
		((TimesheetServiceImpl)timesheetService).setTimesheetCommentDAO(timesheetCommentDAO);
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
	}
	
	public void testGetTimesheetEntries()
	{
		Date da = new Date(2006 - 1900, 1, 1);
		Date db = new Date(2006 - 1900, 2, 1);
		DateRange range = new DateRange(da, db);
		
		expect(timesheetDAO.getTimesheetEntriesInRange(1, range))
				.andReturn(new ArrayList<TimesheetEntry>());
		
		expect(timesheetCommentDAO.findForUserInRage(1, range))
			.andReturn(new ArrayList<TimesheetComment>());
		
		replay(timesheetDAO);
		replay(timesheetCommentDAO);
		
		timesheetService.getWeekOverview(1, range);
		
		verify(timesheetDAO);
		verify(timesheetCommentDAO);
	}
}
