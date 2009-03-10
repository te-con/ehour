/**
 * Created on Sep 7, 2007
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

package net.rrm.ehour.ui.timesheet.panel;

import java.util.Calendar;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.timesheet.panel.monthoverview.MonthOverviewPanel;
import net.rrm.ehour.ui.timesheet.panel.projectoverview.ProjectOverviewPanel;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Container for month + project overview 
 **/

public class OverviewPanel extends Panel
{
	private static final long serialVersionUID = 1415235065167294169L;

	@SpringBean
	private TimesheetService	timesheetService;

	/**
	 * 
	 * @param id
	 */
	public OverviewPanel(String id)
	{
		super(id);

		setOutputMarkupId(true);
		EhourWebSession session = ((EhourWebSession)this.getSession());
		User user = session.getUser().getUser();
		
		Calendar overviewFor = session.getNavCalendar();
		
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		
		TimesheetOverview timesheetOverview = timesheetService.getTimesheetOverview(user, overviewFor);
		
		add(new StyleSheetReference("overviewStyle", new CompressedResourceReference(OverviewPanel.class, "style/overview.css")));
		
		add(new ProjectOverviewPanel("projectOverview", overviewFor, timesheetOverview.getProjectStatus()));
		add(new MonthOverviewPanel("monthOverview", timesheetOverview, overviewFor));
	}
}

