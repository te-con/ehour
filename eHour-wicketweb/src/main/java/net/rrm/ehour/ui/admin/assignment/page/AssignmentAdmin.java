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

package net.rrm.ehour.ui.admin.assignment.page;

import java.util.List;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.admin.BaseAdminPage;
import net.rrm.ehour.ui.admin.assignment.panel.AssignmentPanel;
import net.rrm.ehour.ui.admin.assignment.panel.NoUserSelectedPanel;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.border.GreyRoundedBorder;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorAjaxEventType;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorFilter;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import net.rrm.ehour.ui.common.util.WebWidth;
import net.rrm.ehour.user.service.UserService;
import net.rrm.ehour.util.EhourConstants;

import org.apache.log4j.Logger;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;


/**
 * Project assignments page
 **/

@SuppressWarnings("serial")
public class AssignmentAdmin extends BaseAdminPage
{
	private static final long serialVersionUID = 566527529422873370L;
	private final String			USER_SELECTOR_ID = "userSelector";
	
	@SpringBean
	private	UserService					userService;
	private EntrySelectorFilter			currentFilter;
	private	final static Logger			logger = Logger.getLogger(AssignmentAdmin.class);
	private ListView					userListView;
	private	Panel						assignmentPanel;		
	
	/**
	 * Default constructor
	 */
	public AssignmentAdmin()
	{
		super(new ResourceModel("admin.assignment.title"), null, 
				"admin.assignment.help.header",
				"admin.assignment.help.body");
		
		List<User>	users;
		users = getUsers();
		
		Fragment userListHolder = getUserListHolder(users);
		
		GreyRoundedBorder grey = new GreyRoundedBorder("entrySelectorFrame", 
																new ResourceModel("admin.assignment.title"), 
																WebWidth.ENTRYSELECTOR_WIDTH);
		add(grey);

		grey.add(new EntrySelectorPanel(USER_SELECTOR_ID,
										userListHolder,
										new StringResourceModel("admin.assignment.filter", this, null)));
		
		assignmentPanel = new NoUserSelectedPanel("assignmentPanel", "admin.assignment.noEditEntrySelected");
		
		add(assignmentPanel);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.ajax.AjaxAwareContainer#ajaxRequestReceived(org.apache.wicket.ajax.AjaxRequestTarget, int, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public boolean ajaxEventReceived(AjaxEvent ajaxEvent)
	{
		if (ajaxEvent.getEventType() == EntrySelectorAjaxEventType.FILTER_CHANGE)
		{
			PayloadAjaxEvent<EntrySelectorFilter> payload = (PayloadAjaxEvent<EntrySelectorFilter>)ajaxEvent;
			currentFilter = payload.getPayload();
			
			List<User> users = getUsers();
			userListView.setList(users);
			
		}
		
		return false;
	}	
	
	/**
	 * Get a the userListHolder fragment containing the listView
	 * @param users
	 * @return
	 */
	private Fragment getUserListHolder(List<User> users)
	{
		Fragment fragment = new Fragment("itemListHolder", "itemListHolder", AssignmentAdmin.this);
		
		userListView = new ListView("itemList", users)
		{
			@Override
			protected void populateItem(ListItem item)
			{
				final User		user = (User)item.getModelObject();
				
				AjaxLink	link = new AjaxLink("itemLink")
				{
					@Override
					public void onClick(AjaxRequestTarget target)
					{
						replaceAssignmentPanel(target, user);
					}
				};
				
				item.add(link);
				link.add(new Label("linkLabel", user.getFullName()));				
			}
		};
		
		fragment.add(userListView);
		
		return fragment;
	}	
	
	/**
	 * Set assignment panel for particular user after selection in userList panel
	 * @param target
	 * @param user
	 */
	private void replaceAssignmentPanel(AjaxRequestTarget target, User user)
	{
		AssignmentPanel	newAssignmentPanel = new AssignmentPanel("assignmentPanel",
																user);
		
		assignmentPanel.replaceWith(newAssignmentPanel);
		target.addComponent(newAssignmentPanel);
		
		assignmentPanel = newAssignmentPanel;
	}
	
	/**
	 * Get the users from the backend
	 * @return
	 */
	private List<User> getUsers()
	{
		List<User>	users;
		
		if (currentFilter == null)
		{
			users = userService.getUsers(new UserRole(EhourConstants.ROLE_CONSULTANT));
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Filtering on " + currentFilter.getCleanFilterInput());
			}
			
			users = userService.getUsersByNameMatch(currentFilter.getCleanFilterInput(), true, new UserRole(EhourConstants.ROLE_CONSULTANT));
		}
		
		return users;
	}	
}
