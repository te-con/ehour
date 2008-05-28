package net.rrm.ehour.ui.ajax;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.Component.IVisitor;

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
	
	/**
	 * 
	 * @param parent
	 * @param event
	 */
	public static void publishAjaxEventToChildren(MarkupContainer parent, AjaxEvent event)
	{
		IVisitor visitor = new AjaxEventVisitor(event);
		
		visitor.component(parent.getPage());
		parent.getPage().visitChildren(new AjaxEventVisitor(event));
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
	private static class AjaxEventVisitor implements IVisitor
	{
		private AjaxEvent event;
		
		private AjaxEventVisitor(AjaxEvent ajaxEvent)
		{
			this.event = ajaxEvent;
		}

		public Object component(Component component)
		{
			boolean proceed = true;
			
			if (component instanceof AjaxAwareContainer)
			{
				proceed = ((AjaxAwareContainer)component).ajaxEventReceived(event);
			}
			
			return proceed ? IVisitor.CONTINUE_TRAVERSAL : IVisitor.STOP_TRAVERSAL;
		}
		
	}	
}
