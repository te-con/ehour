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
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel.EntrySelectorBuilder;
import net.rrm.ehour.ui.common.panel.entryselector.InactiveFilterChangedEvent;
import net.rrm.ehour.ui.manage.AbstractTabbedManagePage;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

        entrySelectorPanel = constructEntrySelectorBuilder().build();
        greyBorder.addOrReplace(entrySelectorPanel);
    }

    protected EntrySelectorBuilder constructEntrySelectorBuilder() {
        EntrySelectorPanel.ClickHandler clickHandler = new EntrySelectorPanel.ClickHandler() {
            @Override
            public void onClick(EntrySelectorData.EntrySelectorRow row, AjaxRequestTarget target) throws ObjectNotFoundException {
                Integer departmentId = (Integer) row.getId();
                getTabbedPanel().setEditBackingBean(createEditBean(departmentId));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        return EntrySelectorBuilder.startAs(DEPT_SELECTOR_ID)
                .withData(createSelectorData(getUserDepartments()))
                .onClick(clickHandler);
    }

    protected EntrySelectorData createSelectorData(List<UserDepartment> userDepartments) {
        List<Header> headers = Lists.newArrayList(new Header("admin.dept.code"),
                                                  new Header("admin.dept.name"),
                                                  new Header("admin.dept.users", EntrySelectorData.ColumnType.NUMERIC));

        List<EntrySelectorData.EntrySelectorRow> rows = Lists.newArrayList();

        for (UserDepartment department : userDepartments) {
            List<String> cells = Lists.newArrayList(department.getCode(),
                                                    department.getName(),
                                                    Integer.toString(department.getUsers() == null ? 0 : department.getUsers().size()));

            rows.add(new EntrySelectorData.EntrySelectorRow(cells, department.getDepartmentId()));
        }

        return new EntrySelectorData(headers, rows);
    }

    @Override
    protected void onFilterChanged(InactiveFilterChangedEvent inactiveFilterChangedEvent, AjaxRequestTarget target) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == DepartmentAjaxEventType.DEPARTMENT_DELETED || type == DepartmentAjaxEventType.DEPARTMENT_UPDATED) {
            entrySelectorPanel.updateData(createSelectorData(getUserDepartments()));
            entrySelectorPanel.reRender(ajaxEvent.getTarget());
            getTabbedPanel().succesfulSave(ajaxEvent.getTarget());
        }

        return true;
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
