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

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.verify;

import java.io.Serializable;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.config.service.ConfigurationServiceImpl;
import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.ui.common.AbstractSpringWebAppTester;

import org.junit.After;
import org.junit.Before;

@SuppressWarnings("serial")
public abstract class AbstractMainConfigTest extends AbstractSpringWebAppTester implements Serializable
{
	protected ConfigurationServiceImpl configService;
	private MailService mailService;
	private EhourConfigStub config;

	@Before
	public void before() throws Exception
	{
//		configService = createMock(ConfigurationService.class);
		configService = createMock(ConfigurationServiceImpl.class, 
				ConfigurationServiceImpl.class.getMethod("getConfiguration", null),
				ConfigurationServiceImpl.class.getMethod("persistConfiguration", EhourConfig.class));
		getMockContext().putBean("configService", configService);

		mailService = createMock(MailService.class);
		getMockContext().putBean("mailService", mailService);	

		
		
		config = new EhourConfigStub();
		expect(configService.getConfiguration())
				.andReturn(config);
	}

	@After
	public void tearDown()
	{
		verify(configService);
	}

	protected void startPage()
	{
		getTester().startPage(MainConfigPage.class);
		getTester().assertRenderedPage(MainConfigPage.class);
		getTester().assertNoErrorMessage();
	}
	
	protected EhourConfigStub getConfigStub()
	{
		return config;
	}
	
	protected ConfigurationServiceImpl getConfigService()
	{
		return configService;
	}
	
	protected MailService getMailService()
	{
		return mailService;
	}
}
