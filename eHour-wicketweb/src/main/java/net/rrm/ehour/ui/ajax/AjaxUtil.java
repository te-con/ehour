package net.rrm.ehour.ui.ajax;

import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * 
 * @author Thies
 *
 */
public class AjaxUtil
{
	public static void publishEvents(WebMarkupContainer parent, AjaxEvent event)
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
			if (component instanceof AjaxAwareContainer)
			{
				((AjaxAwareContainer)component).ajaxEventReceived(event);
			}
			
			return IVisitor.CONTINUE_TRAVERSAL;
		}
		
	}
}
