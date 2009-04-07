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
