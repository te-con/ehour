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

package net.rrm.ehour.project.service;

import net.rrm.ehour.audit.annot.Auditable;
import net.rrm.ehour.domain.AuditActionType;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;
import net.rrm.ehour.report.reports.util.ReportUtil;
import net.rrm.ehour.report.service.AggregateReportService;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Project service
 */
@Service("projectService")
public class ProjectServiceImpl implements ProjectService {
    private static final Logger LOGGER = Logger.getLogger(ProjectServiceImpl.class);

    @Autowired
    private ProjectDao projectDAO;

    @Autowired
    private ProjectAssignmentManagementService projectAssignmentManagementService;

    @Autowired
    private AggregateReportService aggregateReportService;

    @Autowired
    private UserService userService;

    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<Project> getProjects(boolean hideInactive) {
        return hideInactive ? projectDAO.findAllActive() : projectDAO.findAll();
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.project.service.ProjectService#getProject(java.lang.Integer)
      */
    public Project getProject(Integer projectId) throws ObjectNotFoundException {
        Project project = projectDAO.findById(projectId);

        if (project == null) {
            throw new ObjectNotFoundException("Project not found for id " + projectId);
        }

        return project;
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.project.service.ProjectService#getProjectAndCheckDeletability(java.lang.Integer)
      */
    public Project getProjectAndCheckDeletability(Integer projectId) throws ObjectNotFoundException {
        Project project = getProject(projectId);

        setProjectDeletability(project);

        return project;
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.project.service.ProjectService#setProjectDeletability(net.rrm.ehour.persistence.persistence.project.domain.Project)
      */
    public void setProjectDeletability(Project project) {
        List<Integer> ids = EhourUtil.getIdsFromDomainObjects(project.getProjectAssignments());
        List<AssignmentAggregateReportElement> aggregates = null;

        if (ids != null && ids.size() > 0) {
            aggregates = aggregateReportService.getHoursPerAssignment(ids);
        }

        project.setDeletable(ReportUtil.isEmptyAggregateList(aggregates));
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.project.service.ProjectService#persistProject(net.rrm.ehour.persistence.persistence.project.domain.Project)
      */
    @Transactional
    @Auditable(actionType = AuditActionType.CREATE)
    public Project persistProject(Project project) {
        projectDAO.persist(project);

        userService.validateProjectManagementRoles(project.getProjectManager() == null ? null : project.getProjectManager().getUserId());

        if (project.isDefaultProject() && project.isActive()) {
            projectAssignmentManagementService.assignUsersToProjects(project);
        }

        return project;
    }

    /*
    * (non-Javadoc)
    * @see net.rrm.ehour.persistence.persistence.project.service.ProjectService#deleteProject(java.lang.Integer)
    */
    @Transactional
    @Auditable(actionType = AuditActionType.DELETE)
    public void deleteProject(Integer projectId) throws ParentChildConstraintException {
        Project project;

        project = projectDAO.findById(projectId);

        deleteEmptyAssignments(project);
        LOGGER.debug("Deleting project " + project);
        projectDAO.delete(project);
    }

    private void deleteEmptyAssignments(Project project) throws ParentChildConstraintException {
        checkProjectDeletability(project);

        if (project.getProjectAssignments() != null &&
                project.getProjectAssignments().size() > 0) {
            deleteAnyAssignments(project);
        }
    }

    private void deleteAnyAssignments(Project project) throws ParentChildConstraintException {
        for (ProjectAssignment assignment : project.getProjectAssignments()) {
            try {
                projectAssignmentManagementService.deleteProjectAssignment(assignment.getAssignmentId());
            } catch (ObjectNotFoundException e) {
                // safely ignore
            }
        }

        project.getProjectAssignments().clear();
    }

    private void checkProjectDeletability(Project project) throws ParentChildConstraintException {
        setProjectDeletability(project);

        if (!project.isDeletable()) {
            LOGGER.debug("Can't delete project, still has " + project.getProjectAssignments().size() + " assignments");
            throw new ParentChildConstraintException("Project assignments still attached");
        }
    }

    /*
      * (non-Javadoc)
      * @see net.rrm.ehour.persistence.persistence.project.service.ProjectService#getProjectManagerProjects(net.rrm.ehour.persistence.persistence.user.domain.User)
      */
    public List<Project> getProjectManagerProjects(User user) {
        return projectDAO.findActiveProjectsWhereUserIsPM(user);
    }

    public void setProjectDAO(ProjectDao dao) {
        this.projectDAO = dao;
    }

    public void setAggregateReportService(AggregateReportService aggregateReportService) {
        this.aggregateReportService = aggregateReportService;
    }
}
