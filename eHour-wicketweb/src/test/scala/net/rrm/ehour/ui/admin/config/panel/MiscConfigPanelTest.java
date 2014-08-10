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

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.UserObjectMother;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.config.AbstractMainConfigTest;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created on Apr 22, 2009, 4:20:02 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
@SuppressWarnings("serial")
public class MiscConfigPanelTest extends AbstractMainConfigTest {
    @Test
    public void should_submit() {
        startPage();

        tester.assertComponent(AbstractMainConfigTest.FORM_PATH, Form.class);

        FormTester miscFormTester = tester.newFormTester(AbstractMainConfigTest.FORM_PATH);

        miscFormTester.setValue("config.completeDayHours", "4");
        miscFormTester.setValue("config.splitAdminRole", "true");

        tester.executeAjaxEvent(AbstractMainConfigTest.FORM_PATH + ":submitButton", "onclick");

        tester.assertNoErrorMessage();

        assertEquals(4f, config.getCompleteDayHours(), 0.001);
        assertTrue(config.isSplitAdminRole());

        verify(iPersistConfiguration).persistAndCleanUp(config, UserRole.ADMIN);
    }

    @Test
    public void should_show_convert_to_dropdown_when_manager_is_disabled() {
        startPage();

        FormTester miscFormTester = tester.newFormTester(AbstractMainConfigTest.FORM_PATH);
        miscFormTester.setValue("config.splitAdminRole", false);

        when(userService.getUsers(UserRole.MANAGER)).thenReturn(Lists.newArrayList(UserObjectMother.createUser()));

        tester.executeAjaxEvent(AbstractMainConfigTest.FORM_PATH + ":config.splitAdminRole", "click");

        tester.assertVisible(AbstractMainConfigTest.FORM_PATH + ":convertManagers:convertManagersTo");
        tester.assertNoErrorMessage();
    }
}
