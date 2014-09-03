package net.rrm.ehour.ui.manage.department;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.sort.UserDepartmentComparator;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorListView;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.Collections;
import java.util.List;

public abstract class AbstractDepartmentManagePageTemplate<T extends AdminBackingBean> extends AbstractTabbedManagePage<T> {
    private static final String DEPT_SELECTOR_ID = "deptSelector";
    private static final long serialVersionUID = -6686097898699382233L;

    @SpringBean
    private UserService userService;

    private ListView<UserDepartment> deptListView;

    private EntrySelectorPanel entrySelectorPanel;

    public AbstractDepartmentManagePageTemplate() {
        super(new ResourceModel("admin.dept.title"),
                new ResourceModel("admin.dept.addDepartment"),
                new ResourceModel("admin.dept.editDepartment"),
                new ResourceModel("admin.dept.noEditEntrySelected"));

        List<UserDepartment> departments = getUserDepartments();

        Fragment deptListHolder = getDepartmentListHolder(departments);

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.dept.title"));
        add(greyBorder);

        entrySelectorPanel = new EntrySelectorPanel(DEPT_SELECTOR_ID, deptListHolder);
        greyBorder.add(entrySelectorPanel);
    }

    @Override
    protected Component onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == DepartmentAjaxEventType.DEPARTMENT_DELETED
                || type == DepartmentAjaxEventType.DEPARTMENT_UPDATED) {
            // update customer list
            List<UserDepartment> depts = getUserDepartments();
            deptListView.setList(depts);

            entrySelectorPanel.refreshList(ajaxEvent.getTarget());

            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
        }

        return true;
    }

    @SuppressWarnings("serial")
    private Fragment getDepartmentListHolder(List<UserDepartment> departments) {
        Fragment fragment = new Fragment("itemListHolder", "itemListHolder", AbstractDepartmentManagePageTemplate.this);

        deptListView = new EntrySelectorListView<UserDepartment>("itemList", departments) {
            @Override
            protected void onPopulate(ListItem<UserDepartment> item, IModel<UserDepartment> itemModel) {
                UserDepartment dept = item.getModelObject();

                item.add(new Label("name", dept.getName()));
                item.add(new Label("code", dept.getCode()));
                item.add(new Label("users", dept.getUsers() == null ? 0 : dept.getUsers().size()));
            }

            @Override
            protected void onClick(ListItem<UserDepartment> item, AjaxRequestTarget target) throws ObjectNotFoundException {
                final Integer deptId = item.getModelObject().getDepartmentId();

                getTabbedPanel().setEditBackingBean(createEditBean(deptId));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        fragment.add(deptListView);

        return fragment;
    }

    protected abstract T createEditBean(Integer deptId) throws ObjectNotFoundException;

    private List<UserDepartment> getUserDepartments() {
        List<UserDepartment> userDepartments = userService.getUserDepartments();
        Collections.sort(userDepartments, new UserDepartmentComparator());

        return userDepartments;
    }
}
