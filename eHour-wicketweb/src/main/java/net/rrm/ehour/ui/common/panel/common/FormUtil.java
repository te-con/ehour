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

package net.rrm.ehour.ui.common.panel.common;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.ajax.AjaxEvent;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.ajax.AjaxUtil;
import net.rrm.ehour.ui.common.ajax.DemoDecorator;
import net.rrm.ehour.ui.common.ajax.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.ajax.PayloadAjaxEvent;
import net.rrm.ehour.ui.common.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.common.model.AdminBackingBean;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Common form stuff
 **/
@SuppressWarnings("serial")
public class FormUtil
{
	/**
	 * Set submit actions for form
	 * @param form
	 */
	public static void setSubmitActions(final Form form, 
										boolean includeDelete, 
										final MarkupContainer submitTarget,
										final AjaxEventType submitEventType,
										final AjaxEventType deleteEventType,
										final EhourConfig config)
	{
		setSubmitActions(form, includeDelete, submitTarget, submitEventType, deleteEventType, null, config);
	}

	/**
	 * 
	 * @param form
	 * @param includeDelete
	 * @param submitTarget
	 * @param submitEventType
	 * @param deleteEventType
	 * @param errorEventType
	 * @param config
	 */
	public static void setSubmitActions(final Form form, 
										boolean includeDelete, 
										final MarkupContainer submitTarget,
										final AjaxEventType submitEventType,
										final AjaxEventType deleteEventType,
										final AjaxEventType errorEventType,
										final EhourConfig config)
	{
		AjaxButton submitButton = new AjaxButton("submitButton", form)
		{
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form form)
			{
				if (!config.isInDemoMode())
				{
					AdminBackingBean backingBean = (AdminBackingBean) (((IWrapModel)form.getModel()).getWrappedModel()).getObject();
					PayloadAjaxEvent<AdminBackingBean> ajaxEvent = new PayloadAjaxEvent<AdminBackingBean>(target, submitEventType, backingBean);
					
					AjaxUtil.publishAjaxEvent(submitTarget, ajaxEvent);
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
				
				if (errorEventType != null)
				{
					AjaxEvent errorEvent = new AjaxEvent(target, errorEventType);
					AjaxUtil.publishAjaxEvent(submitTarget, errorEvent);
				}
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
					AdminBackingBean backingBean = (AdminBackingBean) (((IWrapModel)form.getModel()).getWrappedModel()).getObject();
					PayloadAjaxEvent<AdminBackingBean> ajaxEvent = new PayloadAjaxEvent<AdminBackingBean>(target, deleteEventType, backingBean);
					
					AjaxUtil.publishAjaxEvent(submitTarget, ajaxEvent);
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
        
        deleteButton.add(new JavaScriptConfirmation("onclick", new ResourceModel("general.deleteConfirmation")));
        deleteButton.setVisible(includeDelete);
        form.add(deleteButton);
	}	
}
