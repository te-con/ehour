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

package net.rrm.ehour.ui.login.page;

import com.google.common.base.Optional;
import com.richemont.jira.JiraService;
import com.richemont.windchill.WindChillUpdateService;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.domain.Activity;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.sysinfo.SystemInfo;
import net.rrm.ehour.sysinfo.SystemInfoService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.ui.common.page.DummyPage;
import net.rrm.ehour.ui.common.util.AuthUtil;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest extends BaseSpringWebAppTester {

    @Mock
    private AuthUtil authUtil;

    @Mock
    private ConfigurationService configService;

    @Mock
    private SystemInfoService infoService;

    @Override
    protected AuthUtil buildAuthUtil() {
        return authUtil;
    }

    @Mock
    private WindChillUpdateService windChillUpdateService;

    @Mock
    private JiraService jiraService;

    @Before
    public void setUpDeps() throws Exception {
        getMockContext().putBean(windChillUpdateService);
        getMockContext().putBean(jiraService);
    }

    @Before
    public void setupMocks() throws Exception {
        getMockContext().putBean("configService", configService);
        getMockContext().putBean(infoService);
        getMockContext().putBean(userService);
    }

    @Test
    public void shouldLoginPageRender() {
        tester.startPage(Login.class);
        tester.assertRenderedPage(Login.class);
        tester.assertNoErrorMessage();

        User user = new User(1, "thies");
        expect(userService.getAuthorizedUser("thies")).andReturn(user);
        expect(userService.isLdapUserMemberOf("thies", "cn=timesheet-tracking,ou=people,cn=AdministrativeLdap,cn=Windchill,o=ptc")).andReturn(true);
        Map<String, Activity> activityHashMap = new HashMap<String, Activity>();
        expect(windChillService.getAllAssignedActivitiesByCode(user)).andReturn(activityHashMap);
        expect(windChillService.updateDataForUser(activityHashMap, "thies")).andReturn(true);

        when(infoService.info()).thenReturn(new SystemInfo("a", "b", "c"));
        when(authUtil.getHomepageForRole(any(Roles.class))).thenReturn(new AuthUtil.Homepage(DummyPage.class, Optional.<PageParameters>absent()));

        replay(userService, windChillService);

        FormTester form = tester.newFormTester("loginform");
        form.setValue("username", "thies");
        form.setValue("password", "Ttst");
        form.submit();

        tester.assertNoErrorMessage();
        tester.assertRenderedPage(DummyPage.class);
    }
}
