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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.ui.admin.config.page.MainConfig;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

/**
 * Tests the login tests
 **/

public class LoginTest extends AbstractSpringWebAppTester
{
	@Test
	public void testLoginPageRender()
	{
		getTester().startPage(Login.class);
		getTester().assertRenderedPage(Login.class);
		getTester().assertNoErrorMessage();

		ConfigurationService configService = createMock(ConfigurationService.class);
		getMockContext().putBean("configService", configService);

		MailService mailService = createMock(MailService.class);
		getMockContext().putBean("mailService", mailService);

		
		expect(configService.getConfiguration())
				.andReturn(new EhourConfigStub())
				.anyTimes();
		
		replay(configService);
		FormTester form = getTester().newFormTester("loginform");
		form.setValue("username", "thies");
		form.setValue("password", "Ttst");

		form.submit();
		verify(configService);
		
		getTester().assertNoErrorMessage();
		getTester().assertRenderedPage(MainConfig.class);

		
	}
}
