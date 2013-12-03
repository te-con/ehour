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
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.sort.UserComparator;
import net.rrm.ehour.sort.UserDepartmentComparator;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.admin.assignment.AssignmentAdminPage;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorListView;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

import static net.rrm.ehour.ui.admin.user.UserEditAjaxEventType.*;

/**
 * User management page using 2 tabs, an entrySelector panel and the UserForm panel
 */

public class UserAdminPage extends AbstractTabbedAdminPage<UserAdminBackingBean> {
    @SpringBean
    private UserService userService;

    private ListView<User> userListView;
    private EntrySelectorFilter currentFilter;
    private List<UserRole> roles;
    private List<UserDepartment> departments;
    private EntrySelectorPanel selectorPanel;

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

    private Fragment getUserListHolder(List<User> users) {
        Fragment fragment = new Fragment("itemListHolder", "itemListHolder", UserAdminPage.this);

        userListView = new EntrySelectorListView<User>("itemList", users) {
            @Override
            protected void onPopulate(ListItem<User> item, IModel<User> itemModel) {
                User user = item.getModelObject();

                if (!user.isActive()) {
                    item.add(AttributeModifier.append("class", "inactive"));
                }

                item.add(new Label("firstName", user.getFirstName()));
                item.add(new Label("lastName", user.getLastName()));
                item.add(new Label("userName", user.getUsername()));
            }

            @Override
            protected void onClick(ListItem<User> item, AjaxRequestTarget target) throws ObjectNotFoundException {
                final Integer userId = item.getModelObject().getUserId();
                getTabbedPanel().setEditBackingBean(new UserAdminBackingBean(userService.getUserAndCheckDeletability(userId)));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };
        fragment.add(userListView);
        fragment.setOutputMarkupId(true);

        return fragment;
    }


    /**
     * Handle Ajax request
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == USER_CREATED) {
            PayloadAjaxEvent<AdminBackingBean> payloadAjaxEvent = (PayloadAjaxEvent<AdminBackingBean>) ajaxEvent;

            UserAdminBackingBean bean = (UserAdminBackingBean) payloadAjaxEvent.getPayload();

            if (bean.isShowAssignments()) {
                setResponsePage(new AssignmentAdminPage(bean.getUser()));
                return false;

            } else {
                return updateUserList(ajaxEvent);
            }
        } else if (type == USER_UPDATED
                || type == USER_DELETED
                || type == PASSWORD_CHANGED) {
            return updateUserList(ajaxEvent);
        }

        return true;
    }

    private boolean updateUserList(AjaxEvent ajaxEvent) {
        List<User> users = getUsers();
        userListView.setList(users);

        selectorPanel.refreshList(ajaxEvent.getTarget());

        getTabbedPanel().succesfulSave(ajaxEvent.getTarget());

        return false;
    }

    @Override
    protected Component onFilterChanged(EntrySelectorPanel.FilterChangedEvent filterChangedEvent) {
        currentFilter = filterChangedEvent.getFilter();

        List<User> users = getUsers();
        userListView.setList(users);


        return userListView.getParent();
    }

    private List<User> getUsers() {
        List<User> users = currentFilter == null ? userService.getActiveUsers() : userService.getUsers(currentFilter.isFilterToggle());

        Collections.sort(users, new UserComparator(false));

        return users;
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
