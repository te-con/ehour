package net.rrm.ehour.ui.admin.project.panel.addusers;

import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.service.user.service.UserService;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class ListUsersPanel extends Panel
{
	private static final long serialVersionUID = 3469910926919217439L;

	@SpringBean(name = "userService")
	private UserService userService;
	
	public ListUsersPanel(String id, List<User> selectedUsers)
	{
		super(id);
		
		CheckGroup<User> checkers = new CheckGroup<User>("group", selectedUsers);

		List<User> users = userService.getUsers(UserRole.CONSULTANT);
		ListView<User> listView = createUserList(users);
		checkers.add(listView);
		add(checkers);
	}
	
	@SuppressWarnings("serial")
	private ListView<User> createUserList(List<User> users)
	{
		ListView<User> listView = new ListView<User>("users",users)
		{
			@Override
			protected void populateItem(ListItem<User> item)
			{
				User user = item.getModelObject();
				
				item.add(new Check<User>("check", item.getModel()));
				item.add(new Label("name", user.getFullName()));
			}
		};
		return listView;
	}
}
