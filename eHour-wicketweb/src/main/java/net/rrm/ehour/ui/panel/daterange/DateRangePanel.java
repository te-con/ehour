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

package net.rrm.ehour.ui.panel.daterange;

import java.util.Date;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.ui.panel.sidepanel.SidePanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.wicketstuff.dojo.markup.html.form.DojoDatePicker;
import org.wicketstuff.dojo.toggle.DojoFadeToggle;

/**
 * Date range selection
 **/

public class DateRangePanel extends SidePanel
{
	private static final long serialVersionUID = -6129389795390181179L;

	private DateRange	dateRangeLimits;
	private	DateRange	selectedDateRange;
	/**
	 * 
	 * @param id
	 * @param model
	 */
	public DateRangePanel(String id, DateRange dateRangeLimits)
	{
		super(id);
		
		this.dateRangeLimits = dateRangeLimits;
		
		selectedDateRange = new DateRange(new Date(), new Date());
		
		Form form = new Form("dateform", new CompoundPropertyModel(selectedDateRange));
		
		DojoDatePicker dateStart = new DojoDatePicker("dateStart", "dd/MM/yyyy");
		dateStart.setToggle(new DojoFadeToggle(200));
		form.add(dateStart);
		
		DojoDatePicker dateEnd = new DojoDatePicker("dateEnd", "dd/MM/yyyy");
		dateEnd.setToggle(new DojoFadeToggle(600));
		form.add(dateEnd);
		
		this.add(new FeedbackPanel("feedback"));

		this.add(form);		
	}

}
