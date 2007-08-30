/**
 * Created on Aug 17, 2007
 * Created by Thies Edeling
 * Copyright (C) 2005, 2006 te-con, All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
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
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackIndicator;
import org.apache.wicket.model.PropertyModel;

/**
 * Displays error indicator when indicated field does not validate
 * 
 **/

public class AjaxFormComponentFeedbackIndicator extends FormComponentFeedbackIndicator
{

	private static final long serialVersionUID = 7840885174109746055L;
	private	List<FeedbackMessage>	messages = new ArrayList<FeedbackMessage>();
	
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
		
		if (indicatorFor != null)
		{
			setIndicatorFor(indicatorFor);
		}
		
		add(new ErrorIndicator("errorIndicator"));
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.validation.FormComponentFeedbackIndicator#updateFeedback()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void updateFeedback()
	{
		messages = Session.get().getFeedbackMessages().messages(getFeedbackMessageFilter());
	}
	
	/**
	 * 
	 * @author Thies
	 *
	 */
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
