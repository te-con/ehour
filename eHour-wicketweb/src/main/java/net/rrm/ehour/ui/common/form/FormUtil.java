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

package net.rrm.ehour.ui.common.form;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.ui.common.component.JavaScriptConfirmation;
import net.rrm.ehour.ui.common.event.AjaxEvent;
import net.rrm.ehour.ui.common.event.AjaxEventType;
import net.rrm.ehour.ui.common.event.EventPublisher;
import net.rrm.ehour.ui.common.event.DemoDecorator;
import net.rrm.ehour.ui.common.event.LoadingSpinnerDecorator;
import net.rrm.ehour.ui.common.event.PayloadAjaxEvent;
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
					PayloadAjaxEvent<AdminBackingBean> ajaxEvent = new PayloadAjaxEvent<AdminBackingBean>(submitEventType, backingBean);
					
					EventPublisher.publishAjaxEvent(submitTarget, ajaxEvent);
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
					AjaxEvent errorEvent = new AjaxEvent(errorEventType);
					EventPublisher.publishAjaxEvent(submitTarget, errorEvent);
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
					PayloadAjaxEvent<AdminBackingBean> ajaxEvent = new PayloadAjaxEvent<AdminBackingBean>(deleteEventType, backingBean);
					
					EventPublisher.publishAjaxEvent(submitTarget, ajaxEvent);
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
