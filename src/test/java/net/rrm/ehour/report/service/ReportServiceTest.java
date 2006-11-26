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

package net.rrm.ehour.report.service;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.report.dao.ReportDAO;
import net.rrm.ehour.report.dto.ProjectReport;
import net.rrm.ehour.util.DateUtil;

/**
 *  
 **/

public class ReportServiceTest extends TestCase
{
	private	ReportService	reportService;
	private	ReportDAO		reportDAO;
	
	/**
	 * 
	 */
	protected void setUp()
	{
		reportService = new ReportServiceImpl();

		reportDAO = createMock(ReportDAO.class);
		((ReportServiceImpl)reportService).setReportDAO(reportDAO);
	}
	
	/**
	 * 
	 *
	 */
	
	public void testGetHoursPerAssignmentOnMonth()
	{
		List<ProjectReport>	results = new ArrayList<ProjectReport>();
		Integer		userId = 1;
		Calendar	cal = new GregorianCalendar();
		
		results.add(new ProjectReport());
		
		reportDAO.getCumulatedHoursPerAssignmentForUser(userId, DateUtil.calendarToMonthRange(cal));
		expectLastCall().andReturn(results);
		
		replay();
		
		reportService.getHoursPerAssignmentInMonth(userId, cal);
		
		verify();
	}

}
