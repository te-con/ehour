/**
 * Created on Sep 15, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.session;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.rrm.ehour.ui.report.Report;

import org.junit.Test;
/**
 * TODO 
 **/

public class ReportCacheTest
{

	@Test
	public void testAddReportToCache()
	{
		ReportCache reportCache = new ReportCache();

		MockReport report = new MockReport();
		long id = report.id;
		String cacheId = reportCache.addReportToCache(report, null);
		
		report = null;
		
		report = (MockReport)reportCache.getReportFromCache(cacheId);
		
		assertEquals(id, report.id);
	}
	
	class MockReport extends Report
	{
		private static final long serialVersionUID = 1L;
		long id = new Date().getTime();
	}
}
