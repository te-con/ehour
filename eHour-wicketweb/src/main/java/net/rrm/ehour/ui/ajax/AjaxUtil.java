package net.rrm.ehour.ui.ajax;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;

/**
 * 
 * @author Thies
 *
 */
public class AjaxUtil
{
	/**
	 * Publish ajaxEvent at node and move up the tree
	 * @param ajaxEvent
	 */
	public static void publishAjaxEvent(MarkupContainer node, AjaxEvent ajaxEvent)
	{
		boolean proceed = true;
		
		if (node instanceof AjaxAwareContainer)
		{
			proceed = ((AjaxAwareContainer)node).ajaxEventReceived(ajaxEvent);
		}
		
		if (proceed && !(node instanceof Page))
		{
			MarkupContainer parent = node.getParent();
			
			if (parent == null)
			{
				parent = node.getPage();
			}
			
			
			publishAjaxEvent(parent, ajaxEvent);
		}
	}
}
