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

package net.rrm.ehour.ui.admin.project;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Project admin backing bean
 */

public class ProjectAdminBackingBean extends AdminBackingBeanImpl {
    private Project project;
    private boolean assignExistingUsersToDefaultProjects = false;

    private List<ProjectAssignment> assignmentsToCommit = new CopyOnWriteArrayList<ProjectAssignment>();

    public ProjectAdminBackingBean(Project project) {
        this.project = project;
    }

    public List<ProjectAssignment> getAssignmentsToCommit() {


        return assignmentsToCommit;
    }

    public void addAssignmentToCommit(ProjectAssignment assignment) {
        List<ProjectAssignment> filteredToCommits = filterOutPreviousCommitsForThisAssignments(assignment);
        assignmentsToCommit = filteredToCommits;
        assignmentsToCommit.add(assignment);
    }

    private List<ProjectAssignment> filterOutPreviousCommitsForThisAssignments(ProjectAssignment assignment) {
        List<ProjectAssignment> filteredToCommits = new CopyOnWriteArrayList<ProjectAssignment>();

        for (ProjectAssignment projectAssignment : assignmentsToCommit) {
            if (assignment.getPK() != null) {
                if (!projectAssignment.getPK().equals(assignment.getPK())) {
                    filteredToCommits.add(projectAssignment);
                }
            } else {
                if (!(assignment.getUser().equals(projectAssignment.getUser()) &&
                        assignment.getProject().equals(projectAssignment.getProject()))) {
                    filteredToCommits.add(projectAssignment);

                }
            }
        }
        return filteredToCommits;
    }

    public Project getProject() {
        return project;
    }

    public Project getDomainObject() {
        return getProject();
    }

    public boolean isAssignExistingUsersToDefaultProjects() {
        return assignExistingUsersToDefaultProjects;
    }

    public void setAssignExistingUsersToDefaultProjects(boolean assignExistingUsersToDefaultProjects) {
        this.assignExistingUsersToDefaultProjects = assignExistingUsersToDefaultProjects;
    }
}
