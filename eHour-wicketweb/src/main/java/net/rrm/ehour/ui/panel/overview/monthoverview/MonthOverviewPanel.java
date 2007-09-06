/**
 * Created on Sep 6, 2007
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

package net.rrm.ehour.ui.panel.overview.monthoverview;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.ui.border.GreyBlueRoundedBorder;
import net.rrm.ehour.ui.border.GreyRoundedBorder;
import net.rrm.ehour.ui.model.DateModel;
import net.rrm.ehour.ui.session.EhourWebSession;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Month overview panel for consultants
 **/

public class MonthOverviewPanel extends Panel
{
	private static final long serialVersionUID = -8977205040520638758L;

	/**
	 * 
	 * @param id
	 */
	public MonthOverviewPanel(String id, TimesheetOverview timesheetOverview)
	{
		super(id);
		
		setOutputMarkupId(true);
		
		EhourWebSession session = (EhourWebSession)getSession();
		
		GreyRoundedBorder greyBorder = new GreyRoundedBorder("greyFrame", new ResourceModel("monthoverview.overview"));
		GreyBlueRoundedBorder blueBorder = new GreyBlueRoundedBorder("blueFrame");
		
		greyBorder.add(blueBorder);
		add(greyBorder);
		
		addDayLabels(blueBorder, session.getEhourConfig());
	}
	
	/**
	 * Add day labels
	 * @param parent
	 */
	private void addDayLabels(WebMarkupContainer parent, EhourConfig config)
	{
		String[]	days = new String[]{"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
		Calendar cal = new GregorianCalendar();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		
		for (String day : days)
		{
			parent.add(new Label(day, new DateModel(cal, config, DateModel.DATESTYLE_TIMESHEET_DAYONLY)));
			cal.add(Calendar.DAY_OF_WEEK, 1);
		}
	}
}
