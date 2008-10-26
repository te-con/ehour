/**
 * Created on Nov 4, 2006
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

package net.rrm.ehour.timesheet.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.ProjectAssignmentType;
import net.rrm.ehour.domain.TimesheetComment;
import net.rrm.ehour.domain.TimesheetCommentId;
import net.rrm.ehour.domain.TimesheetEntry;
import net.rrm.ehour.domain.TimesheetEntryId;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.dao.TimesheetCommentDAO;
import net.rrm.ehour.timesheet.dao.TimesheetDAO;
import net.rrm.ehour.timesheet.dto.BookedDay;
import net.rrm.ehour.user.dao.CustomerFoldPreferenceDAO;
import net.rrm.ehour.util.DateUtil;

import org.junit.Before;
@SuppressWarnings({"deprecation"})
public class TimesheetServiceTest
{
	private	TimesheetService	timesheetService;
	private	TimesheetDAO		timesheetDAO;
	private TimesheetCommentDAO	timesheetCommentDAO;
	private	EhourConfig			config;
	private CustomerFoldPreferenceDAO	foldPref;
	private	AggregateReportService		aggregateReportService;
	private	ProjectAssignmentService		projectAssignmentService;
	
	/**
	 * 
	 */
	@Before
	public void setUp()
	{
		timesheetService = new TimesheetServiceImpl();

		config = createMock(EhourConfig.class);
		timesheetDAO = createMock(TimesheetDAO.class);
		aggregateReportService = createMock(AggregateReportService.class);
		timesheetCommentDAO = createMock(TimesheetCommentDAO.class);
		projectAssignmentService = createMock(ProjectAssignmentService.class);
		foldPref = createMock(CustomerFoldPreferenceDAO.class);
		
		((TimesheetServiceImpl)timesheetService).setTimesheetDAO(timesheetDAO);
		((TimesheetServiceImpl)timesheetService).setReportService(aggregateReportService);
		((TimesheetServiceImpl)timesheetService).setEhourConfig(config);
		((TimesheetServiceImpl)timesheetService).setTimesheetCommentDAO(timesheetCommentDAO);
		((TimesheetServiceImpl)timesheetService).setProjectAssignmentService(projectAssignmentService);
		((TimesheetServiceImpl)timesheetService).setCustomerFoldPreferenceDAO(foldPref);
	}
	
	/**
	 * 
	 *
	 */
	public void testGetBookedDaysMonthOverview() throws Exception
	{
		List<BookedDay>		daoResults = new ArrayList<BookedDay>();
		List<BookedDay>		results;
		BookedDay	bda, bdb;
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
				.andReturn(8f)
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
		List<AssignmentAggregateReportElement>		reportResults = new ArrayList<AssignmentAggregateReportElement>();
		Calendar	cal = new GregorianCalendar();
		
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
		
		AssignmentAggregateReportElement agg = new AssignmentAggregateReportElement();
		ProjectAssignmentType type = new ProjectAssignmentType(0);
		ProjectAssignment pa = new ProjectAssignment();
		pa.setAssignmentType(type);
		agg.setProjectAssignment(pa);
		reportResults.add(agg);
		
		expect(timesheetDAO.getTimesheetEntriesInRange(1, DateUtil.calendarToMonthRange(cal)))
				.andReturn(daoResults);
		
		expect(aggregateReportService.getHoursPerAssignmentInRange(1, DateUtil.calendarToMonthRange(cal)))
				.andReturn(reportResults);

		replay(timesheetDAO);
		replay(aggregateReportService);
		
		timesheetService.getTimesheetOverview(new User(1), cal);
		
		verify(timesheetDAO);
		verify(aggregateReportService);
	}
	
	public void testGetTimesheetEntries()
	{
		Date da = new Date(2006 - 1900, 12 - 1, 31);
		Date db = new Date(2007 - 1900, 1 - 1, 6);
		DateRange range = new DateRange(da, db);
		
		DateRange rangeB = new DateRange(new Date(2006 - 1900, 12 - 1, 31), new Date(2007 - 1900, 1 - 1, 6));

		expect(timesheetDAO.getTimesheetEntriesInRange(1, range))
				.andReturn(new ArrayList<TimesheetEntry>());
		
		expect(timesheetCommentDAO.findById(new TimesheetCommentId(1, range.getDateStart())))
			.andReturn(new TimesheetComment());
		
		expect(projectAssignmentService.getProjectAssignmentsForUser(1, rangeB))
				.andReturn(new ArrayList<ProjectAssignment>());
		
		replay(timesheetDAO);
		replay(timesheetCommentDAO);
		replay(projectAssignmentService);
		
		timesheetService.getWeekOverview(new User(1), new GregorianCalendar(2007, 1 - 1, 1), new EhourConfigStub());
		
		verify(timesheetDAO);
		verify(timesheetCommentDAO);
		verify(projectAssignmentService);
	}
}
