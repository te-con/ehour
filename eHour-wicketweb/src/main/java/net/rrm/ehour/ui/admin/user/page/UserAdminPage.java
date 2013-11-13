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

package net.rrm.ehour.ui.admin.user.page;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.admin.user.dto.UserBackingBean;
import net.rrm.ehour.ui.admin.user.panel.UserAdminFormPanel;
import net.rrm.ehour.ui.common.AdminAction;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.sort.UserComparator;
import net.rrm.ehour.ui.common.sort.UserDepartmentComparator;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

import static net.rrm.ehour.ui.admin.user.panel.UserEditAjaxEventType.*;
import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorAjaxEventType.FILTER_CHANGE;

/**
 * User management page using 2 tabs, an entrySelector panel and the UserForm panel
 */

public class UserAdminPage extends AbstractTabbedAdminPage<UserBackingBean> {
    private static final Logger LOGGER = Logger.getLogger(UserAdminPage.class);

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

        userListView = new ListView<User>("itemList", users) {
            private static final long serialVersionUID = 5334338761736798802L;

            @Override
            protected void populateItem(ListItem<User> item) {
                final User user = item.getModelObject();
                final Integer userId = user.getUserId();

                AjaxLink<Void> link = new AjaxLink<Void>("itemLink") {
                    private static final long serialVersionUID = -3898942767521616039L;

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        try {
                            getTabbedPanel().setEditBackingBean(new UserBackingBean(userService.getUserAndCheckDeletability(userId), AdminAction.EDIT));
                            getTabbedPanel().switchTabOnAjaxTarget(target, 1);
                        } catch (ObjectNotFoundException e) {
                            LOGGER.error("User not found for id: " + userId);
                        }

                    }
                };

                item.add(link);
                link.add(new Label("linkLabel", user.getFullName() + (user.isActive() ? "" : "*")));
            }
        };

        fragment.add(userListView);

        return fragment;
    }


    /**
     * Handle Ajax request
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();


        if (type == FILTER_CHANGE) {
            currentFilter = ((PayloadAjaxEvent<EntrySelectorFilter>) ajaxEvent).getPayload();

            List<User> users = getUsers();
            userListView.setList(users);

            return false;
        } else if (type == USER_UPDATED
                || type == USER_DELETED
                || type == PASSWORD_CHANGED) {
            // update user list
            List<User> users = getUsers();
            userListView.setList(users);

            selectorPanel.refreshList(ajaxEvent.getTarget());

            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());

            return false;
        }

        return true;
    }

    private List<User> getUsers() {
        List<User> users = currentFilter == null ? userService.getActiveUsers() : userService.getUsers(currentFilter.isActivateToggle());

        Collections.sort(users, new UserComparator(false));

        return users;
    }


    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.ui.admin.BasedTabbedAdminPage#getAddPanel(java.lang.String)
      */
    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new UserAdminFormPanel(panelId,
                new CompoundPropertyModel<UserBackingBean>(getTabbedPanel().getAddBackingBean()),
                getUserRoles(),
                getUserDepartments());
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.ui.admin.BaseTabbedAdminPage#getNewAddBackingBean()
      */
    @Override
    protected UserBackingBean getNewAddBaseBackingBean() {
        UserBackingBean userBean;

        userBean = new UserBackingBean(new User(), AdminAction.NEW);
        userBean.getUser().setActive(true);

        return userBean;
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.ui.admin.BaseTabbedAdminPage#getNewEditBackingBean()
      */
    @Override
    protected UserBackingBean getNewEditBaseBackingBean() {
        return new UserBackingBean(new User(), AdminAction.EDIT);
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.ui.admin.BasedTabbedAdminPage#getEditPanel(java.lang.String)
      */
    @Override
    protected Panel getBaseEditPanel(String panelId) {
        return new UserAdminFormPanel(panelId,
                new CompoundPropertyModel<UserBackingBean>(getTabbedPanel().getEditBackingBean()),
                getUserRoles(),
                getUserDepartments());
    }

    /**
     * Get all the roles from the backend
     *
     * @return
     */
    private List<UserRole> getUserRoles() {
        if (roles == null) {
            roles = userService.getUserRoles();
        }

        return roles;
    }

    /**
     * Get all departments from the backend
     *
     * @return
     */
    private List<UserDepartment> getUserDepartments() {
        if (departments == null) {
            departments = userService.getUserDepartments();
        }

        Collections.sort(departments, new UserDepartmentComparator());

        return departments;
    }
}
