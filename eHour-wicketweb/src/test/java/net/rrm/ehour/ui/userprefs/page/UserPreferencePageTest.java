/**
 * Created on Oct 25, 2008
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

package net.rrm.ehour.ui.userprefs.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.common.MockExpectations;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;
import net.rrm.ehour.user.service.UserService;

import org.junit.Test;

public class UserPreferencePageTest extends BaseUIWicketTester
{
	@Test
	public void testReportPageRender() throws ObjectNotFoundException
	{
		TimesheetService timesheetService = createMock(TimesheetService.class);
		mockContext.putBean("timesheetService", timesheetService);
		
		MockExpectations.navCalendar(timesheetService, webapp);
		
		UserService userService = createMock(UserService.class);
		mockContext.putBean("userService", userService);

		expect(userService.getUser(1))
			.andReturn(new User(1));
		
		replay(userService);
		replay(timesheetService);
		
		tester.startPage(UserPreferencePage.class);
		tester.assertRenderedPage(UserPreferencePage.class);
		tester.assertNoErrorMessage();
		
		verify(userService);
		verify(timesheetService);
	}
}
