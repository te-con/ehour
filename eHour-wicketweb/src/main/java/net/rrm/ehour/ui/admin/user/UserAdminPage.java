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
import net.rrm.ehour.sort.UserDepartmentComparator;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;

import java.util.Collections;
import java.util.List;

/**
 * User management page using 2 tabs, an entrySelector panel and the UserForm panel
 */

public class UserAdminPage extends AbstractUserAdminPage {

    private List<UserRole> roles;
    private List<UserDepartment> departments;

    private static final long serialVersionUID = 1883278850247747252L;

    public UserAdminPage() {
        super(new ResourceModel("admin.user.title"),
                new ResourceModel("admin.user.addUser"),
                new ResourceModel("admin.user.editUser"),
                new ResourceModel("admin.user.noEditEntrySelected"));

        List<User> users;
        users = getUsers();

        Fragment userListHolder = getUserListHolder(users);

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame",
                new ResourceModel("admin.user.title")
        );
        add(greyBorder);

        selectorPanel = new EntrySelectorPanel("userSelector", userListHolder, new ResourceModel("admin.user.hideInactive"));
        greyBorder.add(selectorPanel);
    }


    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new UserAdminFormPanel(panelId,
                new CompoundPropertyModel<UserAdminBackingBean>(getTabbedPanel().getAddBackingBean()),
                getUserRoles(),
                getUserDepartments());
    }

    @Override
    protected UserAdminBackingBean getNewAddBaseBackingBean() {
        UserAdminBackingBean userBean;

        userBean = new UserAdminBackingBean();
        userBean.getUser().setActive(true);

        return userBean;
    }

    @Override
    protected UserAdminBackingBean getNewEditBaseBackingBean() {
        return new UserAdminBackingBean();
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        return new UserAdminFormPanel(panelId,
                new CompoundPropertyModel<UserAdminBackingBean>(getTabbedPanel().getEditBackingBean()),
                getUserRoles(),
                getUserDepartments());
    }

    private List<UserRole> getUserRoles() {
        if (roles == null) {
            roles = userService.getUserRoles();
        }

        return roles;
    }

    private List<UserDepartment> getUserDepartments() {
        if (departments == null) {
            departments = userService.getUserDepartments();
        }

        Collections.sort(departments, new UserDepartmentComparator());

        return departments;
    }
}
