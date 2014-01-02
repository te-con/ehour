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

package net.rrm.ehour.ui.admin.department;

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.sort.UserDepartmentComparator;
import net.rrm.ehour.ui.admin.AbstractTabbedAdminPage;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorListView;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
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

/**
 * Department admin page
 */

public class DepartmentAdminPage extends AbstractTabbedAdminPage<DepartmentAdminBackingBean> {
    private static final String DEPT_SELECTOR_ID = "deptSelector";
    private static final long serialVersionUID = -6686097898699382233L;

    @SpringBean
    private UserService userService;

    private static final Logger logger = Logger.getLogger(DepartmentAdminPage.class);
    private ListView<UserDepartment> deptListView;

    private EntrySelectorPanel entrySelectorPanel;

    public DepartmentAdminPage() {
        super(new ResourceModel("admin.dept.title"),
                new ResourceModel("admin.dept.addDepartment"),
                new ResourceModel("admin.dept.editDepartment"),
                new ResourceModel("admin.dept.noEditEntrySelected"));

        List<UserDepartment> departments;
        departments = getUserDepartments();

        Fragment deptListHolder = getDepartmentListHolder(departments);

        GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", new ResourceModel("admin.dept.title"));
        add(greyBorder);

        entrySelectorPanel = new EntrySelectorPanel(DEPT_SELECTOR_ID, deptListHolder);
        greyBorder.add(entrySelectorPanel);
    }

    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new DepartmentFormPanel(panelId, new CompoundPropertyModel<DepartmentAdminBackingBean>(getTabbedPanel().getAddBackingBean()));
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        return new DepartmentFormPanel(panelId, new CompoundPropertyModel<DepartmentAdminBackingBean>(getTabbedPanel().getEditBackingBean()));
    }

    @Override
    protected Component onFilterChanged(EntrySelectorPanel.FilterChangedEvent filterChangedEvent) {
        throw new IllegalArgumentException("Not supported");
    }

    @Override
    protected DepartmentAdminBackingBean getNewAddBaseBackingBean() {
        return new DepartmentAdminBackingBean(new UserDepartment());
    }

    @Override
    protected DepartmentAdminBackingBean getNewEditBaseBackingBean() {
        return new DepartmentAdminBackingBean(new UserDepartment());
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
        Fragment fragment = new Fragment("itemListHolder", "itemListHolder", DepartmentAdminPage.this);

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

                getTabbedPanel().setEditBackingBean(new DepartmentAdminBackingBean(userService.getUserDepartment(deptId)));
                getTabbedPanel().switchTabOnAjaxTarget(target, AddEditTabbedPanel.TABPOS_EDIT);
            }
        };

        fragment.add(deptListView);

        return fragment;
    }

    /**
     * Get the user departments from the backend
     *
     * @return
     */
    private List<UserDepartment> getUserDepartments() {
        List<UserDepartment> userDepartments = userService.getUserDepartments();
        Collections.sort(userDepartments, new UserDepartmentComparator());

        return userDepartments;
    }
}
