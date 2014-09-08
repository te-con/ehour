package net.rrm.ehour.ui.manage.user;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.security.SecurityRules;
import net.rrm.ehour.sort.UserDepartmentComparator;
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
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.WebGeo;
import net.rrm.ehour.ui.userprefs.panel.PasswordFieldFactory;
import net.rrm.ehour.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;

import java.util.Collections;
import java.util.List;

import static net.rrm.ehour.ui.manage.user.UserManageAjaxEventType.*;

public class UserFormPanel<T extends UserManageBackingBean> extends AbstractFormSubmittingPanel<T> {
    private static final long serialVersionUID = -7427807216389657732L;
    private static final String BORDER = "border";
    private static final String FORM = "userForm";

    @SpringBean
    private UserService userService;

    public UserFormPanel(String id,
                         CompoundPropertyModel<T> userModel) {
        super(id, userModel);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<T> userModel = getPanelModel();

        T manageUserBackingBean = getPanelModelObject();
        User user = manageUserBackingBean.getUser();

        boolean editMode = user.getPK() != null;

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder(BORDER, WebGeo.AUTO);
        add(greyBorder);

        setOutputMarkupId(true);

        final Form<T> form = new Form<T>(FORM, userModel);

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
        label.setVisible(manageUserBackingBean.isEditMode());
        form.add(label);

        PasswordFieldFactory.createOptionalPasswordFields(form, new PropertyModel<String>(userModel, "user.password"));

        // department
        DropDownChoice<UserDepartment> userDepartment = new DropDownChoice<UserDepartment>("user.userDepartment", getUserDepartments(), new ChoiceRenderer<UserDepartment>("name"));
        userDepartment.setRequired(true);
        userDepartment.setLabel(new ResourceModel("admin.user.department"));
        userDepartment.add(new ValidatingFormComponentAjaxBehavior());
        form.add(userDepartment);
        form.add(new AjaxFormComponentFeedbackIndicator("departmentValidationError", userDepartment));

        // user roles
        ListMultipleChoice<UserRole> userRoles = new ListMultipleChoice<UserRole>("user.userRoles", getUserRoles(), new UserRoleRenderer());
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
        showAssignments.setVisible(!manageUserBackingBean.isEditMode());
        form.add(showAssignments);


        // data save label
        form.add(new ServerMessageLabel("serverMessage", "formValidationError"));

        boolean deletable = user.isDeletable();

        FormConfig formConfig = FormConfig.forForm(form).withDelete(deletable)
                .withDeleteEventType(USER_DELETED)
                .withSubmitTarget(this)
                .withSubmitEventType(editMode ? USER_UPDATED : USER_CREATED);


        FormUtil.setSubmitActions(formConfig);

        greyBorder.add(form);

        onFormCreated(form);
    }

    protected void onFormCreated(Form<T> form) {

    }

    private List<UserRole> getUserRoles() {
        List<UserRole> roles = userService.getUserRoles();

        roles.remove(UserRole.PROJECTMANAGER);

        User user = EhourWebSession.getUser();

        if (!SecurityRules.isAdmin(user)) {
            roles.remove(UserRole.ADMIN);
        }

        if (!EhourWebSession.getEhourConfig().isSplitAdminRole()) {
            roles.remove(UserRole.MANAGER);
        }

        return roles;
    }

    @Override
    protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        UserManageBackingBean userManageBackingBean = (UserManageBackingBean) backingBean;

        boolean eventHandled;

        try {
            User user = userManageBackingBean.getUser();
            eventHandled = false;

            if (type == USER_CREATED) {
                userService.persistNewUser(user, user.getPassword());
            } else if (type == USER_UPDATED) {
                userService.persistEditedUser(user);

                String password = user.getPassword();
                if (StringUtils.isNotBlank(password)) {
                    userService.changePassword(user.getUsername(), password);
                }
            } else if (type == USER_DELETED) {
                deleteUser(userManageBackingBean);
            }
        } catch (ObjectNotUniqueException obnu) {
            backingBean.setServerMessage(obnu.getMessage());
            target.add(this);
            eventHandled = true;
        }

        return !eventHandled;
    }

    private void deleteUser(UserManageBackingBean userManageBackingBean) {
        userService.deleteUser(userManageBackingBean.getUser().getUserId());
    }


    private class DuplicateUsernameValidator implements IValidator<String> {
        private static final long serialVersionUID = 542950054849279025L;

        @Override
        public void validate(IValidatable<String> validatable) {
            String username = validatable.getValue();
            String orgUsername = ((UserManageBackingBean) getDefaultModelObject()).getOriginalUsername();

            if ((StringUtils.isNotBlank(orgUsername) && !username.equalsIgnoreCase(orgUsername) && userService.getUser(username) != null)) {
                validatable.error(new ValidationError("admin.user.errorUsernameExists"));
            }
        }
    }

    private List<UserDepartment> getUserDepartments() {
        List<UserDepartment> departments = userService.getUserDepartments();

        Collections.sort(departments, new UserDepartmentComparator());

        return departments;
    }
}
