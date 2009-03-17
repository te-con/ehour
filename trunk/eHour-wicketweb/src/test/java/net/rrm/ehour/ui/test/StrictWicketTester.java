/**
 * Created on Feb 18, 2009
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

package net.rrm.ehour.ui.test;

import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Component;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormSubmittingComponent;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.protocol.http.MockHttpServletRequest;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;

/**
 * WicketTester with some enhancements: 
 *  - uses a StrictFormTester
 *  - WICKET-254 inclusion
 **/

public class StrictWicketTester extends WicketTester
{
	public StrictWicketTester(WebApplication webApplication)
	{
		super(webApplication);
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.util.getTester().BaseWicketTester#newFormTester(java.lang.String, boolean)
	 */
	@Override
	public FormTester newFormTester(String path, boolean fillBlankString)
	{
		return new StrictFormTester(path, (Form)getComponentFromLastRenderedPage(path), this, fillBlankString);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.util.getTester().BaseWicketTester#executeAjaxEvent(org.apache.wicket.Component, java.lang.String)
	 */
	@Override
	public void executeAjaxEvent(Component component, String event)
	{
		String failMessage = "Can't execute event on a component which is null.";
		notNull(failMessage, component);

		failMessage = "event must not be null";
		notNull(failMessage, event);

		// Run through all the behavior and select the LAST ADDED behavior which
		// matches the event parameter.
		AbstractDefaultAjaxBehavior ajaxEventBehavior = null;
		@SuppressWarnings("unchecked")

		List<IBehavior> behaviors = component.getBehaviors();
		for (IBehavior behavior : behaviors)
		{
			// AjaxEventBehavior is the one to look for
			if (behavior instanceof AjaxEventBehavior)
			{
				AjaxEventBehavior tmp = (AjaxEventBehavior) behavior;

				if (event.equals(tmp.getEvent()))
				{
					ajaxEventBehavior = tmp;
				}
			} else if (behavior instanceof AjaxFormChoiceComponentUpdatingBehavior)
			{
				if (event.equalsIgnoreCase("onclick"))
				{
					ajaxEventBehavior = (AbstractDefaultAjaxBehavior) behavior;
				}
			}
		}

		// If there haven't been found any event behaviors on the component
		// which matches the parameters we fail.
		failMessage = "No AjaxEventBehavior found on component: " + component.getId() + " which matches the event: " + event;
		notNull(failMessage, ajaxEventBehavior);

		// initialize the request only if needed to allow the user to pass
		// request parameters, see
		// WICKET-254
		WebRequestCycle requestCycle;
		if (RequestCycle.get() == null)
		{
			requestCycle = setupRequestAndResponse(true);
		} else
		{
			requestCycle = (WebRequestCycle) RequestCycle.get();
		}
		// when the requestcycle is not created via
		// setupRequestAndResponse(true), it can happen
		// that the request is not an ajax request -> we have to set the header
		// manually
		if (!requestCycle.getWebRequest().isAjax())
		{
			HttpServletRequest req = requestCycle.getWebRequest().getHttpServletRequest();
			if (req instanceof MockHttpServletRequest)
			{
				((MockHttpServletRequest) req).addHeader("Wicket-Ajax", "Yes");
			}
		}

		boolean submit = true;
		if (component instanceof IFormSubmittingComponent)
		{
			submit = ((IFormSubmittingComponent) component).getDefaultFormProcessing();
		}
		// If the event is an FormSubmitBehavior then also "submit" the form
		if (ajaxEventBehavior instanceof AjaxFormSubmitBehavior && submit)
		{
			AjaxFormSubmitBehavior ajaxFormSubmitBehavior = (AjaxFormSubmitBehavior) ajaxEventBehavior;
			submitAjaxFormSubmitBehavior(ajaxFormSubmitBehavior);
		}

		ajaxEventBehavior.onRequest();

		// process the request target
		processRequestCycle(requestCycle);
	}

	private void submitAjaxFormSubmitBehavior(AjaxFormSubmitBehavior behavior)
	{
		// We need to get the form submitted, using reflection.
		// It needs to be "submitted".
		Form form = null;
		try
		{
			Field formField = AjaxFormSubmitBehavior.class.getDeclaredField("form");
			formField.setAccessible(true);
			form = (Form) formField.get(behavior);
		} catch (Exception e)
		{
			fail(e.getMessage());
		}

		String failMessage = "No form attached to the submitlink.";
		notNull(failMessage, form);

		form.visitFormComponents(new FormComponent.AbstractVisitor()
		{
			@Override
			public void onFormComponent(FormComponent formComponent)
			{
				// !(formComponent instanceof Button) &&
				if (!(formComponent instanceof RadioGroup) && !(formComponent instanceof CheckGroup))
				{
					String name = formComponent.getInputName();
					String value = formComponent.getValue();

					// Set request parameter with the field value, but do not
					// modify an existing
					// request parameter explicitly set using
					// FormTester.setValue()
					if (getServletRequest().getParameterMap().get(name) == null)
					{
						getServletRequest().setParameter(name, value);
					}
				}
			}
		});
	}

	private void notNull(String message, Object object)
	{
		if (object == null)
		{
			fail(message);
		}
	}

	private void fail(String message)
	{
		throw new WicketRuntimeException(message);
	}
}
