package net.rrm.ehour.ui.admin.content.page;

import org.apache.wicket.model.ResourceModel;

import net.rrm.ehour.ui.admin.content.assignables.AssignablesPanel;
import net.rrm.ehour.ui.admin.content.assignees.AssigneesPanel;
import net.rrm.ehour.ui.common.page.AbstractBasePage;

/**
 * Created on Feb 6, 2010 8:12:54 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
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
}
