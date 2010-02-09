package net.rrm.ehour.ui.admin.content.assignees;

import java.util.Collection;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.admin.content.tree.TreeFinder;

/**
 * Created on Feb 9, 2010 11:44:25 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class UserMatcher implements TreeFinder.UserObjectMatcher
{
	private static final UserMatcher instance = new UserMatcher();
	
	public static UserMatcher getInstance()
	{
		return instance;
	}
	
	private UserMatcher()
	{
		
	}
	
	@Override
	public boolean isMatch(Collection<?> matchIds, Object userObject)
	{
		if (userObject instanceof User)
		{
			User user = (User)userObject;
			
			return matchIds.contains(user.getUserId());
		}
			
		return false;
	}
}
