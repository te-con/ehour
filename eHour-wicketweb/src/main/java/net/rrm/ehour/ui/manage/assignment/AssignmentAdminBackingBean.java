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

package net.rrm.ehour.ui.manage.assignment;

import com.google.common.collect.Lists;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;
import net.rrm.ehour.util.EhourConstants;

import java.util.List;

/**
 * Backing bean for project assignments
 */

public class AssignmentAdminBackingBean extends AdminBackingBeanImpl<ProjectAssignment> {
    private static final long serialVersionUID = 487430742116953930L;
    private ProjectAssignment projectAssignment;
    private List<Project> selectedProjects;
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
        projectAssignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_DATE);
        projectAssignment.setUser(user);
        projectAssignment.setActive(true);

        return new AssignmentAdminBackingBean(projectAssignment);
    }

    public static AssignmentAdminBackingBean createAssignmentAdminBackingBean(Project project) {
        ProjectAssignment projectAssignment = new ProjectAssignment();
        projectAssignment.setAssignmentType(EhourConstants.ASSIGNMENT_TYPE_DATE);
        projectAssignment.setProject(project);
        projectAssignment.setActive(true);

        return new AssignmentAdminBackingBean(projectAssignment);
    }

    public void updateCustomerBasedOnSelectedProject() {
        if (!isNewAssignment()) {
                setCustomer(getProjectAssignment().getProject().getCustomer());
        } else {
            int selectedProjectsSize = selectedProjects.size();
            if (selectedProjectsSize == 1) {
                setCustomer(selectedProjects.get(0).getCustomer());
            } else {
                setCustomer(null);
            }
        }
    }

    public boolean isNewAssignment() {
        return getProjectAssignment().isNew();
    }

    public List<ProjectAssignment> getProjectAssignmentsForSave() {
        correctDatesInAssignment();

        List<ProjectAssignment> assignments = Lists.newArrayList();
        for (Project selectedProject : selectedProjects) {
            assignments.add(ProjectAssignment.createProjectAssignment(projectAssignment, selectedProject));
        }

        return assignments;
    }

    public ProjectAssignment getProjectAssignmentForSave() {
        correctDatesInAssignment();
        return projectAssignment;
    }

    private void correctDatesInAssignment() {
        if (isInfiniteStartDate()) {
            projectAssignment.setDateStart(null);
        }


        if (isInfiniteEndDate()) {
            projectAssignment.setDateEnd(null);
        }
    }

    public User getUser() {
        return projectAssignment.getUser();
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

    public void setProjectAssignment(ProjectAssignment projectAssignment) {
        this.projectAssignment = projectAssignment;
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

    public ProjectAssignment getDomainObject() {
        return getProjectAssignment();
    }

    public List<Project> getSelectedProjects() {
        return selectedProjects;
    }

    public void setSelectedProjects(List<Project> selectedProjects) {
        this.selectedProjects = selectedProjects;
    }

    @Override
    public boolean isDeletable() {
        return projectAssignment != null && projectAssignment.isDeletable();
    }

    public boolean isBookable() {
        return (projectAssignment != null && projectAssignment.isBookable()) || projectAssignment == null;
    }
}
