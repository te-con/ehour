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
public class TreeFinder
{
	private UserObjectMatcher matcher;
	
	public TreeFinder(UserObjectMatcher matcher)
	{
		this.matcher = matcher;
	}
	
	public List<AssigneeTreeNode<?>> findMatchingNodes(TreeModel treeModel, Collection<Integer> matchIds)
	{
		Object rootNode = treeModel.getRoot();

		List<AssigneeTreeNode<?>> matches = new ArrayList<AssigneeTreeNode<?>>();
		
		matches = findObject(treeModel, rootNode, matchIds, matches);
		
		return matches;
	}
	
	
	private List<AssigneeTreeNode<?>> findObject(TreeModel treeModel, Object parent, Collection<Integer> matchIds, List<AssigneeTreeNode<?>> matches)
	{
		int childCount = treeModel.getChildCount(parent);
		
		for (int i = 0; i < childCount; i++)
		{
			AssigneeTreeNode<?> node = (AssigneeTreeNode<?>)treeModel.getChild(parent, i);
			
			Object userObject = node.getUserObject();

			if (matcher.isMatch(matchIds, userObject))
			{
				matches.add(node);
			}
			
			findObject(treeModel, node, matchIds, matches);
		}
		
		return matches;
	}
	
	public interface UserObjectMatcher
	{
		public boolean isMatch(Collection<?> matchIds, Object userObject);
	}
}
