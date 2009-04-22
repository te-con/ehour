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

package net.rrm.ehour.ui.admin.config.page;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationService;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;

public class MainConfigTest extends AbstractSpringWebAppTester
{

	
	private ConfigurationService configService;
	private MailService mailService;

	@Before
	public void setUp() throws Exception
	{
		super.setUp();
		
		configService = createMock(ConfigurationService.class);
		getMockContext().putBean("configService", configService);

		mailService = createMock(MailService.class);
		getMockContext().putBean("mailService", mailService);	
	}
	
	@Test
	public void testMainConfigRender()
	{
		EhourConfigStub config = new EhourConfigStub();
		expect(configService.getConfiguration())
				.andReturn(config);

		configService.persistConfiguration(config);
		
		replay(configService);
		
		startPage();
		
		getTester().assertComponent("configTabs:panel:border:form", Form.class);
		
		FormTester miscFormTester = getTester().newFormTester("configTabs:panel:border:form");
		
		miscFormTester.setValue("config.completeDayHours", "4");
		
		getTester().executeAjaxEvent("configTabs:panel:border:form:submitButton", "onclick");
	
		verify(configService);
		
		assertEquals(4f, config.getCompleteDayHours(), 0.001);
	}



	/**
	 * 
	 */
	private void startPage()
	{
		getTester().startPage(MainConfig.class);
		getTester().assertRenderedPage(MainConfig.class);
		getTester().assertNoErrorMessage();
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
