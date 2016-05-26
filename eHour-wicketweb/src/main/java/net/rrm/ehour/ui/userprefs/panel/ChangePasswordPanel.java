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

package net.rrm.ehour.ui.userprefs.panel;

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.ValidationError;
import org.springframework.security.authentication.BadCredentialsException;

import static net.rrm.ehour.ui.common.event.CommonAjaxEventType.SUBMIT_ERROR;
import static net.rrm.ehour.ui.manage.user.UserManageAjaxEventType.PASSWORD_CHANGED;

/**
 * User preferences form
 */

public class ChangePasswordPanel extends AbstractFormSubmittingPanel<ChangePasswordBackingBean> {
    private static final long serialVersionUID = 7670153126514499168L;
    protected static final String CHANGE_PASSWORD_FORM = "changePasswordForm";
    protected static final String BORDER = "border";

    @SpringBean
    private UserService userService;

    private Form<ChangePasswordBackingBean> form;
    private final PasswordTextField currentPasswordField;

    @SuppressWarnings({"unchecked"})
    public ChangePasswordPanel(String id, ChangePasswordBackingBean changePasswordBackingBean) throws ObjectNotFoundException {
        super(id, new Model<>(changePasswordBackingBean));

        Border greyBorder = new GreyRoundedBorder(BORDER, new ResourceModel("userprefs.title"));
        add(greyBorder);

        setOutputMarkupId(true);

        form = new Form<>(CHANGE_PASSWORD_FORM, (IModel<ChangePasswordBackingBean>) getDefaultModel());
        form.setOutputMarkupId(true);

        // password inputs
        PasswordFieldFactory.createPasswordFields(form, new PropertyModel<String>(getDefaultModel(), "password"));

        // current password input
        currentPasswordField = new PasswordTextField("currentPassword", new PropertyModel<String>(getDefaultModel(), "currentPassword"));

        currentPasswordField.setRequired(true);

        form.add(currentPasswordField);
        form.add(new AjaxFormComponentFeedbackIndicator("currentPasswordValidationError", currentPasswordField));


        // data save label
        WebComponent serverMessage = createEmptyServerMessage();
        form.add(serverMessage);


        FormConfig formConfig = FormConfig.forForm(form).withSubmitTarget(this)
                .withSubmitEventType(PASSWORD_CHANGED)
                .withErrorEventType(SUBMIT_ERROR);

        FormUtil.setSubmitActions(formConfig);

        greyBorder.add(form);
    }

    @Override
    protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        ChangePasswordBackingBean bean = (ChangePasswordBackingBean) backingBean;

        if (type == PASSWORD_CHANGED) {
            try {
                userService.changePassword(bean.getUsername(), bean.getCurrentPassword(), bean.getPassword());

                Label replacementLabel = new Label("serverMessage", new ResourceModel("userprefs.saved"));
                replacementLabel.setOutputMarkupId(true);
                replacementLabel.add(AttributeModifier.replace("class", "smallText"));

                form.addOrReplace(replacementLabel);
                target.add(replacementLabel);
                target.add(form);
            } catch (BadCredentialsException bce) {
                processFormSubmitError(target);

                currentPasswordField.error(new ValidationError().addKey("user.invalidCurrentPassword"));
                target.add(form);
            }
        }
        return false;
    }

    @Override
    protected boolean processFormSubmitError(AjaxRequestTarget target) {
        WebComponent emptyServerMessage = createEmptyServerMessage();
        form.addOrReplace(emptyServerMessage);
        target.add(emptyServerMessage);

        return false;
    }

    private WebComponent createEmptyServerMessage() {
        WebComponent serverMessage = new WebComponent("serverMessage");
        serverMessage.setOutputMarkupId(true);
        return serverMessage;
    }
}
