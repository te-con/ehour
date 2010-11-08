package net.rrm.ehour.ui.admin.content.page;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.project.dto.ProjectAssignmentCollection;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.ui.admin.content.assignables.AssignablesPanel;
import net.rrm.ehour.ui.admin.content.assignees.AssigneesPanel;
import net.rrm.ehour.ui.admin.content.assignment.ProjectAssignmentManagementPanel;
import net.rrm.ehour.ui.admin.content.tree.AssigneeTreeNode;
import net.rrm.ehour.ui.admin.content.tree.TreeNodeEventType;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.page.AbstractBasePage;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Created on Feb 6, 2010 8:12:54 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
@AuthorizeInstantiation("ROLE_ADMIN")
public class ContentAdminPage extends AbstractBasePage<Void>
{
	private static final String ID_ASSIGNMENT_MANAGEMENT = "assignmentManagement";
	private static final String ID_ASSIGNABLES = "assignables";
	private static final String ID_ASSIGNEES = "assignees";

	private AssignablesPanel assignablesPanel;
	private AssigneesPanel assigneesPanel;
	private Component managementPanel;

	@SpringBean(name = "projectAssignmentManagementService")
	private ProjectAssignmentManagementService projectAssignmentManagementService;
	
	public ContentAdminPage()
	{
		super(new ResourceModel("admin.content.title"));
		
		initComponents();
	}
	
	private void initComponents()
	{
		assigneesPanel = new AssigneesPanel(ID_ASSIGNEES);
		add(assigneesPanel);

		assignablesPanel = new AssignablesPanel(ID_ASSIGNABLES);
		add(assignablesPanel);
		
		managementPanel = new PlaceholderPanel(ID_ASSIGNMENT_MANAGEMENT);
		add(managementPanel);
	}
	
	@Override
	public boolean ajaxEventReceived(AjaxEvent event)
	{
		if (event.getEventType() == TreeNodeEventType.NODE_SELECTED)
		{
			onNodeSelectedEvent(event);
		}
		
		return true;
	}

	@SuppressWarnings("unchecked")
	private void onNodeSelectedEvent(AjaxEvent event)
	{
		PayloadAjaxEvent<AssigneeTreeNode<?>> payloadEvent = (PayloadAjaxEvent<AssigneeTreeNode<?>>)event;
		
		AssigneeTreeNode<?> node = payloadEvent.getPayload();
		
		if (!node.getNodeType().isAssignee())
		{
			onAssignableNodeEvent(event, node);
		}
	}

	private void updateManagementPanel(AjaxEvent event, ProjectAssignmentCollection collection)
	{
		ProjectAssignmentManagementPanel panel = new ProjectAssignmentManagementPanel(ID_ASSIGNMENT_MANAGEMENT, collection);
		panel.setOutputMarkupId(true);
		managementPanel.replaceWith(panel);
		managementPanel = panel;
		event.getTarget().addComponent(panel);
		
	}

	private void onAssignableNodeEvent(AjaxEvent event, AssigneeTreeNode<?> node)
	{
		Project project = (Project)node.getUserObject();
		
		ProjectAssignmentCollection collection = projectAssignmentManagementService.getCombinedProjectAssignments(project.getPK());
		
		assigneesPanel.selectUsers(collection.getUserIds());
		
		updateManagementPanel(event, collection);
	}
}
