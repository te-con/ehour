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

package net.rrm.ehour.ui.admin.assignment;

import net.rrm.ehour.domain.*;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;

import java.util.List;

/**
 * Backing bean for project assignments
 */

public class AssignmentAdminBackingBean extends AdminBackingBeanImpl {
    private static final long serialVersionUID = 487430742116953930L;
    private ProjectAssignment projectAssignment;
    private List<Project> projects;
    private Customer customer;
    private boolean infiniteStartDate;
    private boolean infiniteEndDate;

    public AssignmentAdminBackingBean() {
    }

    public AssignmentAdminBackingBean(ProjectAssignment assignment) {
        projectAssignment = assignment;
        this.customer = (assignment.getProject() != null) ? assignment.getProject().getCustomer() : null;

        infiniteStartDate = assignment.getDateStart() == null;
        infiniteEndDate = assignment.getDateEnd() == null;
    }

    public static AssignmentAdminBackingBean createAssignmentAdminBackingBean(User user) {
        ProjectAssignment projectAssignment = new ProjectAssignment();
        projectAssignment.setUser(user);
        projectAssignment.setActive(true);

        return new AssignmentAdminBackingBean(projectAssignment);
    }

    public static AssignmentAdminBackingBean createAssignmentAdminBackingBean(Project project) {
        ProjectAssignment projectAssignment = new ProjectAssignment();
        projectAssignment.setProject(project);
        projectAssignment.setActive(true);

        return new AssignmentAdminBackingBean(projectAssignment);
    }



    public boolean isShowAllottedHours() {
        return (projectAssignment.getAssignmentType() != null) && projectAssignment.getAssignmentType().isAllottedType();
    }

    public boolean isNotifyPmEnabled() {
        return (projectAssignment.getProject() != null) && projectAssignment.getProject().getProjectManager() != null;
    }

    public boolean isShowOverrunHours() {
        return (projectAssignment.getAssignmentType() != null) && projectAssignment.getAssignmentType().isFlexAllottedType();
    }

    public ProjectAssignment getProjectAssignment() {
        return projectAssignment;
    }

    public ProjectAssignment getProjectAssignmentForSave() {
        if (isInfiniteStartDate()) {
            projectAssignment.setDateStart(null);
        }


        if (isInfiniteEndDate()) {
            projectAssignment.setDateEnd(null);
        }

        return projectAssignment;
    }

    public void setProjectAssignment(ProjectAssignment projectAssignment) {
        this.projectAssignment = projectAssignment;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isInfiniteStartDate() {
        return infiniteStartDate;
    }

    public void setInfiniteStartDate(boolean infiniteStartDate) {
        this.infiniteStartDate = infiniteStartDate;
    }

    public boolean isInfiniteEndDate() {
        return infiniteEndDate;
    }

    public void setInfiniteEndDate(boolean infiniteEndDate) {
        this.infiniteEndDate = infiniteEndDate;
    }

    public DomainObject<?, ?> getDomainObject() {
        return getProjectAssignment();
    }
}
