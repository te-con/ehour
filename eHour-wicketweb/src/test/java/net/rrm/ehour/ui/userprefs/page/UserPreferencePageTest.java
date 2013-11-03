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

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.timesheet.service.IOverviewTimesheet;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.MockExpectations;
import net.rrm.ehour.user.service.UserService;
import org.junit.Test;

import static org.easymock.EasyMock.*;

public class UserPreferencePageTest extends BaseSpringWebAppTester
{
	@Test
	public void shouldRenderPreferencePage() throws ObjectNotFoundException
	{
		IOverviewTimesheet overviewTimesheet = createMock(IOverviewTimesheet.class);
		getMockContext().putBean(overviewTimesheet);

		MockExpectations.navCalendar(overviewTimesheet, getWebApp());

		UserService userService = createMock(UserService.class);
		getMockContext().putBean("userService", userService);

		replay(userService);
		replay(overviewTimesheet);

		tester.startPage(UserPreferencePage.class);
		tester.assertRenderedPage(UserPreferencePage.class);
		tester.assertNoErrorMessage();

		verify(userService);
		verify(overviewTimesheet);
	}

    @Test
    public void shouldChangePasswordForUserWithPm12() throws ObjectNotFoundException
    {
        IOverviewTimesheet overviewTimesheet = createMock(IOverviewTimesheet.class);
        getMockContext().putBean(overviewTimesheet);

        MockExpectations.navCalendar(overviewTimesheet, getWebApp());

        UserService userService = createMock(UserService.class);
        getMockContext().putBean("userService", userService);

        replay(userService);
        replay(overviewTimesheet);

        tester.startPage(UserPreferencePage.class);
        tester.assertRenderedPage(UserPreferencePage.class);
        tester.assertNoErrorMessage();

        verify(userService);
        verify(overviewTimesheet);
    }

}
