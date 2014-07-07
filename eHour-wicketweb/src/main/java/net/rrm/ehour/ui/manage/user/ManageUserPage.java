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
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.security.SecurityRules;
import net.rrm.ehour.sort.UserDepartmentComparator;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.EntryListUpdatedEvent;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectedEvent;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import net.rrm.ehour.ui.manage.assignment.AssignmentManagePage;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import scala.Option;

import java.util.Collections;
import java.util.List;

/**
 * User management page using 2 tabs, an entrySelector panel and the UserForm panel
 */
public class ManageUserPage extends AbstractTabbedManagePage<ManageUserBackingBean> {
    @SpringBean
    private UserService userService;

    private List<UserDepartment> departments;

    private static final long serialVersionUID = 1883278850247747252L;

    public ManageUserPage() {
        super(new ResourceModel("admin.user.title"),
                new ResourceModel("admin.user.addUser"),
                new ResourceModel("admin.user.editUser"),
                new ResourceModel("admin.user.noEditEntrySelected"));

        add(new UserSelectionPanel("userSelection", Option.apply("admin.user.title")));
    }

    @Override
    public void onEvent(IEvent<?> event) {
        Object payload = event.getPayload();

        if (payload instanceof EntrySelectedEvent) {
            EntrySelectedEvent entrySelectedEvent = (EntrySelectedEvent) payload;
            Integer userId = entrySelectedEvent.userId();

            try {
                getTabbedPanel().setEditBackingBean(new ManageUserBackingBean(userService.getUserAndCheckDeletability(userId)));
                getTabbedPanel().switchTabOnAjaxTarget(entrySelectedEvent.target(), AddEditTabbedPanel.TABPOS_EDIT);
            } catch (ObjectNotFoundException e) {
                // TODO deal with it
                e.printStackTrace();
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        AjaxRequestTarget target = ajaxEvent.getTarget();
        if (type == ManageUserAjaxEventType.USER_CREATED) {
            PayloadAjaxEvent<AdminBackingBean> payloadAjaxEvent = (PayloadAjaxEvent<AdminBackingBean>) ajaxEvent;

            ManageUserBackingBean bean = (ManageUserBackingBean) payloadAjaxEvent.getPayload();

            if (bean.isShowAssignments()) {
                setResponsePage(new AssignmentManagePage(bean.getUser()));
                return false;

            } else {
                updateEntryList(target);
                succesfulSave(target);

                return false;
            }
        } else if (type == ManageUserAjaxEventType.USER_UPDATED
                || type == ManageUserAjaxEventType.USER_DELETED) {
            updateEntryList(target);
            succesfulSave(target);

            return updateUserList(target);
        } else if (type == ManageUserAjaxEventType.PASSWORD_CHANGED) {
            succesfulSave(target);
            return false;
        }

        return true;
    }

    private boolean updateUserList(AjaxRequestTarget target) {
        updateEntryList(target);

        succesfulSave(target);

        return false;
    }

    private void updateEntryList(AjaxRequestTarget target) {
        send(this, Broadcast.DEPTH, new EntryListUpdatedEvent(target));
    }

    private void succesfulSave(AjaxRequestTarget target) {
        getTabbedPanel().succesfulSave(target);
    }

    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new ManageUserFormPanel(panelId,
                new CompoundPropertyModel<ManageUserBackingBean>(getTabbedPanel().getAddBackingBean()),
                getUserDepartments());
    }

    @Override
    protected ManageUserBackingBean getNewAddBaseBackingBean() {
        ManageUserBackingBean userBean = new ManageUserBackingBean();
        userBean.getUser().setActive(true);

        return userBean;
    }

    @Override
    protected ManageUserBackingBean getNewEditBaseBackingBean() {
        return new ManageUserBackingBean();
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        ManageUserBackingBean bean = getTabbedPanel().getEditBackingBean();
        User user = bean.getUser();

        boolean editableUser = SecurityRules.allowedToModify(EhourWebSession.getUser(),
                user,
                EhourWebSession.getEhourConfig().isSplitAdminRole());

        if (editableUser) {
            return new ManageUserFormPanel(panelId,
                    new CompoundPropertyModel<ManageUserBackingBean>(bean),
                    getUserDepartments());
        } else {
            return new ManageUserReadOnlyPanel(panelId,
                    new CompoundPropertyModel<ManageUserBackingBean>(bean));
        }
    }

    private List<UserDepartment> getUserDepartments() {
        if (departments == null) {
            departments = userService.getUserDepartments();
        }

        Collections.sort(departments, new UserDepartmentComparator());

        return departments;
    }
}
