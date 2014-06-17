package net.rrm.ehour.ui.admin.user;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.sort.UserComparator;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

public abstract class AbstractUserAdminPage extends AbstractTabbedAdminPage<UserAdminBackingBean> {
    @SpringBean
    protected UserService userService;

    private EntrySelectorFilter currentFilter;

    public AbstractUserAdminPage(ResourceModel pageTitle, ResourceModel addTabTitle, ResourceModel editTabTitle, ResourceModel noEntrySelectedText) {
        super(pageTitle, addTabTitle, editTabTitle, noEntrySelectedText);
    }


    /**
     * Handle Ajax request
     */
//    @Override
//    @SuppressWarnings("unchecked")
//    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
//        AjaxEventType type = ajaxEvent.getEventType();
//
//        if (type == USER_CREATED) {
//            PayloadAjaxEvent<AdminBackingBean> payloadAjaxEvent = (PayloadAjaxEvent<AdminBackingBean>) ajaxEvent;
//
//            UserAdminBackingBean bean = (UserAdminBackingBean) payloadAjaxEvent.getPayload();
//
//            if (bean.isShowAssignments()) {
//                setResponsePage(new AssignmentAdminPage(bean.getUser()));
//                return false;
//
//            } else {
//                return updateUserList(ajaxEvent);
//            }
//        } else if (type == USER_UPDATED
//                || type == USER_DELETED
//                || type == PASSWORD_CHANGED) {
//            return updateUserList(ajaxEvent);
//        }
//
//        return true;
//    }

//    private boolean updateUserList(AjaxEvent ajaxEvent) {
//        List<User> users = getUsers();
//        userListView.setList(users);
//
//        selectorPanel.refreshList(ajaxEvent.getTarget());
//
//        getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
//
//        return false;
//    }
//
//    @Override
//    protected Component onFilterChanged(EntrySelectorPanel.FilterChangedEvent filterChangedEvent) {
//        currentFilter = filterChangedEvent.getFilter();
//
//        List<User> users = getUsers();
////        userListView.setList(users);
//
//        return
////        return userListView.getParent();
//    }

    protected List<User> getUsers() {
        List<User> users = currentFilter == null || currentFilter.isFilterToggle() ? userService.getActiveUsers() : userService.getUsers();

        Collections.sort(users, new UserComparator(false));

        return users;
    }

    @Override
    protected abstract Panel getBaseAddPanel(String panelId);

    @Override
    protected abstract UserAdminBackingBean getNewAddBaseBackingBean();

    @Override
    protected abstract UserAdminBackingBean getNewEditBaseBackingBean();

    @Override
    protected abstract Panel getBaseEditPanel(String panelId);
}
