/**
 * Created on Aug 24, 2007
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

package net.rrm.ehour.ui.panel.admin;

import net.rrm.ehour.ui.border.GreyRoundedBorder;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * No entry selected
 **/

public class NoEntrySelectedPanel extends Panel
{
	private static final long serialVersionUID = -4318947090257979895L;

	public NoEntrySelectedPanel(String id)
	{
		super(id);
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("border");
		add(greyBorder);

		greyBorder.add(new Label("noEntry", new ResourceModel("admin.noEditEntrySelected")));
	}
}
