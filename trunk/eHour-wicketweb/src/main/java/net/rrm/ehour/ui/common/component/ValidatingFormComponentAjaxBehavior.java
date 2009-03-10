/**
 * Created on Jan 22, 2008
 * Author: Thies
 *
 * Copyright (C) 2007 TE-CON, All Rights Reserved.
 *
 * This Software is copyright TE-CON 2007. This Software is not open source by definition. The source of the Software is available for educational purposes.
 * TE-CON holds all the ownership rights on the Software.
 * TE-CON freely grants the right to use the Software. Any reproduction or modification of this Software, whether for commercial use or open source,
 * is subject to obtaining the prior express authorization of TE-CON.
 * 
 * thies@te-con.nl
 * TE-CON
 * Legmeerstraat 4-2h, 1058ND, AMSTERDAM, The Netherlands
 *
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
		getComponent().getPage().visitChildren(IFeedback.class, new IVisitor()
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