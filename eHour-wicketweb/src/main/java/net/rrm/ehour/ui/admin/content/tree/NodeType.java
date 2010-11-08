package net.rrm.ehour.ui.admin.content.tree;

/**
 * Created on Feb 9, 2010 3:58:15 PM
 *
 * @author thies (www.te-con.nl)
 *
 */
public enum NodeType
{
	DEPARTMENT(true),
	USER(true),
	CUSTOMER(false),
	PROJECT(false);
	
	private boolean assignee;
	
	private NodeType(boolean assignee)
	{
		this.assignee = assignee;
	}
	
	/**
	 * @return the assignee
	 */
	public boolean isAssignee()
	{
		return assignee;
	}
}
