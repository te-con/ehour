/**
 * Created on Dec 31, 2007
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
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

package net.rrm.ehour.ui.report.trend;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Locale;

import net.rrm.ehour.report.reports.ReportData;
import net.rrm.ehour.report.reports.element.FlatReportElement;
import net.rrm.ehour.ui.report.panel.ReportTestUtil;

import org.junit.Test;

/**
 * Test of detailed report 
 **/
@SuppressWarnings({"unchecked"})
public class DetailedReportTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@Test
	public void testCreateDetailedReport()
	{
		DetailedReport detailedReport = new DetailedReport(ReportTestUtil.getFlatReportData(), Locale.ENGLISH);
		
		// customer = root
		assertEquals(6, detailedReport.getReportMatrix().size());
//		
//		for (ReportNode node : detailedReport.getNodes())
//		{
//			if (((Number)node.getId()).intValue() == 1)
//			{
//				assertEquals(2, node.getReportNodes().size());
//				
//				for (ReportNode projectNode : node.getReportNodes())
//				{
//					if (((Number)projectNode.getId()).intValue() == 1)
//					{
//						assertEquals(2, projectNode.getReportNodes().size());
//						
//						  ReportNode nodeEnd = projectNode.getReportNodes().get(0).getReportNodes().get(0).getReportNodes().get(0);
//						  
//						  assertNotNull( ((FlatEntryEndNode)nodeEnd).getHours());
//					}
//				}
//			}
//		}
	}
	
	@Test
	public void testCreateNullDetailedReport()
	{
		ReportData data = ReportTestUtil.getFlatReportData();
		data.setReportElements(null);
		
		DetailedReport detailedReport = new DetailedReport(data, Locale.ENGLISH);
	}
	
	@Test
	public void testCreateEmptyDetailedReport()
	{
		ReportData data = ReportTestUtil.getFlatReportData();
		
		data.setReportElements(new ArrayList<FlatReportElement>());
		DetailedReport detailedReport = new DetailedReport(data, Locale.ENGLISH);
	}	
}
