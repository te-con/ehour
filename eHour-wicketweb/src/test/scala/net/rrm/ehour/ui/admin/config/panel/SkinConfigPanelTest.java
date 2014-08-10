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


import net.rrm.ehour.persistence.value.ImageLogo;
import net.rrm.ehour.ui.admin.config.AbstractMainConfigTest;
import org.apache.wicket.markup.html.form.Form;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on Apr 22, 2009, 4:19:36 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class SkinConfigPanelTest extends AbstractMainConfigTest {
    @Test
    public void shouldSubmit() {
        when(configService.getExcelLogo()).thenReturn(new ImageLogo());
        startPage();

        tester.assertComponent(AbstractMainConfigTest.FORM_PATH, Form.class);

        tester.clickLink("configTabs:tabs-container:tabs:" + ConfigTab.SKIN.getTabIndex() + ":link", true);

        verify(configService).getExcelLogo();
    }
}
