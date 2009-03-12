/**
 * Created on 7-feb-2007
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

package net.rrm.ehour.report.criteria;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.service.ReportCriteriaService;
import net.rrm.ehour.util.DateUtil;

/**
 * TODO 
 **/

public class ReportCriteriaTest extends TestCase
{
	ReportCriteriaService 	reportCriteriaService;
	
	protected void setUp() throws Exception
	{
		super.setUp();
	}


	
	public void testGetReportRange()
	{
		DateRange dr = DateUtil.calendarToMonthRange(new GregorianCalendar());
		
		AvailableCriteria availCriteria = new AvailableCriteria();
		ReportCriteria reportCriteria = new ReportCriteria(availCriteria);

		availCriteria.setReportRange(dr);
		
		assertEquals(dr, reportCriteria.getReportRange());
	}

//	
//	public void testSetUserCriteria()
//	{
//		expect(reportCriteriaService.syncUserReportCriteria(reportCriteria, ReportCriteria.UPDATE_ALL)).andReturn(reportCriteria);
//
//		replay(reportCriteriaService);
//		
//		reportCriteria.setUserCriteria(new UserCriteria());
//		reportCriteria.updateAvailableCriteria();
//		
//		verify(reportCriteriaService);
//	}

}
