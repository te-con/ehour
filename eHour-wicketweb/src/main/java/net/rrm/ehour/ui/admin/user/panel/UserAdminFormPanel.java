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

package net.rrm.ehour.ui.admin.user.panel;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean;
import net.rrm.ehour.ui.common.AdminAction;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.form.FormConfig;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.renderers.UserRoleRenderer;
import net.rrm.ehour.ui.userprefs.panel.PasswordFieldFactory;
import net.rrm.ehour.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.List;

/**
 * User Form Panel for admin
 */

public class UserAdminFormPanel extends AbstractFormSubmittingPanel<UserBackingBean> {
    private static final long serialVersionUID = -7427807216389657732L;
    protected static final String BORDER = "border";
    protected static final String FORM = "userForm";

    @SpringBean
    private UserService userService;

    public UserAdminFormPanel(String id,
                              CompoundPropertyModel<UserBackingBean> userModel,
                              List<UserRole> roles,
                              List<UserDepartment> departments) {
        super(id, userModel);

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder(BORDER);
        add(greyBorder);

        setOutputMarkupId(true);

        final Form<UserBackingBean> form = new Form<UserBackingBean>(FORM, userModel);

        // username
        RequiredTextField<String> usernameField = new RequiredTextField<String>("user.username");
        form.add(usernameField);
        usernameField.add(new StringValidator.MaximumLengthValidator(32));
        usernameField.add(new DuplicateUsernameValidator());
        usernameField.setLabel(new ResourceModel("admin.user.username"));
        usernameField.add(new ValidatingFormComponentAjaxBehavior());
        form.add(new AjaxFormComponentFeedbackIndicator("userValidationError", usernameField));

        // user info
        TextField<String> firstNameField = new TextField<String>("user.firstName");
        form.add(firstNameField);

        TextField<String> lastNameField = new RequiredTextField<String>("user.lastName");
        form.add(lastNameField);
        lastNameField.setLabel(new ResourceModel("admin.user.lastName"));
        lastNameField.add(new ValidatingFormComponentAjaxBehavior());
        form.add(new AjaxFormComponentFeedbackIndicator("lastNameValidationError", lastNameField));

        form.add(new EmailInputSnippet("email"));

        // password
        Label label = new Label("passwordEditLabel", new ResourceModel("admin.user.editPassword"));
        label.setVisible(userModel.getObject().getAdminAction() == AdminAction.EDIT);
        form.add(label);

        PasswordFieldFactory.createOptionalPasswordFields(form, new PropertyModel<String>(userModel, "user.password"));

        // department
        DropDownChoice<UserDepartment> userDepartment = new DropDownChoice<UserDepartment>("user.userDepartment", departments, new ChoiceRenderer<UserDepartment>("name"));
        userDepartment.setRequired(true);
        userDepartment.setLabel(new ResourceModel("admin.user.department"));
        userDepartment.add(new ValidatingFormComponentAjaxBehavior());
        form.add(userDepartment);
        form.add(new AjaxFormComponentFeedbackIndicator("departmentValidationError", userDepartment));

        // user roles
        ListMultipleChoice<UserRole> userRoles = new ListMultipleChoice<UserRole>("user.userRoles", roles, new UserRoleRenderer());
        userRoles.setMaxRows(4);
        userRoles.setLabel(new ResourceModel("admin.user.roles"));
        userRoles.setRequired(true);
        userRoles.add(new ValidatingFormComponentAjaxBehavior());
        form.add(userRoles);
        form.add(new AjaxFormComponentFeedbackIndicator("rolesValidationError", userRoles));

        // active
        form.add(new CheckBox("user.active"));

        // data save label
        form.add(new ServerMessageLabel("serverMessage", "formValidationError"));

        boolean deletable = userModel.getObject().getUser().isDeletable();

        FormConfig formConfig = new FormConfig().forForm(form).withDelete(deletable).withSubmitTarget(this)
                .withDeleteEventType(UserEditAjaxEventType.USER_DELETED)
                .withSubmitEventType(UserEditAjaxEventType.USER_UPDATED);


        FormUtil.setSubmitActions(formConfig);

        greyBorder.add(form);
    }

    @Override
    protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        UserBackingBean userBackingBean = (UserBackingBean) backingBean;

        if (type == UserEditAjaxEventType.USER_UPDATED) {
            if (userBackingBean.getAdminAction() == AdminAction.NEW) {
                userService.newUser(userBackingBean.getUser(), userBackingBean.getUser().getPassword());
            } else {
                userService.editUser(userBackingBean.getUser());

                String password = userBackingBean.getUser().getPassword();
                if (StringUtils.isNotBlank(password)) {
                    userService.changePassword(userBackingBean.getUser().getUsername(), password);
                }
            }
        } else if (type == UserEditAjaxEventType.USER_DELETED) {
            deleteUser(userBackingBean);
        }
    }

    private void deleteUser(UserBackingBean userBackingBean) {
        userService.deleteUser(userBackingBean.getUser().getUserId());
    }

    private class DuplicateUsernameValidator extends AbstractValidator<String> {
        private static final long serialVersionUID = 542950054849279025L;

        @Override
        protected void onValidate(IValidatable<String> validatable) {
            String username = validatable.getValue();
            String orgUsername = ((UserBackingBean) getDefaultModelObject()).getOriginalUsername();

            if ((StringUtils.isNotBlank(orgUsername) && !username.equalsIgnoreCase(orgUsername) && userService.getUser(username) != null)) {
                error(validatable, "admin.user.errorUsernameExists");
            }
        }
    }
}
