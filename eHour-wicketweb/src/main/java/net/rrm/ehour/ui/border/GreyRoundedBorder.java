/**
 * Created on May 28, 2007
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

package net.rrm.ehour.ui.border;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * CSS in ehour.css as it's very common
 **/

public class GreyRoundedBorder extends Border
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7184643596615028876L;

	public GreyRoundedBorder(String id)
	{
		super(id);
		
		Label	label = new Label("greyTabTitle", new Model());
		label.setVisible(false);
		add(label);
	}
	
	/**
	 * 
	 * @param id
	 * @param forceWidth
	 * @param forceHeight
	 */
	public GreyRoundedBorder(String id, Integer forceWidth, Integer forceHeight)
	{
		super(id);
		
		Label	label = new Label("greyTabTitle", new Model());
		label.setVisible(false);
		add(label);
	}		
	
	/**
	 * 
	 * @param id
	 * @param title
	 */
	public GreyRoundedBorder(String id, String title)
	{
		this(id, new Model(title));
	}
	
	public GreyRoundedBorder(String id, IModel title)
	{
		super(id);
		
		Label	label = new Label("greyTabTitle", title);
		add(label);
	}
}
