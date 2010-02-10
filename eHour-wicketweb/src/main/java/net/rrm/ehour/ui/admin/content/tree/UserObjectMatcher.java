package net.rrm.ehour.ui.admin.content.tree;

import java.util.Collection;

/**
 * Created on Feb 10, 2010 7:54:55 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public interface UserObjectMatcher
{
	public enum MatchResult
	{
		MATCH(true),
		NO_MATCH(true),
		INCOMPATIBLE(false);
		
		private boolean compatible;
		
		private MatchResult(boolean compatible)
		{
			this.compatible= compatible;
		}
		
		public boolean isCompatible()
		{
			return compatible;
		}
	}
	
	public MatchResult matches(Collection<?> matchIds, Object userObject);
}
