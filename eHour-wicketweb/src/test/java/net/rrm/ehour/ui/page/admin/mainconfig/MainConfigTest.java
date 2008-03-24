/**
 * Created on Jul 17, 2007
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

package net.rrm.ehour.ui.page.admin.mainconfig;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.ui.common.BaseUIWicketTester;

import org.apache.wicket.util.tester.FormTester;

/**
 * TODO 
 **/

public class MainConfigTest extends BaseUIWicketTester
{
	/**
	 * Test render
	 */
	public void testMainConfigRender()
	{
		ConfigurationService configService = createMock(ConfigurationService.class);
		mockContext.putBean("configService", configService);
		

		MailService mailService = createMock(MailService.class);
		mockContext.putBean("mailService", mailService);	
		
		expect(configService.getConfiguration())
				.andReturn(new EhourConfigStub());

		replay(configService);
		
		tester.startPage(MainConfig.class);
		tester.assertRenderedPage(MainConfig.class);
		tester.assertNoErrorMessage();
		
		verify(configService);
	}
//	
//	/**
//	 * 
////	 */
//	public void testSubmitOKNoLocale()
//	{
//		FormTester	form = tester.newFormTester("configForm");
//	
//		form.setValue("dontForceLocale", "true");
//		
//		tester.executeAjaxEvent("configForm.submitButton", "onclick");
//	}
	
}
