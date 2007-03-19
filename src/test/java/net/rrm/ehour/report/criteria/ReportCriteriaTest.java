/**
 * Created on 7-feb-2007
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

package net.rrm.ehour.report.criteria;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Date;

import junit.framework.TestCase;
import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.report.service.ReportCriteriaService;

/**
 * TODO 
 **/

public class ReportCriteriaTest extends TestCase
{
	ReportCriteriaService 	reportCriteriaService;
	ReportCriteria	reportCriteria;
	
	protected void setUp() throws Exception
	{
		super.setUp();
		reportCriteria = new ReportCriteria();
		
		reportCriteriaService = createMock(ReportCriteriaService.class);
		reportCriteria.setReportCriteriaService(reportCriteriaService);
	}


	
	public void testGetReportRange()
	{
		DateRange dr = new DateRange(new Date(), new Date());
		
		reportCriteria.setUserCriteria(new UserCriteria());
		AvailableCriteria availCriteria = new AvailableCriteria();
		availCriteria.setReportRange(dr);
		reportCriteria.setAvailableCriteria(availCriteria);
		
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
