package net.rrm.ehour.project.service;

import net.rrm.ehour.audit.annot.Auditable;
import net.rrm.ehour.domain.*;
import net.rrm.ehour.persistence.project.dao.ProjectAssignmentDao;
import net.rrm.ehour.persistence.project.dao.ProjectDao;
import net.rrm.ehour.user.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service("projectAssignmentManagementService")
public class ProjectAssignmentManagementServiceImpl implements ProjectAssignmentManagementService {
    private static final Logger LOGGER = Logger.getLogger(ProjectAssignmentServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectDao projectDAO;

    @Autowired
    private ProjectAssignmentDao projectAssignmentDAO;

    @Transactional
    @Auditable(actionType = AuditActionType.CREATE)
    public void assignAllUsersToProject(Project project) {
        List<User> users = userService.getUsers(UserRole.CONSULTANT);

        for (User user : users) {
            ProjectAssignment assignment = ProjectAssignment.createProjectAssignment(project, user);

            if (!isAlreadyAssigned(assignment, user.getProjectAssignments())) {
                LOGGER.debug("Assigning user " + user + " to " + project);
                assignUserToProject(assignment);
            }
        }
    }

    @Override
    @Transactional
    @Auditable(actionType = AuditActionType.CREATE)
    public void assignUsersToProjects(List<User> users, ProjectAssignment assignmentTemplate) {
        for (User user : users) {
            ProjectAssignment assignment = ProjectAssignment.createProjectAssignment(assignmentTemplate, user);

            projectAssignmentDAO.persist(assignment);
        }
    }

    /**
     * Assign user to project
     */
    @Transactional
    @Auditable(actionType = AuditActionType.UPDATE)
    public ProjectAssignment assignUserToProject(ProjectAssignment projectAssignment) {
        projectAssignmentDAO.persist(projectAssignment);

        return projectAssignment;
    }

    /**
     * Assign user to default projects
     */
    @Transactional
    @Auditable(actionType = AuditActionType.UPDATE)
    public User assignUserToDefaultProjects(User user) {
        List<Project> defaultProjects = projectDAO.findDefaultProjects();

        for (Project project : defaultProjects) {
            ProjectAssignment assignment = ProjectAssignment.createProjectAssignment(project, user);

            if (!isAlreadyAssigned(assignment, user.getProjectAssignments())) {
                LOGGER.debug("Assigning user " + user.getUserId() + " to default project " + project.getName());
                user.addProjectAssignment(assignment);

                projectAssignmentDAO.persist(assignment);
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
        projectAssignmentDAO.delete(assignment);
    }

    @Transactional
    public void updateProjectAssignment(ProjectAssignment assignment) {
        projectAssignmentDAO.persist(assignment);
    }

 public void setProjectDAO(ProjectDao projectDAO) {
        this.projectDAO = projectDAO;
    }

    public void setProjectAssignmentDAO(ProjectAssignmentDao projectAssignmentDAO) {
        this.projectAssignmentDAO = projectAssignmentDAO;
    }

}
