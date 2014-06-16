package net.rrm.ehour.ui.admin.user;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.sort.UserComparator;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.admin.assignment.AssignmentAdminPage;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

import static net.rrm.ehour.ui.admin.user.UserEditAjaxEventType.*;

public abstract class AbstractUserAdminPage extends AbstractTabbedAdminPage<UserAdminBackingBean> {
    @SpringBean
    protected UserService userService;
    protected EntrySelectorPanel selectorPanel;
    private ListView<User> userListView;
    private EntrySelectorFilter currentFilter;

    public AbstractUserAdminPage(ResourceModel pageTitle, ResourceModel addTabTitle, ResourceModel editTabTitle, ResourceModel noEntrySelectedText) {
        super(pageTitle, addTabTitle, editTabTitle, noEntrySelectedText);
    }

    protected Fragment getUserListHolder(List<User> users) {
        Fragment fragment = new Fragment("itemListHolder", "itemListHolder", AbstractUserAdminPage.this);

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
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
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

    protected List<User> getUsers() {
        List<User> users = currentFilter == null ? userService.getActiveUsers() : userService.getUsers(currentFilter.isFilterToggle());

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
