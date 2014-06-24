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

package net.rrm.ehour.ui.admin.config.panel;

import net.rrm.ehour.mail.service.MailService;
import net.rrm.ehour.ui.admin.config.dto.MainConfigBackingBean;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.RangeValidator;


public class MailServerConfigPanel extends AbstractConfigPanel {
    private static final long serialVersionUID = 1500312866540540312L;

    @SpringBean
    private MailService mailService;

    public MailServerConfigPanel(String id, IModel<MainConfigBackingBean> model) {
        super(id, model);
    }

    @Override
    protected void addFormComponents(Form<?> form) {
        // reply sender
        RequiredTextField<String> mailFrom = new RequiredTextField<String>("config.mailFrom");
        mailFrom.setLabel(new ResourceModel("admin.config.mailFrom"));
        mailFrom.add(EmailAddressValidator.getInstance());
        mailFrom.add(new ValidatingFormComponentAjaxBehavior());
        form.add(mailFrom);
        form.add(new AjaxFormComponentFeedbackIndicator("mailFromError", mailFrom));

        // smtp server, port, username, pass

        TextField<String> mailSmtp = new RequiredTextField<String>("config.mailSmtp");
        mailSmtp.setLabel(new ResourceModel("admin.config.mailSmtp"));
        mailSmtp.add(new ValidatingFormComponentAjaxBehavior());
        form.add(new AjaxFormComponentFeedbackIndicator("mailSmtpValidationError", mailSmtp));
        form.add(mailSmtp);

        TextField<Integer> smtpPort = new RequiredTextField<Integer>("config.smtpPort");
        smtpPort.setLabel(new ResourceModel("admin.config.smtpPort"));
        smtpPort.add(new ValidatingFormComponentAjaxBehavior());
        form.add(new AjaxFormComponentFeedbackIndicator("smtpPortValidationError", mailSmtp));
        smtpPort.setType(Integer.class);
        smtpPort.add(RangeValidator.minimum(0));
        form.add(smtpPort);

        TextField<String> smtpUsername = new TextField<String>("config.smtpUsername");
        smtpUsername.setLabel(new ResourceModel("admin.config.smtpUsername"));
        form.add(smtpUsername);

        TextField<String> smtpPassword = new PasswordTextField("config.smtpPassword");
        smtpPassword.setRequired(false);
        smtpPassword.setLabel(new ResourceModel("admin.config.smtpPassword"));
        form.add(smtpPassword);
        addTestMailSettingsButton(form);
    }

    /**
     * Add test mail button
     *
     * @param form
     */
    private void addTestMailSettingsButton(Form<?> form) {
        form.add(new AjaxButton("testMail", form) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                MainConfigBackingBean configBackingBean = (MainConfigBackingBean) MailServerConfigPanel.this.getDefaultModelObject();

                mailService.mailTestMessage(configBackingBean.getConfig());

                Label replacementLabel = new Label("serverMessage", new ResourceModel("admin.config.testSmtpSent"));
                replacementLabel.setOutputMarkupId(true);
                replacementLabel.add(AttributeModifier.replace("class", "whiteText"));
                getServerMessage().replaceWith(replacementLabel);
                setServerMessage(replacementLabel);
                target.add(replacementLabel);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                //
            }
        });
    }
}
