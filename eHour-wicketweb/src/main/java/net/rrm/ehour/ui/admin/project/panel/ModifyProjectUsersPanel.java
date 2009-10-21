package net.rrm.ehour.ui.admin.project.panel;

import java.util.List;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.project.service.ProjectAssignmentService;
import net.rrm.ehour.ui.admin.project.panel.addusers.AddUserPanel;
import net.rrm.ehour.ui.admin.project.panel.editusers.ListCurrentProjectUsersPanel;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.PlaceholderPanel;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.util.WebGeo;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ModifyProjectUsersPanel extends AbstractFormSubmittingPanel
{
	private static final long serialVersionUID = -2500957894019585211L;

	@SpringBean
	private ProjectAssignmentManagementService assignmentManagementService;

	@SpringBean
	private ProjectAssignmentService assignmentService;

	private Component message;
	
	public ModifyProjectUsersPanel(String id, Project project)
	{
		super(id);
		
		addModifyPanel(project);
		
		addAddPanel(project);
	}

	private void addAddPanel(Project project)
	{
		Border border = new GreySquaredRoundedBorder("addBorder", WebGeo.W_CONTENT_ADMIN_TAB_WIDE);
		add(border);
		
		border.add(new AddUserPanel("addUsers", project));
		
	}
	
	@SuppressWarnings("serial")
	private void addModifyPanel(Project project)
	{
		Border border = new GreySquaredRoundedBorder("border", WebGeo.W_CONTENT_ADMIN_TAB_WIDE);
		add(border);

		final List<ProjectAssignment> assignments = assignmentService.getProjectAssignments(project, true);
		
		ListCurrentProjectUsersPanel currentProjectUsersPanel = new ListCurrentProjectUsersPanel("currentUsers", assignments);
		border.add(currentProjectUsersPanel);
		
		message = new PlaceholderPanel("message");
		border.add(message);
		
		AjaxLink link = new AjaxLink("submit")
		{
			@Override
			public void onClick(AjaxRequestTarget target)
			{
				for (ProjectAssignment projectAssignment : assignments)
				{
					assignmentManagementService.updateProjectAssignment(projectAssignment);
				}
				
				Label label = new Label("message", new ResourceModel("admin.project.assignmentsUpdated"));
				label.setOutputMarkupId(true);
				
				message.replaceWith(label);
				message = label;
				target.addComponent(message);
			}
		};
		
		border.add(link);
	}

}
