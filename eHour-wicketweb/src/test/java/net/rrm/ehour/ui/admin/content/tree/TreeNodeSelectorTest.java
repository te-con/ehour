package net.rrm.ehour.ui.admin.content.tree;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserDepartmentMother;
import net.rrm.ehour.domain.UserMother;
import net.rrm.ehour.ui.admin.content.assignees.UserMatcher;

import org.apache.wicket.markup.html.tree.ITreeState;
import org.junit.Before;
import org.junit.Test;


/**
 * Created on Feb 9, 2010 11:38:07 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class TreeNodeSelectorTest
{
	private DefaultTreeModel treeModel;
	private AssigneeTreeNode<User> userNode1, userNode2;
	private AssigneeTreeNode<UserDepartment>  departmentNode;
	private ITreeState treeState;
	
	@Before
	public void setUp()
	{
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		
		treeModel = new DefaultTreeModel(rootNode);
		
		treeState = createMock(ITreeState.class);
		
		UserDepartment department = UserDepartmentMother.createUserDepartment();
		
		departmentNode = new AssigneeTreeNode<UserDepartment>(department, NodeType.DEPARTMENT);
		rootNode.add(departmentNode);
		
		userNode1 = new AssigneeTreeNode<User>(department.getUsers().iterator().next(), NodeType.USER);
		departmentNode.add(userNode1);
		
		User user = UserMother.createUser();
		user.setUserId(2);

		userNode2 = new AssigneeTreeNode<User>(user, NodeType.USER);
		departmentNode.add(userNode2);
	}
	
	@Test
	public void matchUserNode()
	{
		TreeNodeSelector finder = new TreeNodeSelector(UserMatcher.getInstance());
		
		List<Integer> userIds = new ArrayList<Integer>();
		userIds.add(1);
		
		treeState.expandNode(departmentNode);
		treeState.expandNode(departmentNode);
		replay(treeState);
		
		List<AssigneeTreeNode<?>> nodes = finder.selectMatchingNodes(treeModel, treeState, userIds);
		
		assertEquals(1, nodes.size());
		assertEquals(userNode1, nodes.get(0));
		
		verify(treeState);
	}
}
