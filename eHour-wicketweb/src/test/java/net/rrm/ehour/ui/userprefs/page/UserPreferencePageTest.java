/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.userprefs.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.service.exception.ObjectNotFoundException;
import net.rrm.ehour.service.timesheet.service.TimesheetService;
import net.rrm.ehour.service.user.service.UserService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;
import net.rrm.ehour.ui.common.MockExpectations;

import org.junit.Test;

public class UserPreferencePageTest extends AbstractSpringWebAppTester
{
	@Test
	public void testReportPageRender() throws ObjectNotFoundException
	{
		TimesheetService timesheetService = createMock(TimesheetService.class);
		getMockContext().putBean("timesheetService", timesheetService);
		
		MockExpectations.navCalendar(timesheetService, getWebApp());
		
		UserService userService = createMock(UserService.class);
		getMockContext().putBean("userService", userService);

		expect(userService.getUser(1))
			.andReturn(new User(1));
		
		replay(userService);
		replay(timesheetService);
		
		getTester().startPage(UserPreferencePage.class);
		getTester().assertRenderedPage(UserPreferencePage.class);
		getTester().assertNoErrorMessage();
		
		verify(userService);
		verify(timesheetService);
	}
}
