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

package net.rrm.ehour.ui.common.component;

import org.apache.wicket.Component;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.util.time.Duration;

/**
 * Validate formcomponent without submitting the form
 */

public class ValidatingFormComponentAjaxBehavior extends AjaxFormComponentUpdatingBehavior
{
	private static final long serialVersionUID = 5521068510893118073L;

	public ValidatingFormComponentAjaxBehavior()
	{
		super("onchange");
		
		setThrottleDelay(Duration.ONE_SECOND);
	}
	
	@Override
	protected void onError(final AjaxRequestTarget target, RuntimeException e)
	{
		super.onError(target, e);
		addFeedbackPanels(target);

	}

	@Override
	protected void onUpdate(final AjaxRequestTarget target)
	{
		addFeedbackPanels(target);
	}
	
	private void addFeedbackPanels(final AjaxRequestTarget target)
	{
		getComponent().getPage().visitChildren(IFeedback.class, new IVisitor<Component>()
		{
			public Object component(Component component)
			{
				if (component instanceof AjaxFormComponentFeedbackIndicator)
				{
					if ( ((AjaxFormComponentFeedbackIndicator)component).getIndicatorFor() == getFormComponent())
					{
						target.addComponent(component);
					}
				}
				else
				{
					target.addComponent(component);
				}
			
				return IVisitor.CONTINUE_TRAVERSAL;
			}
		});		
	}
}