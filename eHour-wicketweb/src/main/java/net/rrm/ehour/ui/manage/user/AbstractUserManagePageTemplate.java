package net.rrm.ehour.ui.manage.user;

import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.EntryListUpdatedEvent;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectedEvent;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import net.rrm.ehour.ui.manage.assignment.AssignmentManagePage;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import scala.Option;

public abstract class AbstractUserManagePageTemplate<T extends UserManageBackingBean> extends AbstractTabbedManagePage<T> {
    @SpringBean
    private UserService userService;

    private static final long serialVersionUID = 1883278850247747252L;

    public AbstractUserManagePageTemplate() {
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
                getTabbedPanel().setEditBackingBean(createEditBean(userId));
                getTabbedPanel().switchTabOnAjaxTarget(entrySelectedEvent.target(), AddEditTabbedPanel.TABPOS_EDIT);
            } catch (ObjectNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected abstract T createEditBean(Integer userId) throws ObjectNotFoundException;

    @Override
    @SuppressWarnings("unchecked")
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        AjaxRequestTarget target = ajaxEvent.getTarget();
        if (type == UserManageAjaxEventType.USER_CREATED) {
            PayloadAjaxEvent<AdminBackingBean> payloadAjaxEvent = (PayloadAjaxEvent<AdminBackingBean>) ajaxEvent;

            UserManageBackingBean bean = (UserManageBackingBean) payloadAjaxEvent.getPayload();

            if (bean.isShowAssignments()) {
                setResponsePage(new AssignmentManagePage(bean.getUser()));
                return false;
            } else {
                updateEntryList(target);
                succesfulSave(target);

                return false;
            }
        } else if (type == UserManageAjaxEventType.USER_UPDATED
                || type == UserManageAjaxEventType.USER_DELETED) {
            updateEntryList(target);
            succesfulSave(target);

            return updateUserList(target);
        } else if (type == UserManageAjaxEventType.PASSWORD_CHANGED) {
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
    protected void onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent, AjaxRequestTarget target) {
        send(this, Broadcast.DEPTH, inactiveFilterChangedEvent);
    }

    protected UserService getUserService() {
        return userService;
    }
}
