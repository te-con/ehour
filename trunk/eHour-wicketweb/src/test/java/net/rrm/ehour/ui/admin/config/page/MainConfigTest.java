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

package net.rrm.ehour.ui.admin.config.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.junit.Test;

/**
 * TODO 
 **/

public class MainConfigTest extends AbstractSpringWebAppTester
{
	/**
	 * Test render
	 */
	@Test
	public void testMainConfigRender()
	{
		ConfigurationService configService = createMock(ConfigurationService.class);
		getMockContext().putBean("configService", configService);
		

		MailService mailService = createMock(MailService.class);
		getMockContext().putBean("mailService", mailService);	
		
		expect(configService.getConfiguration())
				.andReturn(new EhourConfigStub());

		replay(configService);
		
		getTester().startPage(MainConfig.class);
		getTester().assertRenderedPage(MainConfig.class);
		getTester().assertNoErrorMessage();
		
		verify(configService);
	}
//	
//	/**
//	 * 
////	 */
//	public void testSubmitOKNoLocale()
//	{
//		FormTester	form = getTester().newFormTester("configForm");
//	
//		form.setValue("dontForceLocale", "true");
//		
//		getTester().executeAjaxEvent("configForm.submitButton", "onclick");
//	}
	
}
