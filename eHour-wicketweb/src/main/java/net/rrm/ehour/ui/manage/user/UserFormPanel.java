package net.rrm.ehour.ui.manage.user;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.security.SecurityRules;
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
import net.rrm.ehour.user.service.LdapUser;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static net.rrm.ehour.ui.manage.user.UserManageAjaxEventType.USER_DELETED;
import static net.rrm.ehour.ui.manage.user.UserManageAjaxEventType.USER_UPDATED;


/**
 * User Form Panel for admin
 */

public class UserFormPanel extends AbstractFormSubmittingPanel<LdapUserBackingBean> {
    private static final long serialVersionUID = -7427807216389657732L;
    private static final String BORDER = "border";
    private static final String FORM = "userForm";
    private ServerMessageLabel messageLabel;
    
    @SpringBean
    private UserService userService;

    public UserFormPanel(String id, CompoundPropertyModel<LdapUserBackingBean> userModel) {
        super(id, userModel);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        IModel<LdapUserBackingBean> userModel = getPanelModel();

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder(BORDER, WebGeo.AUTO);
        add(greyBorder);

        setOutputMarkupId(true);

        Form<LdapUserBackingBean> form = new Form<LdapUserBackingBean>(FORM, userModel);

        form.add(new Label("user.uid"));
        form.add(new Label("user.fullName"));
        form.add(new Label("user.email"));
        form.add(new Label("user.dn"));

        // user roles
        ListMultipleChoice<UserRole> userRoles = new ListMultipleChoice<UserRole>("user.user.userRoles", getUserRoles(), new UserRoleRenderer());
        userRoles.setMaxRows(4);
        userRoles.setLabel(new ResourceModel("admin.user.roles"));
        userRoles.setRequired(true);
        userRoles.add(new ValidatingFormComponentAjaxBehavior());
        form.add(userRoles);
        form.add(new AjaxFormComponentFeedbackIndicator("rolesValidationError", userRoles));

        // active
        form.add(new CheckBox("user.user.active"));


        // data save label
        messageLabel = new ServerMessageLabel("serverMessage", "formValidationError");
        messageLabel.setOutputMarkupId(true);
        form.add(messageLabel);
        FormConfig formConfig = FormConfig.forForm(form).withDelete(false)
                .withDeleteEventType(USER_DELETED)
                .withSubmitTarget(this)
                .withSubmitEventType(USER_UPDATED);


        FormUtil.setSubmitActions(formConfig);

        greyBorder.add(form);
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
        LdapUserBackingBean ldapUserBackingBean = (LdapUserBackingBean) backingBean;

        if (type == USER_UPDATED) {
            LdapUser ldapUser = ldapUserBackingBean.getUser();

            User user = ldapUser.getUser();
            user.setName(ldapUser.fullName);
            user.setUsername(ldapUser.uid);
            user.setDn(ldapUser.dn);

            userService.editUser(user);

            messageLabel.setDefaultModel(new Model<String>(getLocalizer().getString("general.dataSaved", this)));
            target.add(messageLabel);
        }

        return true;
    }

}
