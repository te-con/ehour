/**
 * Created on Feb 1, 2007
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
import net.rrm.ehour.customer.domain.Customer;
import net.rrm.ehour.project.domain.Project;
import net.rrm.ehour.project.domain.ProjectAssignment;
import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.web.report.reports.CustomerReport;

/**
 * TODO 
 **/

public class CustomerReportTest extends TestCase
{

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	public void testInitialize()
	{
		Customer	c1 = new Customer(1);
		Customer	c2 = new Customer(2);
		
		Project		p1 = new Project(1);
		p1.setCustomer(c1);
		
		Project		p2 = new Project(2);
		p2.setCustomer(c2);
		
		ProjectAssignment	pa1 = new ProjectAssignment(null, p1);
		ProjectAssignment	pa2 = new ProjectAssignment(null, p2);
		
		ProjectAssignmentAggregate pag1 = new ProjectAssignmentAggregate(pa1, 1, 1);
		ProjectAssignmentAggregate pag2 = new ProjectAssignmentAggregate(pa2, 1, 1);
		
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(pag1);
		pags.add(pag2);
		
		ReportData reportData = new ReportData(pags, null, null);
		
		CustomerReport report = new CustomerReport();
		report.initialize(reportData);
		
		assertEquals(2, report.getReportValues().size());
	}

}
