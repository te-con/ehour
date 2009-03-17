/**
 * Created on Jul 9, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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
