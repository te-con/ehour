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

package net.rrm.ehour.ui.admin.config;

import net.rrm.ehour.appconfig.EhourHomeUtil;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationServiceImpl;
import net.rrm.ehour.config.service.IPersistConfiguration;
import net.rrm.ehour.mail.service.MailMan;
import net.rrm.ehour.sysinfo.SystemInfo;
import net.rrm.ehour.sysinfo.SystemInfoService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Serializable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractMainConfigTest extends BaseSpringWebAppTester implements Serializable {
    public static final String FORM_PATH = "configTabs:panel:border:greySquaredFrame:border_body:form";

    @Mock
    protected ConfigurationServiceImpl configService;

    @Mock
    protected MailMan mailMan;

    protected EhourConfigStub config;

    @Mock
    protected UserService userService;

    @Mock
    protected IPersistConfiguration iPersistConfiguration;

    @Before
    public void before() throws Exception {
        EhourHomeUtil.setEhourHome("src/test/resources");

        config = new EhourConfigStub();

        when(configService.getConfiguration()).thenReturn(config);
        getMockContext().putBean("configService", configService);

        SystemInfoService infoService = mock(SystemInfoService.class);
        getMockContext().putBean(infoService);
        when(infoService.info()).thenReturn(new SystemInfo("a", "b", "c"));

        getMockContext().putBean("mailMan", mailMan);
        getMockContext().putBean("userService", userService);
        getMockContext().putBean("configurationPersistence", iPersistConfiguration);

    }

    protected void startPage() {
        getTester().startPage(MainConfigPage.class);
        getTester().assertRenderedPage(MainConfigPage.class);
        getTester().assertNoErrorMessage();
    }
}
