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

import net.rrm.ehour.ui.admin.assignment.page.AssignmentAdmin;
import net.rrm.ehour.ui.admin.config.page.MainConfigPage;
import net.rrm.ehour.ui.admin.customer.page.CustomerAdmin;
import net.rrm.ehour.ui.admin.department.page.DepartmentAdmin;
import net.rrm.ehour.ui.admin.export.page.ExportPage;
import net.rrm.ehour.ui.admin.project.page.ProjectAdmin;
import net.rrm.ehour.ui.admin.user.page.UserAdmin;
import net.rrm.ehour.ui.audit.page.AuditReportPage;
import net.rrm.ehour.ui.common.component.header.menu.MenuItem;
import net.rrm.ehour.ui.common.component.header.menu.SlideMenu;
import net.rrm.ehour.ui.common.panel.AbstractBasePanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.util.AuthUtil;
import net.rrm.ehour.ui.login.page.Login;
import net.rrm.ehour.ui.pm.page.ProjectManagement;
import net.rrm.ehour.ui.report.page.GlobalReportPage;
import net.rrm.ehour.ui.timesheet.export.ExportMonthSelectionPage;
import net.rrm.ehour.ui.timesheet.page.MonthOverviewPage;
import net.rrm.ehour.ui.userprefs.page.UserPreferencePage;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main navigation panel
 **/

public class HeaderPanel extends AbstractBasePanel<Void>
{
	private static final long serialVersionUID = 854412484275829659L;

	public HeaderPanel(String id)
	{
		super(id);

		add(new BookmarkablePageLink<Void>("homeLink", AuthUtil.getHomepageForRole(EhourWebSession.getSession().getRoles())));

		add(createNav("nav"));

		add(new BookmarkablePageLink<Login>("logoffLink", Login.class));
		add(addLoggedInUser("prefsLink"));
	}

	private Component createNav(String id)
	{
		List<MenuItem> items = new ArrayList<MenuItem>();

		{
			MenuItem item = new MenuItem("nav.hours.yourHours");

			Map<String,Object> map = new HashMap<String, Object>();
			map.put(MonthOverviewPage.PARAM_OPEN, MonthOverviewPage.OpenPanel.TIMESHEET);

			item.addSubMenu(new MenuItem("nav.hours.enter", MonthOverviewPage.class, new PageParameters(map)));
			item.addSubMenu(new MenuItem("nav.hours.overview", MonthOverviewPage.class));
			item.addSubMenu(new MenuItem("nav.hours.export", ExportMonthSelectionPage.class));
			items.add(item);
		}

		{
			MenuItem item = new MenuItem("nav.report", GlobalReportPage.class);
			items.add(item);
		}

		{
			MenuItem item = new MenuItem("nav.pm");
			item.addSubMenu(new MenuItem("nav.pm.report", ProjectManagement.class));
			items.add(item);
		}

		{
			MenuItem item = new MenuItem("nav.admin.manage");
			item.addSubMenu(new MenuItem("nav.admin.departments", DepartmentAdmin.class));
			item.addSubMenu(new MenuItem("nav.admin.users", UserAdmin.class));
			item.addSubMenu(new MenuItem("nav.admin.customers", CustomerAdmin.class));
			item.addSubMenu(new MenuItem("nav.admin.projects", ProjectAdmin.class));
			item.addSubMenu(new MenuItem("nav.admin.assignments", AssignmentAdmin.class));
			items.add(item);
		}

		{
			MenuItem item = new MenuItem("nav.admin");
			item.addSubMenu(new MenuItem("nav.admin.config", MainConfigPage.class));
			item.addSubMenu(new MenuItem("nav.admin.audit", AuditReportPage.class));
            item.addSubMenu(new MenuItem("nav.admin.export", ExportPage.class));
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