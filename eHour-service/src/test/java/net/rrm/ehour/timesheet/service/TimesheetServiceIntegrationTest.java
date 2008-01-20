/**
 * Created on Dec 17, 2006
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

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.rrm.ehour.dao.BaseDAOTest;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.report.criteria.AvailableCriteria;
import net.rrm.ehour.report.criteria.ReportCriteria;
import net.rrm.ehour.report.criteria.UserCriteria;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.timesheet.dto.BookedDay;

/**
 * TODO 
 **/

public class TimesheetServiceIntegrationTest extends BaseDAOTest
{
	private TimesheetService	timesheetService;
	private	AggregateReportService		aggregateReportService;
	

	/**
	 * Test method for {@link net.rrm.ehour.timesheet.service.TimesheetServiceImpl#getBookedDaysMonthOverview(java.lang.Integer, java.util.Calendar)}.
	 */
	public void testGetBookedDaysMonthOverview() throws ObjectNotFoundException
	{
		Calendar	cal = new GregorianCalendar(2006, 10 - 1, 1);
		List<BookedDay> res = timesheetService.getBookedDaysMonthOverview(1, cal);
		
		assertEquals(8.0, res.get(0).getHours());
		assertEquals(9.0, res.get(2).getHours());
		assertEquals(9.2f, res.get(3).getHours().floatValue(), 0.1f);
	}



	/**
	 * @param timesheetService the timesheetService to set
	 */
	public void setTimesheetService(TimesheetService timesheetService)
	{
		this.timesheetService = timesheetService;
	}

	
	protected String[] getConfigLocations()
	{
		return new String[] { "classpath:/applicationContext-datasource.xml",
							  "classpath:/applicationContext-dao.xml",
							  "classpath:/applicationContext-mail.xml", 
							  "classpath:/applicationContext-service.xml"};	
	}


//	public void testReport()
//	{
//		ReportCriteria criteria = new ReportCriteria();
//		UserCriteria	uc = new UserCriteria();
//		uc.setSingleUser(true);
//		uc.setUserIds(new Integer[]{1});
//		criteria.setUserCriteria(uc);
//		
//		AvailableCriteria ac = new AvailableCriteria();
//		ac.setReportRange(new DateRange(new Date(2006 - 1900, 2 - 1, 1), new Date(2008 - 1900, 1, 1)));
//		criteria.setAvailableCriteria(ac);
//		
//		reportService.createReportData(criteria);
//	}
	
	/**
	 * @param aggregateReportService the reportService to set
	 */
	public void setReportService(AggregateReportService aggregateReportService)
	{
		this.aggregateReportService = aggregateReportService;
	}
}
