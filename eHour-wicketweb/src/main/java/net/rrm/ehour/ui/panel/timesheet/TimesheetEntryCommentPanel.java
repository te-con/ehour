/**
 * Created on Dec 14, 2007
 * Created by Thies Edeling
 * Created by Thies Edeling
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

package net.rrm.ehour.ui.panel.timesheet;

import net.rrm.ehour.ui.component.KeepAliveTextArea;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Comments panel for timesheet entries
 **/

public class TimesheetEntryCommentPanel extends Panel
{
	private static final long serialVersionUID = -3287327439195576543L;

	/**
	 * 
	 * @param id
	 * @param model
	 */
	public TimesheetEntryCommentPanel(String id, final IModel model)
	{
		super(id);

		TextArea textArea = new KeepAliveTextArea("comment", model);
		add(textArea);
		
		AjaxLink submitButton = new AjaxLink("submit")
		{
			private static final long serialVersionUID = 4796005602570042916L;

			@Override
			public void onClick(AjaxRequestTarget target)
			{
				closeCommentPanel(target);
			}
		};
		
		add(submitButton);
	}
	
	/**
	 * Close parent modal window (if it exists)
	 * @param target
	 */
	private void closeCommentPanel(AjaxRequestTarget target)
	{
		MarkupContainer parent = getParent();
		
		if (parent instanceof ModalWindow)
		{
			((ModalWindow)parent).close(target);
		}
	}
}
