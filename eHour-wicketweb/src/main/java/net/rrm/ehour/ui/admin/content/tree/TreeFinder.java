package net.rrm.ehour.ui.admin.content.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.tree.TreeModel;

/**
 * Created on Feb 9, 2010 11:31:29 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class TreeFinder<T>
{
	private UserObjectMatcher<T> matcher;
	
	public TreeFinder(UserObjectMatcher<T> matcher)
	{
		this.matcher = matcher;
	}
	
	public List<T> findMatchingNodes(TreeModel treeModel, Collection<Integer> matchIds)
	{
		Object rootNode = treeModel.getRoot();

		List<T> matches = new ArrayList<T>();
		
		matches = findObject(treeModel, rootNode, matchIds, matches);
		
		return matches;
	}
	
	
	private List<T> findObject(TreeModel treeModel, Object parent, Collection<Integer> matchIds, List<T> matches)
	{
		int childCount = treeModel.getChildCount(parent);
		
		for (int i = 0; i < childCount; i++)
		{
			AssigneeTreeNode<?> node = (AssigneeTreeNode<?>)treeModel.getChild(parent, i);
			
			Object userObject = node.getUserObject();

			T match = matcher.isMatch(matchIds, userObject);
			
			if (match != null)
			{
				matches.add(match);
			}
			
			findObject(treeModel, node, matchIds, matches);
		}
		
		return matches;
	}
	
	public interface UserObjectMatcher<T>
	{
		public T isMatch(Collection<?> matchIds, Object userObject);
	}
}
