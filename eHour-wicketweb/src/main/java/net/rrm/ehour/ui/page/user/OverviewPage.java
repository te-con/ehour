/**
 * Created on May 8, 2007
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

package net.rrm.ehour.ui.page.user;

import java.util.Calendar;
import java.util.GregorianCalendar;

import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.page.BasePage;
import net.rrm.ehour.ui.panel.calendar.CalendarPanel;
import net.rrm.ehour.ui.panel.overview.projectoverview.ProjectOverviewPanel;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.StringValueConversionException;

/**
 * Overview page
 */

@AuthorizeInstantiation("ADMIN")
public class OverviewPage extends BasePage
{
	@SpringBean
	private TimesheetService	timesheetService;

	private static final long serialVersionUID = -6873845464139697303L;

	
	/**
	 * Setup the page
	 *
	 */
	
	public OverviewPage(PageParameters params)
	{
		super("overview", null);

		int userId;
		
		System.out.println(this.getPath());
		
		// add calendar panel
		add(new CalendarPanel("sidePanel"));
		
		// get the data
		try
		{
			userId = params.getInt("userID");
		} catch (StringValueConversionException e)
		{
//			e.printStackTrace();
			userId = 1;
		}
		
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, -14);
		TimesheetOverview timesheetOverview = timesheetService.getTimesheetOverview(userId, cal);
		
		// project overview panel
		add(new ProjectOverviewPanel("projectOverviewPanel", timesheetOverview.getProjectStatus()));
	
	}
}
