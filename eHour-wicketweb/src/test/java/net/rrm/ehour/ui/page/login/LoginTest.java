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

package net.rrm.ehour.ui.page.login;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;
import net.rrm.ehour.ui.page.admin.mainconfig.MainConfig;

import org.apache.wicket.util.tester.FormTester;

/**
 * Tests the login tests
 **/

public class LoginTest extends BaseUIWicketTester
{
	public void testLoginPageRender()
	{
		tester.startPage(Login.class);
		tester.assertRenderedPage(Login.class);
		tester.assertNoErrorMessage();

		ConfigurationService configService = createMock(ConfigurationService.class);
		mockContext.putBean("configService", configService);
		
		expect(configService.getConfiguration())
				.andReturn(new EhourConfigStub())
				.anyTimes();
		
		replay(configService);
		FormTester form = tester.newFormTester("loginform");
		form.setValue("username", "thies");
		form.setValue("password", "Ttst");

		form.submit();
		verify(configService);
		
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(MainConfig.class);

		
	}
}
