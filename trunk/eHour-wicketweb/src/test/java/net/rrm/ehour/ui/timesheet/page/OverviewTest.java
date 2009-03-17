/**
 * Created on Sep 7, 2007
 * Created by Thies Edeling
 * Copyright (C) 2007 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.timesheet.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.notNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Calendar;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.MockExpectations;

import org.junit.Test;


/**
 * Overview page test
 **/
public class OverviewTest extends AbstractSpringWebAppTester
{
	@Test
	public void testOverviewPageRender()
	{
		TimesheetService timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);
		
		MockExpectations.navCalendar(timesheetService, webapp);

		TimesheetOverview overview = new TimesheetOverview();
		
		expect(timesheetService.getTimesheetOverview((User)notNull(), (Calendar)notNull()))
				.andReturn(overview);					

		
		replay(timesheetService);
		
		tester.startPage(Overview.class);
		tester.assertRenderedPage(Overview.class);
		tester.assertNoErrorMessage();
		
		verify(timesheetService);
	}
}
