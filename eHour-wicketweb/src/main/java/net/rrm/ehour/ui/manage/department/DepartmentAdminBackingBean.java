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
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;

/**
 * Department backing bean
 */

public class DepartmentAdminBackingBean extends AdminBackingBeanImpl<UserDepartment> {
    private static final long serialVersionUID = -4095608816724112187L;
    private UserDepartment department;

    public DepartmentAdminBackingBean(UserDepartment department) {
        this.department = department;
    }

    /**
     * @return the department
     */
    public UserDepartment getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(UserDepartment department) {
        this.department = department;
    }

    @Override
    public UserDepartment getDomainObject() {
        return getDepartment();
    }

    @Override
    public boolean isDeletable() {
        return department != null && department.isDeletable();
    }
}
