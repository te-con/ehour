package net.rrm.ehour.ui.admin.content.assignees;

import java.util.Collection;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.ui.admin.content.tree.UserObjectMatcher;

/**
 * Created on Feb 9, 2010 11:44:25 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class UserMatcher implements UserObjectMatcher
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
	public MatchResult matches(Collection<?> matchIds, Object userObject)
	{
		if (userObject instanceof User)
		{
			User user = (User)userObject;
			
			return matchIds.contains(user.getUserId()) ? MatchResult.MATCH : MatchResult.NO_MATCH;
		}
			
		return MatchResult.INCOMPATIBLE;
	}
}
