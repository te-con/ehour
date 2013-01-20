/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * 
 * eHour is sponsored by TE-CON  - http://www.te-con.nl/
 */

package net.rrm.ehour.ui.admin.config.panel;


import net.rrm.ehour.persistence.config.dao.BinaryConfigurationDao;
import net.rrm.ehour.ui.admin.config.page.AbstractMainConfigTest;
import org.apache.wicket.markup.html.form.Form;
import org.junit.Test;

import static org.easymock.EasyMock.*;

/**
 * Created on Apr 22, 2009, 4:19:36 PM
 * @author Thies Edeling (thies@te-con.nl) 
 *
 */
@SuppressWarnings("serial")
public class SkinConfigPanelTest extends AbstractMainConfigTest
{
	@Test
	public void shouldSubmit()
	{
		BinaryConfigurationDao binConfigDao = createMock(BinaryConfigurationDao.class);

		getConfigService().setBinConfigDAO(binConfigDao);

		expect(binConfigDao.findById("excelHeaderLogo"))
			.andReturn(null)
			.anyTimes();
		
		
		replay(binConfigDao);
		
		getConfigService().getExcelLogo();
		
		replay(getConfigService());
		replay(getMailService());
		
		startPage();
		
		tester.assertComponent(FORM_PATH, Form.class);
		
		tester.clickLink("configTabs:tabs-container:tabs:3:link", true);
		
		verify(getMailService());
		verify(binConfigDao);
	}
}
