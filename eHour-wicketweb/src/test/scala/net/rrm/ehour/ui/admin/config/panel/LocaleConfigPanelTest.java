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

import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.config.AbstractMainConfigTest;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * Created on Apr 22, 2009, 4:19:07 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class LocaleConfigPanelTest extends AbstractMainConfigTest {
    @Test
    public void shouldSubmit() {
        startPage();

        tester.assertComponent(AbstractMainConfigTest.FORM_PATH, Form.class);

        tester.clickLink("configTabs:tabs-container:tabs:1:link", true);

        FormTester miscFormTester = tester.newFormTester(AbstractMainConfigTest.FORM_PATH);


        miscFormTester.select("config.currency", 1);
        miscFormTester.select("localeCountry", 0);
        miscFormTester.select("localeLanguage", 1);

        tester.executeAjaxEvent(AbstractMainConfigTest.FORM_PATH + ":submitButton", "onclick");

        Locale currency = (Locale)tester.getComponentFromLastRenderedPage(AbstractMainConfigTest.FORM_PATH + ":config.currency").getDefaultModelObject();
        Locale formatting = (Locale)tester.getComponentFromLastRenderedPage(AbstractMainConfigTest.FORM_PATH + ":localeCountry").getDefaultModelObject();
        assertEquals(currency, config.getCurrency());
        assertEquals(formatting, config.getFormattingLocale());
        tester.assertNoErrorMessage();

        verify(iPersistConfiguration).persistAndCleanUp(config, UserRole.ADMIN);
    }
}
