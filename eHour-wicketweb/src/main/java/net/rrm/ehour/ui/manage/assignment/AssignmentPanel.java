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

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.project.service.ProjectAssignmentValidationException;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

/**
 * Assignment panel displaying the list and the tabbed form for adding/editing
 */

@SuppressWarnings("serial")
public class AssignmentPanel extends AbstractFormSubmittingPanel<Void> {
    private static final long serialVersionUID = -3721224427697057895L;
    private static final Logger LOGGER = Logger.getLogger(AssignmentPanel.class);

    @SpringBean
    private ProjectAssignmentService assignmentService;

    @SpringBean
    private ProjectAssignmentManagementService projectAssignmentManagementService;

    private AddEditTabbedPanel<AssignmentAdminBackingBean> tabbedPanel;
    private AssignmentListPanel listPanel;

    public AssignmentPanel(String id, final User user) {
        super(id);

        setOutputMarkupId(true);

        listPanel = new AssignmentListPanel("assignmentList", user);
        add(listPanel);

        tabbedPanel = new AddEditTabbedPanel<AssignmentAdminBackingBean>("assignmentTabs",
                new ResourceModel("admin.assignment.newAssignment"),
                new ResourceModel("admin.assignment.editAssignment"),
                new ResourceModel("admin.assignment.noEditEntrySelected")) {

            @Override
            protected Panel getAddPanel(String panelId) {
                return new AssignmentFormPanel(panelId,
                        new CompoundPropertyModel<>(getAddBackingBean()));
            }

            @Override
            protected Panel getEditPanel(String panelId) {
                return new AssignmentFormPanel(panelId,
                        new CompoundPropertyModel<>(getEditBackingBean()));
            }

            @Override
            protected AssignmentAdminBackingBean createAddBackingBean() {
                return AssignmentAdminBackingBean.createAssignmentAdminBackingBean(user);
            }

            @Override
            protected AssignmentAdminBackingBean createEditBackingBean() {
                return AssignmentAdminBackingBean.createAssignmentAdminBackingBean(user);
            }
        };

        add(tabbedPanel);
    }

    @Override
    public Boolean ajaxEventReceived(AjaxEvent ajaxEvent) {
        AjaxEventType type = ajaxEvent.getEventType();

        if (type == AssignmentAjaxEventType.ASSIGNMENT_EDIT) {
            if (editAssignment(ajaxEvent)) {
                return false;
            }
        } else if (type == AssignmentAjaxEventType.ASSIGNMENT_DELETED || type == AssignmentAjaxEventType.ASSIGNMENT_UPDATED) {
            modifyAssignment(ajaxEvent, type);
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private boolean editAssignment(AjaxEvent ajaxEvent) {
        try {
            ProjectAssignment assignment = ((PayloadAjaxEvent<ProjectAssignment>) ajaxEvent).getPayload();

            tabbedPanel.setEditBackingBean(new AssignmentAdminBackingBean(assignmentService.getProjectAssignment(assignment.getAssignmentId())));
            tabbedPanel.switchTabOnAjaxTarget(ajaxEvent.getTarget(), AddEditTabbedPanel.TABPOS_EDIT);
        } catch (ObjectNotFoundException e) {
            LOGGER.error("While getting assignment", e);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void modifyAssignment(AjaxEvent ajaxEvent, AjaxEventType type) {
        AssignmentAdminBackingBean backingBean = (AssignmentAdminBackingBean) ((PayloadAjaxEvent<AdminBackingBean>) ajaxEvent).getPayload();

        AjaxRequestTarget target = ajaxEvent.getTarget();

        try {
            if (type == AssignmentAjaxEventType.ASSIGNMENT_UPDATED) {
                persistAssignment(backingBean);
            } else {
                deleteAssignment(backingBean);
            }

            listPanel.updateList(target, backingBean.getUser());
            tabbedPanel.succesfulSave(target);
        } catch (ProjectAssignmentValidationException e) {
            AssignmentPersistenceError persistenceError = new AssignmentPersistenceError(target);
            send(this, Broadcast.BREADTH, persistenceError);

            LOGGER.warn("Cannot persist assignment", e);
        } catch (Exception e) {
            AssignmentPersistenceError persistenceError = new AssignmentPersistenceError(target);
            send(this, Broadcast.BREADTH, persistenceError);

            LOGGER.warn("While deleting assignment", e);
        }

    }

    private void persistAssignment(AssignmentAdminBackingBean backingBean) throws ProjectAssignmentValidationException {
        if (backingBean.isNewAssignment()) {
            List<ProjectAssignment> projectAssignmentsForSave = backingBean.getProjectAssignmentsForSave();

            for (ProjectAssignment projectAssignment : projectAssignmentsForSave) {
                projectAssignmentManagementService.persistNewProjectAssignment(projectAssignment);
            }
        } else {
            projectAssignmentManagementService.persistUpdatedProjectAssignment(backingBean.getProjectAssignmentForSave());
        }
    }

    private void deleteAssignment(AssignmentAdminBackingBean backingBean) throws ObjectNotFoundException, ParentChildConstraintException {
        projectAssignmentManagementService.deleteProjectAssignment(backingBean.getProjectAssignment());
    }
}
