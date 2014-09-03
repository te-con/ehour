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

package net.rrm.ehour.ui.manage.department;

import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * user department form panel
 */

public class DepartmentFormPanel extends AbstractDepartmentFormPanelTemplate<DepartmentAdminBackingBean> {
    private static final long serialVersionUID = -6469066920645156569L;

    @SpringBean
    private UserService userService;

    public DepartmentFormPanel(String id, CompoundPropertyModel<DepartmentAdminBackingBean> model) {
        super(id, model);
    }

    @Override
    protected boolean processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception {
        DepartmentAdminBackingBean departmentBackingBean = (DepartmentAdminBackingBean) backingBean;

        if (type == DepartmentAjaxEventType.DEPARTMENT_UPDATED) {
            persistDepartment(departmentBackingBean);
        } else if (type == DepartmentAjaxEventType.DEPARTMENT_DELETED) {
            deleteDepartment(departmentBackingBean);
        }
        return true;
    }

    private void persistDepartment(DepartmentAdminBackingBean backingBean) throws ObjectNotUniqueException {
        userService.persistUserDepartment(backingBean.getDepartment());
    }

    private void deleteDepartment(DepartmentAdminBackingBean backingBean) {
        userService.deleteDepartment(backingBean.getDepartment().getDepartmentId());
    }
}
