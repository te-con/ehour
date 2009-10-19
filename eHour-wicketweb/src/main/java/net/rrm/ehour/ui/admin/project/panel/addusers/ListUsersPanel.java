package net.rrm.ehour.ui.admin.project.panel.addusers;

import java.util.List;

import net.rrm.ehour.domain.User;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public class ListUsersPanel extends Panel
{
	private static final long serialVersionUID = 3469910926919217439L;

	public ListUsersPanel(String id, List<User> users)
	{
		super(id);
		
		ListView listView = createUserList(users);
		add(listView);
	}

	@SuppressWarnings("serial")
	private ListView createUserList(List<User> users)
	{
		ListView listView = new ListView("users",users)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				User user = (User)item.getModelObject();
				
				item.add(new Label("name", user.getFullName()));
			}
		};
		return listView;
	}
}
