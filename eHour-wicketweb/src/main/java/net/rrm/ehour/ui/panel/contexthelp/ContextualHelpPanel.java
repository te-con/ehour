/**
 * Created on Jul 10, 2007
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

package net.rrm.ehour.ui.panel.contexthelp;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Contextual help panel, the one that appears most of the time in the left bottom
 **/

public class ContextualHelpPanel extends Panel
{
	private static final long serialVersionUID = 8054029544833333835L;

	/**
	 * 
	 * @param id
	 */
	
	public ContextualHelpPanel(String id)
	{
		this(id, "help.header", "help.body");
	}	
	
	/**
	 * 
	 * @param id
	 */
	public ContextualHelpPanel(String id, String headerResourceId, String bodyResourceId)
	{
		super(id);
		
		add(new Label("header", new ResourceModel(headerResourceId)));
		add(new Label("body", new ResourceModel(bodyResourceId)));
	}

}
