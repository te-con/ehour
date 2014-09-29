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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

import static net.rrm.ehour.ui.manage.user.ManageUserAjaxEventType.USER_DELETED;
import static net.rrm.ehour.ui.manage.user.ManageUserAjaxEventType.USER_UPDATED;

/**
 * User Form Panel for admin
 */

public class ManageUserFormPanel extends AbstractFormSubmittingPanel<LdapUserBackingBean> {
    private static final long serialVersionUID = -7427807216389657732L;
    private static final String BORDER = "border";
    private static final String FORM = "userForm";
    private final ServerMessageLabel messageLabel;

    private List<UserRole> roles;

    @SpringBean
    private UserService userService;

    public ManageUserFormPanel(String id, CompoundPropertyModel<LdapUserBackingBean> userModel) {
        super(id, userModel);

        GreySquaredRoundedBorder greyBorder = new GreySquaredRoundedBorder(BORDER, WebGeo.AUTO);
        add(greyBorder);

        setOutputMarkupId(true);

        Form<LdapUserBackingBean> form = new Form<LdapUserBackingBean>(FORM, userModel);

        form.add(new Label("user.uid"));
        form.add(new Label("user.fullName"));
        form.add(new Label("user.email"));
        form.add(new Label("user.dn"));

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
        if (roles == null) {
            roles = userService.getUserRoles();

            roles.remove(UserRole.PROJECTMANAGER);

            User user = EhourWebSession.getUser();

            if (!SecurityRules.isAdmin(user)) {
                roles.remove(UserRole.ADMIN);
            }

            if (!EhourWebSession.getEhourConfig().isSplitAdminRole()) {
                roles.remove(UserRole.MANAGER);
            }
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
