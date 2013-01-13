/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.ui.timesheet.panel;

import java.util.Calendar;

import net.rrm.ehour.domain.User;
import net.rrm.ehour.timesheet.dto.TimesheetOverview;
import net.rrm.ehour.timesheet.service.TimesheetService;
import net.rrm.ehour.ui.common.session.EhourWebSession;

import net.rrm.ehour.ui.report.panel.TreeReportDataPanel;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Container for month + project overview 
 **/

public class OverviewPanel extends Panel implements IHeaderContributor
{
	private static final long serialVersionUID = 1415235065167294169L;

	@SpringBean
	private TimesheetService	timesheetService;

	public OverviewPanel(String id)
	{
		super(id);

		setOutputMarkupId(true);
		EhourWebSession session = ((EhourWebSession)this.getSession());
		User user = session.getUser().getUser();
		
		Calendar overviewFor = session.getNavCalendar();
		
		overviewFor.set(Calendar.DAY_OF_MONTH, 1);
		
		TimesheetOverview timesheetOverview = timesheetService.getTimesheetOverview(user, overviewFor);
		
		add(new ProjectOverviewPanel("projectOverview", overviewFor, timesheetOverview.getProjectStatus()));
		add(new MonthOverviewPanel("monthOverview", timesheetOverview, overviewFor));
	}

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);

        response.renderCSSReference(new PackageResourceReference(OverviewPanel.class, "css/overview.css"));
    }



}

