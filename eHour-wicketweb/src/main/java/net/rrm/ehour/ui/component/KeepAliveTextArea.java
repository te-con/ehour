/**
 * Created on Jul 9, 2007
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