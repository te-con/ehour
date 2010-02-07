package net.rrm.ehour.ui.admin.content.assignees.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import net.rrm.ehour.domain.DomainObject;

/**
 * Created on Feb 6, 2010 10:34:58 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public class AssigneeTreeNode<T extends DomainObject<?, ?>> extends DefaultMutableTreeNode
{
	private static final long serialVersionUID = -4757873193229912865L;

	public AssigneeTreeNode(T userObject)
	{
		super(userObject);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String toString()
	{
		return ((T)getUserObject()).getFullName(); 
	}
	
	
}
