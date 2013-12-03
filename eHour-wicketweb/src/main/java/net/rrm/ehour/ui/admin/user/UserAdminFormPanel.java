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

package net.rrm.ehour.ui.admin.user;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotUniqueException;
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
import net.rrm.ehour.ui.common.util.WebGeo;
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
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.List;

import static net.rrm.ehour.ui.admin.user.UserEditAjaxEventType.*;

/**
 * User Form Panel for admin
 */

public class UserAdminFormPanel extends AbstractFormSubmittingPanel<UserAdminBackingBean> {
    private static final long serialVersionUID = -7427807216389657732L;
    protected static final String BORDER = "border";
    protected static final String FORM = "userForm";

    private boolean editMode;

    @SpringBean
    private UserService userService;

    public UserAdminFormPanel(String id,
                              CompoundPropertyModel<UserAdminBackingBean> userModel,
                              List<UserRole> roles,
                              List<UserDepartment> departments) {
        super(id, userModel);

        editMode = getPanelModelObject().getUser().getPK() != null;

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder(BORDER, WebGeo.AUTO);
        add(greyBorder);

        setOutputMarkupId(true);

        final Form<UserAdminBackingBean> form = new Form<UserAdminBackingBean>(FORM, userModel);

        // username
        RequiredTextField<String> usernameField = new RequiredTextField<String>("user.username");
        form.add(usernameField);
        usernameField.add(new StringValidator(0, 32));
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


        // email
        TextField<String> emailField = new TextField<String>("user.email");
        emailField.add(EmailAddressValidator.getInstance());
        emailField.add(new ValidatingFormComponentAjaxBehavior());
        form.add(emailField);
        form.add(new AjaxFormComponentFeedbackIndicator("emailValidationError", emailField));

        // password
        Label label = new Label("passwordEditLabel", new ResourceModel("admin.user.editPassword"));
        label.setVisible(userModel.getObject().isEditMode());
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

        // show assignments
        CheckBox showAssignments = new CheckBox("showAssignments");
        showAssignments.setVisible(!userModel.getObject().isEditMode());
        form.add(showAssignments);


        // data save label
        form.add(new ServerMessageLabel("serverMessage", "formValidationError"));

        boolean deletable = userModel.getObject().getUser().isDeletable();

        FormConfig formConfig = FormConfig.forForm(form).withDelete(deletable).withSubmitTarget(this)
                .withDeleteEventType(USER_DELETED)
                .withSubmitEventType(editMode ? USER_UPDATED : USER_CREATED);


        FormUtil.setSubmitActions(formConfig);

        greyBorder.add(form);
    }

    @Override
    protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        UserAdminBackingBean userAdminBackingBean = (UserAdminBackingBean) backingBean;

        boolean eventHandled;

        try {
            User user = userAdminBackingBean.getUser();
            eventHandled = false;

            if (type == USER_CREATED) {
                userService.newUser(user, user.getPassword());
            } else if (type == USER_UPDATED) {
                userService.editUser(user);

                String password = user.getPassword();
                if (StringUtils.isNotBlank(password)) {
                    userService.changePassword(user.getUsername(), password);
                }
            } else if (type == USER_DELETED) {
                deleteUser(userAdminBackingBean);
            }
        } catch (ObjectNotUniqueException obnu) {
            backingBean.setServerMessage(obnu.getMessage());
            target.add(this);
            eventHandled = true;
        }

        return !eventHandled;
    }

    private void deleteUser(UserAdminBackingBean userAdminBackingBean) {
        userService.deleteUser(userAdminBackingBean.getUser().getUserId());
    }

    private class DuplicateUsernameValidator implements IValidator<String> {
        private static final long serialVersionUID = 542950054849279025L;

        @Override
        public void validate(IValidatable<String> validatable) {
            String username = validatable.getValue();
            String orgUsername = ((UserAdminBackingBean) getDefaultModelObject()).getOriginalUsername();

            if ((StringUtils.isNotBlank(orgUsername) && !username.equalsIgnoreCase(orgUsername) && userService.getUser(username) != null)) {
                validatable.error(new ValidationError("admin.user.errorUsernameExists"));
            }
        }
    }
}
