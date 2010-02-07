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

package net.rrm.ehour.ui.common.component.header;

import java.util.ArrayList;
import java.util.List;

import net.rrm.ehour.ui.admin.config.page.MainConfigPage;
import net.rrm.ehour.ui.admin.content.page.ContentAdminPage;
import net.rrm.ehour.ui.common.component.header.menu.MenuItem;
import net.rrm.ehour.ui.common.component.header.menu.SlideMenu;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.ui.login.page.Login;
import net.rrm.ehour.ui.pm.page.ProjectManagement;
import net.rrm.ehour.ui.report.page.GlobalReportPage;
import net.rrm.ehour.ui.report.user.page.UserReport;
import net.rrm.ehour.ui.timesheet.export.ExportMonthSelectionPage;
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

/**
 * Main navigation panel 
 **/

public class HeaderPanel extends AbstractBasePanel<Void>
{
	private static final long serialVersionUID = 854412484275829659L;

	public HeaderPanel(String id)
	{
		super(id);
	
		add(createNav("nav"));

		add(new BookmarkablePageLink<Login>("logoffLink", Login.class));
		add(addLoggedInUser("prefsLink"));
	}

	private Component createNav(String id)
	{
		List<MenuItem> items = new ArrayList<MenuItem>();
		
		{
			MenuItem item = new MenuItem("nav.hours.yourHours");
			item.addSubMenu(new MenuItem("nav.hours.enter", MonthOverviewPage.class));
			item.addSubMenu(new MenuItem("nav.hours.overview", MonthOverviewPage.class));
			item.addSubMenu(new MenuItem("nav.hours.export", ExportMonthSelectionPage.class));
			items.add(item);
		}		

		{
			MenuItem item = new MenuItem("nav.report");
			item.addSubMenu(new MenuItem("nav.report.userreport", UserReport.class));
			item.addSubMenu(new MenuItem("nav.report.report", GlobalReportPage.class));
			items.add(item);
		}		

		{
			MenuItem item = new MenuItem("nav.pm");
			item.addSubMenu(new MenuItem("nav.pm.report", ProjectManagement.class));
			items.add(item);
		}		

		{
			MenuItem item = new MenuItem("nav.admin");
			item.addSubMenu(new MenuItem("nav.admin.config", MainConfigPage.class));
			item.addSubMenu(new MenuItem("nav.admin.assignment", ContentAdminPage.class));
			items.add(item);
		}		

		
		
		return new SlideMenu(id, items);
	}

	
	private Link<UserPreferencePage> addLoggedInUser(String id)
	{
		BookmarkablePageLink<UserPreferencePage> link = new BookmarkablePageLink<UserPreferencePage>(id, UserPreferencePage.class);
		
		Label loggedInUserLabel = new Label("loggedInUser", new Model<String>(AuthUtil.getUser().getFullName()) );
		link.add(loggedInUserLabel);
		
		return link;
	}
}