/**
 * Created on Aug 17, 2007
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

package net.rrm.ehour.ui.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Session;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedback;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

/**
 * Displays error indicator when indicated field does not validate
 * 
 * Not extending FormComponentFeedbackIndicator because that sets visible in onBeforeRender
 * 
 **/

public class AjaxFormComponentFeedbackIndicator extends Panel implements IFeedback
{

	private static final long serialVersionUID = 7840885174109746055L;
	private	List<FeedbackMessage>	messages = new ArrayList<FeedbackMessage>();
	private IFeedbackMessageFilter filter;
	
	/**
	 * 
	 * @param id
	 */
	public AjaxFormComponentFeedbackIndicator(String id)
	{
		this(id, null);
	}

	/**
	 * 
	 * @param id
	 * @param indicatorFor
	 */
	public AjaxFormComponentFeedbackIndicator(String id, Component indicatorFor)
	{
		super(id);
		
		setOutputMarkupId(true);
		
		indicatorFor.setOutputMarkupId(true);
		
		if (indicatorFor != null)
		{
			filter = new ComponentFeedbackMessageFilter(indicatorFor);
		}
		
		add(new ErrorIndicator("errorIndicator"));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.validation.FormComponentFeedbackIndicator#onBeforeRender()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void onBeforeRender()
	{
		super.onBeforeRender();
		// Get the messages for the current page
		messages = Session.get().getFeedbackMessages().messages(filter);
	}	

	
	/**
	 * 
	 * @author Thies
	 *
	 */
	@SuppressWarnings("serial")
	private final class ErrorIndicator extends WebMarkupContainer
	{
		private static final long serialVersionUID = 4005048136024661255L;

		public ErrorIndicator(String id)
		{
			super(id);

			add(new Label("errorText", new PropertyModel(this, "message"))
			{
				@Override
				public boolean isVisible()
				{
					return messages != null && messages.size() > 0;
				}
			});
			
			add(new SimpleAttributeModifier("class", "formValidationError"));
		}
		
		public Serializable getMessage()
		{
			return messages != null && messages.size() > 0 ? messages.get(0).getMessage() : "";
		}			
	}
}
