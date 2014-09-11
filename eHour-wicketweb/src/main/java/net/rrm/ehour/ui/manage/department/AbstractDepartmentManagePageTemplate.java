package net.rrm.ehour.ui.manage.department;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.sort.UserDepartmentComparator;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorListView;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import net.rrm.ehour.user.service.UserService;
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

import static net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorData.Header;

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
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.dept.title"));
        addOrReplace(greyBorder);

        EntrySelectorPanel.ClickHandler clickHandler = new EntrySelectorPanel.ClickHandler() {
            @Override
            public void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException {
                Integer projectId = (Integer) row.getId();
                getTabbedPanel().setEditBackingBean(createEditBean(projectId));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        entrySelectorPanel = new EntrySelectorPanel(DEPT_SELECTOR_ID,
                createSelectorData(getUserDepartments()),
                clickHandler);
        greyBorder.addOrReplace(entrySelectorPanel);
    }

    private EntrySelectorData createSelectorData(List<UserDepartment> userDepartments) {
        List<Header> headers = Lists.newArrayList(new Header("admin.dept.code"),
                                                                    new Header("admin.dept.name"),
                                                                    new Header("admin.dept.users"));

        List<EntrySelectorData.EntrySelectorRow> rows = Lists.newArrayList();

        for (UserDepartment department : userDepartments) {
            rows.add(new EntrySelectorData.EntrySelectorRow(Lists.newArrayList(department.getName(), department.getProjectCode()), active));
        }

        return new EntrySelectorData(headers, rows);
        return null;
    }

    @Override
    protected void onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent, AjaxRequestTarget target) {
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

            entrySelectorPanel.reRender(ajaxEvent.getTarget());

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

    protected final UserService getUserService() {
        return userService;
    }
}
