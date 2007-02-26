/**
 * Created on Feb 22, 2007
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

package net.rrm.ehour.web.report.reports;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import net.rrm.ehour.DummyDataGenerator;
import net.rrm.ehour.user.domain.User;
import net.rrm.ehour.web.report.reports.UserReport;

/**
 * TODO 
 **/

public class UserReportTest extends TestCase
{

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Test method for {@link net.rrm.ehour.web.report.reports.UserReport#initialize(net.rrm.ehour.report.reports.ReportData)}.
	 */
	public void testInitialize()
	{
		ReportData	reportData = new ReportData();
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		User user = DummyDataGenerator.getUser();
		user.setUserId(1);
		
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 2));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(3, 2, 1));
		pags.add(DummyDataGenerator.getProjectAssignmentAggregate(4, 3, 1));
		
		reportData.setProjectAssignmentAggregates(pags);
		
		UserReport report = new UserReport();
		report.initialize(reportData);

		assertEquals(2, report.getReportValues().keySet().size());
		assertEquals(3, report.getReportValues().get(user).size());
	}

}
