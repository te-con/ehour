package net.rrm.ehour.ui.admin.project.panel.addusers;

import java.util.List;

import net.rrm.ehour.domain.User;

import org.apache.wicket.markup.html.panel.Panel;

public class ListUsersPanel extends Panel
{
	private static final long serialVersionUID = 3469910926919217439L;

	public ListUsersPanel(String id, List<User> users)
	{
		super(id);
	}
}
