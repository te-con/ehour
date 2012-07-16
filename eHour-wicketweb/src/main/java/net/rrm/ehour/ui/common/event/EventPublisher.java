/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.common.event;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Page;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public class EventPublisher
{
	private EventPublisher() {
	}
	
	// purely for junit testing
	public static AjaxEventListener listenerHook;

	/**
	 * Publish ajax event to container and upwards upto and including the page
	 */
	public static void publishAjaxEvent(Component container, AjaxEvent event)
	{
		notifyHook(event);

		recursivePublishAjaxEvent(container, event);
	}

	private static void recursivePublishAjaxEvent(Component container, AjaxEvent event)
	{
		boolean proceed = true;

		if (container instanceof AjaxEventListener)
		{
			proceed = ((AjaxEventListener)container).ajaxEventReceived(event);
		}
		
		if (proceed && !(container instanceof Page))
		{
			MarkupContainer parent = container.getParent();
			
			if (parent == null)
			{
				parent = container.getPage();
			}
			
			recursivePublishAjaxEvent(parent, event);
		}
	}
	
	/**
	 * Publish ajax event to page itself and then all it's children
	 */
	public static void publishAjaxEventToPageChildren(MarkupContainer parent, AjaxEvent event)
	{
		notifyHook(event);

		IVisitor<Component> visitor = new AjaxEventVisitor(event);
		
		visitor.component(parent.getPage());
		parent.getPage().visitChildren(AjaxEventListener.class, visitor);
	}
	
	/**
	 * Publish ajax event to children of parent container
	 * @param parent
	 * @param event
	 */
	public static void publishAjaxEventToParentChildren(MarkupContainer parent, AjaxEvent event)
	{
		notifyHook(event);
		
		parent.visitChildren(AjaxEventListener.class, new AjaxEventVisitor(event));
	}	
	
	private static void notifyHook(AjaxEvent event)
	{
		if (listenerHook != null)
		{
			listenerHook.ajaxEventReceived(event);
		}
	}
	
	private static class AjaxEventVisitor implements IVisitor<Component>
	{
		private AjaxEvent event;
		
		private AjaxEventVisitor(AjaxEvent ajaxEvent)
		{
			this.event = ajaxEvent;
		}

		public Object component(Component component)
		{
			boolean proceed = true;
			
			if (component instanceof AjaxEventListener)
			{
				proceed = ((AjaxEventListener)component).ajaxEventReceived(event);
			}
			
			return proceed ? IVisitor.CONTINUE_TRAVERSAL : IVisitor.STOP_TRAVERSAL;
		}

        @Override
        public void component(Component component, IVisit iVisit) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
