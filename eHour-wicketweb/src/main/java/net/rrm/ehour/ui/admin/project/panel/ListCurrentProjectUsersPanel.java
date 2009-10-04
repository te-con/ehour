package net.rrm.ehour.ui.admin.project.panel;

import java.util.List;

import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

public class ListCurrentProjectUsersPanel extends AbstractBasePanel
{
	private static final long serialVersionUID = 7558151841882107334L;

	@SuppressWarnings("serial")
	public ListCurrentProjectUsersPanel(String id, List<ProjectAssignment> assignments)
	{
		super(id);
		
		ListView assignmentList = new ListView("assignmentList", assignments)
		{
			
			@Override
			protected void populateItem(ListItem item)
			{
				ProjectAssignment assignment = (ProjectAssignment)item.getModelObject();
				
				item.add(new Label("user", assignment.getUser().getFullName()));
			}
		};
		
		add(assignmentList);
	}
}
