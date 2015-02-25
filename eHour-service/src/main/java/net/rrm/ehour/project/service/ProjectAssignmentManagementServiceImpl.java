package net.rrm.ehour.project.service;

import com.google.common.collect.Lists;
import net.rrm.ehour.audit.annot.Auditable;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.persistence.timesheet.dao.TimesheetDao;
import net.rrm.ehour.persistence.user.dao.UserDao;
import net.rrm.ehour.project.service.ProjectAssignmentValidationException.Issue;
import net.rrm.ehour.user.service.UserUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service("projectAssignmentManagementService")
public class ProjectAssignmentManagementServiceImpl implements ProjectAssignmentManagementService {
    private static final Logger LOGGER = Logger.getLogger(ProjectAssignmentServiceImpl.class);

    private final UserDao userDAO;

    private final ProjectDao projectDao;

    private final ProjectAssignmentDao projectAssignmentDao;
    private final TimesheetDao timesheetDao;

    @Autowired
    public ProjectAssignmentManagementServiceImpl(UserDao userDAO,
                                                  ProjectDao projectDao,
                                                  ProjectAssignmentDao projectAssignmentDao,
                                                  TimesheetDao timesheetDao) {
        this.userDAO = userDAO;
        this.projectDao = projectDao;
        this.projectAssignmentDao = projectAssignmentDao;
        this.timesheetDao = timesheetDao;
    }

    @Transactional
    @Auditable(actionType = AuditActionType.CREATE)
    public void assignAllUsersToProject(Project project) {
        List<User> users = UserUtil.filterUserOnRole(userDAO.findActiveUsers(), UserRole.USER);

        for (User user : users) {
            ProjectAssignment assignment = ProjectAssignment.createProjectAssignment(project, user);

            if (!isAlreadyAssigned(assignment, user.getProjectAssignments())) {
                LOGGER.debug("Assigning user " + user + " to " + project);
                persistNewProjectAssignment(assignment);
            }
        }
    }

    @Override
    @Transactional
    @Auditable(actionType = AuditActionType.CREATE)
    public void assignUsersToProjects(List<User> users, ProjectAssignment assignmentTemplate) {
        for (User user : users) {
            ProjectAssignment assignment = ProjectAssignment.createProjectAssignment(assignmentTemplate, user);
            persistNewProjectAssignment(assignment);
        }
    }

    /**
     * Assign user to project
     */
    @Transactional
    @Auditable(actionType = AuditActionType.UPDATE)
    public ProjectAssignment persistUpdatedProjectAssignment(ProjectAssignment projectAssignment) throws ProjectAssignmentValidationException {
        if (!projectAssignment.isNew()) {
            List<Issue> issues = Lists.newArrayList();

            if (!timesheetDao.getTimesheetEntriesAfter(projectAssignment, projectAssignment.getDateEnd()).isEmpty()) {
                issues.add(Issue.EXISTING_DATA_AFTER_END);
            }

            if (!timesheetDao.getTimesheetEntriesBefore(projectAssignment, projectAssignment.getDateStart()).isEmpty()) {
                issues.add(Issue.EXISTING_DATA_BEFORE_START);
            }

            if (!issues.isEmpty()) {
                throw new ProjectAssignmentValidationException(issues);
            }
        }

        return projectAssignmentDao.persist(projectAssignment);
    }

    @Override
    public ProjectAssignment persistNewProjectAssignment(ProjectAssignment assignment) {
        return projectAssignmentDao.persist(assignment);
    }

    /**
     * Assign user to default projects
     */
    @Transactional
    @Auditable(actionType = AuditActionType.UPDATE)
    public User assignUserToDefaultProjects(User user) {
        List<Project> defaultProjects = projectDao.findDefaultProjects();

        for (Project project : defaultProjects) {
            ProjectAssignment assignment = ProjectAssignment.createProjectAssignment(project, user);

            if (!isAlreadyAssigned(assignment, user.getProjectAssignments())) {
                LOGGER.debug("Assigning user " + user.getUserId() + " to default project " + project.getName());
                user.addProjectAssignment(assignment);

                persistNewProjectAssignment(assignment);
            }
        }

        return user;
    }

    /**
     * Check if this default assignment is already assigned
     */
    private boolean isAlreadyAssigned(ProjectAssignment projectAssignment, Collection<ProjectAssignment> assignments) {
        boolean alreadyAssigned = false;

        if (assignments == null) {
            return false;
        }

        int projectId = projectAssignment.getProject().getProjectId();

        for (ProjectAssignment assignment : assignments) {
            if (assignment.getProject().getProjectId() == projectId) {
                LOGGER.debug("Default assignment is already assigned as assignmentId " + assignment.getAssignmentId());

                alreadyAssigned = true;
                break;
            }
        }

        return alreadyAssigned;
    }

    @Transactional
    @Auditable(actionType = AuditActionType.DELETE)
    public void deleteProjectAssignment(ProjectAssignment assignment) {
        projectAssignmentDao.delete(assignment);
    }
}
