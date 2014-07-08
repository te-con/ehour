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
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.sysinfo.SystemInfo;
import net.rrm.ehour.sysinfo.SystemInfoService;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import net.rrm.ehour.user.service.UserService;
import org.junit.Before;

import java.io.Serializable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("serial")
public abstract class AbstractMainConfigTest extends BaseSpringWebAppTester implements Serializable {
    public static final String FORM_PATH = "configTabs:panel:border:greySquaredFrame:border_body:form";

    protected ConfigurationServiceImpl configService;
    protected MailService mailService;
    protected EhourConfigStub config;
    protected UserService userService;

    @SuppressWarnings({"deprecation"})
    @Before
    public void before() throws Exception {
        EhourHomeUtil.setEhourHome("src/test/resources");
        configService = mock(ConfigurationServiceImpl.class);/*,
                ConfigurationServiceImpl.class.getMethod("getConfiguration"),
                ConfigurationServiceImpl.class.getMethod("persistConfiguration", EhourConfig.class));*/
        getMockContext().putBean("configService", configService);

        SystemInfoService infoService = mock(SystemInfoService.class);
        getMockContext().putBean(infoService);
        when(infoService.info()).thenReturn(new SystemInfo("a", "b", "c"));

        mailService = mock(MailService.class);
        getMockContext().putBean("mailService", mailService);

        config = new EhourConfigStub();
        when(configService.getConfiguration()).thenReturn(config);

        userService = mock(UserService.class);
        getMockContext().putBean("userService", userService);

    }

    protected void startPage() {
        getTester().startPage(MainConfigPage.class);
        getTester().assertRenderedPage(MainConfigPage.class);
        getTester().assertNoErrorMessage();
    }
}
