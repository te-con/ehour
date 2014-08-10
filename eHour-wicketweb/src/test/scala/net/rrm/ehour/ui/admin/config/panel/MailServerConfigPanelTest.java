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


import net.rrm.ehour.config.EhourConfigStub;
import net.rrm.ehour.mail.service.MailMan;
import net.rrm.ehour.ui.admin.config.MainConfigBackingBean;
import net.rrm.ehour.ui.common.BaseSpringWebAppTester;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created on Apr 22, 2009, 4:19:23 PM
 *
 * @author Thies Edeling (thies@te-con.nl)
 */
public class MailServerConfigPanelTest extends BaseSpringWebAppTester {

    private MailMan mailMan;

    @Before
    public void init_mocks() throws Exception {
        mailMan = mock(MailMan.class);

        getMockContext().putBean(mailMan);
    }

    @Test
    public void should_send_test_email() {
        EhourConfigStub config = new EhourConfigStub();
        config.setSmtpUsername("smtpUser");
        config.setSmtpPassword("smtpPassword");

        MainConfigBackingBean bean = new MainConfigBackingBean(config);
        bean.setSmtpAuthentication(true);

        tester.startComponentInPage(new MailServerConfigPanel("id", new CompoundPropertyModel<MainConfigBackingBean>(bean)));

        FormTester formTester = tester.newFormTester("id:smtpForm");

        formTester.setValue("config.mailFrom", "thies@thies.net");
        formTester.setValue("config.mailSmtp", "localhost");
        formTester.setValue("config.smtpPort", "25");

        tester.executeAjaxEvent("id:smtpForm:testMail", "onclick");

        assertEquals("thies@thies.net", config.getMailFrom());
        assertEquals("localhost", config.getMailSmtp());
        assertEquals("25", config.getSmtpPort());
        assertEquals("smtpPassword", config.getSmtpPassword());

        verify(mailMan).sendTestMail(config);
    }
}
