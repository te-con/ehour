/**
 * Created on Aug 21, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
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

package net.rrm.ehour.ui.panel.admin.common;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.ajax.AjaxAwareContainer;
import net.rrm.ehour.ui.ajax.DemoDecorator;
import net.rrm.ehour.ui.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.util.CommonUIStaticData;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.time.Duration;

/**
 * Common form stuff
 **/
@SuppressWarnings("serial")
public class FormUtil
{
	/**
	 * Get onchange validate behaviour for 1 sec
	 * @param form
	 * @return
	 */
	public static AjaxFormSubmitBehavior getValidateBehavior(Form form)
	{
		AjaxFormValidatingBehavior behavior = new AjaxFormValidatingBehavior(form, "onchange");
		behavior.setThrottleDelay(Duration.ONE_SECOND);
		return behavior;
	}
	
	/**
	 * Set submit actions for form
	 * @param form
	 */
	public static void setSubmitActions(final Form form, 
										boolean includeDelete, 
										final AjaxAwareContainer submitTarget,
										final EhourConfig config)
	{
		AjaxButton submitButton = new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				if (!config.isInDemoMode())
				{
					submitTarget.ajaxRequestReceived(target, CommonUIStaticData.AJAX_FORM_SUBMIT, form.getModel());
				}
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				if (config.isInDemoMode())
				{
					return new DemoDecorator(new ResourceModel("demoMode"));
				}
				else
				{
					return new LoadingSpinnerDecorator();
				}
			}
			
			@Override
            protected void onError(AjaxRequestTarget target, Form form)
			{
				target.addComponent(form);
            }
        };
        
        
        submitButton.setModel(new ResourceModel("general.save"));
		// default submit
		form.add(submitButton);

		AjaxLink deleteButton = new AjaxLink("deleteButton")
        {
			@Override
            public void onClick(AjaxRequestTarget target)
			{
				if (!config.isInDemoMode())
				{
					submitTarget.ajaxRequestReceived(target, CommonUIStaticData.AJAX_DELETE, form.getModel());
				}
            }

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator()
			{
				if (config.isInDemoMode())
				{
					return new DemoDecorator(new ResourceModel("demoMode"));
				}
				else
				{
					return new LoadingSpinnerDecorator();
				}
			}		
        };
        
        deleteButton.add(new JavaScriptConfirmation("onclick", new ResourceModel("deleteConfirmation")));
        deleteButton.setVisible(includeDelete);
        form.add(deleteButton);
	}	
}
