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
	private Component				indicatorFor;
	
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
		
		this.indicatorFor = indicatorFor;
		
		indicatorFor.setOutputMarkupId(true);
		
		if (indicatorFor != null)
		{
			filter = new ComponentFeedbackMessageFilter(indicatorFor);
		}
		
		add(new ErrorIndicator("errorIndicator"));
	}
	
	
	/**
	 * @return the indicatorFor
	 */
	public Component getIndicatorFor()
	{
		return indicatorFor;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wicket.markup.html.form.validation.FormComponentFeedbackIndicator#onBeforeRender()
	 */
	@Override
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

			add(new Label("errorText", new PropertyModel<String>(this, "message"))
			{
				@Override
				public boolean isVisible()
				{
					return messages != null && messages.size() > 0;
				}
			});
			
			add(new SimpleAttributeModifier("class", "formValidationError"));
		}
		
		// used by reflection
		@SuppressWarnings("unused")
		public Serializable getMessage()
		{
			return messages != null && messages.size() > 0 ? messages.get(0).getMessage() : "";
		}			
	}
}
