/**
 * Created on Jul 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
import net.rrm.ehour.ui.common.BaseUITest;

import org.apache.wicket.util.tester.FormTester;

/**
 * TODO 
 **/

public class MainConfigTest extends BaseUITest
{
	/**
	 * Test render
	 */
	public void testMainConfigRender()
	{
		ConfigurationService configService = createMock(ConfigurationService.class);
		mockContext.putBean("configService", configService);
		
		expect(configService.getConfiguration())
				.andReturn(new EhourConfigStub());

		replay(configService);
		
		tester.startPage(MainConfig.class);
		tester.assertRenderedPage(MainConfig.class);
		tester.assertNoErrorMessage();
		
		verify(configService);
//	}
//	
//	/**
//	 * 
//	 */
////	public void testSubmitOKNoLocale()
////	{
//		FormTester	form = tester.newFormTester("configForm");
//	
//		form.setValue("dontForceLocale", "true");
//		
//		tester.executeAjaxEvent("configForm.submitButton", "onclick");
//	}
	
}
