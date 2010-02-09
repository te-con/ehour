package net.rrm.ehour.ui.admin.content.tree;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.domain.UserDepartment;
import net.rrm.ehour.domain.UserDepartmentMother;
import net.rrm.ehour.domain.UserMother;
import net.rrm.ehour.ui.admin.content.assignees.UserMatcher;

import org.junit.Before;
import org.junit.Test;


/**
 * Created on Feb 9, 2010 11:38:07 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class TreeFinderTest
{
	private TreeModel treeModel;

	@Before
	public void setUp()
	{
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
		
		treeModel = new DefaultTreeModel(rootNode);
		
		UserDepartment department = UserDepartmentMother.createUserDepartment();
		
		AssigneeTreeNode<UserDepartment> departmentNode = new AssigneeTreeNode<UserDepartment>(department, NodeType.DEPARTMENT);
		rootNode.add(departmentNode);
		
		AssigneeTreeNode<User> userNode = new AssigneeTreeNode<User>(department.getUsers().iterator().next(), NodeType.USER);
		departmentNode.add(userNode);
		
		User user = UserMother.createUser();
		user.setUserId(2);

		userNode = new AssigneeTreeNode<User>(user, NodeType.USER);
		departmentNode.add(userNode);
	}
	
	@Test
	public void findUser()
	{
		TreeFinder<User> finder = new TreeFinder<User>(UserMatcher.getInstance());
		
		List<Integer> userIds = new ArrayList<Integer>();
		userIds.add(1);
		
		List<User> nodes = finder.findMatchingNodes(treeModel, userIds);
		
		assertEquals(1, nodes.size());
	}
}
