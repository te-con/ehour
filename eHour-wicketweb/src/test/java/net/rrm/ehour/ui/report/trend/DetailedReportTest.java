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

import java.util.Locale;

import net.rrm.ehour.ui.panel.report.ReportTestUtil;
import net.rrm.ehour.ui.report.node.ReportNode;

import org.junit.Test;

/**
 * Test of detailed report 
 **/

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
		assertEquals(2, detailedReport.getNodes().size());
		
		for (ReportNode node : detailedReport.getNodes())
		{
			if (((Number)node.getId()).intValue() == 1)
			{
				assertEquals(2, node.getReportNodes().size());
				
				for (ReportNode projectNode : node.getReportNodes())
				{
					if (((Number)projectNode.getId()).intValue() == 1)
					{
						assertEquals(2, projectNode.getReportNodes().size());
					}
				}
			}
		}
	}
}
