/**
 * Created on Jul 9, 2007
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

package net.rrm.ehour.ui.common.component;

import org.apache.wicket.ajax.AbstractAjaxTimerBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.time.Duration;

/**
 * KeepAlive text area that pings back to the server every 10 minutes to keep the session alive.
 * http://chillenious.wordpress.com/2007/06/19/how-to-create-a-text-area-with-a-heart-beat-with-wicket/
 */

public class KeepAliveTextArea extends TextArea
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7443361204848769680L;

	public KeepAliveTextArea(String id)
	{
		super(id);
		add(new KeepAliveBehavior());
	}

	public KeepAliveTextArea(String id, IModel model)
	{
		super(id, model);
		add(new KeepAliveBehavior());
	}

	@SuppressWarnings("serial")
	private static class KeepAliveBehavior extends AbstractAjaxTimerBehavior
	{
		public KeepAliveBehavior()
		{
			super(Duration.minutes(10));
		}

		@Override
		protected void onTimer(AjaxRequestTarget target)
		{
			// prevent wicket changing focus
			target.focusComponent(null);
		}
	}
}