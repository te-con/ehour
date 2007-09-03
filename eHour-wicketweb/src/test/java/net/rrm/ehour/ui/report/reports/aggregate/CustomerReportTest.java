/**
 * Created on Jul 13, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
 */

package net.rrm.ehour.ui.report.reports.aggregate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.report.reports.ProjectAssignmentAggregate;
import net.rrm.ehour.report.reports.ReportDataAggregate;
import net.rrm.ehour.ui.common.DummyDataGenerator;

import org.junit.Before;
import org.junit.Test;

/**
 * TODO 
 **/

public class CustomerReportTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testInitializeReportDataAggregate()
	{
		ProjectAssignmentAggregate pag1 = DummyDataGenerator.getProjectAssignmentAggregate(1, 1, 1);
		ProjectAssignmentAggregate pag2 = DummyDataGenerator.getProjectAssignmentAggregate(2, 2, 1);
		
		List<ProjectAssignmentAggregate> pags = new ArrayList<ProjectAssignmentAggregate>();
		
		pags.add(pag1);
		pags.add(pag2);
		
		ReportDataAggregate reportDataAggregate = new ReportDataAggregate(pags, null, null);
		
		CustomerReport report = new CustomerReport();
		report.initialize(reportDataAggregate);
		
		assertEquals(2, report.getReportNodes().size());	
	}
}
