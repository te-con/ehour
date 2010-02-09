package net.rrm.ehour.ui.admin.content.page;

import java.util.Set;

import net.rrm.ehour.ui.admin.content.assignables.AssignablesPanel;
import net.rrm.ehour.ui.admin.content.assignees.AssigneesPanel;
import net.rrm.ehour.ui.admin.content.tree.AssigneeTreeNode;
import net.rrm.ehour.ui.admin.content.tree.TreeNodeEventType;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.page.AbstractBasePage;

import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.model.ResourceModel;

/**
 * Created on Feb 6, 2010 8:12:54 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
@AuthorizeInstantiation("ROLE_ADMIN")
public class ContentAdminPage extends AbstractBasePage<Void>
{
	public ContentAdminPage()
	{
		super(new ResourceModel("admin.content.title"));
		
		initComponents();
	}
	
	private void initComponents()
	{
		AssigneesPanel assigneesPanel = new AssigneesPanel("assignees");
		add(assigneesPanel);

		AssignablesPanel assignablesPanel = new AssignablesPanel("assignables");
		add(assignablesPanel);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean ajaxEventReceived(AjaxEvent event)
	{
		if (event.getEventType() == TreeNodeEventType.NODE_SELECTED)
		{
			PayloadAjaxEvent<AssigneeTreeNode> payloadEvent = (PayloadAjaxEvent<AssigneeTreeNode>)event;
			Set objects = payloadEvent.getPayload().getSelectedNodeObjects();
			
			for (Object object : objects)
			{
				System.out.println(object);
			}
		}
		
		return true;
	}
}
