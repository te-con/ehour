/**
 * Created on Jul 20, 2007
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

package net.rrm.ehour.ui.panel.entryselector;

import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Selector with autocompletion filter 
 **/

public class EntrySelectorPanel extends Panel
{
	private	String	filterInput;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7928428437664050056L;

	public EntrySelectorPanel(String id, ResourceModel title, String defaultFilterInputText,
							IModel model, ListView itemList)
	{
		super(id, model);
		
		this.filterInput = defaultFilterInputText;
		
		setUpPanel(title, itemList);
	}
	

	/**
	 * Setup page
	 */
	private void setUpPanel(ResourceModel title, ListView itemList)
	{
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("entrySelectorFrame", title);
		
		setUpFilterForm(greyBorder);
		
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueBorder");
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		blueBorder.add(itemList);
	}
	
	/**
	 * 
	 * @param parent
	 */
	private void setUpFilterForm(WebMarkupContainer parent)
	{
		Form	filterForm = new Form("filterForm", new CompoundPropertyModel(this));
		parent.add(filterForm);
		
		TextField	filterInput = new TextField("filterInput");
		filterForm.add(filterInput);
	}


	/**
	 * 
	 * @return
	 */
	public String getFilterInput()
	{
		return filterInput;
	}

	/**
	 * 
	 * @param filterInput
	 */
	public void setFilterInput(String filterInput)
	{
		this.filterInput = filterInput;
	}
}
