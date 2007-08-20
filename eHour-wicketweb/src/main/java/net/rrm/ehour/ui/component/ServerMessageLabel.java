/**
 * Created on Aug 21, 2007
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

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.util.time.Duration;

/**
 * ServerMessage label which disappears after 5 seconds and is not visible when no content is provided
 * @author Thies
 *
 */
public class ServerMessageLabel extends Label
{
	private boolean overrideVisibility = false;
	
	public ServerMessageLabel(String id)
	{
		super(id);

		add(new SimpleAttributeModifier("class", "formValidationError"));
		setOutputMarkupId(true);

		add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5))
			{
				@Override
				protected void onPostProcessTarget(AjaxRequestTarget target)
				{
					target.addComponent(ServerMessageLabel.this);
					overrideVisibility = true;
				}
			});		
	}

	public boolean isVisible()
	{
		return overrideVisibility ? false : getModel().getObject()!= null;
	}		
}