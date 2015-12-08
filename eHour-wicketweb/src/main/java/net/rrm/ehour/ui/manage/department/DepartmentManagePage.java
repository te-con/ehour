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

import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.user.service.UserService;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Department admin page
 */

public class DepartmentManagePage extends AbstractDepartmentManagePageTemplate<DepartmentAdminBackingBean> {
    private static final long serialVersionUID = -6686097898699382233L;

    @SpringBean
    private UserService userService;

    @Override
    protected Panel getBaseAddPanel(String panelId) {
        return new DepartmentFormPanel<>(panelId, new CompoundPropertyModel<>(getTabbedPanel().getAddBackingBean()));
    }

    @Override
    protected Panel getBaseEditPanel(String panelId) {
        return new DepartmentFormPanel<>(panelId, new CompoundPropertyModel<>(getTabbedPanel().getEditBackingBean()));
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
    protected DepartmentAdminBackingBean createEditBean(Integer deptId) throws ObjectNotFoundException {
        return new DepartmentAdminBackingBean(userService.getUserDepartment(deptId));
    }

}
