package net.rrm.ehour.ui.admin.content.tree;

import static net.rrm.ehour.ui.admin.content.tree.UserObjectMatcher.MatchResult.MATCH;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;

import net.rrm.ehour.ui.admin.content.tree.UserObjectMatcher.MatchResult;

import org.apache.wicket.markup.html.tree.ITreeState;

/**
 * Created on Feb 9, 2010 11:31:29 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class TreeNodeSelector
{
	private UserObjectMatcher matcher;
	
	public TreeNodeSelector(UserObjectMatcher matcher)
	{
		this.matcher = matcher;
	}
	
	public List<AssigneeTreeNode<?>> selectMatchingNodes(DefaultTreeModel treeModel, ITreeState treeState, Collection<Integer> matchIds)
	{
		Object rootNode = treeModel.getRoot();

		List<AssigneeTreeNode<?>> matches = new ArrayList<AssigneeTreeNode<?>>();
		
		matches = selectNode(treeModel, treeState, rootNode, matchIds, matches);
		
		return matches;
	}
	
	private List<AssigneeTreeNode<?>> selectNode(DefaultTreeModel treeModel, ITreeState treeState, Object parent, Collection<Integer> matchIds, List<AssigneeTreeNode<?>> matches)
	{
		int childCount = treeModel.getChildCount(parent);
		
		for (int i = 0; i < childCount; i++)
		{
			AssigneeTreeNode<?> node = (AssigneeTreeNode<?>)treeModel.getChild(parent, i);
			
			Object userObject = node.getUserObject();
			
			MatchResult matchResult = matcher.matches(matchIds, userObject);

			if (matchResult.isCompatible()) 
			{
				boolean updateForSelectableStateSwitch = !node.isSelectable();
				
				boolean updateForSelectedStateSwitch = node.isSelected();
				updateForSelectedStateSwitch ^= matchResult == MATCH;
				
				boolean needUpdate = updateForSelectableStateSwitch | updateForSelectedStateSwitch;
				
				node.setSelectable(true);
				node.setSelected(matchResult == MATCH);

				if (needUpdate)
				{
					treeModel.nodeChanged(node);
				}
				
				treeState.expandNode(node.getParent());
				
				if (matchResult == MATCH)
				{
					matches.add(node);
				}
			}
			else
			{
				if (node.isSelectable())
				{
					node.setSelectable(false);
					treeModel.nodeChanged(node);
				}
				
				node.setSelected(false);
			}
			
			
			selectNode(treeModel, treeState, node, matchIds, matches);
		}
		
		return matches;
	}
}
